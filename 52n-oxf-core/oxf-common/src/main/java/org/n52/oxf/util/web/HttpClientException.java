package org.n52.oxf.util.web;

public class HttpClientException extends Exception {

    private static final long serialVersionUID = -3990386897357577555L;

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(String message) {
        super(message);
    }
    
}
