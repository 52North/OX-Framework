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
 * This interface encapsulates the behavior of an Filter which is conform to the OGC Filter Encoding
 * Specification 1.1.0
 * 
 * @author <a href="mailto:c.stasch@52north.org">Christoph Stasch</a>
 * 
 */
public interface IFilter {

    // LogicFilters
    public static final String AND = "And";
    public static final String OR = "Or";
    public static final String NOT = "Not";

    /**
     * enum represents the possible logical comparison operators as defined in OGC Filter Schema
     * 
     * @author Christoph Stasch
     * 
     * @version 0.1
     */
    public enum ComparisonOperators {
        PropertyIsEqualTo, PropertyIsNotEqualTo, PropertyIsLessThan, PropertyIsGreaterThan, PropertyIsLessThanOrEqualTo, PropertyIsGreaterThanOrEqualTo, PropertyIsLike, PropertyIsNull, PropertyIsBetween
    }

    /**
     * returns the type of filter (e.g. PROPERTY_IS_EQUAL_TO)
     * 
     * @return the type of filter (e.g. PROPERTY_IS_EQUAL_TO)
     */
    public String getFilterType();

    /**
     * creates a string representation of the filter in xml-format
     * 
     * @return filter as xml-string
     */
    public String toXML();

}