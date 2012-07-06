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
package org.n52.oxf.xmlbeans.parser;

import java.util.List;

import javax.xml.namespace.QName;

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
			new QName("http://www.opengis.net/gml", "AbstractFeature");
	
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
		QName offending = xve.getOffendingQName();
		List expected = xve.getExpectedQNames();
		return offending != null && offending.equals(QN_SA_1_0_SAMPLING_POINT) && // correct substitution
				expected != null && expected.contains(QN_GML_ABSTRACT_FEATURE); // correct super class
	}

}
