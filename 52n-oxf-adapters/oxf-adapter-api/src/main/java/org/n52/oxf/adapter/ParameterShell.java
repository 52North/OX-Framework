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
    
    private Parameter parameter;

    private Object[] specifiedValueArray = null;

    /**
     * @param parameter
     * @param specifiedValues
     *        multiple values which are all elements of the valueDomain of the parameter. Use this constructor
     *        if you want to associate a parameter with given values (e.g.: FORMAT=jpeg or COVERAGE=c1,c2,c3).
     * @throws OXFException
     *         if one value of the specifiedValues are not contained in the valueDomain of the parameter.
     */
    public ParameterShell(Parameter parameter, Object... specifiedValues) throws OXFException {
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
     * @deprecated use {@link #getSpecifiedTypedValueArray(Class)} instead
     */
    @Deprecated
    public Object[] getSpecifiedValueArray() {
        return specifiedValueArray;
    }
    
    public <T> T[] getSpecifiedTypedValueArray(Class<T[]> clazz) {
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
    public void setSpecifiedValue(Object specifiedValue) throws OXFException {
        if (specifiedValue == null) {
            String exceptionMsg = "specifiedValue must not be null.";
            LOGGER.warn(exceptionMsg);
            throw new OXFException(new IllegalArgumentException(exceptionMsg));
        }
        else if (!parameter.getValueDomain().containsValue(specifiedValue)) {
            String serviceName = parameter.getServiceSidedName();
            String exceptionMsg = String.format("specifiedValue '%s' is not contained in the valueDomain of the parameter '%s'", specifiedValue, serviceName);
            LOGGER.warn(exceptionMsg);
            throw new OXFException(exceptionMsg);
        }
        this.specifiedValueArray = new Object[] { specifiedValue };
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
    public void setSpecifiedValueArray(Object[] specifiedValueArray) throws OXFException {
        if (specifiedValueArray == null) {
            throw new OXFException(new IllegalArgumentException("specifiedValueArray has to be != null"));
        }
        for (Object obj : specifiedValueArray) {
            if (!this.parameter.getValueDomain().containsValue(obj)) {
                LOGGER.warn("One of the specifiedValueArray is not contained in the valueDomain of the parameter '"
                        + parameter.getServiceSidedName() + "'");
            }
        }
        this.specifiedValueArray = specifiedValueArray;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParemeterShell [");
        sb.append(this.parameter);
        sb.append(" - specifiedValue(s) = ");
        sb.append(Arrays.toString(this.specifiedValueArray));
        sb.append("]");
        return sb.toString();
    }
}