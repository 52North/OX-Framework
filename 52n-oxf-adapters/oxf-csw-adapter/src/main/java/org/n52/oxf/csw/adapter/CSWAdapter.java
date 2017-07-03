/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.oxf.csw.adapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    protected static final List<String> SUPPORTED_VERSIONS = Collections.singletonList("2.0.2");

    /**
     * The name of the service operation which returns the data to be added to a map view as a layer.
     */
    public static final String RESOURCE_OPERATION = "GetRecords";

    public static final String NAMESPACE = "http://www.opengis.net/cat/csw/2.0.2";

    private final CSWRequestBuilder requestBuilder;

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
    @Override
    public ServiceDescriptor initService(String url) throws ExceptionReport, OXFException {
        ParameterContainer paramContainer = new ParameterContainer();
        paramContainer.addParameterShell("acceptVersions", SUPPORTED_VERSIONS.get(0));
        paramContainer.addParameterShell("service", SERVICE_TYPE);

        Operation operation = new Operation("GetCapabilities", url+"?", null);
        return initService(doOperation(operation, paramContainer));
    }

    public ServiceDescriptor initService(OperationResult getCapabilitiesResult) throws ExceptionReport, OXFException {
        try {
            net.opengis.cat.csw.x202.CapabilitiesDocument capsDoc =
                    net.opengis.cat.csw.x202.CapabilitiesDocument.Factory.parse(
                            getCapabilitiesResult.getIncomingResultAsAutoCloseStream());
            return initService(capsDoc);
        }
        catch (XmlException | IOException e) {
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
    @Override
    public OperationResult doOperation(Operation operation, ParameterContainer parameters) throws ExceptionReport,
            OXFException {

        OperationResult result = null;

        String request = null;

        String httpMethod = "GET";

        switch (operation.getName()) {
            case GET_CAPABILITIES:
                request = requestBuilder.buildGetCapabilitiesRequest(parameters);
                break;
            case DESCRIBE_RECORD:
                request = requestBuilder.buildDescribeRecordRequest(parameters);
                break;
            case GET_RECORDS:
                request = requestBuilder.buildGetRecordsRequest(parameters);
                httpMethod = "POST";
                break;
            case GET_RECORD_BY_ID:
                request = requestBuilder.buildGetRecordByIdRequest(parameters);
                break;
            default:
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
    private ExceptionReport parseExceptionReport(OperationResult result) throws XmlException, OXFException {

        String requestResult;
        try {
            requestResult = new String(result.getIncomingResult(),"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new OXFException("Default encoding UTF-8 not supported!");
        }

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

    @Override
    public String getResourceOperationName() {
        return RESOURCE_OPERATION;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getServiceType() {
        return SERVICE_TYPE;
    }

    @Override
    public String[] getSupportedVersions() {
        return new ArrayList<>(
            SUPPORTED_VERSIONS)
            .toArray(new String[SUPPORTED_VERSIONS.size()]);
    }

}
