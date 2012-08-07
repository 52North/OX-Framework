package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * This class describes a set of mandatory and optional parameters, which is necessary to call doGetObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationParamterBuilder_v100 {
	
	private Map<String, Object> parameters = new HashMap<String, Object>(); // set of mandatory and optional parameters
	private List<String> eventTimeList = new Vector<String>(); // list of event time parameters
	private List<String> procedureList = new Vector<String>(); // list of procedure parameters
	private List<String> observedPropertyList = new Vector<String>(); // list of observed property paramerters
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param offering
	 * @param observedProperty
	 * @param responseFormat
	 */
	public GetObservationParamterBuilder_v100(String offering, String observedProperty, String responseFormat) throws IllegalArgumentException {
		if (offering == null || observedProperty == null || responseFormat == null)
			throw new IllegalArgumentException("The parameters \"offering\", \"observedProperty\" and \"responseFormat\" are mandatory. They cannot be left empty!");
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offering);
		observedPropertyList.add(observedProperty);
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedPropertyList);
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, responseFormat);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * Adds the optional parameter "srsName".
	 * 
	 * @param srsName
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addSrsName(String srsName) {
		parameters.put(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER, srsName);
		return this;
	}
	
	/**
	 * Adds the optional parameter "eventTime", which can occur many times.
	 * 
	 * @param eventTime
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addEventTime(String eventTime) {
		eventTimeList.add(eventTime);
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER) == null)
			parameters.put(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTimeList);
		return this;
	}
	
	/**
	 * Adds the optional parameter "procedure", which can occur many times.
	 * 
	 * @param procedure
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addProcedure(String procedure) {
		procedureList.add(procedure);
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER) == null)
			parameters.put(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, procedureList);
		return this;
	}
	
	/**
	 * Extends the list of observed properties.
	 * 
	 * @param observedProperty
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addObservedProperty(String observedProperty) {
		observedPropertyList.add(observedProperty);
		return this;
	}
	
	/**
	 * Adds the optional parameter "featureOfInterest".
	 * 
	 * @param featureOfInterest
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addFeatureOfInterest(String featureOfInterest) {
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, featureOfInterest);
		return this;
	}
	
	/**
	 * Adds the optional parameter "result".
	 * 
	 * @param result
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addResult(String result) {
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER, result);
		return this;
	}
	
	/**
	 * Adds the optional parameter "resultModel".
	 * 
	 * @param resultModel
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addResultModel(String resultModel) {
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
		return this;
	}
	
	/**
	 * Adds the optional parameter "responseMode".
	 * 
	 * @param responseMode
	 * @return parameter builder
	 */
	public GetObservationParamterBuilder_v100 addResponseMode(String responseMode) {
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, responseMode);
		return this;
	}

}
