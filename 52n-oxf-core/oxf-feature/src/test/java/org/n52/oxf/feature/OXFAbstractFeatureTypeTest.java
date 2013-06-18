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
                System.out.println(metadata);
            }
        }
        catch (OXFException e) {
            fail("Could not create FeatureCollection from document: " + observationCollectionDoc);
        }
    }
}
