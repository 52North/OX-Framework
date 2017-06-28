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
package org.n52.oxf.ows.capabilities;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.n52.oxf.ows.Constraint;


@SuppressWarnings("deprecation")
public class RequestMethodTest {

	@Test public void
	shouldReturnFalseIfAddNullAsConstraint()
	{
		assertThat(new RequestMethodSeam(null).addOwsConstraint(null),is(false));
	}

	@Test public void
	shouldReturnTrueIfAddingNotNullConstraint()
	{
		final Constraint c1 = new Constraint("constraint-1", "value-1-1","value-1-2");
		final RequestMethodSeam seam = new RequestMethodSeam(null);
		assertThat(seam.addOwsConstraint(c1), is(true));
		assertThat(seam.getOwsConstraints(),hasItem(c1));
	}

	@Test public void
	shouldReturnFalseIfAddingNullSetAsOwsConstraints()
	{
		assertThat(new RequestMethodSeam(null).setOwsConstraints(null),is(false));
	}

	@Test public void
	shouldReturnTrueIfSettingOwsConstrainsWithEmptySet()
	{
		final Set<Constraint> empty = Collections.emptySet();
		final RequestMethodSeam seam = new RequestMethodSeam(null);
		assertThat(seam.setOwsConstraints(empty),is(true));
		assertThat(seam.getOwsConstraints(), is(emptyCollectionOf(Constraint.class)));
	}

	@Test public void
	shouldReturnTrueIfSettingOwsConstraintsWithValues()
	{
		final RequestMethodSeam seam = new RequestMethodSeam(null, null);
		final Constraint c1 = new Constraint("constraint-1", "value-1-1","value-1-2");
		final Constraint c2 = new Constraint("constraint-2", "value-2-1","value-2-2");
		final Set<Constraint> constraints = new HashSet<Constraint>(2);
		constraints.add(c1);
		constraints.add(c2);
		assertThat(seam.getOwsConstraints(), is(emptyCollectionOf(Constraint.class)));
		assertThat(seam.setOwsConstraints(constraints),is(true));
		assertThat(seam.getOwsConstraints(), hasSize(2));
		assertThat(seam.getOwsConstraints(), is(hasItem(c1)));
		assertThat(seam.getOwsConstraints(), is(hasItem(c2)));
	}

	@Test public void
	shouldReturnEmptySetIfOwsConstraintsIsNotSet()
	{
		RequestMethodSeam seam = new RequestMethodSeam(null);
		assertThat(seam.getOwsConstraints(), is(emptyCollectionOf(Constraint.class)));
		assertThat(seam.getConstraints(), is(nullValue(String[].class)));
		assertThat(seam.getOnlineResource(),is(nullValue(OnlineResource.class)));

		seam = new RequestMethodSeam(null, null);
		assertThat(seam.getOwsConstraints(), is(emptyCollectionOf(Constraint.class)));
	}

	@Test public void
	shouldReturnOwsConstraints()
	{
		final Set<Constraint> constraints = new HashSet<Constraint>(2);
		final Constraint c1 = new Constraint("constraint-1", "value-1-1","value-1-2");
		final Constraint c2 = new Constraint("constraint-2", "value-2-1","value-2-2");
		constraints.add(c1);
		constraints.add(c2);
		final RequestMethodSeam seam = new RequestMethodSeam(null, constraints);
		assertThat(seam.getOwsConstraints(), hasSize(2));
		assertThat(seam.getOwsConstraints(), is(hasItem(c1)));
		assertThat(seam.getOwsConstraints(), is(hasItem(c2)));
	}


	private class RequestMethodSeam extends RequestMethod {
		public RequestMethodSeam(final OnlineResource onlineResource) {
			super(onlineResource);
		}

		public RequestMethodSeam(final OnlineResource onlineResource, final Set<Constraint> constraints) {
			super(onlineResource, constraints);
		}

		@Override
		public String toXML() {return ""; }
	}
}
