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

package org.n52.oxf.util;

/**
 * The OXFEventException will be thrown, if an error occures during event-handling-process.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFEventException extends Exception {

    /**
     * Constructs a new OXFEventException.
     */
    public OXFEventException() {
        super();
    }
    
    /**
     * Constructs a new OXFEventException with the specified reason.
     * 
     * @param reason
     *        the reason of the exception.
     */
    public OXFEventException(String reason) {
        super(reason);
    }

    /**
     * Constructs a new OXFEventException with the specified cause.
     * 
     * @param cause
     *        the cause which is saved for later retrieval by the Throwable.getCause() method. A null value is
     *        permitted, and indicates that the cause is nonexistent or unknown.
     */
    public OXFEventException(Throwable cause) {
        super.initCause(cause);
    }

    /**
     * Constructs a new OXFEventException with the specified reason and cause.
     * 
     * @param reason
     *        the reason of the exception.
     * 
     * @param cause
     *        the cause which is saved for later retrieval by the Throwable.getCause() method. A null value is
     *        permitted, and indicates that the cause is nonexistent or unknown.
     */
    public OXFEventException(String reason, Throwable cause) {
        super(reason);
        super.initCause(cause);
    }

}