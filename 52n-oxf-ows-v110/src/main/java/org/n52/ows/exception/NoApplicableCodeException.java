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
package org.n52.ows.exception;

/**
 * No other exceptionCode specified by this service and server applies to this exception. 
 * <br><br>Http Status Code vary from 3xx, 4xx, and 5xx. 
 */
public class NoApplicableCodeException extends OwsException {

    private static final long serialVersionUID = 7670774591278476665L;
    
    private int statusCode;

    /**
     * @param httpStatusCode HTTP Status Code of category 3xx, 4xx, or 5xx
     */
    public NoApplicableCodeException(int httpStatusCode) {
        super(OwsExceptionCode.NO_APPLICABLE_CODE.getExceptionCode());
        if (!hasValidRange(httpStatusCode)) {
            throw new IllegalArgumentException("HttpStatusCode must be 3xx, 4xx, or 5xx");
        }
        this.statusCode = httpStatusCode;
    }

    private boolean hasValidRange(int httpStatusCode) {
        return 300 <= httpStatusCode && httpStatusCode < 600;
    }

    @Override
    public int getHttpStatusCode() {
        return statusCode;
    }
    
}
