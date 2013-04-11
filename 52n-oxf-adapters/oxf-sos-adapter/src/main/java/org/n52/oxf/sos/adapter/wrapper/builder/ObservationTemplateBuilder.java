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

import org.n52.oxf.OXFException;
import org.n52.oxf.xml.XMLConstants;

import net.opengis.gml.MeasureType;
import net.opengis.om.x10.CategoryObservationType;
import net.opengis.om.x10.CountObservationType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.om.x10.TruthObservationType;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.swe.x10.ScopedNameType;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * Template generating class. Everything needed for generating the template is
 * the type and additional type specific parameters
 * 
 * @author Eric
 */
public class ObservationTemplateBuilder {

	private Map<String, String> parameters = new HashMap<String, String>();
	
	private QName observationType;
	
	/**
	 * Hidden public constructor.
	 */
	private ObservationTemplateBuilder() {
	}
	
	/**
	 * Type specific template builder generator for measurements.
	 * 
	 * @param uom unit of measurement
	 * @return instance of measurement template builder 
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeMeasurement(String uom) {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;
		builder.parameters.put(REGISTER_SENSOR_CODESPACE_PARAMETER, uom);
		return builder;
	}
	
	/**
	 * Type specific template builder generator for category observations.
	 * 
	 * @param codeSpace
	 * @return instance of category observation template builder
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeText() {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_TEXT_OBSERVATION;
		return builder;
	}
	
	/**
	 * Type specific template builder generator for count observations.
	 * 
	 * @return instance of count observation template builder
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeCount() {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION;
		return builder;
	}

	/**
	 * Type specific template builder generator for truth observations.
	 * 
	 * @return instance of truth observation template builder
	 */
	public static ObservationTemplateBuilder createObservationTemplateBuilderForTypeTruth() {
		ObservationTemplateBuilder builder = new ObservationTemplateBuilder();
		builder.observationType = XMLConstants.QNAME_OM_1_0_TRUTH_OBSERVATION;
		return builder;
	}

	public void setDefaultValue(String defaultValue) {
		parameters.put(REGISTER_SENSOR_DEFAULT_RESULT_VALUE, defaultValue);
	}
	
	/**
	 * Generator core of the template.
	 * 
	 * @return type specific observation template
	 * @throws OXFException
	 */
	public String generateObservationTemplate() throws OXFException {
		ObservationTemplate obsTemp = ObservationTemplate.Factory.newInstance();
		ObservationType ot = obsTemp.addNewObservation();
		ot.addNewSamplingTime();
		ot.addNewProcedure();
		ot.addNewObservedProperty();
		ot.addNewFeatureOfInterest();
		
		if (observationType.equals(XMLConstants.QNAME_OM_1_0_TEXT_OBSERVATION)){
			ot.substitute(XMLConstants.QNAME_OM_1_0_OBSERVATION, ObservationType.type);
			ot.addNewResult();
		} else if (observationType.equals(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION)){
			MeasureType mt2 = MeasureType.Factory.newInstance();
			double defaultValue = Double.valueOf(parameters.get(REGISTER_SENSOR_DEFAULT_RESULT_VALUE));
			mt2.setDoubleValue(defaultValue); // default value required by our SOS
			mt2.setUom(parameters.get(REGISTER_SENSOR_UOM_PARAMETER));
			ot = (ObservationType) ot.substitute(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION, MeasurementType.type);
			ot.addNewResult().set(mt2);
		} else if (observationType.equals(XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION)) {
			ot.substitute(XMLConstants.QNAME_OM_1_0_OBSERVATION, ObservationType.type);
			ot.addNewResult();
		} else if (observationType.equals(XMLConstants.QNAME_OM_1_0_TRUTH_OBSERVATION)) {
			ot.substitute(XMLConstants.QNAME_OM_1_0_OBSERVATION, ObservationType.type);
			ot.addNewResult();
		} else {
			throw new OXFException("Observation type '" + observationType + "' not supported.");
		}
		
		return obsTemp.toString();
	}
	
}