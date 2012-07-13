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
package org.n52.oxf.serialization;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.opengis.context.ExtensionType;
import net.opengis.context.LayerListType;
import net.opengis.context.LayerType;
import net.opengis.context.StyleType;
import net.opengis.context.ViewContextType;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.layer.RasterServiceLayer;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.render.IRasterDataRenderer;
import org.n52.oxf.render.OverlayEngine;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class unmarshalls WMC Documents.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public class DeserializationFactory {

    private static Logger LOGGER = LoggingHandler.getLogger(DeserializationFactory.class);

    public LayerContext deserializeLayerContext(InputStream is) throws XmlException,
            IOException,
            ClassNotFoundException,
            InstantiationException,
            IllegalAccessException, OXFException {
        ClassLoader loader = DeserializationFactory.class.getClassLoader();

        ViewContextType xb_context = ViewContextType.Factory.parse(is);

        // Context general:
        String contextTitle = xb_context.getGeneral().getTitle();
        String contextVersion = xb_context.getVersion();
        String contextId = xb_context.getId();
        
        // ContextWindow:
        Integer height = xb_context.getGeneral().getWindow().getHeight().intValue();
        Integer width = xb_context.getGeneral().getWindow().getWidth().intValue();
        ContextWindow window = new ContextWindow(height, width);
        
        // ContextBoundingBox:
        String srs = xb_context.getGeneral().getBoundingBox().getSRS();
        double maxx = xb_context.getGeneral().getBoundingBox().getMaxx().doubleValue();
        double minx = xb_context.getGeneral().getBoundingBox().getMinx().doubleValue();
        double maxy = xb_context.getGeneral().getBoundingBox().getMaxy().doubleValue();
        double miny = xb_context.getGeneral().getBoundingBox().getMiny().doubleValue();
        ContextBoundingBox bbox = new ContextBoundingBox(srs,
                                                         new Point2D.Double(minx, miny),
                                                         new Point2D.Double(maxx, maxy));
        // construct Context:
        LayerContext context = new LayerContext(contextId,
                                                contextTitle,
                                                new OverlayEngine(),
                                                bbox,
                                                window);

        //
        // add Layers:
        //
        
        LayerListType xb_layerList = xb_context.getLayerList();
        for (int i = 0; i < xb_layerList.sizeOfLayerArray(); i++) {
            LayerType xb_layer = xb_layerList.getLayerArray(i);

            String layerTitle = xb_layer.getTitle();
            String layerName = xb_layer.getName();
            boolean hidden = xb_layer.getHidden();
            String format = xb_layer.getFormatList().getFormatArray(0).getStringValue();
            String serviceType = xb_layer.getServer().getService().toString();
            String serviceVersion = xb_layer.getServer().getVersion();
            String serviceTitle = xb_layer.getServer().getTitle();
            String serviceURL = xb_layer.getServer().getOnlineResource().getHref();
            String layerAbstract = xb_layer.getAbstract();
            // String dataURL = xb_layer.getDataURL();
            StyleType[] layerStyles = xb_layer.getStyleList().getStyleArray();

            ParameterContainer paramCon = new ParameterContainer();

            paramCon.addParameterShell(Parameter.COMMON_NAME_SRS, srs);
            paramCon.addParameterShell(Parameter.COMMON_NAME_FORMAT, format);
            paramCon.addParameterShell(Parameter.COMMON_NAME_HEIGHT, height.toString());
            paramCon.addParameterShell(Parameter.COMMON_NAME_WIDTH, width.toString());
            paramCon.addParameterShell(Parameter.COMMON_NAME_RESOURCE_ID, layerName);
            paramCon.addParameterShell(Parameter.COMMON_NAME_VERSION, serviceVersion);

            String[] styleArray = new String[layerStyles.length];
            for (int j=0; j<layerStyles.length; j++) {
                styleArray[j] = layerStyles[j].getName();
            }
            ParameterShell styleParam = new ParameterShell(new Parameter(Parameter.COMMON_NAME_STYLE,
                                                                         false,
                                                                         new StringValueDomain(),
                                                                         Parameter.COMMON_NAME_STYLE),
                                                           styleArray);

            // Parameter.COMMON_NAME_TIME;

            //
            // read out extension data:
            //

            ExtensionType xb_extension = xb_layer.getExtension();
            Node oxf_layerDescription = xb_extension.getDomNode();
            NodeList nodeList = oxf_layerDescription.getChildNodes();
            String layerType = oxf_layerDescription.getLocalName();

            IContextLayer layer = null;

            if (layerType.equals("featureLayer")) {
                IServiceAdapter adapter = null;
                IFeatureDataRenderer renderer = null;
                IFeatureStore featureStore = null;
                IFeaturePicker featurePicker = null;

                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);

                    String className = node.getNodeValue();
                    if (className.equals("serviceConnector")) {
                        Class<IServiceAdapter> classInstance = (Class<IServiceAdapter>) loader.loadClass(className);
                        adapter = classInstance.newInstance();
                    }
                    else if (className.equals("renderer")) {
                        Class<IFeatureDataRenderer> classInstance = (Class<IFeatureDataRenderer>) loader.loadClass(className);
                        renderer = classInstance.newInstance();
                    }
                    else if (className.equals("featureStore")) {
                        Class<IFeatureStore> classInstance = (Class<IFeatureStore>) loader.loadClass(className);
                        featureStore = classInstance.newInstance();
                    }
                    else if (className.equals("featurePicker")) {
                        Class<IFeaturePicker> classInstance = (Class<IFeaturePicker>) loader.loadClass(className);
                        featurePicker = classInstance.newInstance();
                    }
                }
                
                
                ServiceDescriptor descriptor = null; //<-- muss noch gesetzt werden
                String resourceOpName = null; //<-- muss noch gesetzt werden
                layer = new FeatureServiceLayer(adapter,
                                                renderer,
                                                featureStore,
                                                featurePicker,
                                                descriptor,
                                                paramCon,
                                                layerName,
                                                layerTitle,
                                                resourceOpName,
                                                true);
            }
            else if (layerType.equals("rasterLayer")) {
                IServiceAdapter adapter = null;
                IRasterDataRenderer renderer = null;

                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node = nodeList.item(j);

                    String className = node.getNodeValue();
                    if (className.equals("serviceConnector")) {
                        Class<IServiceAdapter> classInstance = (Class<IServiceAdapter>) loader.loadClass(className);
                        adapter = classInstance.newInstance();
                    }
                    else if (className.equals("renderer")) {
                        Class<IRasterDataRenderer> classInstance = (Class<IRasterDataRenderer>) loader.loadClass(className);
                        renderer = classInstance.newInstance();
                    }
                }
                
                ServiceDescriptor descriptor = null; //<-- muss noch gesetzt werden
                String resourceOpName = null; //<-- muss noch gesetzt werden
                layer = new RasterServiceLayer(adapter,
                                               renderer,
                                               descriptor,
                                               paramCon,
                                               layerName,
                                               layerTitle,
                                               resourceOpName);
            }
            else {
                throw new IllegalArgumentException("layerType '" + layerType + "' not supported.");
            }
        }

        return context;
    }

