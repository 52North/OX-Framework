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

package org.n52.oxf.ui.wms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

public class GetCapabilitiesHandler {

    private WebMapServiceFrontend wms;

    public GetCapabilitiesHandler(WebMapServiceFrontend wms) {
        super();
        this.wms = wms;
    }

    public void handleGetCapabilitiesRequest(HttpServletRequest request,
                                             HttpServletResponse response) throws IllegalArgumentException,
            ExceptionReport,
            OXFException,
            OXFEventException,
            IOException {

        InputStream in = WebMapServiceFrontend.class.getResourceAsStream("../../../../../capabilities.xml");
        String capsString = IOHelper.readText(in);

        String replacement = "";

        Map<String, WMSLayer> layerMap = wms.getLayerMap();
        for (WMSLayer layer : layerMap.values()) {
            replacement += "<Layer opaque=\"" + layer.getOpaque() + "\">";

            replacement += "<Name>" + layer.getName() + "</Name>";

            replacement += "<Title>" + layer.getTitle() + "</Title>";

            if (layer.getAbstractDescription() != null) {
                replacement += "<Abstract>" + layer.getAbstractDescription() + "</Abstract>";
            }

            for (String srs : layer.getSrsArray()) {
                replacement += "<SRS>" + srs + "</SRS>";
            }

            if (layer.getBoundingBoxArray() != null) {
                for (BoundingBox bbox : layer.getBoundingBoxArray()) {
                    replacement += "<BoundingBox SRS=\"" + bbox.getCRS() + "\"" + " minx=\""
                            + bbox.getLowerCorner()[0] + "\"" + " miny=\""
                            + bbox.getLowerCorner()[1] + "\"" + " maxx=\""
                            + bbox.getUpperCorner()[0] + "\"" + " maxy=\""
                            + bbox.getUpperCorner()[1] + "\" />";
                }
            }

            BoundingBox bbox = layer.getLatLonBoundingBox();
            replacement += "<LatLonBoundingBox minx=\"" + bbox.getLowerCorner()[0] + "\""
                    + " miny=\"" + bbox.getLowerCorner()[1] + "\"" + " maxx=\""
                    + bbox.getUpperCorner()[0] + "\"" + " maxy=\"" + bbox.getUpperCorner()[1]
                    + "\" />";

            replacement += "</Layer>";
        }

        capsString = capsString.replaceAll("XXXX_anchor_XXXX", replacement);
        
        OutputStream out = response.getOutputStream();
        response.setContentType("application/vnd.ogc.wms_xml");
        out.write(capsString.getBytes());
        out.flush();
        out.close();

    }
}