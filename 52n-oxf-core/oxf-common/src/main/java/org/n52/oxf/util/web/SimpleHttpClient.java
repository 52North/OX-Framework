
package org.n52.oxf.util.web;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleHttpClient implements HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000; // 5s

    private static final int DEFAULT_SOCKET_TIMEOUT = 5000; // 5s

    private DefaultHttpClient httpclient = new DefaultHttpClient();

    /**
     * Creates an instance with <code>timeout = {@value #DEFAULT_CONNECTION_TIMEOUT}</code> ms.
     */
    public SimpleHttpClient() {
        this(DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * Creates an instance with a given connection timeout.
     * 
     * @param connectionTimeout
     *        the connection timeout.
     */
    public SimpleHttpClient(int connectionTimeout) {
        httpclient.getParams().setParameter(CONNECTION_TIMEOUT, connectionTimeout);
        httpclient.getParams().setParameter(SO_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
    }
    
    public DefaultHttpClient getHttpClientToDecorate() {
        return httpclient;
    }

    public HttpResponse executeGet(String uri) throws HttpClientException {
        LOGGER.debug("executing GET method '{}'", uri);
        return executeMethod(new HttpGet(uri));
    }

    public HttpResponse executeGet(String baseUri, RequestParameters parameters) throws HttpClientException {
        try {
            URIBuilder uriBuilder = new URIBuilder(baseUri);
            for (String key : parameters.getAvailableKeys()) {
                if (parameters.isSingleValue(key)) {
                    uriBuilder.addParameter(key, parameters.getSingleValue(key));
                } else {
                    Iterable<String> multipleValues = parameters.getAllValues(key);
                    uriBuilder.addParameter(key, createCsvValue(multipleValues));
                }
            }
            URI uri = uriBuilder.build();
            LOGGER.debug("executing GET method '{}'", uri);
            return executeMethod(new HttpGet(uri));
        }
        catch (URISyntaxException e) {
            throw new HttpClientException("Invalid base URI: " + baseUri, e);
        }
    }

    private String createCsvValue(Iterable<String> multipleValues) {
        StringBuilder csv = new StringBuilder();
        for (String value : multipleValues) {
            if (!value.isEmpty()) {
                csv.append(value).append(",");
            }
        }
        StringBuilder csvWitoutTrailingComma = csv.deleteCharAt(csv.length() - 1);
        return csv.length() > 0 ? csvWitoutTrailingComma.toString() : csv.toString();
    }

    public HttpResponse executePost(String uri, XmlObject payloadToSend) throws HttpClientException {
        return executePost(uri, payloadToSend.xmlText(), ContentType.TEXT_XML);
    }

    public HttpResponse executePost(String uri, String payloadToSend, ContentType contentType) throws HttpClientException {
        StringEntity requestEntity = new StringEntity(payloadToSend, contentType);
        LOGGER.debug("executing POST method to '{}':\n{}", uri, payloadToSend);
        HttpPost post = new HttpPost(uri);
        post.setEntity(requestEntity);
        return executeMethod(post);
    }

    public HttpResponse executePost(String uri, HttpEntity payloadToSend) throws HttpClientException {
        LOGGER.debug("executing POST method to '{}'.", uri);
        HttpPost post = new HttpPost(uri);
        post.setEntity(payloadToSend);
        return executeMethod(post);
    }

    public HttpResponse executeMethod(HttpRequestBase method) throws HttpClientException {
        try {
            return httpclient.execute(method);
        }
        catch (IOException e) {
            throw new HttpClientException("Could not execute GET method.", e);
        }
    }

    public void setConnectionTimout(int timeout) {
        httpclient.getParams().setParameter(CONNECTION_TIMEOUT, timeout);
    }
    
    public void setSocketTimout(int timeout) {
        httpclient.getParams().setParameter(SO_TIMEOUT, timeout);
    }

}
