/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;

/**
 * This class describes a set of parameters, which is necessary to call
 * doGetObservationById([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetObservationByIdParameterBuilder_v100 {
	
	private final Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param observationId
	 * @param responseFormat
	 */
	public GetObservationByIdParameterBuilder_v100(final String observationId, final String responseFormat) {
		if (observationId == null || responseFormat == null) {
			throw new IllegalArgumentException("The parameters 'observationId' and 'responseFormat' are mandatory. They cannot be left empty!");
		}
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
	public GetObservationByIdParameterBuilder_v100 addSrsName(final String srsName) {
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null) {
			parameters.remove(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER);
		}
		parameters.put(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, srsName);
		return this;
	}
	
	/**
	 * Adds or replaces the optional parameter "resultModel".
	 * 
	 * @param resultModel
	 * @return parameter builder
	 */
	public GetObservationByIdParameterBuilder_v100 addResultModel(final String resultModel) {
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
	public GetObservationByIdParameterBuilder_v100 addResponseMode(final String responseMode) {
		if (parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
			parameters.remove(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER);
		}
		parameters.put(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, responseMode);
		return this;
	}
	
}
