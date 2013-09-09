/**
 * Copyright (C) 2013
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
