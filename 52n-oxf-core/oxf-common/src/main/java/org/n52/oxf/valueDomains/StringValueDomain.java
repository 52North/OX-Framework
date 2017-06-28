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
