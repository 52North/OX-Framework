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
package org.n52.oxf.sos.adapter.wrapper;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.*;

import java.util.Map;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.builder.GetFeatureOfInterestParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationByIdParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;
import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.n52.oxf.swes.request.DescribeSensorParameters;

/**
 * SOSWrapper wraps all SOS operations implemented in SOSAdapter class.
 * 
 * @author Eric
 * @see SOSAdapter
 */
public class SOSWrapper {
    
    // XXX SOSWrapper is not capable of intercepting custom IRequestBuilder implementations yet!

	// name of the service
	private static final String SERVICE_TYPE = "SOS";
	
	// GetCapabilities specific description of the service
	private final ServiceDescriptor serviceDescriptor; 
	
	// Binding to use (SOS 2.0 specific)
	private final Binding binding;
	
    protected SOSWrapper(final ServiceDescriptor serviceDescriptor, final Binding binding) {
		this.serviceDescriptor = serviceDescriptor;
		this.binding = binding;
	}
	
	/**
     * Performs a DescribeSensor request.
     * 
     * @param parameters parameter assembler
     * @return Request result
     * @throws OXFException
     * @throws ExceptionReport
     */
    public OperationResult doDescribeSensor(final DescribeSensorParameters parameters) throws OXFException, ExceptionReport {
        final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        if (checkOperationAvailability(DESCRIBE_SENSOR)) {
            final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(DESCRIBE_SENSOR);    
            final ParameterContainer parameterContainer = createParameterContainerForDoDescribeSensor(parameters);
            addBinding(parameterContainer);
            return adapter.doOperation(operation, parameterContainer);
        } else {
            throw new OXFException("Operation: \"" + DESCRIBE_SENSOR + "\" not supported by the SOS!");
        }
    }
	
    private ParameterContainer createParameterContainerWithCommonServiceParameters() throws OXFException {
        final ParameterContainer parameterContainer = new ParameterContainer();
		parameterContainer.addParameterShell(SERVICE, SERVICE_TYPE);
		parameterContainer.addParameterShell(VERSION, serviceDescriptor.getVersion());
        return parameterContainer;
    }
    
