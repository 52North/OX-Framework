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

import static java.lang.String.format;
import static org.apache.http.entity.ContentType.TEXT_XML;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.io.IOException;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.n52.oxf.util.web.GzipEnabledHttpClient;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.HttpClientException;
import org.n52.oxf.util.web.ProxyAwareHttpClient;
import org.n52.oxf.util.web.SimpleHttpClient;
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

    private HttpClient httpClient;

    /**
     * @param serviceVersion
     *        the schema version for which this adapter instance shall be initialized.
     */
    public SOSAdapter(final String serviceVersion) {
        this(serviceVersion, (ISOSRequestBuilder) null);
    }

    /**
     * @param serviceVersion
     *        the schema version for which this adapter instance shall be initialized.
     * @param httpclient
     *        the (decorated) {@link HttpClient} to use for service connections.
     */
    public SOSAdapter(final String serviceVersion, final HttpClient httpclient) {
        this(serviceVersion, (ISOSRequestBuilder) null);
        httpClient = httpclient; // override simple client
    }

    /**
     * Allows to create an SOSAdapter with custom (non-default) instance of {@link ISOSRequestBuilder}.<br>
     * <br>
     * By default the created instance will use a {@link SimpleHttpClient} for service communication. If
     * further features are needed the {@link SimpleHttpClient} can be decorated with further configuration
     * setups like a {@link GzipEnabledHttpClient} or a {@link ProxyAwareHttpClient}.
     * 
     * @param serviceVersion
     *        the schema version for which this adapter instance shall be initialized.
     * @param requestBuilder
     *        a custom request builder implementation, if <code>null</code> a default builder will be used
     *        (according to the given version).
     * @see ISOSRequestBuilder
     */
    public SOSAdapter(final String serviceVersion, final ISOSRequestBuilder requestBuilder) {
        httpClient = new SimpleHttpClient();
        this.serviceVersion = serviceVersion;
        if (requestBuilder == null) {
            this.requestBuilder = SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion);
        }
        else {
            this.requestBuilder = requestBuilder;
        }
    }

    /**
     * @param requestBuilder
     *        a custom {@link ISOSRequestBuilder} implementation the {@link SOSAdapter} shall use.
     */
    public void setRequestBuilder(final ISOSRequestBuilder requestBuilder) {
        if (requestBuilder != null) {
            this.requestBuilder = requestBuilder;
        }
    }

    /**
     * Sets a custom {@link HttpClient} for service communication. A {@link SimpleHttpClient} can be decorated
     * to enable for example GZIP encoding (setting Accept-Encoding plus GZIP decompressing) or being aware of
     * proxies.
     * 
     * @param httpClient
     *        a customly configured {@link HttpClient} the {@link SOSAdapter} shall use.
     * @see ProxyAwareHttpClient
     * @see GzipEnabledHttpClient
     */
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
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
    @Override
	public ServiceDescriptor initService(final String url) throws ExceptionReport, OXFException {
        final ParameterContainer paramContainer = new ParameterContainer();
        paramContainer.addParameterShell("acceptVersions", serviceVersion);
        paramContainer.addParameterShell("service", "SOS");

        final String baseUrlPost = url;
        final String baseUrlGet = baseUrlPost + "?";
        final Operation operation = new Operation("GetCapabilities", baseUrlGet, baseUrlPost);
        return initService(doOperation(operation, paramContainer));
    }

    public ServiceDescriptor initService(final OperationResult getCapabilitiesResult) throws ExceptionReport, OXFException {
        try {
            if (SosUtil.isVersion100(serviceVersion)) {
                final net.opengis.sos.x10.CapabilitiesDocument capsDoc = net.opengis.sos.x10.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
                return initService(capsDoc);
            }
            else if (SosUtil.isVersion200(serviceVersion)) {
                final net.opengis.sos.x20.CapabilitiesDocument capsDoc = net.opengis.sos.x20.CapabilitiesDocument.Factory.parse(getCapabilitiesResult.getIncomingResultAsStream());
                return initService(capsDoc);
            }
            else {
                throw new OXFException("Version is not supported: " + serviceVersion);
            }
        }
        catch (final XmlException e) {
            throw new OXFException(e);
        }
        catch (final IOException e) {
            throw new OXFException(e);
        }
    }

    public ServiceDescriptor initService(final net.opengis.sos.x20.CapabilitiesDocument capsDoc) throws OXFException {
        final SOSCapabilitiesMapper_200 capsMapper = new SOSCapabilitiesMapper_200();
        final ServiceDescriptor result = capsMapper.mapCapabilities(capsDoc);

        return result;
    }

    /**
     * 
     * @param capsDoc
     * @return
     * @throws OXFException
     */
    public ServiceDescriptor initService(final net.opengis.sos.x10.CapabilitiesDocument capsDoc) throws OXFException {
        final SOSCapabilitiesMapper_100 capsMapper = new SOSCapabilitiesMapper_100();

        final ServiceDescriptor result = capsMapper.mapCapabilities(capsDoc);

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
	public OperationResult doOperation(final Operation operation, final ParameterContainer parameters) throws ExceptionReport,
            OXFException {

        OperationResult result = null;

        final String request = buildRequest(operation, parameters);

        if (operation.getDcps().length == 0) {
        	throw new IllegalStateException("No DCP links available to send request to.");
        }

        String uri = null;
        if (operation.getDcps()[0].getHTTPPostRequestMethods().size() > 0) {
        	uri = operation.getDcps()[0].getHTTPPostRequestMethods().get(0).getOnlineResource().getHref();
        }

        /*
         *  TODO implement support for different bindings using other HTTP methods than POST!
         * Ideas: binding information in parameters and some default values resulting in the same 
         * result like the current implementation
         */

        try {
            final HttpResponse httpResponse = httpClient.executePost(uri.trim(), request, TEXT_XML);
            final HttpEntity responseEntity = httpResponse.getEntity();
            result = new OperationResult(responseEntity.getContent(), parameters, request);

            // TODO make us independent from XmlObject
            // DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // DocumentBuilder builder = factory.newDocumentBuilder();
            // Document document = builder.parse(result.getIncomingResultAsStream());
            // Element root = document.getDocumentElement();
            // if (root.getNodeName().equals("ExceptionReport")) {
            // Element exceptionReport = root;
            // throw createExceptionReportException(exceptionReport, result);
            // }
            try {
                final XmlObject result_xb = XmlObject.Factory.parse(result.getIncomingResultAsStream());
                if (result_xb.schemaType() == ExceptionReportDocument.type) {
                    throw parseOws110ExceptionReport(result);
                }
            }
            catch (final XmlException e) {
                throw new OXFException("Could not parse response to XML.", e);
            }
            return result;
        }
        catch (final HttpClientException e) {
            throw new OXFException("Sending request failed.", e);
        }
        catch (final IOException e) {
            throw new OXFException("Could not create OperationResult.", e);
        }
    }

    private String buildRequest(final Operation operation, final ParameterContainer parameters) throws OXFException {
        if (operation.getName().equals(GET_CAPABILITIES)) {
            return requestBuilder.buildGetCapabilitiesRequest(parameters);
        }
        else if (operation.getName().equals(GET_OBSERVATION)) {
            return requestBuilder.buildGetObservationRequest(parameters);
        }
        else if (operation.getName().equals(DESCRIBE_SENSOR)) {
            return requestBuilder.buildDescribeSensorRequest(parameters);
        }
        else if (operation.getName().equals(GET_FEATURE_OF_INTEREST)) {
            return requestBuilder.buildGetFeatureOfInterestRequest(parameters);
        }
        else if (operation.getName().equals(INSERT_OBSERVATION)) {
            return requestBuilder.buildInsertObservationRequest(parameters);
        }
        else if (operation.getName().equals(REGISTER_SENSOR)) {
            return requestBuilder.buildRegisterSensorRequest(parameters);
        }
        else if (operation.getName().equals(GET_OBSERVATION_BY_ID)) {
            return requestBuilder.buildGetObservationByIDRequest(parameters);
        }
        else {
            throw new OXFException(format("Operation '%s' not supported.", operation.getName()));
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
    public OXFFeatureCollection requestObservations(final String sosURL,
                                                    final String offering,
                                                    final String observedProperty,
                                                    final String eventTime) throws OXFException, ExceptionReport {

        final Operation operation = new Operation("GetObservation", "http://GET_URL_not_used", sosURL);

        final ParameterContainer container = new ParameterContainer();
        container.addParameterShell(GET_OBSERVATION_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);
        container.addParameterShell(GET_OBSERVATION_VERSION_PARAMETER, serviceVersion);
        container.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, offering);
        container.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/1.0.0\"");
        container.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
        container.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, "Observation");
        container.addParameterShell(GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTime);
        container.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, "inline");

        final ISOSRequestBuilder requestBuilder = SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion);
        LOGGER.info("Send Request: {}", requestBuilder.buildGetObservationRequest(container));

        final OperationResult opResult = doOperation(operation, container);
        final IFeatureStore featureStore = new SOSObservationStore(opResult);

        // The OperationResult can be used as an input for the 'unmarshalFeatures' operation of the
        // SOSObservationStore to parse the returned O&M document and to build up OXFFeature objects.
        final OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures();

        return featureCollection;
    }

    private ExceptionReport parseOws110ExceptionReport(final OperationResult result) throws IOException, XmlException {
        final ExceptionReportDocument xb_execRepDoc = ExceptionReportDocument.Factory.parse(result.getIncomingResultAsStream());
        final ExceptionType[] xb_exceptions = xb_execRepDoc.getExceptionReport().getExceptionArray();

        final String language = xb_execRepDoc.getExceptionReport().getLang();
        final String version = xb_execRepDoc.getExceptionReport().getVersion();

        final ExceptionReport oxf_execReport = new ExceptionReport(version, language);
        for (final ExceptionType xb_exec : xb_exceptions) {
            final String execCode = xb_exec.getExceptionCode();
            final String[] execMsgs = xb_exec.getExceptionTextArray();
            final String locator = xb_exec.getLocator();

            final OWSException owsExec = new OWSException(execMsgs, execCode, result.getSendedRequest(), locator);

            oxf_execReport.addException(owsExec);
        }
        return oxf_execReport;
    }

    /**
     * returns the ResourceOperationName
     * 
     * @return The name of the service operation which returns the data to be added to a map view as a layer.
     */
    @Override
	public String getResourceOperationName() {
        return RESOURCE_OPERATION;
    }

    /**
     * returns the description of this Service Adapter
     * 
     * @return String the description of the adapter
     */
    @Override
	public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * returns the type of the service which is connectable by this ServiceAdapter
     * 
     * @return String the type of service
     */
    @Override
	public String getServiceType() {
        return SosUtil.SERVICE_TYPE;
    }

    /**
     * returns the supported versions of the service
     * 
     * @return String[] the supported versions of the service which is connectable by this ServiceAdapter
     */
    @Override
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