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

package org.n52.oxf.ui.wms;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javax.media.jai.TiledImage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.adapter.sos.ISOSRequestBuilder;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.render.StaticVisualization;
import org.n52.oxf.render.sos.FeatureGeometryRenderer;
import org.n52.oxf.ui.swing.sos.SOSLayerAdder;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.spatial.BoundingBox2D;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.PNGEncodeParam;

public class GetMapHandler implements IEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetMapHandler.class);

    private WebMapServiceFrontend wms;

    private Image overlayImage;

    public GetMapHandler(WebMapServiceFrontend wms) {
        this.wms = wms;
    }

    public void handleGetMapRequest(HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException,
            ExceptionReport,
            OXFException,
            OXFEventException,
            IOException {
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            LOGGER.info(paramName + " - " + request.getParameter(paramName));
        }

        OverlayEngine imageBuilder = new OverlayEngine();
        imageBuilder.addEventListener(this);

        String[] stringLayers = request.getParameter("LAYERS").split(",");
        String[] stringBBox = request.getParameter("BBOX").split(",");
        String width = request.getParameter("WIDTH");
        String height = request.getParameter("HEIGHT");
        String stringTime = request.getParameter("TIME");
        String crs = request.getParameter("CRS");
        if (crs == null) {
            crs = request.getParameter("SRS");
        }

        // String format = request.getParameter("FORMAT");

        IBoundingBox bBox = new BoundingBox2D(crs,
                                              Double.parseDouble(stringBBox[0]),
                                              Double.parseDouble(stringBBox[1]),
                                              Double.parseDouble(stringBBox[2]),
                                              Double.parseDouble(stringBBox[3]));

        ITime time = null;
        if (stringTime != null) {
            time = TimeFactory.createTime(stringTime);
        }

        LayerContext context = new LayerContext("idX",
                                                "titleX",
                                                imageBuilder,
                                                new ContextBoundingBox(bBox),
                                                new ContextWindow(Integer.parseInt(height),
                                                                  Integer.parseInt(width)));

        List<IContextLayer> contextLayerList = new ArrayList<IContextLayer>();
        for (String layerName : stringLayers) {
            if (wms.getLayerMap().containsKey(layerName)) {
                
                IContextLayer layer = createContextLayer(wms.getLayerMap().get(layerName), time);
                
                contextLayerList.add(layer);
            }
        }

        context.addLayer(contextLayerList);

        OutputStream out = response.getOutputStream();

        if (overlayImage != null) {
            response.setContentType("image/png");

            PNGEncodeParam.RGB ep = (PNGEncodeParam.RGB) PNGEncodeParam.getDefaultEncodeParam((BufferedImage) overlayImage);
            ep.setInterlacing(false);
            int[] rgbTransparent = new int[3];
            rgbTransparent[0] = rgbTransparent[1] = rgbTransparent[2] = 255;
            ep.setTransparentRGB(rgbTransparent);

            ImageEncoder ie = ImageCodec.createImageEncoder("PNG", out, ep);
            TiledImage tile = new TiledImage((BufferedImage) overlayImage, true);
            ie.encode(tile);

            /**
             * PNGEncodeParam.RGB param = new PNGEncodeParam.RGB(); int[] rgb = new int[3]; rgb[0] = rgb[1] =
             * rgb[2] = 0; param.setTransparentRGB(rgb);
             */
        }

        out.flush();
        out.close();
    }

    private IContextLayer createContextLayer(WMSLayer wmsLayer, ITime time) throws ExceptionReport,
            OXFException {

        // TODO: hier m�sste man differenzieren und nicht nur SOS-Layer behandeln:
        ServiceDescriptor serviceDesc = wmsLayer.getServiceAdapter().initService(wmsLayer.getServiceURL());
        ParameterContainer paramCon = wmsLayer.getParameterContainer();

        FeatureServiceLayer contextLayer;
        if (wmsLayer.getRenderer() instanceof FeatureGeometryRenderer) {
            contextLayer = new FeatureServiceLayer(wmsLayer.getServiceAdapter(),
                                                   (IFeatureDataRenderer) wmsLayer.getRenderer(),
                                                   wmsLayer.getFeatureStore(),
                                                   null, // featurePicker
                                                   serviceDesc,
                                                   paramCon,
                                                   wmsLayer.getName(),
                                                   "anyTitle",
                                                   SOSAdapter.GET_FEATURE_OF_INTEREST,
                                                   true);
        }
        else {

            // add TIME parameter to the ParameterContainer
            if (time != null) {
                if (paramCon.containsParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER)) {
                    paramCon.removeParameterShell(paramCon.getParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER));
                }

                Parameter timeParam = new Parameter(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER,
                                                    true,
                                                    new TemporalValueDomain(time),
                                                    ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER);
                paramCon.addParameterShell(new ParameterShell(timeParam, time));
            }
            
            contextLayer = new FeatureServiceLayer(wmsLayer.getServiceAdapter(),
                                                   (IFeatureDataRenderer) wmsLayer.getRenderer(),
                                                   wmsLayer.getFeatureStore(),
                                                   null, // featurePicker
                                                   serviceDesc,
                                                   paramCon,
                                                   wmsLayer.getName(),
                                                   "anyTitle",
                                                   wmsLayer.getServiceAdapter().getResourceOperationName(),
                                                   false);
            
            // use the requested fois as selectedFeatures within the layer:
            ParameterShell foiParam = paramCon.getParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
            
            if (foiParam != null) {
                String[] foiIDs = (String[]) foiParam.getSpecifiedValueArray();
                
                Set<OXFFeature> selectedFeatures = SOSLayerAdder.requestFeatureOfInterestInstances((SOSAdapter)wmsLayer.getServiceAdapter(), serviceDesc, foiIDs);
                contextLayer.setSelectedFeatures(selectedFeatures);
            }
        }

        return contextLayer;
    }

    /**
     * 
     * @param evt
     * @throws OXFEventException
     */
    public void eventCaught(OXFEvent event) throws OXFEventException {

        LOGGER.info("event caught: " + event);

        // reaction on events of the OverlayEngine...
        if (event.getName().equals(EventName.STATIC_OVERLAY_READY)) {
            StaticVisualization visualization = (StaticVisualization) event.getValue();

            Image image = (visualization != null) ? visualization.getRendering() : null;

            overlayImage = image;
        }
    }
}