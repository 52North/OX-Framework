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
 
 Created on: 21.06.2005
 *********************************************************************************/
package org.n52.oxf.valueDomains;

import org.n52.oxf.owsCommon.capabilities.*;



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