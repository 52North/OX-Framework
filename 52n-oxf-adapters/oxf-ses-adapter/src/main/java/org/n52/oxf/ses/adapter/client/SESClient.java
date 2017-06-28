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


import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.opengis.ses.x00.CapabilitiesDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.ses.adapter.client.ISESConnector.SESResponse;


public class SESClient {
	
	
	private static final Log logger = LogFactory
			.getLog(SESClient.class);
	private static Map<URL, SESClient> _instances = new HashMap<URL, SESClient>();
	private URL broker;
	private Map<String,Subscription> subscriptions = new HashMap<String, Subscription>();
	private ISESConnector brokerConnector;
	
	private static Class<?> connectorImpl = SESConnectorImpl.class;
	private static ISESConnector connectorImplGetInst;
	
	static {
		try {
			connectorImplGetInst = (ISESConnector) connectorImpl.newInstance();
		} catch (InstantiationException e) {
			logger.warn(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.warn(e.getMessage(), e);
		}
	}
	
	
	private static final String NOTIFY_ACTION = "http://docs.oasis-open.org/wsn/bw-2/NotificationConsumer/Notify";
	private static final String SUBSCRIBE_ACTION = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";
	private static final String UNSUBSCRIBE_ACTION = "http://docs.oasis-open.org/wsn/bw-2/SubscriptionManager/UnsubscribeRequest";
	private static final String REGISTER_PUBLISHER_ACTION = "http://docs.oasis-open.org/wsn/brw-2/RegisterPublisher/RegisterPublisherRequest";
	@SuppressWarnings("unused")
	private static final String PAUSE_SUBSCRIBE_ACTION = "";

	public static final String SES_PORT_TYPE = "SesPortType";
	public static final String SUB_MGR_PORT_TYPE = "SubscriptionManagerContextPath";
	public static final String PRM_PORT_TYPE = "SesPRMContextPath";

	/**
	 * Use this method before getting an instance with {@link SESClient#getInstance(URL, String, String, String)}.
	 * The defined class will then be used as the connector implementation, replacing the built-in one.
	 * 
	 * If the class is not an impl of {@link ISESConnector} the method will have no effect.
	 * 
	 * @param conn {@link ISESConnector} implementation.
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void setConnectorImplementation(Class<?> conn) throws InstantiationException, IllegalAccessException {
		if (ISESConnector.class.isAssignableFrom(conn)) {
			connectorImpl = conn;
			connectorImplGetInst = (ISESConnector) conn.newInstance();
		}
	}
	
	private SESClient(URL url) {
		this.broker = url;
		
		try {
			this.brokerConnector = (ISESConnector) connectorImpl.newInstance();
			this.brokerConnector.setHost(this.broker);
			this.brokerConnector.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 *
	 * INTERFACE METHODS
	 * 
	 */
	
	public Subscription subscribe(SubscriptionConstraints con) {
		Subscription sub = new Subscription(con);
		SESResponse resp = null;
		try {
			resp = this.brokerConnector.sendHttpPostRequest(
					con.getSubscriptionDocument().toString(), SUBSCRIBE_ACTION);
		} catch (Exception e) {
			sub.setException(e);
		}
		
		if (resp != null) {
			sub.parseResponse(resp.getResponseBody());
		}
		
		return sub;
	}
	
	public boolean pauseSubscription(Subscription sub) {
		return false;
	}
	
	public void unsubscribe(Subscription sub) {
		SESResponse resp = null;
		try {
			resp = sub.getManager().sendHttpPostRequest(
					sub.getUnSubscribeDocument().toString(), UNSUBSCRIBE_ACTION);
		} catch (Exception e) {
			sub.setException(e);
		}
		
		if (resp != null) {
			sub.parseResponse(resp.getResponseBody());
		}
	}
	
	public int notify(XmlObject notification) {
		return notify(notification.toString());
	}
	
	public int notify(String notification) {
		SESResponse resp = null;
		try {
			resp = this.brokerConnector.sendHttpPostRequest(
					notification, NOTIFY_ACTION);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return 500;
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Response: "+resp);
		}
		
		return resp.getStatusCode();
	}
	
	public Publisher registerPublisher(XmlObject sensorML) {
		Publisher pub = new Publisher(sensorML);
		SESResponse resp = null;
		try {
			resp = this.brokerConnector.sendHttpPostRequest(
					pub.getRegisterDocument().toString(), REGISTER_PUBLISHER_ACTION);
		} catch (Exception e) {
			pub.setException(e);
		}
		
		if (resp != null) {
			pub.parseResponse(resp.getResponseBody());
		}
		
		return pub;
	}
	
	public void destroyRegistration(Publisher pub) {
		SESResponse resp = null;
		try {
			resp = pub.getManager().sendHttpPostRequest(
					pub.getDestroyRegistrationDocument().toString(), UNSUBSCRIBE_ACTION);
		} catch (Exception e) {
			pub.setException(e);
		}
		
		if (resp != null) {
			pub.parseResponse(resp.getResponseBody());
		}
	}
	
	
	public static SESResponse sendHttpGetRequest(URI uri) throws Exception {
		if (uri.getScheme().equals("file")) {
			return new SESResponse(200, XmlObject.Factory.parse(new FileInputStream(new File(uri))));
		}
		return connectorImplGetInst.sendHttpGetRequest(uri);
	}
	
	public CapabilitiesDocument getCapabilities() {
		return null;
	}
	
	
	
	/*
	 * 
	 * MANAGEMENT METHODS
	 * 
	 */
	
	public Collection<Subscription> getCurrentSubscriptions() {
		return this.subscriptions.values();
	}
	
	public Subscription getSubscriptionByID(String id) {
		return this.subscriptions.get(id);
	}
	
	
	public static synchronized SESClient getInstance(URL url) {
		if (!_instances.containsKey(url)) {
			_instances.put(url, new SESClient(url));
		}
		return _instances.get(url);
	}

	public static ISESConnector getNewConnectorInstance(URL url) {
		ISESConnector inst;
		try {
			inst = (ISESConnector) connectorImpl.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		
		inst.setHost(url);
		inst.initialize();
		
		return inst;
	}
	

	public String getURL() {
		return this.broker.toString();
	}

	public static void shutdown() {
		for (SESClient inst : _instances.values()) {
			inst.freeResources();
		}
	}
	

	private void freeResources() {
		this.brokerConnector.shutdown();
		for (Subscription sub : this.subscriptions.values()) {
			sub.shutdown();
		}
	}

	public static void main(String[] args) throws MalformedURLException {
		SESClient c = new SESClient(new URL("http://localhost:8080/52n-ses-core-1.0-SNAPSHOT/services/"+SES_PORT_TYPE));
		Subscription sub = c.subscribe(new SubscriptionConstraints.DynamicFilterSubscription("dev.null"));
		System.out.println(sub.isDestroyed());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.unsubscribe(sub);
		System.out.println(sub.isDestroyed());
	}


}
