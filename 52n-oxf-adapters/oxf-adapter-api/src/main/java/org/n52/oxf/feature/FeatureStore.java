/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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

import java.io.IOException;

import net.opengis.gml.AbstractFeatureCollectionType;
import net.opengis.gml.FeatureCollectionDocument2;
import net.opengis.sampling.x10.SamplingPointDocument;
import org.apache.xmlbeans.XmlException;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unmarshals features of interest received by SOS GetFeatureOfInterest operation.
 */
public class FeatureStore extends OperationResultStore implements IFeatureStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureStore.class);

    @Deprecated
    public FeatureStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }

    public FeatureStore(OperationResult operationResult) throws OXFException {
        super(operationResult);
    }

    @Override
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
        throw new RuntimeException(String.format("Unmarshalling of '%s' is not supported", xmlObject.schemaType()));
    }

    private boolean isGmlFeatureCollectionDocument(XmlObject xmlObject) {
        return xmlObject instanceof FeatureCollectionDocument2;
    }

    private boolean isSamplingPointDocument(XmlObject xmlObject) {
        return xmlObject instanceof SamplingPointDocument;
    }

    @Deprecated
    @Override
    public OXFFeatureCollection unmarshalFeatures(OperationResult opsRes) throws OXFException {

        // 1. try to parse the feature data as a FeatureCollection:
        try {
            FeatureCollectionDocument2 xb_featureCollDoc = FeatureCollectionDocument2.Factory.parse(opsRes.getIncomingResultAsAutoCloseStream());
            AbstractFeatureCollectionType xb_collection = xb_featureCollDoc.getFeatureCollection();
            return unmarshalAbstractCollectionType(xb_collection);
        }

        // 2. try to parse the feature data as one single SamplingPoint:
        catch (org.apache.xmlbeans.XmlException xmlException) {
            try {
                SamplingPointDocument xb_samplingPointDoc = SamplingPointDocument.Factory.parse(opsRes.getIncomingResultAsAutoCloseStream());
                OXFFeature feature = OXFSamplingPointType.create(xb_samplingPointDoc);
                OXFFeatureCollection coll = new OXFFeatureCollection("any_ID", null);
                coll.add(feature);
                return coll;
            }
            catch (IOException | XmlException e) {
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
}