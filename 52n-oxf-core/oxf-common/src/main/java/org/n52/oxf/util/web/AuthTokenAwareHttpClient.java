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

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * This {@link HttpClientDecorator} adds an authtoken to each request in the {@link HttpHeader}.AUTHORIZATION.
 * 
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class AuthTokenAwareHttpClient extends HttpClientDecorator {

    public AuthTokenAwareHttpClient(HttpClient toDecorate, String authtoken) {
        super(toDecorate);
        if (authtoken == null || authtoken.isEmpty()) {
            throw new IllegalArgumentException("authtoken might not be null or empty!");
        }
        decorateAuthtokenAwareness(authtoken);
    }

    private void decorateAuthtokenAwareness(String authtoken) {
        DefaultHttpClient clientToDecorate = getHttpClientToDecorate();
        clientToDecorate.addRequestInterceptor(new AuthTokenInterceptor(authtoken));
    }

    private final class AuthTokenInterceptor implements HttpRequestInterceptor {

        private String authtoken;

        public AuthTokenInterceptor(String authtoken) {
            this.authtoken = authtoken;
        }

        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            request.addHeader(HttpHeaders.AUTHORIZATION, authtoken);
        }

    }

}
