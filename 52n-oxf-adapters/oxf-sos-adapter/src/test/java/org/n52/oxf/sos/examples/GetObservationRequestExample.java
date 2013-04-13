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
package org.n52.oxf.sos.examples;

import static org.n52.oxf.ows.capabilities.Parameter.COMMON_NAME_TIME;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.GET_OBSERVATION;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
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
    
    @Before
    public void setUp() throws Exception {
        super.setUp();

        // you can increase timeouts for large observation intervals
        //adapter.setHttpClient(new SimpleHttpClient(60000, 60000));
    }

    @Test
    public void getObservation() {
        handleOperation(createGetObservationOperation());
    }

    private Operation createGetObservationOperation() {
        return new Operation(GET_OBSERVATION, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(GET_OBSERVATION_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(GET_OBSERVATION_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(GET_OBSERVATION_OFFERING_PARAMETER, "WASSERTEMPERATUR_ROHDATEN");
        parameters.addParameterShell(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, "Wassertemperatur");
        parameters.addParameterShell(GET_OBSERVATION_PROCEDURE_PARAMETER, "Wassertemperatur-Stoer-Sperrwerk_Bp_5970040");
        parameters.addParameterShell(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, "text/xml;subtype=\"om/1.0.0\"");
        parameters.addParameterShell(createTimeConstraintParameter());
        return parameters;
    }
    
    private ParameterShell createTimeConstraintParameter() throws OXFException {
        ITime last24Hours = createLast24HoursInterval();
        Parameter timeConstraint = createTimeParameterFor(last24Hours);
        return new ParameterShell(timeConstraint, last24Hours);
    }

    private Parameter createTimeParameterFor(ITime timeValue) {
        TemporalValueDomain domain = new TemporalValueDomain(timeValue);
        return new Parameter(GET_OBSERVATION_EVENT_TIME_PARAMETER, true, domain, COMMON_NAME_TIME);
    }
    
    private ITime createLast24HoursInterval() {
        SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        long nowInMillis = System.currentTimeMillis();
        long aWeekInMillis = 1 * 24 * 60 * 60 * 1000;
        long lastWeekInMillis = nowInMillis - aWeekInMillis;
        String now = iso8601.format(new Date(nowInMillis));
        String lastWeek = iso8601.format(new Date(lastWeekInMillis));
        return new TimePeriod(lastWeek, now);
    }

}
