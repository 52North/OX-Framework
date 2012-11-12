
package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_TEXT_OBSERVATION;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a Text observation.
 */
public class TextObservationParameters extends ObservationParameters {

    /**
     * Crreates truth observations of type {@link XMLConstants#QNAME_OM_1_0_TEXT_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_TEXT} as its type description to the parameter list.
     */
    public TextObservationParameters() {
        super(QNAME_OM_1_0_TEXT_OBSERVATION);
        putNonEmpty(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TEXT);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add.
     */
    public void addObservationValue(String observationValue) {
        put(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}