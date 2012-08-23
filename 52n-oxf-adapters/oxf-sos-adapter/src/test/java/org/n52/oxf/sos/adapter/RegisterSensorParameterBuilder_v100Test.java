package org.n52.oxf.sos.adapter;

import static org.junit.Assert.assertEquals;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER;

import java.util.HashMap;

import org.junit.Test;
import org.n52.oxf.xml.XMLConstants;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;


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
		SensorDescriptionBuilder sdb = new SensorDescriptionBuilder();
		sdb.setSensorId("sensorId");
		sdb.setPosition("name", true, 0.0, 1.1);
		sdb.setSensorObservationType(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION);
		sdb.setObservedProperty("observedProperty");
		sdb.setUnitOfMeasurement("uom");
		
		new RegisterSensorParameterBuilder_v100(sdb.generateRegisterSensorDocument(), "template");
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new RegisterSensorParameterBuilder_v100(null, null);
		new RegisterSensorParameterBuilder_v100(new SensorDescriptionBuilder().generateRegisterSensorDocument(), null);
		new RegisterSensorParameterBuilder_v100(null, "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		SensorDescriptionBuilder sdb = new SensorDescriptionBuilder();
		sdb.setSensorId("sensorId");
		sdb.setPosition("name", true, 0.0, 1.1);
		sdb.setSensorObservationType(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION);
		sdb.setObservedProperty("observedProperty");
		sdb.setUnitOfMeasurement("uom");
		
		RegisterSensorParameterBuilder_v100 rspb = new RegisterSensorParameterBuilder_v100
				(sdb.generateRegisterSensorDocument(), "template");
		
		HashMap<String, String> hm = (HashMap<String, String>) rspb.getParameters();
		String parMan_01 = hm.get(REGISTER_SENSOR_ML_DOC_PARAMETER);
		String parMan_02 = hm.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
		
		// String result = "";
		
		// assertEquals(result, parMan_01);
		assertEquals("template", parMan_02);
	}

}
