/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.oxf.util;

import javax.xml.namespace.QName;

/**
 * This class holds re-usable XML based constants like QName for substituting 
 * XML types.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class XMLConstants {

	private static final String GML_NS_URI = "http://www.opengis.net/gml";
	public static final QName QNAME_GML_TIMEINSTANT = new QName(GML_NS_URI, "TimeInstant");

	private static final String SA_NS_URI = "http://www.opengis.net/sampling/1.0";
	public static final QName QNAME_SA_1_0_SAMPLING_POINT = new QName(SA_NS_URI, "SamplingPoint");

	private static final String OM_1_0_NS_URI = "http://www.opengis.net/om/1.0";
	public static final QName QNAME_OM_1_0_MEASUREMENT = new QName(OM_1_0_NS_URI, "Measurement");
	public static final QName QNAME_OM_1_0_CATEGORY_OBSERVATION = new QName(OM_1_0_NS_URI, "CategoryObservation");
	public static final QName QNAME_OM_1_0_CODE_SPACE = new QName(OM_1_0_NS_URI, "codeSpace");
	public static final QName QNAME_OM_1_0_SAMPLING_TIME = new QName(OM_1_0_NS_URI, "samplingTime");
	public static final QName QNAME_OM_1_0_PROCEDURE = new QName(OM_1_0_NS_URI, "procedure");
	public static final QName QNAME_OM_1_0_OBSERVED_PROPERTY = new QName(OM_1_0_NS_URI, "observedProperty");
	public static final QName QNAME_OM_1_0_FEATURE_OF_INTEREST = new QName(OM_1_0_NS_URI, "featureOfInterest");
	public static final QName QNAME_OM_1_0_RESULT = new QName(OM_1_0_NS_URI, "result");

}
