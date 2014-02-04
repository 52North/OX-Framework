/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlValidationError;

/**
 * Allow sa:SamplingPoint when gml:AbstractFeature is expected.<br />
 * <b>SamplingPoint</b>@http://www.opengis.net/sampling/1.0 substitutes<br />
 * <b>SamplingFeature</b>@http://www.opengis.net/sampling/1.0 substitutes<br />
 * <b>_Feature</b>@http://www.opengis.net/gml<br />
 * <br />
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SASamplingPointCase implements LaxValidationCase {

	private static final Object QN_GML_ABSTRACT_FEATURE = 
			new QName("http://www.opengis.net/gml", "_Feature");
	
	private static final QName QN_SA_1_0_SAMPLING_POINT = 
			new QName("http://www.opengis.net/sampling/1.0", "SamplingPoint");
	
	private static SASamplingPointCase instance = null;
	
	private SASamplingPointCase() {}
	
	public static SASamplingPointCase getInstance() {
		if (instance == null) {
			instance = new SASamplingPointCase();
		}
		return instance;
	}
	
	public boolean shouldPass(XmlValidationError xve) {
		return false;
	}

	public boolean shouldPass(XmlError validationError) {
		if (!(validationError instanceof XmlValidationError)) return false;
		
		XmlValidationError xve = (XmlValidationError) validationError;
		QName offending = xve.getOffendingQName();
		List<?> expected = xve.getExpectedQNames();
		return offending != null && offending.equals(QN_SA_1_0_SAMPLING_POINT) && // correct substitution
				expected != null && expected.contains(QN_GML_ABSTRACT_FEATURE); // correct super class
	}

}
