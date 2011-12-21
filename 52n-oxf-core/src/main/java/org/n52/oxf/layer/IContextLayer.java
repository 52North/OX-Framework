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
 
 Created on: 25.01.2006
 *********************************************************************************/

package org.n52.oxf.layer;

import org.n52.oxf.OXFException;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.serialization.IContextSerializableXML;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventException;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public interface IContextLayer extends IEventEmitter, IEventListener, IContextSerializableXML {

    public ParameterContainer getParameterContainer();

    /**
     * sets the actual values of the attributes into the ParameterContainer if they have changed. Returns true
     * if the ParameterContainer has been refreshed (the values have changed) and false otherwise.
     * 
     * @param contextBBox
     * @param window
     * @return
     * @throws OXFException
     */
    public boolean refreshParameterContainer(ContextBoundingBox contextBBox, ContextWindow window) throws OXFException;

    public boolean isHidden();

    /**
     * sets the hidden-status of this Layer. <br>
     * If b differs from the previous value of hidden, a LAYER_VISIBILITY_CHANGED event will be fired.
     * 
     * @param b
     */
    public void setHidden(boolean b) throws OXFEventException;

    /**
     * @return the BBOX associated with this Layer or null if there is none.
     */
    public IBoundingBox getBBox();

    /**
     * @return the SRS associated with this Layer or null if there is none.
     */
    public String getSrs();

    public boolean isBroken();

    public String getIDName();

    public String getDescription();

    public String getTitle();

    public IVisualization getLayerVisualization();

    public IRenderer getRenderer();

    public String getLayerSourceType();

    public String getLayerSourceTitle();

    /**
     * @return the "temporal position" of this Layer. Possibly <code>null</code> if the Layer has no temporal
     *         stamp.
     */
    public ITime getTimeStamp();

}