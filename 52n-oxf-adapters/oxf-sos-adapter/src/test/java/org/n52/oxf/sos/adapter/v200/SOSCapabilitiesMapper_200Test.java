/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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

import java.io.InputStream;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.sos.capabilities.SOSContents;

import net.opengis.sos.x20.CapabilitiesDocument;

public class SOSCapabilitiesMapper_200Test {

    private static final String SOS_CAPABILITIES_200 = "/sos2_capabilities.xml";
    private static final String SOS_CAPABILITIES_WITHOUT_CONTENTS_SECTION = "/sos2_capabilities_without_contents_section.xml";
    private CapabilitiesDocument capabilitiesDocument;
    private SOSCapabilitiesMapper_200 mapper;
    private CapabilitiesDocument capabilitiesDocWithoutContentsSection;

    @Before
    public void setUp() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream(SOS_CAPABILITIES_200);
        capabilitiesDocument = CapabilitiesDocument.Factory.parse(inputStream);
        mapper = new SOSCapabilitiesMapper_200();
        inputStream = getClass().getResourceAsStream(SOS_CAPABILITIES_WITHOUT_CONTENTS_SECTION);
        capabilitiesDocWithoutContentsSection = CapabilitiesDocument.Factory.parse(inputStream);
    }
    
    @Test
    public void shouldMapServiceVersion() throws OXFException {
        ServiceDescriptor serviceDescriptor = mapper.mapCapabilities(capabilitiesDocument);

        Assert.assertThat(serviceDescriptor.getVersion(), Is.is("2.0.0"));
    }

    @Test
    public void shouldIgnoreMissingContentsSection() throws OXFException {
        ServiceDescriptor serviceDescriptor = mapper.mapCapabilities(capabilitiesDocWithoutContentsSection);
        
        Assert.assertThat(serviceDescriptor.getContents(), IsInstanceOf.instanceOf(SOSContents.class));
        Assert.assertThat(serviceDescriptor.getContents().getDataIdentificationCount(), Is.is(0));
        Assert.assertThat(serviceDescriptor.getOperationsMetadata(), IsNull.nullValue());
    }

}
