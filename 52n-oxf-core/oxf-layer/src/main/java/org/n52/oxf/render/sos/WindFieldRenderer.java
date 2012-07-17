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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WindFieldRenderer implements IFeatureDataRenderer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WindFieldRenderer.class);

    private ObservationSeriesCollection obsValues4FOI = null;

    private Set<OXFFeature> selectedFeaturesCache = null;
    
    public AnimatedVisualization renderLayer(OXFFeatureCollection observationCollection,
                                             ParameterContainer paramCon,
                                             int screenW,
                                             int screenH,
                                             IBoundingBox bbox,
                                             Set<OXFFeature> selectedFeatures) throws OXFException {
        if (selectedFeaturesCache == null) {
            selectedFeaturesCache = selectedFeatures;
        }
        
        // is winddirection the first property?:

        ParameterShell observedPropertyPS = paramCon.getParameterShellWithServiceSidedName("observedProperty");
        String[] observedProperties = (String[]) observedPropertyPS.getSpecifiedValueArray();

        // find tuples:
        if (obsValues4FOI == null) {
            obsValues4FOI = new ObservationSeriesCollection(observationCollection,
                                                        selectedFeaturesCache,
                                                        observedProperties,
                                                        true);
        }

        //
        // render Images for each time stamp (frame) and add them to the resultVis:
        //
        AnimatedVisualization resultVis = new AnimatedVisualization();

        ITimePosition[] sortedArray = obsValues4FOI.getSortedTimeArray();
        for (int i = 0; i < sortedArray.length; i++) {
            resultVis.addFrame(renderFrame(sortedArray[i],
                                           screenW,
                                           screenH,
                                           bbox,
                                           selectedFeaturesCache,
                                           obsValues4FOI));
        }

        return resultVis;
    }

    private Image renderFrame(ITimePosition timePos,
                              int screenW,
                              int screenH,
                              IBoundingBox bbox,
                              Set<OXFFeature> selectedFeatures,
                              ObservationSeriesCollection obsValues) {
        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        BufferedImage image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);

        // draw time-string into map:
        g.setColor(Color.BLACK);
        g.drawString(timePos.toString(), 20, 20);

        for (OXFFeature foi : selectedFeatures) {

            //
            // draw the symbols into the image at the georeferenced position of the corresponding feature:
            //
            Point pRealWorld = (Point) foi.getGeometry();

            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                         screenW,
                                                                         screenH,
                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                            pRealWorld.getCoordinate().y));

            ObservedValueTuple tuple = obsValues.getTuple(foi, timePos);

            if (tuple != null) {
                Object value = tuple.getValue(0);
                double windDirection = 0;

                if (value instanceof Double) {
                    windDirection = (Double) value;
                }
                else if (value instanceof String) {
                    windDirection = Double.parseDouble((String) value);
                }
                
                // -> draw wind-vector:

                drawWindVector(g, pScreen, windDirection);
            }
            else {
                LOGGER.warn("no tuple for feature: " + foi + " at time: " + timePos);
            }
        }

        return image;
    }

    private void drawWindVector(Graphics2D g, java.awt.Point p, double direction) {

        double base = 170;
        double tip = 10;
        double length = 8;
        boolean open = false;

        int l = 20;
        direction = direction - 90; // make 0� = North
        double ax = (Math.cos(Math.toRadians(direction)) * l) + p.x;
        double ay = (Math.sin(Math.toRadians(direction)) * l) + p.y;

        Arrow arrow = new Arrow(ax, ay, p.x, p.y, length, tip, base, open);

        g.draw(arrow);
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServiceType() {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getSupportedVersions() {
        // TODO Auto-generated method stub
        return null;
    }

    //
    // Test-environment:
    //

    public static void main(String[] args) {
        new WindFieldRenderer().new TestFrame(0).setVisible(true);
    }

    class TestFrame extends JFrame {
        TestPanel panel;
        double direction;

        public TestFrame(double direction) {
            super("direction: " + direction);
            this.direction = direction;
            panel = new TestPanel();
            setSize(400, 400);
            getContentPane().add(panel);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }

        class TestPanel extends JPanel {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                drawWindVector((Graphics2D) g, new java.awt.Point(100, 100), direction);
            }
        }
    }

}