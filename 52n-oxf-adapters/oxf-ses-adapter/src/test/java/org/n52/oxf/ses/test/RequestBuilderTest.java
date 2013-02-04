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
package org.n52.oxf.ses.test;

import java.io.IOException;
import java.util.Collection;


import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ses.adapter.SESRequestBuilder_00;
import org.n52.oxf.xmlbeans.parser.SASamplingPointCase;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

public class RequestBuilderTest {

	@Test
	public void shouldCreateValidNotification() throws XmlException, IOException, OXFException {
		String sesUrl = "http://ses.host";
		ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_SES_URL, sesUrl);
		parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_TOPIC_DIALECT, "http://my-funny/dialect");
		parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_TOPIC, "<start>topic</start>");
		parameters.addParameterShell(SESRequestBuilder_00.NOTIFY_XML_MESSAGE, readMessage());

		SESRequestBuilder_00 request = new SESRequestBuilder_00();
		String asText = request.buildNotifyRequest(parameters);
		EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

		XMLBeansParser.registerLaxValidationCase(SASamplingPointCase.getInstance());
		Collection<XmlError> errors = XMLBeansParser.validate(envelope);
		Assert.assertTrue("Notification is not valid: "+ errors, errors.isEmpty());
		//			request.buildNotifyRequestLegacy(parameters);
		//			
		//			long start = System.currentTimeMillis();
		//			request.buildNotifyRequest(parameters);
		//			System.out.println(System.currentTimeMillis()-start);
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequestLegacy(parameters);
		//			System.out.println("vs. "+(System.currentTimeMillis()-start));
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequest(parameters);
		//			System.out.println(System.currentTimeMillis()-start);
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequestLegacy(parameters);
		//			System.out.println("vs. "+(System.currentTimeMillis()-start));
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequest(parameters);
		//			System.out.println(System.currentTimeMillis()-start);
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequestLegacy(parameters);
		//			System.out.println("vs. "+(System.currentTimeMillis()-start));
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequest(parameters);
		//			System.out.println(System.currentTimeMillis()-start);
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequestLegacy(parameters);
		//			System.out.println("vs. "+(System.currentTimeMillis()-start));
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequest(parameters);
		//			System.out.println(System.currentTimeMillis()-start);
		//			
		//			start = System.currentTimeMillis();
		//			request.buildNotifyRequestLegacy(parameters);
		//			System.out.println("vs. "+(System.currentTimeMillis()-start));
	}

	@Test
	public void shouldCreateValidRegisterPublisherRequest() throws OXFException, XmlException, IOException {
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
	
	@Test
	public void shouldCreateValidCapabilitiesRequest() throws OXFException, XmlException, IOException {
		String sesUrl = "http://ses.host";
		ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(SESRequestBuilder_00.GET_CAPABILITIES_SES_URL, sesUrl);

		SESRequestBuilder_00 request = new SESRequestBuilder_00();
		String asText = request.buildGetCapabilitiesRequest(parameters);
		EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

		Collection<XmlError> errors = XMLBeansParser.validate(envelope);
		Assert.assertTrue("GetCapabilities is not valid: "+ errors, errors.isEmpty());
	}
	
	@Test
	public void shouldCreateValidSubscribeRequest() throws OXFException, XmlException, IOException {
		String sesUrl = "http://ses.host";
		ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_SES_URL, sesUrl);
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, "http://consum.er");
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_INITIAL_TERMINATION_TIME, "2099-12-12");
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_MESSAGE_CONTENT, readFilter());
		
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_TOPIC, "<start>topic</start>");
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_TOPIC_DIALECT, "http://my-funny/dialect");
		parameters.addParameterShell(SESRequestBuilder_00.SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, "http://www.opengis.net/ses/filter/level3");

		SESRequestBuilder_00 request = new SESRequestBuilder_00();
		String asText = request.buildSubscribeRequest(parameters);
		EnvelopeDocument envelope = EnvelopeDocument.Factory.parse(asText);

		Collection<XmlError> errors = XMLBeansParser.validate(envelope);
		Assert.assertTrue("SubscribeRequest is not valid: "+ errors, errors.isEmpty());
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
