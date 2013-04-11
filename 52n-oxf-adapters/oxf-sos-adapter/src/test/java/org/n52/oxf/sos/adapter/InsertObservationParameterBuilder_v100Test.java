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

import static org.junit.Assert.assertEquals;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.HashMap;

import org.junit.Test;
import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationBuilder;
import org.n52.oxf.sos.adapter.wrapper.builder.TextObservationBuilder;
import org.n52.oxf.sos.request.observation.TextObservationParameters;


/**
 * Test of correctness for:
 * 		- legal and illegal constructor parameters
 * 		- applying and getting mandatory parameters
 * 
 * @author Eric
 */
public class InsertObservationParameterBuilder_v100Test {
	
	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new InsertObservationParameterBuilder_v100("", ObservationBuilder.createObservationForTypeText());
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new InsertObservationParameterBuilder_v100(null, new TextObservationParameters());
		new InsertObservationParameterBuilder_v100(null, ObservationBuilder.createObservationForTypeText());
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		ObservationBuilder observationBuilder = ObservationBuilder.createObservationForTypeText();
		InsertObservationParameterBuilder_v100 iospb = new InsertObservationParameterBuilder_v100("assignedSensorId", observationBuilder);
		
		HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		String parMan_01 = hm.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
		String parMan_02 = hm.get(INSERT_OBSERVATION_TYPE);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals(INSERT_OBSERVATION_TYPE_TEXT, parMan_02);
	}
	
	/**
	 * Checks, whether the optional parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingOptionalParameters() {
		TextObservationBuilder observationBuilder = ObservationBuilder.createObservationForTypeText();
		observationBuilder.addFoiId(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		observationBuilder.addSamplingTime(INSERT_OBSERVATION_SAMPLING_TIME);
		observationBuilder.addNewFoiName(INSERT_OBSERVATION_NEW_FOI_NAME);
		observationBuilder.addFoiDescription(INSERT_OBSERVATION_NEW_FOI_DESC);
		observationBuilder.addFoiPosition(INSERT_OBSERVATION_NEW_FOI_POSITION);
		observationBuilder.addSrsPosition(INSERT_OBSERVATION_POSITION_SRS);
		observationBuilder.addObservedProperty(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		observationBuilder.addObservationValue(INSERT_OBSERVATION_VALUE_PARAMETER);
		
		InsertObservationParameterBuilder_v100 iospb = new InsertObservationParameterBuilder_v100("assignedSensorId", observationBuilder);
		
		HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		String parMan_01 = hm.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
		String parMan_02 = hm.get(INSERT_OBSERVATION_TYPE);
		String parOpt_01 = hm.get(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		String parOpt_02 = hm.get(INSERT_OBSERVATION_SAMPLING_TIME);
		String parOpt_03 = hm.get(INSERT_OBSERVATION_NEW_FOI_NAME);
		String parOpt_04 = hm.get(INSERT_OBSERVATION_NEW_FOI_DESC);
		String parOpt_05 = hm.get(INSERT_OBSERVATION_NEW_FOI_POSITION);
		String parOpt_06 = hm.get(INSERT_OBSERVATION_POSITION_SRS);
		String parOpt_07 = hm.get(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		String parOpt_08 = hm.get(INSERT_OBSERVATION_VALUE_PARAMETER);
		
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

}
