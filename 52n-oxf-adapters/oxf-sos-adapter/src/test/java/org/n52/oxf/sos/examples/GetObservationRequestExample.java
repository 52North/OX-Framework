/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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
package org.n52.oxf.sos.examples;

import static org.n52.oxf.ows.capabilities.Parameter.COMMON_NAME_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_OBSERVATION;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimePeriod;

@Ignore // comment out to run demo class via JUnit
public class GetObservationRequestExample extends SosAdapterRequestExample {

    @Override
	@Before
    public void setUp() throws Exception {
        super.setUp();

        // you can increase timeouts for large observation intervals
        //adapter.setHttpClient(new SimpleHttpClient(60000, 60000));
    }

    @Test
    public void getObservation() {
        performOperationParseResult(createGetObservationOperation());
    }

    private Operation createGetObservationOperation() {
        return new Operation(GET_OBSERVATION, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        final ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SERVICE, "SOS");
        parameters.addParameterShell(VERSION, "1.0.0");
        parameters.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, "WASSERTEMPERATUR_ROHDATEN");
        parameters.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, "Wassertemperatur");
        parameters.addParameterShell(GET_OBSERVATION_PROCEDURE_PARAMETER, "Wassertemperatur-Stoer-Sperrwerk_Bp_5970040");
        parameters.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/1.0.0\"");
        parameters.addParameterShell(createTimeConstraintParameter());
        return parameters;
    }

    private ParameterShell createTimeConstraintParameter() throws OXFException {
        final ITime last24Hours = createLast24HoursInterval();
        final Parameter timeConstraint = createTimeParameterFor(last24Hours);
        return new ParameterShell(timeConstraint, last24Hours);
    }

    private Parameter createTimeParameterFor(final ITime timeValue) {
        final TemporalValueDomain domain = new TemporalValueDomain(timeValue);
        return new Parameter(GET_OBSERVATION_EVENT_TIME_PARAMETER, true, domain, COMMON_NAME_TIME);
    }

    private ITime createLast24HoursInterval() {
        final SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        final long nowInMillis = System.currentTimeMillis();
        final long aDayInMillis = 1 * 24 * 60 * 60 * 1000;
        final long lastWeekInMillis = nowInMillis - aDayInMillis;
        final String now = iso8601.format(new Date(nowInMillis));
        final String lastWeek = iso8601.format(new Date(lastWeekInMillis));
        return new TimePeriod(lastWeek, now);
    }

}
