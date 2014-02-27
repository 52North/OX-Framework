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
package org.n52.oxf.ows.capabilities;

import org.n52.oxf.OXFException;

/**
 * You can use this class "DatasetParameter" to associate a Parameter with a
 * specific dataset (that means a Dataset).
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class DatasetParameter extends Parameter {

	/**
	 * 
	 */
	private Dataset dataID;

	/**
	 * @param serviceSidedName
	 *            the serviceSidedName of the parameter
	 * @param required
	 *            indicates if a ParameterContainer is required.
	 * @param valueDomain
	 *            holds all possible values.
	 * @param commonName
	 *            the "commonName" is used internally by the OX-framework to
	 *            address this parameter.<br>
	 *            ATTENTION: if the parameter has no corresponding commonName
	 *            please set it on <code>null</code>.
	 * @param dataID
	 * @throws OXFException
	 */
	public DatasetParameter(String name, boolean required,
			IValueDomain valueDomain, Dataset dataID,
			String commonName) {
		super(name, required, valueDomain, commonName);

		this.dataID = dataID;
	}

	public Dataset getAssociatedDataset() {
		return dataID;
	}

	/**
	 * @return a XML representation of this Parameter.
	 */
	public String toXML() {
		String res = "<DatasetParameter dataID=\""+ dataID.getIdentifier() + "\" serviceSidedName=\"" + serviceSidedName
				+ "\" required=\"" + required + "\" commonName=\""
				+ this.getCommonName() + "\">";

		res += "<ValueDomain>";
		if (valueDomain != null) {
			res += valueDomain.toXML();
		}
		res += "</ValueDomain>";

		res += "</DatasetParameter>";

		return res;
	}
}