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
package org.n52.oxf.ses.adapter;

import static org.n52.oxf.ows.OwsExceptionCode.OperationNotSupported;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.opengis.ses.x00.CapabilitiesDocument;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.HttpClientException;
import org.n52.oxf.util.web.ProxyAwareHttpClient;
import org.n52.oxf.util.web.SimpleHttpClient;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

/**
 * SES-Adapter for the OX-Framework<br>
 *
 * TODO implement missing operations<br>
 *
 * Current state (could not be tested because of missing dp compliant instance):
 * <ul>
 * <li>GetCapabilities implemented</li>
 * <li>Subscribe</li>
 * <li>Notify</li>
 * <li>RegisterPublisher</li>
 * </ul>
 *
 * @author <a href="mailto:artur.osmanov@uni-muenster.de">Artur Osmanov</a>
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 */
public class SESAdapter implements IServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SESAdapter.class);

	public static final String SUBSCRIBE = "Subscribe";
	public static final String UNSUBSCRIBE = "UnSubscribe";
	public static final String GET_CAPABILITIES = "GetCapabilities";
	public static final String REGISTER_PUBLISHER = "Register";
	public static final String NOTIFY = "Notify";
	public static final String DESCRIBE_SENSOR = "DescribeSensor";
	public static final String NOTIFY_RESPONSE = "NotifyResponse";
	public static final String DESTROY_REGISTRATION = "DestroyRegistration";

	/**
	 * Description of the SESAdapter
	 */
	public static final String DESCRIPTION = "This Class implements the Service Adapter Interface and is"
			+ "an SES Adapter for the OXF Framework";

	/**
	 * The name of the service operation which returns the data to be added to a map view as a layer.
	 */
	public static final String RESOURCE_OPERATION = null;

	/**
	 * The Type of the service which is connectable by this ServiceAdapter
	 */
	public static final String SERVICE_TYPE = "SES";

	/**
	 * the Versions of the services which are connectable by this ServiceAdapter. Should look like e.g.
	 * {"1.1.0","1.2.0"}.
	 */
	public static final String[] SUPPORTED_VERSIONS = {"0.0.0"};


	private String serviceVersion = SUPPORTED_VERSIONS[0];

	/**
	 * constructor which instantiates
	 */
	public SESAdapter() {
		this(SUPPORTED_VERSIONS[0]);
	}

	/**
	 *
	 * @param serviceVersion
	 *        the schema version for which this adapter instance shall be initialized.
	 */
	public SESAdapter(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	/**
	 * @deprecated constructor parameter has no effect
	 */
	@Deprecated
	public SESAdapter(ParameterContainer paramCont) {
	}

	/*
	 * what needs to be done to init the SESAdapter => GetCapabilities?
	 */
	public ServiceDescriptor initService(String serviceURL) throws ExceptionReport, OXFException {

		ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(ISESRequestBuilder.GET_CAPABILITIES_SES_URL, serviceURL);

		OperationResult opResult = doOperation(new Operation(SESAdapter.GET_CAPABILITIES, serviceURL, serviceURL),
				parameters);

		try {
			// free OWS Response from SOAP-Envelope

			EnvelopeDocument envDoc = EnvelopeDocument.Factory.parse(opResult.getIncomingResultAsAutoCloseStream());

			String owsResponse = envDoc.getEnvelope().getBody().xmlText();

			CapabilitiesDocument capsDoc = CapabilitiesDocument.Factory.parse(owsResponse);

			return handleCapabilities(capsDoc);

		}
		catch (XmlException e) {
			throw new OXFException(e);
		}
		catch (IOException e) {
			throw new OXFException(e);
		}

	}

	private ServiceDescriptor handleCapabilities(CapabilitiesDocument capsDoc) throws OXFException {
		SESCapabilitiesMapper_00 mapper = new SESCapabilitiesMapper_00();

		return mapper.mapCapabilities(capsDoc);
	}

	public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer) throws ExceptionReport,
	OXFException {
		if (operation == null) throw new IllegalStateException("Operation cannot be null!");

		ISESRequestBuilder requestBuilder = SESRequestBuilderFactory.generateRequestBuilder(this.serviceVersion);

		String request;
		// SUBSCRIBE
		if (operation.getName().equals(SESAdapter.SUBSCRIBE)) {
			request = requestBuilder.buildSubscribeRequest(parameterContainer);
		}
		// UNSUBSCRIBE
		else if (operation.getName().equals(SESAdapter.UNSUBSCRIBE)) {
			request = requestBuilder.buildUnsubscribeRequest(parameterContainer);
		}
		// GET_CAPABILITIES
		else if (operation.getName().equals(SESAdapter.GET_CAPABILITIES)) {
			request = requestBuilder.buildGetCapabilitiesRequest(parameterContainer);
		}
		// NOTIFY
		else if (operation.getName().equals(SESAdapter.NOTIFY)) {
			request = requestBuilder.buildNotifyRequest(parameterContainer);
		}
		// REIGSER_PUBLISHER
		else if (operation.getName().equals(SESAdapter.REGISTER_PUBLISHER)) {
			request = requestBuilder.buildRegisterPublisherRequest(parameterContainer);
		}
		// DESCRIBE_SENSOR
		else if (operation.getName().equals(SESAdapter.DESCRIBE_SENSOR)) {
			request = requestBuilder.buildDescribeSensorRequest(parameterContainer);
		}
		//DESTORY_REGISTRATION
		else if (operation.getName().equals(SESAdapter.DESTROY_REGISTRATION)) {
			request = requestBuilder.buildDestroyRegistrationRequest(parameterContainer);
		}
		// NOTIFY_RESPONSE
		// this should never happen
//		else if (operation.getName().equals(SESAdapter.NOTIFY_RESPONSE)) {
//			request = SESResponseBuilderFactory.generateResponseBuilder(this.serviceVersion).buildNotifyResponseRequest(parameterContainer);
//		}
		// Operation not supported
		else {
			throw new OXFException("The operation '" + operation.getName() + "' is not supported.");
		}
		try {
			if (operation.getDcps().length == 0) {
				throw new IllegalStateException("No DCP links available to send request to.");
			}
			DCP dcp = operation.getDcps()[0];
			String uri = dcp.getHTTPPostRequestMethods().get(0).getOnlineResource().getHref();

			// TODO extract to adapter interface
			HttpClient httpClient = new ProxyAwareHttpClient(new SimpleHttpClient());
			HttpResponse httpResponse = httpClient.executePost(uri, request);
			HttpEntity responseEntity = httpResponse.getEntity();
			if (httpResponse.getStatusLine().getStatusCode() != 204 && responseEntity.getContent() != null) {
				return new OperationResult(responseEntity.getContent(), parameterContainer, request);
			}

		}
		catch (IOException e) {
			throw new OXFException(e);
		}
		catch (HttpClientException e) {
			throw new OXFException("Could not send request.", e);
		}

		return null;
	}

	public XmlObject handleResponse(String operationName, ByteArrayInputStream input) throws OXFException {
		XmlObject result = null;
		Envelope env;
		Header header;

		if (operationName != null && input != null) {
			try {
				if (operationName.equals(SESAdapter.REGISTER_PUBLISHER)) {
					env = EnvelopeDocument.Factory.parse(input).getEnvelope();
					header = env.getHeader();

					// check for right action
					// http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherResponse
					XmlObject[] actions = XmlUtil.selectPath("declare namespace s='http://www.w3.org/2005/08/addressing' .//s:Action", header);
					String action = null;

					// is this the right request? If NOT throw Exception
					if (actions != null && actions.length == 1) {
						action = actions[0].getDomNode().getFirstChild().getNodeValue();
						if ( !action.equals("http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherResponse")) {

							StringBuilder sb = new StringBuilder();
							BufferedReader reader = new BufferedReader(new InputStreamReader(input));
							while (reader.ready()) {
								sb.append(reader.readLine());
							}
							LOGGER.error("Unexpected Reponse: {}", sb.toString());
							throw new OXFException(OperationNotSupported
									+ ": Not the right response: \""
									+ action
									+ " \" <-> Expected is: \"http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherResponse\"!");
						}
						else {
							result = XmlObject.Factory.parse(env.newInputStream());
						}
					}

					// TODO check if following problem still exist
					// XXX Problem with Namespace and XMLBeans: The document is not a
					// RegisterPublisherResponse@http://docs.oasis-open.org/wsn/br-2: document element
					// namespace mismatch expected "http://docs.oasis-open.org/wsn/br-2" got
					// "http://docs.oasis-open.org/wsn/brw-2"
					//                    result = XmlObject.Factory.parse(body.toString());
					//                    if (result instanceof /*org.oasisOpen.docs.wsn.br2.impl.*/RegisterPublisherResponseDocumentImpl) {
					//                        // soap envelope => body => registerpublisher
					//                        result = ((/*org.oasisOpen.docs.wsn.br2.impl.*/RegisterPublisherResponseDocumentImpl) result).getRegisterPublisherResponse();
					//                    }
				}
			}
			catch (IOException e) {
				throw new OXFException(e);
			}
			catch (XmlException e) {
				// TODO what about SOAP-Exceptions?
				throw new OXFException("RegisterPublisherResponse not wellformed XML", e);
			}
		}
		return result;
	}

	public String getDescription() {
		return SESAdapter.DESCRIPTION;
	}

	public String getResourceOperationName() {
		return RESOURCE_OPERATION;
	}

	public String getServiceType() {
		return SESAdapter.SERVICE_TYPE;
	}

	public String[] getSupportedVersions() {
		return SESAdapter.SUPPORTED_VERSIONS;
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
	// TODO could be externalized because the same methods are use in the SOSAdapter => identify common
	// ServiceAdapterTasks

	/*
	 * Following methods removed as they never succeed (response is always SOAP-embedded)
	 */
//	private ExceptionReport parseExceptionReport_000(OperationResult result) throws XmlException {
//
//		String requestResult = new String(result.getIncomingResult());
//
//		ExceptionReportDocument xb_execRepDoc = ExceptionReportDocument.Factory.parse(requestResult);
//		ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();
//
//		String language = xb_execRepDoc.getExceptionReport().getLang();
//		String version = xb_execRepDoc.getExceptionReport().getVersion();
//
//		ExceptionReport oxf_execReport = new ExceptionReport(version, language);
//		for (ExceptionType xb_exec : xb_exceptions) {
//			String execCode = xb_exec.getExceptionCode();
//			String[] execMsgs = xb_exec.getExceptionTextArray();
//			String locator = xb_exec.getLocator();
//
//			OWSException owsExec = new OWSException(execMsgs, execCode, result.getSendedRequest(), locator);
//
//			oxf_execReport.addException(owsExec);
//		}
//
//		return oxf_execReport;
//	}
//
//	private ExceptionReport parseExceptionReport_100(OperationResult result) throws XmlException {
//
//		String requestResult = new String(result.getIncomingResult());
//
//		net.opengis.ows.x11.ExceptionReportDocument xb_execRepDoc = net.opengis.ows.x11.ExceptionReportDocument.Factory.parse(requestResult);
//		net.opengis.ows.x11.ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();
//
//		String language = xb_execRepDoc.getExceptionReport().getLang();
//		String version = xb_execRepDoc.getExceptionReport().getVersion();
//
//		ExceptionReport oxf_execReport = new ExceptionReport(version, language);
//		for (net.opengis.ows.x11.ExceptionType xb_exec : xb_exceptions) {
//			String execCode = xb_exec.getExceptionCode();
//			String[] execMsgs = xb_exec.getExceptionTextArray();
//			String locator = xb_exec.getLocator();
//
//			OWSException owsExec = new OWSException(execMsgs, execCode, result.getSendedRequest(), locator);
//
//			oxf_execReport.addException(owsExec);
//		}
//
//		return oxf_execReport;
//
//	}

}
