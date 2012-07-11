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
import java.util.Set;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.time.ITimePosition;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AnimatedMapChartRenderer4OnePhenomenon implements IFeatureDataRenderer {

    private static Logger LOGGER = LoggingHandler.getLogger(AnimatedMapChartRenderer4OnePhenomenon.class);

    protected static final int X_SHIFT = -10;
    protected static final int Y_SHIFT = -10;

    protected static final float CHART_WIDTH = 150;
    protected static final float CHART_HEIGHT = 150;

    private ObservationSeriesCollection tupleFinder;

    /**
     * the Features of Interest for which a chart shall be rendered.
     */
    public Set<OXFFeature> featuresWithCharts;

    /**
     * 
     */
    public AnimatedMapChartRenderer4OnePhenomenon() {
        super();
    }

    /**
     * 
     * @param observationCollection
     * @param paramCon
     * @param screenW
     * @param screenH
     * @param bbox
     * @param featuresWithCharts
     *        the Features of Interest for which a chart shall be renderered.
     * @return
     */
    public AnimatedVisualization renderLayer(OXFFeatureCollection observationCollection,
                                             ParameterContainer paramCon,
                                             int screenW,
                                             int screenH,
                                             IBoundingBox bbox,
                                             Set<OXFFeature> featuresWithCharts) {
        // before starting to render --> run garbageCollection
        Runtime.getRuntime().gc();
        LOGGER.info("Garbage Collection done.");
        // --

        // which observedProperties have been used?:
        String observedProperty;
        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            String[] observedProperties = (String[]) observedPropertyPS.getSpecifiedValueArray();
            observedProperty = observedProperties[0];
        }
        else {
            throw new IllegalArgumentException("No observedProperties specified.");
        }

        // find tuples:
        if (tupleFinder == null) {
            tupleFinder = new ObservationSeriesCollection(observationCollection,
                                                      featuresWithCharts,
                                                      new String[] {observedProperty},
                                                      true);
        }

        //
        // render Images for each time stamp (frame) and add them to the resultVis:
        //
        AnimatedVisualization resultVis = new AnimatedVisualization();

        ITimePosition[] sortedArray = tupleFinder.getSortedTimeArray();
        for (int i = 0; i < sortedArray.length; i++) {
            resultVis.addFrame(renderFrame(sortedArray[i],
                                           screenW,
                                           screenH,
                                           bbox,
                                           featuresWithCharts,
                                           tupleFinder));
        }

        return resultVis;
    }

    /**
     * 
     * @param observationCollection
     *        the observations belonging to the timeStamp (frame) which shall be rendered
     * @param screenW
     * @param screenH
     * @param bbox
     * @return
     */
    protected Image renderFrame(ITimePosition timePos,
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

        for (OXFFeature chartFeature : featuresWithCharts) {

            //
            // create Plot for each "chart feature" and add it to the plotArray:
            //
            CategoryPlot plot = renderFoiBarChart(chartFeature.getID(),
                                                  timePos.toString(),
                                                  tupleFinder.getTuple(chartFeature, timePos));

            //
            // draw the plots into the image at the georeferenced position of the corresponding chartFeature:
            //
            Point pRealWorld = (Point) chartFeature.getGeometry();

            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                         screenW,
                                                                         screenH,
                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                            pRealWorld.getCoordinate().y));

            plot.getRangeAxis(0).setRange((Double) tupleFinder.getMinimum(0),
                                          (Double) tupleFinder.getMaximum(0));

            plot.draw(g, new Rectangle2D.Float(pScreen.x + X_SHIFT + 10,
                                               pScreen.y + Y_SHIFT,
                                               CHART_WIDTH,
                                               CHART_HEIGHT), null, null, null);
        }

        return image;
    }

    /**
     * renders a BarChart consisting of one series for each specified foiID.
     * 
     * @param foiIdArray
     * @param observationCollection
     * @return
     */
    protected CategoryPlot renderFoiBarChart(String foiID,
                                             String timeString,
                                             ObservedValueTuple tuple) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue((Double) tuple.getValue(0), foiID, timeString);

        JFreeChart chart = ChartFactory.createBarChart3D(null, // String title
                                                         null, // String categoryAxisLabel
                                                         null, // String valueAxisLabel
                                                         dataset, // CategoryDataset dataset
                                                         PlotOrientation.VERTICAL, false, // boolean legend
                                                         false, // boolean tooltips
                                                         false // boolean urls
                );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);

        return plot;
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "";
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
}