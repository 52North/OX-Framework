
package org.n52.oxf.util.web;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;

public interface HttpClient {

    DefaultHttpClient getHttpClientToDecorate();

    public HttpEntity executeGet(String uri) throws HttpClientException;

    /**
     * @param baseUri
     *        the target to send the GET request to.
     * @param queryParameters 
     * @return
     * @throws HttpClientException
     */
    public HttpEntity executeGet(String baseUri, Map<String, String> queryParameters) throws HttpClientException;

    /**
     * Sends the given payload as content-type text/xml to the determined URI.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML.
     * @return the response entity returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpEntity executePost(String uri, XmlObject payloadToSend) throws HttpClientException;

    /**
     * Sends the given payload (marked to be of a specific content-type) to the determined URI.
     * 
     * @param uri
     *        the target to send the POST request to.
     * @param payloadToSend
     *        the POST payload as XML.
     * @param contentType
     *        the content-type of the payload.
     * @return the response entity returned by the target.
     * @throws HttpClientException
     *         if sending the request fails.
     */
    public HttpEntity executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException;

    public HttpEntity executePost(String uri, HttpEntity payloadToSend) throws HttpClientException;

    public HttpEntity executeMethod(HttpRequestBase method) throws HttpClientException;

}