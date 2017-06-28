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
package org.n52.oxf.valueDomains.filter;

/**
 * Class represents a comparison filter which is conform to OGC Filter Encoding
 * Specification 1.1.0
 * 
 * 
 */
public class ComparisonFilter implements IFilter {

	// ComparisonFilter types
	public static final String PROPERTY_IS_EQUAL_TO = "PropertyIsEqualTo";
	public static final String PROPERTY_IS_NOT_EQUAL_TO = "PropertyIsNotEqualTo";
	public static final String PROPERTY_IS_LESS_THAN = "PropertyIsLessThan";
	public static final String PROPERTY_IS_GREATER_THAN = "PropertyIsGreaterThan";
	public static final String PROPERTY_IS_LESS_THAN_OR_EQUAL_TO = "PropertyIsLessThanOrEqualTo";
	public static final String PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO = "PropertyIsGreaterThanOrEqualTo";
	public static final String PROPERTY_IS_LIKE = "PropertyIsLike";
	public static final String PROPERTY_IS_NULL = "PropertyIsNull";
	public static final String PROPERTY_IS_BETWEEN = "PropertyIsBetween";

	/** Type of Comparisonfilter (one of the filter types of this class) */
	private String filterType;

	/** propertyName of the ComparisonFilter */
	private String propertyName;

	public ComparisonFilter(String filterType, String propertyName) {
		this.filterType = filterType;
		this.propertyName = propertyName;
	}

	public ComparisonFilter(String filterType) {
		this.filterType = filterType;
	}

	@Override
	public String getFilterType() {
		return filterType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String toString() {
		return getFilterType();
	}

	/**
	 * creates a string representation of this filter in xml-format.<br>
	 * Example A:
	 * 
	 * <pre>
	 *    &lt;Filter&gt;
	 *       &lt;PropertyIsLessThan&gt;
	 *           &lt;PropertyName&gt;DEPTH&lt;/PropertyName&gt;
	 *           &lt;Literal&gt;30&lt;/Literal&gt;
	 *       &lt;/PropertyIsLessThan&gt;
	 *    &lt;/Filter&gt;
	 * </pre>
	 * 
	 * @return string representation of this filter in xml-Format
	 */
	public String toXML() {

		String result = "<ogc:" + filterType
				+ " xmlns:ogc=\"http://www.opengis.net/ogc\">\n";
		if (propertyName != null) {
			result += "<ogc:PropertyName>" + this.propertyName
					+ "</ogc:PropertyName>\n";
		}
		result += "<ogc:Literal>...</ogc:Literal>\n";
		result += "</ogc:" + filterType + ">";
		return result;
	}

}