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

package org.n52.oxf.serviceAdapters.wms;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.DCP;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.n52.wmsclientcore.request.GetCapabilitiesRequest;
import org.n52.wmsclientcore.request.GetCapabilitiesRequest100;
import org.n52.wmsclientcore.request.GetCapabilitiesRequest110;
import org.n52.wmsclientcore.request.GetCapabilitiesRequest111;
import org.n52.wmsclientcore.request.GetMapRequest;
import org.n52.wmsclientcore.request.GetMapRequest100;
import org.n52.wmsclientcore.request.GetMapRequest110;
import org.n52.wmsclientcore.request.GetMapRequest111;
import org.n52.wmsclientcore.request.RequestBoundingBox;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WMSRequestBuilder {

    private static Logger LOGGER = LoggingHandler.getLogger(WMSRequestBuilder.class);
    
    /**
     * 
     * @param operation
     * @param parameterContainer
     * @return
     * @throws OXFException
     */
    public String buildGetCapabilitiesRequest(Operation operation,
                                                     ParameterContainer parameterContainer) throws OXFException {
        String operationURL = null;
        for (DCP dcp : operation.getDcps()) {
            if (dcp.getHTTPGetRequestMethods() != null) {
                operationURL = dcp.getHTTPGetRequestMethods().get(0).getOnlineResource().getHref();
            }
        }
        if (operationURL == null) {
            throw new NullPointerException();
        }
        if (operationURL.equals("")) {
            throw new OXFException("empty operationURL, no GetCapabilitiesURL found.");
        }

        String version = (String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION).getSpecifiedValue();

        GetCapabilitiesRequest req = null;

        if (version.equals(WMSAdapter.VERSION_111)) {
            req = new GetCapabilitiesRequest111(operationURL);
        }
        else if (version.equals(WMSAdapter.VERSION_110)) {
            req = new GetCapabilitiesRequest110(operationURL);
        }
        else if (version.equals(WMSAdapter.VERSION_100)) {
            req = new GetCapabilitiesRequest100(operationURL);
        }
        else {
            throw new OXFException("version not supported: " + version);
        }

        String requestURLString = req.getCompleteRequestURL();

        return requestURLString;
    }

    /**
     * 
     * @param operation
     * @param parameterContainer
     * @return
     * @throws OXFException
     */
    public String buildGetMapRequest(Operation operation,
                                            ParameterContainer parameterContainer) throws OXFException {
        String operationURL = null;
        for (DCP dcp : operation.getDcps()) {
            if (dcp.getHTTPGetRequestMethods() != null) {
                operationURL = dcp.getHTTPGetRequestMethods().get(0).getOnlineResource().getHref();
            }
        }
        if (operationURL == null) {
            throw new NullPointerException();
        }
        if (operationURL.equals("")) {
            throw new OXFException("empty operationURL, no GetMapURL found.");
        }

        String version = (String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION).getSpecifiedValue();

        GetMapRequest req = null;

        if (version.equals(WMSAdapter.VERSION_111)) {
            req = new GetMapRequest111(operationURL);
        }
        else if (version.equals(WMSAdapter.VERSION_110)) {
            req = new GetMapRequest110(operationURL);
        }
        else if (version.equals(WMSAdapter.VERSION_100)) {
            req = new GetMapRequest100(operationURL);
        }
        else {
            throw new OXFException("version not supported: " + version);
        }

        req.setLayers((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_RESOURCE_ID).getSpecifiedValue());
        String crs = (String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_SRS).getSpecifiedValue();
        req.setCrs(crs);
        req.setHeight( ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue()).toString());
        req.setWidth( ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue()).toString());
        req.setFormat( ((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_FORMAT).getSpecifiedValue()).toString());
        BoundingBox bbox = (BoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();
        RequestBoundingBox reqBox = new RequestBoundingBox(crs);

        reqBox.setMaxx(bbox.getUpperCorner()[0]);
        reqBox.setMaxy(bbox.getUpperCorner()[1]);
        reqBox.setMinx(bbox.getLowerCorner()[0]);
        reqBox.setMiny(bbox.getLowerCorner()[1]);
        req.setBbox(reqBox);

        if (parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_STYLE) != null) {
            req.setStyles((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_STYLE).getSpecifiedValue());
        }

        String requestURLString = req.getCompleteRequestURL();

        return requestURLString;
    }

    /*
     * --- test: ---
     */
    public static void main(String[] args) throws OXFException {
        Operation operation = new Operation("GetCapabilities",
                                            "http://divenos.meraka.csir.co.za:8080/geoserver/wms",
                                            null);

        String version = "1.1.1";

        ParameterContainer paramCon = new ParameterContainer();
        paramCon.addParameterShell(new ParameterShell(new Parameter("version",
                                                                    true,
                                                                    new StringValueDomain(new String[] {"1.1.1",
                                                                                                        "1.1.0",
                                                                                                        "1.0.0"}),
                                                                    Parameter.COMMON_NAME_VERSION),
                                                      version));

        String requestString = new WMSRequestBuilder().buildGetCapabilitiesRequest(operation, paramCon);

        LOGGER.info(requestString);
    }
}