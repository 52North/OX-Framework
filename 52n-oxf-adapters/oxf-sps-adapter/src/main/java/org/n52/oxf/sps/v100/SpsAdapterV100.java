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
package org.n52.oxf.sps.v100;

import org.n52.ows.request.getcapabilities.GetCapabilitiesParameters;
import org.n52.ows.request.getcapabilities.GetCapabilitiesRequest;
import org.n52.oxf.request.Request;
import org.n52.oxf.sps.SpsAdapter;

public class SpsAdapterV100 extends SpsAdapter {
    
    private String serviceUrl;
    
    public SpsAdapterV100(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public boolean isSupporting(String version) {
        return "1.0.0".equals(version);
    }

    public GetCapabilitiesRequest createGetCapabilitiesRequest(GetCapabilitiesParameters parameters) {
        return new GetCapabilitiesRequest(parameters);
    }

    public void send(Request request) {
        request.sendViaGet(serviceUrl, httpClient);
//        httpClient.executeGet(serviceUrl, request.getRequestParameters()); ??? 
    }

}
