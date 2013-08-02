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

package org.n52.ows.request.getcapabilities;

import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.ACCEPT_FORMAT;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.ACCEPT_VERSIONS;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.SECTIONS;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.SERVICE;
import static org.n52.ows.request.getcapabilities.GetCapabilitiesParameter.UPDATE_SEQUENCE;

import org.n52.oxf.request.MultiValueRequestParameters;
import org.n52.oxf.request.RequestParameters;

/**
 * <b>This class is test only yet!</b><br>
 * <br>
 * Parameter set needed to create a GetCapabilities request, normally used to discover an OWS service's
 * capabilities and its metadata. Along the common request parameters extension parameters can be added (if
 * understood by the service) via {@link #addNonStandardParameter(String, String)}.<br>
 * <br>
 * The GetCapabilities operation is mandatory to any service which implements an OWS interface Implementation
 * Specification. It allows clients to discover the service and to retrieve metadata about the service's
 * capabilities.<br>
 * <br>
 * The <a href="http://www.opengeospatial.org/standards/common">OWS specification</a> says:<br>
 * <br>
 * <blockquote cite="OGC 06-121r9"> The normal response to the GetCapabilities operation is a service metadata
 * document that is returned to the requesting client. This service metadata document primarily contains
 * metadata about the specific server abilities (such as about the specific data and formats available from
 * that server). This service metadata also makes an OWS server partially self-describing, supporting late
 * binding of clients. </blockquote>
 */
public class GetCapabilitiesParameters {

    private RequestParameters standardParameters = new MultiValueRequestParameters();

    private RequestParameters nonStandardParameters = new MultiValueRequestParameters();

    /**
     * Creates a minimal ready to go GetCapabilities Request. Use the instance methods to add further
     * parameters.
     * 
     * @param service
     *        the service type, e.g. <code>SPS</code>, or <code>SOS</code>.
     */
    public GetCapabilitiesParameters(String service) {
        standardParameters.addParameterValue(SERVICE.name(), service);
    }

    /**
     * Adds an update sequence. If omitted or not supported by the server, the server will return the latest
     * complete service metadata document.<br>
     * <br>
     * There is at maximum one value possible. If an update sequence was set before, it will be overridden.
     * 
     * @param updateSequence
     *        a content section the capabilities shall contain.
     * @return this instance for method chaining.
     */
    public GetCapabilitiesParameters setUpdateSequence(final String updateSequence) {
        standardParameters.addParameterValue(UPDATE_SEQUENCE.name(), updateSequence);
        return this;
    }

    /**
     * Adds accepted versions of the capabilities.<br>
     * <br>
     * The parameter is optional and multiple values are allowed. The ordering is important.
     * 
     * @param acceptVersions
     *        a sequence of accepted capabilities versions (from high priority to low).
     * @return this instance for method chaining.
     */
    public GetCapabilitiesParameters addAcceptVersions(final String... acceptVersions) {
        standardParameters.addParameterStringValues(ACCEPT_VERSIONS.name(), acceptVersions);
        return this;
    }

    /**
     * Adds a content section the capabilities shall contain.<br>
     * <br>
     * The parameter is optional and multiple values are allowed.
     * 
     * @param sections
     *        a sequence of content sections the capabilities shall contain.
     * @return this instance for method chaining.
     */
    public GetCapabilitiesParameters addSections(final GetCapabilitiesSection... sections) {
        standardParameters.addParameterEnumValues(SECTIONS.name(), sections);
        return this;
    }

    /**
     * Adds an accepted format of the capabilities.<br>
     * <br>
     * The parameter is optional and multiple values are allowed.
     * 
     * @param formats
     *        a sequence of accepted formats, e.g. <code>text/xml</code>.
     * @return this instance for method chaining.
     */
    public GetCapabilitiesParameters addAcceptedFormats(final String... formats) {
        standardParameters.addParameterStringValues(ACCEPT_FORMAT.name(), formats);
        return this;
    }

    /**
     * Offers the ability to add non-standard parameters.<br>
     * <br>
     * TODO add some example of a custom request builders (especially for KVP requests)
     * 
     * @param parameter
     *        the parameter name.
     * @param value
     *        the parameter value.
     * @return this instance for method chaining.
     */
    public GetCapabilitiesParameters addNonStandardParameter(final String parameter, final String value) {
        nonStandardParameters.addParameterValue(parameter, value);
        return this;
    }

    public RequestParameters getStandardParameters() {
        return standardParameters;
    }

    public RequestParameters getNonStandardParameters() {
        return nonStandardParameters;
    }

}
