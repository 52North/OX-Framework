/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * This class describes a set of parameters, which is necessary to call
 * doGetObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationParameterBuilder_v100 {
	
	private final Map<String, Object> parameters = new HashMap<String, Object>();
	private final List<String> eventTimeList = new ArrayList<String>();
	private final List<String> procedureList = new ArrayList<String>();
	private final List<String> observedPropertyList = new ArrayList<String>();
	
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
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameters.remove(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER);
		}
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER, srsName);
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
