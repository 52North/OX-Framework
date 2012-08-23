package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import net.opengis.sensorML.x101.SystemDocument;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doRegisterSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class RegisterSensorParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorDescription 
	 * @throws IllegalArgumentException
	 */
	public RegisterSensorParameterBuilder_v100(SystemDocument sensorDescription, String observationTemplate) throws IllegalArgumentException {
		if (sensorDescription == null || observationTemplate == null) {
			throw new IllegalArgumentException("The parameters \"sensorDescription\" and \"template\" are mandatory. They cannot be left empty!");
		}
		parameters.put(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription.toString());
		parameters.put(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}