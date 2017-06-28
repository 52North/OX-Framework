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
package org.n52.oxf.ows.capabilities;

/**
 * This class can be seen as a special container. It is used by the class {@link Parameter}
 * to lay down the domain of values which can be associated with the Parameter.<br>
 * A central point of this class is the {@linkplain IValueDomain#containsValue(Object)} method which can be used by the
 * client to proof whether a value is contained by the domain or not.
 *
 * Some remarks on implementing this interface: Please do not allow empty constructors, because empty constructors
 * do not make sense for the framework.
 *
 * @author <a href="mailto:broering@52north.org">Arne Br&ouml;ring</a>
 * @param <T>
 *        T is the type of the values that are contained in and can be added to this IValueDomain.
 * @see <a href=IRangeValueDomain.html>IRangeValueDomain</a>
 * @see <a href=IDiscreteValueDomain.html>IDiscreteValueDomain</a>
 */
public interface IValueDomain<T> {

    /**
     * @param t the value to check
     *
     * @return <code>true</code>,if the value t is part of this value domain.
     */
    public boolean containsValue(T t);

    /**
     * @return plain text description of this valueDomain.
     */
    public String getDomainDescription();

    /**
     * @return a XML representation of this ValueDomain.
     */
    public String toXML();

//    /**
//     * @return the type of values which can be added to this IValueDomain.
//     */
//    public Class<T> getValueClass();

    // TODO What is this method used for? Most implementing classes to nonsense with it.
    public T produceValue(String... stringArray);
}
