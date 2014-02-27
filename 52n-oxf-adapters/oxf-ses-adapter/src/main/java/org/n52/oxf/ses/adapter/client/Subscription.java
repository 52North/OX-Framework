/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.ses.adapter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.oxf.ses.adapter.client.ISESConnector.SESResponse;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscription {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Subscription.class);
	
	private ISESConnector manager;
	private String resourceID;
	private DateTime terminationTime;
	private SubscriptionConstraints constraints;
	private boolean failed;
	private String exceptionText;
	private String wsdlUrl;
	private boolean destroyed;

	private ResourceIdInstance resourceIdInstance;

	private static List<ResourceIdInstance> resourceIdInstances = new ArrayList<ResourceIdInstance>(); 
	
	private static String SES_RESOURCES_NS = "http://ws.apache.org/muse/addressing";
	private static String WSA_NS = "http://www.w3.org/2005/08/addressing";
	private static String WSDL_INSTANCE_NS = "http://www.w3.org/ns/wsdl-instance";
	private static QName WSDL_LOCATION_QN = new QName(WSDL_INSTANCE_NS, "wsdlLocation");
	private static final String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";
	private static final String WSDL_SOAP12_NS = "http://schemas.xmlsoap.org/wsdl/soap12/";
	private static final QName WSDL_SOAP_LOCATION_QN = new QName("location");
	
	private static Map<String, ISESConnector> MANAGERS = new HashMap<String, ISESConnector>();
	private static final Object MANAGERS_MUTEX = new Object();
	
	private static String MANAGER_WSDL_XPATH = "declare namespace wsa='"+WSA_NS+"'; " +
			"declare namespace muse-wsa='"+SES_RESOURCES_NS+"'; " +
			"declare namespace wsdl-inst='"+WSDL_INSTANCE_NS+"'; " +
			"//wsa:Metadata[@wsdl-inst:"+ WSDL_LOCATION_QN.getLocalPart() +"]";
	private static String MANAGER_XPATH = "declare namespace wsdl='"+WSDL_NS+"'; " +
		"declare namespace wsdl-soap12='"+WSDL_SOAP12_NS+"'; " +
		"//wsdl:service/wsdl:port/wsdl-soap12:address[@location]";
	
	static {
		resourceIdInstances.add(new ResourceIdInstance("http://ws.apache.org/muse/addressing", "ResourceId"));
		resourceIdInstances.add(new ResourceIdInstance("http://www.ids-spa.it/", "ResourceId"));
	}
	
	
	public Subscription(SubscriptionConstraints con) {
		this.constraints = con;
	}
	
	public void parseResponse(XmlObject response) {
		if (response == null) {
			this.failed = true;
			this.exceptionText = "No response received.";
			return;
		}
		/*
		 * SubscribeResponse
		 */
		XmlObject[] body = XmlUtil.selectPath("declare namespace soap='http://www.w3.org/2003/05/soap-envelope'; //soap:Body", response);
		if (body == null || body.length == 0) this.setException(new Exception("Could not parse response: no SOAP body found."));
		
		XmlCursor cur = body[0].newCursor();
		cur.toFirstChild();
		if (cur.getName().getLocalPart().equals("SubscribeResponse")) {
			XmlObject[] wsdlLocation = XmlUtil.selectPath(MANAGER_WSDL_XPATH, response);
			
			/*
			 * get the WSDL definition
			 */
			if (wsdlLocation != null && wsdlLocation.length > 0) {
				this.wsdlUrl = wsdlLocation[0].newCursor().getAttributeText(WSDL_LOCATION_QN).split(" ")[1];
				
				SESResponse wsdl = null; 
				try {
					wsdl = SESClient.sendHttpGetRequest(new URI(this.wsdlUrl));
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
				
				/*
				 * get the managers URL from the wsdl
				 */
				if (wsdl != null) {
					XmlObject[] managerLocation = XmlUtil.selectPath(MANAGER_XPATH, wsdl.getResponseBody());
					if (managerLocation != null && managerLocation.length > 0) {
						try {
							URL url = new URL(managerLocation[0].newCursor().getAttributeText(WSDL_SOAP_LOCATION_QN));
							
							/*
							 * only create one manager instance per URL
							 */
							synchronized (MANAGERS_MUTEX) {
								if (MANAGERS.containsKey(url.toString())) {
									this.manager = MANAGERS.get(url.toString());
								}
								else {
									this.manager = SESClient.getNewConnectorInstance(new URL(managerLocation[0].newCursor().getAttributeText(WSDL_SOAP_LOCATION_QN)));
									this.manager.setAddSoap(false);
									MANAGERS.put(url.toString(), this.manager);
								}
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/*
			 * get the resourceID
			 */
			this.resourceID = resolveSubscriptionResource(response);
		}
		/*
		 * UnsubscribeResponse
		 */
		else if (cur.getName().getLocalPart().equals("DestroyResponse") || cur.getName().getLocalPart().equals("UnsubscribeResponse")) {
			this.destroyed = true;
		}
		
		else {
			this.failed = true;
		}
		
	}
	

	private String resolveSubscriptionResource(XmlObject response) {
		for (ResourceIdInstance rid : resourceIdInstances) {
			XmlObject[] resourceObj = XmlUtil.selectPath(rid.getXPathExpression(), response);
			if (resourceObj != null && resourceObj.length > 0) {
				this.resourceIdInstance = rid;
				return resourceObj[0].newCursor().getTextValue().trim();
			}
		}
		return null;
	}

	public XmlObject getUnSubscribeDocument() {
		XmlObject xo = null;
		
		StringBuilder sb = new StringBuilder();
		InputStream in = getClass().getResourceAsStream("template_unsubscribe.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			xo = XmlObject.Factory.parse(sb.toString().replace("${resource}", this.resourceID).
					replace("${ses_sub_host}", this.manager.getHost().toString()).replace("${resourceIdNamespace}",
							this.resourceIdInstance.getNamespace()));
		} catch (XmlException e) {
			e.printStackTrace();
		}
		
		return xo;
	}
	
	/**
	 * @return the manager instance instantiated dynamically from the SubscribeResponses wsdl
	 */
	public ISESConnector getManager() {
		return manager;
	}
	
	public String getResourceID() {
		return resourceID;
	}
	
	
	public ResourceIdInstance getResourceIdInstance() {
		return resourceIdInstance;
	}

	public DateTime getTerminationTime() {
		return terminationTime;
	}
	
	public void setException(Exception e) {
		this.failed = true;
		this.exceptionText = e.getMessage();
	}

	public boolean isFailed() {
		return failed;
	}

	public String getExceptionText() {
		return exceptionText;
	}

	public SubscriptionConstraints getConstraints() {
		return constraints;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void shutdown() {
		
	}

	

}
