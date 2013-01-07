package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.n52.oxf.sos.request.observation.ObservationParameters;

/**
 * This class describes a set of parameters, which is necessary to call
 * doInsertObservation([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class InsertObservationParameterBuilder_v100 {
	
	private Map<String, String> parameters;
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param assignedSensorId
	 * @param observation
	 * @deprecated Use {@link #InsertObservationParameterBuilder_v100(String, ObservationParameters)}
	 */
	public InsertObservationParameterBuilder_v100(String assignedSensorId, ObservationBuilder observationBuilder) throws IllegalArgumentException {
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
	public InsertObservationParameterBuilder_v100(String assignedSensorId, ObservationParameters obsParameter) {
		if (assignedSensorId == null || obsParameter == null) {
			throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"obsParameter\" are mandatory. They cannot be left empty!");
		}
		parameters = getParameterMapFrom(obsParameter);
		parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
	}

	private Map<String, String> getParameterMapFrom(ObservationParameters obsParameter)
	{
		if (!obsParameter.getAvailableKeys().isEmpty())
		{
			HashMap<String,String> parameterMap = new HashMap<String, String>(obsParameter.getAvailableKeys().size());
			for (String key : obsParameter.getAvailableKeys())
			{
				if (!key.isEmpty())
				{
					Iterable<String> parameterValues = obsParameter.getAllValues(key);
					StringBuilder concatenatedParameterValues = new StringBuilder();
					for (Iterator<String> parameterIterator = parameterValues.iterator(); parameterIterator.hasNext();)
					{
						String parameterValue = parameterIterator.next();
						if (!parameterValue.isEmpty())
						{
							concatenatedParameterValues.append(parameterValue);
							concatenatedParameterValues.append(",");
						}
					}
					// remove last ","
					String parameterValuesAsSingleString = concatenatedParameterValues.toString();
					if (!parameterValuesAsSingleString.isEmpty())
					{
						parameterValuesAsSingleString = parameterValuesAsSingleString.substring(0, parameterValuesAsSingleString.length()-2);
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