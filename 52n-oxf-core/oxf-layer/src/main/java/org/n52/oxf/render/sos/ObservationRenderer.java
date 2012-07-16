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
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.media.jai.PlanarImage;

import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ObservationRenderer implements IFeatureDataRenderer, IFeaturePicker {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationRenderer.class);

    protected static final int X_SHIFT = -10;
    protected static final int Y_SHIFT = -10;

    protected static final int PICKING_TOLERANCE = 5;

    private static final Color COLOR = Color.RED;
    private static final Color SELECTED_COLOR = Color.RED;
    
    protected Color color;
    protected Color selectedColor;
    
    public ObservationRenderer(Color chosenColor) {
        color = chosenColor;
        selectedColor = SELECTED_COLOR;
    }
    
    public ObservationRenderer() {
        color = COLOR;
        selectedColor = SELECTED_COLOR;
    }

    /**
     * this method renders a StaticVisualization which will be used as the graphical representation of a layer.
     * 
     * @param dataToRender
     *        an OXFFeatureCollection. The contained features should have the FeatureType
     *        <code>OXFAbstractObservationType</code>.
     * @param selectedFeatures
     *        might be <code>null</code> if no features are selected.
     */
    public StaticVisualization renderLayer(OXFFeatureCollection dataToRender,
                                           ParameterContainer paramCon,
                                           int screenW,
                                           int screenH,
                                           IBoundingBox bbox,
                                           Set<OXFFeature> selectedFeatures) {
        PlanarImage resultImage = null;

        //
        // Annahme: alles im 2D:
        //

        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        Image image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);

        
        // draw the foi's of all features(/observations):
        for (OXFFeature feature : dataToRender) {

            if (feature.getFeatureType() instanceof OXFAbstractObservationType) {

                OXFFeature foi = (OXFFeature) feature.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                if (foi != null) {

                    if (foi.getGeometry() != null) {

                        if (foi.getGeometry() instanceof Point) {

                            Point pRealWorld = (Point) foi.getGeometry();

                            LOGGER.info("real world coordinates: ->  X: "
                                    + pRealWorld.getCoordinate().x + " Y: "
                                    + pRealWorld.getCoordinate().y);

                            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                                         screenW,
                                                                                         screenH,
                                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                                            pRealWorld.getCoordinate().y));
                            LOGGER.info("Draw point at screen position: ->  X: " + pScreen.x
                                    + " Y: " + pScreen.y);

                            g.setColor(color);

                            if (selectedFeatures != null && selectedFeatures.contains(feature)) {
                                g.setColor(selectedColor);
                            }

                            // -> draw it:
                            g.fillOval(pScreen.x + X_SHIFT, pScreen.y + Y_SHIFT, 10, 10);
                        }
                    }
                }
            }
        }
        return new StaticVisualization(image);
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
    
    //
    // Implementation of IFeaturePicker:
    //

    /**
     * This method returns a Set of all OXFFeatures of this Layer drawn inside the Screen-BoundingBox
     * specified by the lowerLeft-corner (llX, llY) and the upperRight-corner (urX, urY).
     */
    public Set<OXFFeature> pickFeature(int llX,
                                       int llY,
                                       int urX,
                                       int urY,
                                       int screenW,
                                       int screenH,
                                       OXFFeatureCollection featureCollection,
                                       IBoundingBox bbox) {
        Set<OXFFeature> featureSet = new HashSet<OXFFeature>();

        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);

        for (OXFFeature feature : featureCollection) {

            //
            // pick an Observation
            //
            if (feature.getFeatureType() instanceof OXFAbstractObservationType) {

                OXFFeature foi = (OXFFeature) feature.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                if (foi != null) {

                    if (foi.getGeometry() != null) {

                        // TODO : Spec-Too-Flexible-Problem --> various geometry types are possible:
                        if (foi.getGeometry() instanceof Point) {
                            Point pRealWorld = (Point) foi.getGeometry();

                            java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                                         screenW,
                                                                                         screenH,
                                                                                         new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                                            pRealWorld.getCoordinate().y));

                            java.awt.Point correctedPScreeen = new java.awt.Point(pScreen.x
                                    + X_SHIFT / 2, pScreen.y + Y_SHIFT / 2);

                            double correctedX = correctedPScreeen.getX();
                            double correctedY = correctedPScreeen.getY();

                            if ( (correctedX >= llX && correctedX <= urX) && (correctedY >= llY && correctedY <= urY)) {
                                featureSet.add(feature);
                            }
                        }
                    }
                }
            }
        }

        if (featureSet.size() <= 0) {
            LOGGER.info("No features picked.");
        }

        return featureSet;
    }

    public Set<OXFFeature> pickFeature(int clickedX,
                                       int clickedY,
                                       int screenW,
                                       int screenH,
                                       OXFFeatureCollection featureCollection,
                                       IBoundingBox bbox) {
        
        return pickFeature(clickedX-PICKING_TOLERANCE, clickedY-PICKING_TOLERANCE, clickedX+PICKING_TOLERANCE, clickedY+PICKING_TOLERANCE, screenW, screenH, featureCollection, bbox);
    }
}