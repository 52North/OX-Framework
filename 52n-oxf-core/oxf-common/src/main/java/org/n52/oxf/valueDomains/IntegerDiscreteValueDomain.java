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

import java.util.*;

import org.n52.oxf.ows.capabilities.*;


/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class IntegerDiscreteValueDomain implements IDiscreteValueDomain<Integer> {

    /**
     * List with possible values (if this value domain has discrete values
     */
    List<Integer> possibleValues = null;
    
    /**
     * description of the valueDomain
     */
    String description = "The DiscreteIntegerValueDomain contains the possible Integer-values.";
    
    /**
     */
    public IntegerDiscreteValueDomain(Integer possibleValue) {
        this.possibleValues = new ArrayList<Integer>();
        possibleValues.add(possibleValue);
    }
    
    /**
     * @param possibleValues
     *            ArrayList with discrete Integer-Values
     */
    public IntegerDiscreteValueDomain(List<Integer> possibleValues) {
        this.possibleValues = possibleValues;
    }
    
    /**
     * @param possibleValues
     *            ArrayList with discrete Integer-Values
     */
    public IntegerDiscreteValueDomain(Integer[] possibleValues) {
        this.possibleValues = new ArrayList<Integer>(Arrays.asList(possibleValues));
    }
    
    /**
     * @param i Integer to be added to this IntegerValueDomain.
     */
    public void add(Integer i){
        possibleValues.add(i);
    }
    
    /**
     * @return ArrayList with possible (discrete) Integer-values
     */
    public List<Integer> getPossibleValues() {
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
     * test, whether a Integer-value is contained in this valueDomain
     * 
     * @param n
     *            the value which has to be tested
     * @return true, if n is contained
     */
    public boolean containsValue(Integer n) {
        return possibleValues.contains(n);
    }
    
    /**
     * creates an XML-String of the ValueDomain
     * 
     * @return XML-String of this ValueDomain
     */
    public String toXML() {
        
        String res = "<IntegerDiscreteValueDomain>";
        for(Integer c : possibleValues ){
            res += "<xsd:unsignedLong>"
                + c
                + "</xsd:unsignedLong>";
        }
        res += "</IntegerDiscreteValueDomain>";
        
        return res;
    }
    
//    public Class<Integer> getValueType() {
//        return Integer.class;
//    }

    public Integer produceValue(String... stringArray) {
        return Integer.parseInt(stringArray[0]);
    }
}