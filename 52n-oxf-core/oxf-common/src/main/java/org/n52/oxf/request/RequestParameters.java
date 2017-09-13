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
package org.n52.oxf.request;

import java.util.Collection;

/**
 * Represents an assembly of request parameters (key-value pairs). Multiple parameter values are allowed, i.e.
 * one key can be associated to one or more parameter values. <code>null</code> values are hold as empty
 * strings.
 *
 * TODO check how duplicates are handled
 *
 */
public interface RequestParameters {

    /**
     * Checks if the assembly is empty at all.
     *
     * @return <code>true</code> if there are no key-value pairs available,<br>
     *      <code>false</code> if the assembly contains at least one key-value pair.
     */
    boolean isEmpty();

    /**
     * Checks if the given parameter has at least one associated value.
     *
     * @param parameter
     *        the parameter key to check.
     * @return <code>true</code> if the passed parameter has zero associated values
     *                  or is not present in the assembly,<br>
     *         <code>false</code> if the parameter is has one or more associated values.
     */
    boolean isEmpty(String parameter);

    /**
     * Checks if the given parameter is present in the assembly.
     *
     * @param parameter
     *        the parameter key to check.
     * @return <code>true</code> if the passed parameter has one or more associated value(s),<br>
     *         <code>false</code> if parameter is not present.
     */
    boolean contains(String parameter);

    /**
     * Checks if the assembly has exact one parameter value associated to the given key. If the value is not
     * present at all the method will return <code>false</code>. This method is not threadsafe, so do not
     * expect it as a reliable check; other threads may have added new values in the meantime.
     *
     * @param parameter
     *        the parameter key to check.
     * @return <code>true</code> if there is exact one value associated with the given parameter,<br>
     *         <code>false</code> if there are more than one parameter value, or the parameter is not present.
     * @throws NullPointerException
     *         may be thrown if no entry for the given parameter can be found.
     */
    boolean isSingleValue(String parameter);

    /**
     * Checks if the assembly has more than one parameter value associated to the given key. If the value is
     * not present at all the method will return <code>false</code>. This method is not threadsafe, so do not
     * expect it as a reliable check; other threads may have added new values in the meantime.
     *
     * @param parameter
     *        the parameter key to check.
     * @return <code>true</code> if more than one value is associated with the given parameter,<br>
     *         <code>false</code> if there is only one parameter value, or the parameter is not present.
     * @throws NullPointerException
     *         may be thrown if no entry for the given parameter can be found.
     */
    boolean hasMultipleValues(String parameter);

    /**
     * Gets the first parameter value for the given parameter. It is not guaranteed that, if multiple
     * parameters are associated with the given parameter, always the same value is returned. It is meant as
     * convenient method for parameters which have a cardinality of one, or it is clear that only one
     * parameter value is associated with the parameter.<br>
     * <br>
     * <b>Note:</b> If the given parameter is not contained by the assembly or <code>null</code> value
     * associated to it an empty string &quot;&quot; will be returned. Use {@link #contains(String)} to
     * ensure if the parameter is contained by this assembly.
     *
     * @param parameter
     *        the parameter key to check.
     * @return the first parameter value associated to the given parameter.
     * @see #contains(String)
     */
    String getSingleValue(String parameter);

    /**
     * Returns all parameter names available in this parameter assembly.
     *
     * @return a read-only access to all parameter keys added to this assembly.
     */
    Collection<String> getParameterNames();

    /**
     * Get read-only access to all parameter values associated with the given parameter.
     *
     * @param parameter
     *        the parameter key to check.
     * @return read-only access on the parameter values associated with the given parameter.
     */
    Collection<String> getAllValues(String parameter);

    /**
     * Merge the given parameter assembly with this instance.
     *
     * @param parameters
     *        the parameter assembly to merge with.
     * @return <code>true</code> if the assembly has changed,<br><code>false</code> otherwise.
     */
    boolean mergeWith(RequestParameters parameters);

    /**
     * Adds a new parameter value to the given parameter. A parameter can have multiple parameter values
     * associated with it. No replacement takes place when adding different values to exactly the same
     * parameter key. If you want to remove a particular parameter value use {@link #remove(String)}.<br>
     * <br>
     * <b>Note:</b> <code>null</code> values are stored as empty Strings.
     *
     * @param parameter
     *        the parameter key.
     * @param value
     *        the value to associate the with parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    boolean addParameterValue(String parameter, String value);

    /**
     * Adds a bulk of parameter values (from a particular codelist) to the given parameter. If you want to
     * remove a particular parameter value use {@link #remove(String)}.<br>
     * <br>
     * <b>Note:</b> <code>null</code> values are stored as empty Strings.
     *
     * @param parameter
     *        the parameter key.
     * @param values
     *        the {@link Enum#toString()} values to associate with the parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    boolean addParameterEnumValues(String parameter, Enum< ? >... values);

    /**
     * Adds a bulk of parameter values to the given parameter. If you want to remove a particular parameter
     * value use {@link #remove(String)}.<br>
     * <br>
     * <b>Note:</b> <code>null</code> values are stored as empty Strings.
     *
     * @param parameter
     *        the parameter key.
     * @param values
     *        the values to associate with the parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    boolean addParameterStringValues(String parameter, String... values);

    /**
     * Adds a bulk of parameter values to the given parameter. If you want to remove a particular parameter
     * value use {@link #remove(String)}.<br>
     * <br>
     * <b>Note:</b> <code>null</code> values are stored as empty Strings.
     *
     * @param parameter
     *        the parameter key.
     * @param values
     *        the values to associate with the parameter.
     * @return <code>true</code> if the assembly has changed (size has increased).
     */
    boolean addParameterValues(String parameter, Iterable<String> values);

    /**
     * Removes the given parameter and its associated values.
     *
     * @param parameter
     *        the parameter key.
     * @return the previously associated value(s) for the given parameter.
     */
    Collection<String> remove(String parameter);

    /**
     * Performs a complete clean of this assembly instance.
     */
    void removeAll();

}
