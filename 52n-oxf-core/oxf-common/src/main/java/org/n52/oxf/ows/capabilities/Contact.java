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
 * Information which is needed to contact the responsible organization/person.
 * <br>
 * This class refers to CI_Contact of ISO 19115.
 * 
 * @author ISO 19115 and <a href="www.opengeospatial.org">OGC</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class Contact {

	/**
	 * Telephone numbers of the responsible individual or organization.
	 * <br>
	 * Zero or more (optional) values are possible. <br>
	 */
	private String[] telephone;

	/**
	 * Facsimile numbers of the responsible individual or organization.
	 * <br>
	 * Zero or more (optional) values are possible. <br>
	 */
	private String[] fax;

	/**
	 * Time period when you can contact the individual or organization.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String hoursOfService;

	/**
	 * Supplemental instructions to contact the organization or individual.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String contactInstructions;

	/**
	 * Physical and email address at which the organization or individual may be contacted.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private Address address;

	/**
	 * On-line resource to contact the individual or organization.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private OnlineResource onlineResource;

	public Contact(String[] telephone, String[] fax, String hoursOfService,
			String contactInstructions, Address address,
			OnlineResource onlineResource) {
		setTelephone(telephone);
		setFax(fax);
		setHoursOfService(hoursOfService);
		setContactInstructions(contactInstructions);
		setAddress(address);
		setOnlineResource(onlineResource);
	}
	
	/**
	 * @return a XML representation of this Contact. 
	 */
	public String toXML(){
		String res = "<Contact"
			+ " hoursOfService=\""+ hoursOfService + "\""
			+ " contactInstructions=\""	+ contactInstructions + "\">";
		
		if(fax != telephone) {
			for(String s : telephone){
				res += "<Telephone>";
				res += s;
				res += "</Telephone>";
			}
		}
		
		if(fax != null) {
			for(String s : fax){
				res += "<Fax>";
				res += s;
				res += "</Fax>";
			}
		}
		
        if(address != null) {
            res += address.toXML();
        }
        
        if(onlineResource != null) {
			res += onlineResource.toXML();
        }
		
		res += "</Contact>";
		return res;
	}
	

	public Address getAddress() {
		return address;
	}

	protected void setAddress(Address address) {
		this.address = address;
	}

	public String getContactInstructions() {
		return contactInstructions;
	}

	protected void setContactInstructions(String contactInstructions) {
		this.contactInstructions = contactInstructions;
	}

	public String[] getFax() {
		return fax;
	}

	protected void setFax(String[] fax) {
		this.fax = fax;
	}

	public String getHoursOfService() {
		return hoursOfService;
	}

	protected void setHoursOfService(String hoursOfService) {
		this.hoursOfService = hoursOfService;
	}

	public OnlineResource getOnlineResource() {
		return onlineResource;
	}

	protected void setOnlineResource(OnlineResource onlineResource) {
		this.onlineResource = onlineResource;
	}

	public String[] getTelephone() {
		return telephone;
	}

	protected void setTelephone(String[] telephone) {
		this.telephone = telephone;
	}
}