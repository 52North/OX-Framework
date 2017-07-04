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
package org.n52.oxf.sos.adapter.v100;

import java.math.BigInteger;
import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.gml.AbstractTimeObjectType;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.MeasureType;
import net.opengis.gml.PointType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.om.x10.ComplexObservationType;
import net.opengis.om.x10.GeometryObservationType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.om.x10.ProcessPropertyType;
import net.opengis.om.x10.TemporalObservationType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sos.x10.GetFeatureOfInterestDocument;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest;
import net.opengis.sos.x10.GetObservationByIdDocument;
import net.opengis.sos.x10.GetObservationByIdDocument.GetObservationById;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.EventTime;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.Result;
import net.opengis.sos.x10.InsertObservationDocument;
import net.opengis.sos.x10.InsertObservationDocument.InsertObservation;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.sos.x10.RegisterSensorDocument;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor.SensorDescription;
import net.opengis.sos.x10.ResponseModeType;
import net.opengis.swe.x101.AbstractDataArrayType.ElementCount;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x101.DataArrayType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.PhenomenonPropertyType;
import net.opengis.swe.x101.TextBlockDocument.TextBlock;
import net.opengis.swe.x101.TimeObjectPropertyType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.xml.XMLConstants;
import org.n52.oxf.xmlbeans.parser.GMLAbstractFeatureCase;
import org.n52.oxf.xmlbeans.parser.OfferingInSMLOutputsCase;
import org.n52.oxf.xmlbeans.parser.SASamplingPointCase;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains attributes and methods to encode SOSOperationRequests as String in xml-format
 *
 * @author <a href="mailto:broering@52north.org">Arne Br&ouml;ring</a>
 * @author <a href="mailto:ehjuerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class SOSRequestBuilder_100 implements ISOSRequestBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSRequestBuilder_100.class);

    /**
     * Builds the GetCapabilities-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     *
     * <br>
     * <br>
     * The following are optional: The following are optional:
     * <li>updateSequence</li>
     * <li>acceptVersions</li>
     * <li>sections</li>
     * <li>acceptFormats</li>
     *
     * @param parameters
     *        the parameters of the request
     *
     * @return CapabilitiesRequest in xml-Format as String
     */
    @Override
    public String buildGetCapabilitiesRequest(final ParameterContainer parameters) {

        final GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();

        final GetCapabilities getCap = getCapDoc.addNewGetCapabilities();

        //
        // set required elements:
        //

        getCap.setService((String) parameters.getParameterShellWithServiceSidedName(SERVICE).getSpecifiedValue());

        //
        // set optional elements:
        //

        // Parameter "updateSequence":
        if (parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER) != null) {
            getCap.setUpdateSequence((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER).getSpecifiedValue());
        }

        // Parameter "AcceptVersions":
        ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER);
        if (versionPS == null){
            versionPS = parameters.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION);
        }
        if (versionPS != null) {
            final AcceptVersionsType acceptedVersions = getCap.addNewAcceptVersions();

            if (versionPS.hasSingleSpecifiedValue()) {
                acceptedVersions.addVersion((String) versionPS.getSpecifiedValue());
            }
            else {
                final String[] versionArray = versionPS.getSpecifiedTypedValueArray(String[].class);
                for (final String version : versionArray) {
                    acceptedVersions.addVersion(version);
                }
            }
        }

        // Parameter "sections":
        final ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {
            final SectionsType sections = getCap.addNewSections();

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                final String[] selectedSections = sectionParamShell.getSpecifiedTypedValueArray(String[].class);
                for (final String selectedSection : selectedSections) {
                    sections.addSection(selectedSection);
                }
            }
            else if (sectionParamShell.hasSingleSpecifiedValue()) {
                sections.addSection((String) sectionParamShell.getSpecifiedValue());
            }
        }

        return getCapDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    /**
     * Builds the GetObservation-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * <li>version</li>
     * <li>offering</li>
     * <li>observedProperty</li>
     * <li>resultFormat</li>
     *
     * <br>
     * <br>
     * The following ones are optional:
     * <li>eventTime</li>
     * <li>procedure</li>
     * <li>featureOfInterest</li>
     * <li>result</li>
     * <li>resultModel</li>
     * <li>responseMode</li>
     *
     * @param parameters
     *        parameters of the request
     *
     * @return GetObservationRequest in XML-Format as String
     * @throws OXFException
     * @throws OXFException
     *
     */
    @Override
    public String buildGetObservationRequest(final ParameterContainer parameters) throws OXFException {

        final GetObservationDocument xb_getObsDoc = GetObservationDocument.Factory.newInstance();

        final GetObservation xb_getObs = xb_getObsDoc.addNewGetObservation();

        //
        // set required elements:
        //
        xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(SERVICE).getSpecifiedValue());

        xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(VERSION).getSpecifiedValue());

        xb_getObs.setOffering((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OFFERING_PARAMETER).getSpecifiedValue());

        xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());

        final ParameterShell observedPropertyPS = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
        final String[] observedProperties = observedPropertyPS.getSpecifiedTypedValueArray(String[].class);
        xb_getObs.setObservedPropertyArray(observedProperties);

        //
        // set optional elements:
        //
        // TODO eventTime may be multiple: String[]
        // TODO featureOfInterest is only allowed once
        //
        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
            ITime specifiedTime;

            final Object timeParamValue = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER).getSpecifiedValue();
            if (timeParamValue instanceof ITime) {
                specifiedTime = (ITime) timeParamValue;
            }
            else if (timeParamValue instanceof String) {
                specifiedTime = TimeFactory.createTime((String) timeParamValue);
            }
            else {
                throw new OXFException("The class (" + timeParamValue.getClass()
                        + ") of the value of the parameter 'eventTime' is not supported.");
            }


            final BinaryTemporalOpType xb_binTempOp = BinaryTemporalOpType.Factory.newInstance();
            xb_binTempOp.addNewPropertyName();

            XmlCursor cursor = xb_binTempOp.newCursor();
            cursor.toChild(new QName("http://www.opengis.net/ogc", "PropertyName"));
            cursor.setTextValue("urn:ogc:data:time:iso8601");
