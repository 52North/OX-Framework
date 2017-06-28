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
package org.n52.ows.request.getcapabilities;


import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.ACCEPT_FORMAT;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.ACCEPT_VERSIONS;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.SECTIONS;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.SERVICE;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.UPDATE_SEQUENCE;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesSection.CONTENTS;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesSection.OPERATION_METADATA;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.request.RequestParameters;

public class GetCapabilitiesParametersTest {

    private GetCapabilitiesParameters requestParameters;

    @Before public void 
    setUp()
    {
        requestParameters = new GetCapabilitiesParameters("SPS");
    }
    
    @Test public void
    shouldHaveNonNullParameterCollections()
    {
        assertThat(requestParameters.getStandardParameters(), is(not((nullValue()))));
        assertThat(requestParameters.getNonStandardParameters(), is(not((nullValue()))));
    }

    @Test public void
    shouldHavePreInitializedServiceParameter()
    {
        RequestParameters parameters = requestParameters.getStandardParameters();
        assertThat(parameters.getSingleValue(SERVICE.name()), is("SPS"));
    }

    @Test public void
    shouldHoldUpdateSequenceAsStandardParameter()
    {
        requestParameters.setUpdateSequence("42");
        RequestParameters parameters = requestParameters.getStandardParameters();
        assertThat(parameters.getSingleValue(UPDATE_SEQUENCE.name()), is("42"));
    }

    @Test public void
    shouldHoldAcceptVersionsAsStandardParameters()
    {
        requestParameters.addAcceptVersions("42", "17");
        Collection<String> values = getValuesForStandardParameter(ACCEPT_VERSIONS);
        Iterator<String> iterator = values.iterator();
        assertThat(iterator.next(), is("42"));
        assertThat(iterator.next(), is("17"));
    }
    
    @Test public void
    shouldHoldSectionsAsStandardParameters()
    {
        requestParameters.addSections(CONTENTS, OPERATION_METADATA);
        Collection<String> values = getValuesForStandardParameter(SECTIONS);
        assertThat(values.contains(CONTENTS.toString()), is(true));
        assertThat(values.contains(OPERATION_METADATA.toString()), is(true));
    }
    
    @Test public void
    shouldHoldAcceptFormatsAsStandardParameters()
    {
        requestParameters.addAcceptedFormats("text/xml", "application/json");
        Collection<String> values = getValuesForStandardParameter(ACCEPT_FORMAT);
        Iterator<String> iterator = values.iterator();
        assertThat(iterator.next(), is("text/xml"));
        assertThat(iterator.next(), is("application/json"));
    }
    
    @Test public void
    shouldAcceptAnyNonStandardParameter()
    {
        requestParameters.addNonStandardParameter("foo", "bar");
        Collection<String> values = getValuesForNonStandardParameter("foo");
        Iterator<String> iterator = values.iterator();
        assertThat(iterator.next(), is("bar"));
    }
    
    private Collection<String> getValuesForStandardParameter(Enum< ? > parameter) {
        RequestParameters parameters = requestParameters.getStandardParameters();
        return getCollectionFor(parameter.name(), parameters);
    }

    private Collection<String> getValuesForNonStandardParameter(String parameter) {
        RequestParameters parameters = requestParameters.getNonStandardParameters();
        return getCollectionFor(parameter, parameters);
    }
    

    private Collection<String> getCollectionFor(String parameter, RequestParameters parameters) {
        return (Collection<String>) parameters.getAllValues(parameter);
        
    }

    @Test public void
    shouldReturnSelfInstanceAfterEachOperation()
    {
        assertThat(requestParameters.addSections(), is(requestParameters));
        assertThat(requestParameters.addAcceptVersions(), is(requestParameters));
        assertThat(requestParameters.addAcceptedFormats(""), is(requestParameters));
        assertThat(requestParameters.setUpdateSequence(null), is(requestParameters));
        assertThat(requestParameters.addNonStandardParameter("", ""), is(requestParameters));
    }
}
