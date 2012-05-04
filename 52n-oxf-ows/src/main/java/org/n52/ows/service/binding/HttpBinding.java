/**
 * Copyright (C) 2012
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

public class HttpBinding {
    
    private String externalDcpUrl; // external binding url
    
    // XXX GET KVP Binding mandatory?
    private String externalGetCapabilitiesGETDcpUrl = externalDcpUrl; // default
    
    private String bindingType; // xml, soap, kvp
    
    private HttpMethod httpMethod;
    
    private String conformanceClass;
    
    private HttpBinding() {
        // needed for injection
    }

    public HttpBinding(String externalDcpUrl, String bindingType, String httpMethod) {
        this();
        this.externalDcpUrl = externalDcpUrl;
        this.bindingType = bindingType;
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

    public String getBindingType() {
        return bindingType;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
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
    
    public HTTPDocument getHttpInfo() {
        HTTPDocument httpDocument = HTTPDocument.Factory.newInstance();
        HTTP http = httpDocument.addNewHTTP();
        RequestMethodType method = (httpMethod == GET) ? http.addNewGet() : http.addNewPost();
        method.setHref(externalDcpUrl);
        method.setType(bindingType);
        return httpDocument;
    }
    
    /**
     * @return the GetCapabilites GET binding (mandatory for all bindings)
     */
    public HTTPDocument getGetCapabilitiesHttpInfoGET() {
        HTTPDocument httpDocument = HTTPDocument.Factory.newInstance();
        RequestMethodType method = httpDocument.addNewHTTP().addNewGet();
        method.setHref(externalGetCapabilitiesGETDcpUrl);
        method.setType("kvp");
        return httpDocument;
    }
    
}
