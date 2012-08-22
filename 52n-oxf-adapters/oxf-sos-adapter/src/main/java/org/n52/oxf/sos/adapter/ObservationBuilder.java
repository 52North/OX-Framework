package org.n52.oxf.sos.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.oxf.xml.XMLConstants;

import static org.n52.oxf.xml.XMLConstants.*;

public class ObservationBuilder {
	
	private String[] categoryObservationParameter = {
			ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
			ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION,
			ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS,
			ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER,
			
			ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE
	};
	
	private String[] measurementObservationParameter = {
			ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME,
			ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC,
			ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION,
			ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS,
			ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER,
			ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER,
			
			ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE
	};
	
	private QName type;
	private Map<String, String> parameters = new HashMap<String, String>();

	public ObservationBuilder(QName type) {
		this.type = type;
		parameters.put(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE, type.toString());
	}
	
	public void addParameter(String parameter, String value) throws IllegalArgumentException {
		if (isValidForType(parameter)) {
			parameters.put(parameter, value);
		} else {
			throw new IllegalArgumentException("The parameter \"" + parameter +
					"\" does not fit to the observation type \"" + type + "\"!");
		}
	}
	
	public void removeParameter(String parameter) {
		parameters.remove(parameter);
	}
	
	Map<String, String> getParameters() {
		return parameters;
	}
	
	public QName getType() {
		return type;
	}
	
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
