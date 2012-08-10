package org.n52.oxf.sos.adapter;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

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
		new InsertObservationParameterBuilder_v100("", "");
		new InsertObservationParameterBuilder_v100("assignedSensorId", "observation");
		new InsertObservationParameterBuilder_v100("assignedSensorId", "");
		new InsertObservationParameterBuilder_v100("", "observation");
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new InsertObservationParameterBuilder_v100(null, null);
		new InsertObservationParameterBuilder_v100("", null);
		new InsertObservationParameterBuilder_v100(null, "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		InsertObservationParameterBuilder_v100 iospb = new InsertObservationParameterBuilder_v100
				("assignedSensorId", "observation");
		
		HashMap<String, String> hm = (HashMap<String, String>) iospb.getParameters();
		String parMan_01 = hm.get(ISOSRequestBuilder.INSERT_OBSERVATION_SENSOR_ID_PARAMETER);
		String parMan_02 = hm.get(SOSWrapper.INSERT_OBSERVATION_OBSERVATION_PARAMETER);
		
		assertEquals("assignedSensorId", parMan_01);
		assertEquals("observation", parMan_02);
	}

}
