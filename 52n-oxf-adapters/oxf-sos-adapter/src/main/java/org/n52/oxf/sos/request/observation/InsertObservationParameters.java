package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.Map;

import org.n52.ows.request.RequestParameters;

/**
 * Assembles all parameters needed for an InsertObservation request.
 */
public class InsertObservationParameters extends RequestParameters {
	
	public InsertObservationParameters(String assignedSensorId, ObservationParameters observationBuilder) throws IllegalArgumentException {
        putNonEmpty(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
		putAll(observationBuilder);
	}

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
        
    }
	
}