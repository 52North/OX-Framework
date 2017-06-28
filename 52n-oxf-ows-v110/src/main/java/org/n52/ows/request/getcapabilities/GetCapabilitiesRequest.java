/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.ows.request.getcapabilities;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.n52.oxf.request.Request;
import org.n52.oxf.request.RequestParameters;
import org.n52.oxf.request.XmlObjectResponseHandler;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.HttpClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>This class is test only yet!</b>
 */
public class GetCapabilitiesRequest extends Request {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesRequest.class);

    private RequestParameters parameters;

    public GetCapabilitiesRequest(GetCapabilitiesParameters parameters) {
        super(new GetCapabilitiesXmlBeansBuilder(), new XmlObjectResponseHandler());
        this.parameters = parameters.getStandardParameters();
    }
    
    @Override
    public void sendViaGet(String serviceUrl, HttpClient httpClient) {
        try {
            HttpResponse response = httpClient.executeGet(serviceUrl, parameters);
            int statusCode = response.getStatusLine().getStatusCode();
            InputStream responseStream = getResponseStreamFrom(response);
            responseHandler.onSuccess(responseStream, statusCode);
        } catch (HttpClientException e) {
            LOGGER.error("Sending GetCapabilities failed.", e);
            responseHandler.onFailure(e.getMessage());
        }
        catch (IOException e) {
            LOGGER.error("Could not create response stream.", e);
            responseHandler.onFailure(e.getMessage());
        }
    }
    
    @Override
    public void sendViaPost(String serviceUrl, HttpClient httpClient) {
        // TODO implement
    }

    private InputStream getResponseStreamFrom(HttpResponse response) throws IOException {
        return response.getEntity() != null ? response.getEntity().getContent() : null;
    }
    
}
