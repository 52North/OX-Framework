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

import java.util.List;

import net.opengis.gml.AbstractSurfaceType;
import net.opengis.gml.PolygonType;
import net.opengis.gml.SurfacePropertyType;
import net.opengis.sampling.x10.SamplingSurfaceDocument;
import net.opengis.sampling.x10.SamplingSurfaceType;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFSamplingSurfaceType extends OXFAbstractFeatureType {

    public static final String GEOMETRY = "geometry";

    /**
     * 
     */
    public OXFSamplingSurfaceType() {
        super();

        typeName = "OXFSamplingSurfaceType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    
    public static OXFFeature create(SamplingSurfaceDocument xb_saSurfaceDoc) {

        SamplingSurfaceType xb_samplingSurface = xb_saSurfaceDoc.getSamplingSurface();

        String id = xb_samplingSurface.getId();

        // FeatureType of the feature:
        OXFSamplingSurfaceType oxf_saSurfaceType = new OXFSamplingSurfaceType();

        OXFFeature feature = new OXFFeature(id, oxf_saSurfaceType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_saSurfaceType.initializeFeature(feature, xb_samplingSurface);

        return feature;
    }

    /**
     * 
     * @param doc
     * @return
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor position = new OXFFeatureAttributeDescriptor(GEOMETRY,
                                                                                   DataType.OBJECT,
                                                                                   Polygon.class,
                                                                                   1,
                                                                                   1,
                                                                                   "");
        attributeDescriptors.add(position);
        
        // TODO support for other attributes of SamplingSurface (e.g. sampledFeature)

        return attributeDescriptors;
    }

    /**
     * supports the Sampling specification of version 1.0:
     */
    public void initializeFeature(OXFFeature feature,
                                  SamplingSurfaceType xb_saSurface) {
        super.initializeFeature(feature, xb_saSurface);

        //
        // --- initialize the POSITION-attribute:

        SurfacePropertyType shape = xb_saSurface.getShape();
        AbstractSurfaceType surface = shape.getAbstractSurface();
        if (shape != null
        		&& surface != null
                && ((PolygonType)surface).getExterior() != null
                && ((PolygonType)surface).getExterior().getAbstractRing() != null) {

            PolygonType xb_polygon = (PolygonType)surface;

            // TODO: create polygon
            
//            LinearRing linearRing = new LinearRing(points, factory);
//            
//            CoordType[] xb_coordinateArray = ((LinearRingType)xb_polygon.getExterior().getRing()).getCoordArray();
//            for (int i = 0; i < xb_coordinateArray.length; i++) {
//				
//			}
//            
//            DirectPositionType xb_pos = xb_point.getPos();
//
//            List coordsList = xb_pos.getListValue();
//
//            double x = (Double) coordsList.get(0);
//            double y = (Double) coordsList.get(1);
//            double z = Double.NaN;
//            try {
//            		z = (Double) coordsList.get(2);
//			} catch (Exception e) {
//				// no Exception -> 2D Point
//			}
//            
//
//            if (coordsList.size() > 2) {
//                z = (Double) coordsList.get(2);
//            }

            Polygon polygon = new GeometryFactory().createPolygon(null, null);

            feature.setAttribute(GEOMETRY, polygon);
            initializeFeaturesGeometry(feature, polygon);
        }

        // check if the geometry-attribute is set: (could be set in this or the super class)
        if (feature.getGeometry() == null) {
            throw new IllegalArgumentException("The geometry attribute could not be initialized.");
        }
        
    }
}
