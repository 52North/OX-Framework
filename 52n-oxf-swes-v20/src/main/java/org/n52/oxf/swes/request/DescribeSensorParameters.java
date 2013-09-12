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

package org.n52.oxf.swes.request;

import org.n52.oxf.request.MultiValueRequestParameters;


/**
 * Assembles all parameters needed for a DescribeSensor request. 
 */
public class DescribeSensorParameters extends MultiValueRequestParameters {

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

    public boolean isValid() {
        final boolean invalidProcedureValue = isEmptyValue(PROCEDURE_PARAMETER);
        final boolean invalidOutputFormatValue = isEmptyValue(OUTPUT_FORMAT_PARAMETER);
        return ! (invalidProcedureValue || invalidOutputFormatValue);
    }

}
