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
