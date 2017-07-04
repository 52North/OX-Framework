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

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER;

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.sos.observation.ObservationParameters;

/**
 * This class describes a set of parameters, which is necessary to call
 * doInsertObservation([...]) from SOSWrapper.
 *
 * @author Eric
 */
public class InsertObservationParameters {

    private final Map<String, String> parameters;

    /**
     * Assembles mandatory parameters from method parameter list.
     *
     * @param assignedSensorId
     * @param observation
     * @deprecated Use {@link #InsertObservationParameters(String, ObservationParameters)}
     */
    @Deprecated
    public InsertObservationParameters(final String assignedSensorId, final ObservationBuilder observationBuilder) throws IllegalArgumentException {
        if (assignedSensorId == null || observationBuilder == null) {
            throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"observationBuilder\" are mandatory. They cannot be left empty!");
        }
        parameters = observationBuilder.getParameters();
        parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
    }

    /**
     * Assembles mandatory parameters from method parameter list.
     *
     * @param sensorURI
     * @param obsParameter
     */
    public InsertObservationParameters(final String assignedSensorId, final ObservationParameters obsParameter) {
        if (assignedSensorId == null || obsParameter == null) {
            throw new IllegalArgumentException("The parameters \"assignedSensorId\" and \"obsParameter\" are mandatory. They cannot be left empty!");
        }
        parameters = getParameterMapFrom(obsParameter);
        parameters.put(INSERT_OBSERVATION_PROCEDURE_PARAMETER, assignedSensorId);
    }

    private Map<String, String> getParameterMapFrom(final ObservationParameters obsParameter)
    {
        if (!obsParameter.isEmpty())
        {
            final HashMap<String,String> parameterMap = new HashMap<String, String>();
            for (final String key : obsParameter.getParameterNames())
            {
                if (!key.isEmpty())
                {
                    final Iterable<String> parameterValues = obsParameter.getAllValues(key);
                    final StringBuilder concatenatedParameterValues = new StringBuilder();
                    for (final String parameterValue : parameterValues) {
                        if (parameterValue != null && !parameterValue.isEmpty())
                        {
                            concatenatedParameterValues.append(parameterValue);
                            concatenatedParameterValues.append(",");
                        }
                    }
                    // remove last ","
                    String parameterValuesAsSingleString = concatenatedParameterValues.toString();
                    if (!parameterValuesAsSingleString.isEmpty())
                    {
                        parameterValuesAsSingleString = parameterValuesAsSingleString.substring(0, parameterValuesAsSingleString.length()-1);
                        parameterMap.put(key, parameterValuesAsSingleString);
                    }
                }
            }
            return parameterMap;
        }
        return new HashMap<String, String>(0);
    }

    /**
     * @return set of parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

}
