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

import java.util.List;

import net.opengis.om.x10.ObservationCollectionType;
import net.opengis.om.x10.ObservationPropertyType;
import net.opengis.waterml.x20.MeasurementTimeseriesType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFObservationCollectionType extends OXFAbstractFeatureType {

    public OXFObservationCollectionType() {
        super();
        typeName = "OXFObservationCollectionType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    @Override
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {
        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        /*
         * the "member" attribute is realized through the features-attribute in the class
         * <code>OXFFeatureCollection<code><br>
         * <br>
         * so use the <code>add</code>-methods in this class to add members to the FeatureCollection.
         */

        return attributeDescriptors;
    }

    public static OXFFeatureCollection createFeatureCollection(String id, ObservationCollectionType obsCollection) throws OXFException {
    	OXFObservationCollectionType type = new OXFObservationCollectionType();
    	OXFFeatureCollection featureCollection = new OXFFeatureCollection(id, type);
		type.initializeFeature(featureCollection, obsCollection);
		return featureCollection;
    }

    /**
     * supports O&M 1.0
     */
    private void initializeFeature(OXFFeatureCollection featureCollection, ObservationCollectionType observationCollection) throws OXFException {

        super.initializeFeature(featureCollection, observationCollection);
        ObservationPropertyType[] memberArray = observationCollection.getMemberArray();
        for (int i = 0; i < memberArray.length; i++) {
            ObservationPropertyType member = memberArray[i];
            XmlCursor cursor = member.newCursor();
            cursor.toFirstChild();
            XmlObject xb_memberDocument = null;
            try {
                xb_memberDocument = XmlObject.Factory.parse(cursor.getObject().getDomNode());
                Node memberNode = xb_memberDocument.getDomNode().getFirstChild();
                if (memberNode.hasChildNodes()) {
                    addMember(featureCollection, xb_memberDocument);
                }
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }
    }

    /**
     * supports WaterML 2.0.0
     * @throws OXFException
     */
    private static OXFFeatureCollection createFeatureCollection(String id, MeasurementTimeseriesType timeseriesObservation) throws OXFException {
    	OXFObservationCollectionType type = new OXFObservationCollectionType();
    	OXFFeatureCollection featureCollection = new OXFFeatureCollection(id, type);
		type.addMember(featureCollection, timeseriesObservation);
		return featureCollection;
	}

    private void addMember(OXFFeatureCollection featureCollection, XmlObject xb_memberDocument) throws OXFException {

        // this feature shall be initialized and added to collection:
        OXFFeature feature = null;

        // TODO Spec-Too-Flexible-Problem --> various Observation-Types are possible:
        // TODO create factory method to get FeatureInitializer via XmlObject.schemaType()

        //
        // parse O&M 1.0.0:
        //
        if (xb_memberDocument instanceof net.opengis.om.x10.CategoryObservationDocument) {
            net.opengis.om.x10.CategoryObservationDocument xb_observationDoc = (net.opengis.om.x10.CategoryObservationDocument) xb_memberDocument;

            net.opengis.om.x10.CategoryObservationType xb_observation = xb_observationDoc.getCategoryObservation();

            // FeatureType of the feature:
            OXFCategoryObservationType oxf_categoryObservationType = new OXFCategoryObservationType();

            feature = new OXFFeature(xb_observation.getId(), oxf_categoryObservationType);

            // initialize the feature with the attributes from the XMLBean:
            oxf_categoryObservationType.initializeFeature(feature, xb_observation);
        }

        else if (xb_memberDocument instanceof net.opengis.om.x10.MeasurementDocument) {
            net.opengis.om.x10.MeasurementDocument xb_observationDoc = (net.opengis.om.x10.MeasurementDocument) xb_memberDocument;

            net.opengis.om.x10.MeasurementType xb_observation = xb_observationDoc.getMeasurement();

            // FeatureType of the feature:
            OXFMeasurementType oxf_measurementType = new OXFMeasurementType();

            feature = new OXFFeature(xb_observation.getId(), oxf_measurementType);

            // initialize the feature with the attributes from the XMLBean:
            oxf_measurementType.initializeFeature(feature, xb_observation);
        }

        else if (xb_memberDocument instanceof net.opengis.om.x10.ObservationDocument) {
            net.opengis.om.x10.ObservationDocument xb_observationDoc = (net.opengis.om.x10.ObservationDocument) xb_memberDocument;
            net.opengis.om.x10.ObservationType xb_genericObs = xb_observationDoc.getObservation();
            GenericObservationParser.addElementsFromGenericObservation(featureCollection, xb_genericObs);
        }

        else {
            throw new IllegalArgumentException("The FeatureType '" + xb_memberDocument.schemaType()
                    + "' of the ObservationCollections member element is not supported.");
        }

        //
        // add the observation (feature) if it is not null:
        //
        if (feature != null) {
            featureCollection.add(feature);
        }
    }


}
