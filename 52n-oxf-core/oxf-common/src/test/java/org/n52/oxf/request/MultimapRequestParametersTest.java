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
package org.n52.oxf.request;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
        for (final String acceptedVersion : acceptedVersions) {
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
        final Iterable<String> allValues = parameters.getAllValues("acceptVersion");
        assertNotNull(allValues);
    }

    @Test
    public void testGetAllValuesHasNoEntries() {
        final Iterable<String> allValues = parameters.getAllValues("acceptVersion");
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
        final RequestParameters otherParameters = new MultimapRequestParametersSeam();
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
    
    @Test
    public void testIsParameterEmpty() {
    	final String parameter = "parameter";
		assertTrue(parameters.isEmpty(parameter));
    	parameters.addParameterStringValues(parameter, (String[])null);
    	assertTrue(parameters.isEmpty(parameter));
    	parameters.addParameterValue(parameter, "value");
    	assertFalse(parameters.isEmpty(parameter));
    	parameters.addParameterValue(parameter, "value2");
    	assertFalse(parameters.isEmpty(parameter));
    }
    

    private void assertContainingAcceptedVersions(final RequestParameters parameters) {
        assertTrue(parameters.contains("acceptVersion"));
        final Iterable<String> allValues = parameters.getAllValues("acceptVersion");
        for (final String value : allValues) {
            assertTrue(acceptedVersions.contains(value));
        }
    }

    
    private class MultimapRequestParametersSeam extends MultimapRequestParameters {
        // need to have an instantiatable class
    }

}
