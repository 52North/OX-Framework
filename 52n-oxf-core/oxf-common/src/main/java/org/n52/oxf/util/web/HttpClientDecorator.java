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
package org.n52.oxf.util.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;

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
