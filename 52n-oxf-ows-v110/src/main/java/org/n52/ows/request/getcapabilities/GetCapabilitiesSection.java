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