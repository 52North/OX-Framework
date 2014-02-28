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
package org.n52.oxf.ses.adapter;

import java.io.IOException;
import java.net.URL;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ses.adapter.ISESRequestBuilder;
import org.n52.oxf.ses.adapter.SESAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;

public class SESServiceInstance {
	
	private static final Logger logger = LoggerFactory.getLogger(SESServiceInstance.class);
	private boolean ready = false;
	private URL host;
	private Object availableMutex = new Object();
	protected boolean serviceTested = false;
	
	
	public SESServiceInstance(URL instance) {
		this.host = instance;
		logger.info("using ses host at {}", host);

		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				boolean success = false;
				try {
					while (System.currentTimeMillis() - start < 10000) {
						Thread.sleep(1000);
						if (requestCapabilities()) {
							success = true;
							break;
						}
					}
				} catch (InterruptedException e) {
					logger.warn(e.getMessage(), e);
				}
				synchronized (availableMutex) {
					ready = success;
					serviceTested = true;
					availableMutex.notifyAll();
				}
			}

			private boolean requestCapabilities() {
				SESAdapter adapter = new SESAdapter("0.0.0");
		        
		        Operation op = new Operation(SESAdapter.GET_CAPABILITIES, null, host.toExternalForm());
		        
		        ParameterContainer parameter = new ParameterContainer();
		        try {
					parameter.addParameterShell(ISESRequestBuilder.GET_CAPABILITIES_SES_URL, host.toExternalForm());
			        OperationResult opResult = adapter.doOperation(op, parameter);
			        
			        if (opResult == null) return false;
			        
			        XmlObject response = XmlObject.Factory.parse(opResult.getIncomingResultAsStream());
			        if (response instanceof EnvelopeDocument) {
			        	if (((EnvelopeDocument) response).getEnvelope().getBody().xmlText().contains("Capabilities")) {
			        		return true;
			        	}
			        }
				} catch (OXFException e) {
					logger.warn(e.getMessage());
				} catch (XmlException e) {
					logger.warn(e.getMessage());
				} catch (IOException e) {
					logger.warn(e.getMessage());
				} catch (ExceptionReport e) {
					logger.warn(e.getMessage());
				}

		        return false;
			}
		}).start();
	}
	
	public void waitUntilAvailable() {
		synchronized (availableMutex) {
			while (!serviceTested) {
				logger.info("Waiting for service to be ready.");
				try {
					availableMutex.wait();
				} catch (InterruptedException e) {
					logger.warn(e.getMessage());
				}
			}
			if (!ready) throw new IllegalStateException("Could not access the service at "+host);
		}
		logger.info("Service Ready!");		
	}

	public URL getHost() {
		return host;
	}
	
	

}
