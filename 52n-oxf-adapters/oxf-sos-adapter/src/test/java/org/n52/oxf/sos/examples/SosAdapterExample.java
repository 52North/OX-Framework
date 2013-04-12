package org.n52.oxf.sos.examples;

import static org.junit.Assert.fail;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_CAPABILITIES;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SosAdapterExample {
    
    private static final String REPORT_SEPARATOR_LINE = "\n################################\n";

    private static final Logger LOGGER = LoggerFactory.getLogger(SosAdapterExample.class);

    private static final String SOS_BY_GET_URL = "http://sensorweb.demo.52north.org:80/PegelOnlineSOSv2.1/sos";
    
    private static final String SOS_BY_POST_URL = "http://sensorweb.demo.52north.org:80/PegelOnlineSOSv2.1/sos";

    @Test
    public void getCapabilities() {
        try {
            SOSAdapter adapter = new SOSAdapter("1.0.0");
            Operation operation = createGetCapabilitiesOperation();
            OperationResult result = adapter.doOperation(operation, createParameterContainer());
            InputStream responseStream = result.getIncomingResultAsStream();
            parseResponseWithXmlBeans(responseStream); // or parse via other parser 
            //parseResponseWithYourFavouriteXmlAPI(responseStream);
        }
        catch (ExceptionReport e) {
            fail("Error at remote:\n" + formatExceptionReport(e));
        }
        catch (OXFException e) {
            fail("SOS operation failed: " + e.getMessage());
        }
    }

    private void parseResponseWithXmlBeans(InputStream responseStream) {
        try {
            LOGGER.info(XmlObject.Factory.parse(responseStream).xmlText());
        }
        catch (XmlException e) {
            fail("Could not parse XML.");
        }
        catch (IOException e) {
            fail("Could not read response stream.");
        }
    }

    private Operation createGetCapabilitiesOperation() {
        return new Operation(GET_CAPABILITIES, SOS_BY_GET_URL, SOS_BY_POST_URL);
    }

    private ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(GET_CAPABILITIES_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER, "1.0.0");
        return parameters;
    }
    
    private String formatExceptionReport(ExceptionReport report) {
        StringBuilder sb = new StringBuilder();
        Iterator<OWSException> iterator = report.getExceptionsIterator();
        while (iterator.hasNext()) {
            sb.append(REPORT_SEPARATOR_LINE);
            sb.append(formatOwsException(iterator.next()));
        }
        sb.append(REPORT_SEPARATOR_LINE);
        return sb.toString();
    }

    private String formatOwsException(OWSException exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("ExceptionCode: ").append(exception.getExceptionCode()).append("\n");
        sb.append("Message: ").append(exception.getMessage()).append("\n");
        sb.append("Caused by: ").append(exception.getCause());
        return sb.toString();
    }

}
