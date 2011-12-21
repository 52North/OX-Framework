/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 15.8.2005
 *********************************************************************************/

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

import org.apache.log4j.Logger;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * @author <a href="mailto:jaeger@52north.org">Andreas Jaeger</a>
 */
public class MosaikDescriptor extends OperationDescriptorImpl implements RenderedImageFactory {

    private final static Logger LOGGER = LoggingHandler.getLogger(MosaikDescriptor.class);
    
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