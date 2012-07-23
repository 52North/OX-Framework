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