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

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.REGISTER_SENSOR;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.SOSRequestBuilder_100;

@Ignore // comment out to run demo class via JUnit
public class RegisterSensorRequestExample extends SosAdapterRequestExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        
        // TODO what if SOS-T interface is protected
        
        performOperationParseResult(createRegisterSensorOperation());
    }

    private Operation createRegisterSensorOperation() {
        return new Operation(REGISTER_SENSOR, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, "1.0.0");
                                     
//        parameters.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, "text/xml;subtype=\"sensorML/1.0.1\"");
//        parameters.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, "Wasserstand-Ledasperrwerk_Up_3880050");
        return parameters;
    }

}
