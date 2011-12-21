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
 
 Created on: 30.03.2005
 *********************************************************************************/

package org.n52.oxf.owsCommon.capabilities;

import org.n52.oxf.*;

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
	 * Example value for providerName: "Institute for Geoinformatics"
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
	 * @throws OXFException
	 *             if parameters are not correct.
	 */
	public ServiceProvider(String providerName, ServiceContact serviceContact)
			  {
		setProviderName(providerName);
		setServiceContact(serviceContact);
	}

	/**
	 * this constructor has all attributes of the class as its parameters.
	 * 
	 * @param providerName
	 * @param serviceContact
	 * @param providerSite
	 * @throws OXFException
	 */
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
	
	
	/**
	 * @return Returns the providerName.
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * @param providerName
	 *            The providerName to set.
	 * @throws IllegalArgumentException
	 *             if providerName.equals("").
	 */
	protected void setProviderName(String providerName) throws IllegalArgumentException {
		if (providerName.equals("")) {
			throw new IllegalArgumentException("The parameter 'providerName' is illegal.");
		} else {
			this.providerName = providerName;
		}
	}

	/**
	 * @return Returns the providerSite.
	 */
	public OnlineResource getProviderSite() {
		return providerSite;
	}

	/**
	 * @param providerSite
	 *            The providerSite to set.
	 */
	protected void setProviderSite(OnlineResource providerSite) {
		this.providerSite = providerSite;
	}

	/**
	 * @return Returns the serviceContactString.
	 */
	public ServiceContact getServiceContact() {
		return serviceContact;
	}

	/**
	 * @param serviceContactString
	 *            The serviceContactString to set.
	 */
	protected void setServiceContact(ServiceContact serviceContact) {
		this.serviceContact = serviceContact;
	}
}