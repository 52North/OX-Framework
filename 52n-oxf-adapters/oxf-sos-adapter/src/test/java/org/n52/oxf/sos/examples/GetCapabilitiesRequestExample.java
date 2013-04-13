package org.n52.oxf.sos.examples;

import static org.junit.Assert.fail;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_CAPABILITIES;

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
public class GetCapabilitiesRequestExample extends SosAdapterExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    @Ignore // for demonstration
    public void getCapabilities() {
        try {
            Operation operation = createGetCapabilitiesOperation();
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

    private Operation createGetCapabilitiesOperation() {
        return new Operation(GET_CAPABILITIES, getServiceGETUrl(), getServicePOSTUrl());
    }

    private ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(GET_CAPABILITIES_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, "1.0.0");
        return parameters;
    }

}
