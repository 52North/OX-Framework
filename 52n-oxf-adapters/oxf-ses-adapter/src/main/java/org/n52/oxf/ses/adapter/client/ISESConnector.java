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

import java.net.URI;
import java.net.URL;

import org.apache.xmlbeans.XmlObject;

/**
 * Interface for provision of alternative connector implementations.
 * Use {@link SESClient#setConnectorImplementation(Class)} with a class
 * implementing this interface.
 * 
 * All implementing classes must provide the default constructor with
 * no parameters.
 * 
 * @author matthes rieke
 *
 */
public interface ISESConnector {

	
	public SESResponse sendHttpPostRequest(String request, String action) throws Exception;
	
	public SESResponse sendHttpGetRequest(URI uri) throws Exception;
	
	public void setHost(URL h);
	
	public void initialize();
	
	public void setAddSoap(boolean b);

	public URL getHost();
	
	
	public static class SESResponse {
		
		private int statusCode;
		private XmlObject responseBody;
		
		
		public SESResponse(int statusCode, XmlObject responseBody) {
			this.statusCode = statusCode;
			this.responseBody = responseBody;
		}


		public int getStatusCode() {
			return this.statusCode;
		}


		public XmlObject getResponseBody() {
			return this.responseBody;
		}
		
		
	}


	public void shutdown();



}
