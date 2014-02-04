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
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.render.coverage.CoverageProcessor;
import org.n52.oxf.render.coverage.DoubleCoverage;
import org.n52.oxf.render.coverage.IntCoverage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Requirements of this renderer:<br> - for a specific FOI should exist only Observations for one
 * TimePosition.
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class InterpolationRenderer implements IFeatureDataRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterpolationRenderer.class);
    
    private static final int NUMBER_OF_CLASSES = 8;
    private static final String COLOR_PALETTE = CoverageProcessor.WHITE_TO_BLUE;

    /**
     * this attribute determines the size of one interpolated surface pixel (in screen pixels). The value
     * strongly affects the speed of interpolation computations.
     */
    private static final int GRANULARITY = 2;

    private static final int LEGEND_WIDTH = 400;
    private static final int LEGEND_HEIGHT = 600;

    private String[] observedProperties;

    private CoverageProcessor cp;

    public StaticVisualization renderLayer(OXFFeatureCollection observationCollection,
                                           ParameterContainer paramCon,
                                           int screenWidth,
                                           int screenHeight,
                                           IBoundingBox bbox,
                                           Set<OXFFeature> notNeeded) {
        // before starting to render --> run garbageCollection
        Runtime.getRuntime().gc();
        LOGGER.info("Garbage Collection done.");

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

        
        // compute the interpolation surface:
        cp = new CoverageProcessor();
        DoubleCoverage coverage = computeInterpolation(observationCollection.toList(),
                                                       screenWidth,
                                                       screenHeight,
                                                       bbox,
                                                       GRANULARITY);
        IntCoverage classifiedCoverage = cp.classifyCoverage(coverage, NUMBER_OF_CLASSES);

        // draw an image of the surface:
        Image coverageImage = cp.createImage(classifiedCoverage, NUMBER_OF_CLASSES, COLOR_PALETTE);
        Image legendImage = renderLegend(coverage.getMinValue(), coverage.getMaxValue());

        return new StaticVisualization(coverageImage, legendImage);
    }

    /**
     * 
     * @param observationList
     * @param numberOfColumns
     * @param numberOfRows
     * @param bBox
     * @param granularity
     *        the number of cells that get the same value
     * @return
     */
    public DoubleCoverage computeInterpolation(List<OXFFeature> observationList,
                                               int numberOfColumns,
                                               int numberOfRows,
                                               IBoundingBox bBox,
                                               int granularity) {
        DoubleCoverage coverage = new DoubleCoverage(bBox, numberOfColumns, numberOfRows);
        double[][] grid = new double[coverage.getNumberOfRows()][coverage.getNumberOfColumns()];
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        double xRasterLength = coverage.getXRasterLength();
        double yRasterLength = coverage.getYRasterLength();

        for (int i = 0; i < coverage.getNumberOfRows(); i = i + granularity) {
            for (int j = 0; j < coverage.getNumberOfColumns(); j = j + granularity) {

                double bBoxLLX = bBox.getLowerCorner()[0];
                double bBoxURY = bBox.getUpperCorner()[1];

                double real_x = bBoxLLX + j * xRasterLength + xRasterLength / 2 * granularity;
                double real_y = bBoxURY - i * yRasterLength - yRasterLength / 2 * granularity;

                Coordinate coordToInterpolate = new Coordinate(real_x, real_y);

                double interpolatedValue = computeInterpolatedValue(coordToInterpolate,
                                                                    observationList);

                for (int k = i; k < i + granularity & k < coverage.getNumberOfRows(); k++) {
                    for (int l = j; l < j + granularity & l < coverage.getNumberOfColumns(); l++) {
                        grid[k][l] = interpolatedValue;
                    }
                }

                if (interpolatedValue < minValue) {
                    minValue = interpolatedValue;
                }
                if (interpolatedValue > maxValue) {
                    maxValue = interpolatedValue;
                }
            }
        }
        coverage.setGrid(grid);
        coverage.setMinValue(minValue);
        coverage.setMaxValue(maxValue);

        return coverage;
    }

    public abstract Double computeInterpolatedValue(Coordinate coordToInterpolate,
                                                    List<OXFFeature> observationList);

    private Image renderLegend(double minValue, double maxValue) {

        double classDistance = (maxValue - minValue) / (NUMBER_OF_CLASSES - 2);

        BufferedImage image = new BufferedImage(LEGEND_WIDTH,
                                                LEGEND_HEIGHT,
                                                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, LEGEND_WIDTH, LEGEND_HEIGHT);

        // draw information:
        String observedProperty = observedProperties[0].split(":")[observedProperties[0].split(":").length - 1];
        g.setColor(Color.BLACK);
        g.drawString("Observed Property:   '" + observedProperty + "'", 25, 35);

        for (int i = 1; i <= NUMBER_OF_CLASSES; i++) {
            // draw text:
            int x_stringLocation = 70;
            int y_stringLocation = 30 + i * 45;
            g.setColor(Color.BLACK);

            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(Locale.GERMAN);
            df.applyPattern("#,###,##0.00");

            double lowerBorder = minValue + classDistance * (i - 1);
            double upperBorder = minValue + classDistance * i;

            g.drawString(i + ". class: " + df.format(lowerBorder) + " - " + df.format(upperBorder),
                         x_stringLocation,
                         y_stringLocation);

            // draw symbol:
            int x_symbolLocation = 25;
            int y_symbolLocation = y_stringLocation - 15;
            g.setColor(new Color(cp.computeCellColor(i - 1, NUMBER_OF_CLASSES, COLOR_PALETTE)));
            g.fillRect(x_symbolLocation, y_symbolLocation, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRect(x_symbolLocation, y_symbolLocation, 20, 20);
        }

        return image;
    }
}