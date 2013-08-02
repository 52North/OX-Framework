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