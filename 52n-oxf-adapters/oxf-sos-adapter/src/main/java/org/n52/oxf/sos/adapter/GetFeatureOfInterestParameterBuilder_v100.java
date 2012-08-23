package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of mandatory and optional parameters, which is necessary to call doGetObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetFeatureOfInterestParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param identification
	 * @param identificationType
	 * @see ISOSRequestBuilder
	 */
	public GetFeatureOfInterestParameterBuilder_v100(String identification, String identificationType) {
		if (identification == null || identificationType == null ||
				(!identificationType.equals(GET_FOI_ID_PARAMETER) && !identificationType.equals(GET_FOI_LOCATION_PARAMETER)))
			throw new IllegalArgumentException("The parameters \"identification\" and \"identificationType\" are mandatory. " +
					"They cannot be left empty! Take care of the available identification types! (" +
					GET_FOI_ID_PARAMETER + ", " + GET_FOI_LOCATION_PARAMETER + ")");
		if (identificationType.equals(GET_FOI_ID_PARAMETER))
			parameters.put(GET_FOI_ID_PARAMETER, identification);
		else
			parameters.put(GET_FOI_LOCATION_PARAMETER, identification);
	}
	
	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Adds the optional parameter "eventTime".
	 * 
	 * @param eventTime
	 * @return parameter builder
	 */
	public GetFeatureOfInterestParameterBuilder_v100 addEventTime(String eventTime) {
		if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
			parameters.remove(GET_FOI_EVENT_TIME_PARAMETER);
		}
		parameters.put(GET_FOI_EVENT_TIME_PARAMETER, eventTime);
		return this;
	}
	
}
