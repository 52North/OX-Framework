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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.opengis.gml.AbstractFeatureCollectionType;
import net.opengis.gml.AbstractFeatureType;
import net.opengis.gml.FeatureCollectionDocument;
import net.opengis.gml.FeaturePropertyDocument;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.GridCoverageDocument;
import net.opengis.gml.GridCoverageType;
import net.opengis.sampling.x10.SamplingFeatureDocument;
import net.opengis.sampling.x10.SamplingFeatureType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test case for substitution group and the qualification of those using
 * {@link XmlUtil#qualifySubstitutionGroup(org.apache.xmlbeans.XmlObject, javax.xml.namespace.QName, org.apache.xmlbeans.SchemaType)}
 * .
 * 
 * @author matthes rieke
 * 
 */
public class SubstitutionGroupTest {

    private static final Logger logger = LoggerFactory.getLogger(SubstitutionGroupTest.class);
    private static XmlOptions options;
    private FeatureCollectionDocument collectionDoc;
    private AbstractFeatureCollectionType collection;

    static {
        options = new XmlOptions();
        options.setSavePrettyPrint();
    }

    @Before
    public void setUp() throws Exception {
        collectionDoc = FeatureCollectionDocument.Factory.newInstance();
        collection = collectionDoc.addNewFeatureCollection();
    }

    @Test
    public void testAddingExtensionMemberWithoutQualifying() {
        logger.info("NO QUALIFYING");
        SamplingFeatureType sampling = SamplingFeatureType.Factory.newInstance();
        sampling.addNewDescription().setStringValue("testDescription");

        FeaturePropertyType member = collection.addNewFeatureMember();
        member.setFeature(sampling);
        
        // results in invalid xml markup
        logger.info("\nINVALID collection: \n \n{}\n", collectionDoc.xmlText(options));

        assertTrue(member.getFeature() != null);
    }

    @Test
    public void testAddingExtensionMemberViaXmlBeans() {
        try {
            logger.info("USE BUILT-IN XMLBEANS SUBSTITUTION");
            SamplingFeatureDocument samplingDoc = SamplingFeatureDocument.Factory.newInstance();
            SamplingFeatureType sampling = samplingDoc.addNewSamplingFeature();
            sampling.addNewDescription().setStringValue("testDescription");

            
            FeaturePropertyDocument featureMemberDoc = FeaturePropertyDocument.Factory.newInstance();
            FeaturePropertyType member = featureMemberDoc.addNewFeatureProperty();
//            FeaturePropertyType member = collection.addNewFeatureMember();
            member.setFeature(sampling);
            
            collection.addNewFeatureMember().set(featureMemberDoc);
            
            SchemaType type = SamplingFeatureDocument.type;
            XmlObject substitute = member.getFeature().substitute(type.getDocumentElementName(), sampling.schemaType());
            logger.info("substituted element: {}", substitute.xmlText(options));
            
            // results in invalid xml markup
            logger.info("\nINVALID collection: \n \n{}\n", collectionDoc.xmlText(options));

            assertTrue(member.getFeature() != null);
        } catch (Exception e) {
            logger.error("Could not substitute by XmlBeans' built-in substitution means", e);
            fail();
        }
    }
    
    @Test
    public void testAddingExtensionMemberWithQualifyingFromXmlBeansTools() {
        logger.info("SUBSTITUTION VIA XmlUtil");
        SamplingFeatureType sampling = SamplingFeatureType.Factory.newInstance();
        sampling.addNewDescription().setStringValue("testDescription");

        FeaturePropertyType member = collection.addNewFeatureMember();
        AbstractFeatureType feature = member.addNewFeature();
        feature.set(sampling);

        // result is the same - member.getFeature() returns null
        // member.setFeature(sampling);

        XmlUtil.qualifySubstitutionGroup(member.getFeature(),
                                               SamplingFeatureDocument.type.getDocumentElementName(),
                                               SamplingFeatureType.type);

        // results in valid xml markup
        logger.info("\nVALID collection: \n \n{}\n", collectionDoc.xmlText(options));
        assertTrue(member.getFeature() == null);
    }

//    @Test
    public void testBuiltInSubstitution() {
        logger.info("BUILT-IN MEMBER SUBSTITUTION");
        FeatureCollectionDocument collectionDoc = FeatureCollectionDocument.Factory.newInstance();
        AbstractFeatureCollectionType collection = collectionDoc.addNewFeatureCollection();

        addBuiltInMember(collection);

        logger.info("\nVALID collection: \n \n{}\n", collectionDoc.xmlText(options));
    }

    private void addBuiltInMember(AbstractFeatureCollectionType collection) {
        GridCoverageType gridCoverage = GridCoverageType.Factory.newInstance();
        gridCoverage.addNewDescription().setStringValue("testCoverage");

        FeaturePropertyType member = collection.addNewFeatureMember();
        member.setFeature(gridCoverage);

        XmlUtil.qualifySubstitutionGroup(member.getFeature(),
                                               GridCoverageDocument.type.getDocumentElementName(),
                                               GridCoverageType.type);

        assertTrue(member.getFeature() != null);
    }

}
