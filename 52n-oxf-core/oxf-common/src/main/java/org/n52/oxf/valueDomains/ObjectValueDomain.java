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



public class ObjectValueDomain implements IDiscreteValueDomain<Object> {

    private List<Object> possibleValues;

    private final String DOMAIN_DESCRIPTION="Generic ValueDomain for objects of type Object";


    public ObjectValueDomain(List<Object> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public List<Object> getPossibleValues() {
        return this.possibleValues;
    }

    public boolean containsValue(Object object) {
        for (Object o : possibleValues) {
            if (o.equals(this)) {
                return true;
            }
        }
        return false;
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
        String result="";
        for (int i=0; i<this.possibleValues.size();++i) {
            result += this.possibleValues.get(i).toString();
        }
        return result;
    }


    public Object produceValue(String... stringArray) {
        // TODO Auto-generated method stub
        return null;
    }

}
