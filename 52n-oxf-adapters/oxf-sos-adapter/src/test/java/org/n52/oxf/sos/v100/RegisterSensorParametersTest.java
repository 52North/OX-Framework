package org.n52.oxf.sos.v100;

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
