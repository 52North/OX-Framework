package org.n52.oxf.sos.examples;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_CAPABILITIES;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;

@Ignore // comment out to run demo class via JUnit
public class GetCapabilitiesRequestExample extends SosAdapterRequestExample {
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    @Ignore // for demonstration
    public void getCapabilities() {
        handleOperation(createGetCapabilitiesOperation());
    }

    private Operation createGetCapabilitiesOperation() {
        return new Operation(GET_CAPABILITIES, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(GET_CAPABILITIES_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, "1.0.0");
        return parameters;
    }

}
