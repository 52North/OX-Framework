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
package org.n52.oxf.xmlbeans.tools;

import java.util.Collection;
import java.util.UUID;

import javax.xml.namespace.QName;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x11.ExceptionType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.FaultDocument;
import org.w3.x2003.x05.soapEnvelope.Header;
import org.w3.x2005.x08.addressing.ActionDocument;
import org.w3.x2005.x08.addressing.FromDocument;
import org.w3.x2005.x08.addressing.MessageIDDocument;
import org.w3.x2005.x08.addressing.RelatesToDocument;
import org.w3.x2005.x08.addressing.ReplyToDocument;
import org.w3.x2005.x08.addressing.ToDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 */
public class SoapUtil {

    /**
     * Creates an exception report from the {@link Throwable}'s stacktrace.<br>
     * <br/>
     * An example looks like the following.
     *
     * <pre>
     * {@code
     *  <Exception>
     *      <message>exMessage</message>
     *      <stackTrace>
     *           [EXC] "Stacktrace element 1"
     *           [EXC] "Stacktrace element 2"
     *           [EXC] "Stacktrace element 3"
     *           <!-- ... -->
     *      </stackTrace>
     *  </Exception>
     * }
     * </pre>
     *
     *
     * @param t
     *        the {@link Throwable} occured
     * @return and OWS 1.1 ExceptionReport containing the {@link Throwable}'s stacktrace information
     * @throws XmlException
     *         if parsing to {@link XmlObject} is unsuccessful
     */
    public static XmlObject createXmlExceptionResponse(Throwable t) {
        ExceptionReportDocument exceptionReportDoc = ExceptionReportDocument.Factory.newInstance();
        ExceptionReport exceptionReport = exceptionReportDoc.addNewExceptionReport();
        ExceptionType exception = exceptionReport.addNewException();
        exception.setLocator("NoApplicableCode");
        exception.addExceptionText(t.getMessage());
        return exceptionReportDoc;
    }

    /**
     * Seeks for a <code>&lt;wsa:To&gt;</code> header element win SOAP Envelope header.
     *
     * @param xml
     *        the SOAP envelope
     * @return the recipient URL the request shall be sent to.
     * @throws XmlException
     *         if no recipient header could not be parsed from the SOAP header
     */
    public static String getWsaRecipientUrlFromSoapHeader(XmlObject xml) throws XmlException {
        if (isSoapEnvelope(xml)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xml;
            Header header = envelope.getEnvelope().getHeader();
            ToDocument wsaTo = (ToDocument) getXmlFromDomNode(header, "To");
            return wsaTo.getTo().getStringValue();
        }
        return null;
    }

    public static String getWsaReplyToUrlFromSoapHeader(XmlObject xml) throws XmlException {
        if (isSoapEnvelope(xml)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xml;
            Header header = envelope.getEnvelope().getHeader();
            ReplyToDocument wsaReplyTo = (ReplyToDocument) getXmlFromDomNode(header, "ReplyTo");
            return wsaReplyTo.getReplyTo().getAddress().getStringValue();
        }
        return null;
    }

    public static String getWsaMessageIdFromSoapHeader(XmlObject xml) throws XmlException {
        if (isSoapEnvelope(xml)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xml;
            Header header = envelope.getEnvelope().getHeader();
            MessageIDDocument msgIdDoc = (MessageIDDocument) getXmlFromDomNode(header, "MessageID");
            return msgIdDoc.getMessageID().getStringValue();
        }
        return null;
    }

    public static String getWsaRelatesToFromSoapHeader(XmlObject xml) throws XmlException {
        if (isSoapEnvelope(xml)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xml;
            Header header = envelope.getEnvelope().getHeader();
            RelatesToDocument relatesToDoc = (RelatesToDocument) getXmlFromDomNode(header, "RelatesTo");
            return relatesToDoc.getRelatesTo().getStringValue();
        }
        return null;
    }


    public static String getWsaActionFromSoapHeader(XmlObject xml) throws XmlException {
        if (isSoapEnvelope(xml)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xml;
            Header header = envelope.getEnvelope().getHeader();
            ActionDocument action = (ActionDocument) getXmlFromDomNode(header, "Action");
            return action.getAction().getStringValue();
        }
        return null;
    }

