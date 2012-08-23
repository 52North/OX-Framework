package org.n52.oxf.sos.adapter;

import static org.junit.Assert.*;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSWrapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SOSWrapperTest.class);

	@Test
	public void testDoDescribeSensorMethods() {

		try {
			File f = new File(new File(".").getCanonicalPath() + "\\src\\test\\resources\\sos1_capabilities_sample.xml");
			SOSWrapper sw = SOSWrapper.createFromCapabilitiesFile(f, "1.0.0");
			DescribeSensorParamterBuilder_v100 dspb = new DescribeSensorParamterBuilder_v100("sensorId", DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML);
			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
			
			// method: isDescribeSensorDefined(...)
			assertTrue(sw.isDescribeSensorDefined(om));
			
			// method: createParameterContainerForDoDescribeSensor(...)
			ParameterContainer pc = sw.createParameterContainerForDoDescribeSensor(dspb.getParameters());
			String service = (String) pc.getParameterShellWithCommonName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue();
			String version = (String) pc.getParameterShellWithCommonName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue();
			String procedure = (String) pc.getParameterShellWithCommonName(DESCRIBE_SENSOR_PROCEDURE_PARAMETER).getSpecifiedValue();
			String output = (String) pc.getParameterShellWithCommonName(DESCRIBE_SENSOR_OUTPUT_FORMAT).getSpecifiedValue();
			
			assertEquals("SOS", service);
			assertEquals("1.0.0", version);
			assertEquals("sensorId", procedure);
			assertEquals(DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML, output);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionReport e) {
			e.printStackTrace();
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDoGetObservationMethods() {

		try {
			File f = new File(new File(".").getCanonicalPath() + "\\src\\test\\resources\\sos1_capabilities_sample.xml");
			SOSWrapper sw = SOSWrapper.createFromCapabilitiesFile(f, "1.0.0");
			GetObservationParameterBuilder_v100 gopb = new GetObservationParameterBuilder_v100("offering", "observedProperty", "responseFormat");
			gopb.addSrsName("srsName");
			gopb.addEventTime("eventTime");
			gopb.addProcedure("procedure");
			gopb.addFeatureOfInterest("featureOfInterest");
			gopb.addResult("result");
			gopb.addResultModel("resultModel");
			gopb.addResponseMode("responseMode");
			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
			
			// method: isGetObservationDefined(...)
			assertTrue(sw.isGetObservationDefined(om));
			
			// method: createParameterContainerForGetObservation(...)
			ParameterContainer pc = sw.createParameterContainerForGetOservation(gopb.getParameters());
			String service = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue();
			String version = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue();
			String offering = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_OFFERING_PARAMETER).getSpecifiedValue();			
			
			ParameterShell observedPropertyPs = (ParameterShell) pc.getParameterShellWithCommonName(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
			String[] observedPropertyArray = observedPropertyPs.getSpecifiedTypedValueArray(String[].class);
			String observedProperty = observedPropertyArray[0];
			
			String responseFormat = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue();
			String srs = (String) pc.getParameterShellWithCommonName(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER).getSpecifiedValue();
			
			ParameterShell eventTimePs = (ParameterShell) pc.getParameterShellWithCommonName(GET_OBSERVATION_EVENT_TIME_PARAMETER);
			String[] eventTimeArray = eventTimePs.getSpecifiedTypedValueArray(String[].class);
			String eventTime = eventTimeArray[0];
			
			ParameterShell procedurePs = (ParameterShell) pc.getParameterShellWithCommonName(GET_OBSERVATION_PROCEDURE_PARAMETER);
			String[] procedureArray = procedurePs.getSpecifiedTypedValueArray(String[].class);
			String procedure = procedureArray[0];
			
			String foi = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER).getSpecifiedValue();
			String result = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_RESULT_PARAMETER).getSpecifiedValue();
			String resultModel = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue();
			String responseMode = (String) pc.getParameterShellWithCommonName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER).getSpecifiedValue();
			
			assertEquals("SOS", service);
			assertEquals("1.0.0", version);
			assertEquals("offering", offering);
			assertEquals("observedProperty", observedProperty);
			assertEquals("responseFormat", responseFormat);
			assertEquals("srsName", srs);
			
			assertEquals("eventTime", eventTime);
			assertEquals("procedure", procedure);
			assertEquals("featureOfInterest", foi);
			assertEquals("result", result);
			assertEquals("resultModel", resultModel);
			assertEquals("responseMode", responseMode);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionReport e) {
			e.printStackTrace();
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDoRegisterSensor() {

//		try {
//			File f = new File(new File(".").getCanonicalPath() + "\\src\\test\\resources\\sos1_capabilities_sample.xml");
//			SOSWrapper sw = SOSWrapper.createFromCapabilitiesFile(f, "1.0.0");
//			SensorDescriptionBuilder sensorDescription = new SensorDescriptionBuilder();
//			ObservationTemplateBuilder observationTemplate = new ObservationTemplateBuilder();
//			RegisterSensorParameterBuilder_v100 rspb = new RegisterSensorParameterBuilder_v100(sensorDescription, template);
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			
//			// method: isRegisterSensorDefined(...)
//			assertTrue(sw.isGetObservationDefined(om));
//			
//			// method: createParameterContainerForRegisterSensor(...)
//			ParameterContainer pc = sw.createParameterContainerForRegisterSensor(rspb.getParameters());
//			String service = (String) pc.getParameterShellWithCommonName(REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithCommonName(REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue();
//			String sensorDescripiton = (String) pc.getParameterShellWithCommonName(REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue();			
//			String observationTemplate = (String) pc.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals(, sensorDescripiton);
//			assertEquals(, observationTemplate);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
	}
	

}