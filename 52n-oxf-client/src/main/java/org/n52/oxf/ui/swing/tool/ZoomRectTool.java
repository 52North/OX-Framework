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
 
 Created on: 29.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing.tool;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import org.apache.log4j.*;
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
public class ZoomRectTool extends MapTool {

    private static Logger LOGGER = LoggingHandler.getLogger(ZoomRectTool.class);

    private Point startPoint = null;

    /**
     * @param map
     */
    public ZoomRectTool(JFrame owner, MapCanvas map) {
        super(owner, map);

        setIcon(new ImageIcon(IconAnchor.class.getResource("zoom_rect.gif")));
        setToolTipText("Zoom Rect");
    }

    public void activate() {
        super.activate();
    }

    public void mousePressed(MouseEvent evt) {
        rectStart(evt);
    }

    public void mouseDragged(MouseEvent evt) {
        rectDragged(evt);
    }

    public void mouseReleased(MouseEvent evt) {
        rectReleased(evt);
    }

    private void rectStart(MouseEvent event) {
        startPoint = new Point(event.getX(), event.getY());
    }

    private void rectDragged(MouseEvent event) {
        map.drawRect(startPoint.x, startPoint.y, event.getX(), event.getY(), Color.red);
    }

    private void rectReleased(MouseEvent event) {
        int height = map.getLayerContext().getContextWindow().getHeight();
        int width = map.getLayerContext().getContextWindow().getWidth();

        Point endPoint = new Point(event.getX(), event.getY());

        if (startPoint.x != endPoint.x && startPoint.y != endPoint.y) {

            ContextBoundingBox cBBox = map.getLayerContext().getContextBoundingBox();

            try {
                Point2D realWorldStartPoint = ContextBoundingBox.screen2Realworld(cBBox.getActualBBox(),
                                                                                  width,
                                                                                  height,
                                                                                  startPoint);
                Point2D realWorldEndPoint = ContextBoundingBox.screen2Realworld(cBBox.getActualBBox(),
                                                                                width,
                                                                                height,
                                                                                endPoint);

                Point2D lowerLeft = new Point2D.Double(Math.min(realWorldStartPoint.getX(),
                                                                realWorldEndPoint.getX()),
                                                       Math.min(realWorldStartPoint.getY(),
                                                                realWorldEndPoint.getY()));
                Point2D upperRight = new Point2D.Double(Math.max(realWorldStartPoint.getX(),
                                                                 realWorldEndPoint.getX()),
                                                        Math.max(realWorldStartPoint.getY(),
                                                                 realWorldEndPoint.getY()));

                map.getLayerContext().getContextBoundingBox().setExtent(lowerLeft, upperRight, true);
                map.getLayerContext().getContextBoundingBox().fitBBox2Screen(width,
                                                                             height,
                                                                             ContextBoundingBox.FIT_MAXIMUM,
                                                                             false);
            }
            catch (OXFEventException e) {
                LOGGER.error(e, e);
            }
            catch (NoninvertibleTransformException e) {
                LOGGER.error(e, e);
            }
        }
    }
}