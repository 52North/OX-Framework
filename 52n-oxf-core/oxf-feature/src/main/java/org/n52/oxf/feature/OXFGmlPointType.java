/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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

import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.PointType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class OXFGmlPointType extends OXFAbstractFeatureType {

    public static final String POSITION = "position";
    
    public OXFGmlPointType() {
        typeName = "OXFGmlPointType";
    }
    
    public static OXFFeature create(PointType gmlPoint) {
        String id = gmlPoint.isSetIdentifier() ? gmlPoint.getIdentifier().getStringValue() : "NotSet";
        OXFGmlPointType gmlPointType = new OXFGmlPointType();
        OXFFeature feature = new OXFFeature(id, gmlPointType);
        gmlPointType.initializeFeature(feature, gmlPoint);
        return feature;
    }
    
    public void initializeFeature(OXFFeature feature, PointType gmlPoint) {
        
        // create the DESCRIPTION-attribute:
        if (gmlPoint.isSetDescription()) {
            String description = gmlPoint.getDescription().getStringValue();
            feature.setAttribute(DESCRIPTION, description);
        }
        
        // create the NAME-attribute:
        if (hasAtLeastOneName(gmlPoint)) {
            String name = gmlPoint.getNameArray(0).getStringValue();
            feature.setAttribute(NAME, new String[] {name});
        }
        
        // create the POSITION-attribute:
        if (gmlPoint.isSetPos()) {
            DirectPositionType directPosition = gmlPoint.getPos();
            List<?> coordsList = directPosition.getListValue();
            double x = (Double) coordsList.get(0);
            double y = (Double) coordsList.get(1);
            double z = Double.NaN;
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

    private boolean hasAtLeastOneName(PointType gmlPoint) {
        return hasSetNames(gmlPoint) && gmlPoint.getNameArray(0) != null;
    }

    private boolean hasSetNames(PointType gmlPoint) {
        CodeType[] nameArray = gmlPoint.getNameArray();
        return nameArray != null && nameArray.length > 0;
    }
}
