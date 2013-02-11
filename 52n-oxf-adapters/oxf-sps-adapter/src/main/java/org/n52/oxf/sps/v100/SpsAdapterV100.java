package org.n52.oxf.sps.v100;

import java.io.InputStream;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.n52.ows.request.getcapabilities.GetCapabilitiesParameters;
import org.n52.ows.request.getcapabilities.GetCapabilitiesRequest;
import org.n52.oxf.request.ResponseHandler;
import org.n52.oxf.sps.SpsAdapter;
import org.n52.oxf.util.web.HttpClient;

public class SpsAdapterV100 extends SpsAdapter {
    
    private String serviceUrl;
    
    private HttpClient httpClient;
    
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

    public void send(GetCapabilitiesRequest request) {
        // TODO Auto-generated method stub
        
    }

}
