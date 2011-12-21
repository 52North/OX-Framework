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
 
 Created on: 05.08.2005
 *********************************************************************************/

package org.n52.oxf.context;

import java.math.BigInteger;

import net.opengis.context.WindowType;

import org.n52.oxf.OXFException;
import org.n52.oxf.serialization.IContextSerializableXML;
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
public class ContextWindow implements IEventEmitter, IContextSerializableXML {

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

    public void serializeToContext(StringBuffer sb) {
        
        WindowType xb_window = WindowType.Factory.newInstance();
        xb_window.setHeight(new BigInteger(""+getHeight()));
        xb_window.setWidth(new BigInteger(""+getWidth()));
        
        sb.append(xb_window.toString());
    }
}