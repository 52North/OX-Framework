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
package org.n52.oxf.wcs.adapter;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.wcs.capabilities.CoverageDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates how to use the WCSAdapter. You might use it as an example for your own code.
 *
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class TestWCSAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestWCSAdapter.class);

    private final String url = "http://ogcdemo.pcigeomatics.com:8181/swe/wcs";

    public static void main(String[] args) throws OXFException, ExceptionReport {
        new TestWCSAdapter().testServiceInitialization();
    }

    public void testServiceInitialization() throws ExceptionReport, OXFException {

        WCSAdapter adapter = new WCSAdapter();

        ServiceDescriptor desc = adapter.initService(url);

        CoverageDataset coverage = (CoverageDataset) desc.getContents().getDataIdentification(0);

        LOGGER.info("Coverage Title: {}", coverage.getTitle());
    }

}
