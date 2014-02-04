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
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.feature.sos.ObservedValueTuple;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;


public class AnimatedMapChartRenderer4OnePhenomenon implements IFeatureDataRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimatedMapChartRenderer4OnePhenomenon.class);
    
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
            String[] observedProperties = observedPropertyPS.getSpecifiedTypedValueArray(String[].class);
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