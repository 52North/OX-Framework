/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.swes.exception;

import org.n52.ows.exception.OwsException;

/**
 * One or more of the extensions used in the operation request are unknown to or not supported by the service. 
 * <br><br>
 * <b>Note:</b> A service shall add the complete unsupported request extension into the ExtensionText property of the
 * Exception. In the XML encoding, use CDATA to surround markup. If the encoded extension itself contains the
 * string “]]>�?, use two CDATA tags following this pattern: <![CDATA[content]]]]><![CDATA[>content]]>
 */
public class RequestExtensionNotSupportedException extends OwsException {

    private static final long serialVersionUID = -3798668983249511518L;

    public RequestExtensionNotSupportedException() {
        super(SwesExceptionCode.REQUEST_EXTENSION_NOT_SUPPORTED.getExceptionCode());
    }

    @Override
    public int getHttpStatusCode() {
        // not found in specification, but NOT_IMPLEMENTED is most obvious
        return NOT_IMPLEMENTED;
    }

}
