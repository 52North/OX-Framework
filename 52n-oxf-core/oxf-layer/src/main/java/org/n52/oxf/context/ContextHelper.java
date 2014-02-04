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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.n52.oxf.OXFException;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.serialization.ContextWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextHelper.class);
    
    private static final String DEFAULT_ENCODING = "UTF-8";
    
    private ContextCollection contextCollection = null;
    
    private OverlayEngine imageBuilder;
    
    public ContextHelper() {
        imageBuilder = new OverlayEngine();
    }
    
    /**
     * loads a ContextCollection from a specified URL.
     * 
     * @param url
     *        the URL where to find the ContextCollection document.
     */
    public void loadContextCollection(String url) {

    }

    /**
     * saves all Views in a ContextCollection.
     */
    public void saveContextCollection(ContextWriter serializer) {
        serializer.write(getContextCollection());
    }
    
    /**
     * Saves all Views in a Zip files containing all COntext files and the ContextCollection file.
     
    public ZipOutputStream saveAllViews(OutputStream outputStream) throws OXFException {
        
        ZipOutputStream out = new ZipOutputStream(outputStream);
        StringBuffer collectionSb = new StringBuffer();
        contextCollection.serializeToContext(collectionSb);
        try {
            // Writing the Collection to the ZipStream
            String collectionString = collectionSb.toString();
            out.putNextEntry(new ZipEntry("collection.xml"));
            out.write(collectionString.getBytes(DEFAULT_ENCODING));
            // Writing the Contexts to the ZipStream
            for(Iterator<Context> iter = contextCollection.getContextIterator(); iter.hasNext();) {
                Context ctx = iter.next();
                out.putNextEntry(new ZipEntry(ctx.getID() + ".xml"));
                StringBuffer sb = new StringBuffer(200);
                ctx.serializeToContext(sb);
                out.write(sb.toString().getBytes(DEFAULT_ENCODING));
            }
        }
        catch(UnsupportedEncodingException e) {
            LOGGER.warn("Encoding not supported: "+ DEFAULT_ENCODING, e);
        }
        catch(IOException e) {
            LOGGER.warn("Error while writing ZipFile ", e);
            throw new OXFException("Error while writing ZipFile");
        }
        return out;
    }*/
    
    /**
     * If the ContextCollection has not been loaded a new (fresh) ContextCollection will be returned. Else the
     * loaded ContextCollection will be returned.
     */
    public ContextCollection getContextCollection() {
        if (contextCollection != null) {
            return contextCollection;
        }
        else {
            // create new fresh ContextCollection and return it:
            return new ContextCollection();
        }
    }
    
    public OverlayEngine getImageBuilder() {
        return imageBuilder;
    }
}