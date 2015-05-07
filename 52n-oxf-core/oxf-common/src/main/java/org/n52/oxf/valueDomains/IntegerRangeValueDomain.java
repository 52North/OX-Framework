/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
public class IntegerRangeValueDomain implements IRangeValueDomain<Integer> {
	
    /**
     * minimum.
     */
	Integer min = null;
	
    /**
     * maximum.
     */
    Integer max = null;
    
    /**
     * description of the valueDomain
     */
    String description = "The IntegerRangeValueDomain contains a min and max value of a range of integer values.";
    
    
    /**
     * @param min
     *            minvalue of the value range
     * @param max
     *            maxvalue of the value range
     */
	public IntegerRangeValueDomain(int min, int max){
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
     * test, whether a double value is contained in this valueDomain
     * 
     * @param obj
     *            the value which has to be tested (double in this case)
     * @return true, if value is contained
     */
    public boolean containsValue(Integer i) {
        return min <= i  &&  max >= i;
    }

    /**
     * creates an XML-String of the ValueDomain
     * 
     * @return XML-String of this ValueDomain
     */
    public String toXML() {
        
        String res = "<IntegerRangeValueDomain>";
        
        res += "<min><xsd:unsignedLong>" + min + "</xsd:unsignedLong></min>";
        res += "<max><xsd:unsignedLong>" + max + "</xsd:unsignedLong></max>";

        res += "</IntegerRangeValueDomain>";
        return res;
    }

//    public Class<Integer> getValueType() {
//        return Integer.class;
//    }

    public Integer getMaxValue() {
        return max;
    }

    public Integer getMinValue() {
        return min;
    }
    
    public Integer produceValue(String... stringArray){
        return Integer.parseInt(stringArray[0]);
    }
}