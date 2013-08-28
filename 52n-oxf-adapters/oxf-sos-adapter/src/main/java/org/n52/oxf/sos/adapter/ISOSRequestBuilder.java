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
	
	/**
	 * Currently supported by SOS 2.0 request builder only.<br />
	 * Specifies the communication binding uses by the SOS instances the builder will create requests for.
	 * Allowed values: <ul>
	 * <li><b>KVP</b> &rarr; Key Value Pair: all request are executed via HTTP GET &rarr; only read operations are supported.</li>
	 * <li><b>POX</b> &rarr; Plain Old XML: all requests are executed via HTTP POST using OGC XML encoding.</li>
	 * <li><b>SOAP</b>: all requests are executed via HTTP POST using OGC XML encoding in soap:envelopes.</li>
	 * </ul>
	 */
	String BINDING = "binding";
	
	public enum Binding {
		POX, KVP, SOAP;
	}

	String GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER = "updateSequence";
	String GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER = "AcceptVersions";
	String GET_CAPABILITIES_SECTIONS_PARAMETER = "sections";
	String GET_CAPABILITIES_SERVICE_PARAMETER = "service";

	String GET_OBSERVATION_SERVICE_PARAMETER = "service";
	String GET_OBSERVATION_VERSION_PARAMETER = "version";
	String GET_OBSERVATION_OFFERING_PARAMETER = "offering";
	String GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
	String GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER = "responseFormat";
	String GET_OBSERVATION_EVENT_TIME_PARAMETER = "eventTime"; // SOS 1.0
	String GET_OBSERVATION_TEMPORAL_FILTER_PARAMETER = "temporalFilter"; // SOS 2.0
	String GET_OBSERVATION_PROCEDURE_PARAMETER = "procedure";
	String GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER = "featureOfInterest";
	String GET_OBSERVATION_SPATIAL_FILTER_PARAMETER = "spatialFilter"; // SOS 2.0
	String GET_OBSERVATION_RESULT_PARAMETER = "result"; // SOS 1.0
	String GET_OBSERVATION_RESULT_MODEL_PARAMETER = "resultModel"; // SOS 1.0
	String GET_OBSERVATION_RESPONSE_MODE_PARAMETER = "responseMode"; // SOS 1.0
	String GET_OBSERVATION_SRS_NAME_PARAMETER = "srsName";

	String DESCRIBE_SENSOR_SERVICE_PARAMETER = "service";
	String DESCRIBE_SENSOR_VERSION_PARAMETER = "version";
	String DESCRIBE_SENSOR_PROCEDURE_PARAMETER = "procedure";
	String DESCRIBE_SENSOR_OUTPUT_FORMAT = "outputFormat";
	String DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT = "procedureDescriptionFormat"; // SOS 2.0

	String GET_FOI_SERVICE_PARAMETER = "service";
	String GET_FOI_VERSION_PARAMETER = "version";
	String GET_FOI_EVENT_TIME_PARAMETER = "eventTime";
	String GET_FOI_ID_PARAMETER = "featureOfInterestId";
	String GET_FOI_LOCATION_PARAMETER = "location";

	String INSERT_OBSERVATION_SERVICE_PARAMETER = "service";
	String INSERT_OBSERVATION_VERSION_PARAMETER = "version";
	String INSERT_OBSERVATION_FOI_ID_PARAMETER = "featureOfInterestID";
	String INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER = "newFoiID";
	String INSERT_OBSERVATION_NEW_FOI_NAME = "newFoiName";
	String INSERT_OBSERVATION_NEW_FOI_DESC = "newFoiDesc";
	/**
	 * SOS 2.0 Optional: Defines the id of the optional parent feature of the 
	 * feature of interest of an observation. If not specified for a "new" 
	 * feature of interest "unknown" will be used.
	 */
	String INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID = "parentFeatureId";
	String INSERT_OBSERVATION_NEW_FOI_POSITION = "newFoiPosition";
	String INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
	String INSERT_OBSERVATION_SAMPLING_TIME = "samplingTime";
	String INSERT_OBSERVATION_VALUE_PARAMETER = "value";
	String INSERT_OBSERVATION_POSITION_SRS = "srsPosition";
	String INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE = "catObsCodespace";
	String INSERT_OBSERVATION_TYPE_TEXT = "text";
	String INSERT_OBSERVATION_TYPE_MEASUREMENT = "measurement";
	String INSERT_OBSERVATION_TYPE = "type";
	String INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE = "resultUom";
	String INSERT_OBSERVATION_NEW_FOI_POSITION_SRS = "insertObSRS";
	String INSERT_OBSERVATION_PROCEDURE_PARAMETER = "procedure";
	String INSERT_OBSERVATION_SENSOR_ID_PARAMETER = "sensorid";
	String INSERT_OBSERVATION_TYPE_COUNT = "count";
	String INSERT_OBSERVATION_TYPE_TRUTH = "truth";
	String INSERT_OBSERVATION_TYPE_TEMPORAL = "temporal";
	String INSERT_OBSERVATION_TYPE_GEOMETRY = "geometry";
	String INSERT_OBSERVATION_TYPE_COMPLEX = "complex";
	/**
	 * Supported by SOS 2.0 request builder only
	 */
	String INSERT_OBSERVATION_TYPE_CATEGORY = "category";
	/**
	 * SOS 2.0 Mandatory: Defines the time when the observation <i>occurred</i>. 
	 */
	String INSERT_OBSERVATION_PHENOMENON_TIME = "phenomenonTime";
	/**
	 * SOS 2.0 Mandatory: Defines the time when the observation is <i>performed</i>.
	 */
	String INSERT_OBSERVATION_RESULT_TIME = "resultTime";
	/*
	 * Mandatory SOS 2.0 parameter: specifies the offering(s) the observation(s)
	 * of the InsertObservation shall be added to.
	 */
	String INSERT_OBSERVATION_OFFERINGS_PARAMETER = "offerings";

	String REGISTER_SENSOR_SERVICE_PARAMETER = "service";
	String REGISTER_SENSOR_VERSION_PARAMETER = "version";
	String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";
	String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";
	String REGISTER_SENSOR_OBSERVATION_TYPE = "type";
	String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";
	String REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT = "measurement";
	String REGISTER_SENSOR_ID_PARAMETER = "id";
	String REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER = "observedProperty";
	String REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER = "latitude";
	String REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER = "longitude";
	String REGISTER_SENSOR_POSITION_NAME_PARAMETER = "positionName";
	String REGISTER_SENSOR_POSITION_FIXED_PARAMETER = "fixed";
	String REGISTER_SENSOR_UOM_PARAMETER = "uom";
	/**
	 * Required for SOS 2.0 InsertSensor requests. This parameter specifies the
	 * format of the procedure description in the InsertSensor request. 
	 */
	String REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER = "procedureDescriptionFormat";
	/**
	 * Required for SOS 2.0 InsertSensor requests. This parameter specifies the
	 * allowed feature types for observations of the inserted sensor.
	 */
	String REGISTER_SENSOR_FEATURE_TYPE_PARAMETER = "featureType";
	/**
	 * Optional: Must be used in case of category observation type
	 */
	String REGISTER_SENSOR_CODESPACE_PARAMETER = "codeSpace";
	/**
	 * Mandatory: Used in the observation template
	 */
	String REGISTER_SENSOR_DEFAULT_RESULT_VALUE = "defaultResultValue";

	String GET_OBSERVATION_BY_ID_SERVICE_PARAMETER = "service";
	String GET_OBSERVATION_BY_ID_VERSION_PARAMETER = "version";
	String GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER = "ObservationID";
	String GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER = "responseFormat";
	String GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER = "responseMode";
	String GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER = "resultModel";
	String GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER = "srsName";

	String buildGetCapabilitiesRequest(ParameterContainer parameters) throws OXFException;

	String buildGetObservationRequest(ParameterContainer parameters) throws OXFException;

	String buildGetObservationByIDRequest(ParameterContainer parameters) throws OXFException;

	String buildDescribeSensorRequest(ParameterContainer parameters) throws OXFException;

	String buildGetFeatureOfInterestRequest(ParameterContainer parameters) throws OXFException;

	String buildInsertObservationRequest(ParameterContainer parameters) throws OXFException;

	/**
	 * Builds a RegisterSensor request and returns it.
	 * A SensorML file can either be passed along or a set of parameters is used to create one.
	 * @throws OXFException
	 */
	String buildRegisterSensorRequest(ParameterContainer parameters) throws OXFException;
}