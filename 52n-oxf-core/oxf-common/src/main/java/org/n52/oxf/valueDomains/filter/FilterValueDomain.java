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

import java.util.*;

import org.n52.oxf.ows.capabilities.*;

/**
 * ValueDomain for OGC Filter Implementation Specification 1.1.0 conform filters, containsValue()-method
 * checks only the FilterType!
 *
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class FilterValueDomain implements IDiscreteValueDomain<IFilter> {

    /** possible filters (only filterType is initialized!!) */
    private ArrayList<IFilter> possibleValues;

    private final String DOMAIN_DESCRIPTION = "Value Domain for Filters";

    public FilterValueDomain() {
        possibleValues = new ArrayList<IFilter>();
    }

    public void addPossibleValue(IFilter possibleValue) {
        possibleValues.add(possibleValue);
    }

    /**
     * @return possible filters (only filterType is initialized!!)
     */
    public List<IFilter> getPossibleValues() {
        return this.possibleValues;
    }

    /**
     * checks if a filter (exactly: only the filterType) is contained in the possible filters (or exactly:
     * filterTypes)
     *
     * @param filter
     *        the filter which should be checked
     *
     * @return true, if the filter is contained
     *
     */
    public boolean containsValue(IFilter filter) {
        boolean isContained = false;

        // if binary LogicFilter (AND or OR)
        if (filter.getFilterType().equals(IFilter.AND) | filter.getFilterType().equals(IFilter.OR)) {

            // test, whether AND or OR is contained in possibleValues
            boolean bool = false;
            for (IFilter f : possibleValues) {
                if (f.getFilterType().equals(filter.getFilterType())) {
                    bool = true;
                }
            }

            // recursive invocation of containsValue() method for leftFilter and rightFilters of this filter
            // isContained is true when ALL filters of this LogicFilter are contained
            isContained = bool && containsValue( ((LogicFilter) filter).getLeftFilter())
                    && containsValueList( ((LogicFilter) filter).getRightFilters());
        }

        // if unary LogicFilter(only NOT)
        else if (filter.getFilterType().equals(IFilter.NOT)) {

            // test, if NOT is valid filter
            boolean bool = false;
            for (IFilter f : possibleValues) {
                if (f.getFilterType().equals(filter.getFilterType())) {
                    bool = true;
                }
            }

            isContained = bool && containsValue( ((LogicFilter) filter).getLeftFilter());

        }

        // if ComparisonFilter, simple test whether filterType is contained in possibleValues
        else
            for (IFilter f : possibleValues) {
                if (f.getFilterType().equals(filter.getFilterType())) {
                    isContained = true;
                }
            }

        return isContained;
    }

    /**
     *
     * @return domain description
     */
    public String getDomainDescription() {
        return DOMAIN_DESCRIPTION;
    }

    /**
     *
     * @return xml-string of this valueDomain
     */
    public String toXML() {
        String result = "";
        for (int i = 0; i < this.possibleValues.size(); ++i) {
            result += this.possibleValues.get(i).toXML();
        }
        return result;
    }

    // public Class getValueType() {
    // return IFilter.class;
    // }

    /**
     * help method for containsValue method, check if filters are contained in possibleValues
     *
     * @param filters
     *        filters which should be checked
     *
     * @return true if all filters are contained in possibleValues
     */
    private boolean containsValueList(ArrayList<IFilter> filters) {
        boolean res = true;

        for (IFilter f : filters) {
            if ( !containsValue(f))
                res = false;
        }
        return res;
    }

    public IFilter produceValue(String... stringArray) {
        // TODO Auto-generated method stub
        return null;
    }

}