//            cursor.setTextValue("om:samplingTime");

            String timeType = null;

            if (specifiedTime instanceof ITimePeriod) {
                final ITimePeriod oc_timePeriod = (ITimePeriod) specifiedTime;
                final TimePeriodType xb_timePeriod = TimePeriodType.Factory.newInstance();

                final TimePositionType xb_beginPosition = xb_timePeriod.addNewBeginPosition();
                xb_beginPosition.setStringValue(oc_timePeriod.getStart().toISO8601Format());

                final TimePositionType xb_endPosition = xb_timePeriod.addNewEndPosition();
                xb_endPosition.setStringValue(oc_timePeriod.getEnd().toISO8601Format());

                xb_binTempOp.setTimeObject(xb_timePeriod);
                timeType = "TimePeriod";
            }
            else if (specifiedTime instanceof ITimePosition) {
                final ITimePosition oc_timePosition = (ITimePosition) specifiedTime;
                final TimeInstantType xb_timeInstant = TimeInstantType.Factory.newInstance();

                final TimePositionType xb_timePosition = TimePositionType.Factory.newInstance();
                xb_timePosition.setStringValue(oc_timePosition.toISO8601Format());

                xb_timeInstant.setTimePosition(xb_timePosition);

                xb_binTempOp.setTimeObject(xb_timeInstant);
                timeType = "TimePosition";
            }

            final EventTime eventTime = xb_getObs.addNewEventTime();
            eventTime.setTemporalOps(xb_binTempOp);

            // rename elements:
            cursor.dispose();
            cursor = eventTime.newCursor();
            cursor.toChild(new QName("http://www.opengis.net/ogc", "temporalOps"));
            cursor.setName(new QName("http://www.opengis.net/ogc", "TM_Equals"));

            cursor.toChild(new QName("http://www.opengis.net/gml", "_TimeObject"));
            cursor.setName(new QName("http://www.opengis.net/gml", timeType));

            // TODO Spec-Too-Flexible-Problem: for eventTime are several other "temporalOps" possible (not
            // only "TEquals")
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
            final Object[] procedures = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedTypedValueArray(Object[].class);
            xb_getObs.setProcedureArray(objectArrayToStringArray(procedures));
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
            final ParameterShell foiParamShell = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
            if (foiParamShell.hasMultipleSpecifiedValues()) {
                final Object[] fois = foiParamShell.getSpecifiedTypedValueArray(Object[].class);
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray(objectArrayToStringArray(fois));
            }
            else {
                final Object foi = foiParamShell.getSpecifiedValue();
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray(new String[] { (String) foi });
            }
            // TODO Spec-Too-Flexible-Problem: it is also possible that the FeatureOfInterest is specified as
            // a "spatialOps"
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER) != null) {
            final String filter = (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER).getSpecifiedValue();

            final Result resultFilter = xb_getObs.addNewResult();

            try {
                final XmlObject xobj = XmlObject.Factory.parse(filter);
                resultFilter.set(xobj);
            }
            catch (final XmlException e) {
                throw new OXFException(e);
            }
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
            xb_getObs.setResultModel( (new QName("http://www.opengis.net/om/1.0", //http://www.opengis.net/sos/0.0
                                                 (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue())));
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
            final ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
            xb_getObs.setResponseMode(responseModeEnum);
        }

        return xb_getObsDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    /**
     * builds the GetObservationByID-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <ul><li>service</li>
     * <li>version</li>
     * <li>observationid</li>
     * <li>responseFormat</li></ul>
     *
     * <br>
     * <br>
     * The following ones are optional:
     * <li>responseMode</li>
     *
     * @param parameters
     *        parameters of the request
     *
     * @return GetObservationByID-Request in XML-Format as String
     * @throws OXFException
     *
     */
    @Override
    public String buildGetObservationByIDRequest(final ParameterContainer parameters)
    throws OXFException {
        final GetObservationByIdDocument xb_getObsDoc = GetObservationByIdDocument.Factory.newInstance();

        final GetObservationById xb_getObs = xb_getObsDoc.addNewGetObservationById();


//      set required elements:

        xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(SERVICE).getSpecifiedValue());

        xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(VERSION).getSpecifiedValue());

        xb_getObs.setObservationId((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER).getSpecifiedValue());

        xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());


