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
package org.n52.oxf.sos.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SosUtil {

    /**
     * The Type of the service which is connectable by this ServiceAdapter
     */
    public static final String SERVICE_TYPE = "SOS";

    /**
     * the Versions of the services which are connectable by this ServiceAdapter.
     * List contains: [0.0.0, 1.0.0, 2.0.0] hence index is matching version
     */
    public static final List<String> SUPPORTED_VERSIONS = Collections.unmodifiableList(Arrays.asList("0.0.0","1.0.0", "2.0.0"));

    public static boolean isVersion100(final String version) {
        return SUPPORTED_VERSIONS.get(1).equals(version);
    }

    public static boolean isVersion200(final String version) {
        return SUPPORTED_VERSIONS.get(2).equals(version);
    }

    private SosUtil() {}

}
