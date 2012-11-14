package org.n52.oxf.sos.adapter.wrapper;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_FOI_LOCATION_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_DEFAULT_RESULT_VALUE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TEMPLATE;

import java.util.Map;

import org.n52.ows.request.MultimapRequestParameters;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.builder.GetFeatureOfInterestParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationByIdParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.swes.request.DescribeSensorParameters;

/**
 * SOSWrapper wraps all SOS operations implemented in SOSAdapter class.
 * 
 * @author Eric
 * @see SOSAdapter
 */
public class SOSWrapper {
    
    // XXX SOSWrapper is not capable of intercepting custom IRequestBuilder implementations yet!

	// TODO MOVE CONSTANTS TO ISOSRequestBuilder
	public static final String GET_OBSERVATION_SRS_NAME_PARAMETER = "srsName";
	public static final String GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER = "srsName";
	
	private static final String SERVICE_TYPE = "SOS"; // name of the service
	private ServiceDescriptor serviceDescriptor; // GetCapabilities specific description of the service
	
    /**
     * Creates a SOSWrapper instance by initiating a GetCapabilities request.
     * 
     * @param url Location of the SOS service.
     * @param acceptVersion Compatible specification version.
     * @return Instance of a SOSWrapper for a certain SOS service.
     * @throws ExceptionReport
     * @throws OXFException
     */
    public static SOSWrapper createFromCapabilities(String url, String acceptVersion) throws ExceptionReport, OXFException {
        ServiceDescriptor capabilities = doGetCapabilities(url, acceptVersion);
        return new SOSWrapper(capabilities);
    }

    /**
     * Requests and returns the SOS capability description.
     * 
     * @param url Location of the SOS service.
     * @param acceptVersion Compatible specification version.
     * @return ServiceDescriptor filled with data resulting from the GetCapabilities request.
     * @throws ExceptionReport
     * @throws OXFException
     */
    public static ServiceDescriptor doGetCapabilities(String url, String acceptVersion) throws ExceptionReport, OXFException {
        SOSAdapter adapter = new SOSAdapter(acceptVersion);
        return adapter.initService(url);
    }

	/**
	 * Constructs a wrapper for a certain SOS and defines GetCapabilities specific metadata of the service.
	 * 
	 * @param serviceDesciptor Specific description of the service.
	 * @param serviceBaseUrl Base url of the service.
	 */
	protected SOSWrapper(ServiceDescriptor serviceDesciptor) {
		this.serviceDescriptor = serviceDesciptor;
	}
	
	/**
     * Performs a DescribeSensor request.
     * 
     * @param parameters parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    public OperationResult doDescribeSensor(DescribeSensorParameters parameters) throws OXFException, ExceptionReport {
        SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
        // if describe sensor operation is defined
        if (isDescribeSensorDefined(operationsMetadata)) {
            Operation operation = operationsMetadata.getOperationByName(SOSAdapter.DESCRIBE_SENSOR);    
            ParameterContainer parameterContainer = createParameterContainerForDoDescribeSensor(parameters);
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
	boolean isDescribeSensorDefined(OperationsMetadata operationsMetadata) {
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
	ParameterContainer createParameterContainerForDoDescribeSensor(MultimapRequestParameters parameters) throws OXFException, ExceptionReport {
		ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		String procedure = parameters.getSingleValue(DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedure);
		String outputFormat = parameters.getSingleValue(DESCRIBE_SENSOR_OUTPUT_FORMAT);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
		return parameterContainer;
	}

    private ParameterContainer createParameterContainerWithCommonServiceParameters() throws OXFException {
        ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, SERVICE_TYPE);
		parameterContainer.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, serviceDescriptor.getVersion());
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
	boolean isGetObservationDefined(OperationsMetadata operationsMetadata) {
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
	ParameterContainer createParameterContainerForGetOservation(Map <String, Object> parameters) throws OXFException, ExceptionReport {
	    ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, (String) parameters.get(GET_OBSERVATION_OFFERING_PARAMETER));
		parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(GET_OBSERVATION_SRS_NAME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
			parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_EVENT_TIME_PARAMETER));
		}
		if (parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
			parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_PROCEDURE_PARAMETER));
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
     * @param parameters parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    public OperationResult doRegisterSensor(RegisterSensorParameters parameters) throws OXFException, ExceptionReport {
        // wrapped SOSAdapter instance
        SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        OperationsMetadata operationsMetadata = serviceDescriptor.getOperationsMetadata();
        if (isRegisterSensorDefined(operationsMetadata)) {
            Operation operation = operationsMetadata.getOperationByName(SOSAdapter.REGISTER_SENSOR);
            ParameterContainer parameterContainer = createParameterContainerForRegisterSensor(parameters);
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
	boolean isRegisterSensorDefined(OperationsMetadata operationsMetadata) {
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
	ParameterContainer createParameterContainerForRegisterSensor(MultimapRequestParameters parameters) throws OXFException, ExceptionReport {
	    ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		String smlDoc = parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
        parameterContainer.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, smlDoc);
		String template = parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
        parameterContainer.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE, template);
		
		// parameters not from builder but importer TODO CHECK
        if (parameters.contains(REGISTER_SENSOR_DEFAULT_RESULT_VALUE)) {
            String defaultResult = parameters.getSingleValue(REGISTER_SENSOR_DEFAULT_RESULT_VALUE);
			parameterContainer.addParameterShell(REGISTER_SENSOR_DEFAULT_RESULT_VALUE, defaultResult);
		}
		
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
	boolean isInsertObservationDefined(OperationsMetadata operationsMetadata) {
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
	 */
	ParameterContainer createParameterContainerForInsertObservation(Map<String, String> parameters) throws OXFException, ExceptionReport {
	    ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
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
	boolean isGetObservationByIdDefined(OperationsMetadata operationsMetadata) {
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
	ParameterContainer createParameterContainerForGetObservationById(Map<String, String> parameters) throws OXFException, ExceptionReport {
	    ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
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
		if (isGetFeatureOfInterestDefined(operationsMetadata)) {
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
	boolean isGetFeatureOfInterestDefined(OperationsMetadata operationsMetadata) {
		return operationsMetadata.getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST) != null;
	}
	
	ParameterContainer createParameterContainerForGetFeatureOfInterest(Map<String, String> parameters) throws OXFException, ExceptionReport {
	    ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
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

    /**
     * Return the GetCapabilities specific description of the service.
     * 
     * @return service description  
     */
    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

}
