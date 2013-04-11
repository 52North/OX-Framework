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

package org.n52.oxf.request;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

public interface ResponseHandler {

    /**
     * Called when a response was received successfully. Content and HTTP status code is passed for further
     * processing.
     * 
     * @param response
     *        response's content as stream.
     * @param httpResponseCode
     *        the HTTP status code.
     */
    public void onSuccess(InputStream response, int httpStatusCode);

    /**
     * Called when no response could be received. When this method is called, any low-level exceptions (e.g.
     * {@link IOException} {@link TimeoutException}, etc.) should already have been handled and logged
     * appropriatly. Use this method to trigger higher-level error messaging.
     */
    public void onFailure(String reason);

}
