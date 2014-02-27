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
package org.n52.oxf.sos.request.v200;

import java.util.Collection;

/**
 * Assembles all parameters needed for a InsertSensor request. This request is SOS 2.0.0 specific. 
 */
public class InsertSensorParameters extends Sos200RequestParameters {
    
    public static final String PROCEDURE_DESCRIPTION = "procedureDescription";
    
    public static final String PROCEDURE_DESCRIPTION_FORMAT = "procedureDescriptionFormat";
    
    public static final String OBSERVABLE_PROPERTIES = "observableProperties";
    
    public static final String FEATURE_OF_INTEREST_TYPES = "featureOfInterestTypes";
    
    public static final String DEFAULT_DESCRIPTION_FORMAT = "http://www.opengis.net/sensorML/1.0.1";

    public static final String OBSERVATION_TYPES = "observationTypes";

    /**
     * Creates InsertSensor parameters.
     * 
     * @param procedureDescriptionXml
     *        the procedure's description document.
     * @param procedureDescriptionFormat
     *        the procedureDescription's format.
     * @param observableProperties
     *        the procedure's observable properties (1..n)
     * @param featureOfInterestTypes
     *        the procedure's feature of interest types (1..n)
     * @throws IllegalArgumentException
     *         if passed arguments are <code>null</code> or empty.
     */
    public InsertSensorParameters(final String procedureDescriptionXml,
    		final String procedureDescriptionFormat,
    		final Collection<String> observableProperties,
    		final Collection<String> featureOfInterestTypes,
    		final Collection<String> observationTypes) {
    	addNonEmpty(PROCEDURE_DESCRIPTION, procedureDescriptionXml);
    	addNonEmpty(PROCEDURE_DESCRIPTION_FORMAT, procedureDescriptionFormat);
    	addNonEmpty(OBSERVABLE_PROPERTIES,observableProperties);
    	addNonEmpty(FEATURE_OF_INTEREST_TYPES,featureOfInterestTypes);
    	addNonEmpty(OBSERVATION_TYPES, observationTypes);
    }
    
    public boolean isValid() {
        return ! (isEmptyValue(PROCEDURE_DESCRIPTION) || 
        		isEmptyValue(PROCEDURE_DESCRIPTION_FORMAT) ||
        		isEmptyValue(OBSERVABLE_PROPERTIES));
    }    
}