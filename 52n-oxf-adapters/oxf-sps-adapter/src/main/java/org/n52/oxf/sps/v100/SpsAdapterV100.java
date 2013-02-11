package org.n52.oxf.sps.v100;

import org.n52.ows.request.getcapabilities.GetCapabilitiesParameters;
import org.n52.ows.request.getcapabilities.GetCapabilitiesRequest;
import org.n52.oxf.request.Request;
import org.n52.oxf.sps.SpsAdapter;

public class SpsAdapterV100 extends SpsAdapter {
    
    private String serviceUrl;
    
    public SpsAdapterV100(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public boolean isSupporting(String version) {
        return "1.0.0".equals(version);
    }

    public GetCapabilitiesRequest createGetCapabilitiesRequest(GetCapabilitiesParameters parameters) {
        return new GetCapabilitiesRequest(parameters);
    }

    public void send(Request request) {
        request.sendViaGet(serviceUrl, httpClient);
//        httpClient.executeGet(serviceUrl, request.getRequestParameters()); ??? 
    }

}
