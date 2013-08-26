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

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlValidationError;

/**
 * Interface for providing exceptional cases
 * in XML validation (e.g. substitution groups).
 * 
 * @author <a href="mailto:m.rieke@52north.org">Matthes Rieke</a>
 *
 */
public interface LaxValidationCase {
	
	/**
	 * @param validationError the validation error
	 * @return true, if this lax case embodies an exceptional
	 * validation case on the given error
	 */
	boolean shouldPass(XmlError validationError);
	
	/**
	 * @deprecated no longer called, due to the fact that errors can also be XmlError (the supertype).
	 * 	Use {@link #shouldPass(XmlError)} instead and cast if needed.
	 * @param validationError the validation error
	 * @return true, if this lax case embodies an exceptional validation case on the given error
	 */
	@Deprecated
	boolean shouldPass(XmlValidationError validationError);
	
}