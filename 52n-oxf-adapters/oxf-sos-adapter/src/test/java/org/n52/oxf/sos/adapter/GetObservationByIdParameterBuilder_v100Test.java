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
package org.n52.oxf.sos.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationByIdParameterBuilder_v100;

/**
 * Test of correctness for:
 * 		- legal and illegal constructor parameters
 * 		- applying and getting mandatory parameters
 * 		- applying and getting optional parameters
 * 
 * @author Eric
 */
public class GetObservationByIdParameterBuilder_v100Test {

	/**
	 * Checks the behaviour on valid constructor parameters.
	 */
	@Test
	public void testValidConstructorParameters() {
		new GetObservationByIdParameterBuilder_v100("", "");
	}
	
	/**
	 * Checks the behaviour on invalid constructor parameters.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructorParameters() {
		new GetObservationByIdParameterBuilder_v100(null, null);
		new GetObservationByIdParameterBuilder_v100("", null);
		new GetObservationByIdParameterBuilder_v100(null, "");
	}
	
	/**
	 * Checks, whether the mandatory parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingMandatoryParameters() {
		GetObservationByIdParameterBuilder_v100 gobipb = new GetObservationByIdParameterBuilder_v100
				("observationId", "responseFormat");
		
		HashMap<String, String> hm = (HashMap<String, String>) gobipb.getParameters();
		String parMan_01 = hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER);
		String parMan_02 = hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER);
		
		assertEquals("observationId", parMan_01);
		assertEquals("responseFormat", parMan_02);
	}
	
	/**
	 * Checks, whether the optional parameters were applied correctly.
	 */
	@Test
	public void testApplyingAndGettingOptionalParameters() {
		GetObservationByIdParameterBuilder_v100 gobipb = new GetObservationByIdParameterBuilder_v100
				("observationId", "responseFormat");
		
		HashMap<String, String> hm = (HashMap<String, String>) gobipb.getParameters();
		
		assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER));
		assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER));
		assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER));
		
		gobipb.addSrsName("srsNameOld");
		gobipb.addSrsName("srsName");
		gobipb.addResultModel("resultModelOld");
		gobipb.addResultModel("resultModel");
		gobipb.addResponseMode("responseModeOld");
		gobipb.addResponseMode("responseMode");
		
		String parOpt_01 = hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_SRS_NAME_PARAMETER);
		String parOpt_02 = hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESULT_MODEL_PARAMETER);
		String parOpt_03 = hm.get(ISOSRequestBuilder.GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER);
		
		assertEquals("srsName", parOpt_01);
		assertEquals("resultModel", parOpt_02);
		assertEquals("responseMode", parOpt_03);
	}

}
