package org.n52.oxf.sos.adapter;

import static org.junit.Assert.*;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.HashMap;

import org.junit.Test;
import org.n52.oxf.xml.XMLConstants;


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
		new InsertObservationParameterBuilder_v100("", new ObservationBuilder(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION));
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new InsertObservationParameterBuilder_v100(null, null);
		new InsertObservationParameterBuilder_v100("", null);
		new InsertObservationParameterBuilder_v100(null, new ObservationBuilder(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION));
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		ObservationBuilder observationBuilder = new ObservationBuilder(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION);
		InsertObservationParameterBuilder_v100 iospb = new InsertObservationParameterBuilder_v100("assignedSensorId", observationBuilder);
		
		HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		String parMan_01 = hm.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
		String parMan_02 = hm.get(INSERT_OBSERVATION_TYPE);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION.toString(), parMan_02);
	}
	
	/**
	 * Checks, whether the optional parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingOptionalParameters() {
		ObservationBuilder observationBuilder = new ObservationBuilder(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION);
		observationBuilder.addParameter(INSERT_OBSERVATION_FOI_ID_PARAMETER, INSERT_OBSERVATION_FOI_ID_PARAMETER);
		observationBuilder.addParameter(INSERT_OBSERVATION_SAMPLING_TIME, INSERT_OBSERVATION_SAMPLING_TIME);
		observationBuilder.addParameter(INSERT_OBSERVATION_NEW_FOI_NAME, INSERT_OBSERVATION_NEW_FOI_NAME);
		observationBuilder.addParameter(INSERT_OBSERVATION_NEW_FOI_DESC, INSERT_OBSERVATION_NEW_FOI_DESC);
		observationBuilder.addParameter(INSERT_OBSERVATION_NEW_FOI_POSITION, INSERT_OBSERVATION_NEW_FOI_POSITION);
		observationBuilder.addParameter(INSERT_OBSERVATION_POSITION_SRS, INSERT_OBSERVATION_POSITION_SRS);
		observationBuilder.addParameter(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		observationBuilder.addParameter(INSERT_OBSERVATION_VALUE_PARAMETER, INSERT_OBSERVATION_VALUE_PARAMETER);
		observationBuilder.addParameter(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE);		
		
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
		String parOpt_09 = hm.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION.toString(), parMan_02);
		assertEquals(INSERT_OBSERVATION_FOI_ID_PARAMETER, parOpt_01);
		assertEquals(INSERT_OBSERVATION_SAMPLING_TIME, parOpt_02);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_NAME, parOpt_03);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_DESC, parOpt_04);
		assertEquals(INSERT_OBSERVATION_NEW_FOI_POSITION, parOpt_05);
		assertEquals(INSERT_OBSERVATION_POSITION_SRS, parOpt_06);
		assertEquals(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parOpt_07);
		assertEquals(INSERT_OBSERVATION_VALUE_PARAMETER, parOpt_08);
		assertEquals(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, parOpt_09);
	}

}
