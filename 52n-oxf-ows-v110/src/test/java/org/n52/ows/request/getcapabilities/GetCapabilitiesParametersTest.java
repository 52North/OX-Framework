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
