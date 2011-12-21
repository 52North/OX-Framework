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
 
 Created on: 15.04.2005
 *********************************************************************************/

package org.n52.oxf.owsCommon.capabilities;

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

	
	/**
	 * @param individualName
	 * @param organisationName
	 * @param positionName
	 * @param contactInfo
	 */
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