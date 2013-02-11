package org.n52.oxf.sps.v100;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesSection.CONTENTS;

import java.io.IOException;
import java.io.InputStream;

import net.opengis.sps.x10.CapabilitiesDocument;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.n52.ows.request.getcapabilities.GetCapabilitiesParameters;
import org.n52.ows.request.getcapabilities.GetCapabilitiesRequest;
import org.n52.oxf.request.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpsAdapterV100Test {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SpsAdapterV100Test.class);

    private SpsAdapterV100 adapter;
    
    @Before public void 
    setUp()
    {
        adapter = new TestableSpsAdapterV100();
    }

    @Test
    public void 
    shouldReturnGetCapabilitiesRequestParametersBuilder()
    {
//        GetCapabilitiesParameters parameters = createParameters();
        GetCapabilitiesParameters parameters = new GetCapabilitiesParameters("SPS");
        adapter.send(createRequest(parameters));
    }

    private GetCapabilitiesParameters createParameters() {
        return new GetCapabilitiesParameters("SPS")
                    .setUpdateSequence("0.1.8")
                    .addAcceptVersions("1.0.0")
                    .addSections(CONTENTS)
                    // ...
                    .addAcceptedFormats("text/xml")
                    .addNonStandardParameter("extension", "foobar");
    }

    private GetCapabilitiesRequest createRequest(GetCapabilitiesParameters parameters) {
        return adapter.createGetCapabilitiesRequest(parameters)
                    .setResponseHandler(getCustomResponseHandler());
    }

    private ResponseHandler getCustomResponseHandler() {
        return new ResponseHandler() {
            @Override
            public void onSuccess(InputStream inputStream) {
                try {
                    XmlObject response = XmlObject.Factory.parse(inputStream);
                    assertThat(response.schemaType(), is(CapabilitiesDocument.type));
                } 
                catch (IOException e) {
                    fail("Could not read fake Capabilities response:" + e.getMessage());
                }
                catch (XmlException e) {
                    fail("Could not parse fake Capabilities response: " + e.getMessage());
                }
            }
            
            @Override
            public void onFailure() {
                fail("Sending test request failed for some reason.");
            }
        };
    }
    
    private class TestableSpsAdapterV100 extends SpsAdapterV100 {

        public TestableSpsAdapterV100() {
            super("http://fake.url");
        }

        @Override
        public void send(GetCapabilitiesRequest request) {
            ResponseHandler handler = request.getResponseHandler();
            CapabilitiesDocument capabilitiesDoc = CapabilitiesDocument.Factory.newInstance();
            capabilitiesDoc.addNewCapabilities();
            handler.onSuccess(capabilitiesDoc.newInputStream());
        }
        
    }
}
