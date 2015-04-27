/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.adapter.v200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;

import net.opengis.sos.x20.CapabilitiesDocument;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.sos.adapter.v200.SOSCapabilitiesMapper_200;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSCapabilitiesMapper_200Test {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSCapabilitiesMapper_200Test.class);
    
    private static final String SOS_CAPABILITIES_200 = "/sos2_capabilities.xml";
    private CapabilitiesDocument capabilitiesDocument;
    private SOSCapabilitiesMapper_200 mapper;

    @Before
    public void setUp() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream(SOS_CAPABILITIES_200);
        capabilitiesDocument = CapabilitiesDocument.Factory.parse(inputStream);
        mapper = new SOSCapabilitiesMapper_200();
    }
    
    @Test
    public void testMapContents() {
        try {
            ServiceDescriptor serviceDescriptor = mapper.mapCapabilities(capabilitiesDocument);
            assertCorrectVersion(serviceDescriptor.getVersion());
            assertCorrectContent(serviceDescriptor.getContents());
            assertCorrectMetadata(serviceDescriptor.getOperationsMetadata());
            assertCorrectServiceIdentification(serviceDescriptor.getServiceIdentification());
            assertCorrectUpddateSequence(serviceDescriptor.getUpdateSequence());
        } catch (OXFException e) {
            LOGGER.error("Could not map capabilities!", e);
            fail("Could not map capabilties!");
        }
    }

    private void assertCorrectVersion(String version) {
        assertEquals("Wrong version detected.", "2.0.0", version);
    }

    private void assertCorrectContent(Contents contents) {
        // TODO check content according to example SOS_CAPABILTIES_200 file
    }

    private void assertCorrectMetadata(OperationsMetadata operationsMetadata) {
     // TODO check metadata according to example SOS_CAPABILTIES_200 file
    }
    
    private void assertCorrectServiceIdentification(ServiceIdentification serviceIdentification) {
     // TODO check serviceIdentification according to example SOS_CAPABILTIES_200 file
    }

    private void assertCorrectUpddateSequence(String updateSequence) {
     // TODO check updateSequence according to example SOS_CAPABILTIES_200 file
    }

}
