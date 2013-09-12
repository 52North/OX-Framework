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

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlValidationError;
import org.n52.oxf.xml.XMLConstants;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SFSpatialSamplingFeatureCase extends AbstractLaxValidationCase {
	
	private static SFSpatialSamplingFeatureCase instance;

	@Override
	public boolean shouldPass(final XmlValidationError xve)
	{
		final QName offending = xve.getOffendingQName();
		final List<?> expected = xve.getExpectedQNames();
		final QName field = xve.getFieldQName();
		if (offending != null && 
				offending.equals(XMLConstants.QN_SF_2_0_SPATIAL_SAMPLING_FEATURE) &&
				field.equals(XMLConstants.QN_OM_2_0_FEATURE_OF_INTEREST) &&
				expected.contains(XMLConstants.QN_GML_3_2_ABSTRACT_FEATURE))
		{
			return validateSubstitutionGroup(xve);
		}
		return false;
	}

	private boolean validateSubstitutionGroup(final XmlValidationError xve)
	{
		try {
			final SFSpatialSamplingFeatureType featureDocument = SFSpatialSamplingFeatureType.Factory.parse(xve.getObjectLocation().xmlText());
			final Collection<XmlError> revalidation = XMLBeansParser.validate(featureDocument);
			return revalidation.size()==0?true:false;
		} catch (final XmlException e) {}
		return false;
	}

	public static SFSpatialSamplingFeatureCase getInstance()
	{
		if (instance == null) {
			instance = new SFSpatialSamplingFeatureCase();
		}
		return instance;
	}

	private SFSpatialSamplingFeatureCase() {}
}
