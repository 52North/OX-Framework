package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.Map;

import javax.print.attribute.standard.MediaSize.ISO;

import net.opengis.sensorML.x101.SystemDocument;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;

/**
 * TODO NEEDS TESTING!!!
 * 
 * SOSWrapper wraps all SOS operations implemented in SOSAdapter class.
 * 
 * @author Eric
 * @see SOSAdapter
 */
public class SOSWrapper {

	// TODO MOVE CONSTANTS TO ISOSRequestBuilder
	public static final String GET_OBSERVATION_SRS_NAME_PARAMETER = "srsName";
	public static final String GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER = "srsName";
	
	private static final String SERVICE_TYPE = "SOS"; // name of the service
	private String serviceBaseUrl; // base url of the service
	private ServiceDescriptor serviceDescriptor; // GetCapabilities specific description of the service	

//	// TEMPORARY TEST !!! SHALL BE MOVED TO UNIT TEST !!!
//	public static void main(String[] args) {
//		try {
//			System.out.print("Test GetCapabilities... ");
//			SOSWrapper sw = SOSWrapper.createFromCapabilities("http://v-swe.uni-muenster.de:8080/WeatherSOS/sos", "1.0.0");
//			// System.out.println(sw.serviceDescriptor.toString());
//			System.out.println("Test GetCapabilities done!\n");
//			
//			/*System.out.print("Test DescribeSensor... ");
//			OperationResult or = sw.doDescribeSensor(new DescribeSensorParamterBuilder_v100("urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1", DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML));
//			System.out.println(or.toString());
//			System.out.print("Test DescribeSensor done!\n");
//			
//			System.out.print("Test GetObservation...");
//			OperationResult or = sw.doGetObservation(new GetObservationParameterBuilder_v100("TEMPERATURE", "urn:ogc:def:phenomenon:OGC:1.0.30:temperature", "text/xml;subtype=\"om/1.0.0\"").addObservedProperty("urn:ogc:def:phenomenon:OGC:1.0.30:waterlevel1"));
//			System.out.println(or.toString());
//			System.out.println("Test GetObservation done!");
//			
//			TODO ? System.out.print("Test GetObservationById...");
//			OperationResult or = sw.doGetObservationById(new GetObservationByIdParameterBuilder_v100("1012", "text/xml;subtype=\"om/1.0.0\""));
//			System.out.println(or.toString());
//			System.out.println("Test GetObservationById done!");
//			
//			System.out.print("Test GetFeatureOfInterest... ");
//			OperationResult or = sw.doGetFeatureOfInterest(new GetFeatureOfInterestParameterBuilder_v100("foi_1001", GET_FOI_ID_PARAMETER));
//			System.out.println(or.toString());
//			System.out.print("Test GetFeatureOfInterest  done!");
//			
//			System.out.print("Test GetFeatureOfInterest... ");
//			OperationResult or = sw.doGetFeatureOfInterest(new GetFeatureOfInterestParameterBuilder_v100("foi_1001", GET_FOI_ID_PARAMETER).addEventTime(""));
//			System.out.println(or.toString());
//			System.out.print("Test GetFeatureOfInterest  done!");*/
//
//			// System.out.print("Test InsertObservation... ");
//			
//	    	// insert.setAssignedSensorId(sensorId);
//	    	//insert.setObservation(observationType); // see sosrequestbuilder
//	    	// add sampling time INSERT_OBSERVATION_SAMPLING_TIME see sosrequestbuilder
//	    	// procedure
//	    	// observed property
//	    	// INSERT_OBSERVATION_FOI_ID_PARAMETER
//	    	// INSERT_OBSERVATION_NEW_FOI_NAME | INSERT_OBSERVATION_NEW_FOI_DESC | INSERT_OBSERVATION_NEW_FOI_POSITION
//	    	// INSERT_OBSERVATION_POSITION_SRS
//	    	
//	    	// CATEGORY: INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, INSERT_OBSERVATION_VALUE_PARAMETER
//	    	// MEASUREMENT: INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, INSERT_OBSERVATION_VALUE_PARAMETER
//	    	
//			/*ObservationBuilder obsBuilder = new ObservationBuilder(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION);
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME, "2009-02-26T23:44:15+00:00");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER, "STATIONID");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME, "STATIONID");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC, "waterpoint for humans (@ID@)".replaceAll("@ID@", "STATIONID"));
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION, 7.0d + " " + 52.0d);
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS, "4326");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, "urn:ogc:def:phenomenon:OGC:0.1:WaterPointState");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER, "urn:ogc:object:feature:Sensor:human-sensor-web:reporter:0815_23-42_005_invaliddata");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER, "ALL_OKAY");
//			obsBuilder.addParameter(ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, "http://example.com/myCodespace");
//	    	InsertObservationParameterBuilder_v100 insertObsBuilder = new InsertObservationParameterBuilder_v100("urn:ogc:object:feature:Sensor:human-sensor-web:reporter:0815_23-42_005_invaliddata", obsBuilder);
//			OperationResult or = sw.doInsertObservation(insertObsBuilder);
//			System.out.println(or.toString());
//			System.out.print("Test InsertObservation  done!");*/
//			
//			// REGISTER_SENSOR_ML_DOC_PARAMETER, REGISTER_SENSOR_OBSERVATION_TEMPLATE, REGISTER_SENSOR_OBSERVATION_TYPE
//			
//			System.out.print("Test RegisterSensor... ");
//			
//			SensorDescriptionBuilder sdb = new SensorDescriptionBuilder();
//			sdb.setSensorId("ifgi-sensor-5");
//			sdb.setPosition("TestArea_5200_0797", true, 52.0, 7.97);
//			sdb.setObservedProperty("urn:ogc:def:phenomenon:OGC:1.0.30:waterlevel");
//			sdb.setSensorObservationType(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
//			sdb.setUnitOfMeasurement("mm");
//			
//			SystemDocument sysDoc = sdb.createRegisterSensorDocument();
//
//			RegisterSensorParameterBuilder_v100 builder = RegisterSensorParameterBuilder_v100.createBuilderWithType(sysDoc, ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT, "mm");
//			OperationResult or = sw.doRegisterSensor(builder);
//			System.out.println(or.toString());
//			System.out.print("Test RegisterSensor  done!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Constructs a wrapper for a certain SOS and defines GetCapabilities specific metadata of the service.
	 * 
	 * @param capabilities Specific description of the service.
	 * @param serviceBaseUrl Base url of the service.
	 */
	private SOSWrapper(String serviceBaseUrl, ServiceDescriptor capabilities) {
		this.serviceBaseUrl = serviceBaseUrl;
		this.serviceDescriptor = capabilities;
	}
	
	/**
	 * Calls the service's capability description request resulting in returning a SOSWrapper containing the capability description.
	 * 
	 * @param url Location of the SOS service.
	 * @param acceptVersion Compatible specification versions.
	 * @return Instance of a SOSWrapper for a certain SOS service.
	 * @throws ExceptionReport
	 * @throws OXFException
	 */
	public static SOSWrapper createFromCapabilities(String url, String acceptVersion) throws ExceptionReport, OXFException {
		ServiceDescriptor capabilities = doGetCapabilities(url, acceptVersion);
		return new SOSWrapper(url, capabilities);
	}
	
	/**
	 * Requests and returns the SOS capability description.
	 * 
	 * @param url Location of the SOS service.
	 * @param acceptVersion Compatible specification versions.
	 * @return ServiceDescriptor filled with data resulting from the GetCapabilities request.
	 * @throws ExceptionReport
	 * @throws OXFException
	 */
	public static ServiceDescriptor doGetCapabilities(String url, String acceptVersion) throws ExceptionReport, OXFException {
		SOSAdapter adapter = new SOSAdapter(acceptVersion);
		return adapter.initService(url);
	}

	/**
	 * Requests and returns the SOS sensor description.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doDescribeSensor(DescribeSensorParamterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.DESCRIBE_SENSOR)) != null) {
			// retrieve parameter list of mandatory parameters
			Map<String, String> parameters = builder.getParameters();
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, parameters.get(DESCRIBE_SENSOR_OUTPUT_FORMAT));
			// TODO SENSOR_ID or PROCEDURE 
			// parameterContainer.addParameterShell(DESCRIBE_SENSOR_SENSOR_ID_PARAMETER, parameters.get(DESCRIBE_SENSOR_SENSOR_ID_PARAMETER));
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, parameters.get(DESCRIBE_SENSOR_PROCEDURE_PARAMETER));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.DESCRIBE_SENSOR + "\" not supported by the SOS!");
	}
	
	/**
	 * Requests an observation.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservation(GetObservationParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION)) != null) {
			// retrieve parameter list of mandatory and optional parameters
			Map<String, Object> parameters = builder.getParameters();
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(GET_OBSERVATION_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(GET_OBSERVATION_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, (String) parameters.get(GET_OBSERVATION_OFFERING_PARAMETER));
			ParameterShell observedPropertyParameterList = (ParameterShell) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
			parameterContainer.addParameterShell(observedPropertyParameterList);
			parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
			// optional parameters from builder
			if (parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER));
			if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
				ParameterShell eventTimeParameterList = (ParameterShell) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER);
				parameterContainer.addParameterShell(eventTimeParameterList);
			}
			if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
				ParameterShell procedureParameterList = (ParameterShell) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER);
				parameterContainer.addParameterShell(procedureParameterList);
			}
			if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, (String) parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
			if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_PARAMETER));
			if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER));
			if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION + "\" not supported by the SOS!");
	}
	
	/**
	 * Requests the registration of a sensor.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doRegisterSensor(RegisterSensorParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.REGISTER_SENSOR)) != null) {
			// retrieve parameter list of mandatory parameters
			Map<String, String> parameters = builder.getParameters();
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, parameters.get(REGISTER_SENSOR_ML_DOC_PARAMETER));
			if (parameters.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE) != null) {
				parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE, parameters.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE));
			} else {
				parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TYPE, parameters.get(REGISTER_SENSOR_OBSERVATION_TYPE));
			}
			// check the necesserity of the following one
			if (parameters.get(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER) != null) {
				parameterContainer.addParameterShell(REGISTER_SENSOR_UOM_PARAMETER, parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
			}
				
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.REGISTER_SENSOR + "\" not supported by the SOS!");
		
	}

	/**
	 * Requests the insertion of an observation.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doInsertObservation(InsertObservationParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.INSERT_OBSERVATION)) != null) {
			// retrieve parameter list of mandatory parameters
			
			Map<String, String> parameters = builder.getParameters();
			
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(INSERT_OBSERVATION_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(INSERT_OBSERVATION_PROCEDURE_PARAMETER, parameters.get(INSERT_OBSERVATION_PROCEDURE_PARAMETER));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_TYPE, parameters.get(INSERT_OBSERVATION_TYPE));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_SAMPLING_TIME, parameters.get(INSERT_OBSERVATION_SAMPLING_TIME));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parameters.get(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_FOI_ID_PARAMETER, parameters.get(INSERT_OBSERVATION_FOI_ID_PARAMETER));
			// optional parameters from builder
			if (parameters.get(INSERT_OBSERVATION_NEW_FOI_NAME) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME, parameters.get(INSERT_OBSERVATION_NEW_FOI_NAME));
			if (parameters.get(INSERT_OBSERVATION_NEW_FOI_DESC) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC, parameters.get(INSERT_OBSERVATION_NEW_FOI_DESC));
			if (parameters.get(INSERT_OBSERVATION_NEW_FOI_POSITION) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION, parameters.get(INSERT_OBSERVATION_NEW_FOI_POSITION));
			if (parameters.get(INSERT_OBSERVATION_POSITION_SRS) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS, parameters.get(INSERT_OBSERVATION_POSITION_SRS));
			if (parameters.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, parameters.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE));
			if (parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE));
			if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null)
				parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.INSERT_OBSERVATION + "\" not supported by the SOS!");
	}

	/**
	 * Requests an observation by its id.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservationById(GetObservationByIdParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION_BY_ID)) != null) {
			// retrieve parameter list of mandatory and optional parameters
			Map<String, String> parameters = builder.getParameters();
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER));
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER));
			// optional parameters from builder
			if (parameters.get(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER));
			if (parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER));
			if (parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION_BY_ID + "\" not supported by the SOS!");
	}
	
	/**
	 * Requests a feature of interst.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetFeatureOfInterest(GetFeatureOfInterestParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST)) != null) {
			// retrieve parameter list of mandatory and optional parameters
			Map<String, String> parameters = builder.getParameters();
			// transfer parameters into ParameterContainer
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(GET_FOI_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(GET_FOI_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			if (parameters.get(GET_FOI_ID_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_FOI_ID_PARAMETER, parameters.get(GET_FOI_ID_PARAMETER));
			else
				parameterContainer.addParameterShell(GET_FOI_LOCATION_PARAMETER, parameters.get(GET_FOI_LOCATION_PARAMETER));
			// optional parameters from builder
			if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_FOI_EVENT_TIME_PARAMETER, parameters.get(GET_FOI_EVENT_TIME_PARAMETER));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION + "\" not supported by the SOS!");
	}

}
