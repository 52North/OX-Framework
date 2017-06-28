/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sos.request.v100;



/**
 * Assembles all parameters needed for a RegisterSensor request. This request is SOS 1.0.0 specific.
 */
public class RegisterSensorParameters extends Sos100RequestParameters {

    private static final String REQUEST_PARAMETER = "request";

    static final String REGISTER_SENSOR_ML_DOC_PARAMETER = "sensorMLDoc";

    static final String REGISTER_SENSOR_OBSERVATION_TEMPLATE = "observationTemplate";

    static final String REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY = "category";

    static final String REGISTER_SENSOR_OBSERVATION_TYPE = "type";

    /**
     * Creates RegisterSensor parameters.
     *
     * @param sensorDescription
     *        the sensor's description document.
     * @param observationTemplate
     *        the sensor's observation template.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public RegisterSensorParameters(final String sensorDescription, final String observationTemplate) {
        addNonEmpty(REQUEST_PARAMETER, "RegisterSensor");
        addNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
        addNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
    }

    /**
     * Overrides required sensorDescription parameter.
     *
     * @param sensorDescription
     *        the new sensorDescription.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public void setSensorDescription(final String sensorDescription) {
        addNonEmpty(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
    }

    /**
     * Overrides required observationTemplate parameter.
     *
     * @param observationTemplate
     *        the new observationTemplate.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public void setObservationTemplate(final String observationTemplate) {
        addNonEmpty(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
    }

    public boolean isValid() {
        final boolean invalidSmlParameterValue = isEmptyValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
        final boolean invalidTemplateValue = isEmptyValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
        return ! (invalidSmlParameterValue || invalidTemplateValue);
    }

}