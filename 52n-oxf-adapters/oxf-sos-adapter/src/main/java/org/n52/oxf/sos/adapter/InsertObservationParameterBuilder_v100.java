package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION;

import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a set of mandatory parameters, which is necessary to call doInsertObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class InsertObservationParameterBuilder_v100 {
	
	private Map<String, String> parameters; // set of mandatory parameters
	
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
		parameters.put(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
}