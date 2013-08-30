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
public abstract class MultiValueRequestParameters implements RequestParameters {

    private final Map<String, MultiValue> parameters;
    
    public MultiValueRequestParameters() {
        parameters = new HashMap<String, MultiValue>();
    }
    
    @Override
    public boolean isEmpty() {
        return parameters.isEmpty();
    }
    
	@Override
	public boolean isEmpty(final String parameter)
	{
		return !parameters.containsKey(parameter) || 
				parameters.get(parameter).isEmpty() ||
        		(parameters.get(parameter).size() == 1 &&
        		parameters.get(parameter).getValues().size() == 1 &&
        		parameters.get(parameter).getValues().iterator().next().length() == 0);
	}


    @Override
    public boolean contains(final String parameter) {
        return parameters.containsKey(parameter);
    }

    @Override
    public boolean isSingleValue(final String parameter) {
        final MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) && multiValue.size() == 1;
    }

    @Override
    public boolean hasMultipleValues(final String parameter) {
        final MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) && multiValue.size() > 1;
    }

    @Override
    public String getSingleValue(final String parameter) {
        final MultiValue multiValue = parameters.get(parameter);
        return isPresent(multiValue) ? multiValue.getValues().iterator().next() : "";

    }

    @Override
    public Iterable<String> getParameterNames() {
        return Collections.unmodifiableCollection(parameters.keySet());
    }

    @Override
    public Iterable<String> getAllValues(final String parameter) {
        if (!parameters.containsKey(parameter)) {
            return Collections.emptyList();
        }
        final MultiValue multiValue = parameters.get(parameter);
        return Collections.unmodifiableCollection(multiValue.getValues());
    }

    @Override
    public boolean mergeWith(final RequestParameters parameters) {
    	if (parameters == null) {
            throw new IllegalArgumentException("Parameter assembly to merge with may not be null!");
    	}
        boolean hasChanged = false;
        for (final String parameter : parameters.getParameterNames()) {
            final MultiValue multiValue = getMultiValueFor(parameter);
            for (final String value : parameters.getAllValues(parameter)) {
                final boolean changed = multiValue.addValue(value);
                hasChanged = changed ? changed : hasChanged;
            }
        }
        return hasChanged;
    }

    @Override
    public boolean addParameterValue(final String parameter, final String value) {
        final MultiValue multiValue = getMultiValueFor(parameter);
        return multiValue.addValue(value);

    }
    
    @Override
    public boolean addParameterEnumValues(final String parameter, final Enum<?>... values) {
        final List<String> valuesAsList = getVarArgsAsList(values);
        return addParameterValues(parameter, valuesAsList);
    }

    @Override
    public boolean addParameterStringValues(final String parameter, final String... values) {
        final List<String> valuesAsList = getVarArgsAsList(values);
        return addParameterValues(parameter, valuesAsList);
    }
    
    @Override
    public boolean addParameterValues(final String parameter, final Iterable<String> values) {
        final MultiValue multiValue = getMultiValueFor(parameter);
        boolean hasChanged = false;
        for (final String value : values) {
            final boolean changed = multiValue.addValue(value);
            hasChanged = changed ? changed : hasChanged;
        }
        return hasChanged;
    }

    @Override
    public Collection<String> remove(final String parameter) {
        final MultiValue removedMultiMap = parameters.remove(parameter);
        final List<String> emptyList = Collections.emptyList();
        return removedMultiMap != null ? removedMultiMap.getValues() : emptyList;
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
     *         if the parameter <code>value</code> is <code>null</code> or empty.
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
        return parameter == null || 
        		!parameters.containsKey(parameter) ||
        		parameters.get(parameter).isEmpty() ||
        		(parameters.get(parameter).size() == 1 &&
        		parameters.get(parameter).getValues().size() == 1 &&
        		parameters.get(parameter).getValues().iterator().next().length() == 0);
    }

    protected boolean isEmptyString(final String value) {
        return value == null || value.isEmpty();
    }

    private boolean isPresent(final MultiValue multiValue) {
        return multiValue != null;
    }

    private MultiValue getMultiValueFor(final String parameter) {
        if (!parameters.containsKey(parameter)) {
            parameters.put(parameter, new MultiValue());
        }
        return parameters.get(parameter);
    }

    private List<String> getVarArgsAsList(final Enum<?>[] values) {
        if (values == null) {
            return Collections.emptyList();
        }
        return getVarArgsAsList(copyEnumValuesAsStrings(values));
    }

    private String[] copyEnumValuesAsStrings(final Enum< ? >[] values) {
        final String[] enumValuesAsStrings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            enumValuesAsStrings[i] = values[i].toString();
        }
        return enumValuesAsStrings;
    }

    private List<String> getVarArgsAsList(final String[] values) {
        List<String> valuesAsList = Collections.emptyList();
        if (values != null) {
            valuesAsList = Arrays.asList(values);
        }
        return valuesAsList;
    }

}
