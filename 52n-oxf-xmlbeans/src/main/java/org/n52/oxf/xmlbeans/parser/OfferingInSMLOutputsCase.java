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
public class OfferingInSMLOutputsCase implements LaxValidationCase {
	private static final Object QN_GML_ABSTRACT_METADATA = 
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
	
	public boolean shouldPass(XmlValidationError xve) {
		QName offending = xve.getOffendingQName();
		List expected = xve.getExpectedQNames();
		QName field = xve.getFieldQName();
		return offending != null && offending.equals(QN_SOS_1_0_OFFERING) && // correct substitution
				expected != null && expected.contains(QN_GML_ABSTRACT_METADATA) && // correct super class
				field != null && field.equals(QN_GML_METADATA_PROPERTY); // correct field
	}

}