//      set optional elements:

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
            final ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
            xb_getObs.setResponseMode(responseModeEnum);
        }
//      TODO ResultModel?
        return xb_getObsDoc.xmlText(XmlUtil.PRETTYPRINT);
    }


    /**
     * builds a DescribeSensor-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <ul><li>service</li>
     * <li>version</li>
     * <li>sensorID</li></ul>
     *
     * <br>
     * <br>
     * The following are optional:
     * <li>outputFormat</li>
     *
     *
     * @param parameters
     *        parameters and values of the request
     *
     * @return DescribeSensorRequest in XML-Format as String
     */
    @Override
    public String buildDescribeSensorRequest(final ParameterContainer parameters) {

        final DescribeSensorDocument descSensorDoc = DescribeSensorDocument.Factory.newInstance();

        final DescribeSensor descSensor = descSensorDoc.addNewDescribeSensor();

        // set required elements:

        descSensor.setService((String) parameters.getParameterShellWithServiceSidedName(SERVICE).getSpecifiedValue());

        descSensor.setVersion((String) parameters.getParameterShellWithServiceSidedName(VERSION).getSpecifiedValue());

        descSensor.setProcedure((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_PROCEDURE_PARAMETER).getSpecifiedValue());

        if (parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT) != null) {
            descSensor.setOutputFormat((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT).getSpecifiedValue());
        }
        // TODO HACK -> outputformat is hard-coded:
        else {
            descSensor.setOutputFormat("text/xml;subtype=\"sensorML/1.0.1\"");
        }

        return descSensorDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    @Override
    public String buildGetFeatureOfInterestRequest(final ParameterContainer parameters) {

        final GetFeatureOfInterestDocument getFoIDoc = GetFeatureOfInterestDocument.Factory.newInstance();

        final GetFeatureOfInterest getFoI = getFoIDoc.addNewGetFeatureOfInterest();

        // set required elements:

        getFoI.setService((String) parameters.getParameterShellWithServiceSidedName(SERVICE).getSpecifiedValue());

        final ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(VERSION);
        getFoI.setVersion((String) versionPS.getSpecifiedValue());

        // set optional elements:

        if (parameters.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER) != null) {
            final ParameterShell foiIDParamShell = parameters.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER);
            if (foiIDParamShell.hasSingleSpecifiedValue()) {
                final String foiIDParamValue = (String) foiIDParamShell.getSpecifiedValue();
                getFoI.setFeatureOfInterestIdArray(new String[] {foiIDParamValue});
            }
            else {
                final String[] foiIDParamValue = foiIDParamShell.getSpecifiedTypedValueArray(String[].class);
                getFoI.setFeatureOfInterestIdArray(foiIDParamValue);
            }
        }
        else if (parameters.getParameterShellWithServiceSidedName(GET_FOI_LOCATION_PARAMETER) != null) {
            throw new UnsupportedOperationException("The parameter '" + GET_FOI_LOCATION_PARAMETER
                    + "' is not yet supported.");
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_FOI_EVENT_TIME_PARAMETER) != null) {
            throw new UnsupportedOperationException("The parameter '"
                    + GET_FOI_EVENT_TIME_PARAMETER + "' is not yet supported.");
        }

        return getFoIDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    @Override
    public String buildInsertObservationRequest(final ParameterContainer parameters) throws OXFException {
        final InsertObservationDocument insObDoc = InsertObservationDocument.Factory.newInstance();
        final InsertObservation insert = insObDoc.addNewInsertObservation();
        addOperationMetadata(insert,parameters);
        addAssignedSensorId(insert,parameters);
        addObservation(insert,parameters);
        /*
         * Validate before returning -> throw OXFException if validation fails
         */
        doLaxRequestValidation(insObDoc);

        return insObDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    @Override
    public String buildRegisterSensorRequest(final ParameterContainer parameters) throws OXFException{

        final RegisterSensorDocument regSensorDoc = RegisterSensorDocument.Factory.newInstance();
        final RegisterSensor regSensor = regSensorDoc.addNewRegisterSensor();
        addOperationMetadata(regSensor, parameters);
        addSensorDescription(regSensor,parameters);
        addObservationTemplate(regSensor,parameters);
        doLaxRequestValidation(regSensorDoc);
        return regSensorDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    /**
     * Observation Type, e.g. om:MeasurementObservationParameters<br />
     *  om:samplingTime<br />
     *  om:procedure<br />
     *  om:observedProperty<br />
     *  om:featureOfInterest<br />
     *  om:result<br />
     */
    private void addObservation(final InsertObservation insert,
            final ParameterContainer parameters) {
        ObservationType obsType = insert.addNewObservation();
        String observationType = INSERT_OBSERVATION_TYPE_MEASUREMENT;
        try {
            observationType = (String)parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_TYPE).getSpecifiedValue();
        } catch (final Exception e) {
            // ignoring -> value not defined -> using default
        }
        // TODO add more observation types
        if (observationType.equals(INSERT_OBSERVATION_TYPE_MEASUREMENT)){
            // fill in measurement
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION,
                            MeasurementType.type);
        } else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_TEXT)){
            //fill in text
            obsType = (ObservationType) obsType.substitute(
                    XMLConstants.QNAME_OM_1_0_OBSERVATION,
                            ObservationType.type);
        } else if (observationType.equals(INSERT_OBSERVATION_TYPE_COUNT)) {
            // fill in count
            obsType = (ObservationType) obsType.substitute(
                    XMLConstants.QNAME_OM_1_0_OBSERVATION,
                            ObservationType.type);
        } else if (observationType.equals(INSERT_OBSERVATION_TYPE_TRUTH)) {
            // fill in truth
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_OBSERVATION,
                            ObservationType.type);
        } else if (observationType.equals(INSERT_OBSERVATION_TYPE_TEMPORAL)) {
            // fill in temporal
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_TEMPORAL_OBSERVATION,
                            TemporalObservationType.type);
        } else if (observationType.equals(INSERT_OBSERVATION_TYPE_GEOMETRY)) {
            // fill in geometry
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_GEOMETRY_OBSERVATION,
                            GeometryObservationType.type);
        } else if (observationType.equals(INSERT_OBSERVATION_TYPE_COMPLEX)) {
            // fill in complex
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_COMPLEX_OBSERVATION,
                            ComplexObservationType.type);
        }

        // types for varying properties
        /* else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_DISCRETE_COVERAGE)) {
            // fill in discrete coverage
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_DISCRETE_COVERAGE_OBSERVATION,
                            DiscreteCoverageType.type);
        } else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_POINT_COVERAGE)) {
            // fill in point coverage
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_POINT_COVERAGE_OBSERVATION,
                            PointCoverageType.type);
        } else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_TIME_SERIES)) {
            // fill in time series
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_TIME_SERIES_OBSERVATION,
                            TimeseriesType.type);
        } else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_ELEMENT_COVERAGE)) {
            // fill in point coverage
            obsType = (ObservationType) obsType.
                    substitute(XMLConstants.QNAME_OM_1_0_ELEMENT_COVERAGE_OBSERVATION,
                            ElementCoverageType.type);
        }*/


        insert.setObservation(obsType);
        addSamplingTime(obsType,parameters);
        addProcedure(obsType,parameters);
        addObservedProperty(obsType,parameters);
        addFeatureOfInterest(obsType,parameters);
        addResult(obsType,parameters,observationType);
    }

    /**
     * om:result
     */
    private void addResult(final ObservationType obsType,
            final ParameterContainer parameters,
            final String observationType) {
        final XmlObject result = obsType.addNewResult();

        /*
         * if observation is a CategoryObservation
         * and the CodeSpace for the result value is set, add it
         */
        // TODO Check if this is working with validating SOS?
        if(observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_TEXT)){
            final String value = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue();
            final String obsPropId = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue();
            final String time = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
            final String foi = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue();

            final DataArrayDocument doc = DataArrayDocument.Factory.newInstance();
            final DataArrayType arrayType = doc.addNewDataArray1();
            final ElementCount ec = arrayType.addNewElementCount();
            ec.addNewCount().setValue(BigInteger.ONE);
            final DataComponentPropertyType et = arrayType.addNewElementType();
            et.setName("Components");
            final AbstractDataRecordType adr = et.addNewAbstractDataRecord();
            final DataRecordType drt = (DataRecordType) adr.substitute(XMLConstants.QNAME_SWE_1_0_1_DATA_RECORD, DataRecordType.type);
            final DataComponentPropertyType textField1 = drt.addNewField();
            textField1.setName("Time");
            textField1.addNewTime().setDefinition("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
            final DataComponentPropertyType textField2 = drt.addNewField();
            textField2.setName("feature");
            textField2.addNewText().setDefinition("http://www.opengis.net/def/property/OGC/0/FeatureOfInterest");
            final DataComponentPropertyType textField3 = drt.addNewField();
            textField3.setName("resultValue");
            textField3.addNewText().setDefinition(obsPropId);
            final TextBlock block = arrayType.addNewEncoding().addNewTextBlock();
            block.setDecimalSeparator(".");
            block.setTokenSeparator(",");
            block.setBlockSeparator(";");

            final XmlCursor dataArrayCursor = arrayType.addNewValues().newCursor();
            dataArrayCursor.toChild("values");
            dataArrayCursor.toNextToken();
            dataArrayCursor.insertChars(time + "," + foi + "," + value);
            dataArrayCursor.dispose();

            result.set(doc);
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_MEASUREMENT)) {
            final MeasureType mt = MeasureType.Factory.newInstance();

            final ParameterShell ps = parameters.getParameterShellWithCommonName(
                    INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
            if (ps != null) {
                 final String uomCode = ps.getSpecifiedValue().toString();
                 mt.setUom(uomCode);
            }
            mt.setStringValue((String)
                    parameters.getParameterShellWithCommonName(
                            INSERT_OBSERVATION_VALUE_PARAMETER).
                            getSpecifiedValue());
            result.set(mt);
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_COUNT)) {
            final int value = (int) Double.parseDouble((String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue());
            final String obsPropId = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue();
            final String time = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
            final String foi = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue();

            final DataArrayDocument doc = DataArrayDocument.Factory.newInstance();
            final DataArrayType arrayType = doc.addNewDataArray1();
            final ElementCount ec = arrayType.addNewElementCount();
            ec.addNewCount().setValue(BigInteger.ONE);
            final DataComponentPropertyType et = arrayType.addNewElementType();
            et.setName("Components");
            final AbstractDataRecordType adr = et.addNewAbstractDataRecord();
            final DataRecordType drt = (DataRecordType) adr.substitute(XMLConstants.QNAME_SWE_1_0_1_DATA_RECORD, DataRecordType.type);
            final DataComponentPropertyType countField1 = drt.addNewField();
            countField1.setName("Time");
            countField1.addNewTime().setDefinition("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
            final DataComponentPropertyType countField2 = drt.addNewField();
            countField2.setName("feature");
            countField2.addNewText().setDefinition("http://www.opengis.net/def/property/OGC/0/FeatureOfInterest");
            final DataComponentPropertyType countField3 = drt.addNewField();
            countField3.setName("resultValue");
            countField3.addNewCount().setDefinition(obsPropId);
            final TextBlock block = arrayType.addNewEncoding().addNewTextBlock();
            block.setDecimalSeparator(".");
            block.setTokenSeparator(",");
            block.setBlockSeparator(";");

            final XmlCursor dataArrayCursor = arrayType.addNewValues().newCursor();
            dataArrayCursor.toChild("values");
            dataArrayCursor.toNextToken();
            dataArrayCursor.insertChars(time + "," + foi + "," + value);
            dataArrayCursor.dispose();

            result.set(doc);
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_TRUTH)) {
            final String value = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue();
            final String obsPropId = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue();
            final String time = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
            final String foi = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue();

            final DataArrayDocument doc = DataArrayDocument.Factory.newInstance();
            final DataArrayType arrayType = doc.addNewDataArray1();
            final ElementCount ec = arrayType.addNewElementCount();
            ec.addNewCount().setValue(BigInteger.ONE);
            final DataComponentPropertyType et = arrayType.addNewElementType();
            et.setName("Components");
            final AbstractDataRecordType adr = et.addNewAbstractDataRecord();
            final DataRecordType drt = (DataRecordType) adr.substitute(XMLConstants.QNAME_SWE_1_0_1_DATA_RECORD, DataRecordType.type);
            final DataComponentPropertyType booleanField1 = drt.addNewField();
            booleanField1.setName("Time");
            booleanField1.addNewTime().setDefinition("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
            final DataComponentPropertyType booleanField2 = drt.addNewField();
            booleanField2.setName("feature");
            booleanField2.addNewText().setDefinition("http://www.opengis.net/def/property/OGC/0/FeatureOfInterest");
            final DataComponentPropertyType booleanField3 = drt.addNewField();
            booleanField3.setName("resultValue");
            booleanField3.addNewBoolean().setDefinition(obsPropId);
            final TextBlock block = arrayType.addNewEncoding().addNewTextBlock();
            block.setDecimalSeparator(".");
            block.setTokenSeparator(",");
            block.setBlockSeparator(";");

            final XmlCursor dataArrayCursor = arrayType.addNewValues().newCursor();
            dataArrayCursor.toChild("values");
            dataArrayCursor.toNextToken();
            dataArrayCursor.insertChars(time + "," + foi + "," + value);
            dataArrayCursor.dispose();

            result.set(doc);
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_TEMPORAL)) {
            final TemporalObservationType tt = TemporalObservationType.Factory.newInstance();
            logTypeNotImplemented(tt.getClass());
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_GEOMETRY)) {
            final GeometryObservationType gt = GeometryObservationType.Factory.newInstance();
            logTypeNotImplemented(gt.getClass());
        } else if (observationType != null && observationType.equals(INSERT_OBSERVATION_TYPE_COMPLEX)) {
            final ComplexObservationType ct = ComplexObservationType.Factory.newInstance();
            logTypeNotImplemented(ct.getClass());
        }
    }

    private void logTypeNotImplemented(final Class<?> clazz)
    {
        LOGGER.info("Support for type '{}' NOT YET IMPLEMENTED",clazz.getName());
    }

    /**
     * Feature of Interest
     */
    private void addFeatureOfInterest(final ObservationType obsType,
            final ParameterContainer parameters) {
        final FeaturePropertyType featureProp = obsType.addNewFeatureOfInterest();
        final SamplingPointDocument sampPointDoc =
                SamplingPointDocument.Factory.newInstance();
        final SamplingPointType sampPoint = sampPointDoc.addNewSamplingPoint();
        sampPoint.setId((String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue());
        //Code for new Features
        final ParameterShell nameObj =  parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_NEW_FOI_NAME);
        if(nameObj !=null){
            final String name = (String) nameObj.getSpecifiedValue();
            sampPoint.addNewName().setStringValue(name);

            final String desc = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_NEW_FOI_DESC).getSpecifiedValue();
            sampPoint.addNewDescription().setStringValue(desc);

            sampPoint.addNewSampledFeature();

            final PointType type = sampPoint.addNewPosition().addNewPoint();
            type.addNewPos().setStringValue((String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_NEW_FOI_POSITION).getSpecifiedValue());
            final String srsName = (String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_POSITION_SRS).getSpecifiedValue();
            if (srsName != null) {
                type.setSrsName(srsName);
            } else { //default is epsg:4326 (= WGS84)
                type.setSrsName("urn:ogc:def:crs:EPSG::4326");
            }
        }
        //End Code for new Features
        featureProp.set(sampPointDoc);
    }

    /**
     * Observed Property
     */
    private void addObservedProperty(final ObservationType obsType,
            final ParameterContainer parameters) {
        final PhenomenonPropertyType phenProp = obsType.addNewObservedProperty();
        phenProp.setHref((String) parameters.
                getParameterShellWithCommonName(
                        INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).
                        getSpecifiedValue());
    }

    /**
     * Procedure/Sensor
     */
    private void addProcedure(final ObservationType obsType,
            final ParameterContainer parameters) {
        final ProcessPropertyType procedure = obsType.addNewProcedure();
        procedure.setHref((String) parameters.
                getParameterShellWithCommonName(
                        INSERT_OBSERVATION_PROCEDURE_PARAMETER).
                        getSpecifiedValue());
    }

    /**
     * om:samplingTime
     */
    private void addSamplingTime(final ObservationType obsType,
            final ParameterContainer parameters) {
        final TimeObjectPropertyType timeProp = obsType.addNewSamplingTime();
        final AbstractTimeObjectType atot = timeProp.addNewTimeObject();
        final TimeInstantType timeInstant =
                (TimeInstantType) atot.
                substitute(XMLConstants.QNAME_GML_TIMEINSTANT,
                        TimeInstantType.type);
        final TimePositionType timePos = timeInstant.addNewTimePosition();
        final String timeParamValue = (String)parameters.
                getParameterShellWithCommonName(
                        INSERT_OBSERVATION_SAMPLING_TIME).
                        getSpecifiedValue();
        final ITimePosition timePosition = (ITimePosition)TimeFactory.createTime(timeParamValue);
        timePos.setStringValue(timePosition.toISO8601Format());
        timeInstant.setId("t_"+timeInstant.hashCode());
    }

    /**
     * sos:AssignedSensorId
     */
    private void addAssignedSensorId(final InsertObservation insert,
            final ParameterContainer parameters) {
        insert.setAssignedSensorId((String) parameters.getParameterShellWithCommonName(INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());
    }

    /**
     * Operation Metadata: Service type and version
     */
    private void addOperationMetadata(final InsertObservation insert,
            final ParameterContainer parameters) {
        final String version =(String) parameters.getParameterShellWithCommonName(VERSION).getSpecifiedValue();
        insert.setVersion(version);
        final String service =(String) parameters.getParameterShellWithCommonName(SERVICE).getSpecifiedValue();
        insert.setService(service);
    }

    /**
     * TODO Review and make better use of XmlError methods
     * Throws OXFException at the first error that is found
     * @throws OXFException in the case of not valid request
     */
    private void doLaxRequestValidation(final XmlObject xmlDoc) throws OXFException {
        XMLBeansParser.registerLaxValidationCase(GMLAbstractFeatureCase.getInstance());
        XMLBeansParser.registerLaxValidationCase(SASamplingPointCase.getInstance());

        if (xmlDoc instanceof RegisterSensorDocument) {
            XMLBeansParser.registerLaxValidationCase(OfferingInSMLOutputsCase.getInstance());
        }

        /*
         * get errors. if empty, do not throw exception
         */
        final Collection<XmlError> exs = XMLBeansParser.validate(xmlDoc);

        String message = null;
        String parameterName = null;
        for (final XmlError error : exs) {
            // ExceptionCode for Exception
            OwsExceptionCode exCode = null;

            // get name of the missing or invalid parameter
            message = error.getMessage();
            if (message != null) {

                // check, if parameter is missing or value of parameter
                // is
                // invalid to ensure, that correct
                // exceptioncode in exception response is used

                // invalid parameter value
                if (message.startsWith("The value")) {
                    exCode = OwsExceptionCode.InvalidParameterValue;

                    // split message string to get attribute name
                    final String[] messAndAttribute = message
                            .split("attribute '");
                    if (messAndAttribute.length == 2) {
                        parameterName = messAndAttribute[1]
                                .replace("'", "");
                    }
                }

                // invalid enumeration value --> InvalidParameterValue
                else if (message.contains("not a valid enumeration value")) {
                    exCode = OwsExceptionCode.InvalidParameterValue;

                    // get attribute name
                    final String[] messAndAttribute = message.split(" ");
                    parameterName = messAndAttribute[10];
                }

                // mandatory attribute is missing -->
                // missingParameterValue
                else if (message.startsWith("Expected attribute")) {
                    exCode = OwsExceptionCode.MissingParameterValue;

                    // get attribute name
                    final String[] messAndAttribute = message
                            .split("attribute: ");
                    if (messAndAttribute.length == 2) {
                        final String[] attrAndRest = messAndAttribute[1]
                                .split(" in");
                        if (attrAndRest.length == 2) {
                            parameterName = attrAndRest[0];
                        }
                    }
                }

                // mandatory element is missing -->
                // missingParameterValue
                else if (message.startsWith("Expected element")) {
                    exCode = OwsExceptionCode.MissingParameterValue;

                    // get element name
                    final String[] messAndElements = message.split(" '");
                    if (messAndElements.length >= 2) {
                        final String elements = messAndElements[1];
                        if (elements.contains("offering")) {
                            parameterName = "offering";
                        } else if (elements.contains("observedProperty")) {
                            parameterName = "observedProperty";
                        } else if (elements.contains("responseFormat")) {
                            parameterName = "responseFormat";
                        } else if (elements.contains("procedure")) {
                            parameterName = "procedure";
                        } else if (elements.contains("featureOfInterest")) {
                            parameterName = "featureOfInterest";
                        }
                        // TODO check if other elements are invalid
                    }
                }
                // invalidParameterValue
                else if (message.startsWith("Element")) {
                    exCode = OwsExceptionCode.InvalidParameterValue;

                    // get element name
                    final String[] messAndElements = message.split(" '");
                    if (messAndElements.length >= 2) {
                        final String elements = messAndElements[1];
                        if (elements.contains("offering")) {
                            parameterName = "offering";
                        } else if (elements.contains("observedProperty")) {
                            parameterName = "observedProperty";
                        } else if (elements.contains("responseFormat")) {
                            parameterName = "responseFormat";
                        } else if (elements.contains("procedure")) {
                            parameterName = "procedure";
                        } else if (elements.contains("featureOfInterest")) {
                            parameterName = "featureOfInterest";
                        }
                        // TODO check if other elements are invalid
                    }
                } else {
                    // create simple OXFExcpetion exception
                    final String msg = String.format("Error in XMLBeans request validation: %s",
                            message);
                    throw new OXFException(msg);
                }

                // create OXFException with more information exception
                final String msg = String.format("Error in XMLBeans request validation. Causing error: %s. Element name: %s. Message: %s",
                        exCode,
                        parameterName,
                        message);
                throw new OXFException(msg);
            }
        }
    }

    private void addObservationTemplate(final RegisterSensor regSensor,
            final ParameterContainer parameters) throws OXFException {

        ObservationTemplate obsTemp = ObservationTemplate.Factory.newInstance();
        // TODO add method "addParameterShell(String, XmlObject)" to ParameterContainer
        try {
            obsTemp = regSensor.addNewObservationTemplate();
            final ObservationType obsType = ObservationType.Factory.parse((String)parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE).getSpecifiedValue());
            obsTemp.set(obsType);
        } catch (final XmlException e) {
            throw new OXFException("Could not parse observation type from paramter shell.",e);
        }

        regSensor.setObservationTemplate(obsTemp);
    }

    private void addSensorDescription(final RegisterSensor regSensor,
            final ParameterContainer parameters) throws OXFException {
        final SensorDescription sensorDesc = regSensor.addNewSensorDescription();

        SensorMLDocument sensorDescripiton = null;

        try {
            sensorDescripiton = SensorMLDocument.Factory.parse((String) parameters.getParameterShellWithCommonName(REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue());
        } catch (final XmlException e) {
            LOGGER.error(String.format("Exception thrown: %s", e.getMessage()), e);
        }

        sensorDesc.set(sensorDescripiton);
    }

    private void addOperationMetadata(final RegisterSensor regSensor,
            final ParameterContainer parameters) {
        regSensor.setVersion((String) parameters.getParameterShellWithCommonName(VERSION).getSpecifiedValue());
        regSensor.setService((String) parameters.getParameterShellWithCommonName(SERVICE).getSpecifiedValue());
    }

    private String[] objectArrayToStringArray(final Object[] objectArray) {
        final String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            stringArray[i] = (String) objectArray[i];
        }
        return stringArray;
    }

    @Override
    public String buildDeleteSensorRequest(final ParameterContainer parameters) throws OXFException
    {
        final OXFException e = new OXFException("Operation DeleteSensor not supported by SOS 1.0.0");
        LOGGER.error("NOT SUPPORTED:",e);
        throw e;
    }

}