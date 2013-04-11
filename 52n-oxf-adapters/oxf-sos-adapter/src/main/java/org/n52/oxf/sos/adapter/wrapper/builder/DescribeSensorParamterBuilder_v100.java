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
