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

package org.n52.oxf.context;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.layer.AbstractLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.layer.IFeatureLayer;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.serialization.ContextWriter;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class LayerContext extends Context {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerContext.class);

    /**
     * contains all IContextLayer objects of this LayerContext.
     */
    private List<IContextLayer> contextLayerList;

    private ContextBoundingBox contextBBox;

    private ContextWindow contextWindow;

    /**
     * 
     * @param imageBuilder
     * @param contextBBox
     * @param window
     */
    public LayerContext(String id,
                        String title,
                        OverlayEngine imageBuilder,
                        ContextBoundingBox contextBBox,
                        ContextWindow window) {
        super(id, title);
        this.contextLayerList = new ArrayList<IContextLayer>();
        this.contextBBox = contextBBox;
        this.contextWindow = window;

        this.addEventListener(imageBuilder);
        contextBBox.addEventListener(this);
        window.addEventListener(this);
    }

    /**
     * 
     * @param imageBuilder
     * @param contextBBox
     * @param window
     * @param layersToBeAdded
     * @throws OXFException
     * @throws OXFException
     */
    public LayerContext(String id,
                        String title,
                        OverlayEngine imageBuilder,
                        ContextBoundingBox contextBBox,
                        ContextWindow window,
                        List<IContextLayer> layersToBeAdded) throws OXFEventException, OXFException {
        this(id, title, imageBuilder, contextBBox, window);
        addLayer(layersToBeAdded);
    }

    /**
     * 
     * @param imageBuilder
     * @param contextBBox
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public LayerContext(String id,
                        String title,
                        OverlayEngine imageBuilder,
                        ContextBoundingBox contextBBox,
                        ContextWindow window,
                        IContextLayer layer) throws OXFEventException, OXFException {
        this(id, title, imageBuilder, contextBBox, window);
        addLayer(layer);
    }

    public ContextWindow getContextWindow() {
        return contextWindow;
    }

    public ContextBoundingBox getContextBoundingBox() {
        return contextBBox;
    }
    
    public List<IContextLayer> getContextLayers() {
        if (contextLayerList != null) {
            return contextLayerList;
        } else {
            return new ArrayList<IContextLayer>();
        }
    }

    public IContextLayer get(int i) {
        return contextLayerList.get(i);
    }

    public IContextLayer get(String layerId) {
        for (IContextLayer layer : contextLayerList) {
            if (layer.getIDName().equals(layerId)) {
                return layer;
            }
        }
        return null;
    }

    /**
     * 
     * @param layerId
     * @return - true if this LayerContext contains a Layer with the specified layerId.
     */
    public boolean contains(String layerId) {
        for (IContextLayer layer : contextLayerList) {
            if (layer.getIDName().equals(layerId)) {
                return true;
            }
        }
        return false;
    }

    public int getLayerCount() {
        return contextLayerList.size();
    }

    /**
     * Inserts the specified layer in the LayerContext. <br>
     * <br>
     * The layer will be added to the ContextBoundingBox as a IEventListener. And this LayerContext will be
     * added as a IEventListener to the layer.
     * 
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public void addLayer(IContextLayer layer) throws OXFEventException, OXFException {
        contextLayerList.add(layer);

        // realize listener-concept:
        contextBBox.addEventListener(layer);
        this.addEventListener(layer);
        layer.addEventListener(this);

        eventSupport.fireEvent(EventName.NEW_LAYER_ADDED, layer);

        processLayer(layer);
    }

    /**
     * Inserts the specified layers in the LayerContext. The order of the layers in the list will be kept. <br>
     * <br>
     * Each layer will be added to the ContextBoundingBox as a IEventListener. And this LayerContext will be
     * added as a IEventListener to every layer.
     * 
     * @param layerTable
     * @throws OXFException
     * @throws OXFException
     */
    public void addLayer(List<IContextLayer> layers) throws OXFEventException, OXFException {
        for (IContextLayer layer : layers) {
            this.contextLayerList.add(layer);

            // realize listener-concept:
            contextBBox.addEventListener(layer);
            this.addEventListener(layer);
            layer.addEventListener(this);

            eventSupport.fireEvent(EventName.NEW_LAYER_ADDED, layer);
        }

        processLayerList(layers);
    }

    /**
     * Inserts the specified layers in the LayerContext. The first layer of the specified List will be added
     * at the specified position in the LayerContext directly followed by the other layers. The layer
     * currently at that position (if any) and any subsequent layers will be shifted to the right<br>
     * <br>
     * The layers will be added to the ContextBoundingBox as a IEventListener. And this LayerContext will be
     * added as a IEventListener to the layers.
     * 
     * @param index
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public void addLayer(int index, List<IContextLayer> layers) throws OXFEventException, OXFException {
        int i = index;
        for (IContextLayer layer : layers) {
            this.contextLayerList.add(i++, layer);

            // realize listener-concept:
            contextBBox.addEventListener(layer);
            this.addEventListener(layer);
            layer.addEventListener(this);

            eventSupport.fireEvent(EventName.NEW_LAYER_ADDED, layer);
        }

        processLayerList(layers);
    }

    /**
     * Inserts the specified layer at the specified position in the LayerContext. Shifts the layer currently
     * at that position (if any) and any subsequent layers to the right (adds one to their indices). <br>
     * <br>
     * The layers will be added to the ContextBoundingBox as a IEventListener. And this LayerContext will be
     * added as a IEventListener to the layers.
     * 
     * @param index
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public void addLayer(int index, IContextLayer layer) throws OXFEventException, OXFException {
        contextLayerList.add(index, layer);

        // realize listener-concept:
        contextBBox.addEventListener(layer);
        this.addEventListener(layer);
        layer.addEventListener(this);

        eventSupport.fireEvent(EventName.NEW_LAYER_ADDED, layer);

        processLayer(layer);
    }

    /**
     * removes the layer from this LayerContext. <br>
     * 
     * @param layer
     *        layer to be removed from this LayerContext, if present.
     * @return true if the LayerContext contained the specified element.
     * @throws OXFException
     * @throws OXFException
     */
    public boolean removeLayer(IContextLayer layer) throws OXFEventException, OXFException {

        // remove listener:
        contextBBox.removeEventListener(layer);
        this.removeEventListener(layer);
        layer.removeEventListener(this);

        boolean res = contextLayerList.remove(layer);

        if (res) {
            if (contextLayerList.isEmpty()) {
                contextBBox.setSRS("");
                contextBBox.setExtent(new Point2D.Double(Double.NaN, Double.NaN), new Point2D.Double(Double.NaN,
                                                                                                     Double.NaN), true);
            }

            eventSupport.fireEvent(EventName.LAYER_REMOVED, layer);

            // initiate re-rendering:
            if ( !layer.isHidden() && !layer.isBroken()) {
                initiateOverlayRendering(contextLayerList);
            }
        }

        return res;
    }

    /**
     * performs a feature selection for the featureLayer and fires a FEATURES_SELECTED-event, so all
     * <code>IFeatureLayer</code>s. This causes the deselection of all current feature selections.
     * 
     * @param featureLayer
     * @param selectedFeatures
     * @throws OXFEventException
     * @throws OXFException
     */
    public void selectFeatures(IFeatureLayer featureLayer, Set<OXFFeature> selectedFeatures) throws OXFEventException,
            OXFException {
        featureLayer.setSelectedFeatures(selectedFeatures);

        eventSupport.fireEvent(EventName.FEATURES_SELECTED, featureLayer);

        initiateOverlayRendering(contextLayerList);
    }

    /**
     * @return the current feature-selection or <code>null</code> if no features are selected.
     */
    public Set<OXFFeature> getSelectedFeatures() {
        for (IContextLayer layer : contextLayerList) {
            if (layer instanceof IFeatureLayer) {
                IFeatureLayer featureLayer = (IFeatureLayer) layer;
                return featureLayer.getSelectedFeatures();
            }
        }
        return null;
    }

    /**
     * removes the layers from there actual positions and adds them to the specified index.
     * 
     * @param index
     * @param layers
     * @throws OXFException
     * @throws OXFException
     */
    public void moveLayer(int indexP, List<IContextLayer> layers) throws OXFEventException, OXFException {
        for (IContextLayer layer : layers) {
            contextLayerList.remove(layer);

            int index = indexP;
            if (index >= contextLayerList.size()) {
                contextLayerList.add(layer); // add the layer to the end
            }
            else {
                contextLayerList.add(index, layer);
            }
            index = index + 1;
        }

        // the following code must run in a separate for-loop because of thread-safeness.
        for (IContextLayer layer : this.contextLayerList) {
            eventSupport.fireEvent(EventName.LAYER_MOVED, layer);
        }

        initiateOverlayRendering(contextLayerList);
    }

    /**
     * increases the position of the layer in the LayerContext. If the layer is already at last position
     * nothing happens. <br>
     * 
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public void shiftUp(IContextLayer layer) throws OXFEventException, OXFException {
        int index = contextLayerList.indexOf(layer);

        if (index > 0) {
            contextLayerList.remove(layer);
            contextLayerList.add(--index, layer);

            eventSupport.fireEvent(EventName.LAYER_SHIFTED_UP, layer);

            // initiate re-rendering:
            if ( !layer.isHidden() && !layer.isBroken()) {
                initiateOverlayRendering(contextLayerList);
            }
        }
    }

    /**
     * decreases the position of the layer in the LayerContext. If the layer is already at first position
     * nothing happens. <br>
     * 
     * @param layer
     * @throws OXFException
     * @throws OXFException
     */
    public void shiftDown(IContextLayer layer) throws OXFEventException, OXFException {

        int index = contextLayerList.indexOf(layer);
        if (index < contextLayerList.size()) {
            contextLayerList.remove(layer);

            index = index + 1;
            if (index >= contextLayerList.size()) {
                contextLayerList.add(layer); // add the layer to the end
            }
            else {
                contextLayerList.add(index, layer);
            }

            eventSupport.fireEvent(EventName.LAYER_SHIFTED_DOWN, layer);

            // initiate re-rendering:
            if ( !layer.isHidden() && !layer.isBroken()) {
                initiateOverlayRendering(contextLayerList);
            }
        }
    }

    /**
     * 
     * @param layer
     * @return the position of the specified layer in this context.
     */
    public int getPosition(IContextLayer layer) {
        return contextLayerList.indexOf(layer);
    }

    /**
     * <li>requests data for the layer <li>renders the layerImage <li>initiates re-rendering of the
     * overlayImage.
     * 
     * @param layer
     * @throws OXFException
     */
    private void processLayer(IContextLayer layer) throws OXFEventException, OXFException {
        List<IContextLayer> tempLayerList = new ArrayList<IContextLayer>();
        tempLayerList.add(layer);
        processLayerList(tempLayerList);
    }

    /**
     * 
     * <li>requests data for the layers in the list <li>renders the layerImages <li>initiates re-rendering of
     * the overlayImage.
     * 
     * @param layersToProcess
     * @throws OXFException
     */
    private void processLayerList(List<IContextLayer> layersToProcess) throws OXFEventException, OXFException {

        // --- initialize the layers with the correct parameters. then start the RasterServiceLayerProcessor

        for (int i = 0; i < layersToProcess.size(); i++) {
            IContextLayer layer = layersToProcess.get(i);

            if ( !layer.isHidden()) {

                if (layer instanceof AbstractLayer) {
                    AbstractLayer abstractLayer = (AbstractLayer) layer;

                    // TODO remove side effects of the following method: it also fills the parameter container
                    // if values are not set
                    abstractLayer.refreshParameterContainer(contextBBox, contextWindow);

                    // boolean paramContainerHasChanged = abstractLayer.refreshParameterContainer(contextBBox,
                    // contextWindow);
                    try {
                        // process layer only if its ParameterContainer is refreshed:
                        // if (paramContainerHasChanged) {

                        abstractLayer.getProcessor().startProcess();

                        // }
                        // else {
                        // LOGGER.info("NOT processing layer '" + abstractServiceLayer.getIDName()
                        // + "' because its ParameterContainer has not changed.");
                        // }
                    }
                    catch (OXFRuntimeException runtimeExec) {
                        LOGGER.error("OXFRuntimeException occured during request and rendering process of layer '"
                                + abstractLayer.getIDName() + "'", runtimeExec);
                    }
                }
            }
        }

        // --- join the started Threads and add the not-hidden, not-broken layers to the
        // --- layersToRender-list. Then trigger overlay-rendering
        // ArrayList<IContextLayer> layersToRender = new ArrayList<IContextLayer>();

        for (int i = 0; i < layersToProcess.size(); i++) {
            IContextLayer layer = layersToProcess.get(i);

            if ( !layer.isHidden()) {

                if (layer instanceof AbstractLayer) {
                    AbstractLayer abstractLayer = (AbstractLayer) layer;

                    // TODO: Was ist wenn ein Service nicht antwortet bzw. sehr lange braucht?
                    // TODO: --> im RasterServiceLayerProcessor einen TimeOut einbauen.
                    try {

                        abstractLayer.getProcessor().getProcess().join();
                    }
                    catch (InterruptedException e) {
                        throw new OXFException(e);
                    }
                }
            }
        }

        initiateOverlayRendering(contextLayerList);
    }

    /**
     * takes all not-hidden and not-broken layers of the contextLayerList and fires the
     * ALL_LAYERS_READY_TO_OVERLAY event to trigger the overlay rendering. <br>
     * <br>
     * ATTENTION: the layers must be rendered for the actual ContextWindow and ContextBoundingBox before you
     * start this method.
     * 
     * @param contextLayerList
     *        - the list of Layers for which the overlay should be rendered.
     * @throws OXFEventException
     * @throws OXFException
     *         if one not-broken and not-hidden layer has not yet been rendered.
     */
    private void initiateOverlayRendering(List<IContextLayer> layerList) throws OXFEventException, OXFException {
        ArrayList<IContextLayer> layersToRender = new ArrayList<IContextLayer>();
        for (IContextLayer layer : layerList) {
            if ( !layer.isHidden() && !layer.isBroken()) {
                if (layer.getLayerVisualization() == null) {
                    throw new OXFException("LayerVisualization of the Layer '" + layer.getIDName()
                            + "' has not yet been rendered.");
                }
                layersToRender.add(layer);
            }
        }
        // OverlayEngine listens to the following event:
        eventSupport.fireEvent(EventName.ALL_LAYERS_READY_TO_OVERLAY, layersToRender);
    }

    /**
     * @throws
     * 
     */
    public void eventCaught(OXFEvent event) throws OXFEventException {
        LOGGER.info("event caught: " + event);

        try {
            // reaction on events from ContextBoundingBox...
            if (event.getName().equals(EventName.EXTENT_CHANGED)) {
                processLayerList(contextLayerList);
            }

            // reaction on events from ContextWindow...
            else if (event.getName().equals(EventName.WINDOW_SIZE_CHANGED)) {
                processLayerList(contextLayerList);
            }

            // reaction on events from Layer...
            else if (event.getName().equals(EventName.LAYER_VISIBILITY_CHANGED)
                    || event.getName().equals(EventName.LAYER_REAL_TIME_REFRESH)) {
                IContextLayer emittingLayer = (IContextLayer) event.getSource();

                if (emittingLayer.isHidden()) {
                    // the emitting layer changed its visibility from not-hidden to hidden. so initiate an
                    // re-rendering of the new overlay image.
                    initiateOverlayRendering(contextLayerList);
                }
                else {
                    // request data for the layer, render the layerImage and initiate re-rendering of
                    // overlayImage:
                    processLayer(emittingLayer);
                }
            }
        }
        catch (OXFException e) {
            throw new OXFEventException(e);
        }
    }

    public void writeTo(ContextWriter serializer) {
        serializer.write(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LayerContext:\n		layersList=");
        sb.append(this.contextLayerList);
        sb.append("\n		bbox=");
        sb.append(this.contextBBox);
        sb.append("\n		window=");
        sb.append(this.contextWindow);
        sb.append("]");
        return sb.toString();
    }

}