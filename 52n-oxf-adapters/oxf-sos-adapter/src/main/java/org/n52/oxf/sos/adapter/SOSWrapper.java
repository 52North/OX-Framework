package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.util.Map;
import java.util.Vector;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;

/**
 * SOSWrapper wraps all SOS operations implemented in SOSAdapter class.
 * 
 * @author Eric
 * @see SOSAdapter
 */
public class SOSWrapper {

	// TODO MOVE CONSTANTS TO ISOSRequestBuilder
	public static final String DESCRIBE_SENSOR_SENSOR_ID_PARAMETER = "sensorId";
	public static final String GET_OBSERVATION_SRS_NAME_PARAMETER = "srsName";
	public static final String GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER = "srsName";
	public static final String REGISTER_SENSOR_SENSOR_DESCRIPTION_PARAMETER = "sensorDescription";
	public static final String INSERT_OBSERVATION_OBSERVATION_PARAMETER = "observation";
	
	private static final String SERVICE_TYPE = "SOS"; // name of the service
	private String serviceBaseUrl; // base url of the service
	private ServiceDescriptor serviceDescriptor; // GetCapabilities specific description of the service
	
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
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_SENSOR_ID_PARAMETER, parameters.get(DESCRIBE_SENSOR_SENSOR_ID_PARAMETER));
			
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
			Vector<String> parameterList = (Vector<String>) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
			for (int i = 0; i < parameterList.size(); i++)
				parameterContainer.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parameterList.get(i));
			parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
			// optional parameters from builder
			if (parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER));
			if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
				parameterList = (Vector<String>) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER);
				for (int i = 0; i < parameterList.size(); i++)
					parameterContainer.addParameterShell(GET_OBSERVATION_EVENT_TIME_PARAMETER, parameterList.get(i));
			}
			if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
				parameterList = (Vector<String>) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER);
				for (int i = 0; i < parameterList.size(); i++)
					parameterContainer.addParameterShell(GET_OBSERVATION_PROCEDURE_PARAMETER, parameterList.get(i));
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
			parameterContainer.addParameterShell(REGISTER_SENSOR_SENSOR_DESCRIPTION_PARAMETER, parameters.get(REGISTER_SENSOR_SENSOR_DESCRIPTION_PARAMETER));
			parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE, parameters.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE));
			
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
			parameterContainer.addParameterShell(INSERT_OBSERVATION_SENSOR_ID_PARAMETER, parameters.get(INSERT_OBSERVATION_SENSOR_ID_PARAMETER));
			parameterContainer.addParameterShell(INSERT_OBSERVATION_OBSERVATION_PARAMETER, parameters.get(INSERT_OBSERVATION_OBSERVATION_PARAMETER));
			
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
			parameterContainer.addParameterShell(GET_FOI_SERVICE_PARAMETER, serviceDescriptor.getVersion());
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
