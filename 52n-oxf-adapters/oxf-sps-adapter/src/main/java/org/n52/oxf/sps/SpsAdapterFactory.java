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
package org.n52.oxf.sps;

import static java.lang.String.format;

import org.n52.oxf.sps.v100.SpsAdapterV100;

public class SpsAdapterFactory {
    
    private static final String VERSION_100 = "1.0.0";
    
    private static final String VERSION_200 = "2.0.0";
    
    private SpsAdapterFactory() {
        // do not instantiate static factory
    }
    
    public static SpsAdapter createAdapter(String serviceUrl, String version) throws MissingAdapterImplementationException {
        if (serviceUrl == null) {
            throw new NullPointerException("Service URL must not be null.");
        }
        if (version == null) {
            throw new NullPointerException("Service version must not be null.");
        }
        if (VERSION_100.equals(version)) {
            return new SpsAdapterV100(serviceUrl);
        } else {
            String msg = "No adapter found for version '%s'.";
            throw new MissingAdapterImplementationException(format(msg, version));
        }
    }

}
