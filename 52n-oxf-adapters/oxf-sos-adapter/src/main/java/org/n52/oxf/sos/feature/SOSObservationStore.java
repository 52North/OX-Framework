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

package org.n52.oxf.sos.feature;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.om.x10.ObservationCollectionType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.sos.x20.GetObservationResponseType;
import net.opengis.sos.x20.GetObservationResponseType.ObservationData;
import net.opengis.waterml.x20.DefaultTVPMeasurementMetadataDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesType;
import net.opengis.waterml.x20.TVPDefaultMetadataPropertyType;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.feature.GenericObservationParser;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFObservationCollectionType;
import org.n52.oxf.feature.OperationResultStore;
import org.n52.oxf.sos.util.SosUtil;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSObservationStore extends OperationResultStore implements IFeatureStore {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSObservationStore.class);

    @Deprecated
    public SOSObservationStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }
    
    public SOSObservationStore(OperationResult operationResult) throws OXFException {
        super(operationResult);
    }
    
    public OXFFeatureCollection unmarshalFeatures(OperationResult operationResult) throws OXFException {
        this.version = getVersion(operationResult);
        if (SosUtil.isVersion100(version)) {
            return unmarshalFeatures100(operationResult);
        } else {
            return unmarshalFeatures();
        }
    }
    
    public OXFFeatureCollection unmarshalFeatures() throws OXFException {
        if (SosUtil.isVersion100(version)) {
            return unmarshalFeatures100();
        } else if (SosUtil.isVersion200(version)) {
            return unmarshalFeatures200();
        } else {
            LOGGER.error("Cannot unmarshal FeatureCollection.");
            throw new OXFException(String.format("SOS version '%s' is not supported!", version));
        }
    }

    /**
     * @deprecated use instead {@link #SOSObservationStore(OperationResult)} and {@link #unmarshalFeatures()}
     */
    @Deprecated
    protected OXFFeatureCollection unmarshalFeatures100(OperationResult operationResult) throws OXFException {
        try {
            this.xmlObject = XMLBeansParser.parse(operationResult.getIncomingResultAsStream());
            return unmarshalFeatures(operationResult);
        } catch (XMLHandlingException e) {
            throw new OXFException("Could not parse OperationResult.", e);
        }
    }
    
    private OXFFeatureCollection unmarshalFeatures100() throws OXFException {
        if (xmlObject == null) {
            // TODO remove when removing deprecated #unmarshalFeatures100()
            throw new IllegalStateException("Store was not initialized with an operationResult!");
        }
        try {
            if (isOM100ObservationCollectionDocument(xmlObject)) {
                ObservationCollectionDocument obsCollectionDoc = (ObservationCollectionDocument) xmlObject;
                ObservationCollectionType observation = obsCollectionDoc.getObservationCollection();
                return OXFObservationCollectionType.createFeatureCollection(observation.getId(), observation);
//            } else if (isWaterML200TimeSeriesObservationDocument(xmlObject)) {
//                OXFObservationCollectionType obsCollectionType = new OXFObservationCollectionType();
//                String noneSenseId = String.valueOf(System.currentTimeMillis()); // XXX no collection id available
//                OXFFeatureCollection featureCollection = new OXFFeatureCollection(noneSenseId, obsCollectionType);
//                return unmarshalTvpMeasurementObservations(featureCollection);
            } else {
                throw new OXFException("Unknown result type.");
            }
        } catch (Exception e) {
            throw new OXFException(e);
        }
    }

    private boolean isOM100ObservationCollectionDocument(XmlObject xmlObject) {
        return xmlObject.schemaType() == ObservationCollectionDocument.type;
    }
    
//    private OXFFeatureCollection unmarshalTvpMeasurementObservations(OXFFeatureCollection featureCollection) throws OXFException {
//        if (xmlObject == null) {
//            // TODO remove when removing deprecated #unmarshalFeatures100()
//            throw new IllegalStateException("Store was not initialized with an operationResult!");
//        }
//        if (!isWaterML200TimeSeriesObservationDocument(xmlObject)) {
//            throw new OXFException("Unknown result type.");
//        }
//        
//
//        MeasurementTimeseriesDocument measurementDocument = (MeasurementTimeseriesDocument) xmlObject;
//        MeasurementTimeseriesType timeseries = measurementDocument.getMeasurementTimeseries();
//        
//        // TODO init common metadata to collection
//        
//        // TODO initialize particular measurements
//        
//        /*
//         * private OXFFeatureCollection initializeFeature(OXFFeatureCollection featureCollection, ObservationData[] observationData) throws OXFException {
//        for (ObservationData observation : observationData) {
//            OMObservationType omObservation = observation.getOMObservation();
//            GenericObservationParser.addElementsFromGenericObservation(featureCollection, omObservation);
//        }
//        return featureCollection;
//    }
//         */
//        
//        try {
//            return OXFObservationCollectionType.createFeatureCollection(timeseries.getId(), timeseries);
//        } catch (OXFException e) {
//            LOGGER.warn("Could not initialize features. Returning an empty collection!");
//            return featureCollection;
//        }
//    }

    private OXFFeatureCollection unmarshalFeatures200() throws OXFException {
        if (xmlObject == null) {
            // TODO remove when removing deprecated #unmarshalFeatures100()
            throw new IllegalStateException("Store was not initialized with an operationResult!");
        }
        if (isSOS200GetObservationResponseDocument(xmlObject)) {
            OXFObservationCollectionType obsCollectionType = new OXFObservationCollectionType();
            GetObservationResponseDocument observationResponseDocument = (GetObservationResponseDocument) xmlObject;
            GetObservationResponseType observationResponseType = observationResponseDocument.getGetObservationResponse();
            String noneSenseId = String.valueOf(System.currentTimeMillis()); // XXX no collection id available
            OXFFeatureCollection featureCollection = new OXFFeatureCollection(noneSenseId, obsCollectionType);
            try {
                ObservationData[] observationData = observationResponseType.getObservationDataArray();
                return initializeFeature(featureCollection, observationData);
            } catch (OXFException e) {
                LOGGER.warn("Could not initialize features. Returning an empty collection!", e);
                return featureCollection;
            }
        } else {
            throw new OXFException("Unknown result type.");
        }
    }

    private boolean isSOS200GetObservationResponseDocument(XmlObject xmlObject) {
        return xmlObject.schemaType() == GetObservationResponseDocument.type;
    }

    private OXFFeatureCollection initializeFeature(OXFFeatureCollection featureCollection, ObservationData[] observationData) throws OXFException {
        for (ObservationData observation : observationData) {
            OMObservationType omObservation = observation.getOMObservation();
            GenericObservationParser.addElementsFromGenericObservation(featureCollection, omObservation);
        }
        return featureCollection;
    }

}