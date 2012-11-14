package org.n52.oxf.util.web;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HttpClientDecoratorTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testDecorateProcyAwareness() throws Exception {
//        System.setProperty("http.proxyHost", "my.proxy");
//        System.setProperty("http.proxyPort", "8080");
        HttpClient proxyAwareClient = new ProxyAwareHttpClient(new SimpleHttpClient());
        HttpClient httpclient = new GzipEnabledHttpClient(proxyAwareClient);
        httpclient.executeGet("http://sensorweb.demo.52north.org/PegelOnlineSOSv2.1/sos");
    }

}
