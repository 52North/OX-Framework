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

package org.n52.oxf.feature.sos;

import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.gml.AbstractFeatureCollectionType;
import net.opengis.gml.FeatureCollectionDocument2;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.x32.PointType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingSurfaceDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.samplingSpatial.x20.ShapeType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFAbstractFeatureCollectionType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureAttributeDescriptor;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFFeatureType;
import org.n52.oxf.feature.OXFGmlPointType;
import org.n52.oxf.feature.OXFGrdcSamplingPointType;
import org.n52.oxf.feature.OXFSamplingPointType;
import org.n52.oxf.feature.OXFSamplingSurfaceType;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.grdc.sampling.x10.GrdcSamplingPointDocument;

/**
 * Unmarshals features of interest received by SOS GetFeatureOfInterest operation.
 */
public class SOSFoiStore extends OperationResultStore implements IFeatureStore {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSFoiStore.class);
    
    @Deprecated
    public SOSFoiStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }
    
    public SOSFoiStore(OperationResult operationResult) throws OXFException {
        super(operationResult);
    }

    public OXFFeatureCollection unmarshalFeatures() throws OXFException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Unmarshalling features from: {}\n", xmlObject.xmlText());
        }
        
        if (isGmlFeatureCollectionDocument(xmlObject)) {
            FeatureCollectionDocument2 xb_featureCollDoc = (FeatureCollectionDocument2) xmlObject;
            return unmarshalAbstractCollectionType(xb_featureCollDoc.getFeatureCollection());
        } else if (isSamplingPointDocument(xmlObject)) {
            SamplingPointDocument xb_samplingPointDoc = (SamplingPointDocument) xmlObject;
            OXFFeature feature = OXFSamplingPointType.create(xb_samplingPointDoc);
            OXFFeatureCollection coll = new OXFFeatureCollection("any_ID", null);
            coll.add(feature);
            return coll;
        }
        
        // FIXME IMPLEMENT
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    private boolean isGmlFeatureCollectionDocument(XmlObject xmlObject) {
        return xmlObject instanceof FeatureCollectionDocument2;
    }

    private boolean isSamplingPointDocument(XmlObject xmlObject) {
        return xmlObject instanceof SamplingPointDocument;
    }

    @Deprecated
    public OXFFeatureCollection unmarshalFeatures(OperationResult opsRes) throws OXFException {

        // 1. try to parse the feature data as a FeatureCollection:
        try {
            FeatureCollectionDocument2 xb_featureCollDoc = FeatureCollectionDocument2.Factory.parse(opsRes.getIncomingResultAsStream());
            AbstractFeatureCollectionType xb_collection = xb_featureCollDoc.getFeatureCollection();
            return unmarshalAbstractCollectionType(xb_collection);
        }
        
        // 2. try to parse the feature data as one single SamplingPoint:
        catch (org.apache.xmlbeans.XmlException xmlException) {
            try {
                SamplingPointDocument xb_samplingPointDoc = SamplingPointDocument.Factory.parse(opsRes.getIncomingResultAsStream());
                OXFFeature feature = OXFSamplingPointType.create(xb_samplingPointDoc);
                OXFFeatureCollection coll = new OXFFeatureCollection("any_ID", null);
                coll.add(feature);
                return coll;
            }
            catch (Exception e) {
                throw new OXFException(e);
            }
        }
        catch (IOException ioException) {
            throw new OXFException(ioException);
        }
    }

    private OXFFeatureCollection unmarshalAbstractCollectionType(AbstractFeatureCollectionType xb_featureColl) throws OXFException {

        OXFAbstractFeatureCollectionType oxf_abstFeatureCollType = new OXFAbstractFeatureCollectionType();

        // create empty OXFFeatureCollection-object:
        OXFFeatureCollection oxf_featureCollection = new OXFFeatureCollection("any_ID", oxf_abstFeatureCollType);

        // initialize the OXFFeatureCollection-object:
        oxf_abstFeatureCollType.initializeFeature(oxf_featureCollection, xb_featureColl);

        return oxf_featureCollection;
    }

    /**
     * Parses a single feature entity to an OXFFeature object.
     * The method supports the Sampling Specification of version 0.0 and 1.0.
     */
    public OXFFeature parseFoi(FeaturePropertyType xb_featureMember) throws OXFException {
        OXFFeature feature = null;

        XmlCursor c = xb_featureMember.newCursor();
        
        //
        // parse: Sampling 1.0.0
        //
        
        // if feature is a SamplingPoint:
        if (c.toChild(new QName("http://www.opengis.net/sampling/1.0", "SamplingPoint"))){
            try {
                SamplingPointDocument xb_saPointDoc = SamplingPointDocument.Factory.parse(c.getDomNode());

                feature = OXFSamplingPointType.create(xb_saPointDoc);

                return feature;
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }
        
        // if feature is a SamplingSurface:
        else if (c.toChild(new QName("http://www.opengis.net/sampling/1.0", "SamplingSurface"))){
            try {
                SamplingSurfaceDocument xb_saSurfaceDoc = SamplingSurfaceDocument.Factory.parse(c.getDomNode());

                feature = OXFSamplingSurfaceType.create(xb_saSurfaceDoc);

                return feature;
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }
        
        // if feature is a GrdcSamplingPoint:
        else if (c.toChild(new QName("http://www.grdc.de/sampling/1.0","GrdcSamplingPoint"))) {
			try {
				GrdcSamplingPointDocument xb_grdcSaPoDoc = GrdcSamplingPointDocument.Factory.parse(c.getDomNode());
				
				feature = OXFGrdcSamplingPointType.create(xb_grdcSaPoDoc);
				
				return feature;
			} catch (Exception e) {
				throw new OXFException(e);
			}
		}
        
        
        // if feature is not known:
        else {
            String featureID = xb_featureMember.getHref();

            // TODO: not nice --> feature has no FeatureType
            feature = new OXFFeature(featureID, null);

            return feature;
        }
    }

    
    public OXFFeature parseFoi(SFSpatialSamplingFeatureType samplingFeatureType) {
        ShapeType shapeType = samplingFeatureType.getShape();
        XmlCursor c = samplingFeatureType.newCursor();
        
        //
        // parse: Sampling 2.0.0
        //
        
        boolean hasGmlPoint32 = c.toChild(new QName("http://www.opengis.net/gml/3.2", "Point"));
        if (hasGmlPoint32){
            PointType gmlPoint = (PointType) shapeType;
            return OXFGmlPointType.create(gmlPoint);
        } else {
            String featureId = samplingFeatureType.getIdentifier().getStringValue();
            String featureType = samplingFeatureType.getType().getHref();
            // XXX check if setting featureAttributeDescriptors is appropriate
            List<OXFFeatureAttributeDescriptor> featureAttributeDescriptors = new OXFSamplingPointType().getAttributeDescriptors();
            return new OXFFeature(featureId, new OXFFeatureType(featureType, featureAttributeDescriptors));
        }
    }

}