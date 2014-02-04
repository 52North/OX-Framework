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
package org.n52.oxf.render.wcs;

import java.awt.image.renderable.ParameterBlock;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.render.IRasterDataRenderer;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.SeekableStream;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class WCSRenderer implements IRasterDataRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WCSRenderer.class);
    
    /**
     * this method renders a java.awt.Image which will be used as the graphical representation of a layer. The
     * data for the rendering process will come from the InputStream which is contained in the
     * OperationResult-parameter.
     * 
     * @param operationResult
     * @return
     * @throws OXFException 
     */
    public StaticVisualization renderLayer(OperationResult operationResult) throws OXFException {
        PlanarImage resultImage = null;

        URL sendedRequest = null;
        
        try {
            sendedRequest = new URL(operationResult.getSendedRequest());
        }
        catch (MalformedURLException e) {
            throw new OXFException(e);
        }
        
        ParameterMap paramMap = new ParameterMap(sendedRequest.getQuery());
        String incomingFormat = paramMap.getParameterValue("FORMAT");

        SeekableStream seekableStream = SeekableStream.wrapInputStream(operationResult.getIncomingResultAsStream(), true);
        ParameterBlock pb = new ParameterBlock();
        pb.add(seekableStream);
        
        if (incomingFormat == null){
            LOGGER.warn("parameter 'FORMAT' not specified.");
            return null;
        }
        
        if (incomingFormat.equalsIgnoreCase("geotiff")) {
            resultImage = JAI.create("tiff", pb);
        }
        else if (incomingFormat.equalsIgnoreCase("tiff")) {
            resultImage = JAI.create("tiff", pb);
        }
        else if (incomingFormat.equalsIgnoreCase("png")) {
            resultImage = JAI.create("PNG", pb);
        }
        else if (incomingFormat.equalsIgnoreCase("gif")) {
            resultImage = JAI.create("GIF", pb);
        }
        else {
            throw new OXFException("Incoming format '" + incomingFormat + "' is not supported by this WCSRenderer.");
        }
        
        return new StaticVisualization(resultImage.getAsBufferedImage());
    }

    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "This WCSRenderer renders Coverages from an OGC Web Coverage Service 1.0.0 ...";
    }

    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:WCS" will be returned.
     */
    public String getServiceType() {
        return "OGC:WCS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer. In this case
     *         {"1.0.0"} will be returned.
     */
    public String[] getSupportedVersions() {
        return new String[] {"1.0.0"};
    }
}