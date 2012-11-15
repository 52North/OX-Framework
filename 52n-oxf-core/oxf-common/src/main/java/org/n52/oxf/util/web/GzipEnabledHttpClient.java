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

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GzipEnabledHttpClient extends HttpClientDecorator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GzipEnabledHttpClient.class);
    
    private static final String GZIP_ENCODING_VALUE = "gzip";
    
    private static final String ENCODING_HEADER_NAME = "Accept-Encoding";
    
    public GzipEnabledHttpClient(HttpClient httpclient) {
        super(httpclient);
        addGzipInterceptors(getHttpClientToDecorate());
    }
    
    private void addGzipInterceptors(DefaultHttpClient httpclient) {
        httpclient.addRequestInterceptor(new GzipRequestInterceptor());
        httpclient.addResponseInterceptor(new GzipResponseInterceptor());
    }

    private final class GzipResponseInterceptor implements HttpResponseInterceptor {
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header header = entity.getContentEncoding();
                if (header != null) {
                    HeaderElement[] codecs = header.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase(GZIP_ENCODING_VALUE)) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }                
        }
    }

    private final class GzipRequestInterceptor implements HttpRequestInterceptor {
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            if (!request.containsHeader(ENCODING_HEADER_NAME)) {
                LOGGER.trace("add gzip header.");
                request.addHeader(ENCODING_HEADER_NAME, GZIP_ENCODING_VALUE);
            }                
        }
    }

}
