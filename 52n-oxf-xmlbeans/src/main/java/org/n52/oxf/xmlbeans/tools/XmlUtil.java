/**
 * Copyright (C) 2012
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

import java.util.HashMap;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
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

public class XmlUtil {

    public static XmlOptions xmlFormat = XmlSetup.doXmlSetup();

    private static final class XmlSetup {
        static XmlOptions doXmlSetup() {
            // XMLBeansParser.registerLaxValidationCase(new SpsOfferingLaxValidationCase());
            // XMLBeansParser.registerLaxValidationCase(new SosOfferingLaxValidationCase());

            HashMap<String, String> namespaces = new HashMap<String, String>();
            namespaces.put("http://www.opengis.net/sos/2.0", "sos");
            namespaces.put("http://www.opengis.net/sps/2.0", "sps");
            namespaces.put("http://www.opengis.net/swes/2.0", "swes");
            namespaces.put("http://www.opengis.net/swe/2.0", "swe20");
            namespaces.put("http://www.opengis.net/swe/1.0.1", "swe101");
            namespaces.put("http://www.opengis.net/sensorML/1.0.1", "sml");
            namespaces.put("http://www.opengis.net/gml/3.2", "gml32");
            namespaces.put("http://www.opengis.net/gml", "gml311");
            namespaces.put("http://www.opengis.net/om/2.0", "om");
            
            namespaces.put("http://www.w3.org/1999/xlink", "xlink");
            namespaces.put("http://www.w3.org/2005/08/addressing", "wsa");
            namespaces.put("http://www.w3.org/2003/05/soap-envelope", "soap12");

            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(2);
            options.setSaveAggressiveNamespaces();
            options.setSaveSuggestedPrefixes(namespaces);
            return options;
        }
    }

    // /**
    // * Creates an exception report from the {@link Throwable}'s stacktrace.<br>
    // * <br/>
    // * An example looks like the following.
    // *
    // * <pre>
    // * {@code
    // * <Exception>
    // * <message>exMessage</message>
    // * <stackTrace>
    // * [EXC] "Stacktrace element 1"
    // * [EXC] "Stacktrace element 2"
    // * [EXC] "Stacktrace element 3"
    // * <!-- ... -->
    // * </stackTrace>
    // * </Exception>
    // * }
    // * </pre>
    // *
    // *
    // * @param t
    // * the {@link Throwable} occured
    // * @return and OWS 1.1 ExceptionReport containing the {@link Throwable}'s stacktrace information
    // * @throws XmlException
    // * if parsing to {@link XmlObject} is unsuccessful
    // */
    // public static XmlObject createXmlExceptionResponse(Throwable t) throws XmlException {
    //
    // // TODO wrap exception report with appropriate SOAP header?!
    //
    // ExceptionReportDocument exceptionReportDoc = ExceptionReportDocument.Factory.newInstance();
    // ExceptionReport exceptionReport = exceptionReportDoc.addNewExceptionReport();
    // ExceptionType exception = exceptionReport.addNewException();
    // exception.setLocator("NoApplicableCode");
    // exception.addExceptionText(t.getMessage());
    // // if (t.getStackTrace().length > 0) {
    // // for (StackTraceElement e : t.getStackTrace()) {
    // // exception.addExceptionText(e.toString());
    // // }
    // // }
    // return exceptionReportDoc;
    //
    // /*
    // StringBuilder sb = new StringBuilder();
    // sb.append("<Exception>");
    // sb.append("<message>").append(t.getMessage()).append("</message>");
    // if (t.getStackTrace().length > 0) {
    // sb.append("<stackTrace>");
    // String format = "\n\t[EXC] %s";
    // for (StackTraceElement e : t.getStackTrace()) {
    // sb.append(String.format(format, e.toString()));
    // }
    // sb.append("</stackTrace>");
    // }
    // sb.append("</Exception>");
    // return XmlObject.Factory.parse(sb.toString());
    // */
    // }

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
            return XMLBeansTools.getXmlAnyNodeFrom(soapBody, nodeName);
        }
        catch (XmlException e) {
            throw new IllegalArgumentException("Cannot parse from envelope");
        }
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
        if ( !envelope.isSetHeader()) {
            envelope.addNewHeader();
        }
        Header header = envelope.getHeader();
        ToDocument toDoc = ToDocument.Factory.newInstance();
        toDoc.addNewTo().setStringValue(recipient);
        addToHeader(header, toDoc.getTo().getDomNode());
    }

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

    public static void addNewWsaMessageId(EnvelopeDocument envelopeDoc) {
        addWsaMessageId(envelopeDoc, null);
    }
    
    public static void addNewWsaMessageIdWithPrefix(EnvelopeDocument envelopeDoc, String prefix) {
        addWsaMessageId(envelopeDoc, generateSoapMessageId(prefix));
    }

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

    private static String generateSoapMessageId(String prefix) {
        prefix = prefix == null ? "" : prefix;
        return String.format("%s%s", prefix, UUID.randomUUID());
    }

    private static void addToHeader(Header header, Node nodeToAdd) {
        Document ownerDocument = header.getDomNode().getOwnerDocument();
        Node importedNode = ownerDocument.importNode(nodeToAdd, true);
        header.getDomNode().appendChild(importedNode);
    }

