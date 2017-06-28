/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.n52.oxf.sps.SpsAdapterFactory.createAdapter;

import org.junit.Test;

public class SpsAdapterFactoryTest {

    @Test(expected = IllegalArgumentException.class) public void 
    shouldThrowExceptionWhenPassedServiceUrlIsNull() 
    throws Exception {
        createAdapter(null, null);
    }
    
    @Test(expected = IllegalArgumentException.class) public void 
    shouldThrowExceptionWhenPassedVersionIsNull() 
    throws Exception {
        createAdapter("http://fake.url", null);
    }
    
    @Test(expected = MissingAdapterImplementationException.class) public void 
    shouldThrowExceptionWhenPassedVersionMatchesNoAdapterImplementation() 
    throws Exception {
        createAdapter("http://fake.url", "42");
    }

    @Test public void 
    shouldCreateSpsAdapterWithVersion100() 
    {
        final String version = "1.0.0";
        try {
            final SpsAdapter adapter = createAdapter("http://fake.url", version);
            assertThat(adapter.isSupporting(version), is(true));
        } catch (final MissingAdapterImplementationException e) {
            fail("SpsAdapter with " + version + " could not be created");
        }
    }
    
}


