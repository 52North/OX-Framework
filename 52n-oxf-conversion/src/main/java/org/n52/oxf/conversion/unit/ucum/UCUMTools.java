/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
