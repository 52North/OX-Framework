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
package org.n52.oxf.ses.adapter;

import java.util.UUID;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 20.07.2009
 */
public class TestSESAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSESAdapter.class);

    private final String serviceVersion = "0.0.0";

    private final String testSESEndpoint = "http://localhost:9090/SES-2009/services/SesPortType";

    public static void main(String[] args) throws OXFException, ExceptionReport{

//        new TestSESAdapter().testGetCapabilities();
        
//        new TestSESAdapter().testRegisterPublisher();
        
//        new TestSESAdapter().testNotify();
        
        new TestSESAdapter().testSubscribe();
        
//        new TestSESAdapter().testDescribeSensor();
        
//        new TestSESAdapter().testNotifyResponse();

    }

    private void testNotifyResponse() throws OXFException, ExceptionReport{
        LOGGER.info("TEST NOTIFY RESPONSE");
        
        SESAdapter adapter = new SESAdapter(this.serviceVersion);
        
        Operation op = new Operation(SESAdapter.NOTIFY_RESPONSE, this.testSESEndpoint + "?",this.testSESEndpoint);
        
        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_FROM, "myclient.example.com:4242");
        parameter.addParameterShell(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_TO, "myses.example.com:8080/OGC-SES/ses");
        parameter.addParameterShell(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_RELATES_TO, "the message id of the incoming message.");
        parameter.addParameterShell(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_MESSAGE_ID, UUID.randomUUID().toString());
        
        LOGGER.info(SESResponseBuilderFactory.generateResponseBuilder(serviceVersion).buildNotifyResponseRequest(parameter));
        
        OperationResult opResult = adapter.doOperation(op, parameter);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST NOTIFY RESPONSE DONE");
    }

    private void testGetCapabilities() throws OXFException, ExceptionReport{
        
        LOGGER.info("TEST GET_CAPABILITIES\n");

        SESAdapter adapter = new SESAdapter(this.serviceVersion);

        Operation op = new Operation(SESAdapter.GET_CAPABILITIES,this.testSESEndpoint + "?",this.testSESEndpoint);

        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.GET_CAPABILITIES_SES_URL, this.testSESEndpoint);

        LOGGER.info(SESRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetCapabilitiesRequest(parameter));

        OperationResult opResult = adapter.doOperation(op, parameter);

        ServiceDescriptor desc = adapter.initService(this.testSESEndpoint);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST GET_CAPABILITIES DONE.\n\n");

    }
    
    private void testNotify() throws OXFException, ExceptionReport{
        
        LOGGER.info("TEST NOTIFY");
        
        SESAdapter adapter = new SESAdapter(this.serviceVersion);
        
        Operation op = new Operation(SESAdapter.NOTIFY, this.testSESEndpoint + "?", this.testSESEndpoint);
        
        String xmlMsg = "<om:CategoryObservation xmlns:om=\"http://www.opengis.net/om/1.0\" xmlns:sa=\"http://www.opengis.net/sampling/1.0\">" + 
                    "<om:samplingTime>" + 
                            "<gml:TimeInstant>" + 
                                    "<gml:timePosition>2009-06-09T19:57:25.00</gml:timePosition>" + 
                            "</gml:TimeInstant>" + 
                    "</om:samplingTime>" + 
                    "<om:procedure xlink:href=\"urn:ogc:object:feature:Sensor:human-sensor-web:reporter:01234567890\"/>" + 
                    "<om:observedProperty xlink:href=\"urn:ogc:def:phenomenon:OGC:0.1:WaterPointState\"/>" + 
                    "<om:featureOfInterest>" + 
                            "<sa:SamplingPoint gml:id=\"urn:ogc:object:feature:OGC:0.1:HumanSensorWeb:WaterPoint:ZAWA:53-2-076309\" xsi:type=\"ns:SamplingPointType\">" + 
                            "<gml:name>WaterPoint Miembeni (53-2-076309)</gml:name>" + 
                            "<sa:sampledFeature xlink:href=\"urn:ogc:def:phenomenon:OGC:0.1:WaterPointState\"/>" + 
                                    "<sa:position>" + 
                                            "<gml:Point srsName=\"urn:ogc:def:crs:EPSG:4326\">" + 
                                                    "<gml:pos>-4.8 38.2</gml:pos>" + 
                                            "</gml:Point>" + 
                                    "</sa:position>" + 
                           "</sa:SamplingPoint>" + 
                   "</om:featureOfInterest>" + 
                    "<om:result codeSpace=\"http://www.52north.org/hsw/messagecodes\">WATER_BAD</om:result>" + 
            "</om:CategoryObservation>";
        
        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.NOTIFY_SES_URL, this.testSESEndpoint);
        parameter.addParameterShell(ISESRequestBuilder.NOTIFY_TOPIC, "http://www.opengis.net/ses/topics:AIXMData");
        parameter.addParameterShell(ISESRequestBuilder.NOTIFY_TOPIC_DIALECT, "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        parameter.addParameterShell(ISESRequestBuilder.NOTIFY_XML_MESSAGE, xmlMsg);
        
        LOGGER.info(SESRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildNotifyRequest(parameter));

        OperationResult opResult = adapter.doOperation(op, parameter);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST NOTIFY DONE.\n\n");
        
    }
    
    private void testSubscribe() throws OXFException, ExceptionReport{
        
        LOGGER.info("TEST SUBSCRIBE");
        
        SESAdapter adapter = new SESAdapter(this.serviceVersion);
        
        Operation op = new Operation(SESAdapter.SUBSCRIBE,this.testSESEndpoint + "?",this.testSESEndpoint);
        
        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_SES_URL,this.testSESEndpoint);
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS,"http://localhost:9090/GSM2SWE/sesl");
//        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC_DIALECT,"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC,"ses:Measurements");
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, "http://www.w3.org/TR/1999/REC-xpath-19991116");
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT, "//@xlink:href='urn:ogc:object:procedure:CITE:WeatherService:LGA'");
        // if you do not want your subscription to end automatically, do not define the initial termination time paramter
        // parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_INITIAL_TERMINATION_TIME, "2009-07-24T12:00:00.00000Z");
        
        LOGGER.info(SESRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildSubscribeRequest(parameter));
        
        OperationResult opResult = adapter.doOperation(op, parameter);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST SUBSCRIBE DONE.\n\n");
    }
    
    private void testDescribeSensor() throws OXFException, ExceptionReport{
        
        LOGGER.info("TEST DESCRIBE_SENSOR");
        
        SESAdapter adapter = new SESAdapter(this.serviceVersion);
        
        Operation op = new Operation(SESAdapter.DESCRIBE_SENSOR,this.testSESEndpoint + "?",this.testSESEndpoint);
        
        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.DESCRIBE_SENSOR_SES_URL, this.testSESEndpoint);
        parameter.addParameterShell(ISESRequestBuilder.DESCRIBE_SENSOR_SENSOR_ID, "urn:ogc:object:feature:Sensor:human-sensor-web:reporter:01234567890");
        
        LOGGER.info(SESRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildDescribeSensorRequest(parameter));
        
        OperationResult opResult = adapter.doOperation(op, parameter);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST DESCRIBE_SENSOR DONE.\n\n");
        
    }

    private void testRegisterPublisher() throws OXFException, ExceptionReport{
        
        LOGGER.info("TEST REGISTER_PUBLISHER");
        
        SESAdapter adapter = new SESAdapter(this.serviceVersion);
        
        Operation op = new Operation(SESAdapter.REGISTER_PUBLISHER,this.testSESEndpoint + "?",this.testSESEndpoint);
        
        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_SES_URL, this.testSESEndpoint);
        parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_LIFETIME_DURATION, "2999-12-31T23:59:59+00:00"); // FIXME farfaraway is the year 2999
        parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC_DIALECT,"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_TOPIC,/*"http://www.opengis.net/ses/topics:*/"AIXMData");
        parameter.addParameterShell(ISESRequestBuilder.REGISTER_PUBLISHER_SENSORML,"<sml:member xlink:role=\"urn:ogc:def:role:OGC:sensorSystem\"><sml:system /></sml:member>");
        
        LOGGER.info(SESRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildRegisterPublisherRequest(parameter));
        
        OperationResult opResult = adapter.doOperation(op, parameter);

        LOGGER.info("opResult: " + opResult);
        
        LOGGER.info("TEST REGISTER_PUBLISHER DONE.\n\n");
        
    }

}
