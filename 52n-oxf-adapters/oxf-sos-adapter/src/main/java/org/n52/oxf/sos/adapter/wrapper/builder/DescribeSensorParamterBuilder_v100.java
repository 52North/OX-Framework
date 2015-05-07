/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.swes.request.DescribeSensorParameters;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of parameters, which is necessary to call
 * doDescribeSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 * @deprecated use {@link DescribeSensorParameters}
 */
@Deprecated
public class DescribeSensorParamterBuilder_v100 {

	// only SensorML is supported at the moment
	public static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";
	// public static final String OUTPUT_FORMAT_TML = "text/xml;subtype=\"TML/1.0\"";
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorId
	 * @param outputFormat
	 */
	public DescribeSensorParamterBuilder_v100(String sensorId, String outputFormat) throws IllegalArgumentException {
		if (sensorId == null || outputFormat == null) {
			throw new IllegalArgumentException("The parameters \"sensorId\" and \"outputFormat\" are mandatory. They cannot be left empty!");
		}
		parameters.put(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, sensorId);
		parameters.put(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}