    public static SchemaType getSchemaTypeOfXmlPayload(XmlObject xml) {
        if (isSoapEnvelope(xml)) {
            return stripSoapEnvelope((EnvelopeDocument) xml, null).schemaType();
        }
        return xml.schemaType();
    }

    /**
     * Strips SOAP envelope from passed argument and returns SOAP body's XML payload. If passed XML is not a
     * SOAP envelope the argument is returned without any processing.
     *
     * @param xmlToStrip
     *        the XML to strip SOAP envelope from.
     * @return the SOAP body's XML payload, or the XML itself if it is not a SOAP envelope.
     */
    public static XmlObject stripSoapEnvelope(XmlObject xmlToStrip) {
        return stripSoapEnvelope(xmlToStrip, null);
    }

    /**
     * Strips SOAP envelope from passed argument and returns SOAP body's XML payload. If passed XML is not a
     * SOAP envelope the argument is returned without any processing.
     *
     * @param xmlToStrip
     *        the XML to strip SOAP envelope from.
     * @param nodeName
     *        the body payload's node name.
     * @return the SOAP body's XML payload, or the XML itself if it is not a SOAP envelope.
     */
    public static XmlObject stripSoapEnvelope(XmlObject xmlToStrip, String nodeName) {
        if (isSoapEnvelope(xmlToStrip)) {
            EnvelopeDocument envelope = (EnvelopeDocument) xmlToStrip;
            return readPayload(envelope, nodeName);
        }
        return xmlToStrip;
    }

    public static boolean isSoapEnvelope(XmlObject xml) {
        return xml != null && xml.schemaType() == EnvelopeDocument.type;
    }


    public static boolean isSoapFault(XmlObject response) {
        XmlObject request = stripSoapEnvelope(response, "Fault");
        return request != null && request instanceof FaultDocument;
    }

    public static XmlObject readPayload(EnvelopeDocument envelope, String nodeName) {
        try {
            return readBodyNodeFrom(envelope, nodeName);
        }
        catch (XmlException e) {
            throw new IllegalArgumentException("Cannot parse from envelope");
        }
    }

    /**
     * @param envelope
     *        the SOAP envelope to read body from
     * @param nodeName
     *        the node's name of the expected body payload
     * @return an XmlBeans {@link XmlObject} representation of the body, or <code>null</code> if node could
     *         not be found.
     * @throws XmlException
     *         if parsing to XML fails
     */
    public static XmlObject readBodyNodeFrom(EnvelopeDocument envelope, String nodeName) throws XmlException {
        Body soapBody = envelope.getEnvelope().getBody();
        if (nodeName == null) {
            XmlCursor bodyCursor = soapBody.newCursor();
            return bodyCursor.toFirstChild() ? bodyCursor.getObject() : null;
        }
        return getXmlFromDomNode(soapBody, nodeName);
    }

    /**
     * @param xml
     *        the node containing xml
     * @param nodeName
     *        the node's name of the DOM node
     * @return an XmlBeans {@link XmlObject} representation of the body, or <code>null</code> if node could
     *         not be found.
     * @throws XmlException
     *         if parsing to XML fails
     */
    public static XmlObject getXmlFromDomNode(XmlObject xml, String nodeName) throws XmlException {
        Node bodyNode = XmlUtil.getDomNode(xml, nodeName);
        return bodyNode == null ? null : XmlObject.Factory.parse(bodyNode);
    }

    public static QName getElementType(XmlObject xml) {
        return xml == null ? null : xml.schemaType().getDocumentElementName();
    }

    public static String getTextContentFromXml(XmlObject xmlElement) {
        Node node = xmlElement.getDomNode();
        return node.getFirstChild().getFirstChild().getNodeValue();
    }

    public static String getTextContentFromAnyNode(XmlObject xmlAnyElement) {
        Node node = xmlAnyElement.getDomNode();
        return node.getFirstChild().getNodeValue();
    }

    public static XmlObject setTextContent(XmlObject xmlObject, String content) {
        XmlCursor cursor = xmlObject.newCursor();
        cursor.toFirstChild();
        cursor.setTextValue(content);
        cursor.dispose();
        return xmlObject;
    }

