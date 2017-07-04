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

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.oxf.sos.observation.DefaultObservationParametersFactory;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.xml.XMLConstants;

// TODO add more tests regarding offeringIds and observations
public class InsertObservationParametersTest {

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Test public void
    shouldThrowIllegalArgumentExceptionIfMissingAllParameters() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters(null, null);
    }

    @Test public void
    shouldThrowIllegalArgumentExceptionIfMissingProcedureId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'offerings' is required and may not be null or empty!");
        final MeasurementObservationParameters measurementParameters = (MeasurementObservationParameters) new DefaultObservationParametersFactory()
        .createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION);
        measurementParameters.addObservationValue("1.0");
        measurementParameters.addUom("uom");
        new InsertObservationParameters(measurementParameters,null);
    }

    @Test public void
    shouldThrowIllegalArgumentExceptionIfMissingObservationParameter() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters(null,Collections.singletonList("sdf"));
    }

    @Test public void
    shouldThrowIllegalArgumentExceptionIfReceivingInvalidObservationParameter() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters( new DefaultObservationParametersFactory()
        .createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION),Collections.singletonList("sdf"));
    }

}
