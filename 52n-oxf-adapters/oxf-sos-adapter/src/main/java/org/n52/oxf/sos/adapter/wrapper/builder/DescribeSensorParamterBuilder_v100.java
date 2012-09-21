package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.HashMap;
import java.util.Map;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of parameters, which is necessary to call
 * doDescribeSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class DescribeSensorParamterBuilder_v100 {

	// only SensorML is supported at the moment
	public static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";
	// public static final String OUTPUT_FORMAT_TML = "text/xml;subtype=\"TML/1.0\"";
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorId
	 * @param outputFormat
	 */
	public DescribeSensorParamterBuilder_v100(String sensorId, String outputFormat) throws IllegalArgumentException {
		if (sensorId == null || outputFormat == null) {
			throw new IllegalArgumentException("The parameters \"sensorId\" and \"outputFormat\" are mandatory. They cannot be left empty!");
		}
		parameters.put(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, sensorId);
		parameters.put(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}
