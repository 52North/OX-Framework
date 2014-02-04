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
package org.n52.oxf.render.jai;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
public class MosaikDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {
    
    private static final long serialVersionUID = 1228091893048693261L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MosaikDescriptor.class);

    private final static String PRODUCT = "52north OX-Framework";

    private final static String[][] resources = {
        {"GlobalName",  "52nMosaik"},
        {"LocalName",   "52nMosaik"},
        {"Vendor",	  	"org.n52"},
        {"Description", "An operation that combines potentially overlapping images respecting transparency information"},
        {"DocURL",	  	"http://www.52north.org"},
        {"Version",	 	"1.0"},
        {"arg0Desc",	"The desired background color of the destination image"},
        {"arg1Desc",    "The (crop) image bounds, or null for the union of all source images"},
        {"arg2Desc",    "Boolean value to indicate whether a new tile layout should be computed (false by default)"}
    };

    private final static String[] supportedModes = {
        "rendered"
    };

    private final static String[] paramNames = {
        "destBGColor", "imageBounds", "computeTileLayout"
    };

    private final static Class[] paramClasses = {
        Color.class, Rectangle.class, Boolean.class
    };

    private final static Object[] paramDefaults = {
        Color.white, null, Boolean.FALSE
    };

    private final static Object[] validParamValues = {
        null, null, null
    };


    private static MosaikDescriptor descriptor;

    public synchronized static void register() {
        try {
            if (descriptor != null) {
                return;
            }
            descriptor = new MosaikDescriptor();
            OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
            or.registerDescriptor(descriptor);
            RIFRegistry.register(or, resources[0][1], PRODUCT, descriptor);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to register descriptor");
        }
    }

    public synchronized static void unregister() {
        try {
            if (descriptor == null) {
                return;
            }
            OperationRegistry or = JAI.getDefaultInstance().getOperationRegistry();
            or.unregisterDescriptor(descriptor);
            RIFRegistry.unregister(or, resources[0][1], PRODUCT, descriptor);
            descriptor = null;
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unable to register descriptor");
        }
    }

    private MosaikDescriptor() {
        super(resources, supportedModes, 0, paramNames, paramClasses, paramDefaults, validParamValues);
    }

    protected boolean validateSources(String modeName, ParameterBlock args, StringBuffer msg) {
        if (! super.validateSources(modeName, args, msg)){
            return false;
        }
        if (args.getNumSources() == 0 && args.getObjectParameter(1) == null) {
            msg.append("For a mosaik without sources, image bounds must be set.");
            return false;
        }
        return true;
    }

    public RenderedImage create(ParameterBlock args, RenderingHints rh) {
        StringBuffer msg = new StringBuffer(100);
        if (! validateArguments("rendered", args, msg)){
            return null;
        }
        return new MosaikOpImage(
            (Color) args.getObjectParameter(0),
            (Rectangle) args.getObjectParameter(1),
            ((Boolean) args.getObjectParameter(2)).booleanValue(),
            args.getSources(),
            rh
        );
    }

}