    public static EnvelopeDocument wrapToSoapEnvelope(XmlObject bodyContent) {
        EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
        Envelope envelope = envelopeDoc.addNewEnvelope();
        Body body = envelope.addNewBody();
        body.set(bodyContent);
        return envelopeDoc;
    }

    public static void addWsaRecipientTo(EnvelopeDocument envelopeDoc, String recipient) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ToDocument toDoc = ToDocument.Factory.newInstance();
        toDoc.addNewTo().setStringValue(recipient);
        addToHeader(header, toDoc.getTo().getDomNode());
    }

    public static void addWsaReplyTo(EnvelopeDocument envelopeDoc, String replyTo) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ReplyToDocument replyToDoc = ReplyToDocument.Factory.newInstance();
        replyToDoc.addNewReplyTo().addNewAddress().setStringValue(replyTo);
        addToHeader(header, replyToDoc.getReplyTo().getDomNode());
    }

    public static void addWsaFrom(EnvelopeDocument envelopeDoc, String from) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        FromDocument fromDoc = FromDocument.Factory.newInstance();
        fromDoc.addNewFrom().addNewAddress().setStringValue(from);
        addToHeader(header, fromDoc.getFrom().getDomNode());
    }

    public static void addWsaAction(EnvelopeDocument envelopeDoc, String action) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ActionDocument actionDoc = ActionDocument.Factory.newInstance();
        actionDoc.addNewAction().setStringValue(action);
        addToHeader(header, actionDoc.getAction().getDomNode());
    }

    public static void addRelatedWsaMessageId(EnvelopeDocument envelopeDoc, String relatedMessageId) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        RelatesToDocument relatesToDoc = RelatesToDocument.Factory.newInstance();
        relatesToDoc.addNewRelatesTo().setStringValue(relatedMessageId);
        addToHeader(header, relatesToDoc.getRelatesTo().getDomNode());
    }


    public static void addNewWsaMessageId(EnvelopeDocument envelopeDoc) {
        addNewWsaMessageId(envelopeDoc, (String) null);
    }

    public static void addNewWsaMessageId(EnvelopeDocument envelopeDoc, String messageIdPrefix) {
        addWsaMessageId(envelopeDoc, generateSoapMessageId(messageIdPrefix));
    }

    private static String generateSoapMessageId(String messageIdPrefix) {
        String randomId = UUID.randomUUID().toString();
        return (messageIdPrefix == null) ? randomId : messageIdPrefix + randomId;
    }

    public static void addWsaMessageId(EnvelopeDocument envelopeDoc, String messageId) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if (!envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        MessageIDDocument messageIdDoc = MessageIDDocument.Factory.newInstance();
        messageIdDoc.addNewMessageID().setStringValue(messageId);
        addToHeader(header, messageIdDoc.getMessageID().getDomNode());
    }

    private static void addToHeader(Header header, Node nodeToAdd) {
        Document ownerDocument = header.getDomNode().getOwnerDocument();
        Node importedNode = ownerDocument.importNode(nodeToAdd, true);
        header.getDomNode().appendChild(importedNode);
    }
//
//    public static void addWssAuthentication(EnvelopeDocument envelopeDocument, String username, String password) throws WSSecurityException {
//        Envelope envelope = envelopeDocument.getEnvelope();
//        WSSecHeader secHeader = new WSSecHeader();
//        secHeader.setMustUnderstand(false);
//        secHeader.insertSecurityHeader(envelope.getDomNode().getOwnerDocument());
//
//        // create the user information
//        WSSecUsernameToken utBuilder = new WSSecUsernameToken();
//        utBuilder.setSecretKeyLength(40);
//        utBuilder.setUserInfo(username, password);
//
//        // add the user information to the Security header of the SOAP message
//        utBuilder.build(envelope.getDomNode().getOwnerDocument(), secHeader);
//    }

    public static boolean validateXml(XmlObject xml) throws XMLHandlingException {
        Collection<XmlError> errors = XMLBeansParser.validate(xml);
        if ( !errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("Invalid request/response:");
            for (XmlError xmlError : errors) {
                sb.append("\n[xmlError] ").append(xmlError.toString());
            }
            throw new XMLHandlingException(sb.toString());
        }
        return true;
    }

}
