/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.oxf.sos.request.v100;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import org.n52.oxf.sos.observation.ObservationParameters;

/**
 * Assembles all parameters needed for an InsertObservation request.
 */
public class InsertObservationParameters extends org.n52.oxf.sos.request.InsertObservationParameters {
    
    private static final String REQUEST_PARAMETER = "request";
	
	public InsertObservationParameters(final String assignedSensorId, final ObservationParameters observationParameters) throws IllegalArgumentException {
	    addNonEmpty(REQUEST_PARAMETER, "InsertObservation");
        addNonEmpty(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
        if (observationParameters == null || observationParameters.isEmpty() || !observationParameters.isValid()) {
        	throw new IllegalArgumentException("Parameter 'ObservationParameters' with may not be null or empty!");
        }
		mergeWith(observationParameters);
	}

    public boolean isValid() {
    	// TODO how to validate the already mergedStuff?
        return !isEmptyValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER);
    }
	
}