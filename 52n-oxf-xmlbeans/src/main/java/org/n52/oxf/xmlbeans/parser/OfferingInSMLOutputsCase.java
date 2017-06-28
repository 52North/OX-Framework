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
package org.n52.oxf.xmlbeans.parser;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlValidationError;

/**
 * The following element is not allowed at 
 * <code>sml:system.outputs.OutputList.output.swe:Quantity.gml:metaDataProperty</code>
 *  but the 1.0.0 implemtation required, so we have this LaxValidationCase to accept the following element:<br />
 * &lt;gml:metaDataProperty&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;sos100:offering&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;sos100:id&gt;http://nsava.gov/sensors/gldas&lt;/sos100:id&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;sos100:name&gt;GLDAS&lt;/sos100:name&gt;<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/sos100:offering&gt;<br />
 * &lt;/gml:metaDataProperty&gt;<br />
 * This case checks for the correct field, expected {@link javax.xml.namespace.QName QName} and offending 
 * {@link javax.xml.namespace.QName QName}. Should only be applied at {@link net.opengis.sos.x10.RegisterSensorDocument RegisterSensorDocument} 
 * instances.<br />
 * Errore message:<br />
 * <code>error: cvc-complex-type.2.4a: Expected element 'AbstractMetaData@http://www.opengis.net/gml' instead of 'offering@http://www.opengis.net/sos/1.0' here in element metaDataProperty@http://www.opengis.net/gml</code>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class OfferingInSMLOutputsCase extends AbstractLaxValidationCase {
	
	private static final QName QN_GML_ABSTRACT_METADATA = 
			new QName("http://www.opengis.net/gml", "AbstractMetaData");
	
	private static final QName QN_GML_METADATA_PROPERTY = 
			new QName("http://www.opengis.net/gml","metaDataProperty");
	
	private static final QName QN_SOS_1_0_OFFERING = 
			new QName("http://www.opengis.net/sos/1.0", "offering");
	
	private static OfferingInSMLOutputsCase instance = null;
	
	private OfferingInSMLOutputsCase() {}
	
	public static OfferingInSMLOutputsCase getInstance() {
		if (instance == null) {
			instance = new OfferingInSMLOutputsCase();
		}
		return instance;
	}
	
	@Override
	public boolean shouldPass(final XmlValidationError xve) {
		final QName offending = xve.getOffendingQName();
		final List<?> expected = xve.getExpectedQNames();
		final QName field = xve.getFieldQName();
		return offending != null && offending.equals(QN_SOS_1_0_OFFERING) && // correct substitution
				expected != null && expected.contains(QN_GML_ABSTRACT_METADATA) && // correct super class
				field != null && field.equals(QN_GML_METADATA_PROPERTY); // correct field
	}
}
