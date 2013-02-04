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

import static org.n52.oxf.ses.adapter.SESAdapter.SUPPORTED_VERSIONS;
import static org.n52.oxf.ses.adapter.SESRequestBuilder_00.SesRequestBuilder.aSesRequest;
import static org.n52.oxf.ses.adapter.SESUtils.addNamespacesToEnvelope_000;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSA;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSN_B;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSN_BR;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.XSD;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addNewWsaMessageId;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaAction;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaFrom;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaRecipientTo;

import java.util.Calendar;
import java.util.Locale;

import javax.xml.namespace.QName;

import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.ses.x00.DescribeSensorDocument;
import net.opengis.ses.x00.DescribeSensorDocument.DescribeSensor;
import net.opengis.ses.x00.GetCapabilitiesDocument;
import net.opengis.ses.x00.GetCapabilitiesDocument.GetCapabilities;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.xmlbeans.tools.SoapUtil;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.oasisOpen.docs.wsn.b2.NotificationMessageHolderType;
import org.oasisOpen.docs.wsn.b2.NotificationMessageHolderType.Message;
import org.oasisOpen.docs.wsn.b2.NotifyDocument;
import org.oasisOpen.docs.wsn.b2.NotifyDocument.Notify;
import org.oasisOpen.docs.wsn.b2.TopicExpressionType;
import org.oasisOpen.docs.wsn.br2.RegisterPublisherDocument;
import org.oasisOpen.docs.wsn.br2.RegisterPublisherDocument.RegisterPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

