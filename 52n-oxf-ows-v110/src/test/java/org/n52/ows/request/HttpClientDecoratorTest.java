package org.n52.ows.request;

import org.apache.http.HttpEntity;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Test;
import org.n52.oxf.util.web.GzipEnabledHttpClient;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.ProxyAwareHttpClient;
import org.n52.oxf.util.web.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientDecoratorTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDecoratorTest.class);
    
    @Test
    public void testGetCapabilities() throws Exception {
        HttpClient proxyAwareClient = new ProxyAwareHttpClient(new SimpleHttpClient());
        HttpClient httpclient = new GzipEnabledHttpClient(proxyAwareClient);

        String baseUri = "http://sensorweb.demo.52north.org/PegelOnlineSOSv2.1/sos";
        GetCapabilitiesParameters parameters = new GetCapabilitiesParameters("SOS", "1.0.0");
        parameters.addAcceptedVersion("2.0.0");
        HttpEntity responseEntity = httpclient.executeGet(baseUri, parameters);
        XmlObject capabilities = XmlObject.Factory.parse(responseEntity.getContent());
        
        LOGGER.debug(capabilities.xmlText(new XmlOptions().setSavePrettyPrint()));
    }
}
