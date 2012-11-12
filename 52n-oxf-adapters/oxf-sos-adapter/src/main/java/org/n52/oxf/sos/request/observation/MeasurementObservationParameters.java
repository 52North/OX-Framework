
package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a MeasurementObservationParameters observation.
 */
public class MeasurementObservationParameters extends ObservationParameters {

    /**
     * Crreates truth observations of type {@link XMLConstants#QNAME_OM_1_0_MEASUREMENT_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_MEASUREMENT} as its type description to the parameter
     * list.
     */
    public MeasurementObservationParameters() {
        super(QNAME_OM_1_0_MEASUREMENT_OBSERVATION);
        putNonEmpty(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add
     */
    public void addObservationValue(String observationValue) {
        put(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
    }

    /**
     * Adds or replaces the (current) unit of measure.
     * 
     * @param uom
     *        the unit of measure to add.
     */
    public void addUom(String uom) {
        put(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);
    }

    @Override
    public boolean isValid() {
        return true;
    }

}