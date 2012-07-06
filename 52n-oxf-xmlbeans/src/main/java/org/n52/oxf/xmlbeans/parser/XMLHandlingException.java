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
package org.n52.oxf.xmlbeans.parser;

import java.util.Iterator;
import java.util.List;

import org.apache.xmlbeans.XmlError;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * @author matthes rieke <m.rieke@52north.org>
 * 
 */
public class XMLHandlingException extends Exception {

	/**
	 * auto generated serialVerionUID
	 */
	private static final long serialVersionUID = -5705266650634591453L;

	protected String message = "";

	/**
	 * @param xmlErrors
	 *            the errors string.
	 * @param cause
	 *            the cause
	 */
	public XMLHandlingException(String xmlErrors, Throwable cause) {
		super(xmlErrors, cause);
	}

	/**
	 * @param xmlErrors
	 *            the errors string.
	 */
	public XMLHandlingException(String xmlErrors) {
		super(xmlErrors);
	}

	/**
	 * @param xmlErrors
	 *            The error string.
	 * @param validationErrors
	 *            List of validation errors.
	 */
	public XMLHandlingException(String xmlErrors, List<?> validationErrors) {
		super(xmlErrors);
		StringBuffer sb = new StringBuffer();
		Iterator<?> iter = validationErrors.iterator();

		while (iter.hasNext()) {
			sb.append("[Validation-Error] " + iter.next() + "\n");
		}
		this.message = sb.toString();
	}
	
	public XMLHandlingException(XmlError err) {
		super(err.getMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return super.getMessage() + this.message;
	}


}
