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
package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.oxf.sos.request.observation.ObservationParameters;
import org.n52.oxf.sos.request.observation.ObservationParametersFactory;
import org.n52.oxf.sos.request.observation.TextObservationParameters;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class is used to collect parameter information about Observations.
 * 
 * @author Eric
 * @deprecated use {@link ObservationParametersFactory} or {@link ObservationParameters}
 */
@Deprecated
public abstract class ObservationBuilder {
	
	protected QName type;
	protected Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Creates an observation builder with type specific parameter setters for measurements.
	 * 
	 * @return measurement builder
	 */
	public static MeasurementBuilder createObservationForTypeMeasurement() {
		return new MeasurementBuilder();
	}
	
	/**
	 * Creates an observation builder with type specific parameter setters for category observations.
	 * 
	 * @return category observation builder
	 */
	public static TextObservationBuilder createObservationForTypeText() {
		return new TextObservationBuilder();
	}
	
	/**
	 * Creates an observation builder with type specific parameter setters for truth observations.
	 * 
	 * @return boolean obervation builder
	 */
	public static BooleanObservationBuilder createObservationForTypeBoolean() {
		return new BooleanObservationBuilder();
	}
	
	/**
	 * Creates an observation builder with type specific parameter setters for count observations.
	 * 
	 * @return count observation builder
	 */
	public static CountObservationBuilder createObservationForTypeCount() {
		return new CountObservationBuilder();
	}

	/**
	 * Empty constructor, which is only used to hide direct instantiation.
	 */
	protected ObservationBuilder() {
	}
	
	/**
	 * Return all parameters of this observation description.
	 * 
	 * @return parameter list
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Return the type of Observation.
	 * 
	 * @return observation type
	 */
	public QName getType() {
		return type;
	}
	
	/**
	 * Removes a certain parameter from the list.
	 * 
	 * @param parameter
	 */
	public void removeParameter(String parameter) {
		parameters.remove(parameter);
	}
	
	// be careful when changing following methods
	// begin -> parameter methods necessary for: MeasurementObservationParameters and CategoryObservationBuilder
	
	public void addSamplingTime(String samplingTime) {
		if (parameters.get(INSERT_OBSERVATION_SAMPLING_TIME) != null) {
			parameters.remove(INSERT_OBSERVATION_SAMPLING_TIME);
		}
		parameters.put(INSERT_OBSERVATION_SAMPLING_TIME, samplingTime);
	}
	
	public void addFoiId(String foiId) {
		if (parameters.get(INSERT_OBSERVATION_FOI_ID_PARAMETER) != null) {
			parameters.remove(INSERT_OBSERVATION_FOI_ID_PARAMETER);
		}
		parameters.put(INSERT_OBSERVATION_FOI_ID_PARAMETER, foiId);
	}
	
	public void addNewFoiName(String foiName) {
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_NAME) != null) {
			parameters.remove(INSERT_OBSERVATION_NEW_FOI_NAME);
		}
		parameters.put(INSERT_OBSERVATION_NEW_FOI_NAME, foiName);
	}
	
	public void addFoiDescription(String foiDescription) {
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_DESC) != null) {
			parameters.remove(INSERT_OBSERVATION_NEW_FOI_DESC);
		}
		parameters.put(INSERT_OBSERVATION_NEW_FOI_DESC, foiDescription);
	}
	
	public void addFoiPosition(String foiPosition) {
		if (parameters.get(INSERT_OBSERVATION_NEW_FOI_POSITION) != null) {
			parameters.remove(INSERT_OBSERVATION_NEW_FOI_POSITION);
		}
		parameters.put(INSERT_OBSERVATION_NEW_FOI_POSITION, foiPosition);
	}

	public void addSrsPosition(String srsPosition) {
		if (parameters.get(INSERT_OBSERVATION_POSITION_SRS) != null) {
			parameters.remove(INSERT_OBSERVATION_POSITION_SRS);
		}
		parameters.put(INSERT_OBSERVATION_POSITION_SRS, srsPosition);
	}
	
	public void addObservedProperty(String observedProperty) {
		if (parameters.get(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER) != null) {
			parameters.remove(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
		}
		parameters.put(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
	}
	
	// end -> parameter methods shared by: MeasurementObservationParameters and CategoryObservationBuilder

}
