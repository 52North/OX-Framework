/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sos.examples;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.INSERT_OBSERVATION;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;

//@Ignore // comment out to run demo class via JUnit
public class InsertObservationRequestExample extends SosAdapterRequestExample {

    @Override
	@Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        // should return an exception when transactional interface is
        // not supported of public
        performOperationParseResult(createInsertObservationOperation());
    }

    private Operation createInsertObservationOperation() {
        return new Operation(INSERT_OBSERVATION, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        final ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SERVICE, "SOS");
        parameters.addParameterShell(VERSION, "1.0.0");
        parameters.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1");
        parameters.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, "urn:ogc:def:phenomenon:OGC:1.0.30:waterspeed");
        parameters.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, "id_2001");
        parameters.addParameterShell(INSERT_OBSERVATION_SAMPLING_TIME, "2013-12-12T12:00:00Z");
        parameters.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, "m3/s");
        parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, "23.5");
        return parameters;
    }

}
