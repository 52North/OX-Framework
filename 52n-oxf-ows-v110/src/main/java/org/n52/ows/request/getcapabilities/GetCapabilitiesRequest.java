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
