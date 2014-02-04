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

import java.util.*;
import org.n52.oxf.serialization.*;

/**
 * Context Collections represent a list of context documents. Context collections could be used in several
 * ways:
 * <li> A particular Viewer Client could use a Collection to construct a menu of default start-up views. </li>
 * <li> A Collection of related contexts could serve as a script for a demonstration. </li>
 * <li> A user could create a Collection to "bookmark" public or user-specific contexts. The creation of such
 * a Collection might be managed by the Viewer Client itself. </li>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ContextCollection implements WritableContext {

    /**
     * The published specification version number contains three positive integers, separated by decimal
     * point, in the form �x.y.z�. Each context collection specification is numbered independently. This
     * attribute is required.
     */
    protected String version;

    
    protected ArrayList<Context> contexts;

    public ContextCollection() {
        contexts = new ArrayList<Context>();
        version = "1.1.0";
    }

    public void addContext(Context context) {
        contexts.add(context);
    }

    public Iterator<Context> getContextIterator() {
        return contexts.iterator();
    }

    /**
     * @param id
     * @return the context with the specified id.
     */
    public Context getContextByID(String id) {
        for (Context c : contexts) {
            if (c.getID().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public List<LayerContext> getLayerContexts() {
        List<LayerContext> res = new ArrayList<LayerContext>();

        for (Context c : contexts) {
            if (c instanceof LayerContext) {
                res.add((LayerContext) c);
            }
        }
        return res;
    }

    public void writeTo(ContextWriter serializer) {
        // TODO has to be implemented
    }
}