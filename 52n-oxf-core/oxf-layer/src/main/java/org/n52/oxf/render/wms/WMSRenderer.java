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