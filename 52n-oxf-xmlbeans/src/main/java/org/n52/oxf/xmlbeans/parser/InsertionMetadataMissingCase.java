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
package org.n52.oxf.xmlbeans.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.xml.XMLConstants;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class InsertionMetadataMissingCase extends AbstractDependentLaxValidationCase {

	private static InsertionMetadataMissingCase instance;

	@Override
	public boolean shouldPass(final XmlError validationError,
			final List<XmlError> allExceptionalCases)
	{
		if (allExceptionalCases == null || allExceptionalCases.isEmpty()) {
			return false;
		}
		final XmlValidationError xve = (XmlValidationError) validationError;
		if (isContextCorrect(xve)) {
			// clone list
			final ArrayList<XmlError> workingCopy = new ArrayList<XmlError>(allExceptionalCases);
			// remove validation error
			workingCopy.remove(validationError);
			// check for potential cause
			for (final XmlError xmlError : workingCopy) {
				if (SosInsertionMetadataCase.getInstance().shouldPass(xmlError))
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isContextCorrect(final XmlValidationError xve)
	{
		return xve != null &&
				xve.getOffendingQName() == null &&
				xve.getFieldQName().equals(XMLConstants.QN_SWES_2_0_METADATA) &&
				xve.getExpectedQNames().contains(XMLConstants.QN_SWES_2_0_INSERTION_METADATA);
	}

	public static InsertionMetadataMissingCase getInstance() {
		if (instance == null) {
			instance = new InsertionMetadataMissingCase();
		}
		return instance;
	}

}
