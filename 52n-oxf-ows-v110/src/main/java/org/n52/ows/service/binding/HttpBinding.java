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
package org.n52.ows.service.binding;

import static org.n52.ows.service.binding.HttpMethod.GET;
import net.opengis.ows.x11.HTTPDocument;
import net.opengis.ows.x11.HTTPDocument.HTTP;
import net.opengis.ows.x11.RequestMethodType;

import org.w3.x1999.xlink.TypeType.Enum;

public class HttpBinding {
    
    private String externalDcpUrl;
    
    private String externalGetCapabilitiesGETDcpUrl = externalDcpUrl; // default
    
    private HttpMethod httpMethod;
    
    private String conformanceClass;
    
    /* flags extension operation not defined within spec */
    private boolean extension = false;
    
    private HttpBinding() {
        // needed for injection
    }

    public HttpBinding(String externalDcpUrl, String httpMethod) {
        this();
        this.externalDcpUrl = externalDcpUrl;
        this.httpMethod = HttpMethod.getForString(httpMethod);
    }

    public String getExternalDcpUrl() {
        return externalDcpUrl;
    }

    public void setExternalDcpUrl(String externalDcpUrl) {
        this.externalDcpUrl = externalDcpUrl;
    }
    
    public String getExternalGetCapabilitiesGETDcpUrl() {
        return externalGetCapabilitiesGETDcpUrl;
    }
    
    public void setExternalGetCapabilitiesGETDcpUrl(String externalGetCapabilitiesGETDcpUrl) {
        this.externalGetCapabilitiesGETDcpUrl = externalGetCapabilitiesGETDcpUrl;
    }

    public String getHttpMethod() {
        return httpMethod.name();
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = HttpMethod.getForString(httpMethod);
    }
    
    public String getConformanceClass() {
        return conformanceClass;
    }
    
    public void setConformanceClass(String conformanceClass) {
        this.conformanceClass = conformanceClass;
    }
    
    public boolean isExtension() {
        return this.extension;
    }
    
    public void setExtension(boolean extension) {
        this.extension = extension;
    }
    
    public HTTPDocument getHttpInfo() {
        return getHttpInfo("");
    }
    
    /**
     * @param resource an extension string for a base http binding.
     */
    public HTTPDocument getHttpInfo(String resource) {
        HTTPDocument httpDocument = HTTPDocument.Factory.newInstance();
        HTTP http = httpDocument.addNewHTTP();
        RequestMethodType method = (httpMethod == GET) ? http.addNewGet() : http.addNewPost();
        String concatenatedUrl = concatenateToDcpUrl(resource);
        method.setHref(concatenatedUrl);
        method.setType(Enum.forString("simple"));
        return httpDocument;
    }

    private String concatenateToDcpUrl(String resource) {
        if (isPathSeparatorNeeded(resource)) {
            return externalDcpUrl + "/" + resource;
        } else {
            return externalDcpUrl + resource;
        }
    }

    private boolean isPathSeparatorNeeded(String resource) {
        return !externalDcpUrl.endsWith("/") && !resource.startsWith("/");
    }
    
    /**
     * @return the GetCapabilites GET binding (mandatory for all bindings)
     */
    public HTTPDocument getGetCapabilitiesHttpInfoGET() {
        HTTPDocument httpDocument = HTTPDocument.Factory.newInstance();
        RequestMethodType method = httpDocument.addNewHTTP().addNewGet();
        method.setHref(externalGetCapabilitiesGETDcpUrl);
        method.setType(Enum.forString("kvp"));
        return httpDocument;
    }
    
}
