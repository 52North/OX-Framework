/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.util;

/**
 * this enum stores all possibe events (respectively event names) which could be fired by the emitter-classes
 * (implementing the <code>IEventEmitter</code> interface).<br>
 * <br>
 * The event-listener-concept is used for internal communication in the OX-Framework between the different
 * classes. Another important advantage of this concept is that also external applications can listen up these
 * fired <code>OXFEvent</code>s and can react on them. These applications just need to register as an
 * <code>IEventListener</code> by the specific <code>IEventEmitter</code>-class.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public enum EventName {

    /**
     * fired by the ContextBoundingBox-class when the extent of the BBox has changed. <br>
     * source := org.n52.oxf.context.ContextBoundingBox <br>
     * value := null
     */
    EXTENT_CHANGED,

    /**
     * fired by the ContextBoundingBox-class when the extent of the BBox has changed. <br>
     * Unlike the EXTENT_CHANGED event this event will not trigger directly the
     * "request-data-and-render-image"-proces.<br>
     * source := org.n52.oxf.context.ContextBoundingBox <br>
     * value := null
     */
    EXTENT_CHANGED_SILENTLY,

    /**
     * fired by the ContextBoundingBox-class when the SRS of the BBox has changed. <br>
     * source := org.n52.oxf.context.ContextBoundingBox <br>
     * value := String (the SRS name)
     */
    SRS_CHANGED,

    /**
     * fired by the RasterServiceLayer-class when the data from the service has been received. <br>
     * source := org.n52.oxf.context.ILayer <br>
     * value := null
     */
    OPERATION_RESULT_RECEIVED,

    /**
     * fired by the LayerProcessor-class when the visualization of the Layer has been rendered. <br>
     * source := org.n52.oxf.context.ILayer <br>
     * value := IVisualization
     */
    LAYER_VISUALIZATION_READY,

    /**
     * fired by the LayerContext-class when the layerImages of all visible Layers have been rendered. <br>
     * source := org.n52.oxf.context.LayerContext <br>
     * value := ArrayList<Layer> (containing all layers that should be overlaid)
     */
    ALL_LAYERS_READY_TO_OVERLAY,

    /**
     * fired by OverlayEngine-class when the overlay of all <code>StaticVisualization</code>s of the current
     * layer set has been renderer. Connected UIs should to listen to this OXFEvent. <br>
     * source := org.n52.oxf.render.OverlayEngine <br>
     * value := StaticVisualization (the overlay result) (!! maybe null if there were no images to overlay)
     */
    STATIC_OVERLAY_READY,

    /**
     * fired by OverlayEngine-class when the overlay has been computed. Connected GUIs should to listen to this
     * OXFEvent. <br>
     * source := org.n52.oxf.render.OverlayEngine <br>
     * value := AnimatedVisualization (the overlay result) (!! maybe null if there were no images to overlay)
     */
    ANIMATED_OVERLAY_READY,

    /**
     * fired by LayerContext when the addLayer-method is called and a new Layer will be added. <br>
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.Layer (the Layer which will be added)
     */
    NEW_LAYER_ADDED,

    /**
     * fired by Layer when the setHidden-method is called and the visibility status of a Layer changes. <br>
     * source := org.n52.oxf.context.Layer <br>
     * value := null
     */
    LAYER_VISIBILITY_CHANGED,

    /**
     * fired by ContextWindow when the setHeight- or setWidht-method is called and window size changes. <br>
     * source := org.n52.oxf.context.ContextWindow <br>
     * value := null
     */
    WINDOW_SIZE_CHANGED,

    /**
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.Layer (the Layer which will be removed)
     */
    LAYER_REMOVED,

    /**
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.Layer (the Layer which will be shifted up)
     */
    LAYER_SHIFTED_UP,

    /**
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.Layer (the Layer which will be shifted down)
     */
    LAYER_SHIFTED_DOWN,

    /**
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.Layer (the Layer which will be moved)
     */
    LAYER_MOVED,

    /**
     * fired by LoggingOutputStream when a message will be logged.<br>
     * source := org.n52.oxf.util.LoggingOutputStream <br>
     * value := null
     */
    LOG_MESSAGE,

    /**
     * source := org.n52.oxf.context.LayerContext <br>
     * value := org.n52.oxf.context.IFeatureLayer
     */
    FEATURES_SELECTED,
    
    /**
     * source := ? <br>
     * value := Integer (the current frame the animation stepped to)
     */
    ANIMATION_STEPPED,
    
    /**
     * source := org.n52.oxf.render.sosMobile.RealTimeFeatureServiceLayer.RealTimeRefresher<br>
     * value := org.n52.oxf.layer.IContextLayer (the layer to be refreshed)
     */
    LAYER_REAL_TIME_REFRESH,
    
}