/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.sos.adapter.wrapper.builder.GetObservationParameterBuilder_v100;

/**
 * Test of correctness for:
 *      - legal and illegal constructor parameters
 *      - applying and getting mandatory parameters
 *      - applying and getting optional parameters
 *
 * @author Eric
 */
public class GetObservationParameterBuilder_v100Test {

    /**
     * Checks the behaviour on valid constructor parameters.
     */
    @Test
    public void testValidConstructorParameters() {
        new GetObservationParameterBuilder_v100("", "", "");
    }

    /**
     * Checks the behaviour on invalid constructor parameters.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructorParameters() {
        new GetObservationParameterBuilder_v100(null, null, null);
        new GetObservationParameterBuilder_v100("", null, null);
        new GetObservationParameterBuilder_v100(null, "", null);
        new GetObservationParameterBuilder_v100(null, null, "");
        new GetObservationParameterBuilder_v100("", "", null);
        new GetObservationParameterBuilder_v100("", null, "");
        new GetObservationParameterBuilder_v100(null, "", "");
    }

    /**
     * Checks, whether the mandatory parameters were applied correctly.
     */
    @Test
    public void testApplyingAndGettingMandatoryParameters() {
        GetObservationParameterBuilder_v100 dspb = new GetObservationParameterBuilder_v100
                ("offering", "observedProperty", "responseFormat");

        HashMap<String, Object> hm;
        try {
            hm = (HashMap<String, Object>) dspb.getParameters();
            String parMan_01 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER);
            ParameterShell parMan_02_ps = (ParameterShell) (hm.get(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
            String[] parMan_02_array = parMan_02_ps.getSpecifiedTypedValueArray(String[].class);
            String parMan_02 = parMan_02_array[0];
            String parMan_03 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER);

            assertEquals("offering", parMan_01);
            assertEquals("observedProperty", parMan_02);
            assertEquals("responseFormat", parMan_03);
        } catch (OXFException e) {
            System.out.println("Error while retrieving parameters!");
        }
    }

    /**
     * Checks, whether the optional parameters were applied correctly.
     */
    @Test
    public void testApplyingAndGettingOptionalParameters() {
        GetObservationParameterBuilder_v100 gopb = new GetObservationParameterBuilder_v100
                ("offering", "observedProperty1", "responseFormat");

        HashMap<String, Object> hm;
        try {
            hm = (HashMap<String, Object>) gopb.getParameters();
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER));
            assertNull(hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER));

            gopb.addSrsName("srsNameOld");
            gopb.addSrsName("srsName");
            gopb.addEventTime("eventTime1");
            gopb.addEventTime("eventTime2");
            gopb.addProcedure("procedure1");
            gopb.addProcedure("procedure2");
            gopb.addObservedProperty("observedProperty2");
            gopb.addFeatureOfInterest("featureOfInterestOld");
            gopb.addFeatureOfInterest("featureOfInterest");
            gopb.addResult("resultOld");
            gopb.addResult("result");
            gopb.addResultModel("resultModelOld");
            gopb.addResultModel("resultModel");
            gopb.addResponseMode("responseModeOld");
            gopb.addResponseMode("responseMode");

            hm = (HashMap<String, Object>) gopb.getParameters();

            String parOpt_01 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_SRS_NAME_PARAMETER);
            ParameterShell parOpt_02_ps = (ParameterShell) (hm.get(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER));
            String[] parOpt_02_1_array = parOpt_02_ps.getSpecifiedTypedValueArray(String[].class);
            String parOpt_02_1 = parOpt_02_1_array[0];
            String parOpt_02_2 = parOpt_02_1_array[1];
            ParameterShell parOpt_03_ps = (ParameterShell) (hm.get(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER));
            String[] parOpt_03_array = parOpt_03_ps.getSpecifiedTypedValueArray(String[].class);
            String parOpt_03_1 = parOpt_03_array[0];
            String parOpt_03_2 = parOpt_03_array[1];
            ParameterShell parOpt_04_ps = (ParameterShell) (hm.get(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
            String[] parOpt_04_array = parOpt_04_ps.getSpecifiedTypedValueArray(String[].class);
            String parOpt_04_1 = parOpt_04_array[0];
            String parOpt_04_2 = parOpt_04_array[1];
            String parOpt_05 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
            String parOpt_06 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER);
            String parOpt_07 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER);
            String parOpt_08 = (String) hm.get(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER);

            assertEquals("srsName", parOpt_01);
            assertEquals("eventTime1", parOpt_02_1);
            assertEquals("eventTime2", parOpt_02_2);
            assertEquals("procedure1", parOpt_03_1);
            assertEquals("procedure2", parOpt_03_2);
            assertEquals("observedProperty1", parOpt_04_1);
            assertEquals("observedProperty2", parOpt_04_2);
            assertEquals("featureOfInterest", parOpt_05);
            assertEquals("result", parOpt_06);
            assertEquals("resultModel", parOpt_07);
            assertEquals("responseMode", parOpt_08);
        } catch (OXFException e) {
            System.out.println("Error while retrieving parameters!");
        }
    }

}
