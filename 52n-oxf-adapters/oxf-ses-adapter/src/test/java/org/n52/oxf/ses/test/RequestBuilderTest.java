/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.ses.test;

import java.io.IOException;
import java.util.Collection;


import net.opengis.om.x10.ObservationType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESAdapter;
import org.n52.oxf.ses.adapter.SESRequestBuilder_00;
import org.n52.oxf.xmlbeans.parser.SASamplingPointCase;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.oasisOpen.docs.wsn.b2.NotifyDocument.Notify;
import org.oasisOpen.docs.wsn.b2.QueryExpressionType;
import org.oasisOpen.docs.wsn.b2.SubscribeDocument.Subscribe;
import org.oasisOpen.docs.wsn.b2.TopicExpressionType;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

public class RequestBuilderTest {

    @Test public void
    shouldCreateValidNotification()
            throws XmlException, IOException, OXFException {
        String sesUrl = "http://ses.host";
        String dialect = "http://my-funny/dialect";
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_SES_URL, sesUrl);
        parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_TOPIC_DIALECT, dialect);
        parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_TOPIC, "<start>topic</start>");
        parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_XML_MESSAGE, readMessage());

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildNotifyRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        XMLBeansParser.registerLaxValidationCase(SASamplingPointCase.getInstance());
        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("Notification is not valid: "+ errors, errors.isEmpty());

        XmlCursor bodyCur = envelope.getEnvelope().getBody().newCursor();
        bodyCur.toFirstChild();
        Assert.assertTrue(bodyCur.getObject() instanceof Notify);
        XmlCursor tmpCur = ((Notify) bodyCur.getObject()).getNotificationMessageArray()[0].getMessage().newCursor();
        tmpCur.toFirstChild();
        Assert.assertTrue(tmpCur.getObject() instanceof ObservationType);

        Assert.assertTrue(((Notify) bodyCur.getObject()).getNotificationMessageArray()[0].getTopic().getDialect().trim().equals(dialect));

        tmpCur = ((Notify) bodyCur.getObject()).getNotificationMessageArray()[0].getTopic().newCursor();

        Assert.assertTrue(tmpCur.getObject().xmlText().contains("<start>"));

    }

    @Test public void
    shouldCreateValidRegisterPublisherRequest()
            throws OXFException, XmlException, IOException {
        String sesUrl = "http://ses.host";
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_SES_URL, sesUrl);
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_FROM, "http://my-funny/sender/address");
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_LIFETIME_DURATION, "2012-12-02");
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_TOPIC_DIALECT, "http://my-funny/dialect");
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_TOPIC, "<start>topic</start>");
        parameters.addParameterShell(SESRequestBuilder_00.REGISTER_PUBLISHER_SENSORML, readSensorML());

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildRegisterPublisherRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("RegisterPublisher is not valid: "+ errors, errors.isEmpty());
    }

    @Test public void
    shouldCreateValidDestroyRegistrationRequest()
            throws OXFException, XmlException, IOException {
        String sesUrl = "http://ses.host";
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.DESTROY_REGISTRATION_SES_URL, sesUrl);
        parameters.addParameterShell(SESRequestBuilder_00.DESTROY_REGISTRATION_REFERENCE, "Publisher-1");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildDestroyRegistrationRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("DestroyRegistration is not valid: "+ errors, errors.isEmpty());
    }

    @Test public void
    shouldCreateValidCapabilitiesRequest()
            throws OXFException, XmlException, IOException {
        String sesUrl = "http://ses.host";
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.GET_CAPABILITIES_SES_URL, sesUrl);

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildGetCapabilitiesRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("GetCapabilities is not valid: "+ errors, errors.isEmpty());
    }

    @Test public void
    shouldCreateValidSubscribeRequest()
            throws OXFException, XmlException, IOException {
        String sesUrl = "http://ses.host";
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_SES_URL, sesUrl);
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, "http://consum.er");
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_INITIAL_TERMINATION_TIME, "2099-12-12");
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_MESSAGE_CONTENT,
                readFilter(),
                readFilter());
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT,
                "http://www.opengis.net/ses/filter/level3",
                "http://www.opengis.net/ses/filter/level3");

        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_TOPIC, "<start>topic</start>");
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_TOPIC_DIALECT, "http://my-funny/dialect");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildSubscribeRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("SubscribeRequest is not valid: "+ errors, errors.isEmpty());
    }

    @Test public void
    shouldCreateMinimalSubscribeRequest()
                throws OXFException, XmlException, IOException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_SES_URL, "http://ses.host");
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, "http://consum.er");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildSubscribeRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("SubscribeRequest is not valid: "+ errors, errors.isEmpty());
    }

    @Test(expected=IllegalArgumentException.class) public void
    shouldThrowExceptionOnMissingConsumerForSubscription()
            throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_SES_URL, "http://ses.host");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        request.buildSubscribeRequest(parameters);
    }

    @Test(expected=IllegalArgumentException.class) public void
    shouldThrowExceptionOnMissingServiceUrlForSubscription()
            throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, "http://test.test");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        request.buildSubscribeRequest(parameters);
    }

    @Test public void
    shouldCreateValidLevel1SubscribeRequest()
            throws OXFException, XmlException {
        String consumer = "http://localhost:9090/GSM2SWE/sesl";
        String host = "http://ses.host";
        String topicString = "ses:Measurements";
        String dialect = "http://www.w3.org/TR/1999/REC-xpath-19991116";
        String xpath = "//@xlink:href='urn:ogc:object:procedure:CITE:WeatherService:LGA'";

        ParameterContainer parameter = new ParameterContainer();
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_SES_URL, host);
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, consumer);
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_TOPIC, topicString);
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, dialect);
        parameter.addParameterShell(ISESRequestBuilder.SUBSCRIBE_FILTER_MESSAGE_CONTENT, xpath);

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildSubscribeRequest(parameter);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("SubscribeRequest is not valid: "+ errors, errors.isEmpty());

        Body body = envelope.getEnvelope().getBody();
        XmlCursor cur = body.newCursor();
        cur.toFirstChild();
        XmlObject subscribeRequestObject = cur.getObject();
        cur.dispose();

        if (!(subscribeRequestObject instanceof Subscribe)) throw new IllegalStateException("Body does not contain a Subscribe request.");

        Subscribe subscribeRequest = (Subscribe) subscribeRequestObject;
        Assert.assertTrue(subscribeRequest.getConsumerReference().getAddress().getStringValue().trim().equals(consumer.trim()));

        cur = subscribeRequest.getFilter().newCursor();
        cur.toFirstChild();
        do {
            XmlObject filterElement = cur.getObject();
            if (filterElement instanceof QueryExpressionType) {
                QueryExpressionType qet = (QueryExpressionType) filterElement;
                Assert.assertTrue(qet.getDialect().trim().equals(dialect.trim()));
                XmlCursor tmpCur = qet.newCursor();
                Assert.assertTrue(tmpCur.toFirstContentToken() == TokenType.TEXT);
                Assert.assertTrue(tmpCur.getChars().trim().equals(xpath.trim()));
            }
            else if (filterElement instanceof TopicExpressionType) {
                TopicExpressionType tet = (TopicExpressionType) filterElement;
                Assert.assertTrue(tet.getDialect().trim().equals(SESRequestBuilder_00.DEFAULT_TOPIC_DIALECT));
                XmlCursor tmpCur = tet.newCursor();
                Assert.assertTrue(tmpCur.toFirstContentToken() == TokenType.TEXT);
                Assert.assertTrue(tmpCur.getChars().trim().equals(topicString.trim()));
            }
        } while (cur.toNextSibling());
    }

    @Test public void
    shouldCreateValidDescribeSensorRequest()
            throws OXFException, XmlException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.DESCRIBE_SENSOR_SES_URL, "http://ses.host");
        parameters.addParameterShell(SESRequestBuilder_00.DESCRIBE_SENSOR_SENSOR_ID, "urn:n52:dummy");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildDescribeSensorRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("DescribeSensor request is not valid: "+ errors, errors.isEmpty());
    }

    @Test public void
    shouldCreateValidUnsubscribeRequest()
            throws OXFException, XmlException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.UNSUBSCRIBE_SES_URL, "http://ses.host");
        parameters.addParameterShell(SESRequestBuilder_00.UNSUBSCRIBE_REFERENCE, "urn:n52:dummy");

        SESRequestBuilder_00 request = new SESRequestBuilder_00();
        String asText = request.buildUnsubscribeRequest(parameters);
        EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

        Collection<XmlError> errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("Unsubscribe request is not valid: "+ errors, errors.isEmpty());

        parameters = new ParameterContainer();
        parameters.addParameterShell(SESRequestBuilder_00.UNSUBSCRIBE_SES_URL, "http://ses.host");
        parameters.addParameterShell(SESRequestBuilder_00.UNSUBSCRIBE_REFERENCE_XML, "<muse-wsa:ResourceId " +
                "xmlns:muse-wsa=\"http://ws.apache.org/muse/addressing\" wsa:IsReferenceParameter=\"true\" " +
                " xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" +
                "Subscription-2</muse-wsa:ResourceId>");

        asText = request.buildUnsubscribeRequest(parameters);
        envelope = EnvelopeDocument.Factory.parse(asText);

        errors = XMLBeansParser.validate(envelope);
        Assert.assertTrue("Unsubscribe request is not valid: "+ errors, errors.isEmpty());
    }

    private String readFilter() throws XmlException, IOException {
        XmlObject xo = XmlObject.Factory.parse(getClass().getResourceAsStream("Filter.xml"));
        return xo.xmlText(new XmlOptions().setSavePrettyPrint());
    }

    private String readSensorML() throws XmlException, IOException {
        XmlObject xo = XmlObject.Factory.parse(getClass().getResourceAsStream("SensorML.xml"));
        return xo.xmlText(new XmlOptions().setSavePrettyPrint());
    }

    private String readMessage() throws XmlException, IOException {
        XmlObject xo = XmlObject.Factory.parse(getClass().getResourceAsStream("NotifyMessage.xml"));
        return xo.xmlText(new XmlOptions().setSavePrettyPrint());
    }

}
