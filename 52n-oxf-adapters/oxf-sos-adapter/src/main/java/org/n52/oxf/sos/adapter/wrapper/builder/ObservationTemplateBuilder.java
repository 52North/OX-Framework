/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.n52.oxf.OXFException;
import org.n52.oxf.xml.XMLConstants;

import net.opengis.gml.MeasureType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;

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
