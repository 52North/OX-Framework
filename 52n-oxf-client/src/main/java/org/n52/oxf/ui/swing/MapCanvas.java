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

package org.n52.oxf.ui.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.ui.swing.tool.MapTool;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapCanvas extends JPanel implements IEventListener {

    private static final long serialVersionUID = 3654563726177316466L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapCanvas.class);

    private LayerContext layerContext;

    private OverlayEngine imageBuilder;

    protected Image overlayImage = null;

    private MapTool activeTool;

    private int width;
    private int height;

    private Image doubleBufferImage;
    private Graphics doubleBufferGraphics;

    // attributes needed by different Tools:

    private int imageOriginX = 0;
    private int imageOriginY = 0;

    private int rectY1 = -1;
    private int rectX1 = -1;
    private int rectY2 = -1;
    private int rectX2 = -1;

    private Color rectColor = Color.RED;

    /**
     * 
     * @param imageBuilder
     * @param layerContext
     */
    public MapCanvas(OverlayEngine imageBuilder, LayerContext layerContext) {

        this.layerContext = layerContext;
        this.imageBuilder = imageBuilder;

        imageBuilder.addEventListener(this);

        width = super.getWidth();
        height = super.getHeight();

        setBackground(Color.WHITE);
    }

    /**
     * 
     * @param imageBuilder
     */
    public MapCanvas(OverlayEngine imageBuilder) {

        this.layerContext = new LayerContext("01",
                                             "LayerContext 01",
                                             imageBuilder,
                                             new ContextBoundingBox(),
                                             new ContextWindow(super.getHeight(), super.getWidth()));
        this.imageBuilder = imageBuilder;

        imageBuilder.addEventListener(this);

        width = super.getWidth();
        height = super.getHeight();

        setBackground(Color.WHITE);
    }

    /**
     * just one and only one MapTool can be selected per MapCanvas at any point in time. Therefore this method
     * will automatically de-select the previously selected MapTool.
     * 
     * @param tool
     */
    public void setActiveTool(MapTool tool) {
        this.removeMouseListener(activeTool);
        this.removeMouseMotionListener(activeTool);

        activeTool = tool;

        activeTool.setMap(this);
        this.addMouseListener(activeTool);
        this.addMouseMotionListener(activeTool);

        activeTool.activate();
    }

    public void eventCaught(OXFEvent evt) throws OXFEventException {
        LOGGER.info("event caught: " + evt);

        if (evt.getName().equals(EventName.STATIC_OVERLAY_READY)) {
            StaticVisualization visualization = (StaticVisualization) evt.getValue();

            Image image = (visualization != null) ? visualization.getRendering() : null;

            if (image != null) {
                overlayImage = image;
            }
            else {
                overlayImage = null;
            }

            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.WHITE);

        // --- draw OverlayImage:
        if (overlayImage != null) {
            g.drawImage(overlayImage, imageOriginX, imageOriginY, this);
        }
        else {
            g.clearRect(0, 0, width, height);
        }

        // --- draw Rect:
        if (rectX1 != -1 && rectY1 != -1 && rectX2 != -1 && rectY2 != -1) {
            g.setColor(rectColor);

            g.drawLine(rectX1, rectY1, rectX2, rectY1);
            g.drawLine(rectX1, rectY1, rectX1, rectY2);
            g.drawLine(rectX2, rectY2, rectX2, rectY1);
            g.drawLine(rectX2, rectY2, rectX1, rectY2);
        }
        rectX1 = -1;
        rectY1 = -1;
        rectX2 = -1;
        rectY2 = -1;

        // --- react on size-changing:
        try {
            if (width != super.getWidth() | height != super.getHeight()) {
                width = super.getWidth();
                height = super.getHeight();
                layerContext.getContextBoundingBox().fitBBox2Screen(width, height, true);
                layerContext.getContextWindow().setDimension(width, height);
            }
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }
    }

    /**
     * realizes the double-buffering.
     */
    @Override
    public void update(Graphics g) {
        // initialize Double-Buffer:
        if ( (doubleBufferImage == null) || (doubleBufferImage.getHeight(this) != this.getHeight())
                || (doubleBufferImage.getWidth(this)) != this.getWidth()) {
            doubleBufferImage = createImage(this.getSize().width, this.getSize().height);
            doubleBufferGraphics = doubleBufferImage.getGraphics();
        }

        // draw background
        doubleBufferGraphics.setColor(getBackground());
        doubleBufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // draw foreground
        doubleBufferGraphics.setColor(getForeground());
        paint(doubleBufferGraphics);

        // show offscreen
        g.drawImage(doubleBufferImage, 0, 0, this);
    }

    public void drawRect(int x1, int y1, int x2, int y2, Color color) {
        rectX1 = x1;
        rectY1 = y1;
        rectX2 = x2;
        rectY2 = y2;
        rectColor = color;

        repaint();
    }

    public LayerContext getLayerContext() {
        return layerContext;
    }

    public OverlayEngine getImageBuilder() {
        return imageBuilder;
    }

    public int getImageOriginX() {
        return imageOriginX;
    }

    public void setImageOriginX(int imageOriginX) {
        this.imageOriginX = imageOriginX;
    }

    public int getImageOriginY() {
        return imageOriginY;
    }

    public void setImageOriginY(int imageOriginY) {
        this.imageOriginY = imageOriginY;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MapCanvas:\n		width=");
        sb.append(this.width);
        sb.append(", height=");
        sb.append(this.height);
        sb.append(";\n		imageOriginX=");
        sb.append(this.imageOriginX);
        sb.append(", imageOriginY=");
        sb.append(this.imageOriginY);
        sb.append(";\n");
        sb.append(this.layerContext);
        sb.append("\n");
        sb.append(this.imageBuilder);
        sb.append("\n");
        sb.append(this.overlayImage);
        sb.append("\n		activeTool=");
        sb.append(this.activeTool);
        sb.append("\n		image=");
        sb.append(this.doubleBufferImage);
        sb.append("\n		graphics=");
        sb.append(this.doubleBufferGraphics);
        sb.append("\n		rextY1, X1, Y2, X2=");
        sb.append(this.rectX1);
        sb.append(", ");
        sb.append(this.rectY1);
        sb.append(", ");
        sb.append(this.rectX2);
        sb.append(", ");
        sb.append(this.rectY2);
        sb.append(";\n		rectColor=");
        sb.append(this.rectColor);
        return sb.toString();
    }

}