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
package org.n52.oxf.sos.request.v200;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.request.MultiValueRequestParameters.*;
import static org.n52.oxf.sos.request.v200.InsertSensorParameters.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class InsertSensorParametersTest {
	
	private final Collection<String> oneElementList = Collections.singletonList("test-element");
	
	private final Collection<String> emptyList = Collections.emptyList();
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test public void 
	shouldThrowExceptionIfMissingParameterProcedureDescription()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'procedureDescription' is required and may not be null or empty!");
		new InsertSensorParameters(null, null, null, null, null);
	}
	
	@Test public void
	shouldThrowExceptionIfEmptyParameterProcedureDescription()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'procedureDescription' is required and may not be null or empty!");
		new InsertSensorParameters("", null, null,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfMissingParameterProcedureDescriptionFormat()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'procedureDescriptionFormat' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", null, null,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfEmptyParameterProcedureDescriptionFormat()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'procedureDescriptionFormat' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", "", null,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfMissingParameterObservableProperties()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'observableProperties' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", "test-description-format",null,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfEmptyParameterObservableProperties()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'observableProperties' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", "test-description-format",emptyList,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfMissingParameterFeatureTypes()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'featureOfInterestTypes' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", "test-description-format",oneElementList,null,null);
	}
	
	@Test public void
	shouldThrowExceptionIfEmptyParameterFeatureTypes()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter 'featureOfInterestTypes' is required and may not be null or empty!");
		new InsertSensorParameters("test-description", "test-description-format",oneElementList,emptyList,null);
	}
	
	@Test public void 
	shouldSetServiceAndVersion()
	{
		final InsertSensorParameters parameters = new InsertSensorParameters("sdf", "sdf", 
				oneElementList, oneElementList, oneElementList);
		assertThat(parameters.getSingleValue(SERVICE_TYPE),is("SOS"));
		assertThat(parameters.getSingleValue(SERVICE_VERSION),is("2.0.0"));
	}

	@Test public void
	shouldSetObservableProperties()
	{
		final Collection<String> observableProperties = Arrays.asList("test-obsProp-1","test-obsProp-2","test-obsProp-3");
		final InsertSensorParameters parameters = new InsertSensorParameters("sdf", "sdf", 
				observableProperties, oneElementList, oneElementList);
		final Iterator<String> values = parameters.getAllValues(OBSERVABLE_PROPERTIES).iterator();
		checkMultipleValues(observableProperties, values);
	}
	
	@Test public void
	shouldSetFeatureOfInterestTypes()
	{
		final Collection<String> foiTypes = Arrays.asList("test-foi-type-1","test-foi-type-2","test-foi-type-3");
		final InsertSensorParameters parameters = new InsertSensorParameters("sdf", "sdf", 
				oneElementList, foiTypes, oneElementList);
		final Iterator<String> values = parameters.getAllValues(FEATURE_OF_INTEREST_TYPES).iterator();
		checkMultipleValues(foiTypes, values);
	}
	
	@Test public void
	shouldSetObservationTypes()
	{
		final Collection<String> obsTypes = Arrays.asList("test-obs-type-1","test-obs-type-2","test-foi-obs-3");
		final InsertSensorParameters parameters = new InsertSensorParameters("sdf", "sdf", 
				oneElementList, oneElementList, obsTypes);
		final Iterator<String> values = parameters.getAllValues(OBSERVATION_TYPES).iterator();
		checkMultipleValues(obsTypes, values);
	}

	@Test public void
	shouldSetProcedureDescription()
	{
		final String description = "sdf";
		final InsertSensorParameters parameters = new InsertSensorParameters(description, "pdf", 
				oneElementList, oneElementList, oneElementList);
		final String value = parameters.getSingleValue(PROCEDURE_DESCRIPTION);
		assertThat(value, is(description));
	}
	
	@Test public void
	shouldSetProcedureDescriptionFormat()
	{
		final String descriptionFormat = "sdf";
		final InsertSensorParameters parameters = new InsertSensorParameters("pdf",descriptionFormat,  
				oneElementList, oneElementList, oneElementList);
		final String value = parameters.getSingleValue(PROCEDURE_DESCRIPTION_FORMAT);
		assertThat(value, is(descriptionFormat));
	}
	
	@Test public void 
	shouldCreateValidParameterAssembly()
	{
		final InsertSensorParameters parameters = new InsertSensorParameters("sdf", "sdf", 
				oneElementList, oneElementList, oneElementList);
		assertThat(parameters.isValid(),is(true));
	}
	
	private void checkMultipleValues(final Collection<String> observableProperties,
			final Iterator<String> values)
	{
		int counter = 0;
		while (values.hasNext()) {
			assertThat(observableProperties,hasItem(values.next()));
			counter++;
		}
		assertThat(observableProperties.size(),is(counter));
	}
}