	private ParameterContainer createParameterContainerForDoDescribeSensor(final DescribeSensorParameters parameters) throws OXFException, ExceptionReport {
		final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		final String procedure = parameters.getSingleValue(DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, procedure);
		final String outputFormat = parameters.getSingleValue(DESCRIBE_SENSOR_OUTPUT_FORMAT);
        parameterContainer.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, outputFormat);
		return parameterContainer;
	}
	
	/**
	 * Requests observation(s).
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetObservation(final GetObservationParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_OBSERVATION)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_OBSERVATION);
			final ParameterContainer parameterContainer = createParameterContainerForGetOservation(builder.getParameters());
			addBinding(parameterContainer);
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION + "\" not supported by the SOS!");
		}
	}
	
	private ParameterContainer createParameterContainerForGetOservation(final Map <String, Object> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, (String) parameters.get(GET_OBSERVATION_OFFERING_PARAMETER));
		parameterContainer.addParameterShell((ParameterShell) parameters.get(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, (String) parameters.get(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER, (String) parameters.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER));
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
    public OperationResult doRegisterSensor(final RegisterSensorParameters parameters) throws OXFException, ExceptionReport {
        // wrapped SOSAdapter instance
        final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
        if (checkOperationAvailability(REGISTER_SENSOR)) {
            final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(REGISTER_SENSOR);
            final ParameterContainer parameterContainer = createParameterContainerForRegisterSensor(parameters);
            return adapter.doOperation(operation, parameterContainer);
        } else {
            throw new OXFException("Operation: \"" + REGISTER_SENSOR + "\" not supported by the SOS!");
        }
    }
    
    /**
	 * @throws OXFException 
     * @throws ExceptionReport 
     * @see {@link #doRegisterSensor(RegisterSensorParameters)}
	 */
	public OperationResult doInsertSensor(final InsertSensorParameters insertSensorParameters) throws OXFException, ExceptionReport
	{
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		if (checkOperationAvailability(INSERT_SENSOR)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(INSERT_SENSOR);
			final ParameterContainer parameterContainer = getParameterContainer(insertSensorParameters);
			addBinding(parameterContainer);
			return adapter.doOperation(operation, parameterContainer);
		}
		else {
			throw new OXFException("Operation: '" + INSERT_SENSOR + "' not supported by the SOS instance!");
		}
	}
	
	private ParameterContainer getParameterContainer(final InsertSensorParameters insertSensorParameters) throws OXFException
	{
		final ParameterContainer paramContainer = createParameterContainerWithCommonServiceParameters();
		paramContainer.addParameterShell(
				REGISTER_SENSOR_ML_DOC_PARAMETER,
				insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER,
				insertSensorParameters.getSingleValue(InsertSensorParameters.PROCEDURE_DESCRIPTION_FORMAT));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVABLE_PROPERTIES).size()]));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVATION_TYPE,
				insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVATION_TYPES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.OBSERVATION_TYPES).size()]));
		paramContainer.addParameterShell(
				REGISTER_SENSOR_FEATURE_TYPE_PARAMETER,
				insertSensorParameters.getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES)
				.toArray(new String[insertSensorParameters.getAllValues(InsertSensorParameters.FEATURE_OF_INTEREST_TYPES).size()]));
		return paramContainer;
	}

	/**
	 * 
	 * @param sensorId
	 * @throws ExceptionReport 
	 */
	public OperationResult doDeleteSensor(final String sensorId) throws OXFException, ExceptionReport
	{
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		if (checkOperationAvailability(DELETE_SENSOR)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(DELETE_SENSOR);
			final ParameterContainer pc = new ParameterContainer();
			pc.addParameterShell(ISOSRequestBuilder.SERVICE, "SOS");
			pc.addParameterShell(ISOSRequestBuilder.VERSION, serviceDescriptor.getVersion());
			pc.addParameterShell(ISOSRequestBuilder.DELETE_SENSOR_PROCEDURE, sensorId);
			addBinding(pc);
			return adapter.doOperation(operation, pc);
		}else {
			throw new OXFException("Operation: '" + DELETE_SENSOR + "' not supported by the SOS instance!");
		}
	}

	
	private void addBinding(final ParameterContainer parameterContainer) throws OXFException
	{
		if (binding != null) {
			parameterContainer.addParameterShell(BINDING, binding.name());
		}
	}
	
	private boolean checkOperationAvailability(final String operationName)
	{
		return serviceDescriptor.getOperationsMetadata().getOperationByName(operationName) != null;
	}

	private ParameterContainer createParameterContainerForRegisterSensor(final RegisterSensorParameters parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		parameterContainer.addParameterShell(
				REGISTER_SENSOR_ML_DOC_PARAMETER,
				parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER));
		parameterContainer.addParameterShell(
				REGISTER_SENSOR_OBSERVATION_TEMPLATE,
				parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE));
        if (parameters.contains(REGISTER_SENSOR_DEFAULT_RESULT_VALUE)) {
            final String defaultResult = parameters.getSingleValue(REGISTER_SENSOR_DEFAULT_RESULT_VALUE);
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
	public OperationResult doInsertObservation(final InsertObservationParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(INSERT_OBSERVATION)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(INSERT_OBSERVATION);
			final ParameterContainer parameterContainer = createParameterContainerForInsertObservation(builder.getParameters());
			addBinding(parameterContainer);
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + INSERT_OBSERVATION + "\" not supported by the SOS!");
		}
	}

	private ParameterContainer createParameterContainerForInsertObservation(final Map<String, String> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
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
	public OperationResult doGetObservationById(final GetObservationByIdParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_OBSERVATION_BY_ID)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_OBSERVATION_BY_ID);
			final ParameterContainer parameterContainer = createParameterContainerForGetObservationById(builder.getParameters());
			addBinding(parameterContainer);	
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION_BY_ID + "\" not supported by the SOS!");
		}
	}

	private ParameterContainer createParameterContainerForGetObservationById(final Map<String, String> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
		// mandatory parameters from builder
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER));
		parameterContainer.addParameterShell(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER, parameters.get(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER));
		// optional parameters from builder
		if (parameters.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER) != null) {
			parameterContainer.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER, parameters.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER));
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
	 * Requests a feature of interest.
	 * 
	 * @param builder parameter assembler
	 * @return Request result
	 * @throws OXFException
	 * @throws ExceptionReport
	 */
	public OperationResult doGetFeatureOfInterest(final GetFeatureOfInterestParameterBuilder_v100 builder) throws OXFException, ExceptionReport {
		// wrapped SOSAdapter instance
		final SOSAdapter adapter = new SOSAdapter(serviceDescriptor.getVersion());
		// if there are operations defined
		if (checkOperationAvailability(GET_FEATURE_OF_INTEREST)) {
			final Operation operation = serviceDescriptor.getOperationsMetadata().getOperationByName(GET_FEATURE_OF_INTEREST);
			final ParameterContainer parameterContainer = createParameterContainerForGetFeatureOfInterest(builder.getParameters());
			addBinding(parameterContainer);
			return adapter.doOperation(operation, parameterContainer);
		} else {
			throw new OXFException("Operation: \"" + GET_OBSERVATION + "\" not supported by the SOS!");
		}
	}
	
	private ParameterContainer createParameterContainerForGetFeatureOfInterest(final Map<String, String> parameters) throws OXFException, ExceptionReport {
	    final ParameterContainer parameterContainer = createParameterContainerWithCommonServiceParameters();
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
