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
package org.n52.oxf.sos.adapter;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;

/**
 * TODO Document fields and methods!
 * 
 * @author <a href="mailto:broering@52north.org">Arne Br&ouml;ring</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @author <a href="mailto:h.bredel@52north.org">Henning Bredel</a>
 * 
 */
public interface ISOSRequestBuilder {

    public static String GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER = "updateSequence";
    public static String GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER = "AcceptVersions";
    public static String GET_CAPABILITIES_SECTIONS_PARAMETER = "sections";
    public static String GET_CAPABILITIES_SERVICE_PARAMETER = "service";

    public static String GET_OBSERVATION_SERVICE_PARAMETER = "service";
    public static String GET_OBSERVATION_VERSION_PARAMETER = "version";
    public static String GET_OBSERVATION_OFFERING_PARAMETER = "offering";
    public static String GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
    public static String GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER = "responseFormat";
    public static String GET_OBSERVATION_EVENT_TIME_PARAMETER = "eventTime"; // SOS 1.0
    public static String GET_OBSERVATION_TEMPORAL_FILTER_PARAMETER = "temporalFilter"; // SOS 2.0
    public static String GET_OBSERVATION_PROCEDURE_PARAMETER = "procedure";
    public static String GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER = "featureOfInterest";
    public static String GET_OBSERVATION_SPATIAL_FILTER_PARAMETER = "spatialFilter"; // SOS 2.0
    public static String GET_OBSERVATION_RESULT_PARAMETER = "result"; // SOS 1.0
    public static String GET_OBSERVATION_RESULT_MODEL_PARAMETER = "resultModel"; // SOS 1.0
    public static String GET_OBSERVATION_RESPONSE_MODE_PARAMETER = "responseMode"; // SOS 1.0

    public static String DESCRIBE_SENSOR_SERVICE_PARAMETER = "service";
    public static String DESCRIBE_SENSOR_VERSION_PARAMETER = "version";
    public static String DESCRIBE_SENSOR_PROCEDURE_PARAMETER = "procedure";
    public static String DESCRIBE_SENSOR_OUTPUT_FORMAT = "outputFormat";
    public static String DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT = "procedureDescriptionFormat"; // SOS 2.0

    public static String GET_FOI_SERVICE_PARAMETER = "service";
    public static String GET_FOI_VERSION_PARAMETER = "version";
    public static String GET_FOI_EVENT_TIME_PARAMETER = "eventTime";
    public static String GET_FOI_ID_PARAMETER = "featureOfInterestId";
    public static String GET_FOI_LOCATION_PARAMETER = "location";

    public static String INSERT_OBSERVATION_SERVICE_PARAMETER = "service";
    public static String INSERT_OBSERVATION_VERSION_PARAMETER = "version";
    public static String INSERT_OBSERVATION_FOI_ID_PARAMETER = "featureOfInterestID";
    public static String INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER = "newFoiID";
    public static String INSERT_OBSERVATION_NEW_FOI_NAME = "newFoiName";
    public static String INSERT_OBSERVATION_NEW_FOI_DESC = "newFoiDesc";
    public static String INSERT_OBSERVATION_NEW_FOI_POSITION = "newFoiPosition";
    public static String INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
    public static String INSERT_OBSERVATION_SAMPLING_TIME = "samplingTime";
    public static String INSERT_OBSERVATION_VALUE_PARAMETER = "value";
	public static String INSERT_OBSERVATION_POSITION_SRS = "srsPosition";
	public static String INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE = "catObsCodespace";
	public static String INSERT_OBSERVATION_TYPE_TEXT = "text";
	public static String INSERT_OBSERVATION_TYPE_MEASUREMENT = "measurement";
	public static String INSERT_OBSERVATION_TYPE = "type";
    public static String INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE = "resultUom";
    public static String INSERT_OBSERVATION_NEW_FOI_POSITION_SRS = "insertObSRS";
    public static String INSERT_OBSERVATION_PROCEDURE_PARAMETER = "procedure";
	public static String INSERT_OBSERVATION_SENSOR_ID_PARAMETER = "sensorid";
    
    public static String REGISTER_SENSOR_SERVICE_PARAMETER = "service";
    public static String REGISTER_SENSOR_VERSION_PARAMETER = "version";
    public static String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";
    public static String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";
    public static String REGISTER_SENSOR_OBSERVATION_TYPE = "type";
	public static String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";
	public static String INSERT_OBSERVATION_TYPE_COUNT = "count";
	public static String INSERT_OBSERVATION_TYPE_TRUTH = "truth";
	public static String INSERT_OBSERVATION_TYPE_TEMPORAL = "temporal";
	public static String INSERT_OBSERVATION_TYPE_GEOMETRY = "geometry";
	public static String INSERT_OBSERVATION_TYPE_COMPLEX = "complex";
	public static String REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT = "measurement";
    public static String REGISTER_SENSOR_ID_PARAMETER = "id";
    public static String REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
    public static String REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER = "latitude";
    public static String REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER = "longitude";
    public static String REGISTER_SENSOR_POSITION_NAME_PARAMETER = "positionName";
    public static String REGISTER_SENSOR_POSITION_FIXED_PARAMETER = "fixed";
    public static String REGISTER_SENSOR_UOM_PARAMETER = "uom";
    /**
     * Optional: Must be used in case of category observation type
     */
    public static String REGISTER_SENSOR_CODESPACE_PARAMETER = "codeSpace";
    /**
     * Mandatory: Used in the observation template
     */
    public static String REGISTER_SENSOR_DEFAULT_RESULT_VALUE = "defaultResultValue";

    public static String GET_OBSERVATION_BY_ID_SERVICE_PARAMETER = "service";
    public static String GET_OBSERVATION_BY_ID_VERSION_PARAMETER = "version";
    public static String GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER = "ObservationID";
    public static String GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER = "responseFormat";
    public static String GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER = "responseMode";
    public static String GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER = "resultModel";

    public String buildGetCapabilitiesRequest(ParameterContainer parameters) throws OXFException;

    public String buildGetObservationRequest(ParameterContainer parameters) throws OXFException;

    public String buildGetObservationByIDRequest(ParameterContainer parameters) throws OXFException;

    public String buildDescribeSensorRequest(ParameterContainer parameters) throws OXFException;

    public String buildGetFeatureOfInterestRequest(ParameterContainer parameters) throws OXFException;

    public String buildInsertObservation(ParameterContainer parameters) throws OXFException;

    /**
     * Builds a RegisterSensor request and returns it.
     * A SensorML file can either be passed along or a set of parameters is used to create one.
     * @throws OXFException 
     */
    public String buildRegisterSensor(ParameterContainer parameters) throws OXFException;

}