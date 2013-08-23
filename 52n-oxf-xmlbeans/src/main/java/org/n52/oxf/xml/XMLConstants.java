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
package org.n52.oxf.xml;

import javax.xml.namespace.QName;

/**
 * This class holds re-usable XML based constants like QName for substituting 
 * XML types.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class XMLConstants {

	private static final String GML_NS_URI = "http://www.opengis.net/gml";
	public static final QName QNAME_GML_TIMEINSTANT = new QName(GML_NS_URI, "TimeInstant");
	private static final String GML_3_2_NS_URI = "http://www.opengis.net/gml/3.2";
	public static final QName QNAME_GML_3_2_TIMEINSTANT = new QName(GML_3_2_NS_URI, "TimeInstant");
	public static final QName QNAME_GML_3_2_TIME_PERIOD = new QName(GML_3_2_NS_URI, "TimePeriod");

	private static final String SA_NS_URI = "http://www.opengis.net/sampling/1.0";
	public static final QName QNAME_SA_1_0_SAMPLING_POINT = new QName(SA_NS_URI, "SamplingPoint");

	private static final String OM_1_0_NS_URI = "http://www.opengis.net/om/1.0";
	public static final QName QNAME_OM_1_0_OBSERVATION = new QName(OM_1_0_NS_URI, "Observation");
	public static final QName QNAME_OM_1_0_MEASUREMENT_OBSERVATION = new QName(OM_1_0_NS_URI, "Measurement");
	public static final QName QNAME_OM_1_0_TEXT_OBSERVATION = new QName(OM_1_0_NS_URI, "TextObservation");
	public static final QName QNAME_OM_1_0_COUNT_OBSERVATION = new QName(OM_1_0_NS_URI, "CountObservation");
	public static final QName QNAME_OM_1_0_TRUTH_OBSERVATION = new QName(OM_1_0_NS_URI, "TruthObservation");
	public static final QName QNAME_OM_1_0_TEMPORAL_OBSERVATION = new QName(OM_1_0_NS_URI, "TemporalObservation");
	public static final QName QNAME_OM_1_0_GEOMETRY_OBSERVATION = new QName(OM_1_0_NS_URI, "GeometryObservation");
	public static final QName QNAME_OM_1_0_COMPLEX_OBSERVATION = new QName(OM_1_0_NS_URI, "ComplexObservation");
	
	// for observations with varying properties
	// public static final QName QNAME_OM_1_0_DISCRETE_COVERAGE_OBSERVATION = new QName(OM_1_0_NS_URI, "DiscreteCoverageObservation");
	// public static final QName QNAME_OM_1_0_POINT_COVERAGE_OBSERVATION = new QName(OM_1_0_NS_URI, "PointCoverageObservation");
	// public static final QName QNAME_OM_1_0_TIME_SERIES_OBSERVATION = new QName(OM_1_0_NS_URI, "TimeSeriesObservation");
	// public static final QName QNAME_OM_1_0_ELEMENT_COVERAGE_OBSERVATION = new QName(OM_1_0_NS_URI, "ElementCoverageObservation");
	
	public static final QName QNAME_OM_1_0_TEXT = new QName(OM_1_0_NS_URI, "text");
	public static final QName QNAME_OM_1_0_SAMPLING_TIME = new QName(OM_1_0_NS_URI, "samplingTime");
	public static final QName QNAME_OM_1_0_PROCEDURE = new QName(OM_1_0_NS_URI, "procedure");
	public static final QName QNAME_OM_1_0_OBSERVED_PROPERTY = new QName(OM_1_0_NS_URI, "observedProperty");
	public static final QName QNAME_OM_1_0_FEATURE_OF_INTEREST = new QName(OM_1_0_NS_URI, "featureOfInterest");
	public static final QName QNAME_OM_1_0_RESULT = new QName(OM_1_0_NS_URI, "result");
	
	public static final String SWE_1_0_1_NS_URI = "http://www.opengis.net/swe/1.0.1";
	
	public static final QName QNAME_SWE_1_0_1_DATA_RECORD = new QName(SWE_1_0_1_NS_URI, "DataRecord");

	/*
	 * 	O&M 2.0 
	 */
	public static final String OGC_OM_2_0_OM_CATEGORY_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation";
	public static final String OGC_OM_2_0_OM_MEASUREMENT = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
	public static final String OGC_OM_2_0_OM_TRUTH_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
	public static final String OGC_OM_2_0_OM_TEXT_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
	public static final String OGC_OM_2_0_OM_COUNT_OBSERVATION = "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";

	public static final String OGC_OM_2_0_SF_SAMPLING_POINT = "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";
	
	public static final String OM_2_0_NS_URI = "http://www.opengis.net/om/2.0";
	
	public static final String SAMS_2_0_NS_URI = "http://www.opengis.net/samplingSpatial/2.0";
	
	public static final QName QNAME_SAMS_2_0_SF_SSF = new QName(SAMS_2_0_NS_URI,"SF_SpatialSamplingFeature");
	
	public static final String OGC_UNKNOWN_VALUE = "http://www.opengis.net/def/nil/OGC/0/unknown";
	public static final String OGC_URI_START_CRS = "http://www.opengis.net/def/crs/EPSG/0/";
	
	private XMLConstants() {}
}
