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