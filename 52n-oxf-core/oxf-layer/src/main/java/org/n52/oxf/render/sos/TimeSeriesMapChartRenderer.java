/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.render.sos;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.feature.sos.ObservedValueTuple;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TimeSeriesMapChartRenderer implements IFeatureDataRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeriesMapChartRenderer.class);

    protected static final float CHART_WIDTH = 150;
    protected static final float CHART_HEIGHT = 160;

    protected static final int X_SHIFT = -5;
    protected static final int Y_SHIFT = -10;

    private ObservationSeriesCollection obsValues4FOI = null;
    private Map<OXFFeature, Plot> chartCache;

    private Set<OXFFeature> selectedFeaturesCache = null;

    /**
     * 
     * @param featuresWithCharts
     *        the Features of Interest for which a chart shall be renderered.
     */
    public TimeSeriesMapChartRenderer() {
        super();
        chartCache = new HashMap<OXFFeature, Plot>();
    }

    /**
     * @param observationCollection
     * @param screenW
     * @param screenH
     * @param bbox
     * @param selectedFeatures
     *        the Features of Interest for which a chart shall be renderered.
     */
    public StaticVisualization renderLayer(OXFFeatureCollection observationCollection,
                                           ParameterContainer paramCon,
                                           int screenW,
                                           int screenH,
                                           IBoundingBox bbox,
                                           Set<OXFFeature> selectedFeatures) {
        if (selectedFeaturesCache == null) {
            selectedFeaturesCache = selectedFeatures;
        }

        // before starting to render --> run garbageCollection
        Runtime.getRuntime().gc();
        LOGGER.info("Garbage Collection done.");
        // --

        String[] observedProperties;
        // which observedProperty has been used?:
        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            observedProperties = observedPropertyPS.getSpecifiedTypedValueArray(String[].class);
        }
        else if (observedPropertyPS.hasSingleSpecifiedValue()) {
            observedProperties = new String[] {(String) observedPropertyPS.getSpecifiedValue()};
        }
        else {
            throw new IllegalArgumentException("no observedProperties found.");
        }

        // find tuples:
        if (obsValues4FOI == null) {
            obsValues4FOI = new ObservationSeriesCollection(observationCollection,
                                                            selectedFeaturesCache,
                                                            observedProperties,
                                                            true);
        }

        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        BufferedImage image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);

        g.setColor(Color.BLACK);
        
        for (OXFFeature chartFeature : selectedFeaturesCache) {

            //
            // CACHING: create Plot for each "chart feature" and add it to the cache:
            //
            if ( !chartCache.containsKey(chartFeature)) {
                Map<ITimePosition, ObservedValueTuple> timeMap = obsValues4FOI.getAllTuples(chartFeature);

                // draw a chart if there are tuples for the chartFeature available:
                if (timeMap != null) {
                    XYPlot chart = drawChart4FOI(chartFeature.getID(), timeMap);
                    chartCache.put(chartFeature, chart);
                }
            }

            //
            // draw the plots (which are in the cache):
            //
            Point pRealWorld = (Point) chartFeature.getGeometry();

            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                         screenW,
                                                                         screenH,
                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                            pRealWorld.getCoordinate().y));
            XYPlot cachedPlot = (XYPlot) chartCache.get(chartFeature);

            // if there is a plot in the cache for the chartFeature -> draw it:
            if (cachedPlot != null) {
                cachedPlot.getRangeAxis().setRange((Double) obsValues4FOI.getMinimum(0),
                                                   (Double) obsValues4FOI.getMaximum(0));

                cachedPlot.draw(g, new Rectangle2D.Float(pScreen.x + X_SHIFT,
                                                         pScreen.y + Y_SHIFT,
                                                         CHART_WIDTH,
                                                         CHART_HEIGHT), null, null, null);
            }
            else {
                g.drawString("No data available", pScreen.x + X_SHIFT, pScreen.y + Y_SHIFT);
            }
            
            // draw point of feature:
            g.fillOval(pScreen.x - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                       pScreen.y - (FeatureGeometryRenderer.DOT_SIZE_POINT / 2),
                       FeatureGeometryRenderer.DOT_SIZE_POINT,
                       FeatureGeometryRenderer.DOT_SIZE_POINT);

        }

        return new StaticVisualization(image);
    }

    /**
     * The resulting chart consists a TimeSeries for each FeatureOfInterest contained in the
     * observationCollection.
     * 
     * @param foiIdArray
     *        the IDs of the FeaturesOfInterest whose Observations shall be rendered
     * @param observationCollection
     * @return
     */
    protected XYPlot drawChart4FOI(String foiID, Map<ITimePosition, ObservedValueTuple> timeMap) {

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries timeSeries = new TimeSeries(foiID, Second.class);

        for (ITimePosition timePos : timeMap.keySet()) {
            Number value = (Number) timeMap.get(timePos).getValue(0);
            timeSeries.add(new Second(new Float(timePos.getSecond()).intValue(),
                                      timePos.getMinute(),
                                      timePos.getHour(),
                                      timePos.getDay(),
                                      timePos.getMonth(),
                                      new Long(timePos.getYear()).intValue()), value);
        }

        dataset.addSeries(timeSeries);
        dataset.setDomainIsPointsInTime(true);

        //
        // create Plot:
        //

        XYPlot plot = new XYPlot();

        plot.setDataset(dataset);
        plot.setBackgroundPaint(Color.white);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(false);
        plot.setRenderer(renderer);

        DateAxis dateAxis = new DateAxis();
        dateAxis.setTickMarkPosition(DateTickMarkPosition.START);
        dateAxis.setTickMarksVisible(true);
        dateAxis.setVerticalTickLabels(true);
        dateAxis.setDateFormatOverride(new SimpleDateFormat("dd'.'MM'.'"));
        plot.setDomainAxis(dateAxis);

        plot.setRangeAxis(new NumberAxis());

        return plot;
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "TimeSeriesMapChartRenderer - draws small time series charts into a map";
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

}