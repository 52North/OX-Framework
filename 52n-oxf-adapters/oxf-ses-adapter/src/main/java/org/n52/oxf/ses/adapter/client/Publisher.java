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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Publisher.class);

	private static final Object MANAGERS_MUTEX = new Object();

	private static Map<String,ISESConnector> MANAGERS = new HashMap<String, ISESConnector>();
	
	private static String SES_RESOURCES_NS = "http://ws.apache.org/muse/addressing";
	private static String WSA_NS = "http://www.w3.org/2005/08/addressing";
	private static String WSBR_NS = "http://docs.oasis-open.org/wsn/br-2";
	private static String WSNT_NS = "http://docs.oasis-open.org/wsn/b-2";
	
	private static String RESOURCE_ID_XPATH = "declare namespace res='"+SES_RESOURCES_NS+"'; " +
		"//res:ResourceId";
	private static String PUBLISHER_ADDRESS_XPATH = "declare namespace wsa='"+WSA_NS+"'; " +
			"declare namespace wsbr='"+WSBR_NS+"'; " +
			"//wsbr:PublisherRegistrationReference/wsa:Address";
	private static String CONSUMER_ADDRESS_XPATH = "declare namespace wsa='"+WSA_NS+"'; " +
			"declare namespace wsnt='"+WSNT_NS+"'; " +
			"//wsnt:ConsumerReference/wsa:Address";
	
	
	private XmlObject document;
	private XmlObject sensorML;
	private boolean failed;
	private String exceptionText;

	private String resourceID;

	private String publisherAddress;

	private String consumerAddress;

	private ISESConnector manager;

	private boolean destroyed;

	public Publisher(XmlObject sensorML) {
		this.sensorML = sensorML;
	}

	public XmlObject getRegisterDocument() {
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("template_registerpublisher.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		try {
			this.document = XmlObject.Factory.parse(
					sb.toString().replace("${sensorml}", this.sensorML.toString()).
					replace("${topic}", "default"));
		} catch (XmlException e) {
			logger.warn(e.getMessage(), e);
		}
		
		return this.document;
	}

	public void setException(Exception e) {
		this.failed = true;
		this.exceptionText = e.getMessage();
	}

	public void parseResponse(XmlObject response) {
		XmlObject[] body = response.selectPath("declare namespace soap='http://www.w3.org/2003/05/soap-envelope'; //soap:Body");
		if (body == null || body.length == 0) this.setException(new Exception("Could not parse response: no SOAP body found."));
		
		XmlCursor cur = body[0].newCursor();
		cur.toFirstChild();
		if (cur.getName().getLocalPart().equals("RegisterPublisherResponse")) {
			/*
			 * get the resourceID
			 */
			XmlObject[] xo = response.selectPath(RESOURCE_ID_XPATH);
			if (xo != null && xo.length > 0) {
				this.resourceID = xo[0].newCursor().getTextValue();
			}
			
			/*
			 * get the publishers address
			 */
			xo = response.selectPath(PUBLISHER_ADDRESS_XPATH);
			publisher:
			if (xo != null && xo.length > 0) {
				this.publisherAddress = xo[0].newCursor().getTextValue();
				URL url = null;
				try {
					url = new URL(this.publisherAddress);
				} catch (MalformedURLException e) {
					e.printStackTrace();
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
						this.manager = SESClient.getNewConnectorInstance(url);
						this.manager.setAddSoap(false);
						MANAGERS.put(url.toString(), this.manager);
					}
				}
			}
			
			/*
			 * get the consumer address
			 */
			xo = response.selectPath(CONSUMER_ADDRESS_XPATH);
			if (xo != null && xo.length > 0) {
				this.consumerAddress = xo[0].newCursor().getTextValue();
			}
		} else if (cur.getName().getLocalPart().equals("DestroyRegistrationResponse")){
			this.destroyed = true;
		} else {
			this.failed = true;
		}
	}
	
	public XmlObject getDestroyRegistrationDocument() {
		XmlObject xo = null;
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("template_destroyregistration.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			xo = XmlObject.Factory.parse(sb.toString().replace("${resource}", this.resourceID).replace("${reg_pub_host}", this.manager.getHost().toString()));
		} catch (XmlException e) {
			e.printStackTrace();
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
