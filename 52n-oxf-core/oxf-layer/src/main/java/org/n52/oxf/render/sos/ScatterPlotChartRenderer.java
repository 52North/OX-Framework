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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.ui.RectangleEdge;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.feature.sos.ObservedValueTuple;
import org.n52.oxf.render.IChartRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.render.StaticVisualization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ScatterPlotChartRenderer implements IChartRenderer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScatterPlotChartRenderer.class);

    private String observedPropertyX;
    private String observedPropertyY;

    /**
     * The resulting IVisualization shows a scatterplot.
     * 
     * Auf X- und Y-Achse wird jeweils eine observedProperty abgetragen.
     * 
     * Ein Punkt steht dann f�r ein Wertepaar (X,Y) f�r ein FOI zu einem bestimmten Zeitpunkt.
     * 
     * TODO PROBLEM: Macht nur Sinn, wenn f�r jedes (FOI, Time)-Tuple jeweils ein Wert f�r BEIDE
     * observedPropertys enthalten ist !!!!
     */
    public IVisualization renderChart(OXFFeatureCollection observationCollection,
                                      ParameterContainer paramCon,
                                      int width,
                                      int height) {
        JFreeChart chart = renderChart(observationCollection, paramCon);

        Plot plot = chart.getPlot();

        // draw plot into image:

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        plot.draw(g, new Rectangle2D.Float(0, 0, width, height), null, null, null);

        return new StaticVisualization(image);
    }

    public JFreeChart renderChart(OXFFeatureCollection observationCollection,
                                  ParameterContainer paramCon) {
        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            String[] observedProperties =observedPropertyPS.getSpecifiedTypedValueArray(String[].class);;
            observedPropertyX = observedProperties[0];
            observedPropertyY = observedProperties[1];
        }
        else {
            throw new IllegalArgumentException("2 observedProperties needed.");
        }

        String[] foiIdArray = paramCon.getParameterShellWithServiceSidedName("featureOfInterest").getSpecifiedTypedValueArray(String[].class);

        ObservationSeriesCollection tupleFinder = new ObservationSeriesCollection(observationCollection,
                                                                          foiIdArray,
                                                                          new String[] {observedPropertyX,
                                                                                        observedPropertyY},
                                                                          true);

        return drawChart(tupleFinder.getAllTuples());
    }

    /**
     * 
     * @param dataset
     * @param width
     * @param height
     * @return
     */
    private JFreeChart drawChart(List<ObservedValueTuple> tupleList) {

        float[][] data = new float[2][tupleList.size()];
        for (int i = 0; i < tupleList.size(); i++) {
            data[0][i] = ((Double) tupleList.get(i).getValue(0)).floatValue();
            data[1][i] = ((Double) tupleList.get(i).getValue(1)).floatValue();
        }

        String xAxisTitle = observedPropertyX.split(":")[observedPropertyX.split(":").length - 1];
        String yAxisTitle = observedPropertyY.split(":")[observedPropertyY.split(":").length - 1];

        final NumberAxis domainAxis = new NumberAxis(yAxisTitle);
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis(xAxisTitle);
        rangeAxis.setAutoRangeIncludesZero(false);

        MyScatterPlot plot = new MyScatterPlot(data, domainAxis, rangeAxis);

        return new JFreeChart(plot);
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "ScatterPlotChartRenderer - visualizes a scatterplot chart for 2 selected phenomena";
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


class MyScatterPlot extends FastScatterPlot {

    public static final int dotSize = 4;

    public MyScatterPlot(float[][] data, ValueAxis domainAxis, ValueAxis rangeAxis) {
        super(data, domainAxis, rangeAxis);
    }

    @Override
    public void render(Graphics2D g2,
                       Rectangle2D dataArea,
                       PlotRenderingInfo info,
                       CrosshairState crosshairState) {
        float[][] data = getData();

        g2.setPaint(Color.red);

        if (super.getData() != null) {
            for (int i = 0; i < data[0].length; i++) {
                float x = data[0][i];
                float y = data[1][i];

                int transX = (int) getDomainAxis().valueToJava2D(x, dataArea, RectangleEdge.BOTTOM);
                int transY = (int) getRangeAxis().valueToJava2D(y, dataArea, RectangleEdge.LEFT);
                g2.fillRect(transX, transY, dotSize, dotSize);
            }
        }
    }

}