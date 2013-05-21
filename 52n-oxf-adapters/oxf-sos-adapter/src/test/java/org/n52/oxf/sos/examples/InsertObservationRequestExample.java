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
package org.n52.oxf.sos.examples;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.INSERT_OBSERVATION;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;

//@Ignore // comment out to run demo class via JUnit
public class InsertObservationRequestExample extends SosAdapterRequestExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        // should return an exception when transactional interface is 
        // not supported of public
        performOperationParseResult(createInsertObservationOperation());
    }

    private Operation createInsertObservationOperation() {
        return new Operation(INSERT_OBSERVATION, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(INSERT_OBSERVATION_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(INSERT_OBSERVATION_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1");
        parameters.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, "urn:ogc:def:phenomenon:OGC:1.0.30:waterspeed");
        parameters.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, "id_2001");
        parameters.addParameterShell(INSERT_OBSERVATION_SAMPLING_TIME, "2013-12-12T12:00:00Z");
        parameters.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, "m3/s");
        parameters.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, "23.5");
        return parameters;
    }

}
