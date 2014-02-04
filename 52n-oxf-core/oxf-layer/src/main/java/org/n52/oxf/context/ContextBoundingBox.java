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
package org.n52.oxf.context;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

public class ContextBoundingBox implements IEventEmitter {

    /**
     * Angefragter Ausschnitt wird an die kleinere Ausdehung angepasst; das f�hrt insbesondere beim initialen
     * Request dazu, dass die angefragte BoundingBox immer innerhalb der Gesamt-BoundingBox eines Layers
     * liegt.
     */
    public static final int FIT_MINIMUM = 0;
    public static final int FIT_INTERMEDIATE = 1;
    public static final int FIT_MAXIMUM = 2;

    /**
     * classes which want to listen to this class must be added to this OXFEventSupport.
     */
    private OXFEventSupport eventSupport;

    /**
     * Spatial Reference System of this ContextBoundingBox. E.g.: "EPSG:4326".
     */
    private String srs;

    private Rectangle2D actualBBox;

    /**
     * Constructs a new empty ContextBoundingBox. The values for lowerLeft and upperRight must be setted
     * afterwards.
     */
    public ContextBoundingBox() {
        this("", new Point2D.Double(0, 0), new Point2D.Double(0, 0));
    }

    /**
     * Constructs a new ContextBoundingBox with the specified lowerLeft and upperRight Points.
     * 
     * @param lowerLeft
     *        Point2D witch includes the x- and y-value of the lowerLeft corner.
     * @param upperRight
     *        Point2D witch includes the x- and y-value of the upperRight corner.
     */
    public ContextBoundingBox(Point2D lowerLeft, Point2D upperRight) {
        this("", lowerLeft, upperRight);
    }

    /**
     * ContextBoundingBox with corresponding SRS and Resolution in x resp. y.
     * 
     * @param srs
     *        Spatial Reference System of the ContextBoundingBox
     * @param lowerLeft
     *        Point2D witch includes the x- and y-value of the lowerLeft corner.
     * @param upperRight
     *        Point2D witch includes the x- and y-value of the upperRight corner.
     */
    public ContextBoundingBox(String srs, Point2D lowerLeft, Point2D upperRight) {
        eventSupport = new OXFEventSupport(this);

        this.srs = srs;
        this.actualBBox = buildRectangle(lowerLeft, upperRight);
    }

    /**
     * Constructs a new ContextBoundingBox from a given org.n52.oxf.ows.capabilities.BoundingBox.
     * 
     * @param bBox
     */
    public ContextBoundingBox(IBoundingBox bBox) {
        eventSupport = new OXFEventSupport(this);

        Point2D.Double lowerLeft = new Point2D.Double(bBox.getLowerCorner()[0],
                                                      bBox.getLowerCorner()[1]);
        Point2D.Double upperRight = new Point2D.Double(bBox.getUpperCorner()[0],
                                                       bBox.getUpperCorner()[1]);

        this.srs = bBox.getCRS();
        this.actualBBox = buildRectangle(lowerLeft, upperRight);
    }

