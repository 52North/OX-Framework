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

import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.xml.XMLConstants;

/**
 * Allow substitutions of gml:AbstractFeature.
 * This lax validation lets pass every child, hence
 * it checks not _if_ this is a valid substitution.
 * 
 * @author matthes rieke <m.rieke@52north.org>
 */
public class GMLAbstractFeatureCase extends AbstractLaxValidationCase {

	private static GMLAbstractFeatureCase instance = null;
	
	private GMLAbstractFeatureCase() {}
	
	public static GMLAbstractFeatureCase getInstance() {
		if (instance == null) {
			instance = new GMLAbstractFeatureCase();
		}
		return instance;
	}

	@Override
	public boolean shouldPass(final XmlValidationError xve) {
		return xve.getExpectedQNames() != null && (xve.getExpectedQNames().contains(XMLConstants.FEATURE_QN)
				|| xve.getExpectedQNames().contains(XMLConstants.FEATURE_COLLECTION_QN));
	}
}