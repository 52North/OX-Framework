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

package org.n52.oxf.layer;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.util.OXFEventSupport;
import org.n52.oxf.valueDomains.IntegerRangeValueDomain;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLayer implements IContextLayer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLayer.class);

    /**
     * classes which want to listen to this class must be added to this OXFEventSupport.
     */
    protected OXFEventSupport eventSupport;

    /**
     * Holds all <code>ParameterShell</code>s (associating a <code>Parameter</code>s with its value(s))
     * which are necessary for the next data request.
     */
    protected ParameterContainer parameterContainer;

    protected IRenderer renderer;

    /**
     * the "temporal position" of this Layer. Possibly <code>null</code> if the Layer has no temporal stamp.
     */
    protected ITime timeStamp = null;

    /**
     * the result of the rendering process will be stored in this attribute. If the Layer could not be
     * rendered this attribute will be set on <code>null</code>.
     */
    protected IVisualization layerVisualization = null;

    /**
     * status of the layers visibility. true if the layer should be hidden in the client result map.
     */
    protected boolean hidden = false;

    /**
     * indicates whether this Layer could be correctly initialized or not. If the layer data could not be
     * requested sucessfully this attribute will be set on true.
     */
    protected boolean broken = false;

    /**
     * required identification of a layer.
     */
    protected String idName;

    /**
     * a human readable title of the Layer.
     */
    protected String title;

    protected String description;

    /**
     * initializes a new layer.
     * 
     * @param identificationName
     *        the identification of the layer.
     */
    public AbstractLayer(String identificationName,
                         String title,
                         ParameterContainer parameterContainer,
                         IRenderer renderer) {
        eventSupport = new OXFEventSupport(this);

        this.idName = identificationName;
        this.title = title;
        this.parameterContainer = parameterContainer;
        this.renderer = renderer;
    }

    /**
     * sets the actual values of the attributes into the ParameterContainer if they have changed. Returns true
     * if the ParameterContainer has been refreshed (the values have changed) and false otherwise.
     * 
     * @param contextBBox
     * @param window
     * @return
     * @throws OXFException
     */
    public boolean refreshParameterContainer(ContextBoundingBox contextBBox, ContextWindow window) throws OXFException {

        boolean refreshed = false;

        //
        // refresh SRS:
        //
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_SRS)) {
            String actualSRS = contextBBox.getSRS();
            String oldSRS = (String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_SRS).getSpecifiedValue();

            if ( !oldSRS.equals(actualSRS)) {
                parameterContainer.setParameterValue(Parameter.COMMON_NAME_SRS, actualSRS);
                refreshed = true;
            }
        }
        else {// if a Parameter 'SRS' has not yet been setted:
            Parameter srsParam = new Parameter("srs",
                                               false,
                                               new StringValueDomain(contextBBox.getSRS()),
                                               Parameter.COMMON_NAME_SRS);
            parameterContainer.addParameterShell(new ParameterShell(srsParam, contextBBox.getSRS()));
            refreshed = true;
        }

        //
        // refresh BBOX:
        //
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX)) {
            BoundingBox actualBBox = contextBBox.asCommonCapabilitiesBoundingBox();
            BoundingBox oldBBox = (BoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

            if ( !oldBBox.equals(actualBBox)) {
                parameterContainer.setParameterValue(Parameter.COMMON_NAME_BBOX, actualBBox);
                refreshed = true;
            }
        }
        else {// if a Parameter 'BBOX' has not yet been setted:
            Parameter bboxParam = new Parameter("bbox",
                                                false,
                                                contextBBox.asCommonCapabilitiesBoundingBox(),
                                                Parameter.COMMON_NAME_BBOX);
            parameterContainer.addParameterShell(new ParameterShell(bboxParam,
                                                                    contextBBox.asCommonCapabilitiesBoundingBox()));
            refreshed = true;
        }

        //
        // refresh HEIGHT:
        //
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT)) {
            int actualHeight = window.getHeight();
            int oldHeight = Integer.MIN_VALUE;

            if (parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue() != null) {
                oldHeight = (Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue();
            }

            if (oldHeight != actualHeight) {
                parameterContainer.setParameterValue(Parameter.COMMON_NAME_HEIGHT, actualHeight);
                refreshed = true;
            }
        }
        else {// if a Parameter 'HEIGHT' has not yet been setted:
            Parameter heightParam = new Parameter("HEIGHT",
                                                  false,
                                                  new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                  Parameter.COMMON_NAME_HEIGHT);
            parameterContainer.addParameterShell(new ParameterShell(heightParam, window.getHeight()));
            refreshed = true;
        }

        //
        // refresh WIDTH:
        //
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH)) {
            int actualWidth = window.getWidth();
            int oldWidth = -1;

            if (parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue() != null) {
                oldWidth = (Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue();
            }

            if (oldWidth != actualWidth) {
                parameterContainer.setParameterValue(Parameter.COMMON_NAME_WIDTH, window.getWidth());
                refreshed = true;
            }
        }
        else {// if a Parameter 'WIDTH' has not yet been setted:
            Parameter widthParam = new Parameter("WIDTH",
                                                 false,
                                                 new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                 Parameter.COMMON_NAME_WIDTH);
            parameterContainer.addParameterShell(new ParameterShell(widthParam, window.getWidth()));
            refreshed = true;
        }

        return refreshed;
    }

    public boolean isHidden() {
        return hidden;
    }

    /**
     * sets the hidden-status of this Layer. <br>
     * If b differs from the previous value of hidden, a LAYER_VISIBILITY_CHANGED event will be fired.
     * 
     * @param b
     * @throws OXFException
     *         if there occurs an exception during visibility change.
     */
    public void setHidden(boolean b) throws OXFEventException {
        boolean oldHidden = hidden;

        // the new value must be set before the event will be fired.
        hidden = b;

        if (oldHidden != b) {
            eventSupport.fireEvent(EventName.LAYER_VISIBILITY_CHANGED, null);
        }
    }

    public void setBroken(boolean b) {
        broken = b;
    }

    public boolean isBroken() {
        return broken;
    }

    public String getIDName() {
        return idName;
    }
    
    public void setIDName(String idName){
    	this.idName = idName;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setLayerVisualization(IVisualization layerVisualization) {
        this.layerVisualization = layerVisualization;
    }

    public IVisualization getLayerVisualization() {
        return layerVisualization;
    }

    public IRenderer getRenderer() {
        return renderer;
    }

    public ParameterContainer getParameterContainer() {
        return parameterContainer;
    }

    /**
     * convenience method for:
     * <code>getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_TIME.getSpecifiedValue())</code>
     * 
     * @return the "temporal position" of this Layer. Possibly <code>null</code> if the Layer has no
     *         temporal stamp.
     */
    public ITime getTimeStamp() {
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_TIME)) {
            return (ITime) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_TIME).getSpecifiedValue();
        }

        return null;
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

    /**
     * 
     */
    public void eventCaught(OXFEvent event) throws OXFEventException {
        LOGGER.info("event caught: " + event + "  - layerID: " + this.getIDName());

        // NEW_LAYER_ADDED fired by LayerContext:
        //if (event.getName().equals(EventName.NEW_LAYER_ADDED)) {
            //
            // IContextLayer newLayer = (IContextLayer) event.getValue();
            //
            // if (newLayer.equals(this)) {
            // LayerContext emittingLayerContext = (LayerContext) event.getSource();
            //
            // emittingLayerContext.processLayer(this);
            // }
        //}
    }

    /**
     * 
     * @return an LayerProcessor which can be used to execute the "request - and - render - data"-process in a
     *         separate <code>Thread</code>.
     */
    public abstract LayerProcessor getProcessor();
}