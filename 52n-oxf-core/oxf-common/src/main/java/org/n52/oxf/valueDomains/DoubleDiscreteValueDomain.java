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
package org.n52.oxf.valueDomains;

import java.util.*;

import org.n52.oxf.ows.capabilities.*;

/**
 *
 *
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class DoubleDiscreteValueDomain implements IDiscreteValueDomain<Double> {

    /**
     * List with possible values
     */
    List<Double> possibleValues = null;

    /**
     * description of the valueDomain
     */
    String description = "The DoubleDiscreteValueDomain contains the possible double values.";

    /**
     * Constructor with discrete Values
     *
     * @param possibleValues
     *        ArrayList with discrete Values
     */
    public DoubleDiscreteValueDomain(List<Double> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public DoubleDiscreteValueDomain(Double[] possibleValues) {
        this.possibleValues = new ArrayList<Double>(Arrays.asList(possibleValues));
    }

    /**
     * returns the discrete Values of the DoubleValueDomain
     *
     * @return ArrayList with possible (discrete) values
     */
    public List<Double> getPossibleValues() {
        return possibleValues;
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
        return possibleValues.contains(d);
    }

    /**
     * creates an XML-String of the ValueDomain
     *
     * @return XML-String of this ValueDomain
     */
    public String toXML() {

        String res = "<DoubleDiscreteValueDomain>";
        for (Double c : possibleValues) {
            res += "<xsd:double>" + c + "</xsd:double>";
        }
        res += "</DoubleDiscreteValueDomain>";

        return res;
    }

    public Double produceValue(String... stringArray) {
        // TODO Auto-generated method stub
        return null;
    }


}
