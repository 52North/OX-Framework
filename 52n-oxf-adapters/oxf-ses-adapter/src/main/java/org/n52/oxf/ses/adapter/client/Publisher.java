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
package org.n52.oxf.ses.adapter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher {

	private static final Logger LOG = LoggerFactory
			.getLogger(Publisher.class);

	private static final Object MANAGERS_MUTEX = new Object();

	private static final Map<String,ISESConnector> MANAGERS = new HashMap<>();

	private static final String SES_RESOURCES_NS = "http://ws.apache.org/muse/addressing";
	private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";
	private static final String WSBR_NS = "http://docs.oasis-open.org/wsn/br-2";
	private static final String WSNT_NS = "http://docs.oasis-open.org/wsn/b-2";

	private static final String RESOURCE_ID_XPATH = "declare namespace res='"+SES_RESOURCES_NS+"'; " +
		"//res:ResourceId";
	private static final String PUBLISHER_ADDRESS_XPATH = "declare namespace wsa='"+WSA_NS+"'; " +
			"declare namespace wsbr='"+WSBR_NS+"'; " +
			"//wsbr:PublisherRegistrationReference/wsa:Address";
	private static final String CONSUMER_ADDRESS_XPATH = "declare namespace wsa='"+WSA_NS+"'; " +
			"declare namespace wsnt='"+WSNT_NS+"'; " +
			"//wsnt:ConsumerReference/wsa:Address";

	private XmlObject document;
	private final XmlObject sensorML;
	private boolean failed;
	private String exceptionText;

	private String resourceID;

	private String publisherAddress;

	private String consumerAddress;

	private ISESConnector manager;

	private boolean destroyed;
    private static final String DEFAULT_ENCODING = "UTF-8";

	public Publisher(XmlObject sensorML) {
		this.sensorML = sensorML;
	}

	public XmlObject getRegisterDocument() {
		StringBuilder sb = new StringBuilder();
        try (
                InputStream in = getClass().getResourceAsStream("template_registerpublisher.xml");
                BufferedReader br = new BufferedReader(new InputStreamReader(in, DEFAULT_ENCODING));
                ) {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e) {
			LOG.warn(e.getMessage(), e);
		}

		try {
			document = XmlObject.Factory.parse(
					sb.toString().replace("${sensorml}", sensorML.toString()).
					replace("${topic}", "default"));
		} catch (XmlException e) {
			LOG.warn(e.getMessage(), e);
		}

		return this.document;
	}

	public void setException(Exception e) {
		failed = true;
		exceptionText = e.getMessage();
	}

	public void parseResponse(XmlObject response) {
		XmlObject[] body = XmlUtil.selectPath("declare namespace soap='http://www.w3.org/2003/05/soap-envelope'; //soap:Body", response);
		if (body == null || body.length == 0) {
            this.setException(new Exception("Could not parse response: no SOAP body found."));
            return;
        }

		XmlCursor cur = body[0].newCursor();
		cur.toFirstChild();
        switch (cur.getName().getLocalPart()) {
            case "RegisterPublisherResponse":
                /*
                * get the resourceID
                */
                XmlObject[] xo = XmlUtil.selectPath(RESOURCE_ID_XPATH, response);
                if (xo != null && xo.length > 0) {
                    resourceID = xo[0].newCursor().getTextValue().trim();
                }   /*
                * get the publishers address
                */
                xo = XmlUtil.selectPath(PUBLISHER_ADDRESS_XPATH, response);
                publisher:
                if (xo != null && xo.length > 0) {
                    publisherAddress = xo[0].newCursor().getTextValue().trim();
                    URL url = null;
                    try {
                        url = new URL(publisherAddress);
                    } catch (MalformedURLException e) {
                        LOG.error("Exception thrown:", e);
                    }

                    if (url == null) break publisher;

                    /*
                    * only create one manager instance per URL
                    */
                    synchronized (MANAGERS_MUTEX) {
                        if (MANAGERS.containsKey(url.toString())) {
                            this.manager = MANAGERS.get(url.toString());
                        }
                        else {
                            manager = SESClient.getNewConnectorInstance(url);
                            manager.setAddSoap(false);
                            MANAGERS.put(url.toString(), manager);
                        }
                    }
                }   /*
                * get the consumer address
                */
                xo = XmlUtil.selectPath(CONSUMER_ADDRESS_XPATH, response);
                if (xo != null && xo.length > 0) {
                    consumerAddress = xo[0].newCursor().getTextValue().trim();
                }   break;
            case "DestroyRegistrationResponse":
                destroyed = true;
                break;
            default:
                failed = true;
                break;
        }
	}

	public XmlObject getDestroyRegistrationDocument() {
		XmlObject xo = null;

		StringBuilder sb = new StringBuilder();
        try (
                InputStream in = getClass().getResourceAsStream("template_destroyregistration.xml");
                BufferedReader br = new BufferedReader(new InputStreamReader(in, DEFAULT_ENCODING));
        ){
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e1) {
			LOG.error("Exception thrown:", e1);
		}

		try {
			xo = XmlObject.Factory.parse(
                    sb.toString().replace("${resource}", this.resourceID)
                    .replace("${reg_pub_host}", this.manager.getHost().toString()));
		} catch (XmlException e) {
			LOG.error("Exception thrown:", e);
		}

		return xo;
	}

	public boolean isFailed() {
		return failed;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public String getExceptionText() {
		return exceptionText;
	}

	public String getResourceID() {
		return resourceID;
	}

	public String getPublisherAddress() {
		return publisherAddress;
	}

	public String getConsumerAddress() {
		return consumerAddress;
	}

	public ISESConnector getManager() {
		return manager;
	}
}
