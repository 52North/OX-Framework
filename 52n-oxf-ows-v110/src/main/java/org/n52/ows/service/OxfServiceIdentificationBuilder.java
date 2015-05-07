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
package org.n52.ows.service;


public class OxfServiceIdentificationBuilder {
    
    private String[] titles;
    
    private String[] abstracts;
    
    private String[] keywords;

    private String serviceType;
    
    private String[] serviceTypeVersion;
    
    private String[] profiles;
    
    private String fees;
    
    private String[] accessConstraints;
    
    public OxfServiceIdentificationBuilder(String serviceType, String... serviceTypeVersion) {
        this.serviceType = serviceType;
        this.serviceTypeVersion = serviceTypeVersion;
    }
    
    public OxfServiceIdentificationBuilder addTitles(String... titles) {
        this.titles = titles;
        return this;
    }
    
    public OxfServiceIdentificationBuilder addAbstracts(String... abstracts) {
        this.abstracts = abstracts;
        return this;
    }
    
    public OxfServiceIdentificationBuilder addKeywords(String... keywords) {
        this.keywords = keywords;
        return this;
    }
    
    public OxfServiceIdentificationBuilder addProfiles(String... profiles) {
        this.profiles = profiles;
        return this;
    }
    
    public OxfServiceIdentificationBuilder addFees(String fees) {
        this.fees = fees;
        return this;
    }
    
    public OxfServiceIdentificationBuilder addAccessConstraints(String... accessConstraints) {
        this.accessConstraints = accessConstraints;
        return this;
    }

    public OxfServiceIdentification build() {
        return new OxfServiceIdentification(this);
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public String[] getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String[] abstracts) {
        this.abstracts = abstracts;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String[] getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public String getFees() {
        return fees;
    }

    public String[] getAccessConstraints() {
        return accessConstraints;
    }
    
}