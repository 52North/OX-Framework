/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
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
import org.n52.ows.request.getcapabilities.GetCapabilitiesXmlBeansBuilder;
import org.n52.oxf.request.Request;
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
//        GetCapabilitiesParameters parameters = createCustomizedGetCapabilitiesParameters();
        GetCapabilitiesParameters parameters = new GetCapabilitiesParameters("SPS");
        adapter.send(createRequestWith(parameters));
    }

    private GetCapabilitiesParameters createCustomizedGetCapabilitiesParameters() {
        return new GetCapabilitiesParameters("SPS")
                    .setUpdateSequence("0.1.8")
                    .addAcceptVersions("1.0.0")
                    .addSections(CONTENTS)
                    // ...
                    .addAcceptedFormats("text/xml")
                    .addNonStandardParameter("extension", "foobar");
    }

    private GetCapabilitiesRequest createRequestWith(GetCapabilitiesParameters parameters) {
        GetCapabilitiesRequest request = adapter.createGetCapabilitiesRequest(parameters);
        request.setRequestBuilder(new GetCapabilitiesXmlBeansBuilder());
        request.setResponseHandler(getCustomResponseHandler());
        return request;
    }

    private ResponseHandler getCustomResponseHandler() {
        return new ResponseHandler() {
            @Override
            public void onSuccess(InputStream inputStream, int httpStatusCode) {
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
            public void onFailure(String reason) {
                fail("Sending test request failed: " + reason);
            }
        };
    }
    
    private class TestableSpsAdapterV100 extends SpsAdapterV100 {

        public TestableSpsAdapterV100() {
            super("http://fake.url");
        }

        @Override
        public void send(Request request) {
            ResponseHandler handler = request.getResponseHandler();
            CapabilitiesDocument capabilitiesDoc = CapabilitiesDocument.Factory.newInstance();
            capabilitiesDoc.addNewCapabilities();
            handler.onSuccess(capabilitiesDoc.newInputStream(), 200);
        }
        
    }
}
