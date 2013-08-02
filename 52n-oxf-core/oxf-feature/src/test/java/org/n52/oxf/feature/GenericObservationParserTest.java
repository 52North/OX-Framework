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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;

import net.opengis.om.x20.OMObservationDocument;
import net.opengis.om.x20.OMObservationType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.xmlbeans.tools.SoapUtil;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

import com.vividsolutions.jts.geom.Geometry;

public class GenericObservationParserTest {
    
    private static final QName OM_OBSERVATION_QNAME = new QName("http://www.opengis.net/om/2.0", "OMObservation");
    
    private static final QName SOS_OBSERVATION_DATA_QNAME = new QName("http://www.opengis.net/sos/2.0", "observationData");
    
    private static final String SOS_200_GETOBSERVATION_WML_20_KISTERS = "/files/observationData/SOS_2.0.0_GetObservationResponse_wml_2.0_Kisters.xml";
    
    private static final String SOS_200_GETOBSERVATION_INVALID_OM20_TIDEELBE = "/files/observationData/SOS_2.0.0_GetObservationResponse_invalid_om_2.0_tideelbe.xml";
    
    private static final String SOS_200_GETOBSERVATION_VALID_OM20_TIDEELBE = "/files/observationData/SOS_2.0.0_GetObservationResponse_valid_om_2.0_tideelbe.xml";
    
    private static final String SOS_200_GETOBSERVATION_VALID_WML20_GEOWOW = "/files/observationData/SOS_2.0.0_GetObservationResponse_valid_wml_2.0_geowow.xml";
    
    @Test public void 
    shouldParseHydroWml20ResultIntoFeatureCollection() 
    throws Exception {
        OMObservationDocument omObservation = parseObservationDataFrom(SOS_200_GETOBSERVATION_WML_20_KISTERS);
        OXFFeatureCollection featureCollection = createFeatureCollectionFrom(omObservation);
        
        // TODO check featureCollection
//        Geometry boundingBox = featureCollection.getBoundingBox();
    }

    @Test public void
    shouldParseInvalidTideelbeResult()
    throws Exception 
    {
        OMObservationDocument omObservation = parseObservationDataFrom(SOS_200_GETOBSERVATION_INVALID_OM20_TIDEELBE);
        OXFFeatureCollection featureCollection = createFeatureCollectionFrom(omObservation);
        Geometry geometry = featureCollection.getGeometry();
        
    }

    @Test public void
    shouldParseValidTideelbeResult()
    throws Exception 
    {
        OMObservationDocument omObservation = parseObservationDataFrom(SOS_200_GETOBSERVATION_VALID_OM20_TIDEELBE);
        OXFFeatureCollection featureCollection = createFeatureCollectionFrom(omObservation);
        Geometry geometry = featureCollection.getGeometry();
        
    }

    @Test public void
    shouldParseValidGeowowResult()
    throws Exception 
    {
        OMObservationDocument omObservation = parseObservationDataFrom(SOS_200_GETOBSERVATION_VALID_WML20_GEOWOW);
        OXFFeatureCollection featureCollection = createFeatureCollectionFrom(omObservation);
        Geometry geometry = featureCollection.getGeometry();
    }
    
    private OMObservationDocument parseObservationDataFrom(String resource) throws XmlException, IOException {
        InputStream is = getClass().getResourceAsStream(resource);
        XmlObject xmlObject = XmlObject.Factory.parse(is);
        xmlObject = SoapUtil.stripSoapEnvelope(xmlObject);
        XmlCursor cursor = xmlObject.newCursor();
        cursor.toFirstChild();
        cursor.toChild(SOS_OBSERVATION_DATA_QNAME);
        cursor.toChild(OM_OBSERVATION_QNAME);
        OMObservationDocument omObservation = OMObservationDocument.Factory.parse(cursor.getObject().newInputStream());
        assertTrue(omObservation.schemaType() == OMObservationDocument.type);
        return omObservation;
    }

    private OXFFeatureCollection createFeatureCollectionFrom(OMObservationDocument omObservation) throws OXFException {
        OXFObservationCollectionType obsCollectionType = new OXFObservationCollectionType();
        String noneSenseId = String.valueOf(System.currentTimeMillis()); 
        OXFFeatureCollection featureCollection = new OXFFeatureCollection(noneSenseId, obsCollectionType);
        OMObservationType observation = omObservation.getOMObservation();
        GenericObservationParser.addElementsFromGenericObservation(featureCollection, observation);
        return featureCollection;
    }

}
