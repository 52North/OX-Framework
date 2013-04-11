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
package org.n52.oxf.sos.adapter.wrapper.builder;

import java.util.HashMap;
import java.util.Map;

import org.n52.oxf.sos.adapter.ISOSRequestBuilder;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of parameters, which is necessary to call
 * doGetFeatureOfInterst([...]) from SOSWrapper.
 * 
 * @author Eric
 */
public class GetFeatureOfInterestParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>();

	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param identification
	 * @param identificationType
	 * @see ISOSRequestBuilder
	 */
	public GetFeatureOfInterestParameterBuilder_v100(String identification, String identificationType) {
		if (identification == null || identificationType == null ||
				(!identificationType.equals(GET_FOI_ID_PARAMETER) && !identificationType.equals(GET_FOI_LOCATION_PARAMETER)))
			throw new IllegalArgumentException("The parameters \"identification\" and \"identificationType\" are mandatory. " +
					"They cannot be left empty! Take care of the available identification types! (" +
					GET_FOI_ID_PARAMETER + ", " + GET_FOI_LOCATION_PARAMETER + ")");
		if (identificationType.equals(GET_FOI_ID_PARAMETER))
			parameters.put(GET_FOI_ID_PARAMETER, identification);
		else
			parameters.put(GET_FOI_LOCATION_PARAMETER, identification);
	}
	
	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Adds or replaces the optional parameter "eventTime".
	 * 
	 * @param eventTime
	 * @return parameter builder
	 */
	public GetFeatureOfInterestParameterBuilder_v100 addEventTime(String eventTime) {
		if (parameters.get(GET_FOI_EVENT_TIME_PARAMETER) != null) {
			parameters.remove(GET_FOI_EVENT_TIME_PARAMETER);
		}
		parameters.put(GET_FOI_EVENT_TIME_PARAMETER, eventTime);
		return this;
	}
	
}
