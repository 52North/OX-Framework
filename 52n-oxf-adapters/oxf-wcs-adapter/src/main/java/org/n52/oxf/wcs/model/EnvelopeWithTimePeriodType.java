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


package org.n52.oxf.wcs.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "EnvelopeWithTimePeriodType", namespace = "http://www.opengis.net/gml")
public class EnvelopeWithTimePeriodType
    extends EnvelopeType
{

    @XmlElement(name = "timePosition", namespace = "http://www.opengis.net/gml", type = TimePositionType.class)
    protected List<TimePositionType> timePosition;
    @XmlAttribute(name = "frame", namespace = "")
    protected String frame;

    protected List<TimePositionType> _getTimePosition() {
        if (timePosition == null) {
            timePosition = new ArrayList<TimePositionType>();
        }
        return timePosition;
    }

    /**
     * Gets the value of the timePosition property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timePosition property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimePosition().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.n52.oxf.wcs.model.TimePositionType}
     *
     */
    public List<TimePositionType> getTimePosition() {
        return this._getTimePosition();
    }

    /**
     * Gets the value of the frame property.
     *
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    public String getFrame() {
        if (frame == null) {
            return "#ISO-8601";
        } else {
            return frame;
        }
    }

    /**
     * Sets the value of the frame property.
     *
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    public void setFrame(String value) {
        this.frame = value;
    }

}
