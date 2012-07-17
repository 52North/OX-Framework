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

package org.n52.oxf.adapter.wms;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.wmsclientcore.WmsException;
import org.n52.wmsclientcore.capabilities.CapabilitiesFactory;
import org.n52.wmsclientcore.capabilities.model.WMS_Capabilities;
import org.n52.wmsclientcore.operations.GetCapabilitiesOperation;
import org.n52.wmsclientcore.request.GetCapabilitiesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class enables the communication to a OGC WMS. <br/>The Adapter is capable to handle WMS <b>1.0.0,
 * 1.1.0, 1.1.1</b>.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 */
public class WMSAdapter implements IServiceAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WMSAdapter.class);
    
    public static final String SERVICE_TYPE = "OGC:WMS";
    
    public static final String VERSION_100 = "1.0.0";
    public static final String VERSION_110 = "1.1.0";
    public static final String VERSION_111 = "1.1.1";

    public static final String OPERATION_GETMAP = "GetMap";
    public static final String OPERATION_GETFEATUREINFO = "GetFeatureInfo";
    public static final String OPERATION_GETCAPABILITIES = "GetCapabilities";
    
    private String version = null;

    private WMSCapabilitiesMapper capsMapper;
    private WMSRequestBuilder requestBuilder;

    /**
     * standard constructor
     */
    public WMSAdapter() {
        capsMapper = new WMSCapabilitiesMapper();
        requestBuilder = new WMSRequestBuilder();
    }

    /**
     * 
     * @param version
     *        the version of the service - use one of the static variables <code>VERSION_100</code>,
     *        <code>VERSION_110</code> or <code>VERSION_111</code>.
     */
    public WMSAdapter(String version) {
        this.version = version;
        
        capsMapper = new WMSCapabilitiesMapper();
        requestBuilder = new WMSRequestBuilder();
    }

    /**
     * if url is empty or <code>null</code>, a
     * 
     * @link OXFException will be thrown.
     * @see IServiceAdapter#initService(String)
     */
    public ServiceDescriptor initService(String url) throws ExceptionReport, OXFException {
        if (url == null || url.equals("")) {
            throw new OXFException("The URL is empty or null!");
        }
        WMS_Capabilities capabilities = null;
        GetCapabilitiesRequest request = null;
        try {
            GetCapabilitiesOperation getCapabilities = new GetCapabilitiesOperation();

            String ver = this.version;
            if (ver == null) {
                ver = VERSION_111; // if no version was specified use latest version 1.1.1
            }

            request = getCapabilities.getGetCapabilitiesRequest(ver, url);
            
            // get the URL so that we can log the call to the service- 
            String completeRequest = request.getCompleteRequestURL();       
            
            LOGGER.info("WMSAdapter: initService: Capabilities requested from: " + completeRequest);
            
            capabilities = CapabilitiesFactory.createCapabilities(new URL(request.getCompleteRequestURL()));
        }
        catch (WmsException e) {
            throw new OXFException(e);
        }
        catch (MalformedURLException e) {
            throw new OXFException(e);
        }
        if (capabilities == null) {
            throw new OXFException("Could not generate capabilities-object for: "
                    + request.getCompleteRequestURL());
        }
        
        return capsMapper.mapCapabilities(capabilities);
    }

    /**
     * 
     */
    public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer) throws ExceptionReport,
            OXFException {
        String requestURLString = null;

        if (operation == null) {
            throw new NullPointerException();
        }

        else if (operation.getName().equals(WMSAdapter.OPERATION_GETCAPABILITIES)) {
            requestURLString = requestBuilder.buildGetCapabilitiesRequest(operation,
                                                                             parameterContainer);
        }

        else if (operation.getName().equals(WMSAdapter.OPERATION_GETMAP)) {
            requestURLString = requestBuilder.buildGetMapRequest(operation, parameterContainer);
        }
        else {
            throw new OXFException("Operation name not supported: " + operation.getName());
        }

        try {
            URL requestURL = new URL(requestURLString);
            LOGGER.info("Following operation requested: " + requestURLString);
            return new OperationResult(requestURL.openStream(),
                                       parameterContainer,
                                       requestURLString);
            // OGC SERVICE EXCEPTIONS should be processed!
        }
        catch (MalformedURLException e) {
            throw new OXFException("Malformed url: " + requestURLString, e);
        }
        catch (IOException e) {
            throw new OXFException("error occured while receiving data", e);
        }
    }

    public String getDescription() {
        return "This adapter communicates with OGC WebMapServices. The supported versions are: 1.0.0, 1.1.0 and 1.1.1. The adapter does not support SLDs.";
    }

    public String getServiceType() {
        return SERVICE_TYPE;
    }

    public String[] getSupportedVersions() {
        return new String[] {VERSION_100, VERSION_110, VERSION_111};
    }

    /**
     * concatenates all entries of the list to a comma-seperated String.
     * 
     * @param stringArray
     * @return
     */
    private String toCSString(ArrayList<String> stringArray) {
        StringBuffer sb = new StringBuffer();
        for (String string : stringArray) {
            sb.append(string);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length());
        return sb.toString();
    }

    public String getResourceOperationName() {
        return OPERATION_GETMAP;
    }
}