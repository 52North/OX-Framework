/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

public class SESUtils {

    /**
     * Namespaces are taken from public SES wsdl file.
     */
    public enum SesNamespace {
        /**
         * Official SES namespace used in OGC 11-088r1.<br>
         * <br>
         * <b>prefix:</b>  {@link SesNamespace#SES} <code>ses</code><br>
         * <b>namespace:</b> <code>http://www.opengis.net/ses/0.0</code>
         */
        SES("ses", "http://www.opengis.net/ses/0.0"), 
        /**
         * <b>prefix:</b> <code>wsa</code><br>
         * <b>namespace:</b> <code>http://www.w3.org/2005/08/addressing</code>
         */
        WSA("wsa", "http://www.w3.org/2005/08/addressing"),
        /**
         * <b>prefix:</b> <code>essf</code><br>
         * <b>namespace:</b> <code>http://www.opengis.net/es-sf/0.0</code>
         */
        ESSF("essf", "http://www.opengis.net/es-sf/0.0"),
        /**
         * <b>prefix:</b> <code>wsdl</code><br>
         * <b>namespace:</b> <code>http://schemas.xmlsoap.org/wsdl/</code>
         */
        WSDL("wsdl", "http://schemas.xmlsoap.org/wsdl/"),
        /**
         * <b>prefix:</b> <code>wsdl-soap12</code><br>
         * <b>namespace:</b> <code>http://schemas.xmlsoap.org/wsdl/soap12/</code>
         */
        WSDL_SOAP12("wsdl-soap12", "http://schemas.xmlsoap.org/wsdl/soap12/"),
        /**
         * <b>prefix:</b> <code>s</code><br>
         * <b>namespace:</b> <code>http://www.w3.org/2001/XMLSchema</code>
         */
        XSD("xsd", "http://www.w3.org/2001/XMLSchema"),
        /**
         * <b>prefix:</b> <code>wsx</code><br>
         * <b>namespace:</b> <code>http://schemas.xmlsoap.org/ws/2004/09/mex</code>
         */
        WSX("wsx", "http://schemas.xmlsoap.org/ws/2004/09/mex"),
        /**
         * <b>prefix:</b> <code>wsrf-r</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsrf/r-2</code>
         */
        WSRF_R("wsrf-r", "http://docs.oasis-open.org/wsrf/r-2"),
        /**
         * <b>prefix:</b> <code>wsrf-rl</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsrf/rl-2</code>
         */
        WSRF_RL("wsrf-rl", "http://docs.oasis-open.org/wsrf/rl-2"),
        /**
         * <b>prefix:</b> <code>wsrf-bf</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsrf/bf-2</code>
         */
        WSRF_BF("wsrf-bf", "http://docs.oasis-open.org/wsrf/bf-2"),
        /**
         * <b>prefix:</b> <code>wsrf-rp</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsrf/rp-2</code>
         */
        WSRF_RP("wsrf-rp", "http://docs.oasis-open.org/wsrf/rp-2"),
        /**
         * <b>prefix:</b> <code>wsrmd</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsrf/rmd-1</code>
         */
        WSRMD("wsrmd", "http://docs.oasis-open.org/wsrf/rmd-1"),
        /**
         * <b>prefix:</b> <code>wsn-b</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsn/b-2</code>
         */
        WSN_B("wsn-b", "http://docs.oasis-open.org/wsn/b-2"),
        /**
         * <b>prefix:</b> <code>wsn-br</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsn/br-2</code>
         */
        WSN_BR("wsn-br", "http://docs.oasis-open.org/wsn/br-2"),
        /**
         * <b>prefix:</b> <code>wsntw</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsn/bw-2</code>
         */
        WSNTW("wsntw", "http://docs.oasis-open.org/wsn/bw-2"),
        /**
         * <b>prefix:</b> <code>wsn-t</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsn/t-1</code>
         */
        WSN_T("wsn-t", "http://docs.oasis-open.org/wsn/t-1"),
        /**
         * <b>prefix:</b> <code>muws1</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsdm/muws1-2.xsd</code>
         */
        MUWS1("muws1", "http://docs.oasis-open.org/wsdm/muws1-2.xsd"),
        /**
         * <b>prefix:</b> <code>muws2</code><br>
         * <b>namespace:</b> <code>http://docs.oasis-open.org/wsdm/muws2-2.xsd</code>
         */
        MUWS2("muws2", "http://docs.oasis-open.org/wsdm/muws2-2.xsd"),
        /**
         * <b>prefix:</b> <code>sml</code><br>
         * <b>namespace:</b> <code>http://www.opengis.net/sensorML/1.0.1</code>
         */
        SML("sml", "http://www.opengis.net/sensorML/1.0.1"),
        /**
         * <b>prefix:</b> <code>ows</code><br>
         * <b>namespace:</b> <code>http://www.opengis.net/ows/1.1</code>
         */
        OWS("ows", "http://www.opengis.net/ows/1.1"),
        /**
         * <b>prefix:</b> <code>xlink</code><br>
         * <b>namespace:</b> <code>http://www.w3.org/1999/xlink</code>
         */
        XLINK("xlink", "http://www.w3.org/1999/xlink"),
        /**
         * <b>prefix:</b> <code>SOAP-ENV</code><br>
         * <b>namespace:</b> <code>http://www.w3.org/2003/05/soap-envelope</code>
         */
        SOAP_ENV("SOAP-ENV", "http://www.w3.org/2003/05/soap-envelope"),
        /**
         * <b>prefix:</b> <code>xsi</code><br>
         * <b>namespace:</b> <code>http://www.w3.org/2001/XMLSchema-instance</code>
         */
        XSI("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        private String prefix;
        private String namespace;
        
        private SesNamespace(String prefix, String namespace) {
            this.prefix = prefix;
            this.namespace = namespace;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getNamespace() {
            return namespace;
        }
        
        public QName createQNameFor(String localPart) {
            return new QName(namespace, localPart, prefix);
        }
    }

    /**
     * Creates a qualified element for given elementName with prefix known by {@link #SES_NAMESPACES}.
     * 
     * @param elementName
     *        the element name to get a qualified name for.
     * @param namespace
     *        the prefix of a known (#SES_NAMESPACES) namespace.
     * @return a qualified name for given element name and known prefix.
     */
    public static QName getQElementFor(String elementName, SesNamespace namespace) {
        return namespace.createQNameFor(elementName);
    }

    /**
     * Adds a set of namespaces to the SOAP-Envelope document.
     * 
     * @param envelope
     *        the {@link EnvelopeDocument} to which the namespace set is added
     */
    public static void addNamespacesToEnvelope_000(Envelope envelope) {
        XmlCursor obsCursor = envelope.newCursor();
        obsCursor.toFirstContentToken();
        for (SesNamespace namespace : SesNamespace.values()) {
            obsCursor.insertNamespace(namespace.getPrefix(), namespace.getNamespace());
        }

        // these are old
        // obsCursor.insertNamespace("wsbn","http://docs.oasis-open.org/wsn/br-2");
        // obsCursor.insertNamespace("wsnt", "http://docs.oasis-open.org/wsn/b-2");
        // obsCursor.insertNamespace("wsrf","http://docs.oasis-open.org/wsrf/rl-2");
    }
}
