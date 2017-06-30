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

import java.util.ArrayList;

/**
 * Class represents a logical filter which is conform to OGC Filter
 * Encoding Specification 1.1.0
 *
 * @author <a href="mailto:staschc@52north.org">Christoph Stasch</a>
 *
 */
public class LogicFilter implements IFilter {

    /** First Filter of the LogicFilter*/
    private IFilter leftFilter;

    /** Second and following Filters of the LogicFilter (only for AND and OR Filters)*/
    private ArrayList<IFilter> rightFilters;

    /**Type of LogicFilter (one of the filter types of this class. e.g. NOT)*/
    private final String filterType;

    /**
     * Constructor with filterType as parameter
     *
     * @param filterType
     *              one of the three logical filter types
     */
    public LogicFilter(String filterType){
        this.filterType = filterType;
    }

    /**
     * Constructor with all parameters, should be used as constructor for
     * AND and OR filters
     *
     * @param filterType
     *          type of this LogicFilter (AND or OR)
     * @param leftFilter
     *          firstFilter of the LogicFilter
     * @param rightFilters
     *          second and following filters of the logical filter
     */
    public LogicFilter(String filterType, IFilter leftFilter, ArrayList<IFilter> rightFilters){
        this.filterType = filterType;
        this.leftFilter = leftFilter;
        this.rightFilters = rightFilters;
    }

    /**
     * Constructor only useable for NOT Filters
     *
     * @param filterType
     *              logical filtertype (only NOT!!)
     * @param leftFilter
     *              Filter which should be applied of a NOT Filter
     */
    public LogicFilter(String filterType, IFilter leftFilter) {
        this.filterType=IFilter.NOT;
        this.leftFilter=leftFilter;
    }

    /**
     * returns the type of this logical filter
     *
     * @return the type of this logical filter (e.g. NOT)
     */
    @Override
    public String getFilterType() {
        return filterType;
    }

    /**
     *
     * @return the first filter of this LogicFilter
     */
    public IFilter getLeftFilter() {
        return this.leftFilter;
    }

    /**
    *
    * @return the second filter and following filters of this LogicFilter as ArrayList
    */
    public ArrayList<IFilter> getRightFilters() {
        return this.rightFilters;
    }


    /**
     * creates a string representation of the logical filter (without &lt;ogc:Filter&gt; begin and end-tag!)
     * in xml-format
     *
     * @return logical filter as xml-string
     */
    @Override
    public String toXML() {

        String result = "<"+filterType+">";
        result += leftFilter.toXML();

        //Adding second and following Filters to xml string
        if (!filterType.equals(IFilter.NOT)) {
            StringBuilder sb = new StringBuilder(result);
            for (IFilter f: rightFilters){
                sb.append(f.toXML());
            }
            result = sb.toString();
        }

        result += "</"+filterType+">";

        return result;
    }

}
