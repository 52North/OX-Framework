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

package org.n52.oxf.wcs.adapter;

import java.net.MalformedURLException;
import java.net.URL;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WCSRequestBuilder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WCSRequestBuilder.class);

    /**
     * 
     * @param operation
     * @param parameterContainer
     * @return
     * @throws OXFException
     */
    public String buildGetCapabilitiesRequest(Operation operation, ParameterContainer parameterContainer) throws OXFException {
        String requestStringGET = cleanOperationURL(operation.getDcps()[0].getHTTPGetRequestMethods().get(0).getOnlineResource().getHref());

        requestStringGET += "REQUEST=GetCapabilities&SERVICE=WCS";

        // Add Version
        if (parameterContainer.containsParameterShellWithServiceSidedName("VERSION")) {
            ParameterShell version = parameterContainer.getParameterShellWithServiceSidedName("VERSION");
            requestStringGET += "&VERSION=" + version.getSpecifiedValue();
        }

        // SECTION-Parameter: wenn gesetzt dann entsprechend anh�ngen:
        if (parameterContainer.containsParameterShellWithServiceSidedName("SECTION")) {
            ParameterShell section = parameterContainer.getParameterShellWithServiceSidedName("SECTION");

            if (section.hasMultipleSpecifiedValues()) {
                String[] specifiedValues = (String[]) section.getSpecifiedValueArray();

                requestStringGET += "&SECTION=" + specifiedValues[0];
                for (int i = 1; i < specifiedValues.length; i++) {
                    requestStringGET += "," + specifiedValues[i];
                }
            }
            else if (section.hasSingleSpecifiedValue()) {
                requestStringGET += (String) section.getSpecifiedValue();
            }
        }

        return requestStringGET;
    }

    /**
     * 
     * @param operation
     * @param parameterContainer
     * @return
     * @throws OXFException
     */
    public String buildDescribeCoverageRequest(Operation operation, ParameterContainer parameterContainer) throws OXFException {
        String requestStringGET = cleanOperationURL(operation.getDcps()[0].getHTTPGetRequestMethods().get(0).getOnlineResource().getHref());

        requestStringGET += "REQUEST=DescribeCoverage&SERVICE=WCS";

        String versionStr;

        if (parameterContainer.containsParameterShellWithServiceSidedName("VERSION")) {
            ParameterShell version = parameterContainer.getParameterShellWithServiceSidedName("VERSION");
            requestStringGET += "&VERSION=" + version.getSpecifiedValue();
            versionStr = version.getSpecifiedValue().toString();
        }
        else {
            requestStringGET += "&VERSION=1.0.0";
            versionStr = "1.0.0";
        }

        // COVERAGE-Parameter: if defined:
        if (parameterContainer.containsParameterShellWithServiceSidedName("COVERAGE")) {
            ParameterShell coverage = parameterContainer.getParameterShellWithServiceSidedName("COVERAGE");

            if (versionStr.equals("1.0.0")) {
                requestStringGET += "&COVERAGE=";
            }
            else if (versionStr.equals("1.1.1")) {
                requestStringGET += "&IDENTIFIERS=";
            }
            else {
                throw new OXFException("Usupported WCS version: '" + versionStr + "'");
            }
            
            if (coverage.hasMultipleSpecifiedValues()) {
                String[] specifiedValues = (String[]) coverage.getSpecifiedValueArray();
                
                requestStringGET += specifiedValues[0];
                for (int i = 1; i < specifiedValues.length; i++) {
                    requestStringGET += "," + specifiedValues[i];
                }
            }
            else if (coverage.hasSingleSpecifiedValue()) {
                requestStringGET += (String) coverage.getSpecifiedValue();
            }
        }

        return requestStringGET;
    }

    /**
     * 
     * @param operation
     * @param parameterContainer
     * @return
     * @throws OXFException
     */
    public String buildGetCoverageRequest(Operation operation, ParameterContainer parameterContainer) throws OXFException {
        String requestStringGET = cleanOperationURL(operation.getDcps()[0].getHTTPGetRequestMethods().get(0).getOnlineResource().getHref());

        requestStringGET += "REQUEST=GetCoverage&SERVICE=WCS";

        String versionStr;

        // Add Version - Default ist 1.0.0
        if (parameterContainer.containsParameterShellWithServiceSidedName("VERSION")) {
            ParameterShell version = parameterContainer.getParameterShellWithServiceSidedName("VERSION");
            requestStringGET += "&VERSION=" + version.getSpecifiedValue();
            versionStr = version.getSpecifiedValue().toString();
        }
        else {
            requestStringGET += "&VERSION=1.0.0";
            versionStr = "1.0.0";
        }

        // COVERAGE-Parameter:
        ParameterShell coverage = parameterContainer.getParameterShellWithServiceSidedName("COVERAGE");
        // bisher sind fuer diesen Parameter lediglich "Single Values"
        // zugelassen:
        if (versionStr.equals("1.1.1")) {
            requestStringGET += "&IDENTIFIER=" + (String) coverage.getSpecifiedValue();
        }
        else {
            requestStringGET += "&COVERAGE=" + (String) coverage.getSpecifiedValue();
        }
        // CRS-Parameter:
        ParameterShell crs = parameterContainer.getParameterShellWithServiceSidedName("CRS");

        // BBOX-Parameter:
        ParameterShell bBox = parameterContainer.getParameterShellWithServiceSidedName("BBOX");
        if (versionStr.equals("1.1.1")) {
            requestStringGET += "&BoundingBox=" + ((BoundingBox) bBox.getSpecifiedValue()).toKVPString();
            try {
                requestStringGET += "," + (String) crs.getSpecifiedValue();
            }
            catch (NullPointerException e) {
                // TODO: CRS not defined; how react?
                LOGGER.error("CRS not defined");
            }
        }
        else {
            try {
                requestStringGET += "&CRS=" + (String) crs.getSpecifiedValue();
            }
            catch (NullPointerException e) {
                // TODO: CRS not defined; how react?
                LOGGER.error("CRS not defined");
            }
            requestStringGET += "&BBOX=" + ((BoundingBox) bBox.getSpecifiedValue()).toKVPString();
        }

        // FORMAT-Parameter:
        ParameterShell format = parameterContainer.getParameterShellWithServiceSidedName("FORMAT");
        try {
            requestStringGET += "&FORMAT=" + (String) format.getSpecifiedValue();
        }
        catch (NullPointerException e) {
            // TODO: FORMAT not defined; how react?
            LOGGER.error("FORMAT not defined");
        }

        if (versionStr.equals("1.1.1")) {

            // GRID Options

            // GridBaseCRS-Parameter:
            if (parameterContainer.containsParameterShellWithServiceSidedName("GRIDBASECRS")) {
                ParameterShell gridBaseCRS = parameterContainer.getParameterShellWithServiceSidedName("GRIDBASECRS");
                requestStringGET += "&GRIDBASECRS=" + gridBaseCRS.getSpecifiedValue();
            }

            if (parameterContainer.containsParameterShellWithServiceSidedName("GRIDTYPE")) {
                // GridType-Parameter:
                ParameterShell gridType = parameterContainer.getParameterShellWithServiceSidedName("GRIDTYPE");
                requestStringGET += "&GRIDTYPE=" + gridType.getSpecifiedValue();
            }

            if (parameterContainer.containsParameterShellWithServiceSidedName("GRIDCS")) {
                // GridCS-Parameter:
                ParameterShell gridCS = parameterContainer.getParameterShellWithServiceSidedName("GRIDCS");
                requestStringGET += "&GRIDCS=" + gridCS.getSpecifiedValue();
            }

            if (parameterContainer.containsParameterShellWithServiceSidedName("GRIDORIGIN")) {
                // GridOrigin-Parameter:
                ParameterShell gridOrigin = parameterContainer.getParameterShellWithServiceSidedName("GRIDORIGIN");
                requestStringGET += "&GRIDORIGIN=" + gridOrigin.getSpecifiedValue();
            }

            if (parameterContainer.containsParameterShellWithServiceSidedName("GRIDOFFSETS")) {
                // Gridoffsets-Parameter:
                ParameterShell gridOffsets = parameterContainer.getParameterShellWithServiceSidedName("GRIDOFFSETS");
                requestStringGET += "&GRIDOFFSETS=" + gridOffsets.getSpecifiedValue();
            }

            ////// dirty OWS-5 hack:
            requestStringGET += "&Store=true";
            //////
        }
        else {
            // WIDTH-Parameter:
            ParameterShell width = parameterContainer.getParameterShellWithCommonName("WIDTH");
            requestStringGET += "&WIDTH=" + width.getSpecifiedValue();

            // HEIGHT-Parameter:
            ParameterShell height = parameterContainer.getParameterShellWithCommonName("HEIGHT");
            requestStringGET += "&HEIGHT=" + height.getSpecifiedValue();
        }

        // Replace ' ' character with '%20' character:
        requestStringGET = requestStringGET.replace(" ", "%20");
        
        return requestStringGET;
    }

    private String cleanOperationURL(String originalRequest) throws OXFException {
        try {
            // requestStringGET should look like this:
            // http://server:port/file?
            // http://server:port/file?query&

            char lastChar = originalRequest.charAt(originalRequest.length() - 1);

            if (lastChar == '/') {
                originalRequest = originalRequest.substring(0, originalRequest.length() - 1);
            }

            // if not yet '?' appended, append it:
            if (new URL(originalRequest).getQuery() == null) {
                originalRequest += "?";
            }
            else if (lastChar != '?' && lastChar != '&') {
                originalRequest += "&";
            }

            return originalRequest;
        }
        catch (MalformedURLException e) {
            throw new OXFException(e);
        }
    }

}