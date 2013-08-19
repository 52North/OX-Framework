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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;

public class SOSRequestBuilder_200Test {

	/**
	 * 
	 */
	private static final String SENSOR_IDENTIFIER = "test-identifier";

	private static final String TEST_OBSERVABLE_PROPERTY_2 = "test-observable-property-2";

	private static final String TEST_OBSERVABLE_PROPERTY_1 = "test-observable-property-1";

	private SOSRequestBuilder_200 builder;
	
	private final String sosVersion = "test-version";
	private final String sosService = "test-service";
	private final String format = "test-format";
	
		@Test (expected=OXFException.class) public void 
	buildRegisterSensor_should_return_OXFException_if_parameters_is_null()
			throws OXFException {
		builder.buildRegisterSensor(null);
	}
	
	@Test public void 
	buildRegisterSensor_should_set_service_and_version()
			 throws OXFException, XmlException {
		final ParameterContainer parameters = createParamConWithMandatoryValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getVersion(),is(sosVersion));
		assertThat(insertSensorType.getService(),is(sosService));
	}
	
	@Test public void
	buildRegisterSensor_should_add_observable_properties()
			throws OXFException, XmlException {
		final ParameterContainer parameters = createParamConWithMandatoryValues();
		
		parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER,
				TEST_OBSERVABLE_PROPERTY_1,
				TEST_OBSERVABLE_PROPERTY_2);
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getObservablePropertyArray().length, is(2));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_1));
		assertThat(insertSensorType.getObservablePropertyArray(),hasItemInArray(TEST_OBSERVABLE_PROPERTY_2));
	}
	
	@Test public void
	buildRegisterSensor_should_set_procedure_description_format()
			throws XmlException, OXFException {
		final ParameterContainer parameters = createParamConWithMandatoryValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getProcedureDescriptionFormat(),is(format));
	}
	
	@Test public void
	buildRegisterSensor_should_set_procedure_description()
			throws XmlException, OXFException {
		final ParameterContainer parameters = createParamConWithMandatoryValues();
		
		final String registerSensor = builder.buildRegisterSensor(parameters);
		final InsertSensorType insertSensorType = InsertSensorDocument.Factory.parse(registerSensor).getInsertSensor();
		
		assertThat(insertSensorType.getProcedureDescription().toString(),is(createSensorDescription()));
	}
	
	@Test public void
	buildRegisterSensor_should_throw_exception_if_receiving_invalid_procedure_description()
			throws XmlException, OXFException {
		final ParameterContainer parameters = createParamConWithMandatoryValues();
		parameters.removeParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, "INVALID");
		
		try {
			builder.buildRegisterSensor(parameters);
		}
		catch (final Exception e) {
			assertThat(e,is(instanceOf(OXFException.class)));
			assertThat(e.getCause(),(is(instanceOf(XmlException.class))));
			assertThat(e.getMessage(),is("Error while parsing MANDATORY parameter 'procedure description'!"));
		}
	}
	
	private String createSensorDescription()
	{
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		builder.setIdentifierUniqeId(SENSOR_IDENTIFIER);
		final SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
		sensorMLDocument.addNewSensorML().addNewMember().set(builder.buildSensorDescription());
		sensorMLDocument.getSensorML().setVersion("1.0.1");
		return sensorMLDocument.toString();
	}

	private ParameterContainer createParamConWithMandatoryValues() throws OXFException
	{
		final ParameterContainer parameters = new ParameterContainer();
		parameters.addParameterShell(REGISTER_SENSOR_SERVICE_PARAMETER, sosService);
		parameters.addParameterShell(REGISTER_SENSOR_VERSION_PARAMETER, sosVersion);
		parameters.addParameterShell(ISOSRequestBuilder.REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER, format);
		parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, createSensorDescription());
		return parameters;
	}
	
	@Before
	public void init() {
		builder = new SOSRequestBuilder_200();
	}
}
