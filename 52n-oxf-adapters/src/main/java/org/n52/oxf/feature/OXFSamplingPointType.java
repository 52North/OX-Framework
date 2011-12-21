/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 11.01.2008
 *********************************************************************************/
package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.PointType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;

import org.opengis.feature.DataType;
import org.opengis.feature.FeatureAttributeDescriptor;

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
    protected List<FeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<FeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

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