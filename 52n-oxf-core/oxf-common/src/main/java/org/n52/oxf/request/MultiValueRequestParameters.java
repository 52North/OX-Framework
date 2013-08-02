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
