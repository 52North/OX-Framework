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