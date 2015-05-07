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
package org.n52.oxf.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <b>This class is test only yet!</b>
 */
public class MultiValue {

    private final List<String> values = new ArrayList<String>();

    /**
     * @param value
     *        the value to add. <code>null</code>s are hold as an empty string.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean addValue(String value) {
        if (value == null) {
            value = "";
        }
        if (values.contains(value)) {
        	// we don't add the same value twice
            return false;
        }
        return values.add(value);
    }

    /**
     * @param value
     *        the value to remove.
     * @return <code>true</code> if the list of values has been changed, <code>false</code> otherwise.
     */
    public boolean removeValue(final String value) {
        return values.remove(value);
    }

    /**
     * @return a read-only collection containing all added values preserving the order they were added.
     */
    public Collection<String> getValues() {
        return Collections.unmodifiableCollection(values);
    }

    /**
     * @return the amount of values.
     */
    public int size() {
        return values.size();
    }

    /**
     * @param value
     *        the value to check.
     * @return <code>true</code> if the value is contained by this instance.
     */
    public boolean contains(final String value) {
        return values.contains(value);
    }

    /**
     * @return <code>true</code> if at least one value is was added to this instance, <code>false</code>
     *         otherwise.
     */
    public boolean hasValues() {
        return values.size() > 0;
    }

    /**
     * Removes all values contained by this instance.
     */
    public void removeAll() {
        values.clear();
    }

	/**
	 * @return <code>true</code>, if this instance is empty
	 */
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public String toString() {
		return String.format("MultiValue [values=%s]", values);
	}

}
