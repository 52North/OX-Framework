
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
