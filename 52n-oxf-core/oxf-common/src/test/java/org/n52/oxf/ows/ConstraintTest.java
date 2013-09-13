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
