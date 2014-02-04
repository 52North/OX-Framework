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

import org.n52.oxf.OXFException;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;

/**
 * This class works like a bean to store the actual values for 'height' and 'width' of the viewing area
 * (ContextWindow).
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ContextWindow implements IEventEmitter {

    protected int height;

    protected int width;

    protected OXFEventSupport eventSupport;

    /**
     * 
     */
    public ContextWindow() {
        height = 0;
        width = 0;

        eventSupport = new OXFEventSupport(this);
    }

    /**
     * 
     */
    public ContextWindow(int height, int width) {
        this.height = height;
        this.width = width;

        eventSupport = new OXFEventSupport(this);
    }

    /**
     * sets the height and the width of this ContextWindow. <br>
     * If newHeight or newWidth differs from the previous values of height or width, a WINDOW_SIZE_CHANGED
     * event will be fired.
     * 
     * @param newHeight
     * @param newWidth
     * @throws OXFException
     */
    public void setDimension( int newWidth, int newHeight ) throws OXFEventException {
        int oldHeight = this.height;
        int oldWidth = this.width;

        this.height = newHeight;
        this.width = newWidth;

        if (oldHeight != newHeight || oldWidth != newWidth) {
            // LayerContext will listen to this event:
            eventSupport.fireEvent(EventName.WINDOW_SIZE_CHANGED, null);
        }
    }

    public int getHeight() {
        return height;
    }

    /**
     * sets the height of this ContextWindow. <br>
     * If h differs from the previous value of height, a WINDOW_SIZE_CHANGED event will be fired.
     * 
     * @param height
     * @throws OXFException
     */
    public void setHeight(int h) throws OXFEventException {
        int oldHeight = height;

        height = h;

        if (oldHeight != h) {
            // LayerContext will listen to this event:
            eventSupport.fireEvent(EventName.WINDOW_SIZE_CHANGED, null);
        }
    }

    public int getWidth() {
        return width;
    }

    /**
     * sets the width of this ContextWindow. <br>
     * If w differs from the previous value of width, a WINDOW_SIZE_CHANGED event will be fired.
     * 
     * @param width
     * @throws OXFException
     */
    public void setWidth(int w) throws OXFEventException {
        int oldWidth = width;

        width = w;

        if (oldWidth != w) {
            // LayerContext will listen to this event:
            eventSupport.fireEvent(EventName.WINDOW_SIZE_CHANGED, null);
        }
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
    
    public String toString(){
        return "ContextWindow: [" + getWidth() + ", " + getHeight() + "]";
    }

}