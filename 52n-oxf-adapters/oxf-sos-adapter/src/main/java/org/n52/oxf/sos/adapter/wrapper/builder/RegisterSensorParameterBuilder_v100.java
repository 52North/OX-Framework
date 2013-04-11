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

import org.n52.oxf.sos.request.v100.RegisterSensorParameters;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

/**
 * This class describes a set of parameters, which is necessary to call
 * doRegisterSensor([...]) from SOSWrapper.
 * 
 * @author Eric
 * @deprecated use {@link InsertSensorParameters}
 */
@Deprecated
public class RegisterSensorParameterBuilder_v100 {
	
	private Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Assembles mandatory parameters from method parameter list.
	 * 
	 * @param sensorDescription 
	 * @throws IllegalArgumentException
	 */
	public RegisterSensorParameterBuilder_v100(String sensorDescription, String observationTemplate) throws IllegalArgumentException {
		if (sensorDescription == null || observationTemplate == null) {
			throw new IllegalArgumentException("The parameters \"sensorDescription\" and \"template\" are mandatory. They cannot be left empty!");
		}
		parameters.put(REGISTER_SENSOR_ML_DOC_PARAMETER, sensorDescription);
		parameters.put(REGISTER_SENSOR_OBSERVATION_TEMPLATE, observationTemplate);
	}

	/**
	 * @return set of parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

}