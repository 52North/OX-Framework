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
 * SOSWrapper wraps all SOS operations.
 * 
 * @author Eric
 */
public class SOSWrapper {

	private static final String SERVICE_TYPE = "SOS"; // name of the service
	private String serviceBaseUrl; // base url of the service
	private ServiceDescriptor serviceDescriptor; // GetCapabilities specific description of the service
	
	/**
	 * Calls the service's capability description request with resulting in returning a SOSWrapper containing the capability description.
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
	 * @return request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doDescribeSensor(DescribeSensorParamterBuilder_v100 builder) throws OXFException, ExceptionReport {
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.DESCRIBE_SENSOR)) != null) {
			Map<String, String> parameters = builder.getParameters();
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, parameters.get(DESCRIBE_SENSOR_OUTPUT_FORMAT));
			// TODO create constant in ISOSRequestBuilder?
			parameterContainer.addParameterShell("sensorId", parameters.get("sensorId"));
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.DESCRIBE_SENSOR + "\" not supported by the SOS!");
	}
	
	/**
	 * Requests an observation.
	 * 
	 * @param builder Request parameter assembler.
	 * @return Request result (observation)
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservation(GetObservationParamterBuilder_v100 builder) throws OXFException, ExceptionReport {
		SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
		Operation operation = null;
		// if there are operations defined
		if ((operation = operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION)) != null) {
			Map<String, Object> parameters = builder.getParameters();
			ParameterContainer parameterContainer = new ParameterContainer();
			parameterContainer.addParameterShell(GET_OBSERVATION_SERVICE_PARAMETER, SERVICE_TYPE);
			parameterContainer.addParameterShell(GET_OBSERVATION_VERSION_PARAMETER, serviceDescriptor.getVersion());
			// mandatory parameters from builder
			parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, (String) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER));
			Vector<String> parameterList = (Vector<String>) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
			for (int i = 0; i < parameterList.size(); i++)
				parameterContainer.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, parameterList.get(i));
			parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
			// optional parameters from builder
			// TODO create constant in ISOSRequestBuilder?
			if (parameters.get("srsName") != null)
				parameterContainer.addParameterShell("srsName", (String) parameters.get("srsName"));
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
				parameterList = (Vector<String>) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER);
				for (int i = 0; i < parameterList.size(); i++)
					parameterContainer.addParameterShell(GET_OBSERVATION_EVENT_TIME_PARAMETER, parameterList.get(i));
			}
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
				parameterList = (Vector<String>) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER);
				for (int i = 0; i < parameterList.size(); i++)
					parameterContainer.addParameterShell(GET_OBSERVATION_PROCEDURE_PARAMETER, parameterList.get(i));
			}
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, (String) parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_PARAMETER));
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER));
			if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null)
				parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER));
			
			
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION + "\" not supported by the SOS!");
	}
	
	// TODO implement
	public OperationResult doRegisterSensor() {
		return null;
	}

	// TODO implement
	public OperationResult doInsertObservation() {
		return null;
	}

	// TODO implement
	public OperationResult doGetObservationById() {
		return null;
	}
	
	// TODO implement
	public OperationResult doGetFeatureOfInterest() {
		return null;
	}
}
