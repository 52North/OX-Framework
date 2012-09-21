package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.Map;

/**
 * This class describes a set of parameters, which is necessary to call
 * doInsertObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class InsertObservationParameterBuilder_v100 {
	
	private Map<String, String> parameters;
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param assignedSensorId
	 * @param observation
	 */
	public InsertObservationParameterBuilder_v100(String assignedSensorId, ObservationBuilder observationBuilder) throws IllegalArgumentException {
		if (assignedSensorId == null || observationBuilder == null) {
			throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"observation\" are mandatory. They cannot be left empty!");
		}
		parameters = observationBuilder.getParameters();
		parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
}