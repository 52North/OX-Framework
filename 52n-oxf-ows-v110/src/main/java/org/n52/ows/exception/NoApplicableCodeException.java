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
