/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 01.07.2006
 *********************************************************************************/

package org.n52.oxf.render.sos;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.SOSObservationStore;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.render.coverage.CoverageProcessor;
import org.n52.oxf.render.coverage.DoubleCoverage;
import org.n52.oxf.render.coverage.IntCoverage;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.LoggingHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Requirements of this renderer:<br> - for a specific FOI should exist only Observations for one
 * TimePosition.
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class InterpolationRenderer implements IFeatureDataRenderer {

    private static Logger LOGGER = LoggingHandler.getLogger(InterpolationRenderer.class);

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
            observedProperties = (String[]) observedPropertyPS.getSpecifiedValueArray();
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

    //
    // -----------------------------------------------------------
    //

    public static void main(String[] args) throws OXFException, IOException {
        SOSObservationStore featureStore = new SOSObservationStore();

        InputStream in = new FileInputStream("C:/temp/sos_getObs_response2.xml");

        OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(new OperationResult(in,
                                                                                                    null,
                                                                                                    null));

        int screenW = 400;
        int screenH = 300;

        // --- compute the whole Envelope of all foi's of the features(/observations):
        Geometry envelopeGeom = null;
        for (OXFFeature feature : featureCollection) {

            if (feature.getFeatureType() instanceof OXFAbstractObservationType) {

                OXFFeature foi = (OXFFeature) feature.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                if (foi != null) {

                    if (foi.getBoundingBox() != null) {
                        if (envelopeGeom != null) {
                            envelopeGeom = envelopeGeom.union(foi.getBoundingBox());
                        }
                        else {
                            envelopeGeom = foi.getBoundingBox();
                        }
                    }
                }
            }
        }

        Envelope envelope = envelopeGeom.getEnvelopeInternal();

        Point2D.Double lowerCorner = new Point2D.Double(envelope.getMinX() - 1500,
                                                        envelope.getMinY() - 1500);
        Point2D.Double upperCorner = new Point2D.Double(envelope.getMaxX() + 1500,
                                                        envelope.getMaxY() + 1500);

        IBoundingBox bbox = new ContextBoundingBox(lowerCorner, upperCorner).asCommonCapabilitiesBoundingBox();
        // ---

        // NNRenderer renderer = new NNRenderer();
        IDWRenderer renderer = new IDWRenderer();

        final Image image = renderer.renderLayer(featureCollection,
                                                 null,
                                                 screenW,
                                                 screenH,
                                                 bbox,
                                                 null).getRendering();

        JFrame frame = new JFrame();
        Canvas canvas = new Canvas() {
            public void paint(Graphics g) {
                g.drawImage(image, 0, 0, this);
            }
        };
        canvas.setSize(screenW, screenH);

        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.getContentPane().setBackground(Color.MAGENTA);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}