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
package org.n52.ows.request;

import org.n52.oxf.request.MultiValueRequestParameters;

/**
 * Assembles all parameters needed for a GetCapabilities request.
 */
public class GetCapabilitiesParameters extends MultiValueRequestParameters {
    
    private final String REQUEST_PARAMETER = "request";

    static final String SERVICE_PARAMETER = "service";

    static final String SECTION_PARAMETER = "sections";

    static final String UPDATE_SEQUENCE = "updateSequence";

    static final String ACCEPT_FORMATS_PARAMETER = "acceptFormat";

    static final String ACCEPT_VERSIONS_PARAMETER = "acceptVersion";

    public GetCapabilitiesParameters(final String service, final String acceptVersion) {
        addNonEmpty(REQUEST_PARAMETER, "GetCapabilities");
        addNonEmpty(SERVICE_PARAMETER, service);
        addParameterValue(ACCEPT_VERSIONS_PARAMETER, acceptVersion);
    }

    public GetCapabilitiesParameters(final String service, final String... acceptVersions) {
        addNonEmpty(SERVICE_PARAMETER, service);
        if (acceptVersions != null) {
            for (final String acceptVersion : acceptVersions) {
                addAcceptedVersion(acceptVersion);
            }
        }
    }

    /**
     * Adds an update sequence. If omitted or not supported by the server, the server will return the latest
     * complete service metadata document.<br>
     * <br>
     * There is at maximum one value possible. If an update sequence was set before, it will be overridden.
     * 
     * @param updateSequence
     *        a content section the capabilities shall contain.
     */
    public void setUpdateSequence(final String updateSequence) {
        addParameterValue(UPDATE_SEQUENCE, updateSequence);
    }

    /**
     * Adds a content section the capabilities shall contain.<br>
     * <br>
     * The parameter is optional and multiple values are can be added.
     * 
     * @param section
     *        a content section the capabilities shall contain.
     */
    public void addSection(final String section) {
        addParameterValue(SECTION_PARAMETER, section);
    }

    /**
     * Adds an accepted format of the capabilities.<br>
     * <br>
     * The parameter is optional and multiple values are can be added.
     * 
     * @param acceptedFormat
     *        an accepted capabilities format.
     */
    public void addAcceptedFormat(final String acceptedFormat) {
        addParameterValue(ACCEPT_FORMATS_PARAMETER, acceptedFormat);
    }

    /**
     * Adds an accepted version of the capabilities.<br>
     * <br>
     * The parameter is optional and multiple values are can be added.
     * 
     * @param acceptedVersion
     *        an accepted capabilities version.
     */
    public void addAcceptedVersion(final String acceptedVersion) {
        addParameterValue(ACCEPT_VERSIONS_PARAMETER, acceptedVersion);
    }

    public boolean isValid() {
        return !isEmptyValue(SERVICE_PARAMETER);
    }

}
