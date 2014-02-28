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
