package org.n52.oxf.util.web;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.params.CoreConnectionPNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

    private static final int DEFAULT_TIMEOUT = 30000; // 30s
    private DefaultHttpClient httpclient = new DefaultHttpClient();
    private int timeout;

    public SimpleHttpClient() {
        this(DEFAULT_TIMEOUT);
    }
    
    public SimpleHttpClient(int timeout) {
        this.timeout = timeout;
        initProxyAwareClient();
    }

    private void initProxyAwareClient() {
        httpclient.setRoutePlanner(createProxyPlanner());
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
    }

    private ProxySelectorRoutePlanner createProxyPlanner() {
        ClientConnectionManager connectionManager = httpclient.getConnectionManager();
        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        return new ProxySelectorRoutePlanner(schemeRegistry, defaultProxySelector);
    }
    
    public HttpEntity executeGet(String uri) throws HttpClientException {
        LOGGER.debug("executing GET method '{}'", uri);
        return executeMethod(new HttpGet(uri));
    }

    public HttpEntity executeGet(String baseUri, Map<String, String> queryParameters) throws HttpClientException {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUri);
            for (String key : queryParameters.keySet()) {
                uriBuilder.addParameter(key, queryParameters.get(key));
            }
            URI uri = uriBuilder.build();
            LOGGER.debug("executing GET method '{}'", uri);
            return executeMethod(new HttpGet(uri));
        } catch (URISyntaxException e) {
            throw new HttpClientException("Invalid base URI: " + baseUri, e);
        }
    }

    public HttpEntity executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        LOGGER.debug("executing POST method '{}' \n", uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(payloadToSend);
        return executeMethod(post);
    }
    
    public HttpEntity executeMethod(HttpRequestBase method) throws HttpClientException {
        try {
            return httpclient.execute(method).getEntity();
        } catch (IOException e) {
            throw new HttpClientException("Could not execute GET method.", e);
        }
    }
    
    public ClientConnectionManager getClientConnectionManager() {
        return httpclient.getConnectionManager();
    }
}

