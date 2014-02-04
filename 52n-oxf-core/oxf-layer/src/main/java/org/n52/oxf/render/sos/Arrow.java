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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Arrow implements Shape {

    private double points[];
    private double length;
    private double tip;
    private double base;
    private boolean open;
    
    private ArrowPathIterator pathIterator;

    public Arrow(Point2D start, Point2D end, double length, double tip, double base, boolean open) {
        this(start.getX(), start.getY(), end.getX(), end.getY(), length, tip, base, open);
    }
    
    public Arrow(double xStart, double yStart, double xEnd, double yEnd, double length, double tip, double base, boolean open) {
        points = new double[2*5];
        
        // P0
        points[0] = xStart;        
        points[1] = yStart;        
        
        // P3
        points[2*3] = xEnd;        
        points[2*3+1] = yEnd;        
        
        this.length = Math.min(length, Point2D.distance(points[0], points[1], points[2*3], points[2*3+1]));
        if (this.length <= 0) 
            throw new IllegalArgumentException(getClass()+": length less or equal to 0");
            
        if ((tip <= 0) || (tip >= 90)) 
            throw new IllegalArgumentException(getClass()+": tip angle not in range <0..90>");
        this.tip = Math.toRadians(tip);
        
        if ((base <= tip) || (base >= 180)) 
            throw new IllegalArgumentException(getClass()+": base angle not in range <"+tip+"..180>");
        this.base = Math.toRadians(base);
        
        this.open = open;
        
        pathIterator = new ArrowPathIterator(points);
        
        // P1 = P3 - length*alpha
        double alpha = Math.atan2(points[2*3+1] - points[1], points[2*3] - points[0]);
        points[1*2]   = points[2*3]   - length*Math.cos(alpha);
        points[1*2+1] = points[2*3+1] - length*Math.sin(alpha);
        
        // SL (sideLength) = length*cos(tip) + length * sin(tip) * cos(base-tip) / sin(base-tip) 
        double SL = length*Math.cos(tip) + length*Math.sin(tip) * Math.cos(base-tip) / Math.sin(base-tip);
        
        // P2 = P3 - SL*alpha+tip
        points[2*2]   = points[2*3]   - SL*Math.cos(alpha+tip);
        points[2*2+1] = points[2*3+1] - SL*Math.sin(alpha+tip);
        
        // P4 = P3 - SL*alpha-tip
        points[2*4]   = points[2*3]   - SL*Math.cos(alpha-tip);
        points[2*4+1] = points[2*3+1] - SL*Math.sin(alpha-tip);
    }

    public boolean contains(double x, double y) {
        return new Area(this).contains(x, y);
    }

    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && contains(x+w,y) &&
               contains(x,y+h) && contains(x+w,y+h);
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /*** Returns true, if at least one of the points is contained by the shape. */
    public boolean intersects(double x, double y, double w, double h) {
        return contains(x, y) || contains(x+w,y) ||
               contains(x,y+h) || contains(x+w,y+h);
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }

    public Rectangle2D getBounds2D() {
        return new Area(this).getBounds2D();
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public PathIterator getPathIterator(AffineTransform t) {
        if (t != null) {
            t.transform(points, 0, pathIterator.points, 0, points.length/2);
        } 

        pathIterator.reset();
        return pathIterator;
    }

    public String toString() {
        return getClass()+": ("+points[0]+","+points[1]+")-("+points[2*3]+","+points[2*3+1]+") length="+length+"; "
              +"tip="+Math.toDegrees(tip)+"; base="+Math.toDegrees(base)+"; open="+open+";";
    }
    
    /***
     * Returns a straight line shape, followed by the arrowhead.
     */
    private class ArrowPathIterator implements PathIterator {

        private double[] points;
        private int currentPoint = 0;

        private ArrowPathIterator(double[] points) {
            this.points = points;
        }

        public boolean isDone() {
            int nPoints = open ? points.length/2 : points.length/2+2;
            return currentPoint >= nPoints;
        }

        public void next() {
            currentPoint++;
        }

        public int currentSegment(double[] coords) {
            switch (currentPoint) {
                default:
                    coords[0] = points[2*currentPoint];
                    coords[1] = points[2*currentPoint+1];
                    return ((currentPoint == 0) || (currentPoint == 2)) ? SEG_MOVETO : SEG_LINETO;
                case 5:
                    coords[0] = points[2];
                    coords[1] = points[3];                
                    return SEG_LINETO;
                case 6:
                    return SEG_CLOSE;
            }
        }

        public int currentSegment(float[] coords) {
            switch (currentPoint) {
                default:
                    coords[0] = (float)points[2*currentPoint];
                    coords[1] = (float)points[2*currentPoint+1];
                    return ((currentPoint == 0) || (currentPoint == 2)) ? SEG_MOVETO : SEG_LINETO;
                case 5:
                    coords[0] = (float)points[2];
                    coords[1] = (float)points[3];                
                    return SEG_LINETO;
                case 6:
                    return SEG_CLOSE;
            }
        }

        public int getWindingRule() {
            return PathIterator.WIND_NON_ZERO;
        }

        private void reset() {
            currentPoint = 0;
        }

        private void done() {
            currentPoint = open ? points.length/2 : points.length/2+2;
        }
    }
}