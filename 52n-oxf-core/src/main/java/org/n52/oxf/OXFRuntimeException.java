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
 
 Created on: 25.08.2005
 *********************************************************************************/

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