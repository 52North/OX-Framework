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
package org.n52.oxf.conversion.gml32.geometry;

/**
 * Class representing altitudes with limits.
 */
public class AltitudeLimits {

	/**
	 * Enumeration defining the common altitude reference definitions (derived from AIXM 5.1)
	 */
	public static enum AltitudeReferences {SFC, STD, FL, MSL, W84 }
	
	private AltitudeReferences lowerLimitReference;
	private AltitudeReferences upperLimitReference;
	private double lowerLimit;
	private double upperLimit;
	private String lowerLimitUom;
	private String upperLimitUom;

	
	public AltitudeLimits(double lowerLimit2,
			AltitudeReferences lowerReference, double upperLimit2,
			AltitudeReferences upperReference) {
		this.lowerLimit = lowerLimit2;
		this.upperLimit = upperLimit2;
		this.lowerLimitReference = lowerReference;
		this.upperLimitReference = upperReference;
	}


	public AltitudeReferences getLowerLimitReference() {
		return lowerLimitReference;
	}


	public AltitudeReferences getUpperLimitReference() {
		return upperLimitReference;
	}


	public double getLowerLimit() {
		return lowerLimit;
	}


	public double getUpperLimit() {
		return upperLimit;
	}


	public String getLowerLimitUom() {
		return lowerLimitUom;
	}


	public String getUpperLimitUom() {
		return upperLimitUom;
	}
	
	
}
