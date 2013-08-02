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
package org.n52.oxf.sos.request.v100;

import static org.junit.Assert.assertEquals;
import static org.n52.oxf.sos.request.v100.RegisterSensorParameters.REGISTER_SENSOR_ML_DOC_PARAMETER;
import static org.n52.oxf.sos.request.v100.RegisterSensorParameters.REGISTER_SENSOR_OBSERVATION_TEMPLATE;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;

public class RegisterSensorParametersTest {
	
    private RegisterSensorParameters parameters;

    @Before
    public void setUp() {
        parameters = new RegisterSensorParameters("sensorDescription", "observationTemplate");
    }
    
	@Test
	public void testValidConstructorParameters() {
        new RegisterSensorParameters("asdf", "adf");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new RegisterSensorParameters(null, null);
		new RegisterSensorParameters("", null);
		new RegisterSensorParameters(null, "");
        new RegisterSensorParameters("", "");
        new RegisterSensorParameters("asdf", "");
        new RegisterSensorParameters("", "asdf");
	}
	
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		String parMan_01 = parameters.getSingleValue(REGISTER_SENSOR_ML_DOC_PARAMETER);
		String parMan_02 = parameters.getSingleValue(REGISTER_SENSOR_OBSERVATION_TEMPLATE);
		
		assertEquals("sensorDescription", parMan_01);
		assertEquals("observationTemplate", parMan_02);
	}

}
