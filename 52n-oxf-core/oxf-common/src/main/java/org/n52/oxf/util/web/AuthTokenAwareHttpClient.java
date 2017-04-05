/**
 * ﻿Copyright (C) 2017
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
