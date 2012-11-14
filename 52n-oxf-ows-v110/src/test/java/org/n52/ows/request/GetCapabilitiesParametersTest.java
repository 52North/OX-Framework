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
package org.n52.ows.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.n52.ows.request.GetCapabilitiesParameters.ACCEPT_FORMATS_PARAMETER;
import static org.n52.ows.request.GetCapabilitiesParameters.ACCEPT_VERSIONS_PARAMETER;
import static org.n52.ows.request.GetCapabilitiesParameters.SECTION_PARAMETER;
import static org.n52.ows.request.GetCapabilitiesParameters.SERVICE_PARAMETER;
import static org.n52.ows.request.GetCapabilitiesParameters.UPDATE_SEQUENCE;

import org.junit.Test;

public class GetCapabilitiesParametersTest {

    @Test
    public void testCreateWithService() {
        RequestParameters parameters = new GetCapabilitiesParameters("SOS");
        assertTrue(parameters.contains(SERVICE_PARAMETER));
        assertFalse(parameters.contains(SECTION_PARAMETER));
        assertFalse(parameters.contains(ACCEPT_VERSIONS_PARAMETER));
        assertFalse(parameters.contains(ACCEPT_FORMATS_PARAMETER));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCreation() {
        new GetCapabilitiesParameters("");
        new GetCapabilitiesParameters(null);
    }
    
    @Test
    public void testCreateWithAcceptedVersion() {
        RequestParameters parameters = new GetCapabilitiesParameters("SOS", "1.0.0");
        assertTrue(parameters.contains(SERVICE_PARAMETER));
        assertFalse(parameters.contains(SECTION_PARAMETER));
        assertTrue(parameters.contains(ACCEPT_VERSIONS_PARAMETER));
        assertFalse(parameters.contains(ACCEPT_FORMATS_PARAMETER));
    }
    
    @Test
    public void testCreateWithMultipleAcceptedVersions() {
        RequestParameters parameters = new GetCapabilitiesParameters("SOS", "1.0.0", "2.0.0");
        assertTrue(parameters.contains(SERVICE_PARAMETER));
        assertFalse(parameters.contains(SECTION_PARAMETER));
        assertTrue(parameters.contains(ACCEPT_VERSIONS_PARAMETER));
        assertFalse(parameters.contains(ACCEPT_FORMATS_PARAMETER));
        assertTrue(parameters.hasMultipleValues(ACCEPT_VERSIONS_PARAMETER));
    }
    
    @Test
    public void testCreateWithMultipleAcceptedFormats() {
        GetCapabilitiesParameters parameters = new GetCapabilitiesParameters("SOS");
        parameters.addAcceptedFormat("text/xml");
        parameters.addAcceptedFormat("application/xml");
        assertTrue(parameters.contains(SERVICE_PARAMETER));
        assertFalse(parameters.contains(SECTION_PARAMETER));
        assertFalse(parameters.contains(ACCEPT_VERSIONS_PARAMETER));
        assertTrue(parameters.contains(ACCEPT_FORMATS_PARAMETER));
        assertTrue(parameters.hasMultipleValues(ACCEPT_FORMATS_PARAMETER));
    }
    
    @Test
    public void testOverridingParameterValue() {
        GetCapabilitiesParameters parameters = new GetCapabilitiesParameters("SOS");
        assertFalse(parameters.overrideSingleValue(SERVICE_PARAMETER, "SPS"));
        assertEquals("SPS", parameters.getSingleValue(SERVICE_PARAMETER));

        // check size change when parameter was not added before
        assertTrue(parameters.overrideSingleValue(UPDATE_SEQUENCE, "300"));
        assertEquals("300", parameters.getSingleValue(UPDATE_SEQUENCE));
    }
    
}
