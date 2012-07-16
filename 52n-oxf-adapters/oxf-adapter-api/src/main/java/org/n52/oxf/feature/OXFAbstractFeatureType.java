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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.gml.AbstractFeatureType;
import net.opengis.gml.CoordinatesType;
import net.opengis.gml.PointType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFAbstractFeatureType extends org.n52.oxf.feature.OXFFeatureType {

    public static final String DESCRIPTION = "description";
    public static final String NAME = "name";
    public static final String LOCATION = "location";

    /**
     * 
     */
    public OXFAbstractFeatureType() {
        super("OXFAbstractFeatureType", null);
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     * 
     * @param doc
     * @return
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = new ArrayList<OXFFeatureAttributeDescriptor>();

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
     *        shall be an instance of <code>net.opengis.gml.AbstractFeatureType</code>, taken form a
     *        concrete <code>AbstractObservationDocument</code>.
     * @return
     */
    public void initializeFeature(OXFFeature feature, AbstractFeatureType xb_abstractFeature) {

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