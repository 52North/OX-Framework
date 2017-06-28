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
package org.n52.oxf.ses.adapter;

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.adapter.ParameterContainer;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 03.08.2009
 * @deprecated where should this be used?!
 */
public class SESResponseBuilder_00 implements ISESResponseBuilder {

    public String buildNotifyResponseRequest(ParameterContainer parameter) {
        
        EnvelopeDocument envDoc = EnvelopeDocument.Factory.newInstance();
        Envelope env = envDoc.addNewEnvelope();
        Header header = env.addNewHeader();
        Body body = env.addNewBody();
        XmlCursor cur = null;
        String to = (String) parameter.getParameterShellWithCommonName(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_TO).getSpecifiedValue();
        String relatesTo = (String) parameter.getParameterShellWithCommonName(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_RELATES_TO).getSpecifiedValue();
        String from = (String) parameter.getParameterShellWithCommonName(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_FROM).getSpecifiedValue();
        // if the message id is not defined, we generated a random one
        String msgID = parameter.getParameterShellWithCommonName(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_FROM)==null?
                UUID.randomUUID().toString():
                    (String) parameter.getParameterShellWithCommonName(ISESResponseBuilder.NOTIFY_SOAP_ENVELOPE_HEADER_MESSAGE_ID).getSpecifiedValue();
        String action = "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/NotifyResponse";       
        
        
        
        SESUtils.addNamespacesToEnvelope_000(env);
        
        /*
         * Header 
         * <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
         *      <wsa:To>
         *              <wsa:Address>http://www.w3.org/2005/08/addressing/role/anonymous</wsa:Address>
         *      </wsa:To>
         *      <wsa:Action>http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/NotifyResponse</wsa:Action>
         *      <wsa:MessageID>uuid:d5136f51-c78d-1d91-facc-40a2896d963c</wsa:MessageID>
         *      <wsa:RelatesTo RelationshipType="wsa:Reply">uuid:1b4d3025-f80a-a5b6-aa37-864c47fa1a7e</wsa:RelatesTo>
         *      <wsa:From>
         *              <wsa:Address>http://localhost:8080/sas-muse-3.0-SNAPSHOT/services/SesPortType</wsa:Address>
         *      </wsa:From>
         * </soap:Header>
         */
        cur = header.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName("http://www.w3.org/2005/08/addressing","To","wsa"));
        cur.insertElementWithText(new QName("http://www.w3.org/2005/08/addressing","Adress","wsa"), to);
        cur.toNextToken();
        
        cur.beginElement(new QName("http://www.w3.org/2005/08/addressing","From","wsa"));
        cur.insertElementWithText(new QName("http://www.w3.org/2005/08/addressing","Adress","wsa"), from);
        cur.toNextToken();
        
        cur.insertElementWithText(new QName("http://www.w3.org/2005/08/addressing","MessageID","wsa"), msgID);
       
        cur.beginElement(new QName("http://www.w3.org/2005/08/addressing","RelatesTo","wsa"));
        cur.insertAttributeWithValue("Relationshiptype","wsa:Reply");
        cur.insertChars(relatesTo);
        cur.toNextToken();
        
        cur.insertElementWithText(new QName("http://www.w3.org/2005/08/addressing","Action","wsa"), action);
        cur.dispose();
        
        /*
         *  Body
         * <soap:Body xmlns:wsnt="http://docs.oasis-open.org/wsn/b-2">
         *      <wsnt:NotifyResponse/>
         * </soap:Body>
         */
        cur = body.newCursor();
        cur.toFirstContentToken();
        cur.beginElement(new QName("http://docs.oasis-open.org/wsn/b-2","NotifyResponse","wsnt"));
        cur.dispose();
        
        return envDoc.toString();
    }

}
