/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public abstract class MultimapRequestParameters implements RequestParameters {

    private Multimap<String, String> parameters;

    protected MultimapRequestParameters() {
        parameters = HashMultimap.create();
    }

    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public boolean contains(String key) {
        return parameters.containsKey(key);
    }

    @Override
    public boolean isSingleValue(String parameter) {
        return parameters.get(parameter).size() == 1;
    }

    @Override
    public boolean hasMultipleValues(String parameter) {
        return parameters.get(parameter).size() > 1;
    }

    @Override
    public String getSingleValue(String parameter) {
        if (parameters.containsKey(parameter)) {
            return parameters.get(parameter).iterator().next();
        }
        return null; // TODO return null?
    }

    @Override
    public Iterable<String> getAllValues(String parameter) {
        return Collections.unmodifiableCollection(parameters.get(parameter));
    }

    @Override
    public Collection<String> getParameterNames() {
        return Collections.unmodifiableCollection(parameters.keySet());
    }

    @Override
    public boolean mergeWith(RequestParameters parameters) {
        boolean hasChanged = false;
        for (String parameter : parameters.getParameterNames()) {
            Iterable<String> values = parameters.getAllValues(parameter);
            boolean changed = this.parameters.putAll(parameter, values);
            hasChanged = changed ? changed : hasChanged;
        }
        return hasChanged;
    }

    @Override
    public boolean addParameterValue(String parameter, String value) {
        String nonNull = value == null ? "" : value;
        return parameters.put(parameter, nonNull);
    }
    
    public boolean addParameterEnumValues(String parameter, Enum< ? >... values) {
        if (values == null) {
            return addEmptyParameter(parameter);
        }
        return addParameterStringValues(parameter, copyEnumValuesAsStrings(values));
    }

    private String[] copyEnumValuesAsStrings(Enum< ? >[] values) {
        String[] enumValuesAsStrings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            enumValuesAsStrings[i] = values[i].toString();
        }
        return enumValuesAsStrings;
    }

    boolean addEmptyParameter(String parameter) {
        return addParameterValue(parameter, null);
    }

    @Override
    public boolean addParameterStringValues(String parameter, String... values) {
        if (values == null) {
            return addEmptyParameter(parameter);
        }
        return addParameterValues(parameter, Arrays.asList(values));
    }

    @Override
    public boolean addParameterValues(String parameter, Iterable<String> values) {
        boolean changed = false;
        for (String value : values) {
            String nonNull = value == null ? "" : value;
            changed = addParameterValue(parameter, nonNull) || changed;
        }
        return changed;
    }
    
    @Override
    public Collection<String> remove(String parameter) {
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
    protected boolean addNonEmpty(String key, String value) {
        if (isEmptyString(value)) {
            String format = "Parameter '%s' is required and may not be null or empty!";
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
    protected boolean isEmptyValue(String parameter) {
//        String value = parameters.get(parameter);
//        return isEmptyString(value);
        return false;
    }

    protected boolean isEmptyString(String value) {
        return value == null || value.isEmpty();
    }

}
