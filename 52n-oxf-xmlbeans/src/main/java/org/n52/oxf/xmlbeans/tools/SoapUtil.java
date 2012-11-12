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

package org.n52.oxf.xmlbeans.tools;

import java.util.UUID;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;
import org.w3.x2005.x08.addressing.ActionDocument;
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
     * Gets the actual schema type of the given XML, even if wrapped in a SOAP envelope.
     * 
     * @param xml
     *        XML where to extract the schema type from.
     * @return the XML schema type. If a SOAP envelope is passed in, the schema type of SOAP body's content is
     *         returned rather than returning SOAP Envelope schema type.
     */
    public static SchemaType getSchemaTypeOfXmlPayload(XmlObject xml) {
        if (isSoapEnvelope(xml)) {
            return stripSoapEnvelope((EnvelopeDocument) xml, null).schemaType();
        }
        return xml.schemaType();
    }

    /**
     * Strips SOAP envelope from passed XML and returns SOAP body's XML payload. If passed XML is not a SOAP
     * envelope the argument is returned without any processing.<br>
     * <br>
     * The method is exactly the same as calling
     * 
     * <pre>
     * {@code stripSoapEnvelope(xmlToStrip, null);}
     * 
     * <pre>
     * 
     * @param xmlToStrip
     *        the XML to strip SOAP envelope from.
     * @return the SOAP body's XML payload, or the XML itself if it is not a SOAP envelope.
     */
    public static XmlObject stripSoapEnvelope(XmlObject xmlToStrip) {
        return stripSoapEnvelope(xmlToStrip, null);
    }

    /**
     * Strips SOAP envelope from passed argument and returns SOAP body's XML payload, if <code>nodeName</code>
     * matches the content. If passed XML is not a SOAP envelope the argument is returned without any
     * processing.<br>
     * <br>
     * If <code>null</code> is passed as <code>nodeName</code>, the method behaves like
     * {@link #stripSoapEnvelope(XmlObject)}.
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
            return readBodyNodeFrom(envelope, nodeName);
        }
        return xmlToStrip;
    }

    /**
     * @param xml
     *        the XML to check.
     * @return <code>true</code> if the passed XML is of type {@link EnvelopeDocument#type},
     *         <code>false</code> if <code>null</code> or of any other type.
     */
    public static boolean isSoapEnvelope(XmlObject xml) {
        return xml != null && xml.schemaType() == EnvelopeDocument.type;
    }

    /**
     * Extracts the body content mathing the <code>nodeName</code>, of the passed SOAP envelope.
     * 
     * @param envelope
     *        the SOAP envelope to read body from
     * @param nodeName
     *        the node's name of the expected body payload
     * @return an XmlBeans {@link XmlObject} representation of the body, or <code>null</code> if node could
     *         not be found.
     * @throws IllegalArgumentException
     *         if SOAP body contains content which cannot be parsed to any XML.
     */
    public static XmlObject readBodyNodeFrom(EnvelopeDocument envelope, String nodeName) {
        Body soapBody = envelope.getEnvelope().getBody();
        if (nodeName == null) {
            XmlCursor bodyCursor = soapBody.newCursor();
            return bodyCursor.toFirstChild() ? bodyCursor.getObject() : null;
        }
        try {
            return XmlUtil.getXmlAnyNodeFrom(soapBody, nodeName);
        }
        catch (XmlException e) {
            throw new IllegalArgumentException("Cannot parse from envelope");
        }
    }

    /**
     * Wraps the given content within the body element of a SOAP envelope.
     * 
     * @param bodyContent
     *        the XML content to wrap.
     * @return the SOAP envelope containing the wrapped XML.
     */
    public static EnvelopeDocument wrapToSoapEnvelope(XmlObject bodyContent) {
        EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
        Envelope envelope = envelopeDoc.addNewEnvelope();
        Body body = envelope.addNewBody();
        body.set(bodyContent);
        return envelopeDoc;
    }

    /**
     * Adds the recepient's adress to the SOAP header (Web Service Adressing). If no header is available yet
     * it will be created first.
     * 
     * @param envelopeDoc
     *        the SOAP envelope to add the recepient's adress to.
     * @param recipient
     *        the recepient's adress to add.
     */
    public static void addWsaRecipientTo(EnvelopeDocument envelopeDoc, String recipient) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ToDocument toDoc = ToDocument.Factory.newInstance();
        toDoc.addNewTo().setStringValue(recipient);
        addToHeader(header, toDoc.getTo().getDomNode());
    }

    /**
     * Adds the replyTo adress to the SOAP header (Web Service Adressing). If no header is available yet it
     * will be created first.
     * 
     * @param envelopeDoc
     *        the SOAP envelope to add the replyTo adress to.
     * @param replyTo
     *        the replyTo adress to add.
     */
    public static void addWsaReplyTo(EnvelopeDocument envelopeDoc, String replyTo) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ReplyToDocument replyToDoc = ReplyToDocument.Factory.newInstance();
        replyToDoc.addNewReplyTo().addNewAddress().setStringValue(replyTo);
        addToHeader(header, replyToDoc.getReplyTo().getDomNode());
    }

    /**
     * Adds the SOAP action to the SOAP header (Web Service Adressing). If no header is available yet it will
     * be created first.
     * 
     * @param envelopeDoc
     *        the SOAP envelope to add the SOAP action to.
     * @param action
     *        the SOAP action.
     */
    public static void addWsaAction(EnvelopeDocument envelopeDoc, String action) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ActionDocument actionDoc = ActionDocument.Factory.newInstance();
        actionDoc.addNewAction().setStringValue(action);
        addToHeader(header, actionDoc.getAction().getDomNode());
    }

    /**
     * Adds the related message id to the SOAP header (Web Service Adressing). If no header is available yet
     * it will be created first.
     * 
     * @param envelopeDoc
     *        the SOAP envelope to add the related message id to.
     * @param relatedMessageId
     *        the related message id.
     */
    public static void addRelatedWsaMessageId(EnvelopeDocument envelopeDoc, String relatedMessageId) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        RelatesToDocument relatesToDoc = RelatesToDocument.Factory.newInstance();
        relatesToDoc.addNewRelatesTo().setStringValue(relatedMessageId);
        addToHeader(header, relatesToDoc.getRelatesTo().getDomNode());
    }

    /**
     * Generates a new message id and adds it to the SOAP header (Web Service Adressing). If no header is
     * available yet it will be created first.
     * 
     * @param envelopeDoc
     *        the envelope to add the created message id to.
     */
    public static void addNewWsaMessageId(EnvelopeDocument envelopeDoc) {
        addWsaMessageId(envelopeDoc, null);
    }

    /**
     * Generates a new prefixed message id and adds it to the SOAP header (Web Service Adressing). If no
     * header is available yet it will be created first.<br>
     * <br>
     * The given prefix must include a trailing separator (like '/') as the generated message id will be
     * concatenated to the prefix. If prefix is <code>null</code> the method has the same effect than calling
     * 
     * <pre>
     * {@code addWsaMessageId(envelopeDoc, null);}
     * 
     * <pre>
     * 
     * @param envelopeDoc
     *        the envelope to add the created message id to.
     * @param prefix the id's prefix.
     */
    public static void addNewWsaMessageIdWithPrefix(EnvelopeDocument envelopeDoc, String prefix) {
        addWsaMessageId(envelopeDoc, generateSoapMessageId(prefix));
    }

    /**
     * Adds the given message id to the SOAP header (Web Service Adressing). If no header is available yet it
     * will be created first.
     * 
     * @param envelopeDoc
     *        the envelope to add the created message id to.
     * @param messageId
     *        the new message id. If <code>null</code>, a randomly created message id will be added.
     */
    public static void addWsaMessageId(EnvelopeDocument envelopeDoc, String messageId) {
        Envelope envelope = envelopeDoc.getEnvelope();
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        MessageIDDocument messageIdDoc = MessageIDDocument.Factory.newInstance();
        String msgId = messageId == null ? generateSoapMessageId(null) : messageId;
        messageIdDoc.addNewMessageID().setStringValue(msgId);
        addToHeader(header, messageIdDoc.getMessageID().getDomNode());
    }

    /**
     * Creates a random UUID message id, optionally prefixed.
     * 
     * @param prefix
     *        a prefix to concatenate with the generated UUID.
     * @return the (prefixed) message id.
     */
    private static String generateSoapMessageId(String prefix) {
        prefix = prefix == null ? "" : prefix;
        return String.format("%s%s", prefix, UUID.randomUUID());
    }

    /**
     * Convenience method to add arbitrary nodes to a SOAP header.
     * 
     * @param header
     *        the header to add nodes to.
     * @param nodeToAdd
     *        the node to add.
     */
    private static void addToHeader(Header header, Node nodeToAdd) {
        if (nodeToAdd == null) {
            return;
        }
        Document ownerDocument = header.getDomNode().getOwnerDocument();
        Node importedNode = ownerDocument.importNode(nodeToAdd, true);
        header.getDomNode().appendChild(importedNode);
    }

    // public static void addWssAuthentication(EnvelopeDocument envelopeDocument, String username, String
    // password) throws WSSecurityException {
    // Envelope envelope = envelopeDocument.getEnvelope();
    // WSSecHeader secHeader = new WSSecHeader();
    // secHeader.insertSecurityHeader(envelope.getDomNode().getOwnerDocument());
    //
    // // create the user information
    // WSSecUsernameToken utBuilder = new WSSecUsernameToken();
    // utBuilder.setSecretKeyLength(40);
    // utBuilder.setUserInfo(username, password);
    //
    // // add the user information to the Security header of the SOAP message
    // utBuilder.build(envelope.getDomNode().getOwnerDocument(), secHeader);
    // }

}
