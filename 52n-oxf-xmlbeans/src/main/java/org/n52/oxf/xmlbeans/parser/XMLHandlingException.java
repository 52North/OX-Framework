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
