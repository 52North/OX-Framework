package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_COUNT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;

import org.n52.oxf.sos.request.observation.CountObservationParameters;
import org.n52.oxf.sos.request.observation.ObservationParametersFactory;
import org.n52.oxf.xml.XMLConstants;

/**
 * 
 * @deprecated use {@link ObservationParametersFactory} or {@link CountObservationParameters}
 */
@Deprecated
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
