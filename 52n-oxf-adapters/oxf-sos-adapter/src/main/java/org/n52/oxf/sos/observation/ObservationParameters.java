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
package org.n52.oxf.sos.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import javax.xml.namespace.QName;

import org.n52.oxf.request.MultiValueRequestParameters;

/**
 * Assembles all Observation parameters needed for an InsertObservation request.
 */
public abstract class ObservationParameters extends MultiValueRequestParameters {

    private final QName type;

    protected ObservationParameters(final QName type) {
        this.type = type;
    }
    
    /**
     * @return <code>true</code> if at least all mandatory parameters are set.
     */
    public abstract boolean isValid();

    /**
     * Return the type of Observation.
     * 
     * @return observation type.
     */
    public QName getType() {
        return type;
    }

    // be careful when changing following methods
    // begin -> parameter methods necessary for: MeasurementObservationParameters and CategoryObservationBuilder

    public void addSamplingTime(final String samplingTime) {
    }
    
    /**
     * SOS 2.0 specific
     * @param resultTime
     */
    public void addResultTime(final String resultTime) {
    	addNonEmpty(INSERT_OBSERVATION_RESULT_TIME, resultTime);
    }
    
    /**
     * SOS 2.0 specific
     * @param phenomenonTime
     */
    public void addPhenomenonTime(final String phenomenonTime) {
    	addNonEmpty(INSERT_OBSERVATION_PHENOMENON_TIME, phenomenonTime);
    }
    
    public void addFoiId(final String foiId) {
    	addNonEmpty(INSERT_OBSERVATION_FOI_ID_PARAMETER, foiId);
    }
    
    public void addNewFoiId(final String foiId) {
    	addNonEmpty(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER, foiId);
    }

    public void addNewFoiName(final String foiName) {
    	addNonEmpty(INSERT_OBSERVATION_NEW_FOI_NAME, foiName);
    }

    public void addFoiDescription(final String foiDescription) {
    	addNonEmpty(INSERT_OBSERVATION_NEW_FOI_DESC, foiDescription);
    }

    public void addFoiPosition(final String foiPosition) {
    	addNonEmpty(INSERT_OBSERVATION_NEW_FOI_POSITION, foiPosition);
    }

    public void addSrsPosition(final String srsPosition) {
        addNonEmpty(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS, srsPosition);
    }

    public void addObservedProperty(final String observedProperty) {
    	addNonEmpty(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperty);
    }

	/**
	 * @param procedureId
	 */
	public void addProcedure(final String procedureId)
	{
		addNonEmpty(INSERT_OBSERVATION_PROCEDURE_PARAMETER, procedureId);
	}

    // end -> parameter methods shared by: MeasurementObservationParameters and CategoryObservationBuilder

}
