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

package org.n52.oxf.valueDomains.filter;

/**
 * Class represents a comparison filter which is conform to OGC Filter Encoding Specification 1.1.0
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

    /**
     * Constructor with all parameters
     */
    public ComparisonFilter(String filterType, String propertyName) {
        this.filterType = filterType;
        this.propertyName = propertyName;
    }

    public ComparisonFilter(String filterType) {
        this.filterType = filterType;
    }

    /**
     * returns the type of the ComparisonFilter
     * 
     * @return the filterType of this ComparisonFilter (e.g. PROPERTY_IS_LIKE)
     */
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

        String result = "<ogc:" + filterType + " xmlns:ogc=\"http://www.opengis.net/ogc\">\n";
        if (propertyName != null) {
            result += "<ogc:PropertyName>" + this.propertyName + "</ogc:PropertyName>\n";
        }
        result += "<ogc:Literal>...</ogc:Literal>\n";
        result += "</ogc:" + filterType + ">";
        return result;
    }

}