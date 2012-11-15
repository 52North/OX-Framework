package org.n52.oxf.util.web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

    public HttpResponse executeGet(String uri) throws HttpClientException {
        return httpclient.executeGet(uri);
    }

    public HttpResponse executeGet(String baseUri, RequestParameters parameters) throws HttpClientException {
        return httpclient.executeGet(baseUri, parameters);
    }

    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend, contentType);
    }

    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        return httpclient.executePost(uri, payloadToSend);
    }

    public HttpResponse executeMethod(HttpRequestBase method) throws HttpClientException {
        return httpclient.executeMethod(method);
    }

    
}
