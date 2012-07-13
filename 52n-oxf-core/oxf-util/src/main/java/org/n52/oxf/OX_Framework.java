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

package org.n52.oxf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.n52.oxf.context.Context;
import org.n52.oxf.context.ContextCollection;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.util.LoggingHandler;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class OX_Framework {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final Logger LOGGER = LoggingHandler.getLogger(OX_Framework.class);
    
    private ContextCollection contextCollection = null;
    private OverlayEngine imageBuilder;

    
    public OX_Framework(){
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
    public StringBuffer saveContextCollection() {
        StringBuffer sb = new StringBuffer(200);
        this.getContextCollection().serializeToContext(sb);
        return sb;
    }
    
    /**
     * Saves all Views in a Zip files containing all COntext files and the ContextCollection file.
     * 
     * @param outputStream
     * @return
     */
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
    }
    
    /**
     * saves the Context (downwardly) compatible to the "Web Map Context Documents" Specification (OGC 05-005)
     * of the OGC in version 1.1.0 in to a StringBuffer in form of a XML representation.
     * 
     * @param context the Context which should be saved.
     */
    public StringBuffer saveContext(LayerContext context) {
        StringBuffer sb = new StringBuffer(200);
        context.serializeToContext(sb);
        return sb;
    }

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