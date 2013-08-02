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
package org.n52.oxf.ses.adapter.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ISESConnector}.
 * This one uses Apache's HttpClient library.
 * 
 * @author matthes rieke
 *
 */
public class SESConnectorImpl implements ISESConnector {

	private static final Logger logger = LoggerFactory
	.getLogger(SESConnectorImpl.class);
	private DefaultHttpClient httpClient;
	private URL host;
	private boolean ready;
	private boolean addSoap = true;

	private static String SOAP_PRE;
	private static String SOAP_POST;
	private static String XML_PRE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	static {
		/*
		 * read soap pre and post
		 */
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				SESConnectorImpl.class.getResourceAsStream("soap_pre.xml")));

		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
		SOAP_PRE = sb.toString();

		br = new BufferedReader(new InputStreamReader(
				SESConnectorImpl.class.getResourceAsStream("soap_post.xml")));
		sb = new StringBuilder();

		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
		SOAP_POST = sb.toString();
	}


	public SESResponse sendHttpPostRequest(String request, String action) throws IllegalStateException, IOException {
		if (!this.ready) return null;

		HttpPost req = new HttpPost(this.host.toString());

		String str;
		if (this.addSoap) {
			String thisPre = SOAP_PRE.replace("${ses_host}", req.getURI().toString()).replace("${soap_action}", action);
			str = XML_PRE +System.getProperty("line.separator")+ 
			thisPre + request + SOAP_POST;
		} else {
			str = XML_PRE +System.getProperty("line.separator")+ request;
		}

		StringEntity postContent = new StringEntity(str);
		postContent.setContentType("text/xml; charset=utf-8");

		req.setEntity(postContent);

		HttpResponse response = null;
		try {
			response = this.httpClient.execute(req);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			req.abort();
			return new SESResponse(500, null);
		}

		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();

			XmlObject xo = null;
			if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES) {
				logger.warn(parseError(instream));
			} else {
				try {
					xo = XmlObject.Factory.parse(instream);
				} catch (XmlException e) {
					logger.warn(e.getMessage(), e);
				}
				instream.close();	
			}

			return new SESResponse(response.getStatusLine().getStatusCode(), xo);
		} else {
			req.abort();
			return new SESResponse(response.getStatusLine().getStatusCode(), null);
		}
	}

	private String parseError(InputStream instream) {
		StringBuilder sb = new StringBuilder();

		BufferedReader br = new BufferedReader(new InputStreamReader(instream));

		try {
			while (br.ready()) {
				sb.append(br.readLine());
			}
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		}

		return sb.toString();
	}

	public URL getHost() {
		return this.host;
	}

	public void setAddSoap(boolean addSoap) {
		this.addSoap = addSoap;
	}

	@Override
	public void setHost(URL h) {
		this.host = h;
	}

	@Override
	public void initialize() {
		this.httpClient = new DefaultHttpClient();

		this.ready = !(SOAP_PRE == null || SOAP_POST == null);
	}

	@Override
	public SESResponse sendHttpGetRequest(URI uri) throws Exception {
		HttpGet req = new HttpGet(uri);

		HttpResponse response = new DefaultHttpClient().execute(req);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			XmlObject xo;
			xo = XmlObject.Factory.parse(instream);
			return new SESResponse(response.getStatusLine().getStatusCode(), xo);
		} else {
			return new SESResponse(response.getStatusLine().getStatusCode(), null);
		}
	}

	@Override
	public void shutdown() {
	}


}
