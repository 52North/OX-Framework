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
