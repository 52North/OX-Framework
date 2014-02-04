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
	
	private static final String SOS_2_0_NS_URI = "http://www.opengis.net/sos/2.0";
	public static final QName QNAME_SOS_2_0_INSERTION_METADATA = new QName(SOS_2_0_NS_URI,"SosInsertionMetadata");
	public static final QName QN_GML_3_2_ABSTRACT_FEATURE = new QName("http://www.opengis.net/gml/3.2", "AbstractFeature");
	public static final QName QN_SF_2_0_SPATIAL_SAMPLING_FEATURE = new QName("http://www.opengis.net/samplingSpatial/2.0","SF_SpatialSamplingFeature");
	public static final QName QN_OM_2_0_FEATURE_OF_INTEREST = new QName("http://www.opengis.net/om/2.0","featureOfInterest");
	public static final String SWES_2_0_NS_URI = "http://www.opengis.net/swes/2.0";
	public static final QName QN_SWES_2_0_INSERTION_METADATA = new QName(SWES_2_0_NS_URI, "InsertionMetadata");
	public static final QName QN_SOS_2_0_SOS_INSERTION_METADATA = new QName("http://www.opengis.net/sos/2.0","SosInsertionMetadata");
	public static final QName QN_SWES_2_0_METADATA = new QName(SWES_2_0_NS_URI,"metadata");
	public static final QName QN_GML_ABSTRACT_FEATURE = 
	new QName("http://www.opengis.net/gml", "_Feature");
	public static final QName QN_SA_1_0_SAMPLING_POINT = 
	new QName("http://www.opengis.net/sampling/1.0", "SamplingPoint");
	public static final QName FEATURE_QN = new QName("http://www.opengis.net/gml", "_Feature");
	public static final QName FEATURE_COLLECTION_QN = new QName("http://www.opengis.net/gml", "_FeatureCollection");
	
	private XMLConstants() {}
}
