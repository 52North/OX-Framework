/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.ows.request.getcapabilities;

/**
 * <b>This class is test only yet!</b>
 * <br><br>
 * The sections names as specified by the SPS 1.0.0 implementation specification (OGC 07-014r3).<br>
 * <br>
 * Call {@link #toString()} method to retrieve the section names as defined in the spec.
 */
public enum GetCapabilitiesSection {
    /**
     * Metadata about this specific server.
     */
    SERVICE_IDENTIFICATION("ServiceIdentification"),
    /**
     * Metadata about the organization operating this server.
     */
    SERVICE_PROVIDER("ServiceProvider"),
    /**
     * Metadata about the operations specified by this service and implemented by this server, including the
     * URLs for operation requests.
     */
    OPERATION_METADATA("OperationsMetadata"),
    /**
     * Metadata about the data served by this server.
     */
    CONTENTS("Contents");

    private String sectionName;

    private GetCapabilitiesSection(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * @return the section name defined within the spec.
     */
    @Override
    public String toString() {
        return sectionName;
    }
}