/**
 * contains attributes and methods to encode SESOperationRequests as String in xml-format
 * 
 * @author <a href="mailto:artur.osmanov@uni-muenster.de">Artur Osmanov</a>
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SESRequestBuilder_00 implements ISESRequestBuilder{

	private static final Logger logger = LoggerFactory.getLogger(SESRequestBuilder_00.class);

	private static final String SOAP_ACTION_DESCRIBE_SENSOR_REQUEST = "http://www.opengis.net/ses/DescribeSensorRequest";
	private static final String SOAP_ACTION_GET_CAPABILITIES_REQUEST = "http://www.opengis.net/ses/GetCapabilitiesRequest";

	// TODO still being used?
	private static final String SOAP_ACTION_DESTROY_REQUEST = "http://docs.oasis-open.org/wsrf/rlw-2/ImmediateResourceTermination/DestroyRequest";

	private static final String SOAP_ACTION_SUBSCRIBE_REQUEST = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";

	private static final String SOAP_ACTION_NOTIFY_REQUEST = "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/Notify";
	private static final String SOAP_ACTION_REGISTER_PUBLISHER_REQUEST = "http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherRequest";
	private static final String W3C_ADDRESSING_ROLE_ANONYMOUS = "http://www.w3.org/2005/08/addressing/role/anonymous";

	private final String ns_resourceId = "http://ws.apache.org/muse/addressing";

	static class SesRequestBuilder {

		EnvelopeDocument envelope = null;

		static SesRequestBuilder aSesRequest() {
			return aSesRequest(null);
		}

		static SesRequestBuilder aSesRequest(XmlObject soapBody) {
			return new SesRequestBuilder(soapBody);
		}

		private SesRequestBuilder(XmlObject body) {
			envelope = body == null 
					? createNewEnvelopeWithAnEmptyBody()
							: SoapUtil.wrapToSoapEnvelope(body);
					addNamespacesToEnvelope_000(envelope.getEnvelope());
		}

		private EnvelopeDocument createNewEnvelopeWithAnEmptyBody() {
			EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
			envelopeDoc.addNewEnvelope().addNewBody();
			return envelopeDoc;
		}

		SesRequestBuilder addSoapAction(String action) {
			addWsaAction(envelope, action);
			return this;
		}

		SesRequestBuilder addRecipient(String recepient) {
			addWsaRecipientTo(envelope, recepient);
			return this;
		}

		SesRequestBuilder addFrom(String from) {
			addWsaFrom(envelope, from);
			return this;
		}

		SesRequestBuilder addMessageId() {
			addNewWsaMessageId(envelope);
			return this;
		}

		EnvelopeDocument build() {
			return envelope;
		}
	}

	public String buildGetCapabilitiesRequest(ParameterContainer parameters) throws OXFException{

		GetCapabilitiesDocument GetCapabilitiesRequest = GetCapabilitiesDocument.Factory.newInstance();
		GetCapabilities getCapabilities = GetCapabilitiesRequest.addNewGetCapabilities();
		getCapabilities.setService("SES");

		EnvelopeDocument request = aSesRequest(GetCapabilitiesRequest)
				.addSoapAction(SOAP_ACTION_GET_CAPABILITIES_REQUEST)
				.addRecipient(getStringValueFor(GET_CAPABILITIES_SES_URL, parameters))
				.addFrom(W3C_ADDRESSING_ROLE_ANONYMOUS)
				.addMessageId()
				.build();

		return request.xmlText();
	}


	/**
	 * @param commonName the common parameter name.
	 * @param parameters the parameter container.
	 * @return the parameter's specificValue if set, <code>null</code> otherwise.
	 */
	private String getStringValueFor(String commonName, ParameterContainer parameters) {
		ParameterShell value = parameters.getParameterShellWithCommonName(commonName);
		return value == null ? null : (String) value.getSpecifiedValue();
	}

	public String buildNotifyRequest(ParameterContainer parameters) {

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_NOTIFY_REQUEST)
				.addRecipient(getStringValueFor(NOTIFY_SES_URL, parameters))
				.addFrom(W3C_ADDRESSING_ROLE_ANONYMOUS)
				.addMessageId()
				.build();

		Body body = request.getEnvelope().getBody();

		NotifyDocument notifyDoc = NotifyDocument.Factory.newInstance();
		Notify notify = notifyDoc.addNewNotify();
		NotificationMessageHolderType notificationMessage = notify.addNewNotificationMessage();
		TopicExpressionType topic = notificationMessage.addNewTopic();

		fillTopic(topic, getStringValueFor(NOTIFY_TOPIC, parameters), getStringValueFor(NOTIFY_TOPIC_DIALECT, parameters));

		Message message = notificationMessage.addNewMessage();

		try {
			message.set(XmlObject.Factory.parse(getStringValueFor(NOTIFY_XML_MESSAGE, parameters)));
		} catch (XmlException e) {
			logger.warn("Could not set <wsnt:Message> contents!", e);
		}

		body.set(notify);

		return request.xmlText(new XmlOptions().setSavePrettyPrint());
	}


	private void fillTopic(TopicExpressionType topic, String topicMarkup,
			String topicDialect) {
		try {
			topic.set(XmlObject.Factory.parse(topicMarkup));
		} catch (XmlException e1) {
			logger.warn("{} not an XML topic expression. Trying plain text.", topicMarkup);
			XmlUtil.setTextContent(topic, topicMarkup);
		}
		topic.setDialect(topicDialect);
	}

	public String buildNotifyRequestLegacy(ParameterContainer parameters) {

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_NOTIFY_REQUEST)
				.addRecipient(getStringValueFor(NOTIFY_SES_URL, parameters))
				.addFrom(W3C_ADDRESSING_ROLE_ANONYMOUS)
				.addMessageId()
				.build();

		Body body = request.getEnvelope().getBody();

		XmlCursor cur = body.newCursor();
		cur.toFirstContentToken();

		cur.beginElement(WSN_B.createQNameFor("Notify"));
		cur.beginElement(WSN_B.createQNameFor("NotificationMessage"));
		cur.beginElement(WSN_B.createQNameFor("Topic"));
		cur.insertAttributeWithValue("Dialect", getStringValueFor(NOTIFY_TOPIC_DIALECT, parameters));
		cur.insertChars("@TOPIC_REPLACER@");
		cur.toNextToken();

		cur.beginElement(WSN_B.createQNameFor("Message"));
		cur.insertChars("@MSG_REPLACER@");
		cur.dispose();

		String toReplace = request.xmlText(new XmlOptions().setSavePrettyPrint());
		String topic = getStringValueFor(NOTIFY_TOPIC, parameters);
		String xmlMessage = getStringValueFor(NOTIFY_XML_MESSAGE, parameters);
		return toReplace.replaceAll("@TOPIC_REPLACER@", topic).replaceAll("@MSG_REPLACER@", xmlMessage);
	}

	public String buildRegisterPublisherRequest(ParameterContainer parameters) {

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_REGISTER_PUBLISHER_REQUEST)
				.addRecipient(getStringValueFor(REGISTER_PUBLISHER_SES_URL, parameters))
				.addFrom(getFromAddress(REGISTER_PUBLISHER_FROM, parameters))
				.addMessageId()
				.build();

		XmlObject body = request.getEnvelope().getBody();

		RegisterPublisherDocument registerPublisherDoc = RegisterPublisherDocument.Factory.newInstance();
		RegisterPublisher registerPublisher = registerPublisherDoc.addNewRegisterPublisher();
		
		try {
			registerPublisher.set(SensorMLDocument.Factory.parse(getStringValueFor(REGISTER_PUBLISHER_SENSORML, parameters)));
		} catch (XmlException e) {
			logger.warn("Could not parse SensorML!", e);
		}
		
		TopicExpressionType topic = registerPublisher.addNewTopic();

		fillTopic(topic, getStringValueFor(REGISTER_PUBLISHER_TOPIC, parameters),
				getStringValueFor(REGISTER_PUBLISHER_TOPIC_DIALECT, parameters));
		
		registerPublisher.setDemand(false);
		
		registerPublisher.setInitialTerminationTime(
				createTerminationTime(getStringValueFor(REGISTER_PUBLISHER_LIFETIME_DURATION, parameters)));

		body.set(registerPublisherDoc);

		return request.xmlText(new XmlOptions().setSavePrettyPrint());
	}
	
	private Calendar createTerminationTime(String stringTime) {
		DateTime date = new DateTime(stringTime);
		DateTime now = new DateTime();
		if (date.isAfter(now)) {
			return date.toCalendar(Locale.getDefault());
		}
		
		/*
		 * default to one month from now
		 */
		return now.plusMonths(1).toCalendar(Locale.getDefault());
	}


	public String buildRegisterPublisherRequestLegacy(ParameterContainer parameters) {

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_REGISTER_PUBLISHER_REQUEST)
				.addRecipient(getStringValueFor(REGISTER_PUBLISHER_SES_URL, parameters))
				.addFrom(getFromAddress(REGISTER_PUBLISHER_FROM, parameters))
				.addMessageId()
				.build();

		XmlObject body = request.getEnvelope().getBody();
		XmlCursor cur = body.newCursor();

		cur.toFirstContentToken();
		cur.beginElement(WSN_BR.createQNameFor("RegisterPublisher"));


		cur.beginElement(WSN_BR.createQNameFor("Topic"));
		cur.insertAttributeWithValue("Dialect", getStringValueFor(REGISTER_PUBLISHER_TOPIC_DIALECT, parameters));
		cur.insertChars("@TOPIC_REPLACER@");
		cur.toNextToken();

		cur.insertChars("@SML_REPLACER@");
		cur.toNextToken();

		cur.insertElementWithText(WSN_BR.createQNameFor("Demand"), "false");
		
		// following elements not allowed
