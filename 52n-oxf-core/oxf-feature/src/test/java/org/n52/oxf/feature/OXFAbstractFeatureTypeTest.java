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
package org.n52.oxf.feature;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.fail;
import static org.n52.oxf.feature.OXFAbstractFeatureType.METADATA_PROPERTY_TYPES;
import static org.n52.oxf.feature.OXFObservationCollectionType.createFeatureCollection;
import static org.n52.oxf.xmlbeans.tools.XmlFileLoader.loadXmlFileViaClassloader;

import java.io.IOException;
import java.util.UUID;

import net.opengis.gml.MetaDataPropertyType;
import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationCollectionType;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.SimpleDataRecordType;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

public class OXFAbstractFeatureTypeTest {

    private static final String OBSERVATION_COLLECTION_WITH_GML_METADATA_PROPERTY_ARRAY = "/files/observationData/SOS_1.0.0_ObservationCollection_with_gmlMetadataPropertyArray.xml";
    
    private ObservationCollectionDocument observationCollectionDoc;

    @Before
    public void
            setUp()
    {
        String fileToLoad = OBSERVATION_COLLECTION_WITH_GML_METADATA_PROPERTY_ARRAY;
        try {
            observationCollectionDoc = (ObservationCollectionDocument) loadXmlFileViaClassloader(fileToLoad, getClass());
        }
        catch (XmlException e) {
            fail("Could not parse file: " + fileToLoad);
        }
        catch (IOException e) {
            fail("Could not read file: " + fileToLoad);
        }
    }
    
    @Test public void
    shouldExpectFileAsValid()
    {
        ObservationCollectionType observationCollection = observationCollectionDoc.getObservationCollection();
        try {
            OXFFeatureCollection featureCollection = createFeatureCollection(randomUUID().toString(), observationCollection);
            String[] metadatas = (String[]) featureCollection.getAttribute(METADATA_PROPERTY_TYPES);
            for (String metadata : metadatas) {
                DataArrayDocument dataArray = DataArrayDocument.Factory.parse(metadata);
                DataComponentPropertyType elementType = dataArray.getDataArray1().getElementType();
                if (elementType.getName().equalsIgnoreCase("components")) {
                    SimpleDataRecordType abstractDataRecord = (SimpleDataRecordType) elementType.getAbstractDataRecord();
                    for (AnyScalarPropertyType scalarType : abstractDataRecord.getFieldArray()) {
                        System.out.println(scalarType.getName());
                    }
                }
            }
        }
        catch (OXFException e) {
            fail("Could not create FeatureCollection from document: " + observationCollectionDoc);
        }
        catch (XmlException e) {
            fail("Could not parse DataArray from MetadataProperties: " + observationCollectionDoc);
        }
    }
}
