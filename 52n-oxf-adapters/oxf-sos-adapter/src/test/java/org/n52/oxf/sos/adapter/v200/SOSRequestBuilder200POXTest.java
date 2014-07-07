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
package org.n52.oxf.sos.adapter.v200;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.xml.XMLConstants.*;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sos.x20.GetObservationByIdDocument;
import net.opengis.sos.x20.GetObservationByIdType;
import net.opengis.sos.x20.InsertObservationDocument;
import net.opengis.sos.x20.InsertObservationType;
import net.opengis.sos.x20.SosInsertionMetadataDocument;
import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlString;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.n52.oxf.valueDomains.time.TimePosition;
import org.n52.oxf.xml.XMLConstants;

/**
 * TODO add tests for the following features:
 * <ul><li>InsertObservation with Extensions</li>
 * <li>InsertObservation with OM_SWEArrayObservation</li></ul>
 */
public class SOSRequestBuilder200POXTest {

	private static final String OBSERVATION_ID_2 = "observation-id-2";

	private static final String OBSERVATION_ID_1 = "observation-id-1";

	private static final String SENSOR_IDENTIFIER = "test-identifier";

	private static final String TEST_OBSERVABLE_PROPERTY_2 = "test-observable-property-2";

	private static final String TEST_OBSERVABLE_PROPERTY_1 = "test-observable-property-1";

	private SOSRequestBuilder_200 builder;

	private final String sosVersion = "2.0.0";
	private final String sosService = "SOS";
	private final String format = "test-format";

	/*
	 * INSERT OBSERVATION VALUES
	 */
	private final String offering = "offering-1";
	private final String procedure = "procedure";
	private final String obsProp = "observed-property";
	private final String foiId = "foiId";
	private final String value = "52.0";
	private final String uom = "degreeNorth";
	private ParameterContainer parameters;
	private final ITime phenTime = new TimePosition("1001-11-22T13:37:13.370+00:00");
	private final ITime resultTime = new TimePosition("2002-11-22T13:37:13.370+00:00");

	private final String newFoiName = "foiId-name";
	private final String newFoiPositionString = "52.0 7.5";
	private final String newFoiEpsgCode = "4326";
	private final String newFoiParentFeatureId = "parent-feature-id";

	private final String category = "test-category";

	private final Boolean truth = true;

	private final String text = "text-observation-text";

	private final int count = 52;

	private final ITimePosition phenTimePeriodStart = (ITimePosition)phenTime;
	private final ITimePosition phenTimePeriodEnd = (ITimePosition)resultTime;
	private final TimePeriod phenTimePeriod = new TimePeriod(phenTimePeriodStart,phenTimePeriodEnd);


	/*
	 *
	 * 		INSERT / REGISTER SENSOR
	 *
	 */
		@Test (expected=OXFException.class) public void
	buildRegisterSensor_should_return_OXFException_if_parameters_is_null()
			throws OXFException {
		builder.buildRegisterSensorRequest(null);
	}

	@Test public void
	buildRegisterSensor_should_set_service_and_version()
			 throws OXFException, XmlException {
		createParamConWithMandatoryInsertSensorValues();
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		final String registerSensor = builder.buildRegisterSensorRequest(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getVersion(),is(sosVersion));
		assertThat(insertSensorType.getService(),is(sosService));
	}

	@Test public void
	buildRegisterSensor_should_add_observable_properties()
			throws OXFException, XmlException {
		createParamConWithMandatoryInsertSensorValues();
		/*
		 * 	MULTIPLE OBSERVED PROPERTIES
		 */
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1,
				TEST_OBSERVABLE_PROPERTY_2);

