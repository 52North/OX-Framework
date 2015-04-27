/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.feature;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * This class shows how to use the OXF's feature concept.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class HowToFeatureModel {

    public static void main(String[] args) {

        // At first we have to set up the feature's type:

        // the feature type shall be called WeatherStation.
        String fTypeName = "WeatherStation";

        // the feature type has got 2 attributes: 'POSITION' and 'SENSOR'
        // a WeatherStation must have exactly 1 POSITION : 
        OXFFeatureAttributeDescriptor positionAttribute = new OXFFeatureAttributeDescriptor("POSITION",
                                                                                         DataType.OBJECT,
                                                                                         Point.class,
                                                                                         1,
                                                                                         1,
                                                                                         "This attribute stores the geometrical position of the station.");
        // a WeatherStation is associated with 1 or multiple SENSORs : 
        OXFFeatureAttributeDescriptor sensorAttribute = new OXFFeatureAttributeDescriptor("SENSOR",
                                                                                       DataType.STRING,
                                                                                       String[].class,
                                                                                       1,
                                                                                       Integer.MAX_VALUE,
                                                                                       "This attribute stores the id of a sensor affiliated with the station.");
        // wrap these attributes up in a list...
        List<OXFFeatureAttributeDescriptor> attributeList = new ArrayList<OXFFeatureAttributeDescriptor>();
        attributeList.add(positionAttribute);
        attributeList.add(sensorAttribute);
        
        // ... which serves as an input for the feature type instantiation:
        OXFFeatureType fType = new OXFFeatureType(fTypeName, attributeList);
        
        
        //---------------------------------------------------
        

        // now we can instantiate features of that type and put them into a feature collection:
        
        // because the feature type of this collection is not important let's leave it null
        OXFFeatureCollection fCollection = new OXFFeatureCollection("id_123", null);
        
        OXFFeature feature1 = new OXFFeature("id_124", fType);
        feature1.setAttribute("POSITION", new GeometryFactory().createPoint(new Coordinate(10, 20, 30)));
        feature1.setAttribute("SENSOR", new String[]{"precipitation_sensor_987", "temperature_sensor_986"});
        
        OXFFeature feature2 = new OXFFeature("id_125", fType);
        feature2.setAttribute("POSITION", new GeometryFactory().createPoint(new Coordinate(5, 6, 7)));
        feature2.setAttribute("SENSOR", new String[]{"precipitation_sensor_985"});
        
        fCollection.add(feature1);
        fCollection.add(feature2);
        
        
        // and finally we are able to access the features and their attributes:
        
        Point p = (Point)fCollection.getFeature("id_124").getAttribute("POSITION");
        System.out.println("Feature's point is: " + p);
    }

}
