package org.n52.oxf.request;

import java.io.InputStream;

public interface ResponseHandler {

    public void onSuccess(/*int responseCode, */InputStream response);

    public void onFailure();

}
