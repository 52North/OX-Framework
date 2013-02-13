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
package org.n52.oxf.ses.adapter.client.httplistener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Simple HTTP client (based on NanoHTTPD) to receive
 * WS-N notifications.
 * 
 * @author matthes rieke
 *
 */
public class SimpleWSNConsumer extends NanoHTTPD implements IWSNConsumer {

	private static final String HTTP_NOCONTENT = "204 No Content";

	private HttpListener listener;
	private URL publicURL;
	
	public SimpleWSNConsumer(int port, String publicUrl) throws IOException, InterruptedException {
		this(port, publicUrl, new File("."), null);
	}
	
	
	public SimpleWSNConsumer(int port, String publicUrl, File wwwroot, HttpListener l) throws IOException, InterruptedException {
		super(port, wwwroot);
		this.listener = l;
		this.publicURL = new URL(publicUrl);
	}

	@Override
	protected Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
		String data = parms.getProperty(POST_BODY);
		
		if (data == null) {
			data = parseURLEncoded(parms);
		}
		
		String resp = null;
		if (data != null && !data.isEmpty() && this.listener != null) {
			resp = this.listener.processRequest(data, uri, method, header);	
		}
		
		if (resp == null || resp.isEmpty()) {
			return new NanoHTTPD.Response(HTTP_NOCONTENT, null, resp);
		} else {
			return new NanoHTTPD.Response(HTTP_OK, MIME_XML, resp);
		}
	}


	private String parseURLEncoded(Properties parms) {
		StringBuilder sb = new StringBuilder();
		for (Object o : parms.keySet()) {
			sb.append(o.toString()+"="+parms.get(o).toString());
		}
		return sb.toString();
	}


	public void setListener(HttpListener listener) {
		this.listener = listener;
	}


	@Override
	public URL getPublicURL() {
		return this.publicURL;
	}

	
	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(SimpleWSNConsumer.class);
		try {
			SimpleWSNConsumer sc = new SimpleWSNConsumer(8082, "http://localhost:8082");
			sc.setListener(new HttpListener() {
				
				@Override
				public String processRequest(String request, String uri, String method,
						Properties header) {
					synchronized (logger) {
						logger.info("Received reqeust for {}:", uri);
						logger.info(request);
					}
					
					return null;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while (true);
	}
	
}
