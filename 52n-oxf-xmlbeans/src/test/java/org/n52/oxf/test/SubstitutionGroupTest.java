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
package org.n52.oxf.test;

import junit.framework.TestCase;

import net.opengis.gml.AbstractFeatureCollectionType;
import net.opengis.gml.AbstractFeatureType;
import net.opengis.gml.FeatureCollectionDocument;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.GridCoverageDocument;
import net.opengis.gml.GridCoverageType;
import net.opengis.sampling.x10.SamplingFeatureDocument;
import net.opengis.sampling.x10.SamplingFeatureType;

import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.xmlbeans.tools.XMLBeansTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test case for substitution group and the qualification of those using
 * {@link XMLBeansTools#qualifySubstitutionGroup(org.apache.xmlbeans.XmlObject, javax.xml.namespace.QName, org.apache.xmlbeans.SchemaType)}.
 * 
 * @author matthes rieke
 *
 */
public class SubstitutionGroupTest extends TestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(SubstitutionGroupTest.class);
	private static XmlOptions options;
	
	static {
		options = new XmlOptions();
		options.setSavePrettyPrint();
	}

	public void testUnresolvedSubstitution() {
		FeatureCollectionDocument collectionDoc = FeatureCollectionDocument.Factory.newInstance();
		AbstractFeatureCollectionType collection = collectionDoc.addNewFeatureCollection();

		addExtensionMemberWithoutQualifying(collection);
		
		//results in invalid xml markup
		logger.info(collectionDoc.xmlText(options));
		
		addExtensionMemberWithQualifying(collection);
		
		//results in  valid xml markup
		logger.info(collectionDoc.xmlText(options));
	}

	private void addExtensionMemberWithoutQualifying(
			AbstractFeatureCollectionType collection) {
		SamplingFeatureType sampling = SamplingFeatureType.Factory.newInstance();
		sampling.addNewDescription().setStringValue("testDescription");

		FeaturePropertyType member = collection.addNewFeatureMember();
		member.setFeature(sampling);
		
		assertTrue(member.getFeature() != null);
	}

	private void addExtensionMemberWithQualifying(AbstractFeatureCollectionType collection) {
		SamplingFeatureType sampling = SamplingFeatureType.Factory.newInstance();
		sampling.addNewDescription().setStringValue("testDescription");

		FeaturePropertyType member = collection.addNewFeatureMember();
		AbstractFeatureType feature = member.addNewFeature();
		feature.set(sampling);
		
		// result is the same - member.getFeature() returns null
//		member.setFeature(sampling);
		
		XMLBeansTools.qualifySubstitutionGroup(member.getFeature(), SamplingFeatureDocument.type.getDocumentElementName(), SamplingFeatureType.type);
		
		assertTrue(member.getFeature() == null);
	}

	public void testBuiltInSubstitution() {
		FeatureCollectionDocument collectionDoc = FeatureCollectionDocument.Factory.newInstance();
		AbstractFeatureCollectionType collection = collectionDoc.addNewFeatureCollection();

		addBuiltInMember(collection);
		
		logger.info(collectionDoc.xmlText(options));
	}

	private void addBuiltInMember(AbstractFeatureCollectionType collection) {
		GridCoverageType gridCoverage = GridCoverageType.Factory.newInstance();
		gridCoverage.addNewDescription().setStringValue("testCoverage");

		FeaturePropertyType member = collection.addNewFeatureMember();
		member.setFeature(gridCoverage);
		
		XMLBeansTools.qualifySubstitutionGroup(member.getFeature(), GridCoverageDocument.type.getDocumentElementName(), GridCoverageType.type);
		
		assertTrue(member.getFeature() != null);
	}

}
