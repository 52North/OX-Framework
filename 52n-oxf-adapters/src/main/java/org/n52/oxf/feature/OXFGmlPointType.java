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
