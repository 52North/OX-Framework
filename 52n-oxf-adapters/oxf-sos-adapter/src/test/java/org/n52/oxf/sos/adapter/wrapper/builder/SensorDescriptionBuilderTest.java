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
package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder.*;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.TimePeriodType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.BooleanDocument.Boolean;
import net.opengis.swe.x101.CountDocument.Count;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.TextDocument.Text;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.junit.Before;
import org.junit.Test;

public class SensorDescriptionBuilderTest {
	
	private SensorDescriptionBuilder builder;
	
	@Before
	public void initBuilder() {
		builder = new SensorDescriptionBuilder();
	}

	@Test public void
	shouldAddCapabilitiesWithNameDefinitionAndValue()
			throws XmlException	{
		final String capabilityName = "test-capability-name";
		final String fieldName = "test-field-name";
		final String fieldDefinition = "test-field-definition";
		final String value = "test-value";
		builder.addCapability(capabilityName, fieldName, fieldDefinition, value);
		final String description = builder.buildSensorDescription();
		final Capabilities capabilities = SystemDocument.Factory.parse(SensorMLDocument.Factory.parse(description).getSensorML().getMemberArray(0).toString()).getSystem().getCapabilitiesArray(0);
		final AnyScalarPropertyType field = ((SimpleDataRecordType) capabilities.getAbstractDataRecord()).getFieldArray(0);
		
		assertThat(capabilities.getName(),is(capabilityName));
		assertThat(field.getName(),is(fieldName));
		assertThat(field.getText().getDefinition(),is(fieldDefinition));
		assertThat(field.getText().getValue(),is(value));
	}
	
	@Test public void
	shouldAddDescriptionIfSet()
			throws XmlException {
		final String description = "test-description";
		builder.setDescription(description);
		
		final SystemType system = getSystem(builder.buildSensorDescription());
		assertThat(system.isSetDescription(),is(true));
		assertThat(system.getDescription().getStringValue(),is(description));
	}

	@Test public void
	shouldNotAddDescriptionIfNull()
			throws XmlException {
		builder.setDescription(null);
		final SystemType system = getSystem(builder.buildSensorDescription());
		assertThat(system.isSetDescription(), is(false));
	}
	
	@Test public void
	shouldNotAddDescriptionIfEmptyString()
			throws XmlException {
		builder.setDescription("");
		final SystemType system = getSystem(builder.buildSensorDescription());
		assertThat(system.isSetDescription(), is(false));
	}
	
	@Test public void
	shouldSetCapabilityCollectingStatusWithCorrectDefinition()
			throws XmlException {
		builder.setCapabilityCollectingStatus(COLLECTION_STATUS_NAME, true);
		final Capabilities capabilities = getSystem(builder.buildSensorDescription()).getCapabilitiesArray(0);
		final DataComponentPropertyType field = ((DataRecordType)capabilities.getAbstractDataRecord()).getFieldArray(0);
		
		assertThat(capabilities.getName(), is(COLLECTION_STATUS_NAME));
		assertThat(field.getName(), is(COLLECTION_STATUS_NAME));
		assertThat(field.getBoolean().getDefinition(), is(COLLECTING_STATUS_DEF));
		assertThat(field.getBoolean().getValue(), is(true));
	}
	
	@Test public void
	shouldSetValidTimeToTimePosition()
			throws XmlException {
		final String timeToSet = "start-time";
		builder.setValidTime(timeToSet);
		
		final String description = builder.buildSensorDescription();
		final String validTime = getSystem(description).getValidTime().getTimeInstant().getTimePosition().getStringValue();
		assertThat(validTime,is(timeToSet));
	}
	
	@Test public void
	shouldSetIndeterminateValues()
			throws XmlException {
		testIndeterminateValue("now");
		testIndeterminateValue("after");
		testIndeterminateValue("before");
		testIndeterminateValue("unknown");
	}
	
	@Test public void
	shouldSetValidTimeWithStartAndIndeterminateValue()
			throws XmlException {
		final String startTime = "test-start-time";
		final String endTime = "unknown";
		
		builder.setValidTime(startTime,endTime);
		
		final TimePeriodType validTime = getSystem(builder.buildSensorDescription()).getValidTime().getTimePeriod();
		assertThat(validTime.getBeginPosition().getStringValue(), is(startTime));
		assertThat(validTime.getEndPosition().getIndeterminatePosition().toString(), is(endTime));
	}
	
	@Test public void
	shouldSetValidTimeWithStartAndEndTime()
			throws XmlException {
		final String startTime = "test-start-time";
		final String endTime = "test-end-time";
		
		builder.setValidTime(startTime,endTime);
		
		final TimePeriodType validTime = getSystem(builder.buildSensorDescription()).getValidTime().getTimePeriod();
		assertThat(validTime.getBeginPosition().getStringValue(), is(startTime));
		assertThat(validTime.getEndPosition().getStringValue(), is(endTime));
	}
	
