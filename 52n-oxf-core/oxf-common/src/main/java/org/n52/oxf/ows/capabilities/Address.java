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


/**
 * Information about the physical or electonical address of the organization or individual.
 * <br>
 * This class refers to CI_Address of ISO 19115.
 * 
 * @author ISO 19115 and <a href="www.opengeospatial.org">OGC</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class Address {

	/**
	 * city of the location
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String city;

	/**
	 * State, province of the location.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String administrativeArea;

	/**
	 * ZIP or other postal code.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String postalCode;

	/**
	 * Country of the physical address.
	 * <br>
	 * Zero or one (optional) value is possible. <br>
	 */
	private String country;

	/**
	 * Address line for the location (as described in ISO 11180, Annex A).
	 * <br>
	 * Zero or more (optional) values are possible. <br>
	 */
	private String[] deliveryPoints;

	/**
	 * Address of the electronic mailbox of the responsible organization or individual.
	 * <br>
	 * Zero or more (optional) values are possible. <br>
	 */
	private String[] electronicMailAddresses;

	
	/**
	 * this constructor has all attributes of the class as its parameters.
	 * @param city
	 * @param administrativeArea
	 * @param postalCode
	 * @param country
	 * @param deliveryPoints
	 * @param electronicMailAddresses
	 */
	public Address(String city, String administrativeArea, String postalCode,
			String country, String[] deliveryPoint,
			String[] electronicMailAddress) {
		setCity(city);
		setAdministrativeArea(administrativeArea);
		setPostalCode(postalCode);
		setCountry(country);
		setDeliveryPoints(deliveryPoint);
		setElectronicMailAddresses(electronicMailAddress);
	}

	
	/**
	 * @return a XML representation of this Address. 
	 */
	public String toXML(){
		String res = "<Address"
			+ " city=\""+ city + "\""
			+ " administrativeArea=\""+ administrativeArea + "\""
			+ " postalCode=\""+ postalCode + "\""
			+ " country=\""+ country + "\">";
		
		if(deliveryPoints != null){
			for(String s : deliveryPoints){
				res += "<DeliveryPoint>";
				res += s;
				res += "</DeliveryPoint>";
			}
		}
		
		if(electronicMailAddresses != null){
			for(String s : electronicMailAddresses){
				res += "<ElectronicMailAddress>";
				res += s;
				res += "</ElectronicMailAddress>";
			}
		}
		
		res += "</Address>";
		return res;
	}
	
	
	public String getAdministrativeArea() {
		return administrativeArea;
	}

	protected void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getCity() {
		return city;
	}

	protected void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	protected void setCountry(String country) {
		this.country = country;
	}

	public String[] getDeliveryPoints() {
		return deliveryPoints;
	}

	protected void setDeliveryPoints(String[] deliveryPoint) {
		this.deliveryPoints = deliveryPoint;
	}

	public String[] getElectronicMailAddresses() {
		return electronicMailAddresses;
	}

	protected void setElectronicMailAddresses(String[] electronicMailAddress) {
		this.electronicMailAddresses = electronicMailAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	protected void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}