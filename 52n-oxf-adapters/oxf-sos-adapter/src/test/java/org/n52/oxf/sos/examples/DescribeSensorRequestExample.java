package org.n52.oxf.sos.examples;

import static org.junit.Assert.fail;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.DESCRIBE_SENSOR;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;

@Ignore // comment out to run demo class via JUnit
public class DescribeSensorRequestExample extends SosAdapterExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        try {
            Operation operation = createDescribeSensorOperation();
            OperationResult result = adapter.doOperation(operation, createParameterContainer());
            InputStream responseStream = result.getIncomingResultAsStream();
            parseResponse(responseStream);
        }
        catch (ExceptionReport e) {
            fail("Error at remote:\n" + formatExceptionReport(e));
        }
        catch (OXFException e) {
            fail("SOS operation failed: " + e.getMessage());
        }
    }

    private Operation createDescribeSensorOperation() {
        return new Operation(DESCRIBE_SENSOR, getServiceGETUrl(), getServicePOSTUrl());
    }

    private ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT, DESCRIBE_SENSOR_OUTPUT_FORMAT);
        parameters.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, "Wasserstand-Ledasperrwerk_Up_3880050");
        return parameters;
    }

}
