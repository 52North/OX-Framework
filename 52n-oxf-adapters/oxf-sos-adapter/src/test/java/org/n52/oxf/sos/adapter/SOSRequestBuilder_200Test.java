/**
 * Copyright (C) 2013
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
package org.n52.oxf.sos.adapter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;

public class SOSRequestBuilder_200Test {

	private SOSRequestBuilder_200 builder;
	
	@Before
	public void init() {
		builder = new SOSRequestBuilder_200();
	}

	@Test (expected=OXFException.class) public void 
	buildRegisterSensor_should_return_OXFException_if_parameters_is_null()
			throws OXFException {
		builder.buildRegisterSensor(null);
	}
	
	@Test public void 
	buildRegisterSensor_should_set_service_and_version()
			 throws OXFException, XmlException {
		final String sosVersion = "test-version";
		final String sosService = "test-service";
		final ParameterContainer parameters = createParamConWithVersionAndService(sosVersion, sosService);
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		assertThat(insertSensorType.getVersion(),is(sosVersion));
		assertThat(insertSensorType.getService(),is(sosService));
	}
	
	private ParameterContainer createParamConWithVersionAndService(final String sosVersion,
			final String sosService) throws OXFException
	{
		final ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, sosService);
		parameters.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, sosVersion);
		return parameters;
	}
	
}
