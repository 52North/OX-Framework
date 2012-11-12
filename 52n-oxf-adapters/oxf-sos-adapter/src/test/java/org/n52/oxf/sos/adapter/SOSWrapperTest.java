package org.n52.oxf.sos.adapter;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER;

import net.sf.saxon.om.Navigator.PrecedingEnumeration;
//
//import org.n52.oxf.OXFException;
//import org.n52.oxf.adapter.ParameterContainer;
//
//public class SOSWrapperTest {
//	
//	public static void main(String[] args) {
//		
//		ParameterContainer paramCon = new ParameterContainer();
//        try {
//			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, "procedure_1");
//			paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, "procedure_2");
//			Object[] procedures = paramCon.getParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValueArray();
//
//			System.out.println(procedures[0].getClass());
//			
//			String[] procs = new String[procedures.length];
//			for (int i = 0; i < procedures.length; i++) {
//				procs[i] = (String) procedures[i];
//				System.out.println(i + ":" + procs[i]);
//			}
//			
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//
//}










































































//package org.n52.oxf.sos.adapter;
//
//import static org.junit.Assert.*;
//import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Map;
//
//import org.apache.xmlbeans.XmlException;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.n52.oxf.OXFException;
//import org.n52.oxf.adapter.ParameterContainer;
//import org.n52.oxf.adapter.ParameterShell;
//import org.n52.oxf.ows.ExceptionReport;
//import org.n52.oxf.ows.ServiceDescriptor;
//import org.n52.oxf.ows.capabilities.OperationsMetadata;
//import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
//import org.n52.oxf.sos.adapter.wrapper.builder.CategoryObservationBuilder;
//import org.n52.oxf.sos.adapter.wrapper.builder.DescribeSensorParamterBuilder_v100;
//import org.n52.oxf.sos.adapter.wrapper.builder.GetFeatureOfInterestParameterBuilder_v100;
//import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationByIdParameterBuilder_v100;
//import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;
//import org.n52.oxf.sos.adapter.wrapper.builder.InsertObservationParameterBuilder_v100;
//import org.n52.oxf.sos.adapter.wrapper.builder.ObservationBuilder;
//import org.n52.oxf.sos.util.SosUtil;
//import org.n52.oxf.xml.XMLConstants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class SOSWrapperTest {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(SOSWrapperTest.class);
//	private static SOSWrapper sw;
//
//	@BeforeClass
//    public static void oneTimeSetUp() {
//		File f;
//		try {
//			f = new File(new File(".").getCanonicalPath() + "\\src\\test\\resources\\sos1_capabilities_sample.xml");
//			sw = MySosWrapperSeam.createFromCapabilitiesFile(f, "1.0.0");
//		} catch (IOException e) {
//			 LOGGER.error("Cannot read capabilities file!", e);
//	         fail("Cannot read capabilities file!");
//		} catch (ExceptionReport e) {
//			LOGGER.error("Cannot read capabilities file!", e);
//	        fail("Cannot read capabilities file!");
//		} catch (OXFException e) {
//			LOGGER.error("Cannot read capabilities file!", e);
//	        fail("Cannot read capabilities file!");
//		}
//    }
//	
//	@Test
//	public void testDoDescribeSensorMethods() {
//		
//		try {
//			DescribeSensorParamterBuilder_v100 dspb = new DescribeSensorParamterBuilder_v100("sensorId", DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML);
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			Map<String, String> parameters = dspb.getParameters();
//			
//			// method: isDescribeSensorDefined(...)
//			assertTrue(sw.isDescribeSensorDefined(om));
//			
//			// method: createParameterContainerForDoDescribeSensor(...)
//			ParameterContainer pc = sw.createParameterContainerForDoDescribeSensor(parameters);
//			String service = (String) pc.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue();
//			String procedure = (String) pc.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_PROCEDURE_PARAMETER).getSpecifiedValue();
//			String output = (String) pc.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals("sensorId", procedure);
//			assertEquals(DescribeSensorParamterBuilder_v100.OUTPUT_FORMAT_SENSORML, output);
//			
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testDoGetObservationMethods() {
//
//		try {
//			GetObservationParameterBuilder_v100 gopb = new GetObservationParameterBuilder_v100("offering", "observedProperty", "responseFormat");
//			gopb.addSrsName("srsName");
//			gopb.addEventTime("eventTime");
//			gopb.addProcedure("procedure");
//			gopb.addFeatureOfInterest("featureOfInterest");
//			gopb.addResult("result");
//			gopb.addResultModel("resultModel");
//			gopb.addResponseMode("responseMode");
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			Map<String, Object> parameters = gopb.getParameters();
//			
//			// method: isGetObservationDefined(...)
//			assertTrue(sw.isGetObservationDefined(om));
//			
//			// method: createParameterContainerForGetObservation(...)
//			ParameterContainer pc = sw.createParameterContainerForGetOservation(parameters);
//			String service = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue();
//			String offering = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_OFFERING_PARAMETER).getSpecifiedValue();			
//			
//			ParameterShell observedPropertyPs = (ParameterShell) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
//			String[] observedPropertyArray = observedPropertyPs.getSpecifiedTypedValueArray(String[].class);
//			String observedProperty = observedPropertyArray[0];
//			
//			String responseFormat = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue();
//			String srs = (String) pc.getParameterShellWithServiceSidedName(SOSWrapper.GET_OBSERVATION_SRS_NAME_PARAMETER).getSpecifiedValue();
//			
//			ParameterShell eventTimePs = (ParameterShell) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER);
//			String[] eventTimeArray = eventTimePs.getSpecifiedTypedValueArray(String[].class);
//			String eventTime = eventTimeArray[0];
//			
//			ParameterShell procedurePs = (ParameterShell) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER);
//			String[] procedureArray = procedurePs.getSpecifiedTypedValueArray(String[].class);
//			String procedure = procedureArray[0];
//			
//			String foi = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER).getSpecifiedValue();
//			String result = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER).getSpecifiedValue();
//			String resultModel = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue();
//			String responseMode = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals("offering", offering);
//			assertEquals("observedProperty", observedProperty);
//			assertEquals("responseFormat", responseFormat);
//			assertEquals("srsName", srs);
//			
//			assertEquals("eventTime", eventTime);
//			assertEquals("procedure", procedure);
//			assertEquals("featureOfInterest", foi);
//			assertEquals("result", result);
//			assertEquals("resultModel", resultModel);
//			assertEquals("responseMode", responseMode);
//			
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	// @Test TODO activate
////	public void testDoRegisterSensorMethods() {
////
////		try {
////			SensorDescriptionBuilder sensorDescription = new SensorDescriptionBuilder("sensorId", REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
////			sensorDescription.setPosition("name", true, 0.1, 2.3);
////			sensorDescription.setObservedProperty("observedProperty");
////			sensorDescription.setUnitOfMeasurement("uom");
////			String sensorML = sensorDescription.generateSensorDescription();
////			
////			ObservationTemplateBuilder templateBuilder = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCategory("codeSpace");
////			String obsTemp = templateBuilder.generateObservationTemplate();
////			
////			RegisterSensorParameterBuilder_v100 rspb = new RegisterSensorParameterBuilder_v100(sensorML, obsTemp);
////			
////			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
////			Map<String, String> parameters = rspb.getParameters();
////			
////			// method: isRegisterSensorDefined(...)
////			assertTrue(sw.isGetObservationDefined(om));
////			
////			// method: createParameterContainerForRegisterSensor(...)
////			ParameterContainer pc = sw.createParameterContainerForRegisterSensor(parameters);
////			String service = (String) pc.getParameterShellWithServiceSidedName(REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue();
////			String version = (String) pc.getParameterShellWithServiceSidedName(REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue();
////			String sensorDescripiton = (String) pc.getParameterShellWithServiceSidedName(REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue();			
////			String observationTemplate = (String) pc.getParameterShellWithServiceSidedName(REGISTER_SENSOR_OBSERVATION_TEMPLATE).getSpecifiedValue();
////			
////			assertEquals("SOS", service);
////			assertEquals("1.0.0", version);
////			assertEquals(parameters.get(REGISTER_SENSOR_ML_DOC_PARAMETER), sensorDescripiton);
////			assertEquals(parameters.get(REGISTER_SENSOR_OBSERVATION_TEMPLATE), observationTemplate);
////			
////		} catch (ExceptionReport e) {
////			e.printStackTrace();
////		} catch (OXFException e) {
////			e.printStackTrace();
////		}
////	}
//	
//	@Test
//	public void testDoInsertObservation() {
//
//		try {			
//			CategoryObservationBuilder categoryObservation = ObservationParameters.createObservationForTypeCategory();
//			categoryObservation.addFoiDescription("foiDescription");
//			categoryObservation.addFoiId("foiId");
//			categoryObservation.addNewFoiName("foiName");
//			categoryObservation.addFoiPosition("foiPosition");
//			categoryObservation.addObservationValue("observationValue");
//			categoryObservation.addOservedProperty("observedProperty");
//			categoryObservation.addSamplingTime("samplingTime");
//			categoryObservation.addSrsPosition("srsPosition");
//			categoryObservation.addResultCodespace("resultCodespace");
//			
//			InsertObservationParameters iopb = new InsertObservationParameters("sensorId", categoryObservation);
//			
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			Map<String, String> parameters = iopb.getParameters();
//			
//			// method: isInsertObservationDefined(...)
//			assertTrue(sw.isGetObservationDefined(om));
//			
//			// method: createParameterContainerForInsertObservation(...)
//			ParameterContainer pc = sw.createParameterContainerForInsertObservation(parameters);
//			String service = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue();
//			String foiDescription = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_DESC).getSpecifiedValue();
//			String foiId = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue();
//			String foiName = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_NAME).getSpecifiedValue();
//			String foiPosition = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_POSITION).getSpecifiedValue();
//			String observationValue = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue();
//			String observedProperty = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue();
//			String samplingTime = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
//			String srsPosition = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_POSITION_SRS).getSpecifiedValue();
//			String resultCodespace = (String) pc.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals("foiDescription", foiDescription);
//			assertEquals("foiId", foiId);
//			assertEquals("foiName", foiName);
//			assertEquals("foiPosition", foiPosition);
//			assertEquals("observationValue", observationValue);
//			assertEquals("observedProperty", observedProperty);
//			assertEquals("samplingTime", samplingTime);
//			assertEquals("srsPosition", srsPosition);
//			assertEquals("resultCodespace", resultCodespace);
//			
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testDoGetObservationByIdMethods() {
//
//		try {
//			GetObservationByIdParameterBuilder_v100 gobipb = new GetObservationByIdParameterBuilder_v100("observationId", "responseFormat");
//			gobipb.addSrsName("srsName");
//			gobipb.addResponseMode("responseMode");
//			gobipb.addResultModel("resultModel");
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			
//			// method: isGetObservationByIdDefined(...)
//			assertTrue(sw.isGetObservationByIdDefined(om));
//			Map<String, String> parameters = gobipb.getParameters();
//						
//			// method: createParameterContainerForGetObservationById(...)
//			ParameterContainer pc = sw.createParameterContainerForGetObservationById(parameters);
//			String service = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_VERSION_PARAMETER).getSpecifiedValue();
//			String observationId = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER).getSpecifiedValue();
//			String responseFormat = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue();
//			String srsName = (String) pc.getParameterShellWithServiceSidedName(SOSWrapper.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER).getSpecifiedValue();
//			String responseMode = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER).getSpecifiedValue();
//			String resultModel = (String) pc.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals("observationId", observationId);
//			assertEquals("responseFormat", responseFormat);
//			assertEquals("srsName", srsName);
//			assertEquals("responseMode", responseMode);
//			assertEquals("resultModel", resultModel);
//			
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testDoGetFeatureOfInterestMethods() {
//
//		try {
//			GetFeatureOfInterestParameterBuilder_v100 gfoipb = new GetFeatureOfInterestParameterBuilder_v100("identification", GET_FOI_ID_PARAMETER);
//			gfoipb.addEventTime("eventTime");
//			OperationsMetadata om = sw.getServiceDescriptor().getOperationsMetadata();
//			
//			// method: isGetFeatureOfInterestDefined(...)
//			assertTrue(sw.isGetFeatureOfInterestDefined(om));
//			Map<String, String> parameters = gfoipb.getParameters();
//			
//			// method: createParameterContainerForGetFeatureOfInterest(...)
//			ParameterContainer pc = sw.createParameterContainerForGetFeatureOfInterest(parameters);
//			String service = (String) pc.getParameterShellWithServiceSidedName(GET_FOI_SERVICE_PARAMETER).getSpecifiedValue();
//			String version = (String) pc.getParameterShellWithServiceSidedName(GET_FOI_VERSION_PARAMETER).getSpecifiedValue();
//			String identification = null;
//			if ((String) pc.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER).getSpecifiedValue() != null) {
//				identification = (String) pc.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER).getSpecifiedValue();
//			} else {
//				identification = (String) pc.getParameterShellWithServiceSidedName(GET_FOI_LOCATION_PARAMETER).getSpecifiedValue();
//			}
//			String eventTime = (String) pc.getParameterShellWithServiceSidedName(GET_FOI_EVENT_TIME_PARAMETER).getSpecifiedValue();
//			
//			assertEquals("SOS", service);
//			assertEquals("1.0.0", version);
//			assertEquals("identification", identification);
//			assertEquals("eventTime", eventTime);
//			
//		} catch (ExceptionReport e) {
//			e.printStackTrace();
//		} catch (OXFException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private static class MySosWrapperSeam extends SOSWrapper {
//
//		protected MySosWrapperSeam(String serviceBaseUrl, ServiceDescriptor capabilities) {
//			super(capabilities);
//		}
//
//		public static MySosWrapperSeam createFromCapabilitiesFile(File capsFile, String acceptVersion) throws ExceptionReport, OXFException, FileNotFoundException, IOException {
//			SOSAdapter adapter = new SOSAdapter(acceptVersion);
//			
//			InputStream is = new FileInputStream(capsFile);
//			long length = capsFile.length();
//			if (length > Integer.MAX_VALUE) {
//				is.close();
//		        throw new OXFException("Test failure caused by a too large SOSCapabilities file!");
//		    }
//			byte[] incomingResult = new byte[(int)length];
//			int offset = 0;
//		    int read = 0;
//		    while (offset < incomingResult.length && (read = is.read(incomingResult, offset, incomingResult.length - offset)) >= 0) {
//		        offset += read;
//		    }
//		    is.close();
//			
//			ServiceDescriptor capabilities;
//			try {
//	            if (SosUtil.isVersion100(acceptVersion)) {
//	                net.opengis.sos.x10.CapabilitiesDocument capsDoc = net.opengis.sos.x10.CapabilitiesDocument.Factory.parse(new ByteArrayInputStream(incomingResult));
//	                capabilities = adapter.initService(capsDoc);
//	            } else {
//	            	throw new OXFException("Version not yet supported!");
//	            }
//
//	        }
//	        catch (XmlException e) {
//	            throw new OXFException(e);
//	        }
//	        catch (IOException e) {
//	            throw new OXFException(e);
//	        }
//
//			return new MySosWrapperSeam("path", capabilities);
//		}
//	}
//
//}