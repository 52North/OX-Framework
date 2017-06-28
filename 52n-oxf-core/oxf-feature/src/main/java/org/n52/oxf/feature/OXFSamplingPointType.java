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

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.PointType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFSamplingPointType extends OXFAbstractFeatureType {

    public static final String POSITION = "position";

    /**
     *
     */
    public OXFSamplingPointType() {
        super();

        typeName = "OXFSamplingPointType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }


    public static OXFFeature create(SamplingPointDocument xb_saPointDoc) {

        SamplingPointType xb_samplingPoint = xb_saPointDoc.getSamplingPoint();

        String id = xb_samplingPoint.getId();

        // FeatureType of the feature:
        OXFSamplingPointType oxf_saPointType = new OXFSamplingPointType();

        OXFFeature feature = new OXFFeature(id, oxf_saPointType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_saPointType.initializeFeature(feature, xb_samplingPoint);

        return feature;
    }

    /**
     *
     * @param doc
     * @return
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor position = new OXFFeatureAttributeDescriptor(POSITION,
                                                                                   DataType.OBJECT,
                                                                                   Point.class,
                                                                                   1,
                                                                                   1,
                                                                                   "");
        attributeDescriptors.add(position);

        // TODO support for other attributes of SamplingPoint (e.g. sampledFeature)

        return attributeDescriptors;
    }

    /**
     * supports the Sampling specification of version 1.0:
     */
    public void initializeFeature(OXFFeature feature,
                                  SamplingPointType xb_saPoint) {
        super.initializeFeature(feature, xb_saPoint);

        //
        // --- initialize the POSITION-attribute:

        if (xb_saPoint.getPosition() != null && xb_saPoint.getPosition().getPoint() != null
                && ((PointType)xb_saPoint.getPosition().getPoint()).getPos() != null) {

            PointType xb_point = xb_saPoint.getPosition().getPoint();

            DirectPositionType xb_pos = xb_point.getPos();

            List coordsList = xb_pos.getListValue();

            double x = (Double) coordsList.get(0);
            double y = (Double) coordsList.get(1);
            double z = Double.NaN;
            try {
            		z = (Double) coordsList.get(2);
			} catch (Exception e) {
				// no Exception -> 2D Point
			}


            if (coordsList.size() > 2) {
                z = (Double) coordsList.get(2);
            }

            Point point = new GeometryFactory().createPoint(new Coordinate(x, y, z));

            feature.setAttribute(POSITION, point);
            initializeFeaturesGeometry(feature, point);
        }

        // check if the geometry-attribute is set: (could be set in this or the super class)
        if (feature.getGeometry() == null) {
            throw new IllegalArgumentException("The geometry attribute could not be initialized.");
        }

    }

}
