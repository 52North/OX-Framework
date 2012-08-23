package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.oxf.xml.XMLConstants;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class is used to collect parameter information about Observations.
 * 
 * @author Eric
 * 
 * TODO RESTRUCTURING OF THE AVAILABLE PARAMETERS!
 */
public class ObservationBuilder {
	
	// valid/available parameters for measurements
	private String[] measurementObservationParameter = {
			INSERT_OBSERVATION_SAMPLING_TIME,
			INSERT_OBSERVATION_FOI_ID_PARAMETER,
			INSERT_OBSERVATION_NEW_FOI_NAME,
			INSERT_OBSERVATION_NEW_FOI_DESC,
			INSERT_OBSERVATION_NEW_FOI_POSITION,
			INSERT_OBSERVATION_POSITION_SRS,
			INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
			INSERT_OBSERVATION_VALUE_PARAMETER,
			
			INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE
	};

	// valid/available parameters for category observations
	private String[] categoryObservationParameter = {
			INSERT_OBSERVATION_SAMPLING_TIME,
			INSERT_OBSERVATION_FOI_ID_PARAMETER,
			INSERT_OBSERVATION_NEW_FOI_NAME,
			INSERT_OBSERVATION_NEW_FOI_DESC,
			INSERT_OBSERVATION_NEW_FOI_POSITION,
			INSERT_OBSERVATION_POSITION_SRS,
			INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
			INSERT_OBSERVATION_VALUE_PARAMETER,
			
			INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE
	};
	
	private QName type;
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Creating an observation description by defining the type of this observaiton.
	 * 
	 * @param type observation type
	 */
	public ObservationBuilder(QName type) {
		this.type = type;
		parameters.put(INSERT_OBSERVATION_TYPE, type.toString());
	}
	
	/**
	 * Generic method to add valid/available parameters of the corresponding observation type.
	 * 
	 * @param parameter
	 * @param value
	 * @throws IllegalArgumentException
	 */
	public void addParameter(String parameter, String value) throws IllegalArgumentException {
		if (isValidForType(parameter)) {
			parameters.put(parameter, value);
		} else {
			throw new IllegalArgumentException("The parameter \"" + parameter +
					"\" does not fit to the observation type \"" + type + "\"!");
		}
	}
	
	/**
	 * Removes a certain parameter from the list.
	 * 
	 * @param parameter
	 */
	public void removeParameter(String parameter) {
		parameters.remove(parameter);
	}
	
	/**
	 * Return all parameters of this observation description.
	 * 
	 * @return parameter list
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Return the type of Observation.
	 * 
	 * @return observation type
	 */
	public QName getType() {
		return type;
	}
	
	/**
	 * Checks whether the parameter requested for adding is valid for this observation type.
	 * 
	 * @param typeParameter
	 * @return truth
	 */
	private boolean isValidForType(String typeParameter) {
		String[] validParameters;
		if (type.equals(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION)) {
			validParameters = categoryObservationParameter;
		} else if (type.equals(XMLConstants.QNAME_OM_1_0_MEASUREMENT)) {
			validParameters = measurementObservationParameter;
		} else
			return false;

		boolean validity = false;
		for (int i = 0; i < validParameters.length; i++) {
			if (typeParameter.equals(validParameters[i])) {
				validity = true;
			}
		}
		return validity;
	}
		
}
