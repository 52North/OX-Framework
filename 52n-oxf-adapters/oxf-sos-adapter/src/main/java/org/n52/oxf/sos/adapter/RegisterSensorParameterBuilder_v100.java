package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import net.opengis.sensorML.x101.SystemDocument;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doRegisterSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class RegisterSensorParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>(); // set of mandatory parameters
	
	public static RegisterSensorParameterBuilder_v100 createBuilderWithTemplate(SystemDocument sensorDescription, String template, String uom) {
		RegisterSensorParameterBuilder_v100 builder = new RegisterSensorParameterBuilder_v100(sensorDescription, uom);
		builder.parameters.put(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TEMPLATE, template);
		return builder;
	}
	
	public static RegisterSensorParameterBuilder_v100 createBuilderWithType(SystemDocument sensorDescription, String type, String uom) {
		RegisterSensorParameterBuilder_v100 builder = new RegisterSensorParameterBuilder_v100(sensorDescription, uom);
		builder.parameters.put(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE, type);
		return builder;
	}
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorDescription 
	 * @throws IllegalArgumentException
	 */
	private RegisterSensorParameterBuilder_v100(SystemDocument sensorDescription, String uom) throws IllegalArgumentException {
		if (sensorDescription == null)
			throw new IllegalArgumentException("The parameter \"sensorDescription\" is mandatory. It cannot be left empty!");
		parameters.put(ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription.toString());
		parameters.put(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER, uom);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}