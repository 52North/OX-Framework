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

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionConstraints {

	private static final Logger logger = LoggerFactory
			.getLogger(SubscriptionConstraints.class);

	public static final String ALL_FILTER = "<wsnt:MessageContent Dialect=\"http://www.w3.org/TR/1999/REC-xpath-19991116\">*</wsnt:MessageContent>";
	
	protected String consumer;
	private XmlObject document;
	protected String filter;


	/**
	 * Creates a {@link SubscriptionConstraints} instance with no filter defined.
	 * A filter can be set using {@link SubscriptionConstraints#setFilter(String)} or 
	 * with the constructor {@link SubscriptionConstraints#SubscriptionConstraints(String, String)}.
	 * 
	 * If no filter is set, a SubscribeForAll Filter will be used.
	 * 
	 * @param consumer the consumer URI
	 */
	public SubscriptionConstraints(String consumer) {
		this.consumer = consumer;
	}
	
	/**
	 * @param consumer the consumer URI
	 * @param filterAsString see {@link SubscriptionConstraints#setFilter(String)} for details.
	 */
	public SubscriptionConstraints(String consumer, String filterAsString) {
		this(consumer);
		this.filter = filterAsString;
	}

	public XmlObject getSubscriptionDocument() throws XmlException {
		StringBuilder sb = new StringBuilder();
		InputStream in = SubscriptionConstraints.class.getResourceAsStream("template_subscribe.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}

		this.document = XmlObject.Factory.parse(
				sb.toString().replace("${consumer}", this.consumer).
				replace("${filter_xml}", (this.filter == null) ? ALL_FILTER : this.filter));
		
		return this.document;
	}

	public String getConsumer() {
		return consumer;
	}

	/**
	 * The Filter compliant to the WS-N spec. In particular, it must be of
	 * following syntax:
	 * 
	 * <wsnt:MessageContent Dialect="{dialectAsString}" xmlns:wsnt="http://docs.oasis-open.org/wsn/b-2">{any}</wsnt:MessageContent>
	 * 
	 * @param f
	 */
	public void setFilter(String f) {
		this.filter = f;
	}
	
	public static class XPathSubscription extends SubscriptionConstraints {

		public XPathSubscription(String consumer, String xpath) {
			super(consumer);
			this.filter = "<wsnt:MessageContent Dialect=\"http://www.w3.org/TR/1999/REC-xpath-19991116\">"
					+xpath+"</wsnt:MessageContent>";
		}
		
	}

	public static class DynamicFilterSubscription extends SubscriptionConstraints {

		public DynamicFilterSubscription(String consumer) {
			super(consumer);
			
			StringBuilder sb = new StringBuilder();
			InputStream in = getClass().getResourceAsStream("dynamic_subscribe.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			try {
				while (br.ready()) {
					sb.append(br.readLine());
				}
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}

			this.filter = sb.toString();
		}


	}

}
