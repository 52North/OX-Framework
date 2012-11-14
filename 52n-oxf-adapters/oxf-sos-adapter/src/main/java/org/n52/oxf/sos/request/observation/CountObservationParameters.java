
package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a Count observation.
 */
public class CountObservationParameters extends ObservationParameters {

    /**
     * Creates truth observations of type {@link XMLConstants#QNAME_OM_1_0_COUNT_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_COUNT} as its type description to the parameter list.
     */
    public CountObservationParameters() {
        super(XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION);
        addParameterValue(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_COUNT);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add.
     */
    public void addObservationValue(int observationValue) {
        addParameterValue(INSERT_OBSERVATION_VALUE_PARAMETER, Integer.toString(observationValue));
    }

    public boolean isValid() {
        return true;
    }
}
