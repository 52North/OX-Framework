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
package org.n52.oxf.swes.request;

import static org.junit.Assert.assertEquals;
import static org.n52.oxf.swes.request.DescribeSensorParameters.OUTPUT_FORMAT_PARAMETER;
import static org.n52.oxf.swes.request.DescribeSensorParameters.PROCEDURE_PARAMETER;
import static org.n52.oxf.swes.request.DescribeSensorParameters.OUTPUT_FORMAT_SENSORML;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.request.RequestParameters;

public class DescribeSensorParametersTest {
    
    private RequestParameters parameterAssembly;

    @Before
    public void setUp() {
        parameterAssembly = new DescribeSensorParameters("sensorId", OUTPUT_FORMAT_SENSORML);
    }

	@Test
	public void testValidConstructorParameters() {
		new DescribeSensorParameters("sdf", "sdf");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new DescribeSensorParameters(null, null);
		new DescribeSensorParameters("", null);
		new DescribeSensorParameters(null, "");
        new DescribeSensorParameters("", "");
        new DescribeSensorParameters("", "sdf");
        new DescribeSensorParameters("sdf", "");
	}
	
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		String parMan_01 = parameterAssembly.getSingleValue(PROCEDURE_PARAMETER);
		String parMan_02 = parameterAssembly.getSingleValue(OUTPUT_FORMAT_PARAMETER);
		
		assertEquals("sensorId", parMan_01);
		assertEquals(OUTPUT_FORMAT_SENSORML, parMan_02);
	}

}
