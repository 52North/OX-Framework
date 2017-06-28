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
package org.n52.ows.service;

import net.opengis.ows.x11.CodeType;
import net.opengis.ows.x11.ServiceIdentificationDocument;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;

public class OxfServiceIdentification {

    private ServiceIdentification serviceIdentification;

    public OxfServiceIdentification(OxfServiceIdentificationBuilder builder) {
        this.serviceIdentification = ServiceIdentification.Factory.newInstance();
    }

    public OxfServiceIdentification(ServiceIdentification serviceIdentification) {
        this.serviceIdentification = serviceIdentification;
    }

    public void setServiceIdentification(ServiceIdentification serviceIdentification) {
        this.serviceIdentification = serviceIdentification;
    }

    public ServiceIdentification getServiceIdentification() {
        return serviceIdentification;
    }

    public ServiceIdentificationDocument getServiceIdentificationAsDocument() {
        ServiceIdentificationDocument document = ServiceIdentificationDocument.Factory.newInstance();
        document.setServiceIdentification(this.serviceIdentification);
        return document;
    }


//    public void setKeywords(String[] keywords) {
//        this.serviceIdentification.setKeywordsArray(keywords);
//    }

    public void setServiceType(String serviceType, String codeSpace) {
        CodeType codeType = CodeType.Factory.newInstance();
        codeType.setStringValue(serviceType);
        codeType.setCodeSpace(codeSpace);
        this.serviceIdentification.setServiceType(codeType);
    }

    public void setServiceTypeVersion(String... serviceTypeVersion) {
        this.serviceIdentification.setServiceTypeVersionArray(serviceTypeVersion);
    }

    public void setProfiles(String[] profiles) {
        this.serviceIdentification.setProfileArray(profiles);
    }

    public void setFee(String fees) {
        this.serviceIdentification.setFees(fees);
    }

    public void setAccessConstraints(String[] accessConstraints) {
        this.serviceIdentification.setAccessConstraintsArray(accessConstraints);
    }


}
