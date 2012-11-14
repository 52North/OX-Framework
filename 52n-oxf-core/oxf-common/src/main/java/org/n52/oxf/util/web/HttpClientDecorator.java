package org.n52.oxf.util.web;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;
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

    public HttpEntity executeGet(String uri) throws HttpClientException {
        return httpclient.executeGet(uri);
    }

    public HttpEntity executeGet(String baseUri, Map<String, String> queryParameters) throws HttpClientException {
        return httpclient.executeGet(baseUri, queryParameters);
    }

    public HttpEntity executePost(String uri, XmlObject payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpEntity executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend, contentType);
    }

    public HttpEntity executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpEntity executeMethod(HttpRequestBase method) throws HttpClientException {
        return httpclient.executeMethod(method);
    }

    
}
