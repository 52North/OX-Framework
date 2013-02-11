
package org.n52.oxf.request;

import org.n52.oxf.util.web.HttpClient;

/**
 * <b>This class is test only yet!</b>
 */
public abstract class Request {
    
    protected RequestBuilder requestBuilder;
    
    protected ResponseHandler responseHandler;
    
    public Request(RequestBuilder defaultRequestBuilder, ResponseHandler defaultResponseHandler) {
        this.requestBuilder = defaultRequestBuilder;
        this.responseHandler = defaultResponseHandler;
    }
    
    public abstract void sendViaGet(String serviceUrl, HttpClient httpClient);

    public abstract void sendViaPost(String serviceUrl, HttpClient httpClient);

    public void setRequestBuilder(RequestBuilder requestBuilder) {
        this.requestBuilder = requestBuilder;
    }
    
    public RequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    public void setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

}