/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.ows;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConstraintTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test public void
	shouldThrowIllegalArgumentExcpetionIfMissingMandatoryParameters()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Mandatory parameters not set. Required are the 'name' and a minimum of one 'allowedValue'!");
		new Constraint(null, (String[])null);
	}
	
	@Test public void
	shouldThrowIllegalArgumentExcpetionIfMissingName()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Mandatory parameters not set. Required are the 'name' and a minimum of one 'allowedValue'!");
		new Constraint(null, "value");
	}
	
	@Test public void
	shouldThrowIllegalArgumentExcpetionIfMissingAllowedValues()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Mandatory parameters not set. Required are the 'name' and a minimum of one 'allowedValue'!");
		new Constraint("name", (String[])null);
	}
	
	@Test public void
	shouldThrowIllegalArgumentExcpetionIfGettingEmptyAllowedValues()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Mandatory parameters not set. Required are the 'name' and a minimum of one 'allowedValue'!");
		new Constraint("name", new String[0]);
	}
	
	@Test public void
	shouldSetName()
	{
		final String name = "name";
		final Constraint constraint = new Constraint(name,new String[1]);
		assertThat(constraint.getName(), is(name));
	}
	
	@Test public void
	shouldSetAllowedValues()
	{
		final String aV1 = "av1";
		final String aV2 = "aV2";
		Constraint constraint = new Constraint("name", aV1);
		assertThat(constraint.getAllowedValues(),hasSize(1));
		assertThat(constraint.getAllowedValues(),hasItem(aV1));
		constraint = new Constraint("name",aV1,aV2);
		assertThat(constraint.getAllowedValues(),hasSize(2));
		assertThat(constraint.getAllowedValues(),hasItem(aV1));
		assertThat(constraint.getAllowedValues(),hasItem(aV2));
	}
	
	@Test public void
	hashCodeTest()
	{
		final Constraint constraint = new Constraint("name","av1");
		final Constraint sameConstraint = new Constraint("name","av1");
		final Constraint anotherConstraint = new Constraint("name2","av");
		assertThat(constraint.hashCode(),is(constraint.hashCode()));
		assertThat(constraint.hashCode(),is(sameConstraint.hashCode()));
		assertThat(constraint.hashCode(),is(not(anotherConstraint.hashCode())));
		assertThat(sameConstraint.hashCode(),is(not(anotherConstraint.hashCode())));
	}
	
	@Test public void
	equalsTest()
	{
		final Constraint constraint = new Constraint("name","av1");
		final Constraint sameConstraint = new Constraint("name","av1");
		final Constraint anotherConstraint = new Constraint("name2","av");
		final Constraint withOtherNameButSameValues = new Constraint("name2","av1");
		assertThat(constraint.equals(constraint), is(true));
		assertThat(constraint.equals(null), is(false));
		assertThat(constraint.equals(new Object()), is(false));
		assertThat(constraint.equals(sameConstraint), is(true));
		assertThat(constraint.equals(anotherConstraint), is(false));
		assertThat(constraint.equals(withOtherNameButSameValues), is(false));
	}
	
}
