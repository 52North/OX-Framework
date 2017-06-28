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
package org.n52.oxf.swes.request;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.n52.oxf.swes.request.DescribeSensorParameters.*;

import org.junit.Before;
import org.junit.Test;

public class DescribeSensorParametersTest {

	private static final String SENSOR_ID = "sensorId";
	private DescribeSensorParameters parameterAssembly;

	@Before
	public void setUp() {
		parameterAssembly = new DescribeSensorParameters(SENSOR_ID, OUTPUT_FORMAT_SENSORML);
	}

	@Test public void 
	shouldThrowIllegalArgumentExceptionsForInvalidConstructorParameters()
	{
		boolean case1 = false, case2 = false,case3 = false,
				case4 = false, case5 = false,case6 = false,
				case7 = false, case8 = false;
		try {
			new DescribeSensorParameters(null, null);
		} catch (final IllegalArgumentException e) {
			case1 = true;
		}
		try {
			new DescribeSensorParameters("", null);
		} catch (final IllegalArgumentException e) {
			case2 = true;
		}
		try {
			new DescribeSensorParameters(null, "");
		} catch (final IllegalArgumentException e) {
			case3 = true;
		}
		try {
			new DescribeSensorParameters("", "");
		} catch (final IllegalArgumentException e) {
			case4 = true;
		}
		try {
			new DescribeSensorParameters("", "sdf");
		} catch (final IllegalArgumentException e) {
			case5 = true;
		}
		try {
			new DescribeSensorParameters("sdf", "");
		} catch (final IllegalArgumentException e) {
			case6 = true;
		}
		try {
			new DescribeSensorParameters("");
		} catch (final IllegalArgumentException e) {
			case7 = true;
		}
		try {
			new DescribeSensorParameters(null);
		} catch (final IllegalArgumentException e) {
			case8 = true;
		}
		if (!(case1&&case2&&case3&&case4&&case5&&case6&&case7&&case8)) {
			fail("Not all cases of illegal argument combinations are handled!");
		}
	}

	@Test public void
	shouldApplyMandatoryParametersWithAllConstructors()
	{
		String procedure = parameterAssembly.getSingleValue(PROCEDURE_PARAMETER);
		String outputFormat = parameterAssembly.getSingleValue(OUTPUT_FORMAT_PARAMETER);

		assertEquals(SENSOR_ID, procedure);
		assertEquals(OUTPUT_FORMAT_SENSORML, outputFormat);
		assertThat(parameterAssembly.isValid(),is(true));
		
		parameterAssembly = new DescribeSensorParameters(SENSOR_ID);
		
		procedure = parameterAssembly.getSingleValue(PROCEDURE_PARAMETER);
		outputFormat = parameterAssembly.getSingleValue(OUTPUT_FORMAT_PARAMETER);
		
		assertEquals(SENSOR_ID, procedure);
		assertEquals(OUTPUT_FORMAT_SENSORML, outputFormat);
		assertThat(parameterAssembly.isValid(),is(true));
	}

}
