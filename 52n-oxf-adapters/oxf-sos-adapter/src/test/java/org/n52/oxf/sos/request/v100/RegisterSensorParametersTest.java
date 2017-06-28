/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sos.request.v100;

import static org.junit.Assert.assertEquals;
import static org.n52.oxf.sos.request.v100.RegisterSensorParameters.*;

import org.junit.Before;
import org.junit.Test;

public class RegisterSensorParametersTest {

    private RegisterSensorParameters parameters;

    @Before
    public void setUp() {
        parameters = new RegisterSensorParameters("sensorDescription", "observationTemplate");
    }

	@Test
	public void testValidConstructorParameters() {
        new RegisterSensorParameters("asdf", "adf");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new RegisterSensorParameters(null, null);
		new RegisterSensorParameters("", null);
		new RegisterSensorParameters(null, "");
        new RegisterSensorParameters("", "");
        new RegisterSensorParameters("asdf", "");
        new RegisterSensorParameters("", "asdf");
	}

	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		final String parMan_01 = parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
		final String parMan_02 = parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);

		assertEquals("sensorDescription", parMan_01);
		assertEquals("observationTemplate", parMan_02);
		assertEquals(parameters.getSingleValue(RegisterSensorParameters.SERVICE_VERSION), "1.0.0");
		assertEquals(parameters.getSingleValue(RegisterSensorParameters.SERVICE_TYPE), "SOS");
	}

}
