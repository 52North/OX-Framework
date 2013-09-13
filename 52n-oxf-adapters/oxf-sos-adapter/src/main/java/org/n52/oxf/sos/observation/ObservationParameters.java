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
