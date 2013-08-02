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


public class StringValueDomain implements IDiscreteValueDomain<String> {

	List<String> possibleValues;
	
    public StringValueDomain(){
        possibleValues = new ArrayList<String>();
    }
    
	public StringValueDomain(String possibleValue){
		possibleValues = new ArrayList<String>();
		possibleValues.add(possibleValue);
	}
	
	public StringValueDomain(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}
	
	public StringValueDomain(String[] possibleValues){
		this.possibleValues = new ArrayList<String>(Arrays.asList(possibleValues));
	}
	
	public void addPossibleValue(String possibleValue){
		possibleValues.add(possibleValue);
	}
	
	/**
	 * checks if this StringValueDomain contains the parameter string. The case
	 * of the string will be ignored.
	 */
	public boolean containsValue(String string) {
		boolean res = false;

		for (String s : possibleValues) {
			if (s.equalsIgnoreCase(string)) {
				res = true;
			}
		}
		return res;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public String getDomainDescription() {
		return "value domain for nominal values...";
	}

	public String toXML() {
		String res = "<StringValueDomain>";
		
		for(String s : possibleValues){
			res += "<PossibleValue>";
			res += s;
			res += "</PossibleValue>";
		}
		
		res += "</StringValueDomain>";
		
		return res;
	}

//    /**
//     * @return the type of values which can be added to this IValueDomain.
//     */
//    public Class<String> getValueType() {
//        return String.class;
//    }

    public String produceValue(String... stringArray) {
        return stringArray[0];
    }
	
}