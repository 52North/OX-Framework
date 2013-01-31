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

import static java.util.UUID.randomUUID;
import static org.n52.oxf.ses.adapter.SESRequestBuilder_00.SesRequestBuilder.aSesRequest;
import static org.n52.oxf.ses.adapter.SESUtils.addNamespacesToEnvelope_000;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.SES;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSA;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSN_B;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSN_BR;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.WSRF_RL;
import static org.n52.oxf.ses.adapter.SESUtils.SesNamespace.XSD;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addNewWsaMessageId;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaAction;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaFrom;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.addWsaRecipientTo;
import static org.n52.oxf.xmlbeans.tools.SoapUtil.stripSoapEnvelope;

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.xmlbeans.tools.SoapUtil;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
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

    private static final String SOAP_ACTION_GET_CAPABILITIES_REQUEST = "http://www.opengis.net/ses/GetCapabilitiesRequest";
    private static final String SOAP_ACTION_DESTROY_REQUEST = "http://docs.oasis-open.org/wsrf/rlw-2/ImmediateResourceTermination/DestroyRequest";
    private static final String SOAP_ACTION_SUBSCRIBE_REQUEST = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";
    private static final String SOAP_ACTION_NOTIFY_REQUEST = "http://docs.oasis-open.org/wsn/b-2/NotificationConsumer/NotifyRequest";
    private static final String SOAP_ACTION_REGISTER_PUBLISHER_REQUEST = "http://docs.oasis-open.org/wsn/br-2/RegisterPublisher/RegisterPublisherRequest";
    private static final String W3C_ADDRESSING_ROLE_ANONYMOUS = "http://www.w3.org/2005/08/addressing/role/anonymous";

    private final String ns_ses = "http://www.opengis.net/ses/0.0";
    
    private final String ns_addressing = "http://www.w3.org/2005/08/addressing";
    
    private final String ns_wsNotification = "http://docs.oasis-open.org/wsn/b-2";
    
    private final String ns_wsBrokeredNotification = "http://docs.oasis-open.org/wsn/br-2";
    
    private final String ns_resourceId = "http://ws.apache.org/muse/addressing";
    
    static class SesRequestBuilder {
        
        EnvelopeDocument envelope = null;
        
        SesRequestBuilder(XmlObject body) {
            envelope = body == null 
                    ? SoapUtil.wrapToSoapEnvelope(XmlObject.Factory.newInstance())
                    : SoapUtil.wrapToSoapEnvelope(body);
            addNamespacesToEnvelope_000(envelope.getEnvelope());
        }
        
        static SesRequestBuilder aSesRequest() {
            return new SesRequestBuilder(null);
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
        EnvelopeDocument request = aSesRequest()
                            .addSoapAction(SOAP_ACTION_GET_CAPABILITIES_REQUEST)
                            .addRecipient(getStringValueFor(GET_CAPABILITIES_SES_URL, parameters))
                            .addFrom(W3C_ADDRESSING_ROLE_ANONYMOUS)
                            .addMessageId()
                            .build();
        
        XmlObject body = stripSoapEnvelope(request);
        
        XmlCursor cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(SES.createQNameFor("GetCapabilities"));
        cur.insertAttributeWithValue("Service", "SES");
        cur.dispose();
        
        return request.xmlText();
    }

    private String getStringValueFor(String commonName, ParameterContainer parameters) {
        return (String) parameters.getParameterShellWithCommonName(commonName).getSpecifiedValue();
    }

    public String buildNotifyRequest(ParameterContainer parameters) {
        EnvelopeDocument request = aSesRequest()
                            .addSoapAction(SOAP_ACTION_NOTIFY_REQUEST)
                            .addRecipient(getStringValueFor(NOTIFY_SES_URL, parameters))
                            .addFrom(W3C_ADDRESSING_ROLE_ANONYMOUS)
                            .addMessageId()
                            .build();

        XmlObject body = stripSoapEnvelope(request);
        XmlCursor cur = body.newCursor();
        cur.toFirstContentToken();
        
        cur.beginElement(WSN_B.createQNameFor("Notify"));
        cur.beginElement(WSN_B.createQNameFor("NotificationMessage"));
        cur.beginElement(WSN_B.createQNameFor("Topic"));
        cur.insertAttributeWithValue("Dialect", getStringValueFor(NOTIFY_TOPIC_DIALECT, parameters));
        cur.insertChars(getStringValueFor(NOTIFY_TOPIC, parameters));
        cur.toNextToken();

        cur.beginElement(WSN_B.createQNameFor("Message"));
        cur.insertChars("@MSG_REPLACER@");
        cur.dispose();

        String toReplace = request.xmlText();
        String xmlMessage = getStringValueFor(NOTIFY_XML_MESSAGE, parameters);
        return toReplace.replaceAll("@MSG_REPLACER@", xmlMessage);
    }

    public String buildRegisterPublisherRequest(ParameterContainer parameters) {
        
        EnvelopeDocument request = aSesRequest()
                        .addSoapAction(SOAP_ACTION_REGISTER_PUBLISHER_REQUEST)
                        .addRecipient(getStringValueFor(REGISTER_PUBLISHER_SES_URL, parameters))
                        .addFrom(getFromAddress(REGISTER_PUBLISHER_FROM, parameters))
                        .addMessageId()
                        .build();
        
        XmlObject body = stripSoapEnvelope(request);
        XmlCursor cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(WSN_BR.createQNameFor("RegisterPublisher"));

        String lifeTimeDuration = getStringValueFor(REGISTER_PUBLISHER_LIFETIME_DURATION, parameters);
        cur.insertElementWithText(WSRF_RL.createQNameFor("RequestedLifetimeDuration"), lifeTimeDuration);

        cur.beginElement(WSN_BR.createQNameFor("Topic"));
        cur.insertAttributeWithValue("Dialect", getStringValueFor(REGISTER_PUBLISHER_TOPIC_DIALECT, parameters));
        cur.insertChars(getStringValueFor(REGISTER_PUBLISHER_TOPIC, parameters));
        cur.toNextToken();

        cur.insertChars("@SML_REPLACER@");
        cur.toNextToken();

        cur.insertElementWithText(new QName(ns_wsBrokeredNotification,"Demand","wsbn"), "no"); // FIXME is this a default value?
        cur.insertElementWithText(new QName(ns_addressing,"EndpointReferenceType","wsa"), "ignore"); // FIXME is this a default value?
        cur.dispose();
        
        String toReplace = request.xmlText();
        String sml = getStringValueFor(REGISTER_PUBLISHER_SENSORML, parameters);
        return toReplace.replaceAll("@SML_REPLACER@", sml);
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

        XmlObject body = stripSoapEnvelope(request);
        XmlCursor cur = body.newCursor();

        cur.toFirstContentToken();
        
        cur.beginElement(WSN_B.createQNameFor("Subscribe"));
        cur.beginElement(WSN_B.createQNameFor("ConsumerReference"));
        
        String consumer = getStringValueFor(SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS, parameters);
        cur.insertElementWithText(WSA.createQNameFor("Address"), consumer);
        cur.toNextToken();

        cur.beginElement(WSN_B.createQNameFor("Filter"));

        // we need to check which filters are defined before building the request
        //
        // TOPIC FILTERING
        //
        String topic = getStringValueFor(SUBSCRIBE_FILTER_TOPIC, parameters);
        String topicDialect = getStringValueFor(SUBSCRIBE_FILTER_TOPIC_DIALECT, parameters);
        if(topic != null && topicDialect != null) {
            cur.beginElement(WSN_B.createQNameFor("TopicExpression"));
            cur.insertAttributeWithValue("Dialect", getStringValueFor(SUBSCRIBE_FILTER_TOPIC_DIALECT, parameters));
            cur.insertChars(getStringValueFor(SUBSCRIBE_FILTER_TOPIC, parameters));
            cur.toNextToken();

        }
        //
        // MESSAGE CONTENT FILTERING
        //
        // TODO use level identifier 1,2,3 instead of dialect definition!?
        String messageContentFilter = null;
        String message = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT, parameters);
        String messageDialect = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, parameters);
        if(message != null && messageDialect != null) {
            messageContentFilter = getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT, parameters);
            cur.beginElement(WSN_B.createQNameFor("MessageContent"));
            cur.insertAttributeWithValue("Dialect", getStringValueFor(SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT, parameters));
            cur.insertChars("@MSG_CONT_FILTER@");
            cur.toNextToken();
        }

        //
        // if a initial termination time is not wanted add attribute xsi:nil="true"
        //
        cur.toNextToken();
        String initialTermination = getStringValueFor(SUBSCRIBE_INITIAL_TERMINATION_TIME, parameters);
        if(initialTermination == null){
            cur.beginElement(WSN_B.createQNameFor("InitialTerminationTime"));
            cur.insertAttributeWithValue(XSD.createQNameFor("nil"), "true");
            cur.toNextToken();
        } else{
            cur.insertElementWithText(WSN_B.createQNameFor("InitialTerminationTime"), initialTermination);

        }
        cur.dispose();

        String toReplace = request.xmlText();
        return messageContentFilter != null 
                ? toReplace.replaceAll("@MSG_CONT_FILTER@", messageContentFilter)
                : toReplace;
    }

    public String buildDescribeSensorRequest(ParameterContainer parameter) throws OXFException {
        String request = "";

        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(DESCRIBE_SENSOR_SES_URL).getSpecifiedValue();
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),
                sesURL);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"), "http://www.opengis.net/ses/DescribeSensorRequest");
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"), randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        W3C_ADDRESSING_ROLE_ANONYMOUS);
        cur.dispose();

        cur = body.newCursor();

        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_ses,"DescribeSensor","ses"));
        cur.insertAttributeWithValue("service","SES");
        cur.insertAttributeWithValue("version", SESAdapter.SUPPORTED_VERSIONS[0]);
        cur.insertElementWithText(new QName(ns_ses,"SensorID","ses"),  (String)parameter.getParameterShellWithCommonName(DESCRIBE_SENSOR_SENSOR_ID).getSpecifiedValue());
        cur.dispose();

        request = envDoc.xmlText();

        return request;
    }
    
    public String buildUnsubscribeRequest(ParameterContainer parameter){
    	String request = "";
    	
        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        String sesURL = (String)parameter.getParameterShellWithCommonName(UNSUBSCRIBE_SES_URL).getSpecifiedValue();
        String resourceId = (String)parameter.getParameterShellWithCommonName(UNSUBSCRIBE_REFERENCE).getSpecifiedValue();
       
        String sesSubscriptionManagerContextPath = "";
        String[] split = sesURL.split("/");
        for (int i = 0; i < split.length-1; i++) {
			sesSubscriptionManagerContextPath += split[i]+"/";
		}
        sesSubscriptionManagerContextPath +="SubscriptionManagerContextPath";
        
        XmlCursor cur = null;

        SESUtils.addNamespacesToEnvelope_000(env);

        // header
        cur = header.newCursor();

        cur.toFirstContentToken();
        cur.insertElementWithText(new QName(ns_addressing,"To","wsa"),
                sesSubscriptionManagerContextPath);
        cur.insertElementWithText(new QName(ns_addressing,"Action","wsa"), SOAP_ACTION_DESTROY_REQUEST);
        cur.insertElementWithText(new QName(ns_addressing,"MessageID","wsa"),
                UUID.randomUUID().toString());
        cur.beginElement(new QName(ns_addressing,"From","wsa"));
        cur.insertElementWithText(new QName(ns_addressing,"Address","wsa"),
        W3C_ADDRESSING_ROLE_ANONYMOUS);
        
        cur.toNextToken();
        cur.beginElement(new QName(ns_resourceId, "ResourceId", "muse-wsa"));
        
        cur.insertAttributeWithValue(new QName(ns_addressing, "IsReferenceParameter"), "true");
        cur.insertChars(resourceId);
        cur.dispose();
        
        // body
        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName(ns_wsNotification, "Destroy", "wsnt"));
        cur.dispose();
        request = envDoc.xmlText();
        
    	return request;
    }
    
    public static void main(String[] args) {
    	ParameterContainer paramCon = new ParameterContainer();
		try {
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_SES_URL, "http://localhost:8080/ses-main-3.0-SNAPSHOT/services/SesPortType");
			paramCon.addParameterShell(ISESRequestBuilder.UNSUBSCRIBE_REFERENCE, "MuseResource-3");
			SESRequestBuilder_00 request = new SESRequestBuilder_00();
			System.out.println(request.buildUnsubscribeRequest(paramCon));
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
}
