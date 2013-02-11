
package org.n52.oxf.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public interface ResponseHandler {

    /**
     * Called when a response was received successfully. Content and HTTP status code is passed for further
     * processing.
     * 
     * @param response
     *        response's content as stream.
     * @param httpResponseCode
     *        the HTTP status code.
     */
    public void onSuccess(InputStream response, int httpStatusCode);

    /**
     * Called when no response could be received. When this method is called, any low-level exceptions (e.g.
     * {@link IOException} {@link TimeoutException}, etc.) should already have been handled and logged
     * appropriatly. Use this method to trigger higher-level error messaging.
     */
    public void onFailure(String reason);

}
