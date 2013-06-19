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
package org.n52.oxf.csw.adapter;

import java.io.IOException;

import net.opengis.ows.ExceptionReportDocument;
import net.opengis.ows.ExceptionType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.HttpClientException;
import org.n52.oxf.util.web.ProxyAwareHttpClient;
import org.n52.oxf.util.web.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class CSWAdapter implements IServiceAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CSWAdapter.class);

    // required operations:
    public static final String GET_CAPABILITIES = "GetCapabilities";
    public static final String DESCRIBE_RECORD = "DescribeRecord";
    public static final String GET_RECORDS = "GetRecords";
    
    // optional operations:
    public static final String GET_RECORD_BY_ID = "GetRecordById";
    public static final String HARVEST = "Harvest";
    public static final String TRANSACTION = "Transaction";
    public static final String GET_DOMAIN = "GetDomain";

    /**
     * Description of the CSWAdapter
     */
    public static final String DESCRIPTION = "This Class implements the Service Adapter Interface and is"
            + "a CSW Adapter for the OXF Framework";

    /**
     * The Type of the service which is connectable by this ServiceAdapter
     */
    public static final String SERVICE_TYPE = "CSW";

    /**
     * the Versions of the services which are connectable by this ServiceAdapter.
     */
    public static final String[] SUPPORTED_VERSIONS = {"2.0.2"};
    
    /**
     * The name of the service operation which returns the data to be added to a map view as a layer.
     */
    public static final String RESOURCE_OPERATION = "GetRecords";

    public static final String NAMESPACE = "http://www.opengis.net/cat/csw/2.0.2";
    
    
    private CSWRequestBuilder requestBuilder;


    /**
     * standard constructor
     */
    public CSWAdapter() {
        requestBuilder = new CSWRequestBuilder();
    }

    /**
     * initializes the ServiceDescriptor by requesting the capabilities document of the CSW.
     * 
     * @param url
     *        the base URL of the CSW
     * 
     * @return the ServiceDescriptor based on the retrieved capabilities document
     * 
     * @throws ExceptionReport
     *         if service side exception occurs
     * @throws OXFException
     *         if internal exception (in general parsing error or Capabilities doc is incorrect) occurs
     * 
     */
    public ServiceDescriptor initService(String url) throws ExceptionReport, OXFException {
        ParameterContainer paramContainer = new ParameterContainer();
        paramContainer.addParameterShell("acceptVersions", SUPPORTED_VERSIONS[0]);
        paramContainer.addParameterShell("service", SERVICE_TYPE);

        Operation operation = new Operation("GetCapabilities", url+"?", null);
        return initService(doOperation(operation, paramContainer));
    }

    public ServiceDescriptor initService(OperationResult getCapabilitiesResult) throws ExceptionReport, OXFException {
        try {
            net.opengis.cat.csw.x202.CapabilitiesDocument capsDoc = net.opengis.cat.csw.x202.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
            return initService(capsDoc);
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
        catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public ServiceDescriptor initService(net.opengis.cat.csw.x202.CapabilitiesDocument capsDoc) throws OXFException {
        CSWCapabilitiesMapper_202 capsMapper = new CSWCapabilitiesMapper_202();
        ServiceDescriptor result = capsMapper.mapCapabilities(capsDoc);

        return result;
    }

    /**
     * 
     * @param operation
     *        the operation which the adapter has to execute on the service. this operation includes also the
     *        parameter values.
     * 
     * @param parameters
     *        Map which contains the parameters of the operation and the corresponding parameter values
     * 
     * @throws ExceptionReport
     *         Report which contains the service sided exceptions
     * 
     * @throws OXFException
     *         if the sending of the post message failed.<br>
     *         if the specified Operation is not supported.
     * 
     * @return the result of the executed operation
     */
    public OperationResult doOperation(Operation operation, ParameterContainer parameters) throws ExceptionReport,
            OXFException {
        
        OperationResult result = null;

        String request = null;

        String httpMethod = "GET";
        
        if (operation.getName().equals(GET_CAPABILITIES)) {
            request = requestBuilder.buildGetCapabilitiesRequest(parameters);
        }

        else if (operation.getName().equals(DESCRIBE_RECORD)) {
            request = requestBuilder.buildDescribeRecordRequest(parameters);
        }

        else if (operation.getName().equals(GET_RECORDS)) {
            request = requestBuilder.buildGetRecordsRequest(parameters);
            httpMethod = "POST";
        }

        else if (operation.getName().equals(GET_RECORD_BY_ID)) {
            request = requestBuilder.buildGetRecordByIdRequest(parameters);
        }
        
        // Operation not supported
        else {
            throw new OXFException("The operation '" + operation.getName() + "' is not supported.");
        }

        try {
            if (operation.getDcps().length == 0) {
                throw new IllegalStateException("No DCP links available to send request to.");
            }

            // TODO pull httpClient to super class (make interface abstract)
            HttpClient httpClient = new ProxyAwareHttpClient(new SimpleHttpClient());
            DCP dcp = operation.getDcps()[0];
            
            if (httpMethod.equals("POST")) {
                String uri = dcp.getHTTPPostRequestMethods().get(0).getOnlineResource().getHref();
                HttpResponse response = httpClient.executePost(uri, request, ContentType.TEXT_XML);
                HttpEntity responseEntity = response.getEntity(); 
                result = new OperationResult(responseEntity.getContent(), parameters, request);
            }
            else {
                String baseURI = dcp.getHTTPGetRequestMethods().get(0).getOnlineResource().getHref();
                String requestURI = baseURI + request;
                HttpResponse response = httpClient.executeGet(requestURI);
                HttpEntity responseEntity = response.getEntity(); 
                result = new OperationResult(responseEntity.getContent(), parameters, request);
            }
            
            try {
                ExceptionReport execRep = parseExceptionReport(result);

                throw execRep;
            }
            catch (XmlException e) {
                // parseError --> no ExceptionReport was returned.
                LOGGER.info("Service reported no exceptions.");
            }
        }
        catch (IOException e) {
            throw new OXFException("Could not create OperationResult.", e);
        }
        catch (HttpClientException e) {
            throw new OXFException("Could not send request.", e);
            
        }

        return result;
    }

    /**
     * checks whether the response of the doOperation is an ExceptionReport. If it is, the report with the
     * contained OWSExceptions are parsed and a new ExceptionReport is created and will be returned.
     * 
     * @throws ExceptionReport
     *         the exception report containing the service exceptions
     * @throws OXFException
     *         if an parsing error occurs
     * @throws XmlException 
     */
    private ExceptionReport parseExceptionReport(OperationResult result) throws XmlException {

        String requestResult = new String(result.getIncomingResult());

        ExceptionReportDocument xb_execRepDoc = ExceptionReportDocument.Factory.parse(requestResult);
        ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();

        String language = null;//xb_execRepDoc.getExceptionReport().getLang();
        String version = xb_execRepDoc.getExceptionReport().getVersion();

        ExceptionReport oxf_execReport = new ExceptionReport(version, language);
        for (ExceptionType xb_exec : xb_exceptions) {
            String execCode = xb_exec.getExceptionCode();
            String[] execMsgs = xb_exec.getExceptionTextArray();
            String locator = xb_exec.getLocator();

            OWSException owsExec = new OWSException(execMsgs,
                                                    execCode,
                                                    result.getSendedRequest(),
                                                    locator);

            oxf_execReport.addException(owsExec);
        }

        return oxf_execReport;

    }

    /**
     * returns the ResourceOperationName
     * 
     * @return The name of the service operation which returns the data to be added to a map view as a layer.
     */
    public String getResourceOperationName() {
        return RESOURCE_OPERATION;
    }

    /**
     * returns the description of this Service Adapter
     * 
     * @return String the description of the adapter
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * returns the type of the service which is connectable by this ServiceAdapter
     * 
     * @return String the type of service
     */
    public String getServiceType() {
        return SERVICE_TYPE;
    }

    /**
     * returns the supported versions of the service
     * 
     * @return String[] the supported versions of the service which is connectable by this ServiceAdapter
     */
    public String[] getSupportedVersions() {
        return SUPPORTED_VERSIONS;
    }
}