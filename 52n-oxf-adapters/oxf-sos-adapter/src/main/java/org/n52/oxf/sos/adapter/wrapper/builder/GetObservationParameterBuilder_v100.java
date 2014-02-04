/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * This class describes a set of parameters, which is necessary to call
 * doGetObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationParameterBuilder_v100 {
	
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	private final ArrayList<String> eventTimeList = new ArrayList<String>();
	private final ArrayList<String> procedureList = new ArrayList<String>();
	private final ArrayList<String> observedPropertyList = new ArrayList<String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param offering
	 * @param observedProperty
	 * @param responseFormat
	 */
	public GetObservationParameterBuilder_v100(final String offering, final String observedProperty, final String responseFormat) throws IllegalArgumentException {
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
	public Map<String, Object> getParameters() throws OXFException {
		// preparing multiple occurrence of observed property parameter for SOSWrapper
		final String[] observedProperties = observedPropertyList.toArray(new String[observedPropertyList.size()]);
		final Parameter observedPropertyParameter = new Parameter(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, true, new StringValueDomain(observedProperties), GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		final ParameterShell observedPropertiesPs = new ParameterShell(observedPropertyParameter, observedProperties);
		parameters.put(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedPropertiesPs);
		// preparing multiple occurrence of event time parameter for SOSWrapper
		if (!eventTimeList.isEmpty()) {
			final String[] eventTimes = eventTimeList.toArray(new String[eventTimeList.size()]);
			final Parameter eventTimeParameter = new Parameter(GET_OBSERVATION_EVENT_TIME_PARAMETER, true, new StringValueDomain(eventTimes), GET_OBSERVATION_EVENT_TIME_PARAMETER);
			final ParameterShell eventTimesPs = new ParameterShell(eventTimeParameter, eventTimes);
			parameters.put(GET_OBSERVATION_EVENT_TIME_PARAMETER, eventTimesPs);
		}
		// preparing multiple occurrence of procedure parameter for SOSWrapper
		if (!procedureList.isEmpty()) {
			final String[] procedures = procedureList.toArray(new String[procedureList.size()]);
			final Parameter procedureParameter = new Parameter(GET_OBSERVATION_PROCEDURE_PARAMETER, true, new StringValueDomain(procedures), GET_OBSERVATION_PROCEDURE_PARAMETER);
			final ParameterShell proceduresPs = new ParameterShell(procedureParameter, procedures);
			parameters.put(GET_OBSERVATION_PROCEDURE_PARAMETER, proceduresPs);	
		}
		return parameters;
	}

	/**
	 * Adds or replaces the optional parameter "srsName".
	 * 
	 * @param srsName
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addSrsName(final String srsName) {
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
	public GetObservationParameterBuilder_v100 addEventTime(final String eventTime) {
		eventTimeList.add(eventTime);
		return this;
	}
	
	/**
	 * Adds the optional parameter "procedure", which can occur many times.
	 * 
	 * @param procedure
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addProcedure(final String procedure) {
		procedureList.add(procedure);
		return this;
	}
	
	/**
	 * Extends the list of observed properties.
	 * 
	 * @param observedProperty
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addObservedProperty(final String observedProperty) {
		observedPropertyList.add(observedProperty);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "featureOfInterest".
	 * 
	 * @param featureOfInterest
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addFeatureOfInterest(final String featureOfInterest) {
		if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, featureOfInterest);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "result".
	 * 
	 * @param result
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResult(final String result) {
		if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESULT_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESULT_PARAMETER, result);
		return this;
	}
	
	/**
	 * Adds the or replaces optional parameter "resultModel".
	 * 
	 * @param resultModel
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResultModel(final String resultModel) {
		if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESULT_MODEL_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "responseMode".
	 * 
	 * @param responseMode
	 * @return parameter builder
	 */
	public GetObservationParameterBuilder_v100 addResponseMode(final String responseMode) {
		if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_RESPONSE_MODE_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, responseMode);
		return this;
	}

}
