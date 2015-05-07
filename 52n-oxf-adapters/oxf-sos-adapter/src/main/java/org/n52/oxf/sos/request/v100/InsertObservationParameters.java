/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.request.v100;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import org.n52.oxf.sos.observation.ObservationParameters;

/**
 * Assembles all parameters needed for an InsertObservation request.
 */
public class InsertObservationParameters extends org.n52.oxf.sos.request.InsertObservationParameters {
    
    private static final String REQUEST_PARAMETER = "request";
	private final ObservationParameters observationParameters;
	
	public InsertObservationParameters(final ObservationParameters observationParameters) throws IllegalArgumentException {
	    addNonEmpty(REQUEST_PARAMETER, "InsertObservation");
        if (observationParameters == null || observationParameters.isEmpty() || !observationParameters.isValid()) {
        	throw new IllegalArgumentException("Parameter 'ObservationParameters' with may not be null or empty!");
        }
		mergeWith(observationParameters);
		this.observationParameters = observationParameters;
	}

    public boolean isValid() {
        return !isEmptyValue(INSERT_OBSERVATION_PROCEDURE_PARAMETER) &&
        		observationParameters.isValid();
    }
	
}