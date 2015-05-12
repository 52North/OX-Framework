/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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


/**
 * This describes the provider of the service. please mind, that the contact
 * representation is only a String.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ServiceProvider {

	/**
	 * Unique identifier for service provider organization. <br>
	 * <br>
	 * One (mandatory) value for providerName is required. <br>
	 * <br>
	 * Example value for providerName: "52&deg;North"
	 */
	private String providerName;

	/**
	 * Reference to the most relevant web site of the service provider. <br>
	 * <br>
	 * Zero or one (optional) value for providerSite is possible. <br>
	 */
	private OnlineResource providerSite;

	/**
	 * Information for contacting service provider. <br>
	 * <br>
	 * One (mandatory) value for serviceContact is required. <br>
	 */
	private ServiceContact serviceContact;

	/**
	 * Constructor which has got the 'required' attributes for the
	 * ServiceProvider-Section as its parameters. The optional attribute
	 * providerSite will stay null.
	 * 
	 * @param providerName
	 *            Unique identifier for service provider organization.
	 * @param serviceContact
	 *            Information for contacting service provider.
	 * @throws IllegalArgumentException
	 *             if parameters are not correct.
	 */
	public ServiceProvider(String providerName, ServiceContact serviceContact)
			  {
		setProviderName(providerName);
		setServiceContact(serviceContact);
	}

	public ServiceProvider(String providerName, ServiceContact serviceContact,
			OnlineResource providerSite)   {
		setProviderName(providerName);
		setServiceContact(serviceContact);
		setProviderSite(providerSite);
	}

	
	/**
	 * @return a XML representation of this ServiceProvider-section. 
	 */
	public String toXML(){
		String res = "<ServiceProvider providerName=\"" + providerName + "\">";
		
		if(serviceContact != null) res += serviceContact.toXML();
		
		if(providerSite != null) res += providerSite.toXML();
		
		res += "</ServiceProvider>";
		return res;
	}
	
	
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName
	 *            The providerName to set.
	 * @throws IllegalArgumentException
	 *             if providerName is null or empty.
	 */
	protected void setProviderName(String providerName) throws IllegalArgumentException {
		if (providerName == null || providerName.isEmpty()) {
			throw new IllegalArgumentException("The parameter 'providerName' is null or empty.");
		} else {
			this.providerName = providerName;
		}
	}

	public OnlineResource getProviderSite() {
		return providerSite;
	}

	protected void setProviderSite(OnlineResource providerSite) {
		this.providerSite = providerSite;
	}

	public ServiceContact getServiceContact() {
		return serviceContact;
	}

	protected void setServiceContact(ServiceContact serviceContact) {
		this.serviceContact = serviceContact;
	}
}