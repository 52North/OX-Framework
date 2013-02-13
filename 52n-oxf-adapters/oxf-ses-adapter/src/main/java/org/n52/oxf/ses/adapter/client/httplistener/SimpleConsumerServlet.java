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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.oxf.ses.adapter.client.httplistener.HttpListener;

/**
 * Specify the use of this servlet
 * if you use the client within a servlet 2.x container (jetty, tomcat, etc).
 * 
 * @author matthes rieke
 *
 */
public class SimpleConsumerServlet extends HttpServlet implements IWSNConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final List<CallbackOnAvailableListener> _callbackOnAvailableList = new ArrayList<CallbackOnAvailableListener>();
	private static final Object MUTEX = new Object();
	private static final List<SimpleConsumerServlet> _instances = new ArrayList<SimpleConsumerServlet>();
	private HttpListener listener;
	private String hostString;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.hostString = getInitParameter("hostUrlAndPort");
		callOnAvailableListeners();
	}
	
	private void callOnAvailableListeners() {
		for (CallbackOnAvailableListener l : _callbackOnAvailableList) {
			l.onConsumerServletAvailable(this);
		}
		synchronized (MUTEX) {
			_instances.add(this);			
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req == null || this.listener == null) {
			finalizeResponse(resp);
			return;
		}
		
		StringBuilder sb = new StringBuilder();
	    Scanner scanner = new Scanner(req.getInputStream());
	    while (scanner.hasNextLine()) {
	    	sb.append(scanner.nextLine());
	    }
		
		scanner.close();
		
		this.listener.processRequest(sb.toString(), req.getRequestURI(), req.getMethod(), null);
		
		finalizeResponse(resp);
	}

	private void finalizeResponse(HttpServletResponse resp) {
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

	@Override
	public void setListener(HttpListener collectListener) {
		this.listener = collectListener;
	}

	@Override
	public void stop() {
		this.listener = null;
	}

	@Override
	public URL getPublicURL() {
		try {
			if (this.hostString != null) {
				log(this.hostString);
				log(getServletContext().getContextPath());
				log(getServletName());
				return new URL(this.hostString +
						getServletContext().getContextPath() + "/" +getServletConfig().getServletName());
			}
		} catch (MalformedURLException e) {
			log(e.getMessage(), e);
		}
		return null;
	}

	public static void registerCallbackOnAvailable(CallbackOnAvailableListener l) {
		_callbackOnAvailableList.add(l);
		
		synchronized (MUTEX) {
			if (!_instances.isEmpty()) {
				for (SimpleConsumerServlet s : _instances) {
					l.onConsumerServletAvailable(s);
				}
			}	
		}
	}
	
	/**
	 * A callback listener which is informed when the servlet is available.
	 * 
	 * @author matthes rieke
	 *
	 */
	public static interface CallbackOnAvailableListener {
		
		public void onConsumerServletAvailable(SimpleConsumerServlet servlet);
		
	}
}
