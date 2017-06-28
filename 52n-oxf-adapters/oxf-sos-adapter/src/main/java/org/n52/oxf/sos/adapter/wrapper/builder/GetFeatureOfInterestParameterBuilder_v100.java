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
 * doGetFeatureOfInterst([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetFeatureOfInterestParameterBuilder_v100 {
	
	private final Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param identification
	 * @param identificationType
	 * @see ISOSRequestBuilder
	 */
	public GetFeatureOfInterestParameterBuilder_v100(final String identification, final String identificationType) {
		if (identification == null || identificationType == null ||
				(!identificationType.equals(GET_FOI_ID_PARAMETER) && !identificationType.equals(GET_FOI_LOCATION_PARAMETER))) {
			throw new IllegalArgumentException("The parameters \"identification\" and \"identificationType\" are mandatory. " +
					"They cannot be left empty! Take care of the available identification types! (" +
					GET_FOI_ID_PARAMETER + ", " + GET_FOI_LOCATION_PARAMETER + ")");
		}
		if (identificationType.equals(GET_FOI_ID_PARAMETER)) {
			parameters.put(GET_FOI_ID_PARAMETER, identification);
		}
		else {
			parameters.put(GET_FOI_LOCATION_PARAMETER, identification);
		}
	}
	
	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Adds or replaces the optional parameter "eventTime".
	 * 
	 * @param eventTime
	 * @return parameter builder
	 */
	public GetFeatureOfInterestParameterBuilder_v100 addEventTime(final String eventTime) {
		if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
			parameters.remove(GET_FOI_EVENT_TIME_PARAMETER);
		}
		parameters.put(GET_FOI_EVENT_TIME_PARAMETER, eventTime);
		return this;
	}
	
}
