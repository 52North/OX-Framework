
package org.n52.ows.request.getcapabilities;

import org.n52.oxf.request.RequestParameters;
import org.n52.oxf.request.ResponseHandler;
import org.n52.oxf.request.XmlObjectResponseHandler;

public class GetCapabilitiesRequest {

    // TODO default vs. inject
    private ResponseHandler responseHandler = new XmlObjectResponseHandler();

    private RequestParameters parameters;

    public GetCapabilitiesRequest(GetCapabilitiesParameters parameters) {
        this.parameters = parameters.getStandardParameters();
    }
    
    public GetCapabilitiesRequest setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

}
