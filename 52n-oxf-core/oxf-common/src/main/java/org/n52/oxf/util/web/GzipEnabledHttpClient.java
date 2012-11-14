package org.n52.oxf.util.web;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GzipEnabledHttpClient extends HttpClientDecorator {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GzipEnabledHttpClient.class);
    
    private static final String GZIP_ENCODING_VALUE = "gzip";
    
    private static final String ENCODING_HEADER_NAME = "Accept-Encoding";
    
    public GzipEnabledHttpClient(HttpClient httpclient) {
        super(httpclient);
        addGzipInterceptors(getHttpClientToDecorate());
    }
    
    private void addGzipInterceptors(DefaultHttpClient httpclient) {
        httpclient.addRequestInterceptor(new GzipRequestInterceptor());
        httpclient.addResponseInterceptor(new GzipResponseInterceptor());
    }

    private final class GzipResponseInterceptor implements HttpResponseInterceptor {
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header header = entity.getContentEncoding();
                if (header != null) {
                    HeaderElement[] codecs = header.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase(GZIP_ENCODING_VALUE)) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }                
        }
    }

    private final class GzipRequestInterceptor implements HttpRequestInterceptor {
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            if (!request.containsHeader(ENCODING_HEADER_NAME)) {
                LOGGER.trace("add gzip header.");
                request.addHeader(ENCODING_HEADER_NAME, GZIP_ENCODING_VALUE);
            }                
        }
    }

}
