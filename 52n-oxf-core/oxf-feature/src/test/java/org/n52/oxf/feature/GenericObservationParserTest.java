package org.n52.oxf.feature;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.xml.namespace.QName;

import net.opengis.om.x20.OMObservationDocument;
import net.opengis.om.x20.OMObservationType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;

public class GenericObservationParserTest {
    
    private static final QName OM_OBSERVATION_QNAME = new QName("http://www.opengis.net/om/2.0", "OMObservation");
    
    private static final QName SOS_OBSERVATION_DATA_QNAME = new QName("http://www.opengis.net/sos/2.0", "observationData");
    
    private static final String SOS_200_GETOBSERVATION_WML_20_KISTERS = "/files/observationData/SOS_2.0.0_GetObservationResponse_wml_2.0_Kisters.xml";
    
    private XmlObject xmlObject;

    private OMObservationDocument omObservation;

    @Before
    public void setUp() throws Exception {
        InputStream is = getClass().getResourceAsStream(SOS_200_GETOBSERVATION_WML_20_KISTERS);
        xmlObject = XmlObject.Factory.parse(is);
        XmlCursor cursor = xmlObject.newCursor();
        cursor.toFirstChild();
        cursor.toChild(SOS_OBSERVATION_DATA_QNAME);
        cursor.toChild(OM_OBSERVATION_QNAME);
        omObservation = OMObservationDocument.Factory.parse(cursor.getObject().newInputStream());
        assertTrue(omObservation.schemaType() == OMObservationDocument.type);
    }

    @Test
    public void test() throws Exception {
        OXFObservationCollectionType obsCollectionType = new OXFObservationCollectionType();
        String noneSenseId = String.valueOf(System.currentTimeMillis()); 
        OXFFeatureCollection featureCollection = new OXFFeatureCollection(noneSenseId, obsCollectionType);
        OMObservationType observation = omObservation.getOMObservation();
        GenericObservationParser.addElementsFromGenericObservation(featureCollection, observation);
    }

}
