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

package org.n52.oxf.render.sosMobile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.OXFSensorType;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.render.sos.FeatureGeometryRenderer;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.LoggingHandler;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * A simple renderer that shows a sensor's position as a colored point according to its state
 * (active, mobile) and some textual information.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class SensorPositionRenderer extends FeatureGeometryRenderer {

    private static final Font STANDART_FONT = new Font("Arial", Font.PLAIN, 12);

    private static Logger LOGGER = LoggingHandler.getLogger(SensorPositionRenderer.class);

    /**
	 * 
	 */
    public SensorPositionRenderer() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.render.sos.FeatureGeometryRenderer#getDescription()
     */
    @Override
    public String getDescription() {
        return "This renderer visualizes the given sensor.";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.render.sos.FeatureGeometryRenderer#getServiceType()
     */
    @Override
    public String getServiceType() {
        return "SOS";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.render.sos.FeatureGeometryRenderer#getSupportedVersions()
     */
    @Override
    public String[] getSupportedVersions() {
        return new String[] {"1.0.0"};
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.n52.oxf.render.sos.FeatureGeometryRenderer#drawFeatures(org.n52.oxf.feature.OXFFeatureCollection,
     * int, int, org.n52.oxf.owsCommon.capabilities.IBoundingBox, java.awt.Graphics, java.awt.Color)
     */
    @Override
    public void drawFeatures(OXFFeatureCollection featuresOfInterest,
                             int screenW,
                             int screenH,
                             IBoundingBox bbox,
                             Graphics g,
                             Color c) {
        // super.drawFeatures(featuresOfInterest, screenW, screenH, bbox, g, c);

        // draw sensor positions
        for (OXFFeature feature : featuresOfInterest) {

            if (feature.getGeometry() != null) {

                Geometry geom = feature.getGeometry();

                if (geom instanceof Point) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("drawing Sensor at " + geom);
                    }
                    Color color = switchColor(feature, c);

                    drawPoint(bbox, screenW, screenH, (Point) geom, g, color, DOT_SIZE_POINT);

                    String text = feature.getID().substring(feature.getID().lastIndexOf(":") + 1,
                                                            feature.getID().length());

                    drawText(bbox, screenW, screenH, (Point) geom, g, Color.BLACK, STANDART_FONT, text);
                }
                else {
                    throw new IllegalArgumentException("Geometry representation '" + feature.getGeometry().getClass()
                            + "' not supported.");
                }
            }
            else {
                throw new IllegalArgumentException("The feature '" + feature.getID()
                        + "' does not provide a geometry attribute.");
            }
        }
    }

    /**
     * 
     * @param bbox
     * @param screenW
     * @param screenH
     * @param point
     * @param g
     * @param color
     * @param font
     * @param text
     */
    private void drawText(IBoundingBox bbox,
                          int screenW,
                          int screenH,
                          Point point,
                          Graphics g,
                          Color color,
                          Font font,
                          String text) {
        java.awt.Point pScreen = computeScreenPoint(bbox, screenW, screenH, point);
        g.setColor(color);
        g.setFont(font);
        // -> draw it:
        g.drawString(text, pScreen.x + 8, pScreen.y + 2);
    }

    /**
     * active, mobile = green
     * 
     * active, immobile = yellow
     * 
     * inactive, mobile = orange
     * 
     * inactive, not mobile = red
     * 
     * if no cases match, the given default color is returned.
     * 
     * @param feature
     * @param defaultColor
     * @return
     */
    private Color switchColor(OXFFeature feature, Color defaultColor) {
        boolean mobile = ((Boolean) feature.getAttribute(OXFSensorType.MOBILE)).booleanValue();
        boolean active = ((Boolean) feature.getAttribute(OXFSensorType.ACTIVE)).booleanValue();

        Color color;
        if (active && mobile) {
            color = Color.GREEN;
        }
        else if (active && !mobile) {
            color = Color.YELLOW;
        }
        else if ( !active && mobile) {
            color = Color.ORANGE;
        }
        else if ( !active && !mobile) {
            color = Color.RED;
        }
        else {
            color = defaultColor;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("switching color for " + feature + " to " + color + "because a=" + active + " m=" + mobile);
        }

        return color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.n52.oxf.render.sos.FeatureGeometryRenderer#renderLayer(org.n52.oxf.feature.OXFFeatureCollection,
     * org.n52.oxf.serviceAdapters.ParameterContainer, int, int,
     * org.n52.oxf.owsCommon.capabilities.IBoundingBox, java.util.Set)
     */
    @Override
    public StaticVisualization renderLayer(OXFFeatureCollection features,
                                           ParameterContainer paramCon,
                                           int screenW,
                                           int screenH,
                                           IBoundingBox bbox,
                                           Set<OXFFeature> selectedFoiSet) {
        StaticVisualization vis = super.renderLayer(features, paramCon, screenW, screenH, bbox, selectedFoiSet);
        return vis;
    }
}
