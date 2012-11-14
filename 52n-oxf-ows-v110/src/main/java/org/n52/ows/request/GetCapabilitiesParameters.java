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

package org.n52.ows.request;

import org.n52.oxf.util.web.MultimapRequestParameters;

/**
 * Assembles all parameters needed for a GetCapabilities request.
 */
public class GetCapabilitiesParameters extends MultimapRequestParameters {
    
    private final String REQUEST_PARAMETER = "request";

    static final String SERVICE_PARAMETER = "service";

    static final String SECTION_PARAMETER = "sections";

    static final String UPDATE_SEQUENCE = "updateSequence";

    static final String ACCEPT_FORMATS_PARAMETER = "acceptFormat";

    static final String ACCEPT_VERSIONS_PARAMETER = "acceptVersion";

    public GetCapabilitiesParameters(String service, String acceptVersion) {
        addNonEmpty(REQUEST_PARAMETER, "GetCapabilities");
        addNonEmpty(SERVICE_PARAMETER, service);
        addParameterValue(ACCEPT_VERSIONS_PARAMETER, acceptVersion);
    }

    public GetCapabilitiesParameters(String service, String... acceptVersions) {
        addNonEmpty(SERVICE_PARAMETER, service);
        if (acceptVersions != null) {
            for (String acceptVersion : acceptVersions) {
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
    public void addUpdateSequence(String updateSequence) {
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
    public void addSection(String section) {
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
    public void addAcceptedFormat(String acceptedFormat) {
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
    public void addAcceptedVersion(String acceptedVersion) {
        addParameterValue(ACCEPT_VERSIONS_PARAMETER, acceptedVersion);
    }

    public boolean isValid() {
        return !isEmptyValue(SERVICE_PARAMETER);
    }

}
