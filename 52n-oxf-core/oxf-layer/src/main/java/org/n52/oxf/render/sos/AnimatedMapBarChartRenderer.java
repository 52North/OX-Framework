/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.oxf.render.sos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.feature.sos.ObservedValueTuple;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

public class AnimatedMapBarChartRenderer implements IFeatureDataRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimatedMapBarChartRenderer.class);
    
    protected static final int X_SHIFT = -10;
    protected static final int Y_SHIFT = -10;

    protected static final float CHART_WIDTH = 60;
    protected static final float CHART_WIDTH_MULTIPLICATOR = 80; // + 100 per observedProperty
    protected static final float CHART_HEIGHT = 150;

    protected static final String[] SUPPORTED_RESULT_TYPES = new String[] {"Measurement","Observation"}; 
    
    private ChartCache chartCache = null;
    private ObservationSeriesCollection obsValues4FOI = null;
    private String[] observedProperties;

    private Set<OXFFeature> featuresWithCharts = null;
    
    /**
     * 
     * @param featuresWithCharts
     *        the Features of Interest for which a chart shall be renderered.
     */
    public AnimatedMapBarChartRenderer() {
        super();
        chartCache = new ChartCache();
    }

    /**
     * Requirements:<br>
     * 1. Observations are of type OXFMeasurementType. <br>
     * 2. Number of observedProperties: min=1 max=n <br>
     * 3. To allow "caching": observationCollection is the same between different renderLayer()-executions.
     * 
     * @param observationCollection
     *        contains the Observations
     * @param paramCon
     * @param screenW
     * @param screenH
     * @param bbox
     * @param featuresWithCharts
     *        the Features of Interest for which a chart shall be rendered.
     * @return
     */
    public AnimatedVisualization renderLayer(OXFFeatureCollection observationCollection,
                                             ParameterContainer paramCon,
                                             int screenW,
                                             int screenH,
                                             IBoundingBox bbox,
                                             Set<OXFFeature> selectedFeatures) throws OXFException {
        if (featuresWithCharts == null) {
            featuresWithCharts = selectedFeatures;
        }
        
        // before starting to render --> run garbageCollection
        Runtime.getRuntime().gc();
        LOGGER.info("Garbage Collection done.");
        // --

        // correct resultModel used?:
        String resultModel = (String)paramCon.getParameterShellWithServiceSidedName("resultModel").getSpecifiedValue();
        if ( ! isResultModelSupported(resultModel)) {
            throw new OXFException("Renderer does not support the specified resultModel '" + resultModel + "'");
        }
        
        // which observedProperties have been used?:
        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
           observedProperties = (String[]) observedPropertyPS.getSpecifiedValueArray();
            //observedProperties = new String[] {"urn:ogc:def:phenomenon:OGC:1.0.30:dryBulbTemp", "urn:ogc:def:phenomenon:OGC:1.0.30:barometricPressure"]};
        }
        else {
            throw new IllegalArgumentException("no observedProperties found.");
        }

        // find tuples:
        if (obsValues4FOI == null) {
            obsValues4FOI = new ObservationSeriesCollection(observationCollection,
                                                      featuresWithCharts,
                                                      observedProperties,
                                                      true);
        }

        //
        // render Images for each time stamp (frame) and add them to the resultVis:
        //
        AnimatedVisualization resultVis = new AnimatedVisualization();

        ITimePosition[] sortedArray = obsValues4FOI.getSortedTimeArray();
        for (int i = 0; i < sortedArray.length; i++) {
            resultVis.addFrame(renderFrame(sortedArray,
                                           i,
                                           screenW,
                                           screenH,
                                           bbox,
                                           featuresWithCharts,
                                           obsValues4FOI));
        }

        return resultVis;
    }

    protected Image renderFrame(ITimePosition[] sortedTimeArray,
                                int currentTimeIndex,
                                int screenW,
                                int screenH,
                                IBoundingBox bbox,
                                Set<OXFFeature> featuresWithCharts,
                                ObservationSeriesCollection tupleFinder) {
        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        BufferedImage image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);
        
        g.setColor(Color.BLACK);
        
        ITimePosition currentTimePos = sortedTimeArray[currentTimeIndex];

        for (OXFFeature chartFeature : featuresWithCharts) {

            //
            // create Plot for each "chart feature" and add it to the cache:
            //
            if ( !chartCache.contains(currentTimePos, chartFeature)) {
                
                // if there is a data tuple for the current time position -> create a new plot
                if (tupleFinder.getTuple(chartFeature, currentTimePos) != null) {
                    CategoryPlot plot = drawChart4FOI(chartFeature.getID(),
                                                      currentTimePos.toString(),
                                                      tupleFinder.getTuple(chartFeature, currentTimePos));
                    chartCache.add(currentTimePos, chartFeature, plot);
                }
            }

            CategoryPlot plot = (CategoryPlot) chartCache.get(currentTimePos, chartFeature);

            // if there wasn't a plot for the current time position go backwards through the sortedTimeArray and take the most recent one:
            int j=currentTimeIndex - 1;
            while (plot == null && j>=0) {
                plot = (CategoryPlot) chartCache.get(sortedTimeArray[j], chartFeature);
                j--;
            }
            
            //
            // draw the plots into the image at the georeferenced position of the corresponding chartFeature:
            //
            Point pRealWorld = (Point) chartFeature.getGeometry();

            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                         screenW,
                                                                         screenH,
                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                            pRealWorld.getCoordinate().y));
            // if an appropriate plot was found -> draw it
            if (plot != null) {
                for (int i = 0; i < observedProperties.length; i++) {
                    plot.getRangeAxis(i).setRange((Double) tupleFinder.getMinimum(i) - 5,
                                                  (Double) tupleFinder.getMaximum(i));
                    if (i > 0) {
                        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
                    }
                }
    
                plot.draw(g, new Rectangle2D.Float(pScreen.x + X_SHIFT + 10,
                                                   pScreen.y + Y_SHIFT,
                                                   CHART_WIDTH + observedProperties.length
                                                           * CHART_WIDTH_MULTIPLICATOR,
                                                   CHART_HEIGHT), null, null, null);
            }
            // otherwise draw "no data available"
            else {
                g.drawString("No data available", pScreen.x + X_SHIFT, pScreen.y + Y_SHIFT);
            }
            
            // draw point of feature:
            g.fillOval(pScreen.x - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                       pScreen.y - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                       FeatureGeometryRenderer.DOT_SIZE_POINT,
                       FeatureGeometryRenderer.DOT_SIZE_POINT);
        }

        return image;
    }

    protected CategoryPlot drawChart4FOI(String featureID,
                                         String timeString,
                                         ObservedValueTuple tuple) {

        BarRenderer3D barRenderer = new BarRenderer3D();

        CategoryPlot plot = new CategoryPlot();
        plot.setDomainAxis(new CategoryAxis(featureID));

        for (int i = 0; i < observedProperties.length; i++) {
            plot.setDataset(i, createDataset(i, timeString, tuple));
            plot.setRangeAxis(i,
                              new NumberAxis(tuple.getPhenomenonNames()[i].split(":")[tuple.getPhenomenonNames()[i].split(":").length - 1]));
            plot.setRenderer(i, barRenderer);
            plot.mapDatasetToRangeAxis(i, i);
        }

        return plot;
    }

    private CategoryDataset createDataset(int indexOfProperty,
                                          String timeString,
                                          ObservedValueTuple tuple) {
        String category = timeString;

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int j = 0; j < observedProperties.length; j++) {
            if (j == indexOfProperty) {
                dataset.addValue((Number) tuple.getValue(indexOfProperty), "Series " + j, category);
            }
            else {
                dataset.addValue(null, "Series " + j, category);
            }
        }
        return dataset;
    }
    
    private boolean isResultModelSupported(String specifiedResultModel) {
        for (String supportedResultModel : SUPPORTED_RESULT_TYPES) {
            if (specifiedResultModel.equals(supportedResultModel)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "AnimatedMapBarChartRenderer - visualizes a temporal animation of bar charts into a map";
    }
    
    public String toString() {
        return getDescription();
    }

    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:SOS" will be returned.
     */
    public String getServiceType() {
        return "OGC:SOS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer. In this case
     *         {"1.0.0"} will be returned.
     */
    public String[] getSupportedVersions() {
        return new String[] {"0.0.0"};
    }

    private class ChartCache {

        private Map<ITimePosition, Map<OXFFeature, Plot>> timePosMap;

        public ChartCache() {
            super();
            timePosMap = new HashMap<ITimePosition, Map<OXFFeature, Plot>>();
        }

        public void add(ITimePosition timePos, OXFFeature feature, CategoryPlot plot) {
            Map<OXFFeature, Plot> featureMap = timePosMap.get(timePos);
            if (featureMap == null) {
                featureMap = new HashMap<OXFFeature, Plot>();
                timePosMap.put(timePos, featureMap);
            }
            featureMap.put(feature, plot);
        }

        public Plot get(ITimePosition timePos, OXFFeature feature) {
            Map<OXFFeature, Plot> featureMap = timePosMap.get(timePos);
            if (featureMap != null) {
                return featureMap.get(feature);
            }
            else {
                return null;
            }
        }

        public boolean contains(ITimePosition timePos, OXFFeature feature) {
            Map<OXFFeature, Plot> featureMap = timePosMap.get(timePos);
            if (featureMap != null) {
                return featureMap.containsKey(feature);
            }
            else {
                return false;
            }
        }

    }
}