package org.n52.oxf.sos.adapter;

import static org.junit.Assert.*;

import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.ows.ExceptionReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test of corectnes for:
 * 		- instantiation
 * 		- doDescribeSensor([...])
 * 
 * @author Eric
 */
public class SOSWrapperTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SOSWrapperTest.class);
	
	// CONFIGURATION NEEDED TO RUN TEST !!! ***********************************
	private final String URL = "";
	private final String VERSION = "";
	// ************************************************************************
	
	/**
	 * Test of instantiation with two parameters:
	 * 		- url of the SOS
	 * 		- version of the SOS
	 */
	// TODO @Test
//	public void testInstantiation() {
//		try {
//			SOSWrapper sw = SOSWrapper.createFromCapabilities(URL, VERSION);
//		} catch (ExceptionReport e) {
//			LOGGER.error("Error while instantiation!", e);
//			fail();
//		} catch (OXFException e) {
//			LOGGER.error("Error while instantiation!", e);
//			fail();
//		}
//	}
	
	// CONFIGURATION NEEDED TO RUN TEST !!! ***********************************
	private final String SENSOR_ID = "";
	private final String OUTPUT_FORMAT = "";
	// ************************************************************************
	
	// @Test
//	public void testDoDescribeSensor() {
//		try {
//			SOSWrapper sw = SOSWrapper.createFromCapabilities(URL, VERSION);
//			
//			DescribeSensorParamterBuilder_v100 dspb = new DescribeSensorParamterBuilder_v100(SENSOR_ID, OUTPUT_FORMAT);
//			OperationResult or = sw.doDescribeSensor(dspb);
//			
//			
//		} catch (ExceptionReport e) {
//			LOGGER.error("Error while instantiation!", e);
//			fail();
//		} catch (OXFException e) {
//			LOGGER.error("Error while instantiation!", e);
//			fail();
//		}
//	}

}
