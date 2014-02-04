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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>This class is test only yet!</b>
 */
public class MultiValueRequestParameters implements RequestParameters {

    private Map<String, MultiValue> parameters;
    
    public MultiValueRequestParameters() {
        parameters = new HashMap<String, MultiValue>();
    }
    
    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    @Override
    public boolean contains(String parameter) {
        return parameters.containsKey(parameter);
    }

    @Override
    public boolean isSingleValue(String parameter) {
        MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) && multiValue.size() == 1;
    }

    @Override
    public boolean hasMultipleValues(String parameter) {
        MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) && multiValue.size() > 1;
    }

    @Override
    public String getSingleValue(String parameter) {
        MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) ? multiValue.getValues().iterator().next() : "";

    }

    @Override
    public Iterable<String> getParameterNames() {
        return Collections.unmodifiableCollection(parameters.keySet());
    }

    @Override
    public Iterable<String> getAllValues(String parameter) {
        if (!parameters.containsKey(parameter)) {
            return Collections.emptyList();
        }
        MultiValue multiValue = parameters.get(parameter);
        return Collections.unmodifiableCollection(multiValue.getValues());
    }

    @Override
    public boolean mergeWith(RequestParameters parameters) {
        boolean hasChanged = false;
        for (String parameter : parameters.getParameterNames()) {
            MultiValue multiValue = getMultiValueFor(parameter);
            for (String value : parameters.getAllValues(parameter)) {
                boolean changed = multiValue.addValue(value);
                hasChanged = changed ? changed : hasChanged;
            }
        }
        return hasChanged;
    }

    @Override
    public boolean addParameterValue(String parameter, String value) {
        MultiValue multiValue = getMultiValueFor(parameter);
        return multiValue.addValue(value);

    }
    
    @Override
    public boolean addParameterEnumValues(String parameter, Enum<?>... values) {
        List<String> valuesAsList = getVarArgsAsList(values);
        return addParameterValues(parameter, valuesAsList);
    }

    @Override
    public boolean addParameterStringValues(String parameter, String... values) {
        List<String> valuesAsList = getVarArgsAsList(values);
        return addParameterValues(parameter, valuesAsList);
    }
    
    @Override
    public boolean addParameterValues(String parameter, Iterable<String> values) {
        MultiValue multiValue = getMultiValueFor(parameter);
        boolean hasChanged = false;
        for (String value : values) {
            boolean changed = multiValue.addValue(value);
            hasChanged = changed ? changed : hasChanged;
        }
        return hasChanged;
    }

    @Override
    public Collection<String> remove(String parameter) {
        MultiValue removedMultiMap = parameters.remove(parameter);
        List<String> emptyList = Collections.emptyList();
        return removedMultiMap != null ? removedMultiMap.getValues() : emptyList;
    }
    
    @Override
    public void removeAll() {
        parameters.clear();
    }

    private boolean isPresent(MultiValue multiValue) {
        return multiValue != null;
    }

    private MultiValue getMultiValueFor(String parameter) {
        if (!parameters.containsKey(parameter)) {
            parameters.put(parameter, new MultiValue());
        }
        return parameters.get(parameter);
    }

    private List<String> getVarArgsAsList(Enum<?>[] values) {
        if (values == null) {
            return Collections.emptyList();
        }
        String[] enumValuesAsStrings = copyEnumValuesAsStrings(values);
        return getVarArgsAsList(enumValuesAsStrings);
    }

    private String[] copyEnumValuesAsStrings(Enum< ? >[] values) {
        String[] enumValuesAsStrings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            enumValuesAsStrings[i] = values[i].toString();
        }
        return enumValuesAsStrings;
    }

    private List<String> getVarArgsAsList(String[] values) {
        List<String> valuesAsList = Collections.emptyList();
        if (values != null) {
            valuesAsList = Arrays.asList(values);
        }
        return valuesAsList;
    }

}
