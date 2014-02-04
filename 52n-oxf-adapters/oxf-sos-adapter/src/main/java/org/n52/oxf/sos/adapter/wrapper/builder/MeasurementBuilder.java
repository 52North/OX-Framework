/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE;

import org.n52.oxf.sos.request.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.request.observation.ObservationParametersFactory;
import org.n52.oxf.xml.XMLConstants;

/**
 * MeasurementObservationParameters assembles parameters, which are necessary to describe a measurement.
 * 
 * @author Eric
 * @deprecated use {@link ObservationParametersFactory} or {@link MeasurementObservationParameters}
 */
@Deprecated
public class MeasurementBuilder extends ObservationBuilder {
			
		/**
		 * Type specific constructor for measurements. It adds the type as parameter to the list.
		 */
		public MeasurementBuilder() {
			type = XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;
			parameters.put(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
		}
		
		/**
		 * Adds or replaces the value for the parameter "uom" to the parameter list.
		 * 
		 * @param uom
		 */
		public void addUom(String uom) {
			if (parameters.get(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE) != null) {
				parameters.remove(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
			}
			parameters.put(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);
		}
		
		public void addObservationValue(String observationValue) {
			if (parameters.get(INSERT_OBSERVATION_VALUE_PARAMETER) != null) {
				parameters.remove(INSERT_OBSERVATION_VALUE_PARAMETER);
			}
			parameters.put(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
		}

}