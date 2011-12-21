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
 
 Created on: 11.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing.tool;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.context.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.icons.*;
import org.n52.oxf.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class PanTool extends MapTool {

    private static Logger LOGGER = LoggingHandler.getLogger(PanTool.class);

    private Point2D.Float startPoint = null;

    /**
     * 
     */
    public PanTool(JFrame owner, MapCanvas map) {
        super(owner, map);

        setIcon(new ImageIcon(IconAnchor.class.getResource("pan.gif")));
        setToolTipText("Pan");
    }

    public void activate() {
        super.activate();
    }

    public void mousePressed(MouseEvent evt) {
        panStart(evt);
    }

    public void mouseDragged(MouseEvent evt) {
        panDragged(evt);
    }

    public void mouseReleased(MouseEvent evt) {
        panReleased(evt);
    }

    /**
     * 
     * @param evt
     */
    private void panStart(MouseEvent event) {
        startPoint = new Point2D.Float(event.getX(), event.getY());
    }

    /**
     * 
     * @param evt
     */
    private void panDragged(MouseEvent event) {

        if (startPoint != null) {
            int screenX = event.getX();
            int screenY = event.getY();

            int height = map.getLayerContext().getContextWindow().getHeight();
            int width = map.getLayerContext().getContextWindow().getWidth();

            map.setImageOriginX(map.getImageOriginX() + (screenX - (int) startPoint.x));
            map.setImageOriginY(map.getImageOriginY() + (screenY - (int) startPoint.y));

            startPoint.x = screenX;
            startPoint.y = screenY;

            map.repaint();
        }
    }

    /**
     * 
     * @param evt
     * @throws OXFException
     */
    private void panReleased(MouseEvent event) {

        if (startPoint != null) {
            int height = map.getLayerContext().getContextWindow().getHeight();
            int width = map.getLayerContext().getContextWindow().getWidth();

            ContextBoundingBox cBBox = map.getLayerContext().getContextBoundingBox();

            try {
                Point2D oldOrigin = ContextBoundingBox.screen2Realworld(cBBox.getActualBBox(),
                                                                        width,
                                                                        height,
                                                                        new Point(0, 0));
                Point2D newOrigin = ContextBoundingBox.screen2Realworld(cBBox.getActualBBox(),
                                                                        width,
                                                                        height,
                                                                        new Point(map.getImageOriginX(),
                                                                                  map.getImageOriginY()));

                double xOffset = newOrigin.getX() - oldOrigin.getX();
                double yOffset = newOrigin.getY() - oldOrigin.getY();

                Point2D oldLowerLeft = map.getLayerContext().getContextBoundingBox().getLowerLeft();
                Point2D oldUpperRight = map.getLayerContext().getContextBoundingBox().getUpperRight();

                Point2D.Double newLowerLeft = new Point2D.Double(oldLowerLeft.getX() - xOffset,
                                                                 oldLowerLeft.getY() - yOffset);
                Point2D.Double newUpperRight = new Point2D.Double(oldUpperRight.getX() - xOffset,
                                                                  oldUpperRight.getY() - yOffset);

                map.getLayerContext().getContextBoundingBox().setExtent(newLowerLeft,
                                                                        newUpperRight,
                                                                        false);
            }
            catch (OXFEventException e) {
                LOGGER.error(e, e);
            }
            catch (NoninvertibleTransformException e) {
                LOGGER.error(e, e);
            }

            map.setImageOriginX(0);
            map.setImageOriginY(0);
        }
    }
}