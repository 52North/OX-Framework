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
package org.n52.oxf.ses.adapter.client.httplistener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.oxf.ses.adapter.client.httplistener.HttpListener;

/**
 * Specify the use of this servlet
 * if you use the client within a servlet 2.x container (jetty, tomcat, etc).
 * 
 * @author matthes rieke
 *
 */
public class SimpleConsumerServlet extends HttpServlet implements IWSNConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final List<CallbackOnAvailableListener> _callbackOnAvailableList = new ArrayList<CallbackOnAvailableListener>();
	private static final Object MUTEX = new Object();
	private static final List<SimpleConsumerServlet> _instances = new ArrayList<SimpleConsumerServlet>();
	private HttpListener listener;
	private String hostString;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.hostString = getInitParameter("hostUrlAndPort");
		callOnAvailableListeners();
	}
	
	private void callOnAvailableListeners() {
		for (CallbackOnAvailableListener l : _callbackOnAvailableList) {
			l.onConsumerServletAvailable(this);
		}
		synchronized (MUTEX) {
			_instances.add(this);			
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req == null || this.listener == null) {
			finalizeResponse(resp);
			return;
		}
		
		StringBuilder sb = new StringBuilder();
	    Scanner scanner = new Scanner(req.getInputStream());
	    while (scanner.hasNextLine()) {
	    	sb.append(scanner.nextLine());
	    }
		
		scanner.close();
		
		this.listener.processRequest(sb.toString(), req.getRequestURI(), req.getMethod(), null);
		
		finalizeResponse(resp);
	}

	private void finalizeResponse(HttpServletResponse resp) {
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

	@Override
	public void setListener(HttpListener collectListener) {
		this.listener = collectListener;
	}

	@Override
	public void stop() {
		this.listener = null;
	}

	@Override
	public URL getPublicURL() {
		try {
			if (this.hostString != null) {
				log(this.hostString);
				log(getServletContext().getContextPath());
				log(getServletName());
				return new URL(this.hostString +
						getServletContext().getContextPath() + "/" +getServletConfig().getServletName());
			}
		} catch (MalformedURLException e) {
			log(e.getMessage(), e);
		}
		return null;
	}

	public static void registerCallbackOnAvailable(CallbackOnAvailableListener l) {
		_callbackOnAvailableList.add(l);
		
		synchronized (MUTEX) {
			if (!_instances.isEmpty()) {
				for (SimpleConsumerServlet s : _instances) {
					l.onConsumerServletAvailable(s);
				}
			}	
		}
	}
	
	/**
	 * A callback listener which is informed when the servlet is available.
	 * 
	 * @author matthes rieke
	 *
	 */
	public static interface CallbackOnAvailableListener {
		
		public void onConsumerServletAvailable(SimpleConsumerServlet servlet);
		
	}
}
