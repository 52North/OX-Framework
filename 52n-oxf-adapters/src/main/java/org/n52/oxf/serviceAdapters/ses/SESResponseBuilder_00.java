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
package org.n52.oxf.serviceAdapters.ses;

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Header;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 03.08.2009
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
