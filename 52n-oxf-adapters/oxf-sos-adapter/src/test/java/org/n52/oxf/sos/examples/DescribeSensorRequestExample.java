package org.n52.oxf.sos.examples;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.DESCRIBE_SENSOR;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;

@Ignore // comment out to run demo class via JUnit
public class DescribeSensorRequestExample extends SosAdapterRequestExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        handleOperation(createDescribeSensorOperation());
    }

    private Operation createDescribeSensorOperation() {
        return new Operation(DESCRIBE_SENSOR, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, "text/xml;subtype=\"sensorML/1.0.1\"");
        parameters.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, "Wasserstand-Ledasperrwerk_Up_3880050");
        return parameters;
    }

}
