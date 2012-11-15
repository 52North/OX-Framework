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
package org.n52.oxf.util.web;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.util.web.MultimapRequestParameters;

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
        parameters.addBulkParameterValues("acceptVersion", acceptedVersions);
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
        for (String value : allValues) {
            fail("Assembly has no entries to iterate on: " + value);
        }
    }
    
    @Test
    public void testGetAvailableKeys() {
        parameters.addParameterValue("service", "SOS");
        parameters.addBulkParameterValues("acceptVersion", acceptedVersions);
        Collection<String> availableKeys = parameters.getAvailableKeys();
        assertTrue(availableKeys.contains("service"));
        assertTrue(availableKeys.contains("acceptVersion"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableKeysCannotModified() {
        parameters.addParameterValue("service", "SOS");
        parameters.addBulkParameterValues("acceptVersion", acceptedVersions);
        parameters.getAvailableKeys().add("foobar");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableKeysCannotBeCleared() {
        parameters.addParameterValue("service", "SOS");
        parameters.addBulkParameterValues("acceptVersion", acceptedVersions);
        parameters.getAvailableKeys().clear();
    }

    @Test
    public void testIsServiceASingleValue() {
        parameters.addParameterValue("service", "SOS");
        parameters.hasMultipleValues("service");
        assertTrue(parameters.isSingleValue("service"));
    }
    
    @Test
    public void testHasMultipleAcceptVersionValues() {
        parameters.addBulkParameterValues("acceptVersion", acceptedVersions);
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
        otherParameters.addBulkParameterValues("acceptVersion", acceptedVersions);
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
            assertTrue(parameters.containsValue(value));
            assertTrue(acceptedVersions.contains(value));
        }
    }

    
    private class MultimapRequestParametersSeam extends MultimapRequestParameters {
        public boolean isValid() {
            return true;
        }
    }

}
