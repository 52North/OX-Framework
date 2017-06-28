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
package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;


/**
 * Test of correctness for:
 * 		- legal and illegal constructor parameters
 * 		- applying and getting mandatory parameters
 * 
 * @author Eric
 */
public class InsertObservationParametersTest {
	
	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new InsertObservationParameters("", ObservationBuilder.createObservationForTypeText());
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new InsertObservationParameters(null, new TextObservationParameters());
		new InsertObservationParameters(null, ObservationBuilder.createObservationForTypeText());
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		final ObservationBuilder observationBuilder = ObservationBuilder.createObservationForTypeText();
		final InsertObservationParameters iospb = new InsertObservationParameters("assignedSensorId", observationBuilder);
		
		final HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		final String parMan_01 = hm.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
		final String parMan_02 = hm.get(INSERT_OBSERVATION_TYPE);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals(INSERT_OBSERVATION_TYPE_TEXT, parMan_02);
	}
	
	/**
	 * Checks, whether the optional parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingOptionalParameters() {
		final TextObservationBuilder observationBuilder = ObservationBuilder.createObservationForTypeText();
		observationBuilder.addFoiId(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		observationBuilder.addSamplingTime(INSERT_OBSERVATION_SAMPLING_TIME);
		observationBuilder.addNewFoiName(INSERT_OBSERVATION_NEW_FOI_NAME);
		observationBuilder.addFoiDescription(INSERT_OBSERVATION_NEW_FOI_DESC);
		observationBuilder.addFoiPosition(INSERT_OBSERVATION_NEW_FOI_POSITION);
		observationBuilder.addSrsPosition(INSERT_OBSERVATION_POSITION_SRS);
		observationBuilder.addObservedProperty(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		observationBuilder.addObservationValue(INSERT_OBSERVATION_VALUE_PARAMETER);
		
		final InsertObservationParameters iospb = new InsertObservationParameters("assignedSensorId", observationBuilder);
		
		final HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		final String parMan_01 = hm.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
		final String parMan_02 = hm.get(INSERT_OBSERVATION_TYPE);
		final String parOpt_01 = hm.get(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		final String parOpt_02 = hm.get(INSERT_OBSERVATION_SAMPLING_TIME);
		final String parOpt_03 = hm.get(INSERT_OBSERVATION_NEW_FOI_NAME);
		final String parOpt_04 = hm.get(INSERT_OBSERVATION_NEW_FOI_DESC);
		final String parOpt_05 = hm.get(INSERT_OBSERVATION_NEW_FOI_POSITION);
		final String parOpt_06 = hm.get(INSERT_OBSERVATION_POSITION_SRS);
		final String parOpt_07 = hm.get(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		final String parOpt_08 = hm.get(INSERT_OBSERVATION_VALUE_PARAMETER);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals(INSERT_OBSERVATION_TYPE_TEXT, parMan_02);
		assertEquals(INSERT_OBSERVATION_FOI_ID_PARAMETER, parOpt_01);
		assertEquals(INSERT_OBSERVATION_SAMPLING_TIME, parOpt_02);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_NAME, parOpt_03);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_DESC, parOpt_04);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_POSITION, parOpt_05);
		assertEquals(INSERT_OBSERVATION_POSITION_SRS, parOpt_06);
		assertEquals(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parOpt_07);
		assertEquals(INSERT_OBSERVATION_VALUE_PARAMETER, parOpt_08);
	}
	
	@Test
	public void should_return_parameters_map_key_and_value_unmodified()
	{
		final ObservationParameters obsParameter = new MeasurementObservationParameters();
		final String foiId = "my-feature-of-interest";
		obsParameter.addFoiId(foiId);
		final String sensorId = "my-sensor-id";
		final InsertObservationParameters builder = new InsertObservationParameters(sensorId, obsParameter);
		final Map<String, String> builderParameters = builder.getParameters();
		
		assertThat(builderParameters, hasKey(INSERT_OBSERVATION_FOI_ID_PARAMETER));
		assertThat(builderParameters, hasEntry(INSERT_OBSERVATION_FOI_ID_PARAMETER,foiId));
		assertThat(builderParameters, hasKey(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
		assertThat(builderParameters, hasEntry(INSERT_OBSERVATION_PROCEDURE_PARAMETER,sensorId));
	}

}
