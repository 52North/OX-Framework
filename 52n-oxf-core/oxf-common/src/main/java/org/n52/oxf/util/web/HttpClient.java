
package org.n52.oxf.util.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;

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
     * Sends the given payload as content-type text/xml to the determined URI.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML.
     * @return the http response returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException;

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