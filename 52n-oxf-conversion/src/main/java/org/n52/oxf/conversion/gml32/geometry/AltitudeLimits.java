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
