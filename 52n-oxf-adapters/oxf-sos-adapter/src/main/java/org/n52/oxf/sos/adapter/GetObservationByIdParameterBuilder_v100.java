package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set ofparameters, which is necessary to call
 * doGetObservationById([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationByIdParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param observationId
	 * @param responseFormat
	 */
	public GetObservationByIdParameterBuilder_v100(String observationId, String responseFormat) {
		if (observationId == null || responseFormat == null)
			throw new IllegalArgumentException("The parameters \"observationId\" and \"responseFormat\" are mandatory. They cannot be left empty!");
		parameters.put(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, observationId);
		parameters.put(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, responseFormat);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Adds or replaces the optional parameter "srsName".
	 * 
	 * @param srsName
	 * @return parameter builder
	 */
	public GetObservationByIdParameterBuilder_v100 addSrsName(String srsName) {
		if (parameters.get(SOSWrapper.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null) {
			parameters.remove(SOSWrapper.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER);
		}
		parameters.put(SOSWrapper.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, srsName);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "resultModel".
	 * 
	 * @param resultModel
	 * @return parameter builder
	 */
	public GetObservationByIdParameterBuilder_v100 addResultModel(String resultModel) {
		if (parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, resultModel);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "responseMode".
	 * 
	 * @param responseMode
	 * @return parameter builder
	 */
	public GetObservationByIdParameterBuilder_v100 addResponseMode(String responseMode) {
		if (parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, responseMode);
		return this;
	}
	
}
