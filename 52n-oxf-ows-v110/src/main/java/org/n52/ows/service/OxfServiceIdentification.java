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
