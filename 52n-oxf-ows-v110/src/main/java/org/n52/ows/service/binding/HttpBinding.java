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
