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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of OWS 1.1.0 Constraint supporting name attribute and allowedValues element
 */
public class Constraint {
	
	private final String name;
	private final List<String> allowedValues;

	public Constraint(final String name, final String... allowedValue) {
		if (name == null || allowedValue == null || allowedValue.length == 0) {
			throw new IllegalArgumentException("Mandatory parameters not set. Required are the 'name' and a minimum of one 'allowedValue'!");
		}
		this.name = name;
		allowedValues = Collections.unmodifiableList(Arrays.asList(allowedValue));
	}

	public String getName()
	{
		return name;
	}

	public Collection<String> getAllowedValues()
	{
		return allowedValues;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + allowedValues.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Constraint)) {
			return false;
		}
		final Constraint other = (Constraint) obj;
		if (!allowedValues.equals(other.allowedValues)) {
			return false;
		}
		if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return String.format("Constraint [name=%s, allowedValues=%s]", name, allowedValues);
	}

}
