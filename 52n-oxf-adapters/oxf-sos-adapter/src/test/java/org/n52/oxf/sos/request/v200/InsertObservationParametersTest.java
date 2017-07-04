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
package org.n52.oxf.sos.request.v200;

import java.util.Collections;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.oxf.om.x20.BooleanParameter;
import org.n52.oxf.om.x20.OmParameter;
import org.n52.oxf.sos.observation.DefaultObservationParametersFactory;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.xml.XMLConstants;

// TODO add more tests regarding offeringIds and observations
public class InsertObservationParametersTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ObservationParameters observationParameters;
    private MeasurementObservationParameters mop;
    private List<String> offerings;

    @Before
    public void setUp() {
        observationParameters = new DefaultObservationParametersFactory()
                .createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION);
        observationParameters.addFoiId("test-foid");
        observationParameters.addProcedure("test-procedure");
        mop = (MeasurementObservationParameters) observationParameters;
        mop.addObservationValue("52.0");
        mop.addUom("test-uom");
        offerings = Collections.singletonList("test-offering");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfMissingAllParameters() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters(null, null);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfMissingProcedureId() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'offerings' is required and may not be null or empty!");
        final MeasurementObservationParameters measurementParameters =
                (MeasurementObservationParameters) new DefaultObservationParametersFactory()
                        .createObservationParametersFor(XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION);
        measurementParameters.addObservationValue("1.0");
        measurementParameters.addUom("uom");
        new InsertObservationParameters(measurementParameters, null);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfMissingObservationParameter() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters(null, Collections.singletonList("sdf"));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionIfReceivingInvalidObservationParameter() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Parameter 'ObservationParameters' is required and may not be null or empty!");
        new InsertObservationParameters( new DefaultObservationParametersFactory()
                .createObservationParametersFor(
                        XMLConstants.QNAME_OM_1_0_MEASUREMENT_OBSERVATION),
                        Collections.singletonList("sdf"));
    }

    @Test
    public void shouldReturnTrueOnlyIfOmParameterIsSet() {
        OmParameter[] omParameters = {new BooleanParameter("test", true)};
        InsertObservationParameters parameters =
                new InsertObservationParameters(observationParameters, offerings, omParameters);
        Assert.assertThat(parameters.isSetOmParameter(), CoreMatchers.is(true));

        parameters = new InsertObservationParameters(observationParameters, offerings);
        Assert.assertThat(parameters.isSetOmParameter(), CoreMatchers.is(false));

        parameters = new InsertObservationParameters(observationParameters, offerings, new OmParameter[0]);
        Assert.assertThat(parameters.isSetOmParameter(), CoreMatchers.is(false));
    }

    @Test
    public void shouldNotThrowIllegalArgumentExceptionIfOmParameterIsNullOrEmpty() {
        new InsertObservationParameters(observationParameters, offerings, null);
        new InsertObservationParameters(observationParameters, offerings, new OmParameter[0]);
    }

    @Test
    public void shouldAlwaysReturnEmptyArrayOrContentForGetOmParameter() {
        InsertObservationParameters iop1 = new InsertObservationParameters(observationParameters, offerings, null);
        InsertObservationParameters iop2 = new InsertObservationParameters(
                observationParameters, offerings, new OmParameter[0]);
        final BooleanParameter bp = new BooleanParameter("test-name", true);
        InsertObservationParameters iop3 = new InsertObservationParameters(
                observationParameters, offerings, bp);

        Assert.assertThat(iop1.getOmParameter(), CoreMatchers.notNullValue());
        Assert.assertThat(iop1.getOmParameter().length, CoreMatchers.is(0));

        Assert.assertThat(iop2.getOmParameter(), CoreMatchers.notNullValue());
        Assert.assertThat(iop2.getOmParameter().length, CoreMatchers.is(0));

        Assert.assertThat(iop3.getOmParameter(), CoreMatchers.notNullValue());
        Assert.assertThat(iop3.getOmParameter().length, CoreMatchers.is(1));
        Assert.assertThat(iop3.getOmParameter()[0], CoreMatchers.is(bp));

    }

}
