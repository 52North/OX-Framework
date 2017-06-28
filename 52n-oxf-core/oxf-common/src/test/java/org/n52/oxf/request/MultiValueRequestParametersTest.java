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
package org.n52.oxf.request;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.request.MultiValueRequestParametersTest.TestEnum.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MultiValueRequestParametersTest {

    private RequestParameters requestParameters;

    @Before public void
    setUp()
    {
        requestParameters = new MultiValueRequestParametersSeam();
    }
    
    @Test
    public void 
    shouldBeEmptyJustAfterCreation()
    {
        assertThat(requestParameters.isEmpty(), is(true));
    }

    @Test public void 
    shouldContainAddedParameter()
    {
        requestParameters.addParameterValue("version", "1.0.0");
        assertThat(requestParameters.contains("version"), is(true));
    }
    
    @Test public void 
    shouldIndicateThatAddingParameterValueChangedSize()
    {
        assertThat(requestParameters.addParameterValue("version", "1.0.0"), is(true));
    }
    
    @Test public void 
    shouldContainAddedValues() 
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterValue("version", "2.0.0");
        final Collection<String> values = getValueCollectionFor("version");
        assertThat(values.contains("1.0.0"), is(true));
        assertThat(values.contains("2.0.0"), is(true));
    }

    @Test public void 
    shouldDetermineSingleValueForParameterWhenOneValueIsPresent()
    {
        requestParameters.addParameterValue("version", "1.0.0");
        assertThat(requestParameters.isSingleValue("version"), is(true));
    }
    
    @Test public void 
    shouldNotDetermineHasSingleValueForParameterWhenMultipleValuesArePresent()
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterValue("version", "2.0.0");
        assertThat(requestParameters.isSingleValue("version"), is(false));
    }
    
    @Test public void 
    shouldDetermineMultipleValuesForParameterWhenMultipleValuesArePresent() 
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterValue("version", "2.0.0");
        assertThat(requestParameters.hasMultipleValues("version"), is(true));
    }

    @Test public void 
    shouldNotDetermineMultipleValuesForParameterWhenSingleValueIsPresent() 
    {
        requestParameters.addParameterValue("version", "1.0.0");
        assertThat(requestParameters.hasMultipleValues("version"), is(false));
    }

    @Test public void 
    shouldNotDetermineNonPresentValueAsSingle()
    {
        assertThat(requestParameters.isSingleValue("version"), is(false));
    }
    
    @Test public void 
    shouldNotDetermineNonPresentValueAsMultipleValue()
    {
        assertThat(requestParameters.hasMultipleValues("version"), is(false));
    }
    
    @Test public void 
    shouldReturnEmptyStringWhenSingleValueIsAskedAndParameterIsNotPresent()
    {
        requestParameters.addParameterValue("version", "1.0.0");
        assertThat(requestParameters.getSingleValue("does_not_exist"), is(""));
    }
    
    @Test public void 
    shouldReturnAllParameterNamesAddedViaVarArgs()
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterStringValues("service", "SOS", "SPS");
        final Collection<String> parameters = requestParameters.getParameterNames();
        assertThat("version is missing.", parameters.contains("version"), is(true));
        assertThat("service is missing", parameters.contains("service"), is(true));
    }
    
    @Test public void 
    shouldContainAllValuesForParameterAddedViaVarArgs() {
        requestParameters.addParameterStringValues("service", "SOS", "SPS");
        final Collection<String> services = getValueCollectionFor("service");
        assertThat(services.contains("SOS"), is(true));
        assertThat(services.contains("SPS"), is(true));
    }
    
    @Test public void 
    shouldAddParameterWithEmptyArrayValueViaVarArgs() {
        requestParameters.addParameterStringValues("service", new String[0]);
        final Iterable<String> services = requestParameters.getAllValues("service");
        assertThat((Collection<String>) services, is(empty()));
    }
    
    @Test public void 
    shouldAddParameterWithNullValueViaVarArgs() {
        requestParameters.addParameterStringValues("service", (String[]) null);
        final Iterable<String> services = requestParameters.getAllValues("service");
        assertThat((Collection<String>) services, is(empty()));
    }

    @Test public void 
    shouldContainAllValuesForParameterAddedViaIterable()
    {
        final List<String> parameterValues = new ArrayList<String>();
        parameterValues.add("1.0.0");
        parameterValues.add("2.0.0");
        requestParameters.addParameterValues("version", parameterValues);
        final Collection<String> versions = getValueCollectionFor("version");
        assertThat(versions.contains("1.0.0"), is(true));
        assertThat(versions.contains("2.0.0"), is(true));
    }
    
    @Test public void
    shouldIndicateSizeChangeWhenAddingMultipleValues() {
        assertThat(requestParameters.addParameterStringValues("version", "1.0.0", "2.0.0"), is(true));
        assertThat(requestParameters.addParameterStringValues("version", "1.0.0", "2.0.0"), is(false));
    }
    
    @Test public void
    shouldPreserveOrderWhenAddingMultipleValuesViaStringVarArgs() {
        requestParameters.addParameterStringValues("version", "2.0.0", "1.0.0");
        final Collection<String> versions = getValueCollectionFor("version");
        assertThat(versions.size(), is(2));
        assertThat(versions.contains("2.0.0"), is(true));
        assertThat(versions.contains("1.0.0"), is(true));
    }
    
    @Test public void
    shouldPreserveOrderWhenAddingMultipleValuesViaDifferentCalls() {
        requestParameters.addParameterValue("version", "3.0.0");
        requestParameters.addParameterStringValues("version", "2.0.0", "1.0.0");
        final Collection<String> versions = getValueCollectionFor("version");
        final Iterator<String> iterator = versions.iterator();
        assertThat(iterator.next(), is("3.0.0"));
        assertThat(iterator.next(), is("2.0.0"));
        assertThat(iterator.next(), is("1.0.0"));
    }
    
    @Test public void
    shouldPreserveOrderWhenAddingMultipleValuesViaEnumVarArgs() {
        requestParameters.addParameterEnumValues("keys", SERVICE, VERSION);
        final Collection<String> versions = getValueCollectionFor("keys");
        final Iterator<String> iterator = versions.iterator();
        assertThat(iterator.next(), is("service"));
        assertThat(iterator.next(), is("version"));
    }

    @Test public void 
    shouldContainAllValuesFromTwoMergedRequestParameters()
    {
        requestParameters.addParameterStringValues("version", "1.0.0", "2.0.0");
        final RequestParameters other = new MultiValueRequestParametersSeam();
        other.addParameterStringValues("version", "2.0.0", "3.0.0");
        other.addParameterStringValues("service", "SPS", "SOS");
        requestParameters.mergeWith(other);
        final Collection<String> versions = getValueCollectionFor("version");
        assertThat(versions.contains("1.0.0"), is(true));
        assertThat(versions.contains("2.0.0"), is(true));
        final Collection<String> services = getValueCollectionFor("service");
        assertThat(services.contains("SPS"), is(true));
        assertThat(services.contains("SOS"), is(true));
    }

    @Test public void 
    shouldAddSingleStringParameterValue() {
        assertThat(requestParameters.addParameterValue("version", "1.0.0"), is(true));
        assertThat(requestParameters.contains("version"), is(true));
        assertThat(requestParameters.getSingleValue("version"), is("1.0.0"));
    }

    @Test public void 
    shouldNotContainParameterAfterRemovingItAndReturnPreviouslyAssignedValues() 
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterValue("version", "2.0.0");
        final Collection<String> removedValues = requestParameters.remove("version");
        assertThat(requestParameters.contains("version"), is(false));
        assertThat(removedValues.size(), is(2));
        assertThat(removedValues.contains("1.0.0"), is(true));
        assertThat(removedValues.contains("2.0.0"), is(true));
    }

    @Test public void 
    shouldReturnEmptyCollectionWhenRemovingNonExistingParameter() 
    {
        final Collection<String> removedValues = requestParameters.remove("version");
        assertThat(removedValues, isA(Collection.class));
        assertThat(removedValues, is(empty()));
    }
    
    @Test public void 
    shouldHaveNoValuesAnymoreAfterRemovingAllValues() 
    {
        requestParameters.addParameterValue("version", "1.0.0");
        requestParameters.addParameterValue("version", "2.0.0");
        requestParameters.addParameterValue("service", "SPS");
        requestParameters.removeAll();
        assertThat(requestParameters.isEmpty(), is(true));
    }

    @Test public void 
    shouldAddEmptyListIfNullIsAddedAsParameterEnum()
    {
    	final String parameter = "test-parameter";
		requestParameters.addParameterEnumValues(parameter, (Enum[])null);
    	final Iterator<String> iterator = requestParameters.getAllValues(parameter).iterator();
		assertThat(iterator.hasNext(), is(false));
    }
    
    @Test public void
    shouldReturnEmptyListWhenRequestingAllElementsForNotContainedParameter()
    {
    	final String parameter = "test";
		assertThat(requestParameters.contains(parameter),is(false));
		final Iterable<String> allValues = requestParameters.getAllValues(parameter);
		assertThat(((List<String>)allValues).isEmpty(), is(true));
    }
    
    @Test public void
    shouldReturnTrueIfNoValueIsAssociatedToParameterOrParameterIsNotContained()
    {
    	final String parameter = "parameter";
		assertThat(requestParameters.isEmpty(parameter),is(true));
		requestParameters.addParameterValue(parameter, null);
		assertThat(requestParameters.isEmpty(parameter),is(true));
		requestParameters.addParameterValue(parameter, "value");
		assertThat(requestParameters.isEmpty(parameter),is(false));
		requestParameters.addParameterValue(parameter, "value2");
		assertThat(requestParameters.isEmpty(parameter),is(false));
    }
    
    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test public void
    shouldThrowIllegalArgumentExceptionIfAddingNullOrEmptyValueViaAddNonEmpty()
    {
    	thrown.expect(IllegalArgumentException.class);
    	thrown.expectMessage(is("Parameter 'key' is required and may not be null or empty!"));
    	((MultiValueRequestParameters)requestParameters).addNonEmpty("key", "");
    }
    
    @Test public void
    shouldAddValueViaAddNonEmpty()
    {
    	final String key = "key";
		final String value = "value";
		assertThat(((MultiValueRequestParameters)requestParameters).addNonEmpty(key,value),is(true));
    	assertThat(requestParameters.contains(key),is(true));
    	assertThat(requestParameters.isEmpty(key),is(false));
    	assertThat(requestParameters.isEmpty(),is(false));
    	assertThat(requestParameters.getSingleValue(key),is(value));
    }
    
    @Test public void
    shouldReturnTrueIfNoValueIsAssociatedToParameterViaIsEmptyValue()
    {
    	final String emptyValue = null;
    	final String nonEmptyValue = "value";
		final String parameter = "parameter";
		requestParameters.addParameterValue(parameter, emptyValue );
		final MultiValueRequestParameters parameters = (MultiValueRequestParameters)requestParameters;
		
		assertThat(parameters.isEmptyValue(parameter),is(true));
		requestParameters.addParameterValue(parameter, nonEmptyValue);
		assertThat(parameters.isEmptyValue(parameter),is(false));
    }

    private Collection<String> getValueCollectionFor(final String parameter) {
        return requestParameters.getAllValues(parameter);
    }
    
    enum TestEnum {
        VERSION, SERVICE;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    private class MultiValueRequestParametersSeam extends MultiValueRequestParameters {}
}
