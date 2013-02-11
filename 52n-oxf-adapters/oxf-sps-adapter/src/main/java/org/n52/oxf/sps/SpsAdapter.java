package org.n52.oxf.sps;

import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.ProxyAwareHttpClient;
import org.n52.oxf.util.web.SimpleHttpClient;

public abstract class SpsAdapter {
    
    protected HttpClient httpClient;
    
    protected SpsAdapter() {
        this(new ProxyAwareHttpClient(new SimpleHttpClient()));
    }
    
    protected SpsAdapter(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    public abstract boolean isSupporting(String wishedVersion);

}