//		String lifeTimeDuration = getStringValueFor(REGISTER_PUBLISHER_LIFETIME_DURATION, parameters); // change to InitialTerminationTime
//		cur.insertElementWithText(WSRF_RL.createQNameFor("RequestedLifetimeDuration"), lifeTimeDuration);
//		cur.insertElementWithText(WSA.createQNameFor("EndpointReferenceType"), "ignore"); // dude, wtf?!
		cur.dispose();

		String toReplace = request.xmlText();
		String topic = getStringValueFor(REGISTER_PUBLISHER_TOPIC, parameters);
		String sml = getStringValueFor(REGISTER_PUBLISHER_SENSORML, parameters);
		return toReplace.replaceAll("@TOPIC_REPLACER@", topic).replaceAll("@SML_REPLACER@", sml);
	}

	/**
	 * Reads a recipient parameter from given the given {@link ParameterContainer}. If no value was 
	 * set for the asked parameter {@value #W3C_ADDRESSING_ROLE_ANONYMOUS} is returned.
	 * 
	 * @param whichAddress the address value to find.
	 * @param parameters where the address was set.
	 * @return the set address, or {@value #W3C_ADDRESSING_ROLE_ANONYMOUS} if parameter was not set.
	 */
	String getFromAddress(String whichAddress, ParameterContainer parameters) {
		return getStringValueFor(whichAddress, parameters) != null 
				? getStringValueFor(whichAddress, parameters) 
						: W3C_ADDRESSING_ROLE_ANONYMOUS;
	}

	public String buildSubscribeRequest(ParameterContainer parameters) {

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_SUBSCRIBE_REQUEST)
				.addRecipient(getStringValueFor(SUBSCRIBE_SES_URL, parameters))
				.addFrom(getFromAddress(SUBSCRIBE_FROM, parameters))
				.addMessageId()
				.build();

		XmlObject body = request.getEnvelope().getBody();
		XmlCursor xmlCursor = body.newCursor();

		xmlCursor.toFirstContentToken();

		xmlCursor.beginElement(WSN_B.createQNameFor("Subscribe"));
		xmlCursor.beginElement(WSN_B.createQNameFor("ConsumerReference"));

		String consumer = getStringValueFor(SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, parameters);
		xmlCursor.insertElementWithText(WSA.createQNameFor("Address"), consumer);
		xmlCursor.toNextToken();

		addFilter(xmlCursor, parameters);

		//
		// if a initial termination time is not wanted add attribute xsi:nil="true"
		//
		xmlCursor.toNextToken();
		String initialTermination = getStringValueFor(SUBSCRIBE_INITIAL_TERMINATION_TIME, parameters);
		if(initialTermination == null){
			xmlCursor.beginElement(WSN_B.createQNameFor("InitialTerminationTime"));
			xmlCursor.insertAttributeWithValue(XSD.createQNameFor("nil"), "true");
			xmlCursor.toNextToken();
		} else{
			xmlCursor.insertElementWithText(WSN_B.createQNameFor("InitialTerminationTime"),
					new DateTime(createTerminationTime(initialTermination)).toString(ISODateTimeFormat.dateTimeNoMillis()));

		}
		xmlCursor.dispose();

		String messageContentFilter = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT, parameters);
		String toReplace = request.xmlText();
		return messageContentFilter != null 
				? toReplace.replaceAll("@MSG_CONT_FILTER@", messageContentFilter)
						: toReplace;
	}

	private void addFilter(XmlCursor xmlCursor, ParameterContainer parameters) {
		xmlCursor.beginElement(WSN_B.createQNameFor("Filter"));
		// we need to check which filters are defined before building the request
		//
		// TOPIC FILTERING
		//
		String topic = getStringValueFor(SUBSCRIBE_FILTER_TOPIC, parameters);
		String topicDialect = getStringValueFor(SUBSCRIBE_FILTER_TOPIC_DIALECT, parameters);
		if(topic != null && topicDialect != null) {
			addTopicFilter(parameters, xmlCursor);
		}
		//
		// MESSAGE CONTENT FILTERING
		//
		// TODO use level identifier 1,2,3 instead of dialect definition!?
		String message = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT, parameters);
		String messageDialect = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, parameters);
		if(message != null && messageDialect != null) {
			addMessageFilter(parameters, xmlCursor);
		}
	}

	private void addTopicFilter(ParameterContainer parameters, XmlCursor cur) {
		cur.beginElement(WSN_B.createQNameFor("TopicExpression"));
		cur.insertAttributeWithValue("Dialect", getStringValueFor(SUBSCRIBE_FILTER_TOPIC_DIALECT, parameters));
		cur.insertChars(getStringValueFor(SUBSCRIBE_FILTER_TOPIC, parameters));
		cur.toNextToken();
	}

	private void addMessageFilter(ParameterContainer parameters, XmlCursor cur) {
		cur.beginElement(WSN_B.createQNameFor("MessageContent"));
		cur.insertAttributeWithValue("Dialect", getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, parameters));
		cur.insertChars("@MSG_CONT_FILTER@");
		cur.toNextToken();
	}

	public String buildDescribeSensorRequest(ParameterContainer parameters) throws OXFException {

		DescribeSensorDocument describeSensorRequest = DescribeSensorDocument.Factory.newInstance();
		DescribeSensor describeSensor = describeSensorRequest.addNewDescribeSensor();
		describeSensor.setService("SES");
		describeSensor.setVersion(SUPPORTED_VERSIONS[0]);
		describeSensor.setSensorID(getStringValueFor(DESCRIBE_SENSOR_SENSOR_ID, parameters));

		EnvelopeDocument request = aSesRequest(describeSensorRequest)
				.addSoapAction(SOAP_ACTION_DESCRIBE_SENSOR_REQUEST)
				.addRecipient(getStringValueFor(DESCRIBE_SENSOR_SES_URL, parameters))
				.addFrom(getFromAddress(W3C_ADDRESSING_ROLE_ANONYMOUS, parameters))
				.addMessageId()
				.build();

		return request.xmlText();
	}

	public String buildUnsubscribeRequest(ParameterContainer parameters){

		EnvelopeDocument request = aSesRequest()
				.addSoapAction(SOAP_ACTION_DESTROY_REQUEST)
				.addRecipient(getStringValueFor(UNSUBSCRIBE_SES_URL, parameters))
				.addFrom(getFromAddress(W3C_ADDRESSING_ROLE_ANONYMOUS, parameters))
				.addMessageId()
				.build();

		Header soapHeader = request.getEnvelope().getHeader();
		XmlCursor headerXmlCursor = soapHeader.newCursor();
		headerXmlCursor.toLastChild();
		headerXmlCursor.beginElement(new QName(ns_resourceId, "ResourceId", "muse-wsa"));
		headerXmlCursor.insertAttributeWithValue(WSA.createQNameFor("IsReferenceParameter"), "true");
		headerXmlCursor.insertChars(getStringValueFor(UNSUBSCRIBE_REFERENCE, parameters));
		headerXmlCursor.dispose();

		XmlObject body = request.getEnvelope().getBody();
		XmlCursor xmlBodyCursor = body.newCursor();

		xmlBodyCursor = body.newCursor();
		xmlBodyCursor.toFirstContentToken();
		xmlBodyCursor.beginElement(WSN_B.createQNameFor("Destroy"));
		xmlBodyCursor.dispose();

		return request.xmlText();
	}

}
