package org.n52.oxf.ses.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.ses.adapter.client.Publisher;
import org.n52.oxf.ses.adapter.client.Subscription;

public class RegisterPublisherResponseParsingTest {

	
	@Test
	public void
	shouldParseRegisterPublisherResponse() throws XmlException, IOException {
		XmlObject xo = readResponse();
		
		Publisher pub = new Publisher(null);
		pub.parseResponse(xo);
		
		Assert.assertTrue(pub.getResourceID().trim().equals("Publisher-1"));
		Assert.assertTrue(pub.getPublisherAddress().trim().endsWith("services/PublisherRegistrationManager"));
	}
	
	private XmlObject readResponse() throws XmlException, IOException {
		return XmlObject.Factory.parse(getClass().getResourceAsStream("RegisterPublisherResponse.xml"));
	}
}
