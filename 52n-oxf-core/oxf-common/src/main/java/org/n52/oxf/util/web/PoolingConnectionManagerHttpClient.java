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

import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class PoolingConnectionManagerHttpClient extends SimpleHttpClient {

	private static SchemeRegistry schemeRegistry;

	static {
		schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
				new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
	}
	
	public PoolingConnectionManagerHttpClient() {
		super();
	}
	
	public PoolingConnectionManagerHttpClient(int connectionTimeout) {
		super(connectionTimeout);
	}
	
	@Override
	public ClientConnectionManager getConnectionManager() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 200
		cm.setMaxTotal(200);
		// Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(20);
		// Increase max connections for localhost:80 to 50
		HttpHost localhost8080 = new HttpHost("localhost", 8080);
		cm.setMaxPerRoute(new HttpRoute(localhost8080), 50);
		HttpHost localhost = new HttpHost("localhost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);

		return cm;
	}


	

}
