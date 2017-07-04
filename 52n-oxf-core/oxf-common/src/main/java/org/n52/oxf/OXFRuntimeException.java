/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.oxf;

/**
 * OXFRuntimeException can be thrown during the normal operation of the Java Virtual Machine.
 * <br>
 * A method is not required to declare in its throws clause any subclasses of OXFRuntimeException that might
 * be thrown during the execution of the method but not caught.
 *
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFRuntimeException extends RuntimeException {

    /**
     * Constructs a new OXFRuntimeException.
     */
    public OXFRuntimeException() {
        super();
    }

    /**
     * Constructs a new OXFRuntimeException with the specified reason.
     *
     * @param reason
     *        the reason of the exception.
     */
    public OXFRuntimeException(String reason) {
        super(reason);
    }

    /**
     * Constructs a new OXFRuntimeException with the specified cause.
     *
     * @param cause
     *        the cause which is saved for later retrieval by the Throwable.getCause() method. A null value is
     *        permitted, and indicates that the cause is nonexistent or unknown.
     */
    public OXFRuntimeException(Throwable cause) {
        super.initCause(cause);
    }

    /**
     * Constructs a new OXFRuntimeException with the specified reason and cause.
     *
     * @param reason
     *        the reason of the exception.
     *
     * @param cause
     *        the cause which is saved for later retrieval by the Throwable.getCause() method. A null value is
     *        permitted, and indicates that the cause is nonexistent or unknown.
     */
    public OXFRuntimeException(String reason, Throwable cause) {
        super(reason);
        super.initCause(cause);
    }

}
