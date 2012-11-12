
package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_TRUTH;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_TRUTH_OBSERVATION;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a Boolean observation.
 */
public class BooleanObservationParameters extends ObservationParameters {
    
    /**
     * Creates truth observations of type {@link XMLConstants#QNAME_OM_1_0_TRUTH_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_TRUTH} as its type description to the parameter list.
     */
    public BooleanObservationParameters() {
        super(QNAME_OM_1_0_TRUTH_OBSERVATION);
        put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_TRUTH);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add.
     */
    public void addObservationValue(boolean observationValue) {
        put(INSERT_OBSERVATION_VALUE_PARAMETER, Boolean.toString(observationValue));
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
