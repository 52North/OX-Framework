/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
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
