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
package org.n52.oxf.render.wms;

import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IRasterDataRenderer;
import org.n52.oxf.render.StaticVisualization;

import com.sun.media.jai.codec.SeekableStream;

public class WMSRenderer implements IRasterDataRenderer {

    private String SERVICE_TYPE = "OGC:WMS";

    public static final String VERSION_100 = "1.0.0";
    public static final String VERSION_110 = "1.1.0";
    public static final String VERSION_111 = "1.1.1";


    public StaticVisualization renderLayer(OperationResult operationResult) throws OXFException {
        PlanarImage resultImage = null;

        String format = (String) operationResult.getUsedParameters().getParameterShellWithCommonName(Parameter.COMMON_NAME_FORMAT).getSpecifiedValue();
        SeekableStream seekableStream = SeekableStream.wrapInputStream(operationResult.getIncomingResultAsStream(),
                                                                       true);
        ParameterBlock pb = new ParameterBlock();
        pb.add(seekableStream);
        if (format.toLowerCase().contains("png")) {
            resultImage = JAI.create("PNG", pb);
        }
        else if (format.toLowerCase().contains("gif")) {
            resultImage = JAI.create("GIF", pb);
        }
        else if (format.toLowerCase().contains("tif")) {
            resultImage = JAI.create("TIFF", pb);
        }
        else if (format.toLowerCase().contains("jpeg") || format.toLowerCase().contains("jpg")) {
            resultImage = JAI.create("JPEG", pb);
        }
        else {
            throw new OXFException("Format '" + format + "' is not supported by this WMSRenderer.");
        }
        return new StaticVisualization(resultImage.getAsBufferedImage());
    }

    public String getDescription() {
        return "Adapter renders GetMapRequests";
    }

    public String getServiceType() {
        return SERVICE_TYPE;
    }

    public String[] getSupportedVersions() {
        return new String[] {VERSION_100, VERSION_110, VERSION_111};
    }

}