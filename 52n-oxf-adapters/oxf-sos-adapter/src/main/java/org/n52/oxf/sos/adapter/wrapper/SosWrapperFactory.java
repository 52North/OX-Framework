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
package org.n52.oxf.sos.adapter.wrapper;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;

public class SosWrapperFactory {

	/**
	 * Creates a SOSWrapper instance by initiating a GetCapabilities request.
	 * 
	 * @param serviceEndpoint Location of the SOS service.
	 * @param serviceVersion specification version.
	 * @return Instance of a SOSWrapper for a certain SOS service.
	 * @throws ExceptionReport
	 * @throws OXFException
	 */
	public static SOSWrapper newInstance(final String serviceEndpoint, final String serviceVersion) throws ExceptionReport, OXFException {
		return newInstance(serviceEndpoint, serviceVersion, null);
	}

	/**
	 * Creates a SOSWrapper instance by initiating a GetCapabilities request.
	 * 
	 * @param serviceEndpoint Location of the SOS service.
	 * @param serviceVersion specification version.
	 * @param binding binding protocol used by this service instance.
	 * @return Instance of a SOSWrapper for a certain SOS service.
	 * @throws OXFException 
	 * @throws ExceptionReport 
	 */
	public static SOSWrapper newInstance(final String serviceEndpoint,
			final String serviceVersion,
			final Binding binding) throws ExceptionReport, OXFException
	{
		final ServiceDescriptor capabilities = doGetCapabilities(serviceEndpoint, serviceVersion,binding);
		return new SOSWrapper(capabilities,binding);
	}

	/**
	 * Requests and returns the SOS capability description.
	 * 
	 * @param serviceEndpoint Location of the SOS service.
	 * @param serviceVersion specification version.
	 * @param binding
	 * @return ServiceDescriptor filled with data resulting from the GetCapabilities request.
	 * @throws OXFException 
	 * @throws ExceptionReport 
	 */
	private static ServiceDescriptor doGetCapabilities(final String serviceEndpoint,
			final String serviceVersion,
			final Binding binding) throws ExceptionReport, OXFException
	{
		final SOSAdapter adapter = new SOSAdapter(serviceVersion);
		return adapter.initService(serviceEndpoint,binding);
	}
	
	/**
	 * Creates a SOSWrapper instance from given ServiceDescriptor
	 * @param capabilities
	 * @return
	 */
	public static SOSWrapper newInstance(final ServiceDescriptor capabilities)
	{
		return new SOSWrapper(capabilities,null);
	}

}
