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
		if (!(validationError instanceof XmlValidationError)) {
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
		return xve.getOffendingQName() == null &&
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
