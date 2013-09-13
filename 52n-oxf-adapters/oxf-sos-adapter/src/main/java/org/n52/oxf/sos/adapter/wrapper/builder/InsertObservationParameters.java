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

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.sos.observation.ObservationParameters;

/**
 * This class describes a set of parameters, which is necessary to call
 * doInsertObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class InsertObservationParameters {
	
	private final Map<String, String> parameters;
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param assignedSensorId
	 * @param observation
	 * @deprecated Use {@link #InsertObservationParameters(String, ObservationParameters)}
	 */
	@Deprecated
	public InsertObservationParameters(final String assignedSensorId, final ObservationBuilder observationBuilder) throws IllegalArgumentException {
		if (assignedSensorId == null || observationBuilder == null) {
			throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"observationBuilder\" are mandatory. They cannot be left empty!");
		}
		parameters = observationBuilder.getParameters();
		parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
	}

	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorURI
	 * @param obsParameter
	 */
	public InsertObservationParameters(final String assignedSensorId, final ObservationParameters obsParameter) {
		if (assignedSensorId == null || obsParameter == null) {
			throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"obsParameter\" are mandatory. They cannot be left empty!");
		}
		parameters = getParameterMapFrom(obsParameter);
		parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
	}

	private Map<String, String> getParameterMapFrom(final ObservationParameters obsParameter)
	{
		if (!obsParameter.isEmpty())
		{
			final HashMap<String,String> parameterMap = new HashMap<String, String>();
			for (final String key : obsParameter.getParameterNames())
			{
				if (!key.isEmpty())
				{
					final Iterable<String> parameterValues = obsParameter.getAllValues(key);
					final StringBuilder concatenatedParameterValues = new StringBuilder();
					for (final String parameterValue : parameterValues) {
						if (parameterValue != null && !parameterValue.isEmpty())
						{
							concatenatedParameterValues.append(parameterValue);
							concatenatedParameterValues.append(",");
						}
					}
					// remove last ","
					String parameterValuesAsSingleString = concatenatedParameterValues.toString();
					if (!parameterValuesAsSingleString.isEmpty())
					{
						parameterValuesAsSingleString = parameterValuesAsSingleString.substring(0, parameterValuesAsSingleString.length()-1);
						parameterMap.put(key, parameterValuesAsSingleString);
					}
				}
			}
			return parameterMap;
		}
		return new HashMap<String, String>(0);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
}