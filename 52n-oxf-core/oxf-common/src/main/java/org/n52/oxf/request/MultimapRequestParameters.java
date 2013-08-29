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

package org.n52.oxf.request;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @deprecated use {@link MultiValueRequestParameters} instead.
 * @see MultiValueRequestParameters 
 */
@Deprecated
public abstract class MultimapRequestParameters implements RequestParameters {

    private final Multimap<String, String> parameters;

    protected MultimapRequestParameters() {
        parameters = HashMultimap.create();
    }

    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
    @Override
    public boolean isEmpty(final String parameter) {
    	return !parameters.containsKey(parameter) || 
    			(parameters.get(parameter).size() == 1 && 
    			parameters.get(parameter).iterator().next().length() < 1);
    }

    @Override
    public boolean contains(final String key) {
        return parameters.containsKey(key);
    }

    @Override
    public boolean isSingleValue(final String parameter) {
        return parameters.get(parameter).size() == 1;
    }

    @Override
    public boolean hasMultipleValues(final String parameter) {
        return parameters.get(parameter).size() > 1;
    }

    @Override
    public String getSingleValue(final String parameter) {
        if (parameters.containsKey(parameter)) {
            return parameters.get(parameter).iterator().next();
        }
        return "";
    }

    @Override
    public Iterable<String> getAllValues(final String parameter) {
        return Collections.unmodifiableCollection(parameters.get(parameter));
    }

    @Override
    public Collection<String> getParameterNames() {
        return Collections.unmodifiableCollection(parameters.keySet());
    }

    @Override
    public boolean mergeWith(final RequestParameters parameters) {
        boolean hasChanged = false;
        for (final String parameter : parameters.getParameterNames()) {
            final Iterable<String> values = parameters.getAllValues(parameter);
            final boolean changed = this.parameters.putAll(parameter, values);
            hasChanged = changed ? changed : hasChanged;
        }
        return hasChanged;
    }

    @Override
    public boolean addParameterValue(final String parameter, final String value) {
        final String nonNull = value == null ? "" : value;
        return parameters.put(parameter, nonNull);
    }
    
    @Override
	public boolean addParameterEnumValues(final String parameter, final Enum< ? >... values) {
        if (values == null) {
            return addEmptyParameter(parameter);
        }
        return addParameterStringValues(parameter, copyEnumValuesAsStrings(values));
    }

    private String[] copyEnumValuesAsStrings(final Enum< ? >[] values) {
        final String[] enumValuesAsStrings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            enumValuesAsStrings[i] = values[i].toString();
        }
        return enumValuesAsStrings;
    }

    private boolean addEmptyParameter(final String parameter) {
        return addParameterValue(parameter, null);
    }

    @Override
    public boolean addParameterStringValues(final String parameter, final String... values) {
        if (values == null) {
            return addEmptyParameter(parameter);
        }
        return addParameterValues(parameter, Arrays.asList(values));
    }

    @Override
    public boolean addParameterValues(final String parameter, final Iterable<String> values) {
        boolean changed = false;
        for (final String value : values) {
            final String nonNull = value == null ? "" : value;
            changed = addParameterValue(parameter, nonNull) || changed;
        }
        return changed;
    }
    
    @Override
    public Collection<String> remove(final String parameter) {
        return parameters.removeAll(parameter);
    }

    @Override
    public void removeAll() {
        parameters.clear();
    }

    /**
     * Adds a required parameter to the map doing a non-null check beforehand.
     * 
     * @param key
     *        the parameter's name.
     * @param value
     *        the non-null value.
     * @return if the assembly has changed.
     * @throws IllegalArgumentException
     *         if the <code>key</code>'s associated <code>value</code> is <code>null</code> or empty.
     */
    protected boolean addNonEmpty(final String key, final String value) {
        if (isEmptyString(value)) {
            final String format = "Parameter '%s' is required and may not be null or empty!";
            throw new IllegalArgumentException(String.format(format, key));
        }
        return addParameterValue(key, value);
    }

    /**
     * Checks if value of the given parameter is empty.
     * 
     * @param parameterName
     *        the parameter name.
     * @return <code>true</code> if parameter value is <code>null</code> or empty, <code>false</code>
     *         otherwise.
     */
    protected boolean isEmptyValue(final String parameter) {
//        String value = parameters.get(parameter);
//        return isEmptyString(value);
        return false;
    }

    protected boolean isEmptyString(final String value) {
        return value == null || value.isEmpty();
    }

}
