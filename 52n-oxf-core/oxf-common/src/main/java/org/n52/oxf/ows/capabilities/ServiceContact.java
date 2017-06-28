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
package org.n52.oxf.ows.capabilities;

/**
 * Description of person(s) and organizations.<br>
 * <br>
 * This class refers to CI_ResponsibleParty of ISO 19115.
 * 
 * @author ISO 19115 and <a href="www.opengeospatial.org">OGC</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ServiceContact {

	/**
	 * Name of the responsible person-surname, given name, title separated by a delimiter.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String individualName;

	/**
	 *  Address of the responsible party.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String organisationName;

	/**
	 * Position of the contact person.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String positionName;

	/**
	 *  Address of the responsible party.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private Contact contactInfo;

	public ServiceContact(String individualName, String organisationName,
			String positionName, Contact contactInfo) {
		this.individualName = individualName;
		this.organisationName = organisationName;
		this.positionName = positionName;
		this.contactInfo = contactInfo;
	}

	/**
	 * @return a XML representation of this ServiceContact. 
	 */
	public String toXML(){
		String res = "<ServiceContact"
			+ " individualName=\""	+ individualName + "\""
			+ " organisationName=\""+ organisationName + "\""
			+ " positionName=\""	+ positionName + "\">";
		
		if(contactInfo != null) res += contactInfo.toXML();
		
		res += "</ServiceContact>";
		return res;
	}
	
	public Contact getContactInfo() {
		return contactInfo;
	}

	protected void setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getIndividualName() {
		return individualName;
	}

	protected void setIndividualName(String individualName) {
		this.individualName = individualName;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	protected void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public String getPositionName() {
		return positionName;
	}

	protected void setPositionName(String positionName) {
		this.positionName = positionName;
	}
}