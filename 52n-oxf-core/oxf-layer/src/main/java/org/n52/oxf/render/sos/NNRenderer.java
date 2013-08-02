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

package org.n52.oxf.render.sos;

import java.util.List;

import org.n52.oxf.feature.OXFAbstractObservationType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFMeasurementType;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class NNRenderer extends InterpolationRenderer {

    /**
     * computes the interpolated value with the Nearest-Neighbour procedure. This is the measurement of the
     * nearest observation.
     */
    public Double computeInterpolatedValue(Coordinate coordToInterpolate,
                                           List<OXFFeature> observationList) {
        OXFFeature tempNearestObservation = null;

        for (OXFFeature observation : observationList) {

            // TODO: the location of the FOI (associated with the observation) will be taken and not the
            // Location of the observation itself.
            if (observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST) != null) {
                OXFFeature featureOfInterest = (OXFFeature) observation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                // TODO Spec-Too-Flexible-Problem --> various GeometryTypes are possible:
                if (featureOfInterest.getGeometry() instanceof Point) {
                    Point foiPoint = (Point) featureOfInterest.getGeometry();

                    if (tempNearestObservation == null) {
                        tempNearestObservation = observation;
                    }
                    else {
                        Coordinate coordinateOfFOI = foiPoint.getCoordinate();

                        OXFFeature tempNearestFOI = (OXFFeature) tempNearestObservation.getAttribute(OXFAbstractObservationType.FEATURE_OF_INTEREST);

                        // TODO Spec-Too-Flexible-Problem --> various GeometryTypes are possible:
                        Coordinate tmpNearestCoordinate = ((Point) tempNearestFOI.getGeometry()).getCoordinate();

                        if (coordToInterpolate.distance(coordinateOfFOI) < coordToInterpolate.distance(tmpNearestCoordinate)) {
                            tempNearestObservation = observation;
                        }
                    }
                }
            }
        }

        // TODO Spec-Too-Flexible-Problem --> various FeatureTypes are possible:
        if (tempNearestObservation.getFeatureType() instanceof org.n52.oxf.feature.OXFMeasurementType) {
            OXFMeasureType measureResult = (OXFMeasureType) tempNearestObservation.getAttribute(OXFMeasurementType.RESULT);
            return measureResult.getValue();
        }
        else {
            return null;
        }
    }
    
    /**
     * @return a plain text description of this Renderer.
     */
    public String getDescription() {
        return "NNRenderer - visualizes an choropleth map for the selected phenomenon";
    }
    
    public String toString() {
        return getDescription();
    }
    
    /**
     * @return the type of the service whose data can be rendered with this ServiceRenderer. In this case
     *         "OGC:SOS" will be returned.
     */
    public String getServiceType() {
        return "OGC:SOS";
    }

    /**
     * @return the versions of the services whose data can be rendered with this ServiceRenderer. In this case
     *         {"1.0.0"} will be returned.
     */
    public String[] getSupportedVersions() {
        return new String[] {"0.0.0"};
    }
}