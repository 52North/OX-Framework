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
import org.junit.Test;

public class MultiValueRequestParametersTest {

    private RequestParameters requestParameters;

    @Before public void
    setUp()
    {
        requestParameters = new MultiValueRequestParameters();
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
        final Collection<String> parameters = (Collection<String>) requestParameters.getParameterNames();
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
        final RequestParameters other = new MultiValueRequestParameters();
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
    
    

    private Collection<String> getValueCollectionFor(final String parameter) {
        return (Collection<String>) requestParameters.getAllValues(parameter);
    }
    
    enum TestEnum {
        VERSION, SERVICE;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
