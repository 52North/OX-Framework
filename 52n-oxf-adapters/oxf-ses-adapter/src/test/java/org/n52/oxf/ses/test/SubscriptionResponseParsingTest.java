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
package org.n52.oxf.ses.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.ses.adapter.client.Subscription;

public class SubscriptionResponseParsingTest {

	
	@Test
	public void
	shouldParseSubscriptionResponse() throws XmlException, IOException {
		XmlObject xo = readResponse();
		
		Subscription sub = new Subscription(null);
		sub.parseResponse(xo);
		
		Assert.assertTrue(sub.getResourceID().trim().equals("Subscription-2"));
		Assert.assertTrue(sub.getManager().getHost().toExternalForm().endsWith("services/SubscriptionManager"));
	}
	
	private XmlObject readResponse() throws XmlException, IOException {
		URL wsdlLocation = getClass().getResource("dummy-wsdl.wsdl");
		
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("SubscriptionResponse.xml")));
		
		while (br.ready()) {
			sb.append(br.readLine());
		}
		br.close();
		
		return XmlObject.Factory.parse(sb.toString().replace("${wsdlLocation}", wsdlLocation.toExternalForm()));
	}
}