//    /**
//     * creates a LayerContext out of a XML WebMapContext input stream.
//     * 
//     * @throws OXFEventException
//     */
//    public LayerContext deserializeLayerContext(InputStream is, OverlayEngine imageBuilder) throws OXFException,
//            SAXException,
//            IOException,
//            OXFEventException {
//        Document doc = docBuilder.parse(is);
//        Element root = doc.getDocumentElement();
//        String version = root.getAttribute("version");
//        if ( !version.equals(SUPPORTED_VERSION_100) && !version.equals(SUPPORTED_VERSION_110)) {
//            throw new OXFException("WMC version not supported: " + version);
//        }
//        String id = root.getAttribute("id");
//        Element generalElem = (Element) root.getFirstChild();
//        if ( !generalElem.getNodeName().equals("General")) {
//            LOGGER.warn("Context could not be pared, throw exception");
//            throw new OXFException(PARSING_ERROR_TEXT + "General");
//        }
//        Element currentElem = (Element) generalElem.getFirstChild();
//
//        ContextWindow window = null;
//        ContextBoundingBox bbox = null;
//        String ctxTitle = null;
//        if (currentElem.getNodeName().equals("Window")) {
//            if (currentElem != null) {
//                String height = currentElem.getAttribute("width");
//                String width = currentElem.getAttribute("height");
//                window = new ContextWindow(Integer.parseInt(height), Integer.parseInt(width));
//            }
//            currentElem = (Element) currentElem.getNextSibling();
//        }
//        if ( !currentElem.getNodeName().equals("BoundingBox")) {
//            throw new OXFException(PARSING_ERROR_TEXT + "BoundingBox");
//        }
//        String srs = currentElem.getAttribute("SRS");
//        String maxx = currentElem.getAttribute("maxx");
//        String minx = currentElem.getAttribute("minx");
//        String maxy = currentElem.getAttribute("maxy");
//        String miny = currentElem.getAttribute("miny");
//        bbox = new ContextBoundingBox(srs,
//                                      new Point2D.Double(Double.parseDouble(minx),
//                                                         Double.parseDouble(miny)),
//                                      new Point2D.Double(Double.parseDouble(maxx),
//                                                         Double.parseDouble(maxy)));
//
//        currentElem = (Element) currentElem.getNextSibling();
//        if ( !currentElem.getNodeName().equals("Title")) {
//            throw new OXFException(PARSING_ERROR_TEXT + "Title");
//        }
//        ctxTitle = currentElem.getTextContent();
//
//        /*
//         * PARSING THE LAYERLIST OF THE CONTEXT
//         */
//        ArrayList<IContextLayer> layerList = new ArrayList<IContextLayer>();
//        Element layerListElem = (Element) root.getFirstChild().getNextSibling();
//        Element currentLayerElem = (Element) layerListElem.getFirstChild();
//        while (currentLayerElem != null) {
//            boolean hidden = Boolean.parseBoolean(currentLayerElem.getAttribute("hidden"));
//            boolean queryable = Boolean.parseBoolean(currentLayerElem.getAttribute("queryable"));
//            currentElem = (Element) currentLayerElem.getFirstChild();
//            if ( !currentElem.getNodeName().equals("Server")) {
//                throw new OXFException(PARSING_ERROR_TEXT + "Server");
//            }
//            String serviceType = currentElem.getAttribute("service");
//            String serviceVersion = currentElem.getAttribute("version");
//            String serviceTitle = currentElem.getAttribute("title");
//            Element orElem = (Element) currentElem.getFirstChild();
//            if ( !orElem.getNodeName().equals("OnlineResource")) {
//                throw new OXFException(PARSING_ERROR_TEXT + "OnlineResource");
//            }
//            String serviceURL = orElem.getAttributeNS("http://www.w3.org/1999/xlink", "href");
//            if (serviceURL.equals("")) {
//                serviceURL = orElem.getAttribute("href");
//            }
//            currentElem = (Element) currentElem.getNextSibling();
//            if ( !currentElem.getNodeName().equals("Name")) {
//                throw new OXFException(PARSING_ERROR_TEXT + "Name");
//            }
//            String layerName = currentElem.getTextContent();
//            currentElem = (Element) currentElem.getNextSibling();
//            if ( !currentElem.getNodeName().equals("Title")) {
//                throw new OXFException(PARSING_ERROR_TEXT + "Title");
//            }
//            String layerTitle = currentElem.getTextContent();
//            // ABSTRACT
//            currentElem = (Element) currentElem.getNextSibling();
//            if (currentElem.getNodeName().equals("Abstract")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // DATAURL
//            if (currentElem.getNodeName().equals("DataURL")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // MetaDataURL
//            if (currentElem.getNodeName().equals("MetadataURL")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // Min/MaxScale
//            if (currentElem.getNodeName().equals("MinScaleDominator")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            if (currentElem.getNodeName().equals("MaxScaleDominator")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // SRS
//            if (currentElem.getNodeName().equals("SRS")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // FormatList
//            String formatString = null;
//            if (currentElem != null && currentElem.getNodeName().equals("FormatList")) {
//                Element formatElem = (Element) currentElem.getFirstChild();
//
//                while (formatElem != null) {
//                    boolean formatCurrent = Boolean.parseBoolean(formatElem.getAttribute("current"));
//                    if (formatCurrent) {
//                        formatString = formatElem.getTextContent();
//                        break;
//                    }
//                    formatElem = (Element) formatElem.getNextSibling();
//                }
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // StyleList
//            if (currentElem != null && currentElem.getNodeName().equals("StyleList")) {
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            // DimensionList
//            String timeStamp = null;
//            if (currentElem != null && currentElem.getNodeName().equals("DimensionList")) {
//                Element dimensionElem = (Element) currentElem.getFirstChild();
//                while (dimensionElem != null) {
//                    String dimName = dimensionElem.getAttribute("name");
//                    String dimUnit = dimensionElem.getAttribute("unit");
//                    String unitSymbol = dimensionElem.getAttribute("unitSymbol");
//                    String userValue = dimensionElem.getAttribute("userValue");
//                    String dimContent = dimensionElem.getTextContent();
//                    boolean currentDim = Boolean.parseBoolean(dimensionElem.getAttribute("current"));
//                    dimensionElem = (Element) dimensionElem.getNextSibling();
//                }
//                currentElem = (Element) currentElem.getNextSibling();
//            }
//            IServiceAdapter serviceAdapter = null;
//            ServiceDescriptor serviceDesc = null;
//            for (IServiceAdapter adapter : ServiceAdapterFactory.getAvailableServiceAdapters()) {
//                if (adapter.getServiceType().equals(serviceType)) {
//                    serviceAdapter = adapter;
//                }
//            }
//            if (serviceAdapter == null) {
//                LOGGER.warn("no applicable ServiceAdapter could be found for: " + serviceType);
//            }
//            else {
//                LOGGER.info("serviceAdapter " + serviceAdapter.getClass().getName()
//                        + " found for serviceType: " + serviceType);
//                try {
//                    serviceDesc = serviceAdapter.initService(serviceURL);
//                }
//                catch (ExceptionReport rep) {
//                    LOGGER.error("error while initializing serviceDescriptor for type: "
//                            + serviceAdapter.getServiceType());
//                    throw new OXFException("error while initializing serviceDescriptor", rep);
//                }
//            }
//            List<Parameter> parameterList = serviceDesc.getOperationsMetadata().getOperationByName(serviceAdapter.getResourceOperationName()).getParameters();
//            Parameter[] params = new Parameter[parameterList.size()];
//            parameterList.toArray(params);
//            ParameterContainer parameterContainer = new ParameterContainer();
//            for (Parameter p : params) {
//                if (p.getCommonName().equals(Parameter.COMMON_NAME_BBOX)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p,
//                                                                            bbox.asCommonCapabilitiesBoundingBox()));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_HEIGHT)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, window.getHeight()));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_WIDTH)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, window.getWidth()));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_HEIGHT)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, window.getHeight()));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_RESOURCE_ID)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, layerName));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_FORMAT)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, formatString));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_SRS)) {
//                    parameterContainer.addParameterShell(new ParameterShell(p, srs));
//                }
//                else if (p.getCommonName().equals(Parameter.COMMON_NAME_TIME)) {
//                    if (timeStamp != null) {
//                        parameterContainer.addParameterShell(new ParameterShell(p, timeStamp));
//                    }
//                }
//            }
//            RasterServiceLayer layer = new RasterServiceLayer(serviceAdapter,
//                                                              null,
//                                                              serviceDesc,
//                                                              parameterContainer,
//                                                              layerName,
//                                                              layerTitle);
//            currentLayerElem = (Element) currentLayerElem.getNextSibling();
//            layerList.add(layer);
//            LOGGER.info("Layer " + layer.getIDName() + " added");
//        }
//        LayerContext layerContext = new LayerContext(id,
//                                                     ctxTitle,
//                                                     imageBuilder,
//                                                     bbox,
//                                                     window,
//                                                     layerList);
//        return layerContext;
//
//    }
//    
//    /**
//   * This looks for one directly child of the elem.
//   */
//  private Element getSingleElement(String elemName, Element elem) {
//      if (elem.getFirstChild() != null) {
//          Node tempNode = elem.getFirstChild();
//          while (tempNode != null) {
//              if (tempNode.getNodeName().equals(elemName)) {
//                  return (Element) tempNode;
//              }
//              tempNode = tempNode.getNextSibling();
//          }
//      }
//      return null;
//  }
//
//  /**
//   * according to {@link #getSingleElement(String, Element)} it has sometimes to be searched for more than
//   * one Element in the direct child hierarchy.
//   */
//  private ArrayList<Element> getMoreElements(String elemName, Element elem) {
//      if (elem.getFirstChild() != null) {
//          ArrayList<Element> elements = new ArrayList<Element>();
//          Node tempNode = elem.getFirstChild();
//          while (tempNode != null) {
//              if (tempNode.getNodeName().equals(elemName)) {
//                  elements.add((Element) tempNode);
//              }
//              tempNode = tempNode.getNextSibling().getNextSibling();
//          }
//          return elements;
//      }
//      return null;
//  }
    
    
    public static void main(String[] args) throws XmlException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, OXFException {
        File f = new File("C:/temp/MyContext.xml");
        
        FileInputStream fis = new FileInputStream(f);
        DeserializationFactory fac = new DeserializationFactory();
        // fac.validateContext(fis, "1.1.0");
        fac.deserializeLayerContext(fis);
    }
}