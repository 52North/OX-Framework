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

import java.util.ArrayList;
import java.util.List;

import net.opengis.gml.AbstractFeatureType;
import net.opengis.gml.CoordinatesType;
import net.opengis.gml.MetaDataPropertyType;
import net.opengis.gml.PointType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class OXFAbstractFeatureType extends OXFFeatureType {

    public static final String METADATA_PROPERTY_TYPES = "metadataProperties";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";
    public static final String NAME = "name";

    public OXFAbstractFeatureType() {
        super("OXFAbstractFeatureType", null);
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = new ArrayList<OXFFeatureAttributeDescriptor>();

        OXFFeatureAttributeDescriptor metadataProperties = new OXFFeatureAttributeDescriptor(METADATA_PROPERTY_TYPES,
                                                                                             DataType.STRING,
                                                                                             String[].class,
                                                                                             0,
                                                                                             Integer.MAX_VALUE,
                                                                                             "Contains or refers to a metadata package that contains metadata properties.");

        attributeDescriptors.add(metadataProperties);
        
        OXFFeatureAttributeDescriptor description = new OXFFeatureAttributeDescriptor(DESCRIPTION,
                                                                                      DataType.STRING,
                                                                                      String.class,
                                                                                      0,
                                                                                      1,
                                                                                      "");
        attributeDescriptors.add(description);

        OXFFeatureAttributeDescriptor name = new OXFFeatureAttributeDescriptor(NAME,
                                                                               DataType.STRING,
                                                                               String[].class,
                                                                               0,
                                                                               Integer.MAX_VALUE,
                                                                               "documentation ...");
        attributeDescriptors.add(name);

        OXFFeatureAttributeDescriptor location = new OXFFeatureAttributeDescriptor(LOCATION,
                                                                                   DataType.OBJECT,
                                                                                   Geometry.class,
                                                                                   0,
                                                                                   1,
                                                                                   "");
        attributeDescriptors.add(location);

        return attributeDescriptors;

    }

    /**
     * 
     * @param feature
     * @param geom
     */
    protected void initializeFeaturesGeometry(OXFFeature feature, Geometry geom) {
        feature.setGeometry(geom);
    }

    /**
     * 
     * @param xb_abstractFeature
     *        shall be an instance of <code>net.opengis.gml.AbstractFeatureType</code>, taken form a concrete
     *        <code>AbstractObservationDocument</code>.
     * @return
     */
    public void initializeFeature(OXFFeature feature, AbstractFeatureType xb_abstractFeature) {

        if (xb_abstractFeature.getMetaDataPropertyArray().length != 0) {
            MetaDataPropertyType[] metadatas = xb_abstractFeature.getMetaDataPropertyArray();
            String[] metadatasAsString = new String[metadatas.length];
            for (int i = 0; i < metadatas.length; i++) {
                metadatasAsString[i] = metadatas[i].xmlText();
            }
            feature.setAttribute(METADATA_PROPERTY_TYPES, metadatasAsString);
        }

        // create the DESCRIPTION-attribute:
        if (xb_abstractFeature.getDescription() != null) {
            String description = xb_abstractFeature.getDescription().getStringValue();
            feature.setAttribute(DESCRIPTION, description);
        }

        // create the NAME-attribute:
        if (xb_abstractFeature.getNameArray() != null
                && xb_abstractFeature.getNameArray().length > 0
                && xb_abstractFeature.getNameArray(0) != null) {
            String name = xb_abstractFeature.getNameArray(0).getStringValue();
            feature.setAttribute(NAME, new String[] {name});
        }

        // create the LOCATION-attribute:
        if (xb_abstractFeature.getLocation() != null
                && xb_abstractFeature.getLocation().getGeometry() != null) {

            // TODO: Spec-Too-Flexible-Problem --> various geometry types are possible:
            if (xb_abstractFeature.getLocation().getGeometry() instanceof PointType) {

                PointType xb_point = (PointType) xb_abstractFeature.getLocation().getGeometry();

                CoordinatesType xb_coords = xb_point.getCoordinates();
                String[] coordsArray = xb_coords.getStringValue().split(" ");

                double x = Double.parseDouble(coordsArray[0]);
                double y = Double.parseDouble(coordsArray[1]);
                double z = Double.NaN;

                if (coordsArray.length > 2) {
                    z = Double.parseDouble(coordsArray[2]);
                }

                Point point = new GeometryFactory().createPoint(new Coordinate(x, y, z));

                feature.setAttribute(LOCATION, point);
                initializeFeaturesGeometry(feature, point);
            }
            else {
                throw new IllegalArgumentException("The geometry type '"
                        + xb_abstractFeature.getLocation().getGeometry().getClass()
                        + "' is not supported.");
            }
        }
    }

    /**
     * 
     * @param feature
     * @param nameValue
     * @param descriptionValue
     * @param locationValue
     */
    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue) {
        if (nameValue != null) {
            feature.setAttribute(NAME, nameValue);
        }

        if (descriptionValue != null) {
            feature.setAttribute(DESCRIPTION, descriptionValue);
        }

        if (locationValue != null) {
            feature.setAttribute(LOCATION, locationValue);
        }

        initializeFeaturesGeometry(feature, locationValue);
    }
}