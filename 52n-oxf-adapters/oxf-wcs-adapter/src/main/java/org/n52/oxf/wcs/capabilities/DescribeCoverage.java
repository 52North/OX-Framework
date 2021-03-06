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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b11-EA
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2005.06.25 at 02:38:11 CEST
//


package org.n52.oxf.wcs.capabilities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "coverage"
})
@XmlRootElement(name = "DescribeCoverage", namespace = "http://www.opengis.net/wcs")
public class DescribeCoverage {

    @XmlElement(name = "Coverage", namespace = "http://www.opengis.net/wcs", type = String.class)
    protected List<String> coverage;
    @XmlAttribute(name = "service", namespace = "", required = true)
    protected String service;
    @XmlAttribute(name = "version", namespace = "", required = true)
    protected String version;

    protected List<String> _getCoverage() {
        if (coverage == null) {
            coverage = new ArrayList<String>();
        }
        return coverage;
    }

    /**
     * Gets the value of the coverage property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverage property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoverage().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     *
     */
    public List<String> getCoverage() {
        return this._getCoverage();
    }

    /**
     * Gets the value of the service property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getService() {
        if (service == null) {
            return "WCS";
        } else {
            return service;
        }
    }

    /**
     * Sets the value of the service property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Gets the value of the version property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getVersion() {
        if (version == null) {
            return "1.0.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
