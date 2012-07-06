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

package org.n52.oxf.layer;

import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A layer to paint the given features if no requests to services are needed for updates.
 * 
 * This class initializes the feature collection on instantiation and just renders these
 * again and again.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeatureLayer extends AbstractLayer implements IFeatureLayer {

    private static Logger LOGGER = LoggingHandler.getLogger(FeatureLayer.class);

    private Set<OXFFeature> selectedFeatures;
    private IFeatureStore featureStore;
    private OXFFeatureCollection featureCollection;
    private IFeaturePicker featurePicker;

    private String srs;

    private BoundingBox bBox;

    private String layerSourceTitle;

    private String layerSourceType;

    private FeatureLayerProcessor processor;

    public FeatureLayer(String identificationName,
                        String title,
                        String layerSourceTitle,
                        String layerSourceType,
                        IFeatureDataRenderer renderer,
                        IFeaturePicker featurePicker,
                        OXFFeatureCollection collection) {
        super(identificationName, title, null, renderer);

        this.layerSourceTitle = layerSourceTitle;
        this.layerSourceType = layerSourceType;
        this.renderer = renderer;
        this.featurePicker = featurePicker;
        this.featureCollection = collection;
        this.processor = new FeatureLayerProcessor(this);
        
        init();
    }

    /**
     * 
     */
    private void init() {
        this.parameterContainer = new ParameterContainer();

        try {
            // add bbox to parameter container because it is needed in the rendering process
            this.parameterContainer.addParameterShell(new ParameterShell(new Parameter(Parameter.COMMON_NAME_BBOX,
                                                                                       true,
                                                                                       getBBox(),
                                                                                       Parameter.COMMON_NAME_BBOX),
                                                                         getBBox()));

            // values will be filled in method super.refreshParameterContainer(contextBBox, window)
            this.parameterContainer.addParameterShell(new ParameterShell(new Parameter(Parameter.COMMON_NAME_SRS,
                                                                                       true,
                                                                                       new StringValueDomain(getSrs()),
                                                                                       Parameter.COMMON_NAME_SRS),
                                                                         getSrs()));
        }
        catch (NullPointerException e) {
            throw new OXFRuntimeException("parameter container of the layer '" + getIDName()
                    + "' could not be initialized successfully.", e);
        }
        catch (OXFException e) {
            throw new OXFRuntimeException("parameter container of the layer '" + getIDName()
                    + "' could not be initialized successfully.", e);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.layer.AbstractLayer#getLayerVisualization()
     */
    @Override
    public IVisualization getLayerVisualization() {
        return this.layerVisualization;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.layer.FeatureServiceLayer#eventCaught(org.n52.oxf.util.OXFEvent)
     */
    @Override
    public void eventCaught(OXFEvent event) throws OXFEventException {
        LOGGER.info("event caught: " + event + "  - layerID: " + getIDName());

        // FEATURES_SELECTED fired by LayerContext:
        if (event.getName().equals(EventName.FEATURES_SELECTED)) {
            IFeatureLayer layer = (IFeatureLayer) event.getValue();

            if (layer.equals(this)) {
                try {
                    getProcessor().renderData();
                }
                catch (OXFException e) {
                    throw new OXFEventException(e);
                }
            }

            // clear all current selections:
            else if ( !layer.equals(this) && layer.getSelectedFeatures() != null) {
                clearSelection();

                try {
                    getProcessor().renderData();
                }
                catch (OXFException e) {
                    throw new OXFEventException(e);
                }
            }
        }
    }

    @Override
    public FeatureLayerProcessor getProcessor() {
        return this.processor;
    }

    public void clearSelection() {
        this.selectedFeatures = null;
    }

    public OXFFeatureCollection getFeatureCollection() {
        return this.featureCollection;
    }

    public IFeatureStore getFeatureStore() {
        return this.featureStore;
    }

    public Set<OXFFeature> getSelectedFeatures() {
        return this.selectedFeatures;
    }

    public Set<OXFFeature> pickFeature(int x, int y) {
        if (this.featurePicker != null) {
            int height = ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue()).intValue();
            int width = ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue()).intValue();
            IBoundingBox bbox = (IBoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

            return featurePicker.pickFeature(x, y, width, height, this.featureCollection, bbox);
        }
        return null;
    }

    public Set<OXFFeature> pickFeature(int llX, int llY, int urX, int urY) {
        if (this.featurePicker != null) {
            int height = Integer.parseInt((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue());
            int width = Integer.parseInt((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue());
            IBoundingBox bbox = (IBoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

            return this.featurePicker.pickFeature(llX, llY, urX, urY, width, height, featureCollection, bbox);
        }
        return null;
    }

    public void setFeatureCollection(OXFFeatureCollection featureCollection) {
        this.featureCollection = featureCollection;
    }

    public void setSelectedFeatures(Set<OXFFeature> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
    }

    public IBoundingBox getBBox() {
        if (this.bBox == null) {

            // TODO check this code with several features in collection
            // create bbox from featureCollection
            Geometry unionOfBoundingBoxes = getFeatureCollection().iterator().next().getGeometry();
            for (OXFFeature feature : this.featureCollection) {
                Geometry geom = feature.getBoundingBox();
                unionOfBoundingBoxes.union(geom);
            }

            Coordinate[] unionCoords = unionOfBoundingBoxes.getEnvelope().getCoordinates();
            double[] uR = {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
            double[] lL = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};

            for (Coordinate coord : unionCoords) {
                lL[0] = Math.min(lL[0], coord.x);
                lL[1] = Math.min(lL[1], coord.y);
                lL[2] = Math.min(lL[2], coord.z);
                uR[0] = Math.max(uR[0], coord.x);
                uR[1] = Math.max(uR[1], coord.y);
                uR[2] = Math.max(uR[2], coord.z);
            }

            this.bBox = new BoundingBox(getSrs(), lL, uR);

            // handle bounding box with extend of 0 in one direction
            double defaultBoundingBoxHalfSize = 0.5; // TODO put the default bounding box size somwhere else
            if (bBox.getWidth() < Double.MIN_VALUE || bBox.getHeight() < Double.MIN_VALUE) {
                double[] bigLL = {bBox.getLowerCorner()[0] - defaultBoundingBoxHalfSize,
                                  bBox.getLowerCorner()[1] - defaultBoundingBoxHalfSize};
                double[] bigUR = {bBox.getUpperCorner()[0] + defaultBoundingBoxHalfSize,
                                  bBox.getUpperCorner()[1] + defaultBoundingBoxHalfSize};

                if (bBox.getDimensions() == 3 && bBox.getDepth() < Double.MIN_VALUE) {
                    double[] bigLL2 = {bigLL[0], bigLL[1], bBox.getLowerCorner()[2] - defaultBoundingBoxHalfSize};
                    bigLL = bigLL2;
                    double[] bigUR2 = {bigUR[0], bigUR[1], bBox.getUpperCorner()[2] + defaultBoundingBoxHalfSize};
                    bigUR = bigUR2;
                }
                this.bBox = new BoundingBox(bBox.getCRS(), bigLL, bigUR);
            }

        }
        return this.bBox;
    }

    public String getLayerSourceTitle() {
        return this.layerSourceTitle;
    }

    public String getLayerSourceType() {
        return this.layerSourceType;
    }

    public String getSrs() {
        if (this.srs == null) {
            if (getFeatureCollection() == null) {
                init();
            }
            int epsg = this.featureCollection.iterator().next().getGeometry().getSRID();
            for (OXFFeature f : this.featureCollection) {
                if ( ! (epsg == f.getGeometry().getSRID())) {
                    LOGGER.warn("More than one EPSG specified in featureCollection > returning the first found.");
                }
            }
//            this.srs = SrsHelper.getSpatialReferenceSystemAndCode(epsg); // XXX TEMPORARY COMMENT TO LET IT COMPILE
        }
        return this.srs;
    }

    public void serializeToContext(StringBuffer sb) {
        // TODO implement method serializeToContext
        throw new NotImplementedException();
    }

    @Override
    public IFeatureDataRenderer getRenderer() {
        return (IFeatureDataRenderer) this.renderer;
    }

}
