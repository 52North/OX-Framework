package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.sos.request.observation.ObservationParametersFactory;
import org.n52.oxf.sos.request.observation.TextObservationParameters;
import org.n52.oxf.xml.XMLConstants;

/**
 * Text ObservationParameters assembles parameters, which are necessary to describe a text observation.
 * 
 * @author Eric
 * @deprecated use {@link ObservationParametersFactory} or {@link TextObservationParameters}
 */
@Deprecated
public class TextObservationBuilder extends ObservationBuilder {

		/**
		 * Type specific constructor for text observations. It adds the type as parameter to the list.
		 */
		public TextObservationBuilder() {
			type = XMLConstants.QNAME_OM_1_0_TEXT_OBSERVATION;
			parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TEXT);
		}
		
		public void addObservationValue(String observationValue) {
			if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
				parameters.remove(INSERT_OBSERVATION_VALUE_PARAMETER);
			}
			parameters.put(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
		}
}