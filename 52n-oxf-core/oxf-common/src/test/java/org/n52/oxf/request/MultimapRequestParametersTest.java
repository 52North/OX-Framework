/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.request;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MultimapRequestParametersTest {
    
    private RequestParameters parameters;

    private Collection<String> acceptedVersions;

    @Before
    public void setUp() throws Exception {
        parameters = new MultimapRequestParametersSeam();
        acceptedVersions = new ArrayList<String>();
        acceptedVersions.add("1.0.0");
        acceptedVersions.add("2.0.0");
    }
    
    @Test
    public void testAddValue() {
        assertTrue(parameters.addParameterValue("service", "SOS"));
        assertFalse(parameters.addParameterValue("service", "SOS"));
        assertTrue(parameters.contains("service"));
    }
    
    @Test
    public void testAddNullValues() {
        assertTrue(parameters.addParameterValue("service", null));
        assertFalse(parameters.addParameterValue("service", ""));
        assertTrue(parameters.addParameterValue("service", "SOS"));
        assertTrue(parameters.contains("service"));
    }
    
    @Test
    public void testAddMultipleValues() {
        for (String acceptedVersion : acceptedVersions) {
            parameters.addParameterValue("acceptVersion", acceptedVersion);
        }
        assertContainingAcceptedVersions(parameters);
    }
    
    @Test
    public void testAddBulkValues() {
        parameters.addParameterValues("acceptVersion", acceptedVersions);
        assertContainingAcceptedVersions(parameters);
    }
    
    @Test
    public void testGetAllValuesNonNull() {
        Iterable<String> allValues = parameters.getAllValues("acceptVersion");
        assertNotNull(allValues);
    }

    @Test
    public void testGetAllValuesHasNoEntries() {
        Iterable<String> allValues = parameters.getAllValues("acceptVersion");
        assertThat(allValues.iterator().hasNext(), is(false));
    }
    
    @Test
    public void testGetParameterNames() {
        parameters.addParameterValue("service", "SOS");
        parameters.addParameterValues("acceptVersion", acceptedVersions);
        assertTrue(parameters.contains("service"));
        assertTrue(parameters.contains("acceptVersion"));
    }

    @Test
    public void testIsServiceASingleValue() {
        parameters.addParameterValue("service", "SOS");
        parameters.hasMultipleValues("service");
        assertTrue(parameters.isSingleValue("service"));
    }
    
    @Test
    public void testHasMultipleAcceptVersionValues() {
        parameters.addParameterValues("acceptVersion", acceptedVersions);
        assertFalse(parameters.isSingleValue("acceptVersion"));
        assertTrue(parameters.hasMultipleValues("acceptVersion"));
    }
    
    @Test
    public void testGetSingleValue() {
        parameters.addParameterValue("service", "SOS");
        assertEquals("SOS", parameters.getSingleValue("service"));
    }
    
    @Test
    public void testGetInsertedNullValue() {
        parameters.addParameterValue("service", null);
        assertTrue(parameters.getSingleValue("service").isEmpty());
    }

    @Test
    public void testMerging() {
        RequestParameters otherParameters = new MultimapRequestParametersSeam();
        otherParameters.addParameterValues("acceptVersion", acceptedVersions);
        parameters.addParameterValue("service", "SOS");
        assertTrue(parameters.mergeWith(otherParameters));
        assertTrue(parameters.contains("service"));
        assertContainingAcceptedVersions(parameters);
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(parameters.isEmpty());
    }
    

    private void assertContainingAcceptedVersions(RequestParameters parameters) {
        assertTrue(parameters.contains("acceptVersion"));
        Iterable<String> allValues = parameters.getAllValues("acceptVersion");
        for (String value : allValues) {
//            assertTrue(parameters.containsValue(value));
            assertTrue(acceptedVersions.contains(value));
        }
    }

    
    private class MultimapRequestParametersSeam extends MultimapRequestParameters {
        // need to have an instantiatable class
    }

}
