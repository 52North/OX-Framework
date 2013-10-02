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
package org.n52.oxf.sos.adapter.wrapper.builder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder.*;
import net.opengis.gml.TimePeriodType;
import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.SimpleDataRecordType;

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
		final SimpleDataRecordType dataRecord = (SimpleDataRecordType) capabilities.getAbstractDataRecord();
		
		assertThat(capabilities.getName(),is(capabilityName));
		assertThat(dataRecord.getFieldArray(0).getName(),is(fieldName));
		assertThat(dataRecord.getFieldArray(0).getText().getDefinition(),is(fieldDefinition));
		assertThat(dataRecord.getFieldArray(0).getText().getValue(),is(value));
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
