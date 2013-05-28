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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SosAdapterRequestExample {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SosAdapterRequestExample.class);
    
    private static final XmlOptions XML_OPTIONS = new XmlOptions().setSavePrettyPrint().setSaveInner().setSaveOuter();

    private static final String REPORT_SEPARATOR_LINE = "################################\n";

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
    
    protected void performOperationParseResult(Operation operation){
        OperationResult operationResult = performOperation(operation);
        parseResponse(operationResult.getIncomingResultAsStream());
    }
    
    protected OperationResult performOperation(Operation operation) {
        try {
            ParameterContainer parameters = createParameterContainer();
            return adapter.doOperation(operation, parameters);
        }
        catch (ExceptionReport e) {
            LOGGER.error("Remote reported an error:\n" + formatExceptionReport(e));
            fail("Remote reported an error. See LOG output.");
        }
        catch (OXFException e) {
            LOGGER.error("SOS operation failed.", e);
            fail("SOS operation failed: " + e.getMessage());
        }
        throw new RuntimeException();
    }
    
    protected abstract ParameterContainer createParameterContainer() throws OXFException;

    protected void parseResponse(InputStream responseStream) {
        // alternatively, parse stream with other parsers (e.g. stax)
        XmlObject xml = parseResponseWithXmlBeans(responseStream); 
        LOGGER.info(xml.xmlText(XML_OPTIONS));
    }

    protected XmlObject parseResponseWithXmlBeans(InputStream responseStream) {
        try {
            return XmlObject.Factory.parse(responseStream, XML_OPTIONS);
        }
        catch (XmlException e) {
            LOGGER.error("Could not parse XML.", e);
            fail("Could not parse XML. See LOG output.");
        }
        catch (IOException e) {
            LOGGER.error("Could not read response stream.", e);
            fail("Could not read response stream. See LOG output.");
        }
        throw new RuntimeException();
    }

    protected String formatExceptionReport(ExceptionReport report) {
        StringBuilder sb = new StringBuilder("\n");
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
        sb.append("Locator: ").append(exception.getLocator()).append("\n");
        sb.append("Caused by: ").append(exception.getCause()).append("\n");
        String[] exceptionDetails = exception.getExceptionTexts();
        if (exceptionDetails != null && exceptionDetails.length > 0) {
            sb.append("\t").append(REPORT_SEPARATOR_LINE);
            for (String exceptionText : exceptionDetails) {
                sb.append("\t[EXC] ").append(exceptionText).append("\n");
            }
            sb.append("\t").append(REPORT_SEPARATOR_LINE);
        }
        sb.append("Sent Request: ").append(exception.getSentRequest());
        return sb.append("\n").toString();
    }

}
