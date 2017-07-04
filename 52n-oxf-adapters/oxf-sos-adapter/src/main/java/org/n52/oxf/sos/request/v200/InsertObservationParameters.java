/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.oxf.sos.request.v200;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OFFERINGS_PARAMETER;

import java.util.Collection;
import org.n52.oxf.om.x20.OmParameter;

import org.n52.oxf.sos.observation.ObservationParameters;

/**
 * Assembles all parameters needed for an InsertObservation request according to
 * SOS 2.0.0 specification version.
 */
public class InsertObservationParameters extends org.n52.oxf.sos.request.InsertObservationParameters {

    private final ObservationParameters observation;

    private OmParameter[] omParameters;

    public InsertObservationParameters(
            final ObservationParameters observationParameters,
            final Collection<String> offerings)
            throws IllegalArgumentException {
        if (observationParameters == null ||
                observationParameters.isEmpty() ||
                !observationParameters.isValid()) {
            throw new IllegalArgumentException(
                    "Parameter 'ObservationParameters' is required and may not be null or empty!");
        }
        addNonEmpty(INSERT_OBSERVATION_OFFERINGS_PARAMETER, offerings);
        mergeWith(observationParameters);
        observation = observationParameters;
        omParameters = new OmParameter[0];
    }

    public InsertObservationParameters(
            final ObservationParameters observationParameters,
            final Collection<String> offerings,
            final OmParameter... omParameters) {
        this(observationParameters, offerings);
        if (omParameters != null && omParameters.length > 0) {
            this.omParameters = omParameters;
        } else {
            this.omParameters = new OmParameter[0];
        }
    }

    @Override
    public boolean isValid() {
        return !isEmptyValue(INSERT_OBSERVATION_OFFERINGS_PARAMETER) &&
                observation.isValid();
    }

    public boolean isSetOmParameter() {
        return omParameters.length > 0;
    }

    public OmParameter[] getOmParameter() {
        return omParameters;
    }

}
