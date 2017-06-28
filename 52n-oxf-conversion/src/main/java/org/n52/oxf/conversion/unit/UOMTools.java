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
package org.n52.oxf.conversion.unit;

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.conversion.unit.aixm.FlightLevelUnitConverter;
import org.n52.oxf.conversion.unit.ucum.UCUMTools;
import org.n52.oxf.conversion.unit.ucum.UCUMTools.UnitConversionFailedException;


/**
 * Unit Conversion helper class.
 */
public class UOMTools {

	public static final String METER_UOM = "m";
	private static Map<String, CustomUnitConverter> customUnitConverters;

	static {
		customUnitConverters = new HashMap<String, CustomUnitConverter>();
		customUnitConverters.put("FL", new FlightLevelUnitConverter());
		customUnitConverters.put("FT", new ProprietaryAIXMFeetUnitConverter());
	}


	public static double convertToTargetUnit(double doubleValue, String sourceUom,
			String targetUom) {
		if (customUnitConverters.containsKey(sourceUom)) {
			NumberWithUOM preProcessed = customUnitConverters.get(sourceUom).convert(doubleValue);
			try {
				return UCUMTools.convert(preProcessed.getUom(), targetUom, preProcessed.getValue()).getValue();
			} catch (UnitConversionFailedException e) {
				return preProcessed.getValue();
			}
		}

		try {
			return UCUMTools.convert(sourceUom, targetUom, doubleValue).getValue();
		} catch (UnitConversionFailedException e) {
			return doubleValue;
		}
	}

	public static double convertToBaseUnit(double doubleValue, String sourceUom) {
		String target;
		if (customUnitConverters.containsKey(sourceUom)) {
			target = customUnitConverters.get(sourceUom).getBaseUnit();
		}
		else {
			target = UCUMTools.getBaseUnit(sourceUom).getUCUMExpression();
		}
		return convertToTargetUnit(doubleValue, sourceUom, target);
	}

	public static void addCustomUnitConverter(CustomUnitConverter c) {
		customUnitConverters.put(c.getUnitString(), c);
	}

}
