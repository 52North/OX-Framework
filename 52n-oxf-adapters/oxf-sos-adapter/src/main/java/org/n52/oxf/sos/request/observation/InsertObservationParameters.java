package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import org.n52.ows.request.MultimapRequestParameters;

/**
 * Assembles all parameters needed for an InsertObservation request.
 */
public class InsertObservationParameters extends MultimapRequestParameters {
	
	public InsertObservationParameters(String assignedSensorId, ObservationParameters observationBuilder) throws IllegalArgumentException {
        addNonEmpty(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
		mergeWith(observationBuilder);
	}

    public boolean isValid() {
        return true;
    }
	
}