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
public class DescribeSensorParameterBuilder_v100Test {

	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new DescribeSensorParamterBuilder_v100("", "");
		new DescribeSensorParamterBuilder_v100("sensorId", "outputFormat");
		new DescribeSensorParamterBuilder_v100("sensorId", "");
		new DescribeSensorParamterBuilder_v100("", "outputFormat");
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new DescribeSensorParamterBuilder_v100(null, null);
		new DescribeSensorParamterBuilder_v100("", null);
		new DescribeSensorParamterBuilder_v100(null, "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		DescribeSensorParamterBuilder_v100 dspb = new DescribeSensorParamterBuilder_v100
				("sensorId", DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML);
		
		HashMap<String, String> hm = (HashMap<String, String>) dspb.getParameters();
		String parMan_01 = hm.get(SOSWrapper.DESCRIBE_SENSOR_SENSOR_ID_PARAMETER);
		String parMan_02 = hm.get(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT);
		
		assertEquals("sensorId", parMan_01);
		assertEquals(DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML, parMan_02);
	}

}
