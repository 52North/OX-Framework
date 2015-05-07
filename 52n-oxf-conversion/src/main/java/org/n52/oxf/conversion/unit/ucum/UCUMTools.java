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
package org.n52.oxf.conversion.unit.ucum;

import org.n52.oxf.conversion.unit.NumberWithUOM;
import org.vast.unit.*;

/**
 * Unit conversion utils based on the Unified Code for
 * Units of Measure: http://unitsofmeasure.org
 */
public class UCUMTools {
	

	private static UnitParser parser;

	static {
		parser = new UnitParserUCUM();
	}

	/**
	 * Converts the number to its base unit (e.g. kilometers to meters)
	 */
	public static NumberWithUOM convert(String sourceUnitString, double value) {
		Unit sourceUnit = parser.getUnit(sourceUnitString);
		return convert(sourceUnit, sourceUnit.getCompatibleSIUnit(), value);

	}
	
	/**
	 * Converts the number to the target unit
	 */
	private static NumberWithUOM convert(Unit sourceUnit, Unit targetUnit,
			double value) {
		double resultValue = UnitConversion.getConverter(sourceUnit,
				targetUnit).convert(value);
		return new NumberWithUOM(resultValue, targetUnit.getUCUMExpression());
	}

	/**
	 * Converts the number to the target unit. Inputs are Strings
	 * representing UCUM codes.
	 */
	public static NumberWithUOM convert(String sourceUnitString, String targetUnitString, double value) throws UnitConversionFailedException {
		Unit sourceUnit = parser.getUnit(sourceUnitString);
		Unit targetUnit = parser.getUnit(targetUnitString);
		return convert(sourceUnit, targetUnit, value);
	}

	/**
	 * Checks if to UCUM codes are compatible (= having the same base unit)
	 */
	public static boolean isCompatible(String unitString1, String unitString2) {
		//build units
		Unit u1 = parser.getUnit(unitString1);
		Unit u2 = parser.getUnit(unitString2);
		
		if (u1 == null || u2 == null) {
			//error: unit not found
			return false;
		}
		
		return u1.isCompatible(u2);
	}
	

	/**
	 * Returns the compatible SI unit.
	 */
	public static Unit getBaseUnit(String unitString) {
		return parser.getUnit(unitString).getCompatibleSIUnit();
	}
	

	public static class UnitConversionFailedException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UnitConversionFailedException(String string) {
			super(string);
		}
		
	}



}
