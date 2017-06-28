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
package org.n52.oxf.swes.request;

import org.n52.oxf.request.MimetypeAwareRequestParameters;


/**
 * Assembles all parameters needed for a DescribeSensor request.
 */
public class DescribeSensorParameters extends MimetypeAwareRequestParameters {

    private final String REQUEST_PARAMETER = "request";

    static final String PROCEDURE_PARAMETER = "procedure";

    static final String OUTPUT_FORMAT_PARAMETER = "outputFormat";

    static final String OUTPUT_FORMAT_SENSORML = "text/xml;subtype=\"sensorML/1.0.1\"";

    /**
     * Creates DescribeSensor request parameters. It uses {@link #OUTPUT_FORMAT_SENSORML} for
     * the required OutputFormat parameter by default.
     *
     * @param procedure
     *        the procedure which sensor description shall requested.
     */
    public DescribeSensorParameters(final String procedure) {
        this(procedure, OUTPUT_FORMAT_SENSORML);
    }

    /**
     * Creates DescribeSensor request parameters.
     *
     * @param procedure
     *        the procedure which sensor description shall requested.
     * @param outputFormat
     *        the output format of the sensor description.
     */
    public DescribeSensorParameters(final String procedure, final String outputFormat) {
        addNonEmpty(REQUEST_PARAMETER, "DescribeSensor");
        addNonEmpty(PROCEDURE_PARAMETER, procedure);
        addNonEmpty(OUTPUT_FORMAT_PARAMETER, outputFormat);
    }

    @Override
	public boolean isValid() {
        final boolean invalidProcedureValue = isEmptyValue(PROCEDURE_PARAMETER);
        final boolean invalidOutputFormatValue = isEmptyValue(OUTPUT_FORMAT_PARAMETER);
        return ! (invalidProcedureValue || invalidOutputFormatValue);
    }

}
