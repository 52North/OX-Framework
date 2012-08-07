package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doRegisterSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class RegisterSensorParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>(); // set of mandatory parameters
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorDescription 
	 * @param observationTemplate
	 * @throws IllegalArgumentException
	 */
	public RegisterSensorParameterBuilder_v100(String sensorDescription, String observationTemplate) throws IllegalArgumentException {
		if (sensorDescription == null || observationTemplate == null)
			throw new IllegalArgumentException("The parameters \"sensorDescription\" and \"observationTemplate\" are mandatory. They cannot be left empty!");
		parameters.put(SOSWrapper.REGISTER_SENSOR_SENSOR_DESCRIPTION_PARAMETER, sensorDescription);
		parameters.put(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}
