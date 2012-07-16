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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.ExceptionTransformer;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.ui.wms.configSchema.WmsConfigDocument;
import org.n52.oxf.ui.wms.configSchema.WmsConfigDocument.WmsConfig;
import org.n52.oxf.ui.wms.configSchema.WmsConfigDocument.WmsConfig.Layer;
import org.n52.oxf.ui.wms.configSchema.WmsConfigDocument.WmsConfig.Layer.BoundingBox;
import org.n52.oxf.ui.wms.configSchema.WmsConfigDocument.WmsConfig.Layer.LatLonBoundingBox;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebMapServiceFrontend extends HttpServlet {

    private static final long serialVersionUID = -3570254002654121917L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebMapServiceFrontend.class);
    
    private Map<String, WMSLayer> wmsLayerMap;

    @Override
    public void init() throws ServletException {
        LOGGER.info("init started");

        // get ServletContext
        ServletContext context = getServletContext();

        // get configFile as InputStream
        InputStream configStream = context.getResourceAsStream(getInitParameter("configFile"));

        try {
            wmsLayerMap = readOutLayerList(configStream);
        }
        catch (Exception e) {
            LOGGER.error("Could not read layer list.", e);
        }
        LOGGER.info("init completed");
    }

    private Map<String, WMSLayer> readOutLayerList(InputStream configStream) throws OXFException,
            XmlException,
            IOException,
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {

        Map<String, WMSLayer> layerMap = new HashMap<String, WMSLayer>();

        WmsConfig wmsConfig = WmsConfigDocument.Factory.parse(configStream).getWmsConfig();

        Layer[] layerArray = wmsConfig.getLayerArray();

        for (Layer layer : layerArray) {

            String layerName = layer.getName().newCursor().getTextValue();
            String layerTitle = layer.getTitle().newCursor().getTextValue();
            String serviceURL = layer.getServiceURL().newCursor().getTextValue();

            String abstractDescription = null;
            if (layer.getAbstract() != null) {
                abstractDescription = layer.getAbstract().newCursor().getTextValue();
            }

            org.n52.oxf.valueDomains.spatial.BoundingBox[] bboxArray = null;
            if (layer.getBoundingBoxArray() != null) {
                bboxArray = new org.n52.oxf.valueDomains.spatial.BoundingBox[layer.getBoundingBoxArray().length];
                for (int i = 0; i < layer.getBoundingBoxArray().length; i++) {
                    BoundingBox xml_bbox = layer.getBoundingBoxArray()[i];
                    String srs = xml_bbox.getSRS();
                    double minx = xml_bbox.getMinx();
                    double miny = xml_bbox.getMiny();
                    double maxx = xml_bbox.getMaxx();
                    double maxy = xml_bbox.getMaxy();

                    bboxArray[i] = new org.n52.oxf.valueDomains.spatial.BoundingBox(srs,
                                                                                    new double[] {minx,
                                                                                                  miny},
                                                                                    new double[] {maxx,
                                                                                                  maxy});
                }
            }

            LatLonBoundingBox xml_latLonBBox = layer.getLatLonBoundingBox();
            double minx = xml_latLonBBox.getMinx();
            double miny = xml_latLonBBox.getMiny();
            double maxx = xml_latLonBBox.getMaxx();
            double maxy = xml_latLonBBox.getMaxy();
            org.n52.oxf.valueDomains.spatial.BoundingBox latLonBBox = new org.n52.oxf.valueDomains.spatial.BoundingBox("EPSG:4326",
                                                                                                                       new double[] {minx,
                                                                                                                                     miny},
                                                                                                                       new double[] {maxx,
                                                                                                                                     maxy});

            int opaque = layer.getOpaque() ? 1 : 0;
            
            String[] srsArray = new String[layer.getSRSArray().length];
            for (int i=0; i<layer.getSRSArray().length; i++) {
                XmlObject srsObject = layer.getSRSArray(i);
                srsArray[i] = srsObject.newCursor().getTextValue();
            }
            
            ClassLoader cl = WebMapServiceFrontend.class.getClassLoader();

            Class<IRenderer> rendererClass = (Class<IRenderer>) cl.loadClass(layer.getServiceConnectors().getRenderer().newCursor().getTextValue());
            IRenderer rendererInstance = rendererClass.newInstance();

            Class<IServiceAdapter> serviceAdapterClass = (Class<IServiceAdapter>) cl.loadClass(layer.getServiceConnectors().getServiceAdapter().newCursor().getTextValue());
            IServiceAdapter serviceAdapterInstance = serviceAdapterClass.newInstance();

            Class<IFeatureStore> featureStoreClass = (Class<IFeatureStore>) cl.loadClass(layer.getServiceConnectors().getFeatureStore().newCursor().getTextValue());
            IFeatureStore featureStoreInstance = featureStoreClass.newInstance();

            WmsConfigDocument.WmsConfig.Layer.Parameter[] parameterArray = layer.getParameterArray();

            ParameterContainer paramCon = new ParameterContainer();
            for (WmsConfigDocument.WmsConfig.Layer.Parameter param : parameterArray) {
                String paramName = param.getName();

                XmlObject[] valueArray = param.getValueArray();
                if (valueArray.length == 1) {
                    String valueString = valueArray[0].newCursor().getTextValue();
                    paramCon.addParameterShell(paramName, valueString);
                }
                else if (valueArray.length > 1) {
                    String[] stringValues = new String[valueArray.length];
                    for (int i=0; i<valueArray.length; i++) {
                        XmlObject value = valueArray[i];
                        stringValues[i] = value.newCursor().getTextValue();
                    }
                    Parameter parameter = new Parameter(paramName, true, new StringValueDomain(), paramName);
                    paramCon.addParameterShell(new ParameterShell(parameter, stringValues));
                }
            }

            WMSLayer wmsLayer = new WMSLayer(layerName,
                                             layerTitle,
                                             abstractDescription,
                                             latLonBBox,
                                             bboxArray,
                                             srsArray,
                                             opaque,
                                             serviceAdapterInstance,
                                             rendererInstance,
                                             featureStoreInstance,
                                             paramCon,
                                             serviceURL);
            layerMap.put(wmsLayer.getName(), wmsLayer);

            LOGGER.info("Layer added to layerMap: " + wmsLayer);
        }

        LOGGER.info("Size of layerMap: " + layerMap.size());

        return layerMap;
    }

    /**
     * 
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        LOGGER.info("incoming QueryString: " + request.getQueryString());

        String requestString = request.getParameter("REQUEST");

        try {
            if (requestString.equalsIgnoreCase("GetCapabilities")) {
                GetCapabilitiesHandler getCapabilitiesHandler = new GetCapabilitiesHandler(this);

                getCapabilitiesHandler.handleGetCapabilitiesRequest(request, response);

                LOGGER.info("Capabilities sended");
            }
            else if (requestString.equalsIgnoreCase("GetMap")) {
                GetMapHandler getMapHandler = new GetMapHandler(this);

                getMapHandler.handleGetMapRequest(request, response);
            }
        }
        catch (Exception e) {
            LOGGER.error("Could not handle '{}' GET request", requestString, e);
            
            response.setContentType("text/html");
            OutputStream out = response.getOutputStream();
            
            String htmlExc = ExceptionTransformer.transformExceptionToHTML(e, true);
            
            out.write(htmlExc.getBytes());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ogc.wms_xml");
            out.write(new String("Post not supported").getBytes());
            out.flush();
            out.close();
        }
        catch (Exception e) {
            LOGGER.error("Could not handle POST request.", e);
        }
    }

    public Map<String, WMSLayer> getLayerMap() {
        return wmsLayerMap;
    }

    public static void main(String[] args) throws OXFException,
            XmlException,
            IOException,
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException {
        FileInputStream fileStream = new FileInputStream("D:/Eigene Dateien/_Job/OXFramework/oxf-utilities/conf/wmsConfig.xml");

        new WebMapServiceFrontend().readOutLayerList(fileStream);
    }
}