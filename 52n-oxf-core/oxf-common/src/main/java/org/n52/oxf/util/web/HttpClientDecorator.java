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
package org.n52.oxf.util.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.request.RequestParameters;

public abstract class HttpClientDecorator implements HttpClient {
    
    private HttpClient httpclient;
    
    public HttpClientDecorator(HttpClient toDecorate) {
        this.httpclient = toDecorate;
    }
    
    public DefaultHttpClient getHttpClientToDecorate() {
        return httpclient.getHttpClientToDecorate();
    }

    public HttpResponse executeGet(String uri) throws HttpClientException {
        return httpclient.executeGet(uri);
    }

    public HttpResponse executeGet(String baseUri, RequestParameters parameters) throws HttpClientException {
        return httpclient.executeGet(baseUri, parameters);
    }

    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }
    
    public HttpResponse executePost(String uri, String payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend, contentType);
    }

    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpResponse executeMethod(HttpRequestBase method) throws HttpClientException {
        return httpclient.executeMethod(method);
    }

    public ClientConnectionManager getConnectionManager() {
        return null;
    }
}
