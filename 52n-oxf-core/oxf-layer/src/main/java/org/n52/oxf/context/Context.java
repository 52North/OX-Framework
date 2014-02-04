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

import java.util.List;

import org.n52.oxf.serialization.WritableContext;
import org.n52.oxf.util.IEventEmitter;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEventSupport;

/**
 * 
 * @see <a href=LayerContext.html>LayerContext</a>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public abstract class Context implements IEventEmitter, IEventListener, WritableContext {

    /**
     * classes which want to listen to this class must be added to this PropertyChangeSupport.
     */
    protected OXFEventSupport eventSupport;

    // --- Context Elements:

    /**
     * The current version of the context according to the WMC Spec of the OGC.
     */
    public static final String CONTEXT_VERSION = "1.1.0";
    
    /**
     * The id attribute should be unique and represent a unique identifier of the Context. This attribute is
     * required
     */
    protected String id;

    /**
     * The element title contains a human readable title of the Context. This element is required
     */
    protected String title;

    /**
     * The element keywordList contains one or more Keyword elements which allow search across context
     * collections. This element is optional.
     */
    protected List<String> keywordList;

    /**
     * The element abstract contains a description for the Context document describing its content. This
     * element is optional.
     */
    protected String abstractDescription;

    public Context(String id, String title) {
        this.id = id;
        this.title = title;
        eventSupport = new OXFEventSupport(this);
    }
    public Context(String id, String title, String abstractDescription, List<String> keywords) {
        this(id, title);
        this.abstractDescription = abstractDescription;
        this.keywordList = keywords;
    }
    public String getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractDescription() {
        return abstractDescription;
    }

    public List<String> getKeywordList() {
        return keywordList;
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
}