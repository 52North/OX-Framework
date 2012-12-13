/**
 * Copyright (C) 2012
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

}
