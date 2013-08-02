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
