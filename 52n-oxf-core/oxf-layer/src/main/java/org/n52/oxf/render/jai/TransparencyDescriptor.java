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
public class TransparencyDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {

    private static final long serialVersionUID = -6610750834705668590L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TransparencyDescriptor.class);

    private final static String PRODUCT = "52north OX-Framework";

    private final static String[][] resources = {
        {"GlobalName",  "52nTransparency"},
        {"LocalName",   "52nTransparency"},
        {"Vendor",	  	"org.52n"},
        {"Description", "An operation that adds transparency to images with opaque color models"},
        {"DocURL",	  	"http://www.52north.org"},
        {"Version",	 	"1.0"},
        {"arg0Desc",	"Implied transparent color"}
    };

    private final static String[] supportedModes = {
        "rendered"
    };

    private final static String[] paramNames = {
        "srcTransColor"
    };

    private final static Class[] paramClasses = {
        Color.class
    };

    private final static Object[] paramDefaults = {
        Color.white
    };

    private final static Object[] validParamValues = {
        null
    };

    private static TransparencyDescriptor descriptor;

    public synchronized static void register() {
        try {
            if (descriptor != null) {
                return;
            }
            descriptor = new TransparencyDescriptor();
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

    private TransparencyDescriptor() {
        super(resources, supportedModes, 1, paramNames, paramClasses, paramDefaults, validParamValues);
    }

    public RenderedImage create(ParameterBlock args, RenderingHints rh) {
        StringBuffer msg = new StringBuffer(100);
        if (! validateArguments("rendered", args, msg)) {
            return null;
        }
        return new TransparencyOpImage((Color) args.getObjectParameter(0), args.getSources(), rh);
    }

}