//    public static void addWssAuthentication(EnvelopeDocument envelopeDocument, String username, String password) throws WSSecurityException {
//        Envelope envelope = envelopeDocument.getEnvelope();
//        WSSecHeader secHeader = new WSSecHeader();
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

//    public static boolean validateSosCapabilities(net.opengis.sos.x20.CapabilitiesDocument xml) throws InvalidXmlException {
//       /*
//        * Requires a LaxValidationCase!!
//        */
//        try {
//            if (xml.getCapabilities().isSetContents()) {
//                Offering[] offeringArray = xml.getCapabilities().getContents().getContents().getOfferingArray();
//                for (Offering offering : offeringArray) {
//                    InputStream is = offering.newInputStream();
//                    ObservationOfferingDocument observationOfferingDoc = ObservationOfferingDocument.Factory.parse(is);
//                    return validateXml(observationOfferingDoc) && validateXml(xml);
//                }
//            }
//            return false;
//        }
//        catch (XmlException e) {
//            throw new InvalidXmlException("Could not parse ObservationOffering.", e);
//        }
//        catch (IOException e) {
//            throw new InvalidXmlException("Could not read ObservationOffering.", e);
//        }
//    }

//    public static boolean validateSpsCapabilities(net.opengis.sps.x20.CapabilitiesDocument xml) throws InvalidXmlException {
//        /*
//         * Requires a LaxValidationCase!!
//         */
//        try {
//            if (xml.getCapabilities().isSetContents()) {
//                Offering[] offeringArray = xml.getCapabilities().getContents().getSPSContents().getOfferingArray();
//                for (Offering offering : offeringArray) {
//                    InputStream is = offering.newInputStream();
//                    SensorOfferingDocument sensorOfferingDoc = SensorOfferingDocument.Factory.parse(is);
//                    return validateXml(sensorOfferingDoc) && validateXml(xml);
//                }
//            }
//            return false;
//        }
//        catch (XmlException e) {
//            throw new InvalidXmlException("Could not parse SensorOffering.", e);
//        }
//        catch (IOException e) {
//            throw new InvalidXmlException("Could not read SensorOffering.", e);
//        }
//    }

//    /**
//     * Validates the given XML and throws an exception if validation fails. 
//     * 
//     * @param xml
//     * @return
//     * @throws InvalidXmlException
//     */
//    public static boolean validateXml(XmlObject xml) throws InvalidXmlException {
//        Collection<XmlError> errors = XMLBeansParser.validate(xml);
//        if ( !errors.isEmpty()) {
//            StringBuilder sb = new StringBuilder("Invalid XML instance:");
//            for (XmlError xmlError : errors) {
//                sb.append("\n[xmlError] ").append(xmlError.toString());
//            }
//            throw new InvalidXmlException(sb.toString());
//        }
//        return true;
//    }

}
