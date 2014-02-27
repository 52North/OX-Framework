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
package org.n52.oxf.conversion.unit.test;

import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.conversion.unit.NumberWithUOM;
import org.n52.oxf.conversion.unit.ucum.UCUMTools;
import org.n52.oxf.conversion.unit.ucum.UCUMTools.UnitConversionFailedException;
import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;

public class TestUnitParsing {

	@Test
	public void testParsing() {
		UnitParserUCUM ucom = new UnitParserUCUM();
		Unit unit = ucom.getUnit("m");
		Assert.assertTrue("Could not get unit", unit != null);
	}
	
	@Test
	public void testConversion() throws UnitConversionFailedException {
		double value = 10000d;
		NumberWithUOM result = UCUMTools.convert("mm", "m", value);
		Assert.assertTrue("Unexpected conversion result.", result.getValue() == 10);
	}
	
}
