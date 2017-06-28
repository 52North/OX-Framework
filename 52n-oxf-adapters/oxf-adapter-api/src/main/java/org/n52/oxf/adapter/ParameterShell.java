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
package org.n52.oxf.adapter;

import java.util.Arrays;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.capabilities.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class associates a <a href=Parameter.html>Parameter</a> with one or multiple values dependend on the
 * used constructor.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ParameterShell {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterShell.class);
    
    private final Parameter parameter;

    private Object[] specifiedValueArray = null;

    /**
     * @param parameter
     * @param specifiedValues
     *        multiple values which are all elements of the valueDomain of the parameter. Use this constructor
     *        if you want to associate a parameter with given values (e.g.: FORMAT=jpeg or COVERAGE=c1,c2,c3).
     * @throws OXFException
     *         if one value of the specifiedValues are not contained in the valueDomain of the parameter.
     */
    public ParameterShell(final Parameter parameter, final Object... specifiedValues) throws OXFException {
        if (parameter == null) {
            throw new IllegalArgumentException("Parameter is null.");
        }
        else if (specifiedValues == null || specifiedValues.length == 0) {
            throw new IllegalArgumentException("No specifiedValues for parameter '" + parameter + "' were given.");
        }
        this.parameter = parameter;
        if (specifiedValues.length > 1) {
            setSpecifiedValueArray(specifiedValues);
        } else {
            setSpecifiedValue(specifiedValues[0]);
        }
    }

    public Parameter getParameter() {
        return parameter;
    }

    /**
     * @return true if the parameter is associated with just one specified value.
     */
    public boolean hasSingleSpecifiedValue() {
        return !hasMultipleSpecifiedValues();
    }

    /**
     * @return true if the parameter is associated with multiple specified values.
     */
    public boolean hasMultipleSpecifiedValues() {
        return specifiedValueArray.length > 1;
    }

    /**
     * @return the specifiedValue
     */
    public Object getSpecifiedValue() {
        return specifiedValueArray[0];
    }

    /**
     * @return the specifiedValueArray
     * @deprecated
     * 		Use {@link #getSpecifiedTypedValueArray(Class&lt;T[]&gt;)} instead,
     * 		e.g. <tt>getSpecifiedTypedValueArray(String[].class)</tt>.
     */
    @Deprecated
    public Object[] getSpecifiedValueArray() {
        return getSpecifiedTypedValueArray(Object[].class);
    }
    
    public <T> T[] getSpecifiedTypedValueArray(final Class<T[]> clazz) {
        return Arrays.copyOf(specifiedValueArray, specifiedValueArray.length, clazz);
    }

    /**
     * sets the specifiedValue attribute (single-mode).
     * 
     * @param specifiedValue
     *        must be an element of the valueDomain of the parameter.
     * @throws OXFException
     *         if the specifiedValue is not contained in the valueDomain of the parameter.
     */
    public void setSpecifiedValue(final Object specifiedValue) throws OXFException {
        if (specifiedValue == null) {
            final String exceptionMsg = "specifiedValue must not be null.";
            LOGGER.warn(exceptionMsg);
            throw new OXFException(new IllegalArgumentException(exceptionMsg));
        }
        else if (!parameter.getValueDomain().containsValue(specifiedValue)) {
            final String serviceName = parameter.getServiceSidedName();
            final String exceptionMsg = String.format("specifiedValue '%s' is not contained in the valueDomain of the parameter '%s'", specifiedValue, serviceName);
            LOGGER.warn(exceptionMsg);
            throw new OXFException(exceptionMsg);
        }
        specifiedValueArray = new Object[] { specifiedValue };
    }

    /**
     * sets the specifiedValueArray attribute (multiple-mode). The specifiedValue attribute (single-mode) will
     * be set on <code>null</code>.
     * 
     * @param specifiedValueArray
     *        multiple values which are all elements of the valueDomain of the parameter.
     * @throws OXFException
     *         if one value of the specifiedValueArray is not contained in the valueDomain of the parameter.
     */
    public void setSpecifiedValueArray(final Object[] specifiedValueArray) throws OXFException {
        if (specifiedValueArray == null) {
            throw new OXFException(new IllegalArgumentException("specifiedValueArray has to be != null"));
        }
        for (final Object obj : specifiedValueArray) {
            if (!parameter.getValueDomain().containsValue(obj)) {
                LOGGER.warn("One of the specifiedValueArray is not contained in the valueDomain of the parameter '"
                        + parameter.getServiceSidedName() + "'");
            }
        }
        this.specifiedValueArray = specifiedValueArray;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ParemeterShell [");
        sb.append(parameter);
        sb.append(" - specifiedValue(s) = ");
        sb.append(Arrays.toString(specifiedValueArray));
        sb.append("]");
        return sb.toString();
    }
}