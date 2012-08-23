package org.n52.oxf.sos.adapter;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

/**
 * Test of correctness for:
 * 		- legal and illegal constructor parameters
 * 		- applying and getting mandatory parameters
 * 		- applying and getting optional parameters
 * 
 * @author Eric
 */
public class GetFeatureOfInterestParameterBuilder_v100Test {

	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new GetFeatureOfInterestParameterBuilder_v100("", ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
		new GetFeatureOfInterestParameterBuilder_v100("", ISOSRequestBuilder.GET_FOI_LOCATION_PARAMETER);
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new GetFeatureOfInterestParameterBuilder_v100(null, null);
		new GetFeatureOfInterestParameterBuilder_v100("", null);
		new GetFeatureOfInterestParameterBuilder_v100(null, "");
		new GetFeatureOfInterestParameterBuilder_v100("", "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		GetFeatureOfInterestParameterBuilder_v100 gfpb = new GetFeatureOfInterestParameterBuilder_v100
				("identification", ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
		
		HashMap<String, String> hm = (HashMap<String, String>) gfpb.getParameters();
		String parMan_01 = hm.get(ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
		
		assertEquals("identification", parMan_01);
	}
	
	/**
	 * Checks, whether the optional parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingOptionalParameters() {
		GetFeatureOfInterestParameterBuilder_v100 gfpb = new GetFeatureOfInterestParameterBuilder_v100
				("identification", ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
		
		HashMap<String, String> hm = (HashMap<String, String>) gfpb.getParameters();
		
		assertNull(hm.get(ISOSRequestBuilder.GET_FOI_EVENT_TIME_PARAMETER));
		
		gfpb.addEventTime("eventTimeOld");
		gfpb.addEventTime("eventTime");
		
		String parOpt_01 = hm.get(ISOSRequestBuilder.GET_FOI_EVENT_TIME_PARAMETER);
		
		assertEquals("eventTime", parOpt_01);
	}

}
