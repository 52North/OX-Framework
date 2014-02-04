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

import org.n52.oxf.*;
import org.n52.oxf.ows.*;
import org.n52.oxf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a specialized Thread which can be used to execute the "request - and - render -
 * data"-process. Perfomance will be increased when this <code>LayerProcessor</code> will be started
 * "parallel" for all <code>Layer</code>s.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class LayerProcessor implements IEventEmitter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerProcessor.class);

    /** the associated IContextLayer */
    protected IContextLayer layer;

    protected OXFEventSupport eventSupport;

    /**
     * 
     */
    protected LayerProcessor(IContextLayer layer) {
        super();

        this.eventSupport = new OXFEventSupport(this);
        
        this.layer = layer;
    }

    /**
     * executes the data request and renders the data directly afterwards.
     * 
     */
    public void startProcess() {

        LOGGER.info("Processing layer: '" + layer.getIDName() + "'");

        getProcess().start();
    }

    /**
     * 
     * @return the process which realizes the parallelism of data processing.
     */
    public abstract Thread getProcess();
    

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
     * Returns the layer currently being processed by this layer processor
     * @return org.n52.oxf.layer.IContextLayer
     */
    public IContextLayer getLayer() {
    	return layer;
    }
}