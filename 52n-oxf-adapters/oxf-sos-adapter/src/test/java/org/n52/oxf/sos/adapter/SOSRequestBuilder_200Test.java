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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sos.x20.InsertObservationDocument;
import net.opengis.sos.x20.InsertObservationType;
import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.oxf.valueDomains.time.TimePosition;

public class SOSRequestBuilder_200Test {

	/**
	 * 
	 */
	private static final String SENSOR_IDENTIFIER = "test-identifier";

	private static final String TEST_OBSERVABLE_PROPERTY_2 = "test-observable-property-2";

	private static final String TEST_OBSERVABLE_PROPERTY_1 = "test-observable-property-1";

	private SOSRequestBuilder_200 builder;
	
	private final String sosVersion = "test-version";
	private final String sosService = "test-service";
	private final String format = "test-format";
	
	/*
	 * INSERT OBSERVATION VALUES
	 */
	private final String offering = "offering-1";
	private final String procedure = "procedure";
	private final String obsProp = "observed-property";
	private final String foi = "foiId";
	private final String value = "52.0";
	private final String uom = "degreeNorth";
	private ParameterContainer parameters;
	private final ITime phenTime = new TimePosition("1001-11-22T13:37:13.370+00:00");
	private final ITime resultTime = new TimePosition("2002-11-22T13:37:13.370+00:00");

	
	/*
	 * 
	 * 		INSERT / REGISTER SENSOR
	 * 
	 */
		@Test (expected=OXFException.class) public void 
	buildRegisterSensor_should_return_OXFException_if_parameters_is_null()
			throws OXFException {
		builder.buildRegisterSensor(null);
	}
	
	@Test public void 
	buildRegisterSensor_should_set_service_and_version()
			 throws OXFException, XmlException {
		createParamConWithMandatoryInsertSensorValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getVersion(),is(sosVersion));
		assertThat(insertSensorType.getService(),is(sosService));
	}
	
	@Test public void
	buildRegisterSensor_should_add_observable_properties()
			throws OXFException, XmlException {
		createParamConWithMandatoryInsertSensorValues();
		
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1,
				TEST_OBSERVABLE_PROPERTY_2);
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getObservablePropertyArray().length, is(2));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_1));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_2));
	}
	
	@Test public void
	buildRegisterSensor_should_set_procedure_description_format()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getProcedureDescriptionFormat(),is(format));
	}
	
	@Test public void
	buildRegisterSensor_should_set_procedure_description()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getProcedureDescription().toString(),is(createSensorDescription()));
	}
	
	@Test public void
	buildRegisterSensor_should_throw_exception_if_receiving_invalid_procedure_description()
			throws XmlException, OXFException {
		createParamConWithMandatoryInsertSensorValues();
		parameters.removeParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, "INVALID");
		
		try {
			builder.buildRegisterSensor(parameters);
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
		
		String registerSensor = builder.buildRegisterSensor(parameters);
		InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getMetadataArray().length,is(1));
		
		SosInsertionMetadataType insertionMetadata = (SosInsertionMetadataType)insertSensorType.getMetadataArray(0).getInsertionMetadata();
		
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
		
		registerSensor = builder.buildRegisterSensor(parameters);
		insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getMetadataArray().length,is(1));
		
		insertionMetadata = (SosInsertionMetadataType)insertSensorType.getMetadataArray(0).getInsertionMetadata();
		
		assertThat(insertionMetadata.getObservationTypeArray().length,is(1));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray().length,is(1));
		assertThat(insertionMetadata.getObservationTypeArray(),hasItemInArray(obsType1));
		assertThat(insertionMetadata.getFeatureOfInterestTypeArray(),hasItemInArray(foiType1));
	}
	
	/*
	 * 
	 * 		INSERT OBSERVATION
	 * 
	 */
	@Test(expected=OXFException.class) public void
	buildInsertObservation_should_throw_OXFException_if_parameters_is_null()
			throws OXFException, XmlException {
		builder.buildInsertObservation(null);
	}
	
	@Test public void
	buildInsertObservation_should_set_service_and_version()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();
		
		final String insertObservation = builder.buildInsertObservation(parameters);
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
		
		String insertObservation = builder.buildInsertObservation(parameters);
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
		
		insertObservation = builder.buildInsertObservation(parameters);
		insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();
		
		assertThat(insertObservationType.getOfferingArray().length, is(1));
		assertThat(insertObservationType.getOfferingArray(),hasItemInArray(offering1));
	}
	
	@Test public void
	buildInsertObservation_should_add_single_measurement_with_foi_ref()
			throws OXFException, XmlException {
		addServiceAndVersion();
		addObservationValues();
		
		final String insertObservation = builder.buildInsertObservation(parameters);
		final InsertObservationType insertObservationType = InsertObservationDocument.Factory.parse(insertObservation).getInsertObservation();
		
		assertThat(insertObservationType.getOfferingArray().length,is(1));
		assertThat(insertObservationType.getOfferingArray(0),is(offering));
		assertThat(insertObservationType.getObservationArray().length,is(1));
		
		final OMObservationType observation = insertObservationType.getObservationArray(0).getOMObservation();
		assertThat(observation.getType().getHref(),is("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement"));
		assertThat(((TimeInstantType)observation.getPhenomenonTime().getAbstractTimeObject()).getTimePosition().getStringValue(),is(phenTime.toISO8601Format())); // TODO phentime
		assertThat(observation.getPhenomenonTime().getAbstractTimeObject().getId(),is("phenomenonTime"));
		assertThat(observation.getResultTime().getTimeInstant().getTimePosition().getStringValue(),is(resultTime.toISO8601Format()));// TODO res time
		assertThat(observation.getResultTime().getTimeInstant().getId(),is("resultTime"));
		assertThat(observation.getProcedure().getHref(),is(procedure)); // proc
		assertThat(observation.getObservedProperty().getHref(),is(obsProp)); // obsProp
		assertThat(observation.getFeatureOfInterest().getHref(),is(foi)); // foi

		final MeasureType result = (MeasureType) observation.getResult();
		assertThat(result.getStringValue(),is(value)); // result value
		assertThat(result.getUom(),is(uom)); // result uom
//		fail("IMPL NOT FINISHED");
	}

	private void addObservationValues() throws OXFException
	{
		parameters.addParameterShell(INSERT_OBSERVATION_OFFERINGS_PARAMETER, offering);
		parameters.addParameterShell(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
		parameters.addParameterShell(INSERT_OBSERVATION_PHENOMENON_TIME, phenTime ); // phentime
		parameters.addParameterShell(INSERT_OBSERVATION_RESULT_TIME, resultTime ); // res time
		parameters.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, procedure);// proc
		parameters.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, obsProp);// obsProp
		parameters.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, foi);// foi
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, value);// result
		parameters.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);// uom
	}
	
	private String createSensorDescription()
	{
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		builder.setIdentifierUniqeId(SENSOR_IDENTIFIER);
		final SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
		sensorMLDocument.addNewSensorML().addNewMember().set(builder.buildSensorDescription());
		sensorMLDocument.getSensorML().setVersion("1.0.1");
		return sensorMLDocument.toString();
	}

	private void createParamConWithMandatoryInsertSensorValues() throws OXFException
	{
		addServiceAndVersion();
		parameters.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER, format);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, createSensorDescription());
	}

	private void addServiceAndVersion() throws OXFException
	{
		parameters.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, sosService);
		parameters.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, sosVersion);
	}
	
	@Before
	public void init() {
		builder = new SOSRequestBuilder_200();
		parameters = new ParameterContainer();
	}
}
