package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doInsertObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class InsertObservationParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>(); // set of mandatory parameters
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param assignedSensorId
	 * @param observation
	 */
	public InsertObservationParameterBuilder_v100(String assignedSensorId, String observation) throws IllegalArgumentException {
		if (assignedSensorId == null || observation == null)
			throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"observation\" are mandatory. They cannot be left empty!");
		parameters.put(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
		parameters.put(SOSWrapper.INSERT_OBSERVATION_OBSERVATION_PARAMETER, observation);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}
