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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.feature.sos.ObservedValueTuple;
import org.n52.oxf.render.IChartRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.valueDomains.time.ITimePosition;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TimeSeriesChartRenderer4xPhenomenons implements IChartRenderer {

    private String[] observedProperties;

    /**
     * The resulting IVisualization shows a chart which consists a TimeSeries for each FeatureOfInterest
     * contained in the observationCollection.
     */
    public IVisualization renderChart(OXFFeatureCollection observationCollection,
                                      ParameterContainer paramCon,
                                      int width,
                                      int height) {
        JFreeChart chart = renderChart(observationCollection, paramCon);

        // draw plot into image:
        Plot plot = chart.getPlot();
        BufferedImage chartImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D chartGraphics = chartImage.createGraphics();
        chartGraphics.setColor(Color.white);
        chartGraphics.fillRect(0, 0, width, height);
        plot.draw(chartGraphics, new Rectangle2D.Float(0, 0, width, height), null, null, null);

        // draw legend into image:
        LegendTitle legend = chart.getLegend();
        BufferedImage legendImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D legendGraphics = legendImage.createGraphics();
        legendGraphics.setColor(Color.white);
        legendGraphics.fillRect(0, 0, (int) legend.getWidth(), (int) legend.getHeight());
        legend.draw(legendGraphics, new Rectangle2D.Float(0, 0, width, height));

        return new StaticVisualization(chartImage, legendImage);
    }

    public JFreeChart renderChart(OXFFeatureCollection observationCollection,
                                  ParameterContainer paramCon) {
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

        String[] foiIdArray;
        ParameterShell foiParamShell = paramCon.getParameterShellWithServiceSidedName("featureOfInterest");
        if (foiParamShell.hasMultipleSpecifiedValues()) {
            foiIdArray = foiParamShell.getSpecifiedTypedValueArray(String[].class);
        }
        else {
            foiIdArray = new String[]{ (String)foiParamShell.getSpecifiedValue()};
        }
        
        ObservationSeriesCollection tuples4FOI = new ObservationSeriesCollection(observationCollection,
                                                                                 foiIdArray,
                                                                                 observedProperties,
                                                                                 false);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(null, // title
                                                              "Date", // x-axis label
                                                              observedProperties[0], // y-axis label
                                                              createDataset(foiIdArray,
                                                                            tuples4FOI,
                                                                            0), // data
                                                              true, // create legend?
                                                              true, // generate tooltips?
                                                              false // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(2.0, 2.0, 2.0, 2.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);

        // add additional datasets:

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat());

        for (int i = 1; i < observedProperties.length; i++) {
            XYDataset additionalDataset = createDataset(foiIdArray, tuples4FOI, i);
            plot.setDataset(i, additionalDataset);
            plot.setRangeAxis(i, new NumberAxis(observedProperties[i]));
            plot.getRangeAxis(i).setRange((Double) tuples4FOI.getMinimum(i),
                                          (Double) tuples4FOI.getMaximum(i));
            plot.mapDatasetToRangeAxis(i, i);
        }

        return chart;
    }

    protected TimeSeriesCollection createDataset(String[] foiIdArray,
                                                 ObservationSeriesCollection tuples4FOI,
                                                 int observedPropertyIndex) {
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        for (String featureID : foiIdArray) {
            Map<ITimePosition, ObservedValueTuple> tupleMap = tuples4FOI.getAllTuples(featureID);

            if (tupleMap != null) {

                // for each selected <feature,observedPropertyIndex> pair construct a new TimeSeries:
                TimeSeries timeSeries = new TimeSeries(featureID + "___"
                        + this.observedProperties[observedPropertyIndex], Second.class);

                for (ITimePosition timePos : tupleMap.keySet()) {
                    ObservedValueTuple tuple = tupleMap.get(timePos);

                    Number result = (Number) tuple.getValue(observedPropertyIndex);

                    timeSeries.add(new Second(new Float(timePos.getSecond()).intValue(),
                                              timePos.getMinute(),
                                              timePos.getHour(),
                                              timePos.getDay(),
                                              timePos.getMonth(),
                                              new Long(timePos.getYear()).intValue()), result);
                }
                dataset.addSeries(timeSeries);
            }
        }
        dataset.setDomainIsPointsInTime(true);

        return dataset;
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "TimeSeriesChartRenderer - visualizes a time series chart for a selected phenomenon";
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