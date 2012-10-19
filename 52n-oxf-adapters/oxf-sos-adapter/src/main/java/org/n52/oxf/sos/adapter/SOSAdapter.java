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

package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.feature.SOSObservationStore;
import org.n52.oxf.sos.util.SosUtil;
import org.n52.oxf.util.IOHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SOS-Adapter for the OX-Framework
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class SOSAdapter implements IServiceAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSAdapter.class);

    public static final String GET_CAPABILITIES = "GetCapabilities";
    public static final String GET_OBSERVATION = "GetObservation";
    public static final String GET_OBSERVATION_BY_ID = "GetObservationById";
    public static final String DESCRIBE_SENSOR = "DescribeSensor";
    public static final String GET_FEATURE_OF_INTEREST = "GetFeatureOfInterest";
    public static final String INSERT_OBSERVATION = "InsertObservation";
    public static final String REGISTER_SENSOR = "RegisterSensor";

    /**
     * Description of the SOSAdapter
     */
    public static final String DESCRIPTION = "This Class implements the Service Adapter Interface and is"
            + "an SOS Adapter for the OXF Framework";

    /**
     * The name of the service operation which returns the data to be added to a map view as a layer.
     */
    public static final String RESOURCE_OPERATION = "GetObservation";

    /**
     * the schema version this adapter instance shall work with.
     */
    protected String serviceVersion = null;

    private ISOSRequestBuilder requestBuilder;

    /**
     * @param serviceVersion
     *        the schema version for which this adapter instance shall be initialized.
     */
    public SOSAdapter(String serviceVersion) {
        this.requestBuilder = SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion);
        this.serviceVersion = serviceVersion;
    }
    
    /**
     * Allows to create an SOSAdapter with custom (non-default) instance of {@link ISOSRequestBuilder}.
     * 
     * @param serviceVersion the schema version for which this adapter instance shall be initialized.
     * @param requestBuilder a custom request builder implementation, if <code>null</code> default will be used.
     * @see ISOSRequestBuilder
     */
    public SOSAdapter(String serviceVersion, ISOSRequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder != null ? requestBuilder : this.requestBuilder;
        this.serviceVersion = serviceVersion;
    }

    /**
     * initializes the ServiceDescriptor by requesting the capabilities document of the SOS.
     * 
     * @param url
     *        the base URL of the SOS
     * @param serviceVersion
     *        the schema version to which the service description shall be conform.
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

        ParameterContainer paramCon = new ParameterContainer();
        paramCon.addParameterShell("acceptVersion", serviceVersion);
        paramCon.addParameterShell("service", "SOS");

        OperationResult opResult = doOperation(new Operation("GetCapabilities", url.toString() + "?", url.toString()),
                                               paramCon);

        return initService(opResult);
    }

    public ServiceDescriptor initService(OperationResult getCapabilitiesResult) throws ExceptionReport, OXFException {
        try {
            if (SosUtil.isVersion100(serviceVersion)) {
                net.opengis.sos.x10.CapabilitiesDocument capsDoc = net.opengis.sos.x10.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
                return initService(capsDoc);
            } else if (SosUtil.isVersion200(serviceVersion)) {
                net.opengis.sos.x20.CapabilitiesDocument capsDoc = net.opengis.sos.x20.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
                return initService(capsDoc);
            } else {
                throw new OXFException("Version is not supported: " + serviceVersion);
            }
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
        catch (IOException e) {
            throw new OXFException(e);
        }
    }

    public ServiceDescriptor initService(net.opengis.sos.x20.CapabilitiesDocument capsDoc) throws OXFException {
        SOSCapabilitiesMapper_200 capsMapper = new SOSCapabilitiesMapper_200();
        ServiceDescriptor result = capsMapper.mapCapabilities(capsDoc);

        return result;
    }
    
    /**
     * 
     * @param capsDoc
     * @return
     * @throws OXFException
     */
    public ServiceDescriptor initService(net.opengis.sos.x10.CapabilitiesDocument capsDoc) throws OXFException {
        SOSCapabilitiesMapper_100 capsMapper = new SOSCapabilitiesMapper_100();

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
     * @param serviceVersion
     *        the schema version to which the operation execution shall be conform.
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("starting Operation: %s with parameters: %s",
            		operation,
            		parameters));
        }

        OperationResult result = null;
        String request = null;

        // GetCapabilities Operation
        if (operation.getName().equals(GET_CAPABILITIES)) {
            request = requestBuilder.buildGetCapabilitiesRequest(parameters);
        }

        // GetObservation Operation
        else if (operation.getName().equals(GET_OBSERVATION)) {
            request = requestBuilder.buildGetObservationRequest(parameters);
        }

        // DescribeSensor Operation
        else if (operation.getName().equals(DESCRIBE_SENSOR)) {
            request = requestBuilder.buildDescribeSensorRequest(parameters);
        }

        // GetFeatureOfInterest Operation
        else if (operation.getName().equals(GET_FEATURE_OF_INTEREST)) {
            request = requestBuilder.buildGetFeatureOfInterestRequest(parameters);
        }

        // InsertObservation Operation
        else if (operation.getName().equals(INSERT_OBSERVATION)) {
            request = requestBuilder.buildInsertObservation(parameters);
        }

        // RegisterSensor Operation
        else if (operation.getName().equals(REGISTER_SENSOR)) {
            request = requestBuilder.buildRegisterSensor(parameters);
        }

        // GetObservationByID Operation
        else if (operation.getName().equals(GET_OBSERVATION_BY_ID)) {
            request = requestBuilder.buildGetObservationByIDRequest(parameters);
        }

        // Operation not supported
        else {
            throw new OXFException("The operation '" + operation.getName() + "' is not supported.");
        }
        
        if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Request builder class: %s; Request: \n%s", 
					requestBuilder.getClass().toString(),
					request));
		}

        // request = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+request;
        HttpMethod method = null;
        try {
        	InputStream is;
        	
        	// TODO will lead into an error if neither get nor post uri is given
        	String uri = null;
        	
        	// just use post uri, when request sended only by post
//        	if (operation.getDcps()[0].getHTTPGetRequestMethods().size() > 0)
//        		uri = operation.getDcps()[0].getHTTPGetRequestMethods().get(0).getOnlineResource().getHref();
//        	else 
        		if (operation.getDcps()[0].getHTTPPostRequestMethods().size() > 0)
        		uri = operation.getDcps()[0].getHTTPPostRequestMethods().get(0).getOnlineResource().getHref();
        		

            /* XXX What sense does it make to always perform a GET request for GetCapabilities?
             * when the user is putting more information (like serviceVersion as CSV) into 
             * the param container, they would not be sent to the service
             */
//            if (operation.getName().equals(GET_CAPABILITIES)) {
//            	String queryString = "REQUEST=GetCapabilities&SERVICE=SOS&acceptversions="+serviceVersion;
//            	method = new GetMethod(uri);
//            	method.setQueryString(queryString);
//            }
//            else {
            
                PostMethod post = new PostMethod(uri.trim());
                post.setRequestEntity(new StringRequestEntity(request, "text/xml", "UTF-8"));
                method = post;
//            }
            method = IOHelper.execute(method);
            is = method.getResponseBodyAsStream();
            result = new OperationResult(is, parameters, request);
            try {
                XmlObject result_xb = XmlObject.Factory.parse(result.getIncomingResultAsStream());
                if (result_xb instanceof net.opengis.ows.x11.ExceptionReportDocument) {
                	throw parseExceptionReport_100(result);
                } else {
                	return result;
                }
            }
            catch (XmlException e) {
            	String msg = String.format("Received XML response could not be parsed to generic class \"%s\". Exception thrown: %s. Message: %s",
            			XmlObject.class.getClass().getName(),
            			e.getClass().getName(),
            			e.getMessage());
            	throw new OXFException(msg,e);
            }
        } catch (IOException e) {
        	String msg = String.format("Sending request to SOS instance caused this IOException: %s",
        			e.getMessage());
            throw new OXFException(msg, e);
        } finally {
        	if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("Reached finally clause. Method: %s; isRequestSent? %b; hasBeenUsed: %s; statusCode: %d; statusText: %s",
						method,
						method.isRequestSent(),
						method.hasBeenUsed(),
						method.getStatusCode(),
						method.getStatusText()
						));
			}
        	if (method != null) {
        		if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Will call releaseConnection()");
				}
        		method.releaseConnection();
        		if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("releaseConnection() called");
				}
        	}
        }
    }

    /**
     * Convenient method to request Observations from an SOS. <br>
     * The method creates a GetObservation request and sends it to the SOS. <br>
     * For the following parameters of the request default values are used:<br>
     * responseFormat := text/xml;subtype="om/1.0.0" <br>
     * resultModel := Observation <br>
     * responseMode := inline
     */
    public OXFFeatureCollection requestObservations(String sosURL,
                                                    String offering,
                                                    String observedProperty,
                                                    String eventTime) throws OXFException, ExceptionReport {

        Operation operation = new Operation("GetObservation", "http://GET_URL_not_used", sosURL);

        ParameterContainer container = new ParameterContainer();
        container.addParameterShell(GET_OBSERVATION_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);
        container.addParameterShell(GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
        container.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, offering);
        container.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/1.0.0\"");
        container.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
        container.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Observation");
        container.addParameterShell(GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTime);
        container.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");

        ISOSRequestBuilder requestBuilder = SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion);
        LOGGER.info("Send Request: {}", requestBuilder.buildGetObservationRequest(container));

        OperationResult opResult = doOperation(operation, container);
		IFeatureStore featureStore = new SOSObservationStore(opResult);

        // The OperationResult can be used as an input for the 'unmarshalFeatures' operation of the
        // SOSObservationStore to parse the returned O&M document and to build up OXFFeature objects.
        OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures();

        return featureCollection;
    }


    private ExceptionReport parseExceptionReport_100(OperationResult result) throws XmlException {

        String requestResult = new String(result.getIncomingResult());

        net.opengis.ows.x11.ExceptionReportDocument xb_execRepDoc = net.opengis.ows.x11.ExceptionReportDocument.Factory.parse(requestResult);
        net.opengis.ows.x11.ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();

        String language = xb_execRepDoc.getExceptionReport().getLang();
        String version = xb_execRepDoc.getExceptionReport().getVersion();

        ExceptionReport oxf_execReport = new ExceptionReport(version, language);
        for (net.opengis.ows.x11.ExceptionType xb_exec : xb_exceptions) {
            String execCode = xb_exec.getExceptionCode();
            String[] execMsgs = xb_exec.getExceptionTextArray();
            String locator = xb_exec.getLocator();

            OWSException owsExec = new OWSException(execMsgs, execCode, result.getSendedRequest(), locator);

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
        return SosUtil.SERVICE_TYPE;
    }

    /**
     * returns the supported versions of the service
     * 
     * @return String[] the supported versions of the service which is connectable by this ServiceAdapter
     */
    public String[] getSupportedVersions() {
        return SosUtil.SUPPORTED_VERSIONS;
    }

    public ISOSRequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    /**
     * @return the schema version for which this adapter instance has been initialized.
     */
    public String getServiceVersion() {
        return serviceVersion;
    }
}