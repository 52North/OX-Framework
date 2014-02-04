/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.valueDomains;

import org.n52.oxf.ows.capabilities.*;


/**
 * 
 * 
 * @author <a href="mailto:staschc@uni-muenster.de">Christoph Stasch</a>
 * 
 */
public class DoubleRangeValueDomain implements IRangeValueDomain<Double> {

    /**
     * min value
     */
    Double min = null;

    /**
     * max value
     */
    Double max = null;

    /**
     * description of the valueDomain
     */
    String description = "The DoubleRangeValueDomain contains a min and max double value of a valuerange.";

    /**
     * Constructor with min and max value
     * 
     * @param min
     *        minvalue of the value range
     * @param max
     *        maxvalue of the value range
     */
    public DoubleRangeValueDomain(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * gives a description of this ValueDomain
     * 
     * @return String with description
     */
    public String getDomainDescription() {
        return description;
    }

    /**
     * tests whether a double value is contained in this valueDomain
     * 
     * @param d
     *        the value which has to be tested
     * @return true, if value is contained
     */
    public boolean containsValue(Double d) {
        return min <= d && max >= d;
    }

    /**
     * creates an XML-String of the ValueDomain
     * 
     * @return XML-String of this ValueDomain
     */
    public String toXML() {

        String res = "<DoubleRangeValueDomain>";
        
        res += "<min><xsd:double>" + min + "</xsd:double></min>";
        res += "<max><xsd:double>" + max + "</xsd:double></max>";
        
        res += "</DoubleRangeValueDomain>";

        return res;
    }

    // public Class<Double> getValueType() {
    // return Double.class;
    // }

    public Double getMaxValue() {
        return max;
    }

    public Double getMinValue() {
        return min;
    }

    public Double produceValue(String... stringArray) {
        // TODO Auto-generated method stub
        return null;
    }

}