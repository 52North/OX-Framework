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
 
 Created on: 25.01.2006
 *********************************************************************************/

package org.n52.oxf.layer;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.OWSException;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class FeatureServiceLayer extends AbstractServiceLayer implements IFeatureLayer {

    /** current state of featureCollection represented by this layer */
    protected OXFFeatureCollection featureCollection;

    protected IFeatureStore featureStore;

    protected FeatureServiceLayerProcessor processor;

    protected Set<OXFFeature> selectedFeatures = null;

    /** stays eventually null if there is no picking support for this Layer */
    protected IFeaturePicker featurePicker = null;

    private static Logger LOGGER = LoggingHandler.getLogger(FeatureServiceLayer.class);

    public FeatureServiceLayer(IServiceAdapter adapter,
                               IFeatureDataRenderer featureRenderer,
                               IFeatureStore featureStore,
                               IFeaturePicker featurePicker,
                               ServiceDescriptor descriptor,
                               ParameterContainer parameterContainer,
                               String layerName,
                               String layerTitle,
                               String resourceOperationName,
                               boolean caching) {
        super(adapter, featureRenderer, descriptor, parameterContainer, layerName, layerTitle, resourceOperationName);

        this.featureStore = featureStore;
        this.featurePicker = featurePicker;

        this.processor = new FeatureServiceLayerProcessor(this, caching);
    }

    /**
     * This method returns a List of all OXFFeatures of this Layer drawn at screen-position (x, y) or
     * <code>null</code> if no <code>FeaturePicker</code> was defined.
     */
    public Set<OXFFeature> pickFeature(int x, int y) {
        if (featurePicker != null) {
            int height = ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue()).intValue();
            int width = ((Integer) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue()).intValue();
            IBoundingBox bbox = (IBoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

            return featurePicker.pickFeature(x, y, width, height, featureCollection, bbox);
        }
        return null;
    }

    /**
     * This method returns a List of all OXFFeatures of this Layer drawn inside the Screen-BoundingBox
     * specified by the lowerLeft-corner (llX, llY) and the upperRight-corner (urX, urY) or <code>null</code>
     * if no <code>FeaturePicker</code> was defined.
     */
    public Set<OXFFeature> pickFeature(int llX, int llY, int urX, int urY) {

        if (featurePicker != null) {
            int height = Integer.parseInt((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue());
            int width = Integer.parseInt((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue());
            IBoundingBox bbox = (IBoundingBox) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

            return featurePicker.pickFeature(llX, llY, urX, urY, width, height, featureCollection, bbox);
        }
        return null;
    }

    /**
     * 
     * @return the currently selected Set of features in this Layer.
     */
    public Set<OXFFeature> getSelectedFeatures() {
        return selectedFeatures;
    }

    /**
     * @param selectedFeatures
     * @throws OXFEventException
     */
    public void setSelectedFeatures(Set<OXFFeature> selectedFeatures) {
        this.selectedFeatures = selectedFeatures;
    }

    /**
     * 
     */
    public void clearSelection() {
        this.selectedFeatures = null;
    }

    /**
     * @return the actual collection of features that will be portrayed in this layer. A null value is allowed
     *         and indicates that nothing will be drawn.
     */
    public OXFFeatureCollection getFeatureCollection() {
        if (this.featureCollection == null || this.featureCollection.isEmpty()) {
            try {
                getProcessor().getProcess().initializeDownloadAndUnmarshalFeatureCollection();
            }
            catch (ExceptionReport e) {
                Iterator<OWSException> iter = e.getExceptionsIterator();
                LOGGER.error("OWS exception: Error downloading data for feature collection: " + e.getMessage());
                while (iter.hasNext()) {
                    System.out.println(iter.next().getExceptionCode());
                }
            }
            catch (OXFException e) {
                LOGGER.error("Error downloading data for feature collection or during unmarshalling" + e.getMessage(),
                             e);
            }
        }
        return featureCollection;
    }

    /**
     * sets the actual collection of features.
     * 
     * @param featureCollection
     */
    public void setFeatureCollection(OXFFeatureCollection featureCollection) {
        this.featureCollection = featureCollection;
    }

    /**
     * 
     */
    public IFeatureStore getFeatureStore() {
        return featureStore;
    }

    /**
     * 
     * @return a FeatureServiceLayerProcessor which can be used to execute the
     *         "download- marshal features - and - render - data"-process in a separate <code>Thread</code>.
     */
    @Override
    public FeatureServiceLayerProcessor getProcessor() {
        return processor;
    }

    @Override
    public IFeatureDataRenderer getRenderer() {
        return (IFeatureDataRenderer) renderer;
    }

    /**
     * 
     */
    @Override
    public void eventCaught(OXFEvent event) throws OXFEventException {
        super.eventCaught(event);

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
    public IBoundingBox getBBox() {
        IBoundingBox superBBox = super.getBBox();
        if (superBBox == null) {
            // create bbox from features

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

            BoundingBox bBox = new BoundingBox(getSrs(), lL, uR);

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
                bBox = new BoundingBox(bBox.getCRS(), bigLL, bigUR);
            }

            return bBox;
        }
        return superBBox;
    }

    @Override
    public String getSrs() {
        String srs = super.getSrs();

        if (srs == null) {

            if (getFeatureCollection() == null) {
                try {

                    getProcessor().getProcess().initializeDownloadAndUnmarshalFeatureCollection();

                }
                catch (ExceptionReport e) {
                    LOGGER.error("OWS exception: Error downloading data for feature collection " + e.getMessage(), e);
                }
                catch (OXFException e) {
                    LOGGER.error("Error downloading data for feature collection or during unmarshalling"
                            + e.getMessage(), e);
                }
            }

            int epsg = this.featureCollection.iterator().next().getGeometry().getSRID();

            for (OXFFeature f : this.featureCollection) {
                if ( ! (epsg == f.getGeometry().getSRID())) {
                    LOGGER.warn("More than one EPSG specified in featureCollection > returning the first found.");
                }
            }

            // srs = SrsHelper.getReferenceFrameString(epsg);
//            srs = SrsHelper.getSpatialReferenceSystemAndCode(epsg);// XXX TEMPORARY COMMENT TO LET IT COMPILE
        }
        return srs;
    }

}