    /**
     * 
     * @param lowerLeft
     * @param upperRight
     * @return
     */
    private Rectangle2D.Double buildRectangle(Point2D lowerLeft, Point2D upperRight) {
        double x = lowerLeft.getX();
        double y = lowerLeft.getY(); // TODO: muss das hier nicht upperRight.getY() hei�en??
        double width = upperRight.getX() - lowerLeft.getX();
        double height = upperRight.getY() - lowerLeft.getY();
        return new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * @return this ContextBoundingBox as a org.n52.oxf.ows.capabilities.BoundingBox.
     * @throws OXFException
     */
    public BoundingBox asCommonCapabilitiesBoundingBox() {
        double[] lowerCorner = new double[2];
        lowerCorner[0] = this.actualBBox.getMinX();
        lowerCorner[1] = this.actualBBox.getMinY();

        double[] upperCorner = new double[2];
        upperCorner[0] = this.actualBBox.getMaxX();
        upperCorner[1] = this.actualBBox.getMaxY();

        return new BoundingBox(srs, lowerCorner, upperCorner);
    }

    /**
     * Sets the srs of this BoundingBox. <br>
     * If the srs changes an SRS_CHANGED event will be fired.
     * 
     * @param srs
     * @throws OXFException
     */
    public void setSRS(String srs) throws OXFEventException {
        if (srs != null) {
            if (this.srs == null || (this.srs != null && !this.srs.equalsIgnoreCase(srs))) {
                this.srs = srs;

                eventSupport.fireEvent(EventName.SRS_CHANGED, srs);
            }
        }
    }

    /**
     * Sets the extent and the srs of this ContextBoundingBox. <br>
     * If 'silently' == true this method will NOT fire an EXTENT_CHANGED event but instead an
     * EXTENT_CHANGED_SILENTLY event.
     * 
     * @throws OXFException
     */
    public void setBBox(IBoundingBox bBox, boolean silently) throws OXFEventException {
        Point2D.Double lowerLeft = new Point2D.Double(bBox.getLowerCorner()[0],
                                                      bBox.getLowerCorner()[1]);
        Point2D.Double upperRight = new Point2D.Double(bBox.getUpperCorner()[0],
                                                       bBox.getUpperCorner()[1]);
        this.srs = bBox.getCRS();
        setExtent(lowerLeft, upperRight, silently);
    }

    /**
     * Sets the extent of this ContextBoundingBox. <br>
     * <br>
     * If 'silently' == true this method will NOT fire an EXTENT_CHANGED event but instead an
     * EXTENT_CHANGED_SILENTLY event.
     * 
     * @param lowerLeft
     *        x- and y-value of the lowerleft corner.
     * @param upperRight
     *        x- and y-value of the upperright corner.
     * @throws OXFException
     */
    public void setExtent(Point2D lowerLeft, Point2D upperRight, boolean silently) throws OXFEventException {
        Rectangle2D oldBox = actualBBox;

        this.actualBBox = buildRectangle(lowerLeft, upperRight);

        if ( !actualBBox.getBounds2D().equals(oldBox) && !silently) {
            eventSupport.fireEvent(EventName.EXTENT_CHANGED, null);
        }
        else {
            eventSupport.fireEvent(EventName.EXTENT_CHANGED_SILENTLY, null);
        }
    }

    /**
     * performs a down/up scaling of this ContextBoundingBox. <br>
     * Fires an EXTENT_CHANGED event.
     * 
     * @param newCenter
     *        the new center of the ContextBoundingBox. If center is null, the actual center will be kept.
     * @param factor
     *        if z > 1 performs an in-zooming with the factor z.<br>
     *        if z < 1 performs an out-zooming with the factor 1/z
     * @throws OXFException
     */
    public void zoom(Point2D newCenter, double factor) throws OXFEventException {
        AffineTransform at = new AffineTransform();

        double actualCenterX = actualBBox.getCenterX();
        double actualCenterY = actualBBox.getCenterY();

        if (newCenter == null) {
            newCenter = new Point2D.Double(actualCenterX, actualCenterY);
        }

        double newCenterX = newCenter.getX();
        double newCenterY = newCenter.getY();

        factor = 1 / factor;

        at.translate(newCenterX, newCenterY);
        at.scale(factor, factor);
        at.translate( -actualCenterX, -actualCenterY);

        actualBBox = at.createTransformedShape(actualBBox).getBounds2D();

        eventSupport.fireEvent(EventName.EXTENT_CHANGED, null);
    }

    /**
     * Adjusts the aspect ratio of this BoundingBox to the aspect ratio of the screen. <br>
     * <br>
     * If 'silently' == true this method will NOT fire an EXTENT_CHANGED event but instead an
     * EXTENT_CHANGED_SILENTLY event.
     * 
     * @param screenWidth
     * @param screenHeight
     * @param fitPolicy -
     *        possible values: FIT_MINIMUM (default), FIT_MAXIMUM, FIT_INTERMEDIATE
     * @param silently -
     *        if true an EXTENT_CHANGED_SILENTLY event will be fired instead of an EXTENT_CHANGED event.
     * @throws OXFException
     */
    public void fitBBox2Screen(int screenWidth, int screenHeight, int fitPolicy, boolean silently) throws OXFEventException {
        double aspectRatio = screenWidth / screenHeight;

        double tW = screenWidth * actualBBox.getHeight(); // factorY
        double tH = screenHeight * actualBBox.getWidth(); // factorX
        double factorX = 1 / tH;
        double factorY = 1 / tW;

        AffineTransform at = new AffineTransform();
        at.translate(actualBBox.getX(), actualBBox.getY());

        switch (fitPolicy) {
        case FIT_MINIMUM:
            at.scale(Math.min(tH, tW), Math.min(tH, tW));
            break;
        case FIT_MAXIMUM:
            at.scale(Math.max(tH, tW), Math.max(tH, tW));
            break;
        case FIT_INTERMEDIATE: {
            if (aspectRatio > 1)
                at.scale(tH, tH);
            else
                at.scale(tW, tW);
            break;
        }
        default:
            at.scale(Math.min(tH, tW), Math.min(tH, tW));
            break;
        }

        /*
         * if (aspectRatio > 1) at.scale(tH,tH); else at.scale(tW,tW);
         */

        at.scale(factorX, factorY);
        at.translate( -actualBBox.getX(), -actualBBox.getY());
        actualBBox = at.createTransformedShape(actualBBox).getBounds2D();

        if (silently) {
            eventSupport.fireEvent(EventName.EXTENT_CHANGED_SILENTLY, null);
        }
        else {
            eventSupport.fireEvent(EventName.EXTENT_CHANGED, null);
        }
    }

    /**
     * Adjusts the aspect ratio of this BoundingBox to the aspect ratio of the screen. <br>
     * <br>
     * If 'silently' == true this method will NOT fire an EXTENT_CHANGED event but instead an
     * EXTENT_CHANGED_SILENTLY event.
     * 
     * @param screenWidth
     * @param screenHeight
     * @param silently -
     *        if true an EXTENT_CHANGED_SILENTLY event will be fired instead of an EXTENT_CHANGED event.
     * @throws OXFException
     */
    public void fitBBox2Screen(int screenWidth, int screenHeight, boolean silently) throws OXFEventException {
        fitBBox2Screen(screenWidth, screenHeight, FIT_MINIMUM, silently);
    }

    /**
     * Transforms the screen-coordinates of the specified screenPoint to realworld-coordinates.<br>
     * <br>
     * ATTENTION: The origin of the screen-coordinates is in the upper left corner.
     * 
     * @see org.n52.oxf.context.ContextBoundingBoxTest#testScreen2Realworld()
     * 
     * @param bBox
     * @param screenWidth
     * @param screenHeight
     * @param screenPoint
     *        screen coordinates (origin = upper left corner) !!
     * @return
     * @throws NoninvertibleTransformException
     */
    public static Point2D screen2Realworld(Rectangle2D bBox,
                                           int screenWidth,
                                           int screenHeight,
                                           Point screenPoint) throws NoninvertibleTransformException {

        // computes the screenPoint-y-coordinate to the cs with origin = lowerLeft-corner
        screenPoint.y = screenHeight - screenPoint.y;

        Point2D.Double tPoint = new Point2D.Double();
        AffineTransform at = new AffineTransform();

        double factorX = screenWidth / bBox.getWidth();
        double factorY = screenHeight / bBox.getHeight();

        at.scale(factorX, factorY);
        at.translate( -bBox.getX(), -bBox.getY());

        at.createInverse().transform(screenPoint, tPoint);

        return tPoint;
    }

    /**
     * Transforms the realworld-coordinates of the specified realWorldPoint to screen-coordinates. <br>
     * <br>
     * ATTENTION!: the returned Point has got rounded integer coordinates!<br>
     * <br>
     * ATTENTION: The origin of the returned screen-coordinates is in the upper left corner.
     * 
     * @param screenWidth
     * @param screenHeight
     * @param realWorldPoint
     * @return computed screen coordinates (origin = upper left corner) !!
     */
    public static Point realworld2Screen(Rectangle2D bBox,
                                         int screenWidth,
                                         int screenHeight,
                                         Point2D realWorldPoint) {

        Point2D.Double tPoint = new Point2D.Double();
        AffineTransform at = new AffineTransform();

        double factorX = screenWidth / bBox.getWidth();
        double factorY = screenHeight / bBox.getHeight();

        at.scale(factorX, factorY);
        at.translate( -bBox.getX(), -bBox.getY());

        at.transform(realWorldPoint, tPoint);

        Point screenPoint = new Point();
        screenPoint.setLocation(tPoint.x, tPoint.y);

        // computes the screenPoint-y-coordinate to the cs with origin = upperLeft-corner
        screenPoint.y = screenHeight - screenPoint.y;

        return screenPoint;
    }

    public static Point[] realworld2Screen(Rectangle2D bBox,
                                           int screenWidth,
                                           int screenHeight,
                                           Point2D[] realWorldPoints) {
        Point[] screenPoints = new Point[realWorldPoints.length];
        Point2D realPoint;
        for (int i = 0; i < realWorldPoints.length; i++) {
            realPoint = realWorldPoints[i];
            screenPoints[i] = realworld2Screen(bBox, screenWidth, screenHeight, realPoint);
        }
        return screenPoints;
    }

    // ----- topological operations:

    public void union(Rectangle2D rectangle) {
        Rectangle2D.union(actualBBox, rectangle, actualBBox);
    }

    public void intersect(Rectangle2D rectangle) {
        Rectangle2D.intersect(actualBBox, rectangle, actualBBox);
    }

    public boolean contains(Rectangle2D rectangle) {
        return this.actualBBox.contains(rectangle);
    }

    public boolean isInside(Rectangle2D rectangle) {
        return rectangle.contains(this.actualBBox);
    }

    // ----- getter methods:

    /**
     * @return the lowerleft corner.
     */
    public Point2D getLowerLeft() {
        return new Point2D.Double(actualBBox.getMinX(), actualBBox.getMinY());
    }

    /**
     * @return the upperright corner.
     */
    public Point2D getUpperRight() {
        return new Point2D.Double(actualBBox.getMaxX(), actualBBox.getMaxY());
    }

    /**
     * @return a representation of the BoundingBox as a java.awt.geom.Rectangle2D
     */
    public Rectangle2D getActualBBox() {
        return actualBBox;
    }

    /**
     * @return the Spatial Reference System of this ContextBoundingBox. E.g.: "EPSG:4326".
     */
    public String getSRS() {
        return srs;
    }

    /**
     * @return a String-representation of this ContextBoundingBox. The format is: "minX,minY,maxX,maxY" -
     *         comma-separated;
     */
    public String toString() {
        return "ContextBBox: [" + String.valueOf(actualBBox.getMinX()) + ","
                + String.valueOf(actualBBox.getMinY()) + "," + String.valueOf(actualBBox.getMaxX())
                + "," + String.valueOf(actualBBox.getMaxY()) + "]";
    }

    // ----- OXFEventEmitting:

    /**
     * Add an IEventListener to the OXFEventSupport.
     */
    public void addEventListener(IEventListener listener) {
        eventSupport.addOXFEventListener(listener);
    }

    /**
     * Remove an IEventListener from the OXFEventSupport.
     */
    public void removeEventListener(IEventListener listener) {
        eventSupport.removeOXFEventListener(listener);

    }

}