package org.n52.oxf.sos.examples;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SosAdapterExample {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SosAdapterExample.class);
    
    private static final XmlOptions XML_OPTIONS = new XmlOptions().setSavePrettyPrint().setSaveOuter();

    private static final String REPORT_SEPARATOR_LINE = "\n################################\n";

    private static final String SOS_BY_GET_URL = "http://sensorweb.demo.52north.org:80/PegelOnlineSOSv2.1/sos";
    
    private static final String SOS_BY_POST_URL = "http://sensorweb.demo.52north.org:80/PegelOnlineSOSv2.1/sos";

    protected SOSAdapter adapter;

    @Before
    public void setUp() throws Exception {
        adapter = new SOSAdapter("1.0.0");
    }
    
    public String getServiceGETUrl() {
        return SOS_BY_GET_URL;
    }
    
    public String getServicePOSTUrl() {
        return SOS_BY_POST_URL;
    }
    
    protected void parseResponse(InputStream responseStream) {
        parseResponseWithXmlBeans(responseStream); // or parse via other parser 
        //parseResponseWithYourFavouriteXmlAPI(responseStream);
    }

    private void parseResponseWithXmlBeans(InputStream responseStream) {
        try {
            XmlObject xmlResponse = XmlObject.Factory.parse(responseStream);
            LOGGER.info(xmlResponse.xmlText(XML_OPTIONS));
        }
        catch (XmlException e) {
            fail("Could not parse XML.");
        }
        catch (IOException e) {
            fail("Could not read response stream.");
        }
    }

    protected String formatExceptionReport(ExceptionReport report) {
        StringBuilder sb = new StringBuilder();
        Iterator<OWSException> iterator = report.getExceptionsIterator();
        while (iterator.hasNext()) {
            sb.append(REPORT_SEPARATOR_LINE);
            sb.append(formatOwsException(iterator.next()));
        }
        sb.append(REPORT_SEPARATOR_LINE);
        return sb.toString();
    }

    protected String formatOwsException(OWSException exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("ExceptionCode: ").append(exception.getExceptionCode()).append("\n");
        sb.append("Message: ").append(exception.getMessage()).append("\n");
        sb.append("Caused by: ").append(exception.getCause());
        return sb.toString();
    }

}
