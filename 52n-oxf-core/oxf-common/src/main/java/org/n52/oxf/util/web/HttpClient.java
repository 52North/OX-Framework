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
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.request.RequestParameters;

public interface HttpClient {

    DefaultHttpClient getHttpClientToDecorate();

    public HttpResponse executeGet(String uri) throws HttpClientException;

    /**
     * @param baseUri
     *        the target to send the GET request to.
     * @param parameters
     *        the request/query parameters.
     * @return the http response returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executeGet(String baseUri, RequestParameters parameters) throws HttpClientException;

    /**
     * Sends the given payload as content-type text/xml with UTF-8 encoding to the determined URI.
     * <strong>Callees are responsible for ensuring that the contents are actually
     * encoded as UTF-8</strong>. If not UTF-8, use {@link #executePost(String, String, ContentType)}
     * instead.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML encoded as UTF-8.
     * @return the http response returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException;

    /**
     * Sends the given payload as content-type text/xml with UTF-8 encoding to the determined URI.
     * <strong>Callees are responsible for ensuring that the contents are actually
     * encoded as UTF-8</strong>. If not UTF-8, use {@link #executePost(String, String, ContentType)}
     * instead.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML encoded as UTF-8.
     * @return the http response returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executePost(String uri, String payloadToSend) throws HttpClientException;
    
    /**
     * Sends the given payload (marked to be of a specific content-type) to the determined URI.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML.
     * @param contentType
     *        the content-type of the payload.
     * @return the http responsey returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException;

    /**
     * Sends the given payload to the determined URI. Refer to the <a
     * href="http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/index.html">HTTP components docs</a>
     * to get more information which entity types are possible.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        a more generic way to send arbitrary content.
     * @return the http response returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws HttpClientException;

    /**
     * @param method
     * @return the http response returned by the target.
     * @throws HttpClientException
     */
    public HttpResponse executeMethod(HttpRequestBase method) throws HttpClientException;

}