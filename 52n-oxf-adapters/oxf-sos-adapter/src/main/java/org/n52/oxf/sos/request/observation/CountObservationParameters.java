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

package org.n52.oxf.sos.request.observation;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.xml.XMLConstants;

/**
 * Assembles parameters for a Count observation.
 */
public class CountObservationParameters extends ObservationParameters {

    /**
     * Creates truth observations of type {@link XMLConstants#QNAME_OM_1_0_COUNT_OBSERVATION}. Adds
     * {@link ISOSRequestBuilder#INSERT_OBSERVATION_TYPE_COUNT} as its type description to the parameter list.
     */
    public CountObservationParameters() {
        super(XMLConstants.QNAME_OM_1_0_COUNT_OBSERVATION);
        addParameterValue(INSERT_OBSERVATION_TYPE, INSERT_OBSERVATION_TYPE_COUNT);
    }

    /**
     * Adds or replaces the (current) observation value.
     * 
     * @param observationValue
     *        the observation value to add.
     */
    public void addObservationValue(int observationValue) {
        addParameterValue(INSERT_OBSERVATION_VALUE_PARAMETER, Integer.toString(observationValue));
    }

    public boolean isValid() {
        return true;
    }
}
