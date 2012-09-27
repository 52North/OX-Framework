package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.xml.XMLConstants;

public class BooleanObservationBuilder extends ObservationBuilder {

	/**
	 * Type specific constructor for truth observations. It adds the type as parameter to the list.
	 */
	public BooleanObservationBuilder() {
		type = XMLConstants.QNAME_OM_1_0_TRUTH_OBSERVATION;
		parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TRUTH);
	}
	
	public void addObservationValue(boolean observationValue) {
		if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
			parameters.remove(INSERT_OBSERVATION_VALUE_PARAMETER);
		}
		parameters.put(INSERT_OBSERVATION_VALUE_PARAMETER, "" + observationValue);
	}
}
