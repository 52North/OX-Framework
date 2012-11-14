package org.n52.oxf.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.n52.ows.request.MultimapRequestParameters;
import org.n52.ows.request.RequestParameters;

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
