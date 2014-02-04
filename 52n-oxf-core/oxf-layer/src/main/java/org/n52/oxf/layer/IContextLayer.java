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
package org.n52.oxf.layer;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventException;

public interface IContextLayer extends IEventEmitter, IEventListener {

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