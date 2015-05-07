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
package org.n52.oxf.ses.adapter.client;

import java.net.URI;
import java.net.URL;

import org.apache.xmlbeans.XmlObject;

/**
 * Interface for provision of alternative connector implementations.
 * Use {@link SESClient#setConnectorImplementation(Class)} with a class
 * implementing this interface.
 * 
 * All implementing classes must provide the default constructor with
 * no parameters.
 * 
 * @author matthes rieke
 *
 */
public interface ISESConnector {

	
	public SESResponse sendHttpPostRequest(String request, String action) throws Exception;
	
	public SESResponse sendHttpGetRequest(URI uri) throws Exception;
	
	public void setHost(URL h);
	
	public void initialize();
	
	public void setAddSoap(boolean b);

	public URL getHost();
	
	
	public static class SESResponse {
		
		private int statusCode;
		private XmlObject responseBody;
		
		
		public SESResponse(int statusCode, XmlObject responseBody) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}


		public int getStatusCode() {
			return this.statusCode;
		}


		public XmlObject getResponseBody() {
			return this.responseBody;
		}
		
		
	}


	public void shutdown();



}
