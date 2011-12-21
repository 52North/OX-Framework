/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 22.07.2007
 *********************************************************************************/


package org.n52.oxf.util;

import org.n52.oxf.OXFRuntimeException;

/**
 * 
 * Simple helder class with static methods for regular needed handling of EPSG codes for spatial
 * reference systems.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class SrsHelper {

    private static final String SWE_POSITION_EPSG_PREFIX = "urn:ogc:def:crs:epsg:";

    /**
     * 
     * @param epsg
     * @return
     */
    public static String getReferenceFrameString(int epsg) {
        return SWE_POSITION_EPSG_PREFIX + epsg;

    }

    /**
     * 
     * @param srsString
     * @return
     */
    public static int getEpsgCode(String srsStringP) {
        String srsString = srsStringP.toLowerCase();

        if ( !srsString.startsWith(SWE_POSITION_EPSG_PREFIX)) {
            throw new OXFRuntimeException("The 'referenceFrame' attribute of swe:positionType has to be set and must be epsg code with prefix '"
                    + SWE_POSITION_EPSG_PREFIX + "' !!");
        }
        try {
            int epsgCode = new Integer(srsString.substring(srsString.lastIndexOf(":") + 1)).intValue();

            return epsgCode;
        }
        catch (Exception e) {
            throw new OXFRuntimeException("The 'referenceFrame' attribute of swe:positionType has to be set and must be epsg code with prefix '"
                    + SWE_POSITION_EPSG_PREFIX + "' !!");
        }
    }

    /**
     * used as identifier in swing ui
     * 
     * @param epsg
     * @return
     */
    public static String getSpatialReferenceSystemAndCode(int epsg) {
        return "EPSG:" + epsg;
    }

}
