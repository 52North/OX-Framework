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
public class RegisterSensorParameterBuilder_v100Test {
	
	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new RegisterSensorParameterBuilder_v100("", "");
		new RegisterSensorParameterBuilder_v100("sensorDescription", "observationTemplate");
		new RegisterSensorParameterBuilder_v100("sensorDescription", "");
		new RegisterSensorParameterBuilder_v100("", "observationTemplate");
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new RegisterSensorParameterBuilder_v100(null, null);
		new RegisterSensorParameterBuilder_v100("", null);
		new RegisterSensorParameterBuilder_v100(null, "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		RegisterSensorParameterBuilder_v100 rspb = new RegisterSensorParameterBuilder_v100
				("sensorDescription", "observationTemplate");
		
		HashMap<String, String> hm = (HashMap<String, String>) rspb.getParameters();
		String parMan_01 = hm.get(SOSWrapper.REGISTER_SENSOR_SENSOR_DESCRIPTION_PARAMETER);
		String parMan_02 = hm.get(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TEMPLATE);
		
		assertEquals("sensorDescription", parMan_01);
		assertEquals("observationTemplate", parMan_02);
	}

}
