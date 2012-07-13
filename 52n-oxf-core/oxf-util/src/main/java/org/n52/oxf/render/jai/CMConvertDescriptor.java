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
package org.n52.oxf.render.jai;

import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;

import org.apache.log4j.Logger;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
public class CMConvertDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {

    private final static Logger LOGGER = LoggingHandler.getLogger(CMConvertDescriptor.class);
    
    private final static String PRODUCT = "52north OX-Framework";

    private final static String[][] resources = {
        {"GlobalName",  "52nCMConvert"},
        {"LocalName",   "52nCMConvert"},
        {"Vendor",	  	"org.n52"},
        {"Description", "An operation for color model conversion"},
        {"DocURL",      "http://www.52north.org"},
        {"Version",	 	"1.0"},
        {"arg0Desc",	"Target color model"}
    };

    private final static String[] supportedModes = {
        "rendered"
    };

    private final static String[] paramNames = {
        "targetCM"
    };

    private final static Class[] paramClasses = {
        ColorModel.class
    };

    private final static Object[] paramDefaults = {
        ColorModel.getRGBdefault()
    };

    private final static Object[] validParamValues = {
        null
    };

    private static CMConvertDescriptor descriptor;

    public synchronized static void register() {
        try {
            if (descriptor != null) {
                return;
            }
            descriptor = new CMConvertDescriptor();
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

    private CMConvertDescriptor() {
        super(resources, supportedModes, 1, paramNames, paramClasses, paramDefaults, validParamValues);
    }

    public RenderedImage create(ParameterBlock args, RenderingHints rh) {
        StringBuffer msg = new StringBuffer(100);
        if (! validateArguments("rendered", args, msg)) return null;
        return new CMConvertOpImage((ColorModel) args.getObjectParameter(0), args.getSources(), rh);
    }

}