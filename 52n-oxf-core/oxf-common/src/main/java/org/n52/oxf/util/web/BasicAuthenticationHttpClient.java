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
package org.n52.oxf.util.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class BasicAuthenticationHttpClient extends HttpClientDecorator {

	protected Map<HttpHost, UserPasswordAuthentication> credentials = new HashMap<HttpHost, UserPasswordAuthentication>();
	
	public BasicAuthenticationHttpClient(HttpClient toDecorate) {
		super(toDecorate);
		addAuthenticationInterceptor(getHttpClientToDecorate());
	}
	
	private void addAuthenticationInterceptor(DefaultHttpClient httpclient) {
        httpclient.addRequestInterceptor(getAuthenticationIntercepter(), 0);
	}

	protected HttpRequestInterceptor getAuthenticationIntercepter() {
		return new BasicAuthenticationInterceptor();
	}

	public synchronized void provideAuthentication(HttpHost host, String user, String pw) {
		this.credentials.put(host, new UserPasswordAuthentication(user, pw));
	}

	private final class BasicAuthenticationInterceptor implements HttpRequestInterceptor {

		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			
			UserPasswordAuthentication creds;
			synchronized (this) {
				creds = credentials.get(targetHost);	
			}
			
			if (creds != null) {
				AuthScope scope = new AuthScope(targetHost);
				
				BasicCredentialsProvider credProvider = new BasicCredentialsProvider();
				credProvider.setCredentials(scope, new UsernamePasswordCredentials(creds.getUsername(),
						creds.getPassword()));
				context.setAttribute(ClientContext.CREDS_PROVIDER, credProvider);	
			}
		}
		
	}
	
}