		String registerSensor = builder.buildRegisterSensorRequest(parameters);
		InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getObservablePropertyArray().length, is(2));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_1));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_2));
		/*
		 * SINGLE OBSERVED PROPERTY
		 */
		parameters.removeParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER);
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		registerSensor = builder.buildRegisterSensorRequest(parameters);
		insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getObservablePropertyArray().length, is(1));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_1));
	}

	@Test public void
	buildRegisterSensor_should_set_procedure_description_format()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		final String registerSensor = builder.buildRegisterSensorRequest(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getProcedureDescriptionFormat(),is(format));
	}

	@Test public void
	buildRegisterSensor_should_set_procedure_description()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		final String registerSensor = builder.buildRegisterSensorRequest(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		final String identifierFromRequestBuilder = SystemType.Factory.parse(
				SensorMLDocument.Factory.parse(insertSensorType.getProcedureDescription().toString())
						.getSensorML()
						.getMemberArray(0)
						.getProcess().xmlText()
						)
						.getIdentificationArray(0)
						.getIdentifierList()
						.getIdentifierArray(0)
						.getTerm()
						.getValue();

		assertThat(identifierFromRequestBuilder,is(SENSOR_IDENTIFIER));
	}

	@Test public void
	buildRegisterSensor_should_throw_exception_if_receiving_invalid_procedure_description()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		parameters.removeParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, "INVALID");

		try {
			builder.buildRegisterSensorRequest(parameters);
		}
		catch (final Exception e) {
			assertThat(e,is(instanceOf(OXFException.class)));
			assertThat(e.getCause(),(is(instanceOf(XmlException.class))));
			assertThat(e.getMessage(),is("Error while parsing MANDATORY parameter 'procedure description'!"));
		}
	}

	@Test public void
	buildRegisterSenosr_should_set_insertion_metadata()
			throws OXFException, XmlException{
		/*
		 * MULTIPLE FEATURE AND OBSERVATION TYPES
		 */
		final String obsType1 = "observation-type-1";
		final String obsType2 = "observation-type-2";
		final String foiType1 = "feature-type-1";
		final String foiType2 = "feature-type-2";
		createParamConWithMandatoryInsertSensorValues();
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVATION_TYPE, obsType1,obsType2);
		parameters.addParameterShell(REGISTER_SENSOR_FEATURE_TYPE_PARAMETER, foiType1,foiType2);
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		String registerSensor = builder.buildRegisterSensorRequest(parameters);
		InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getMetadataArray().length,is(1));

		SosInsertionMetadataType insertionMetadata = SosInsertionMetadataDocument.Factory.parse(insertSensorType.getMetadataArray()[0].xmlText()).getSosInsertionMetadata();

		assertThat(insertionMetadata.getObservationTypeArray().length,is(2));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray().length,is(2));
		assertThat(insertionMetadata.getObservationTypeArray(),hasItemInArray(obsType1));
		assertThat(insertionMetadata.getObservationTypeArray(),hasItemInArray(obsType2));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray(),hasItemInArray(foiType1));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray(),hasItemInArray(foiType2));
		/*
		 * SINGLE FEATURE AND OBSERVATION TYPE
		 */
		parameters = new ParameterContainer();
		createParamConWithMandatoryInsertSensorValues();
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVATION_TYPE, obsType1);
		parameters.addParameterShell(REGISTER_SENSOR_FEATURE_TYPE_PARAMETER, foiType1);
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1);

		registerSensor = builder.buildRegisterSensorRequest(parameters);
		insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();

		assertThat(insertSensorType.getMetadataArray().length,is(1));

		insertionMetadata = SosInsertionMetadataDocument.Factory.parse(insertSensorType.getMetadataArray()[0].xmlText()).getSosInsertionMetadata();

		assertThat(insertionMetadata.getObservationTypeArray().length,is(1));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray().length,is(1));
		assertThat(insertionMetadata.getObservationTypeArray(),hasItemInArray(obsType1));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray(),hasItemInArray(foiType1));
	}

	/*
	 *
	 * 		GET OBSERVATION BY ID
	 *
	 */
	@Test(expected=OXFException.class) public void
	buildGetObservationByIdRequest_should_throw_OXFException_if_parameter_is_null()
			throws OXFException {
		builder.buildGetObservationByIDRequest(null);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test public void
	buildGetObservatinByIdRequest_should_throw_OXFException_if_observation_id_is_missing()
			throws OXFException {
		thrown.expect(OXFException.class);
		thrown.expectMessage("Parameter 'GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER' is mandatory!");
		addServiceAndVersion();
		builder.buildGetObservationByIDRequest(parameters);
	}

	@Test public void
	buildGetObservationById_should_add_single_and_multiple_observation_id()
		throws OXFException, XmlException {
		addServiceAndVersion();
		parameters.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, OBSERVATION_ID_1);

		String getObservationById = builder.buildGetObservationByIDRequest(parameters);

		GetObservationByIdType getObservationByIdType = GetObservationByIdDocument.Factory.parse(getObservationById).getGetObservationById();

		assertThat(getObservationByIdType.getService(), is(sosService));
		assertThat(getObservationByIdType.getVersion(), is(sosVersion));
		assertThat(getObservationByIdType.getObservationArray().length, is(1));
		assertThat(getObservationByIdType.getObservationArray(0), is(OBSERVATION_ID_1));

		parameters.removeParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER);
		parameters.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, OBSERVATION_ID_1, OBSERVATION_ID_2);

		getObservationById = builder.buildGetObservationByIDRequest(parameters);

		getObservationByIdType = GetObservationByIdDocument.Factory.parse(getObservationById).getGetObservationById();

		assertThat(getObservationByIdType.getService(), is(sosService));
		assertThat(getObservationByIdType.getVersion(), is(sosVersion));
		assertThat(getObservationByIdType.getObservationArray().length, is(2));
		assertThat(getObservationByIdType.getObservationArray(), hasItemInArray(OBSERVATION_ID_1));
		assertThat(getObservationByIdType.getObservationArray(), hasItemInArray(OBSERVATION_ID_2));
	}

	/*
	 *
	 * 		INSERT OBSERVATION
	 *
	 */
	@Test(expected=OXFException.class) public void
	buildInsertObservation_should_throw_OXFException_if_parameters_is_null()
			throws OXFException {
		builder.buildInsertObservationRequest(null);
	}

	@Test public void
	buildInsertObservation_should_set_service_and_version()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final InsertObservationType insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();

		assertThat(insertObservationType.getVersion(),is(sosVersion));
		assertThat(insertObservationType.getService(),is(sosService));
	}

	@Test public void
	buildInsertObservation_should_add_offerings()
			throws XmlException, OXFException {
		/*
		 * MULTIPLE OFFERINGS
		 */
		addServiceAndVersion();
		addObservationValues();
		final String offering1 = "test-offering-1";
		final String offering2 = "test-offering-2";
		parameters.removeParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER);
		parameters.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER,offering1,offering2);

		String insertObservation = builder.buildInsertObservationRequest(parameters);
		InsertObservationType insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();

		assertThat(insertObservationType.getOfferingArray().length, is(2));
		assertThat(insertObservationType.getOfferingArray(),hasItemInArray(offering1));
		assertThat(insertObservationType.getOfferingArray(),hasItemInArray(offering2));
		/*
		 * SINGLE OFFERING
		 */
		parameters = new ParameterContainer();
		addServiceAndVersion();
		addObservationValues();
		parameters.removeParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER);
		parameters.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER,offering1);

		insertObservation = builder.buildInsertObservationRequest(parameters);
		insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();

		assertThat(insertObservationType.getOfferingArray().length, is(1));
		assertThat(insertObservationType.getOfferingArray(),hasItemInArray(offering1));
	}

	@Test public void
	buildInsertObservation_should_add_single_measurement_with_foi_ref()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final InsertObservationType insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();

		assertThat(insertObservationType.getOfferingArray().length,is(1));
		assertThat(insertObservationType.getOfferingArray(0),is(offering));
		assertThat(insertObservationType.getObservationArray().length,is(1));

		final OMObservationType observation = insertObservationType.getObservationArray(0).getOMObservation();
		assertThat(observation.getId(),not(isEmptyOrNullString()));
		assertThat(observation.getType().getHref(),is("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement"));
		assertThat(((TimeInstantType)observation.getPhenomenonTime().getAbstractTimeObject()).getTimePosition().getStringValue(),is(phenTime.toISO8601Format())); // phentime
		assertThat(observation.getPhenomenonTime().getAbstractTimeObject().getId(),is("phenomenonTime"));
		assertThat(observation.getResultTime().getTimeInstant().getTimePosition().getStringValue(),is(resultTime.toISO8601Format()));// res time
		assertThat(observation.getResultTime().getTimeInstant().getId(),is("resultTime"));
		assertThat(observation.getProcedure().getHref(),is(procedure)); // proc
		assertThat(observation.getObservedProperty().getHref(),is(obsProp)); // obsProp
		assertThat(observation.getFeatureOfInterest().getHref(),is(foiId)); // foiId

		final MeasureType result = (MeasureType) observation.getResult();
		assertThat(result.getStringValue(),is(value)); // result value
		assertThat(result.getUom(),is(uom)); // result uom
	}

	@Test(expected=OXFException.class) public void
	buildInsertObservation_should_throws_Exception_because_of_invalid_observation_type()
			throws OXFException	{
		addServiceAndVersion();
		addObservationValues();
		parameters.removeParameterShell(INSERT_OBSERVATION_TYPE);
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, "INVALID");

		builder.buildInsertObservationRequest(parameters);
	}

	@Test public void
	buildInsertObservation_shoul_add_feature_instance()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();
		addNewFoiValues();

		String insertObservation = builder.buildInsertObservationRequest(parameters);
		SFSpatialSamplingFeatureType feature = SFSpatialSamplingFeatureDocument.Factory.parse(InsertObservationDocument.Factory.parse(insertObservation)
        		.getInsertObservation()
        		.getObservationArray(0)
        		.getOMObservation()
        		.getFeatureOfInterest()
        		.xmlText())
        		.getSFSpatialSamplingFeature();

		assertThat(feature.getId(),not(isEmptyOrNullString()));
		assertThat(feature.getIdentifier().getStringValue(),is(foiId));
		assertThat(feature.getNameArray(0).getStringValue(),is(newFoiName));
		assertThat(feature.getType().getHref(),is(OGC_OM_2_0_SF_SAMPLING_POINT));
		assertThat(feature.getSampledFeature().getHref(),is(newFoiParentFeatureId));

		final DirectPositionType pos = ((PointType)feature.getShape().getAbstractGeometry()).getPos();
		assertThat(pos.getSrsName(),endsWith(newFoiEpsgCode));
		assertThat(pos.getSrsName(),startsWith(OGC_URI_START_CRS));
		assertThat(pos.getStringValue(),is(newFoiPositionString));
		/*
		 * 2nd to test if parent feature id is set correct if not given
		 */
		parameters.removeParameterShell(INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID);
		insertObservation = builder.buildInsertObservationRequest(parameters);
		feature = SFSpatialSamplingFeatureDocument.Factory.parse(InsertObservationDocument.Factory.parse(insertObservation)
        		.getInsertObservation()
        		.getObservationArray(0)
        		.getOMObservation()
        		.getFeatureOfInterest()
        		.xmlText())
        		.getSFSpatialSamplingFeature();

		assertThat(feature.getSampledFeature().getHref(),is(OGC_UNKNOWN_VALUE));
	}

	@Test public void
	buildInsertObservation_should_add_single_category_observation()
			throws OXFException, XmlException{
		addServiceAndVersion();
		addObservationValues();
		removeObservationTypeAndMeasurementResult();
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_CATEGORY);
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,category);

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final OMObservationType observation = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation().getObservationArray(0).getOMObservation();

		assertThat(observation.getType().getHref(),is(OGC_OM_2_0_OM_CATEGORY_OBSERVATION));

		final ReferenceType result = ReferenceType.Factory.parse(observation.getResult().xmlText());
		assertThat(result.getHref(),is(category));
	}

	@Test public void
	buildInsertObservation_should_add_single_truth_observation()
			throws OXFException, XmlException{
		addServiceAndVersion();
		addObservationValues();
		removeObservationTypeAndMeasurementResult();

		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TRUTH);
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,Boolean.toString(truth));

		String insertObservation = builder.buildInsertObservationRequest(parameters);
		OMObservationType observation = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation().getObservationArray(0).getOMObservation();

		assertThat(observation.getType().getHref(),is(XMLConstants.OGC_OM_2_0_OM_TRUTH_OBSERVATION));

		XmlBoolean result = XmlBoolean.Factory.parse(observation.getResult().xmlText());
		assertThat(result.getBooleanValue(),is(truth));

		parameters.removeParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER);
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,"someStringNotTrue");

		insertObservation = builder.buildInsertObservationRequest(parameters);
		observation = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation().getObservationArray(0).getOMObservation();

		assertThat(observation.getType().getHref(),is(XMLConstants.OGC_OM_2_0_OM_TRUTH_OBSERVATION));

		result = XmlBoolean.Factory.parse(observation.getResult().xmlText());
		assertThat(result.getBooleanValue(),is(false));
	}

	@Test public void
	buildInsertObservation_should_add_single_text_observation()
			throws OXFException, XmlException{
		addServiceAndVersion();
		addObservationValues();
		removeObservationTypeAndMeasurementResult();
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TEXT);
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,text);

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final OMObservationType observation = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation().getObservationArray(0).getOMObservation();

		assertThat(observation.getType().getHref(),is(OGC_OM_2_0_OM_TEXT_OBSERVATION));

		final XmlString result = XmlString.Factory.parse(observation.getResult().xmlText());
		assertThat(result.getStringValue(),is(text));
	}

	@Test public void
	buildInsertObservation_should_add_single_count_observation()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();
		removeObservationTypeAndMeasurementResult();
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_COUNT);
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER,count );

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final OMObservationType observation = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation().getObservationArray(0).getOMObservation();

		assertThat(observation.getType().getHref(),is(OGC_OM_2_0_OM_COUNT_OBSERVATION));

		final XmlInteger result = XmlInteger.Factory.parse(observation.getResult().xmlText());
		assertThat(result.getBigIntegerValue().intValue(),is(count));
	}

	@Test public void
	buildInsertObservation_should_add_single_measurement_with_timePeriod_as_phenomenonTime()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();
		parameters.removeParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME);
		parameters.addParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME,phenTimePeriod);

		final String insertObservation = builder.buildInsertObservationRequest(parameters);
		final TimePeriodType phenomenonTime = (TimePeriodType)InsertObservationDocument.Factory.parse(insertObservation)
				.getInsertObservation()
				.getObservationArray(0)
				.getOMObservation()
				.getPhenomenonTime()
				.getAbstractTimeObject();

		assertThat(phenomenonTime.getBeginPosition().getStringValue(),is(phenTimePeriodStart.toISO8601Format()));
		assertThat(phenomenonTime.getEndPosition().getStringValue(),is(phenTimePeriodEnd.toISO8601Format()));
	}

	private void removeObservationTypeAndMeasurementResult()
	{
		parameters.removeParameterShell(INSERT_OBSERVATION_TYPE);
		parameters.removeParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
		parameters.removeParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER);
	}

	private void addNewFoiValues() throws OXFException
	{
		parameters.removeParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		parameters.addParameterShell(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER,foiId);
		parameters.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME,newFoiName);
		parameters.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION,newFoiPositionString );
		parameters.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS,newFoiEpsgCode );
		parameters.addParameterShell(INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID,newFoiParentFeatureId);
	}

	private void addObservationValues() throws OXFException
	{
		parameters.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER, offering);
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
		parameters.addParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME, phenTime ); // phentime
		parameters.addParameterShell(INSERT_OBSERVATION_RESULT_TIME, resultTime ); // res time
		parameters.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, procedure);// proc
		parameters.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, obsProp);// obsProp
		parameters.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, foiId);// foiId
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, value);// result
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);// uom
	}

	private String createSensorDescription()
	{
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		builder.setIdentifierUniqeId(SENSOR_IDENTIFIER);
		return builder.buildSensorDescription();
	}

	private void createParamConWithMandatoryInsertSensorValues() throws OXFException
	{
		addServiceAndVersion();
		parameters.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER, format);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, createSensorDescription());
	}

	private void addServiceAndVersion() throws OXFException
	{
		parameters.addParameterShell(SERVICE, sosService);
		parameters.addParameterShell(VERSION, sosVersion);
	}

	@Before
	public void init() {
		builder = new SOSRequestBuilder_200();
		parameters = new ParameterContainer();
	}
}
