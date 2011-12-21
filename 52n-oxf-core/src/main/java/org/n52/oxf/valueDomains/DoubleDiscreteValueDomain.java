/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 24.08.2005
 *********************************************************************************/

package org.n52.oxf.valueDomains;

import java.util.*;
import org.n52.oxf.owsCommon.capabilities.*;

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
    ArrayList<Double> possibleValues = null;

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
    public DoubleDiscreteValueDomain(ArrayList<Double> possibleValues) {
        this.possibleValues = possibleValues;
    }

    /**
     * returns the discrete Values of the DoubleValueDomain
     * 
     * @return ArrayList with possible (discrete) values
     */
    public ArrayList getPossibleValues() {
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