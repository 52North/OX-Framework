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
package org.n52.oxf.render;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

public abstract class AbstractFeaturePicker implements IFeaturePicker {
    
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractFeaturePicker.class);

    protected static final int X_SHIFT = -10;
    protected static final int Y_SHIFT = -10;

    protected static final int PICKING_TOLERANCE = 5;

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
            
            //
            // pick a Feature
            //
            else {

                if (feature.getGeometry() != null) {

                    // TODO : Spec-Too-Flexible-Problem --> various geometry types are possible:
                    if (feature.getGeometry() instanceof Point) {
                        Point pRealWorld = (Point) feature.getGeometry();

                        java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                                     screenW,
                                                                                     screenH,
                                                                                     new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                                        pRealWorld.getCoordinate().y));
                        java.awt.Point correctedPScreeen = new java.awt.Point(pScreen.x + X_SHIFT
                                / 2, pScreen.y + Y_SHIFT / 2);

                        double correctedX = correctedPScreeen.getX();
                        double correctedY = correctedPScreeen.getY();

                        if ( (correctedX >= llX && correctedX <= urX) && (correctedY >= llY && correctedY <= urY)) {
                            featureSet.add(feature);
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