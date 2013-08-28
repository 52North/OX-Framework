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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains attributes and methods to encode SOSOperationRequests as String in binding dependent format:<ul>
 * <li>POX &rarr; XML</li>
 * <li>SOAP &rarr; XML</li>
 * <li>KVP &rarr; query String</li>
 * </ul>
 * 
 * TODO: add java doc for each public method including mandatory and optional parameters like {@link SOSRequestBuilder_100}.
 * 
 * TODO: implement binding specific request creation
 *  using new to be implemented request builder hierarchy. This current implementation reflects the default implementation: POX
 * TODO: SOAP
 * TODO: KVP 
 * TODO: REST?
 */
public class SOSRequestBuilder_200 implements ISOSRequestBuilder {
	
	private final SOSRequestBuilder200POX poxBuilder = new SOSRequestBuilder200POX();
	
	private final SOSRequestBuilder200KVP kvpBuilder = new SOSRequestBuilder200KVP();
	
	private final SOSRequestBuilder200SOAP soapBuilder = new SOSRequestBuilder200SOAP();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SOSRequestBuilder_200.class);

    @Override
	public String buildGetCapabilitiesRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildGetCapabilitiesRequest(parameters);
    	}
    	throw new OXFException(String.format("Building GetCapabilities request not supported via binding '%s'!",binding.toString()));
    }

    @Override
	public String buildGetObservationRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildGetObservationRequest(parameters);
    	}
    	throw new OXFException(String.format("Building GetObservation request not supported via binding '%s'!",binding.toString()));
    }
    
    @Override
	public String buildGetObservationByIDRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildGetObservationByIDRequest(parameters);
    	}
    	throw new OXFException(String.format("Building GetObservationById request not supported via binding '%s'!",binding.toString()));
    }

    @Override
	public String buildDescribeSensorRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildDescribeSensorRequest(parameters);
    	}
    	throw new OXFException(String.format("Building DescribeSensor request not supported via binding '%s'!",binding.toString()));
    }
    
    @Override
	public String buildGetFeatureOfInterestRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildGetFeatureOfInterestRequest(parameters);
    	}
    	throw new OXFException(String.format("Building GetFeatureOfInterest request not supported via binding '%s'!",binding.toString()));
    }

    @Override
	public String buildInsertObservationRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildInsertObservationRequest(parameters);
    	}
    	throw new OXFException(String.format("Building InsertObservation request not supported via binding '%s'!",binding.toString()));
    }

	/**
	 * Builds a <b>Insert</b>Sensor request and returns it.
     * A SensorML document MUST be passed using <tt>ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER</tt>.
     * @throws OXFException
     */
    public String buildInsertSensorRequest(final ParameterContainer parameters) throws OXFException {
    	final Binding binding = getBinding(parameters);
    	if (binding.equals(Binding.POX)) {
    		return poxBuilder.buildInsertSensorRequest(parameters);
    	}
    	throw new OXFException(String.format("Building InsertSensor request not supported via binding '%s'!",binding.toString()));
    }

    /**
     * Builds a <b>Insert</b>Sensor request and returns it.
     * A SensorML document MUST be passed using <tt>ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER</tt>.
     * @throws OXFException
     * @Deprecated since SOS 2.0 using SWES 2.0 this operation is called InsertSensor
     * @see #buildInsertSensorRequest(ParameterContainer)
     */
    @Override
	public String buildRegisterSensorRequest(final ParameterContainer parameters) throws OXFException {
    	return buildInsertSensorRequest(parameters);
    }

	private Binding getBinding(final ParameterContainer parameters) throws OXFException
	{
		if (parameters == null) {
    		throw new OXFException(new IllegalArgumentException("ParameterContainer 'parameters' should not be null"));
    	}
		if (isBindingParameterSpecified(parameters)) {
			final String bindingName = (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.BINDING).getSpecifiedValue();
			if (bindingName.equalsIgnoreCase(Binding.POX.toString())) {
				return Binding.POX;
			}
			else if (bindingName.equalsIgnoreCase(Binding.SOAP.toString())) {
				return Binding.SOAP;
			}
			else if (bindingName.equalsIgnoreCase(Binding.KVP.toString())) {
				return Binding.KVP;
			}
		}
		LOGGER.info("Missing parameter 'binding'. Using POX.");
		return Binding.POX;
	}

	private boolean isBindingParameterSpecified(final ParameterContainer parameters)
	{
		return parameters.getParameterShellWithCommonName(ISOSRequestBuilder.BINDING) != null && 
				parameters.getParameterShellWithCommonName(ISOSRequestBuilder.BINDING).hasSingleSpecifiedValue() &&
				parameters.getParameterShellWithCommonName(ISOSRequestBuilder.BINDING).getSpecifiedValue() instanceof String;
	}

}