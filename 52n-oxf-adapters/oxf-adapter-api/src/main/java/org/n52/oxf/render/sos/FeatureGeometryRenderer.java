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

import org.apache.log4j.Logger;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.LoggingHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This renderer visualizes the FeatureOfInterest listed up in the Capabilities document.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class FeatureGeometryRenderer implements IFeatureDataRenderer, IFeaturePicker {

    private static Logger LOGGER = LoggingHandler.getLogger(FeatureGeometryRenderer.class);

    protected static final int PICKING_TOLERANCE = 5;

    public static final int DOT_SIZE_POINT = 10;
    public static final int DOT_SIZE_LINE = 2;

    protected static Color COLOR_SELECTED_FEATURES = Color.BLUE;
    protected static Color COLOR_FEATURES = Color.RED;

    //
    // Implementation of IFeatureDataRenderer:
    //

    /**
     * this method renders a StaticVisualization which will be used as the graphical representation of a
     * layer.
     * 
     * @param featuresOfInterest
     *        an OXFFeatureCollection. The contained features should have the FeatureType
     *        <code>OXFFeature</code>.
     * @param selectedFoiSet
     *        might be <code>null</code> if no features are selected.
     */
    public StaticVisualization renderLayer(OXFFeatureCollection featuresOfInterest,
                                           ParameterContainer paramCon,
                                           int screenW,
                                           int screenH,
                                           IBoundingBox bbox,
                                           Set<OXFFeature> selectedFoiSet) {
        PlanarImage resultImage = null;

        Image image = new BufferedImage(screenW, screenH, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        
        // draw white background:
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenW, screenH);
        
        // draw unselected features:
        drawFeatures(featuresOfInterest, screenW, screenH, bbox, g, COLOR_FEATURES);

        // draw selected features:
        if (selectedFoiSet != null && selectedFoiSet.size() > 0) {
            OXFFeatureCollection selectedFeaturesColl = new OXFFeatureCollection("id", null);
            selectedFeaturesColl.add(selectedFoiSet);
            drawFeatures(selectedFeaturesColl, screenW, screenH, bbox, g, COLOR_SELECTED_FEATURES);
        }

        return new StaticVisualization(image);
    }

    public void drawFeatures(OXFFeatureCollection featuresOfInterest,
                              int screenW,
                              int screenH,
                              IBoundingBox bbox,
                              Graphics g,
                              Color c) {
        for (OXFFeature feature : featuresOfInterest) {

            if (feature.getGeometry() != null) {

                Geometry geom = feature.getGeometry();

                // TODO Spec-Too-Flexible-Problem --> various geometry representations are possible:
                if (geom instanceof Point) {
                    drawPoint(bbox, screenW, screenH, (Point) geom, g, c, DOT_SIZE_POINT);
                }
                else if (geom instanceof MultiPoint) {
                    MultiPoint mPoint = (MultiPoint) geom;
                    for (int i = 0; i < mPoint.getNumGeometries(); i++) {
                        drawPoint(bbox, screenW, screenH, (Point) mPoint.getGeometryN(i), g, c, 1);
                    }
                }
                else if (geom instanceof LineString) {
                    drawLineString(bbox, screenW, screenH, (LineString) geom, g, c);
                }
                else if (geom instanceof Polygon) {
                    drawPolygon(bbox, screenW, screenH, (Polygon) geom, g, c);
                }
                else if (geom instanceof MultiPolygon) {
                    MultiPolygon mPolygon = (MultiPolygon) geom;
                    for (int i = 0; i < mPolygon.getNumGeometries(); i++) { 
                        drawPolygon(bbox,
                                    screenW,
                                    screenH,
                                    (Polygon) mPolygon.getGeometryN(i),
                                    g, c);
                    }
                }
                else {
                    throw new IllegalArgumentException("Geometry representation '"
                            + feature.getGeometry().getClass() + "' not supported.");
                }
            }
            else {
                throw new IllegalArgumentException("The feature '" + feature.getID()
                        + "' does not provide a geometry attribute.");
            }
        }
    }

    protected void drawPoint(IBoundingBox bbox,
                           int screenW,
                           int screenH,
                           Point point,
                           Graphics g,
                           Color c,
                           int dotSize) {
        java.awt.Point pScreen = computeScreenPoint(bbox, screenW, screenH, point);

        g.setColor(c);

        // -> draw it:
        g.fillOval(pScreen.x - (dotSize / 2), pScreen.y - (dotSize / 2), dotSize, dotSize);
    }

    private void drawEdge(IBoundingBox bbox,
                          int screenW,
                          int screenH,
                          Point point1,
                          Point point2,
                          Graphics g,
                          Color c) {
        java.awt.Point pScreen1 = computeScreenPoint(bbox, screenW, screenH, point1);
        java.awt.Point pScreen2 = computeScreenPoint(bbox, screenW, screenH, point2);

        g.setColor(c);

        // -> draw it:
        g.drawLine(pScreen1.x, pScreen1.y, pScreen2.x, pScreen2.y);
    }

    private void drawLineString(IBoundingBox bbox,
                                int screenW,
                                int screenH,
                                LineString lineString,
                                Graphics g,
                                Color c) {
        for (int i = 0; i < lineString.getNumPoints() - 1; i++) {
            Point p = lineString.getPointN(i);
            drawPoint(bbox, screenW, screenH, p, g, c, DOT_SIZE_LINE);

            Point pNext = lineString.getPointN(i + 1);
            drawEdge(bbox, screenW, screenH, p, pNext, g, c);
        }
    }

    private void drawPolygon(IBoundingBox bbox,
                             int screenW,
                             int screenH,
                             Polygon polygon,
                             Graphics g,
                             Color c) {

        // draw external ring:
        LineString extRing = polygon.getExteriorRing();
        drawLineString(bbox, screenW, screenH, extRing, g, c);

        // draw connection:
        Coordinate[] realWorldCoords = new Coordinate[] {extRing.getCoordinateN(0),
                                                         extRing.getCoordinateN(extRing.getNumPoints() - 1)};
        LineString lineString = new GeometryFactory().createLineString(realWorldCoords);
        drawLineString(bbox, screenW, screenH, lineString, g, c);
    }

    /**
     * 
     * @param bbox
     * @param screenW
     * @param screenH
     * @param pRealWorld
     * @return
     */
    protected java.awt.Point computeScreenPoint(IBoundingBox bbox,
                                              int screenW,
                                              int screenH,
                                              Point pRealWorld) {
        ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);
        java.awt.Point pScreen = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                     screenW,
                                                                     screenH,
                                                                     new Point2D.Double(pRealWorld.getCoordinate().x,
                                                                                        pRealWorld.getCoordinate().y));
        return pScreen;
    }

    /**
     * supporting method
     * 
     * @param featureSet
     * @param feature
     * @return
     */
    private boolean containsFeature(Set<OXFFeature> featureSet, OXFFeature feature) {
        for (OXFFeature f : featureSet) {
            if (f.equals(feature)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return "This renderer visualizes the FeatureOfInterest listed up in the Capabilities document.";
    }

    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:SOS" will be returned.
     */
    public String getServiceType() {
        return "OGC:SOS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer.
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

        for (OXFFeature feature : featureCollection) {
            if (feature.getGeometry() != null) {

                Envelope realEnv = feature.getGeometry().getEnvelopeInternal();

                ContextBoundingBox contextBBox = new ContextBoundingBox(bbox);
                java.awt.Point screenP1 = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                              screenW,
                                                                              screenH,
                                                                              new Point2D.Double(realEnv.getMinX(),
                                                                                                 realEnv.getMinY()));

                java.awt.Point screenP2 = ContextBoundingBox.realworld2Screen(contextBBox.getActualBBox(),
                                                                              screenW,
                                                                              screenH,
                                                                              new Point2D.Double(realEnv.getMaxX(),
                                                                                                 realEnv.getMaxY()));

                Envelope screenEnv = new Envelope(screenP1.getX(),
                                                  screenP2.getX(),
                                                  screenP1.getY(),
                                                  screenP2.getY());

                if (new Envelope(llX, urX, llY, urY).contains(screenEnv)) { //Arne: maybe "contains" has to be replaced by "intersects" or something else 
                    featureSet.add(feature);
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

        return pickFeature(clickedX - PICKING_TOLERANCE,
                           clickedY - PICKING_TOLERANCE,
                           clickedX + PICKING_TOLERANCE,
                           clickedY + PICKING_TOLERANCE,
                           screenW,
                           screenH,
                           featureCollection,
                           bbox);
    }
}