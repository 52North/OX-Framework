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
