package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.xml.XMLConstants;

public class CountObservationBuilder extends ObservationBuilder {

	/**
	 * Type specific constructor for count observations. It adds the type as parameter to the list.
	 */
	public CountObservationBuilder() {
		type = XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION;
		parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_COUNT);
	}
	
	public void addObservationValue(int observationValue) {
		if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
			parameters.remove(INSERT_OBSERVATION_VALUE_PARAMETER);
		}
		parameters.put(INSERT_OBSERVATION_VALUE_PARAMETER, "" + observationValue);
	}
}
