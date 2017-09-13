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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.n52.oxf.om.x20;

import org.n52.oxf.valueDomains.AnyValueDomain;
import org.n52.oxf.ows.capabilities.Parameter;

/**
 * Implementation for OGC O&M v2.0 Paramter
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @param <T>
 */
public abstract class OmParameter<T> {

    private String name;

    private T value;

    public final static String PARAMETER_NAME = "omParameters";

    public final static Parameter PARAMETER = new Parameter(PARAMETER_NAME,
            false,
            new AnyValueDomain(),
            PARAMETER_NAME);

    public OmParameter(final String name, final T value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' should not be null or empty!");
        }
        if (value == null) {
            throw new IllegalArgumentException("Parameter 'value' should not be null!");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
