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

package org.n52.oxf.sos.request.v200;

import org.n52.oxf.request.MultimapRequestParameters;

/**
 * Assembles all parameters needed for a RegisterSensor request. This request is SOS 2.0.0 specific. 
 */
public class InsertSensorParameters extends MultimapRequestParameters {
    
    private static final String REQUEST_PARAMETER = "request";

    static final String PROCEDURE_DESCRIPTION = "procedureDescription";
    
    static final String PROCEDURE_DESCRIPTION_FORMAT = "procedureDescriptionFormat";
    
    static final String OBSERVABLE_PROPERTY = "observableProperty";
    
    static final String DEFAULT_DESCRIPTION_FORMAT = "text/xml;subtype=\"sensorML/1.0.1\"";

    /**
     * Creates InsertSensor parameters.
     * 
     * @param sensorDescription
     *        the sensor's description document.
     * @param observableProperties
     *        the sensor's observation template.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public InsertSensorParameters(String sensorDescription, String... observableProperties) {
        this(sensorDescription, DEFAULT_DESCRIPTION_FORMAT, observableProperties);
    }
    
    /**
     * Creates InsertSensor parameters.
     * 
     * @param sensorDescription
     *        the sensor's description document.
     * @param procedureDescriptionFormat
     *        the procedureDescription's format.
     * @param observableProperty
     *        the sensor's observable property.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public InsertSensorParameters(String sensorDescription, String procedureDescriptionFormat, String observableProperty) {
        addNonEmpty(REQUEST_PARAMETER, "InsertSensor");
        addNonEmpty(PROCEDURE_DESCRIPTION, sensorDescription);
        addNonEmpty(PROCEDURE_DESCRIPTION_FORMAT, procedureDescriptionFormat);
        addNonEmpty(OBSERVABLE_PROPERTY, observableProperty);
    }
       
    
    /**
     * Creates InsertSensor parameters.
     * 
     * @param sensorDescription
     *        the sensor's description document.
     * @param procedureDescriptionFormat
     *        the procedureDescription's format.
     * @param observableProperties
     *        the sensor's observable properties.
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public InsertSensorParameters(String sensorDescription, String procedureDescriptionFormat, String... observableProperties) {
        addNonEmpty(PROCEDURE_DESCRIPTION, sensorDescription);
        addNonEmpty(PROCEDURE_DESCRIPTION_FORMAT, procedureDescriptionFormat);
        addParameterStringValues(OBSERVABLE_PROPERTY, observableProperties);
        // XXX add multiple parameter support!
//        putNonEmpty(OBSERVABLE_PROPERTY, observableProperties);
    }

    /**
     * Overrides required procedureDescription parameter.
     * 
     * @param procedureDescription
     *        the new procedureDescription.
     * @throws IllegalArgumentException
     *         if passed argument is <code>null</code> or empty.
     */
    public InsertSensorParameters addProcedureDescription(String procedureDescription) {
        addNonEmpty(PROCEDURE_DESCRIPTION, procedureDescription);
        return this;
    }

    /**
     * Overrides required observationTemplate parameter.
     * 
     * @param procedureDescriptionFormat
     *        the procedureDescription's format.
     * @return this parameter assembly instance.
     * @throws IllegalArgumentException
     *         if passed argument is <code>null</code> or empty.
     */
    public InsertSensorParameters setObservationTemplate(String procedureDescriptionFormat) {
        addNonEmpty(PROCEDURE_DESCRIPTION_FORMAT, procedureDescriptionFormat);
        return this;
    }

    public boolean isValid() {
        boolean invalidProcedureDescription = isEmptyValue(PROCEDURE_DESCRIPTION);
        boolean invalidProcedureDescriptionFormat = isEmptyValue(PROCEDURE_DESCRIPTION_FORMAT);
        boolean invalidObservableProperty = isEmptyValue(OBSERVABLE_PROPERTY);
        return ! (invalidProcedureDescription || invalidProcedureDescriptionFormat || invalidObservableProperty);
    }

}