	@Test public void
	shouldSetFeatureOfInterest()
			throws XmlException {
		final String foiName = "test-foi-name";
		final String foiUri = "test-foi-uri";
		
		builder.addFeatureOfInterest(foiName, foiUri);
		
		final Capabilities capabilities = getSystem(builder.buildSensorDescription()).getCapabilitiesArray(0);
		final DataComponentPropertyType feature = ((DataRecordType) capabilities.getAbstractDataRecord()).getFieldArray(0);
		
		assertThat(capabilities.getName(), is("featuresOfInterest"));
		assertThat(feature.getName(),is("featureOfInterestID"));
		assertThat(feature.getText().getDefinition(),is("http://www.opengis.net/def/featureOfInterest/identifier"));
		assertThat(feature.getText().getValue(),is(foiUri));
	}

	@Test public void
	shouldAddOfferingMetadataToOutput()
			throws XmlException {
		final String offeringName = "test-offering-name";
		final String offeringUri = "test-offering-uri";
		final String definition = "test-output-definition";
		final String name = "test-output-name";
		final String uom = "test-uom";
		builder.addOutputBoolean(name, definition, offeringUri, offeringName);
		builder.addOutputCount(name, definition, offeringUri, offeringName);
		builder.addOutputText(name, definition, offeringUri, offeringName);
		builder.addOutputMeasurement(name, definition, offeringUri, offeringName, uom);
		
		final IoComponentPropertyType[] outputs = getSystem(builder.buildSensorDescription()).getOutputs().getOutputList().getOutputArray();
		
		assertThat(outputs.length,is(4));
		for (final IoComponentPropertyType output : outputs) {
			assertThat(output.getName(),is(name));
			if (output.isSetBoolean()) {
				final Boolean bool = output.getBoolean();
				assertThat(bool.getDefinition(),is(definition));
				testMetaDataProperty(bool.getMetaDataPropertyArray(0),
						offeringName,
						offeringUri);
			}
			else if (output.isSetCount()) {
				final Count count = output.getCount();
				assertThat(count.getDefinition(),is(definition));
				testMetaDataProperty(count.getMetaDataPropertyArray(0),
						offeringName,
						offeringUri);
			}
			else if (output.isSetText()) {
				final Text text = output.getText();
				assertThat(text.getDefinition(),is(definition));
				testMetaDataProperty(text.getMetaDataPropertyArray(0),
						offeringName,
						offeringUri);
			}
			else if (output.isSetQuantity()) {
				final Quantity quantity = output.getQuantity();
				assertThat(quantity.getDefinition(),is(definition));
				assertThat(quantity.getUom().getCode(),is(uom));
				testMetaDataProperty(quantity.getMetaDataPropertyArray(0),
						offeringName,
						offeringUri);
			}
			else {
				fail("Unsupported observed property type: " + output.getClass().getName());
			}
		}
	}
	
	@Test public void
	shouldNotAddOfferingMetadataToOutput()
			throws XmlException {
		final String offeringName = null;
		final String offeringUri = null;
		final String definition = "test-output-definition";
		final String name = "test-output-name";
		final String uom = "test-uom";
		final boolean no = false;
		
		builder.addOutputBoolean(name, definition, offeringUri, offeringName);
		builder.addOutputCount(name, definition, offeringUri, offeringName);
		builder.addOutputText(name, definition, offeringUri, offeringName);
		builder.addOutputMeasurement(name, definition, offeringUri, offeringName, uom);
		builder.setAddOfferingMetadataToOutputs(no);
		
		final IoComponentPropertyType[] outputs = getSystem(builder.buildSensorDescription()).getOutputs().getOutputList().getOutputArray();
		
		for (final IoComponentPropertyType output : outputs) {
			if (output.isSetBoolean()) {
				final Boolean bool = output.getBoolean();
				assertThat(bool.getMetaDataPropertyArray().length,is(0));
			}
			else if (output.isSetCount()) {
				final Count count = output.getCount();
				assertThat(count.getMetaDataPropertyArray().length,is(0));
			}
			else if (output.isSetText()) {
				final Text text = output.getText();
				assertThat(text.getMetaDataPropertyArray().length,is(0));
			}
			else if (output.isSetQuantity()) {
				final Quantity quantity = output.getQuantity();
				assertThat(quantity.getMetaDataPropertyArray().length,is(0));
			}
			else {
				fail("Unsupported observed property type: " + output.getClass().getName());
			}
		}
		
	}

	private void testMetaDataProperty(final MetaDataPropertyType metaDataProperty,
			final String offeringName,
			final String offeringUri)
	{
		final String query = "declare namespace sos='http://www.opengis.net/sos/1.0'; " + 
				"sos:offering";
		final XmlCursor c = metaDataProperty.newCursor();
		c.selectPath(query);
		assertThat(c.getSelectionCount(),is(1));
		c.toNextSelection();
		c.toFirstChild();
		assertThat(c.getTextValue(),is(offeringUri));
		c.toNextSibling();
		assertThat(c.getTextValue(),is(offeringName));
		c.dispose();
	}

	private void testIndeterminateValue(final String indeterminateValue) throws XmlException
	{
		builder.setValidTime(indeterminateValue);
		final String description = builder.buildSensorDescription();
		final String validTime = getSystem(description).getValidTime().getTimeInstant().getTimePosition().getIndeterminatePosition().toString();
		assertThat(validTime,is(indeterminateValue));
	}
	
	private SystemType getSystem(final String sensorDescription) throws XmlException
	{
		return SystemDocument.Factory.parse(SensorMLDocument.Factory.parse(sensorDescription).getSensorML().getMemberArray(0).toString()).getSystem();
	}
}
