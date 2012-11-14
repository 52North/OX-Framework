
package org.n52.oxf.sos.request.observation;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.ows.request.MultimapRequestParameters;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * Assembles all Observation parameters needed for an InsertObservation request.
 */
public abstract class ObservationParameters extends MultimapRequestParameters {

    private QName type;

    protected ObservationParameters(QName type) {
        this.type = type;
    }

    /**
     * Return the type of Observation.
     * 
     * @return observation type.
     */
    public QName getType() {
        return type;
    }

    // be careful when changing following methods
    // begin -> parameter methods necessary for: MeasurementObservationParameters and CategoryObservationBuilder

    public void addSamplingTime(String samplingTime) {
        addParameterValue(INSERT_OBSERVATION_SAMPLING_TIME, samplingTime);
    }

    public void addFoiId(String foiId) {
        addParameterValue(INSERT_OBSERVATION_FOI_ID_PARAMETER, foiId);
    }

    public void addNewFoiName(String foiName) {
        addParameterValue(INSERT_OBSERVATION_NEW_FOI_NAME, foiName);
    }

    public void addFoiDescription(String foiDescription) {
        addParameterValue(INSERT_OBSERVATION_NEW_FOI_DESC, foiDescription);
    }

    public void addFoiPosition(String foiPosition) {
        addParameterValue(INSERT_OBSERVATION_NEW_FOI_POSITION, foiPosition);
    }

    public void addSrsPosition(String srsPosition) {
        addParameterValue(INSERT_OBSERVATION_POSITION_SRS, srsPosition);
    }

    public void addObservedProperty(String observedProperty) {
        addParameterValue(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
    }

    // end -> parameter methods shared by: MeasurementObservationParameters and CategoryObservationBuilder

}
