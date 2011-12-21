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
 
 Created on: 22.05.2006
 *********************************************************************************/

package org.n52.oxf.render;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.util.LoggingHandler;

import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class AbstractFeaturePicker implements IFeaturePicker {

    private static Logger LOGGER = LoggingHandler.getLogger(AbstractFeaturePicker.class);

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