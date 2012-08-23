package org.n52.oxf.sos.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.StringValueDomain;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of parameters, which is necessary to call doGetObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationParameterBuilder_v100 {
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private ArrayList<String> eventTimeList = new ArrayList<String>();
	private ArrayList<String> procedureList = new ArrayList<String>();
	private ArrayList<String> observedPropertyList = new ArrayList<String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param offering
	 * @param observedProperty
	 * @param responseFormat
	 */
	public GetObservationParameterBuilder_v100(String offering, String observedProperty, String responseFormat) throws IllegalArgumentException {
		if (offering == null || observedProperty == null || responseFormat == null) {
			throw new IllegalArgumentException("The parameters \"offering\", \"observedProperty\" and \"responseFormat\" are mandatory. They cannot be left empty!");
		}
		parameters.put(GET_OBSERVATION_OFFERING_PARAMETER, offering);
		observedPropertyList.add(observedProperty);
		parameters.put(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, responseFormat);
	}

	/**
	 * @return set of parameters
	 * @throws OXFException 
	 */
	Map<String, Object> getParameters() throws OXFException {
		// preparing multiple occurance of observed property parameter for SOSWrapper
		String[] observedProperties = observedPropertyList.toArray(new String[observedPropertyList.size()]);
		Parameter observedPropertyParameter = new Parameter(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, true, new StringValueDomain(observedProperties), GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		ParameterShell observedPropertiesPs = new ParameterShell(observedPropertyParameter, observedProperties);
		parameters.put(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedPropertiesPs);
		// preparing multiple occurance of event time parameter for SOSWrapper
		if (!eventTimeList.isEmpty()) {
			String[] eventTimes = eventTimeList.toArray(new String[eventTimeList.size()]);
			Parameter eventTimeParameter = new Parameter(GET_OBSERVATION_EVENT_TIME_PARAMETER, true, new StringValueDomain(eventTimes), GET_OBSERVATION_EVENT_TIME_PARAMETER);
			ParameterShell eventTimesPs = new ParameterShell(eventTimeParameter, eventTimes);
			parameters.put(GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTimesPs);
		}
		// preparing multiple occurance of procedure parameter for SOSWrapper
		if (!procedureList.isEmpty()) {
			String[] procedures = procedureList.toArray(new String[procedureList.size()]);
			Parameter procedureParameter = new Parameter(GET_OBSERVATION_PROCEDURE_PARAMETER, true, new StringValueDomain(procedures), GET_OBSERVATION_PROCEDURE_PARAMETER);
			ParameterShell proceduresPs = new ParameterShell(procedureParameter, procedures);
			parameters.put(GET_OBSERVATION_PROCEDURE_PARAMETER, proceduresPs);	
		}
		return parameters;
	}

	/**
	 * Adds the optional parameter "srsName".
	 * 
	 * @param srsName
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addSrsName(String srsName) {
		if (parameters.get(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameters.remove(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER);
		}
		parameters.put(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER, srsName);
		return this;
	}
	
	/**
	 * Adds the optional parameter "eventTime", which can occur many times.
	 * 
	 * @param eventTime
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addEventTime(String eventTime) {
		eventTimeList.add(eventTime);
		return this;
	}
	
	/**
	 * Adds the optional parameter "procedure", which can occur many times.
	 * 
	 * @param procedure
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addProcedure(String procedure) {
		procedureList.add(procedure);
		return this;
	}
	
	/**
	 * Extends the list of observed properties.
	 * 
	 * @param observedProperty
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addObservedProperty(String observedProperty) {
		observedPropertyList.add(observedProperty);
		return this;
	}
	
	/**
	 * Adds the optional parameter "featureOfInterest".
	 * 
	 * @param featureOfInterest
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addFeatureOfInterest(String featureOfInterest) {
		if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, featureOfInterest);
		return this;
	}
	
	/**
	 * Adds the optional parameter "result".
	 * 
	 * @param result
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResult(String result) {
		if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESULT_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESULT_PARAMETER, result);
		return this;
	}
	
	/**
	 * Adds the optional parameter "resultModel".
	 * 
	 * @param resultModel
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResultModel(String resultModel) {
		if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESULT_MODEL_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
		return this;
	}
	
	/**
	 * Adds the optional parameter "responseMode".
	 * 
	 * @param responseMode
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResponseMode(String responseMode) {
		if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESPONSE_MODE_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, responseMode);
		return this;
	}

}
