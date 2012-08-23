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
	
	/**
	 * Return the GetCapabilities specific description of the service.
	 * 
	 * @return service description	
	 */
	public ServiceDescriptor getServiceDescriptor() {
		return serviceDescriptor;
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
		// if describe sensor operation is defined
		if (isDescribeSensorDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.DESCRIBE_SENSOR);	
			ParameterContainer parameterContainer = createParameterContainerForDoDescribeSensor(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + SOSAdapter.DESCRIBE_SENSOR + "\" not supported by the SOS!");
		}
	}
	
	/**
	 * Subroutine of doDescribeSensor, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isDescribeSensorDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.DESCRIBE_SENSOR) != null;
	}
	
	/**
	 * Subroutine of doDescribeSensor, which is used to reassemble the incoming parameters to fit the structure
	 * used by SOSAdapter.
	 * 
	 * @param parameters list of parameters
	 * @return reassembled set of parameters needed to call the opertion in the SOSAdapter
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	private ParameterContainer createParameterContainerForDoDescribeSensor(Map<String, String> parameters) throws OXFException, ExceptionReport {
		// transfer parameters into ParameterContainer
		ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
		// mandatory parameters from builder
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, parameters.get(DESCRIBE_SENSOR_PROCEDURE_PARAMETER)); // sensor id
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, parameters.get(DESCRIBE_SENSOR_OUTPUT_FORMAT));
		return parameterContainer;
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
		// if there are operations defined
		if (isGetObservationDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION);
			ParameterContainer parameterContainer = createParameterContainerForGetOservation(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION + "\" not supported by the SOS!");
		}
	}
	
	/**
	 * Subroutine of doGetObservation, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isGetObservationDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION) != null;
	}
	
	/**
	 * Subroutine of doGetObservations, which is used to reassemble the incoming parameters to fit the structure
	 * used by SOSAdapter.
	 * 
	 * @param parameters list of parameters
	 * @return reassembled set of parameters needed to call the opertion in the SOSAdapter
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	private ParameterContainer createParameterContainerForGetOservation(Map <String, Object> parameters) throws OXFException, ExceptionReport {
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
		if (parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
			ParameterShell eventTimeParameterList = (ParameterShell) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER);
			parameterContainer.addParameterShell(eventTimeParameterList);
		}
		if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
			ParameterShell procedureParameterList = (ParameterShell) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER);
			parameterContainer.addParameterShell(procedureParameterList);
		}
		if (parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, (String) parameters.get(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESULT_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESULT_MODEL_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESULT_MODEL_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_MODE_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_MODE_PARAMETER));
		}
		return parameterContainer;
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
		if (isRegisterSensorDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.REGISTER_SENSOR);
			ParameterContainer parameterContainer = createParameterContainerForRegisterSensor(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + SOSAdapter.REGISTER_SENSOR + "\" not supported by the SOS!");
		}
	}
	
	/**
	 * Subroutine of doRegisterSensor, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isRegisterSensorDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null;
	}

	/**
	 * Subroutine of doRegisterSensor, which is used to reassemble the incoming parameters to fit the structure
	 * used by SOSAdapter.
	 * 
	 * @param parameters list of parameters
	 * @return reassembled set of parameters needed to call the opertion in the SOSAdapter
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	private ParameterContainer createParameterContainerForRegisterSensor(Map<String, String> parameters) throws OXFException, ExceptionReport {
		// transfer parameters into ParameterContainer
		ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
		parameterContainer.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
		// mandatory parameters from builder
		parameterContainer.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, parameters.get(REGISTER_SENSOR_ML_DOC_PARAMETER));
		parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE, parameters.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE));				
		return parameterContainer;
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
		// if there are operations defined
		if (isInsertObservationDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.INSERT_OBSERVATION);
			ParameterContainer parameterContainer = createParameterContainerForInsertObservation(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.INSERT_OBSERVATION + "\" not supported by the SOS!");
	}

	/**
	 * Subroutine of doInsertObservation, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isInsertObservationDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null;
	}

	/**
	 * Subroutine of doInsertObservation, which is used to reassemble the incoming parameters to fit the structure
	 * used by SOSAdapter.
	 * 
	 * @param parameters list of parameters
	 * @return reassembled set of parameters needed to call the opertion in the SOSAdapter
	 * @throws OXFException
	 * @throws ExceptionReport
	 * 
	 * TODO ALL PARAMETERS USED TO DESCRIBE THE OBSERVATION ARE TAKEN FROM SOSADAPTER RESPECTIVELY
	 * SOSREQUESTBUILDER. THERE SHOULD BE A DEFINITION WHAT PARAMETERS ARE SUPPORTED AND WHICH ARE NOT!
	 */
	private ParameterContainer createParameterContainerForInsertObservation(Map<String, String> parameters) throws OXFException, ExceptionReport {
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
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_NAME) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_NAME, parameters.get(INSERT_OBSERVATION_NEW_FOI_NAME));
		}
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_DESC) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_DESC, parameters.get(INSERT_OBSERVATION_NEW_FOI_DESC));
		}
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_POSITION) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_NEW_FOI_POSITION, parameters.get(INSERT_OBSERVATION_NEW_FOI_POSITION));
		}
		if (parameters.get(INSERT_OBSERVATION_POSITION_SRS) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_POSITION_SRS, parameters.get(INSERT_OBSERVATION_POSITION_SRS));
		}
		if (parameters.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE, parameters.get(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE));
		}
		if (parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE));
		}
		if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
			parameterContainer.addParameterShell(INSERT_OBSERVATION_VALUE_PARAMETER, parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER));
		}
		return parameterContainer;
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
		// if there are operations defined
		if (isGetObservationByIdDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION_BY_ID);
				ParameterContainer parameterContainer = createParameterContainerForGetObservationById(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION_BY_ID + "\" not supported by the SOS!");
	}

	/**
	 * Subroutine of doGetObservationById, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isGetObservationByIdDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.GET_OBSERVATION_BY_ID) != null;
	}
	
	/**
	 * Subroutine of doGetObservationById, which is used to reassemble the incoming parameters to fit the structure
	 * used by SOSAdapter.
	 * 
	 * @param parameters list of parameters
	 * @return reassembled set of parameters needed to call the opertion in the SOSAdapter
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	private ParameterContainer createParameterContainerForGetObservationById(Map<String, String> parameters) throws OXFException, ExceptionReport {
		// transfer parameters into ParameterContainer
		ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_SERVICE_PARAMETER, SERVICE_TYPE);
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_VERSION_PARAMETER, serviceDescriptor.getVersion());
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER));
		}
		return parameterContainer;
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
		// if there are operations defined
		if (isGetFeatureOfInterstDefined(operationsMetadata)) {
			Operation operation = operationsMetadata.getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST);
			ParameterContainer parameterContainer = createParameterContainerForGetFeatureOfInterest(builder.getParameters());
			return adapter.doOperation(operation, parameterContainer);
		} else
			throw new OXFException("Operation: \"" + SOSAdapter.GET_OBSERVATION + "\" not supported by the SOS!");
	}
	
	/**
	 * Subroutine of doGetFeatureOfInterest, which is used to check the existence of that operation in the SOS.
	 * 
	 * @param operationsMetadata
	 * @return truth
	 */
	private boolean isGetFeatureOfInterstDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST) != null;
	}
	
	private ParameterContainer createParameterContainerForGetFeatureOfInterest(Map<String, String> parameters) throws OXFException, ExceptionReport {
		// transfer parameters into ParameterContainer
		ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(GET_FOI_SERVICE_PARAMETER, SERVICE_TYPE);
		parameterContainer.addParameterShell(GET_FOI_VERSION_PARAMETER, serviceDescriptor.getVersion());
		// mandatory parameters from builder
		if (parameters.get(GET_FOI_ID_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_FOI_ID_PARAMETER, parameters.get(GET_FOI_ID_PARAMETER));
		} else {
			parameterContainer.addParameterShell(GET_FOI_LOCATION_PARAMETER, parameters.get(GET_FOI_LOCATION_PARAMETER));
		}
		// optional parameters from builder
		if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_FOI_EVENT_TIME_PARAMETER, parameters.get(GET_FOI_EVENT_TIME_PARAMETER));
		}
		return parameterContainer;
	}

}
