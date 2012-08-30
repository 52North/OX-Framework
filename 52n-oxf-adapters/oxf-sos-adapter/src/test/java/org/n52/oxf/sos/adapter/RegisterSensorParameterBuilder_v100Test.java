package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.n52.oxf.OXFException;

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
		SensorDescriptionBuilder sensorDescription = new SensorDescriptionBuilder("sensorId", REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
		sensorDescription.setPosition("name", true, 0.1, 2.3);
		sensorDescription.setObservedProperty("observedProperty");
		sensorDescription.setUnitOfMeasurement("uom");
		String sensorML = sensorDescription.generateSensorDescription();
		
		ObservationTemplateBuilder templateBuilder = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCategory("codeSpace");
		String obsTemp = null;
		try {
			obsTemp = templateBuilder.generateObservationTemplate();
		} catch (OXFException e) {
			e.printStackTrace();
		}
		
		new RegisterSensorParameterBuilder_v100(sensorML, obsTemp);
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		SensorDescriptionBuilder sensorDescription = new SensorDescriptionBuilder("sensorId", REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
		sensorDescription.setPosition("name", true, 0.1, 2.3);
		sensorDescription.setObservedProperty("observedProperty");
		sensorDescription.setUnitOfMeasurement("uom");
		String sensorML = sensorDescription.generateSensorDescription();
		
		ObservationTemplateBuilder templateBuilder = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCategory("codeSpace");
		String obsTemp = null;
		try {
			obsTemp = templateBuilder.generateObservationTemplate();
		} catch (OXFException e) {
			e.printStackTrace();
		}
		
		new RegisterSensorParameterBuilder_v100(null, null);
		new RegisterSensorParameterBuilder_v100(sensorML, null);
		new RegisterSensorParameterBuilder_v100(null, obsTemp);
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		SensorDescriptionBuilder sensorDescription = new SensorDescriptionBuilder("sensorId", REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
		sensorDescription.setPosition("name", true, 0.1, 2.3);
		sensorDescription.setObservedProperty("observedProperty");
		sensorDescription.setUnitOfMeasurement("uom");
		String sensorML = sensorDescription.generateSensorDescription();
		
		ObservationTemplateBuilder templateBuilder = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCategory("codeSpace");
		String obsTemp = null;
		try {
			obsTemp = templateBuilder.generateObservationTemplate();
		} catch (OXFException e) {
			e.printStackTrace();
		}
		
		RegisterSensorParameterBuilder_v100 rspb = new RegisterSensorParameterBuilder_v100(sensorML, obsTemp);
		
		Map<String, String> hm = (HashMap<String, String>) rspb.getParameters();
		String parMan_01 = hm.get(REGISTER_SENSOR_ML_DOC_PARAMETER);
		String parMan_02 = hm.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
		
		// TODO insert comparable data
		// assertEquals(, parMan_01);
		// assertEquals(, parMan_02);
		
		System.out.println(parMan_01);
		System.out.println(parMan_02);
	}

}
