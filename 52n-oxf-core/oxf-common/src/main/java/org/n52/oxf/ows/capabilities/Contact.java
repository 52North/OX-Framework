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

    
	/**
	 * this constructor has all attributes of the class as its parameters.
	 * @param telephone
	 * @param fax
	 * @param hoursOfService
	 * @param contactInstructions
	 * @param address
	 * @param onlineResource
	 */
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