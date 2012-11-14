
package org.n52.oxf.util.web;

import java.net.ProxySelector;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;

public class ProxyAwareHttpClient extends HttpClientDecorator {

    /**
     * Decorates the given {@link HttpClient} to be proxy aware. This decorated instance will use the standard
     * JRE proxy selector to obtain proxy information.
     * 
     * @param httpclient
     *        the {@link HttpClient} to decorate with proxy awareness.
     * @see ProxySelectorRoutePlanner
     */
    public ProxyAwareHttpClient(HttpClient httpclient) {
        super(httpclient);
        decorateProxyAwareness();
    }

    private void decorateProxyAwareness() {
        DefaultHttpClient clientToDecorate = getHttpClientToDecorate();
        clientToDecorate.setRoutePlanner(createProxyPlanner());
    }

    private ProxySelectorRoutePlanner createProxyPlanner() {
        DefaultHttpClient decoratedHttpClient = getHttpClientToDecorate();
        ClientConnectionManager connectionManager = decoratedHttpClient.getConnectionManager();
        SchemeRegistry schemeRegistry = connectionManager.getSchemeRegistry();
        ProxySelector defaultProxySelector = ProxySelector.getDefault();
        return new ProxySelectorRoutePlanner(schemeRegistry, defaultProxySelector);
    }

}
