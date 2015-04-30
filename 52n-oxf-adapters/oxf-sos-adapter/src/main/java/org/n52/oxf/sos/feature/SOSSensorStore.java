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
package org.n52.oxf.sos.feature;

import net.opengis.sensorML.x101.AbstractProcessType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML.Member;
import net.opengis.sensorML.x101.SystemType;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OperationResultStore;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unmarshalls a collection of OXFSensorTypes from an OperationResult, typically a
 * SensorMLDocument containing a collection of SensorML SystemTypes.
 */
public class SOSSensorStore extends OperationResultStore implements IFeatureStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSSensorStore.class);

    public static final String SENSOR_ML_FEATURE_ID = "sml_id";
    public static final String SENSOR_ML_FEATURE_COLLECTION_ID = "sml_id";

    @Deprecated
    public SOSSensorStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }

    public SOSSensorStore(OperationResult operationResult) throws OXFException {
        super(operationResult);
    }

    public OXFFeatureCollection unmarshalFeatures() throws OXFException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Unmarshalling features from:\n" + xmlObject.xmlText());
        }

        if (isSensorML101Document(xmlObject)) {
            OXFFeatureCollection oxf_featureCollection = createFeatureCollection();
            SensorMLDocument sensorMLDoc = (SensorMLDocument) xmlObject;
            Member[] sensorMLMembers = sensorMLDoc.getSensorML().getMemberArray();
            wrapMembersToFeatureCollection(sensorMLMembers, oxf_featureCollection);
            return oxf_featureCollection;
        } else {
            throw new OXFException("Unknown OperationResult (" + xmlObject.schemaType() + ").");
        }
    }

    private void wrapMembersToFeatureCollection(Member[] members, OXFFeatureCollection oxf_featureCollection) throws OXFException {
        for (Member member : members) {
            AbstractProcessType xb_proc = member.getProcess();
            if (xb_proc instanceof SystemType) {
                SystemType xb_system = (SystemType) xb_proc;
                OXFFeature sensorFeature = OXFSensorType.create(xb_system);
                oxf_featureCollection.add(sensorFeature);
            }
        }
    }

    private boolean isSensorML101Document(XmlObject xmlObject) {
        return xmlObject instanceof SensorMLDocument;
    }

    private OXFFeatureCollection createFeatureCollection() {
        return new OXFFeatureCollection(SENSOR_ML_FEATURE_COLLECTION_ID, new OXFSensorType());
    }

    /**
     * @deprecated Use {@link SOSSensorStore#SOSSensorStore(OperationResult)} with {@link SOSSensorStore#unmarshalFeatures(OperationResult)}
     */
    public OXFFeatureCollection unmarshalFeatures(OperationResult operationResult) throws OXFException {
        try {
            this.xmlObject = XMLBeansParser.parse(operationResult.getIncomingResultAsAutoCloseStream());
            this.version = getVersion(operationResult);
        } catch (XMLHandlingException e) {
            throw new OXFException("Could not parse OperationResult", e);
        }
        return unmarshalFeatures();
    }


    public OXFFeatureCollection unmarshalFeaturesFromHistory(OperationResult opResult) throws OXFException {
        // TODO Auto-generated method stub
        return unmarshalFeatures(opResult);
    }

}
