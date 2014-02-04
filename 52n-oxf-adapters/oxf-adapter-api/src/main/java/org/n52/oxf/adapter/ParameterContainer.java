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
package org.n52.oxf.adapter;

import java.util.*;
import org.n52.oxf.*;
import org.n52.oxf.ows.capabilities.*;
import org.n52.oxf.valueDomains.*;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;

/**
 * Holds all the <code>ParameterShell</code>s of an AbstractServiceLayer that should be used in an operation
 * execution.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ParameterContainer {

    List<ParameterShell> parameterShells = new ArrayList<ParameterShell>();

    /**
     * @return <code>true</code> if the container contained the specified element.
     */
    public boolean removeParameterShell(ParameterShell parameterShell) {
        return parameterShells.remove(parameterShell);
    }

    public void addParameterShell(ParameterShell parameterShell) {
        parameterShells.add(parameterShell);
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * a {@link StringValueDomain}omain containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(String parameterName, String... parameterValue) throws OXFException {
        Parameter parameter = new Parameter(parameterName, true, new StringValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * a {@link TemporalValueDomain} containing only the specified <code>parameterValue</code>(s). 
     */
    public void addParameterShell(String parameterName, ITime... parameterValue) throws OXFException {
        Parameter parameter = new Parameter(parameterName, true, new TemporalValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * an {@link IntegerDiscreteValueDomain}eValueDomain containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(String parameterName, Integer... parameterValue) throws OXFException {
        Parameter parameter = new Parameter(parameterName, true, new IntegerDiscreteValueDomain(parameterValue), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));
    }

    /**
     * Adds a new ParameterShell with a 'required' {@link Parameter} with name <code>parameterName</code> and
     * an {@link OpenValueDomain} containing only the specified <code>parameterValue</code>(s).
     */
    public void addParameterShell(String parameterName, ParameterContainer... parameterValue) throws OXFException {
        Parameter parameter = new Parameter(parameterName, true, new OpenValueDomain(), parameterName);
        addParameterShell(new ParameterShell(parameter, parameterValue));

    }

    /**
     * @param commonName
     * @return the ParameterShell whose parameter has the specified commonName (ignoring case). If there isn't
     *         such a ParameterShell <code>null</code> will be returned.
     */
    public ParameterShell getParameterShellWithCommonName(String commonName) {
        for (ParameterShell ps : parameterShells) {
            Parameter parameter = ps.getParameter();
            if (parameter.getCommonName() != null  && parameter.getCommonName().equalsIgnoreCase(commonName)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * @param serviceSidedName
     * @return the ParameterShell whose parameter has the specified serviceSidedName (ignoring case). If there
     *         isn't such a ParameterShell <code>null</code> will be returned.
     */
    public ParameterShell getParameterShellWithServiceSidedName(String serviceSidedName) {
        for (ParameterShell ps : parameterShells) {
            if (ps.getParameter().getServiceSidedName() != null
                    && ps.getParameter().getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                return ps;
            }
        }
        return null;
    }

    /**
     * @param serviceSidedName
     * @return <code>true</code> if this ParameterContainer contains a ParameterShell with a Parameter whose
     *         serviceSidedName is as specified (ignoring case). Otherwise <code>false</code> will be
     *         returned.
     */
    public boolean containsParameterShellWithServiceSidedName(String serviceSidedName) {
        for (ParameterShell ps : parameterShells) {
            if (ps.getParameter().getServiceSidedName() != null
                    && ps.getParameter().getServiceSidedName().equalsIgnoreCase(serviceSidedName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param commonName
     * @return <code>true</code> if this ParameterContainer contains a ParameterShell with a Parameter whose
     *         commonName is as specified. Otherwise <code>false</code> will be returned.
     */
    public boolean containsParameterShellWithCommonName(String commonName) {
        for (ParameterShell ps : parameterShells) {
            if (ps.getParameter().getCommonName() != null
                    && ps.getParameter().getCommonName().equalsIgnoreCase(commonName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * searches the ParameterShell whose Parameter has the commonName and sets the newValue to this Parameter.
     * 
     * @param commonName
     * @param newValue
     * @throws OXFException
     *         if there is no ParameterShell whose parameter has the specified commonName.<br>
     *         or if the newValue is not contained in the valueDomain of the parameter.
     */
    public void setParameterValue(String commonName, Object newValue) throws OXFException {
        ParameterShell ps = this.getParameterShellWithCommonName(commonName);
        if (ps != null) {
            ps.setSpecifiedValue(newValue);
        }
        else {
            throw new OXFException("There is no ParameterShell whose parameter has the commonName '" + commonName
                    + "'.");
        }
    }

    /**
     * searches the ParameterShell whose Parameter has the commonName and sets the newValues to this
     * Parameter.
     * 
     * @param commonName
     * @param newValues
     * @throws OXFException
     *         if there is no ParameterShell whose parameter has the specified commonName.<br>
     *         or if the one of the newValues is not contained in the valueDomain of the parameter.
     */
    public void setParameterValueArray(String commonName, Object[] newValues) throws OXFException {
        ParameterShell ps = this.getParameterShellWithCommonName(commonName);
        if (ps != null) {
            ps.setSpecifiedValueArray(newValues);
        }
        else {
            throw new OXFException("There is no ParameterShell whose parameter has the commonName '" + commonName
                    + "'.");
        }
    }

    public List<ParameterShell> getParameterShells() {
        return parameterShells;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParameterContainer:\n[");

        for (ParameterShell ps : parameterShells) {
            sb.append("ServiceSidedName: ");
            sb.append(ps.getParameter().getServiceSidedName());
            sb.append("  CommonName: ");
            sb.append(ps.getParameter().getCommonName());
            sb.append("  Value: ");
            sb.append(ps.getSpecifiedValue());
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }

}