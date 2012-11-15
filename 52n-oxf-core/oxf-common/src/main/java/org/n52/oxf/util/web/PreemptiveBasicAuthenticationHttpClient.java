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

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

public class PreemptiveBasicAuthenticationHttpClient extends BasicAuthenticationHttpClient {

	public PreemptiveBasicAuthenticationHttpClient(HttpClient toDecorate) {
		super(toDecorate);
	}

	@Override
	protected HttpRequestInterceptor getAuthenticationIntercepter() {
		return new PreemptiveBasicAuthenticationInterceptor();
	}
	
	
	private final class PreemptiveBasicAuthenticationInterceptor implements HttpRequestInterceptor {

		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			
			UserPasswordAuthentication creds;
			synchronized (this) {
				creds = credentials.get(targetHost);	
			}
			
			if (creds != null) {
				UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(creds.getUsername(),
						creds.getPassword());
				AuthScope scope = new AuthScope(targetHost);
				
				getHttpClientToDecorate().getCredentialsProvider().setCredentials(scope, credentials);
				
				AuthCache cache = new BasicAuthCache();
				cache.put(targetHost, new BasicScheme());
				
				context.setAttribute(ClientContext.AUTH_CACHE, cache);
				
				BasicCredentialsProvider credProvider = new BasicCredentialsProvider();
				credProvider.setCredentials(scope, credentials);
				context.setAttribute(ClientContext.CREDS_PROVIDER, credProvider);	
			}
		}
		
	}
	
}
