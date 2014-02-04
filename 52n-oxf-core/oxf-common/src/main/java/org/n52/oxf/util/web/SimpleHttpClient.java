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
package org.n52.oxf.util.web;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.request.RequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000; // 5s

    private static final int DEFAULT_SOCKET_TIMEOUT = 5000; // 5s

    private DefaultHttpClient httpclient;

    /**
     * Creates an instance with <code>timeout = {@value #DEFAULT_CONNECTION_TIMEOUT}</code> ms.
     */
    public SimpleHttpClient() {
        this(DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates an instance with a given connection timeout.
     * 
     * @param connectionTimeout
     *        the connection timeout in milliseconds.
     */
    public SimpleHttpClient(int connectionTimeout) {
        this(connectionTimeout, DEFAULT_SOCKET_TIMEOUT);
    }

    /**
     * Creates an instance with the given timeouts.
     * 
     * @param connectionTimeout
     *        the connection timeout in milliseconds.
     * @param socketTimeout
     *        the socket timout in milliseconds.
     */
    public SimpleHttpClient(int connectionTimeout, int socketTimeout) {
        ClientConnectionManager cm = getConnectionManager();
        this.httpclient = (cm == null) ? new DefaultHttpClient() : new DefaultHttpClient(cm);
        this.httpclient.getParams().setParameter(CONNECTION_TIMEOUT, connectionTimeout);
        this.httpclient.getParams().setParameter(SO_TIMEOUT, socketTimeout);
    }

    /**
     * @return null by default
     */
    public ClientConnectionManager getConnectionManager() {
        return null;
    }

    public DefaultHttpClient getHttpClientToDecorate() {
        return httpclient;
    }

    public HttpResponse executeGet(String uri) throws HttpClientException {
        LOGGER.debug("executing GET method '{}'", uri);
        return executeMethod(new HttpGet(uri));
    }

    public HttpResponse executeGet(String baseUri, RequestParameters parameters) throws HttpClientException {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUri);
            for (String key : parameters.getParameterNames()) {
                if (parameters.isSingleValue(key)) {
                    uriBuilder.addParameter(key, parameters.getSingleValue(key));
                }
                else {
                    Iterable<String> multipleValues = parameters.getAllValues(key);
                    uriBuilder.addParameter(key, createCsvValue(multipleValues));
                }
            }
            URI uri = uriBuilder.build();
            LOGGER.debug("executing GET method '{}'", uri);
            return executeMethod(new HttpGet(uri));
        }
        catch (URISyntaxException e) {
            throw new HttpClientException("Invalid base URI: " + baseUri, e);
        }
    }

    private String createCsvValue(Iterable<String> multipleValues) {
        StringBuilder csv = new StringBuilder();
        for (String value : multipleValues) {
            if ( !value.isEmpty()) {
                csv.append(value).append(",");
            }
        }
        StringBuilder csvWitoutTrailingComma = csv.deleteCharAt(csv.length() - 1);
        return csv.length() > 0 ? csvWitoutTrailingComma.toString() : csv.toString();
    }

    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException {
        return executePost(uri, payloadToSend.xmlText(), ContentType.create("text/xml", Consts.UTF_8));
    }
    
    public HttpResponse executePost(String uri, String payloadToSend) throws HttpClientException {
        return executePost(uri, payloadToSend, ContentType.create("text/xml", Consts.UTF_8));
    }

    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException {
        StringEntity requestEntity = new StringEntity(payloadToSend, contentType);
        return executePost(uri, requestEntity);
    }

    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        LOGGER.debug("executing POST method to '{}'.", uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(payloadToSend);
        return executeMethod(post);
    }

    public HttpResponse executeMethod(HttpRequestBase method) throws HttpClientException {
        try {
            return httpclient.execute(method);
        }
        catch (IOException e) {
            throw new HttpClientException("Could not execute request.", e);
        }
    }

    public void setConnectionTimout(int timeout) {
        httpclient.getParams().setParameter(CONNECTION_TIMEOUT, timeout);
    }

    public void setSocketTimout(int timeout) {
        httpclient.getParams().setParameter(SO_TIMEOUT, timeout);
    }

}
