/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sos.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.xml.XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a MeasurementObservationParameters observation.
 */
public class MeasurementObservationParameters extends ObservationParameters {

    /**
     * Crreates truth observations of type {@link XMLConstants#QNAME_OM_1_0_MEASUREMENT_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_MEASUREMENT} as its type description to the parameter
     * list.
     */
    public MeasurementObservationParameters() {
        super(QNAME_OM_1_0_MEASUREMENT_OBSERVATION);
        addNonEmpty(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_MEASUREMENT);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add
     */
    public void addObservationValue(final String observationValue) {
        addParameterValue(INSERT_OBSERVATION_VALUE_PARAMETER, observationValue);
    }

    /**
     * Adds or replaces the (current) unit of measure.
     * 
     * @param uom
     *        the unit of measure to add.
     */
    public void addUom(final String uom) {
        addParameterValue(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE, uom);
    }

    @Override
	public boolean isValid() {
        return !isEmpty(INSERT_OBSERVATION_VALUE_PARAMETER) && 
        		!isEmpty(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
    }

}