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
package org.n52.oxf.serialization;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import net.opengis.context.BoundingBoxType;
import net.opengis.context.FormatListType;
import net.opengis.context.FormatType;
import net.opengis.context.LayerType;
import net.opengis.context.OnlineResourceType;
import net.opengis.context.ServerType;
import net.opengis.context.WindowType;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.context.Context;
import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.context.ContextCollection;
import org.n52.oxf.context.ContextWindow;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.layer.AbstractServiceLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.valueDomains.IGenericParameterValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlBeansContextWriter implements ContextWriter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlBeansContextWriter.class);

    private StringBuilder builder = new StringBuilder();
    
    private LayerContext layerContext;
    
    public XmlBeansContextWriter(LayerContext layerContext) {
        this.layerContext = layerContext;
    }

    public void saveContextFile(String filePath) {
        write(layerContext);
        try {
            IOHelper.saveFile(filePath, builder.toString(), false);
            LOGGER.info("Context saved in file: '" + filePath + "'.");
        }
        catch (IOException exc) {
            LOGGER.error("Could not save file.", exc);
        }
    }

    public void write(LayerContext layerToSerialize) {
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        String id = layerToSerialize.getID();
        builder.append("<ViewContext version=\"" + Context.CONTEXT_VERSION + "\" id=\"" + id + "\">");
        builder.append("<General>");
        if (layerToSerialize.getContextWindow() != null) {
            serialize(layerToSerialize.getContextWindow());
        }
        serialize(layerToSerialize.getContextBoundingBox());
        builder.append("<Title>" + layerToSerialize.getTitle() + "</Title>");
        List<String> keywordList = layerToSerialize.getKeywordList();
        if (keywordList != null) {
            builder.append("<KeywordList>");
            for (String keyword : keywordList) {
                builder.append("<Keyword>" + keyword + "</Keyword>");
            }
            builder.append("</KeywordList>");
        }
        String abstractDescription = layerToSerialize.getAbstractDescription();
        if (abstractDescription != null) {
            builder.append("<Abstract>" + abstractDescription + "</Abstract>");
        }
        
        serialize(layerToSerialize.getContextLayers());
        
        builder.append("</General>");
        builder.append("</ViewContext>");
    }

    private void serialize(ContextBoundingBox bboxToSerialize) {
        Rectangle2D actualBBox = bboxToSerialize.getActualBBox();
        BoundingBoxType xb_bbox = BoundingBoxType.Factory.newInstance();
        xb_bbox.setMaxx(new BigDecimal(actualBBox.getMaxX()));
        xb_bbox.setMinx(new BigDecimal(actualBBox.getMinX()));
        xb_bbox.setMaxy(new BigDecimal(actualBBox.getMaxY()));
        xb_bbox.setMiny(new BigDecimal(actualBBox.getMinY()));
        xb_bbox.setSRS(bboxToSerialize.getSRS());
        builder.append(xb_bbox.toString());
    }

    private void serialize(List<IContextLayer> layersToSerialize) {
        builder.append("<LayerList>");
        if (layersToSerialize != null) {
            for (IContextLayer layer : layersToSerialize) {
                if (layer instanceof AbstractServiceLayer) {
                    serialize((AbstractServiceLayer)layer);
                }
            }
        }
        builder.append("</LayerList>");
    }
    
    private void serialize(AbstractServiceLayer layer) {

        LayerType xb_layer = LayerType.Factory.newInstance();
        xb_layer.setHidden(layer.isHidden());
        xb_layer.setQueryable(false);
        xb_layer.setName(layer.getIDName());
        xb_layer.setTitle(layer.getTitle());

        ServerType xb_server = xb_layer.addNewServer();
        ServiceDescriptor serviceDescriptor = layer.getServiceDescriptor();
        xb_server.setVersion(serviceDescriptor.getServiceIdentification().getServiceTypeVersion()[0]);
        //xb_server.setService(net.opengis.context.ServiceType.OGC_WMS);
        xb_server.setService("WMS");
        OnlineResourceType xb_orServer = xb_server.addNewOnlineResource();
        xb_orServer.setHref("???"); // TODO use UNKNOWN URN/URI

        // URLType xb_url = xb_layer.addNewDataURL();
        OnlineResourceType xb_orData = xb_server.addNewOnlineResource();
        // String opName = serviceAdapter.getResourceOperationName();
        // DCP opDCP = serviceDescriptor.getOperationsMetadata().getOperationByName(opName).getDcps()[0];
        // String postHref = opDCP.getHTTPGetRequestMethods().get(0).getOnlineResource().getHref();
        // String getHref = opDCP.getHTTPPostRequestMethods().get(0).getOnlineResource().getHref();
        xb_orData.setHref("");

        ParameterContainer parameterContainer = layer.getParameterContainer();
        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_FORMAT)) {
            FormatListType xb_formatList = xb_layer.addNewFormatList();

            FormatType xb_format = xb_formatList.addNewFormat();
            xb_format.setCurrent(true);
            xb_format.setStringValue((String) parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_FORMAT).getSpecifiedValue());
        }
        // if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_STYLE)) {
        //            
        // }

        if (parameterContainer.containsParameterShellWithCommonName(Parameter.COMMON_NAME_TIME)) {
            builder.append("<DimensionList>");
            builder.append("<Dimension name=\"time\">");
            builder.append(parameterContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_TIME).getSpecifiedValue());
            builder.append("</Dimension>");
        }

        // Setting all the generic Parameters into a GenericParameterList
        boolean firstGenericParam = true;
        boolean hasGenericParams = false;
        // There will be only these parameters serialized which are part of COMMON_NAME_*
        // or instance of IGenericParameterValue
        for (ParameterShell ps : parameterContainer.getParameterShells()) {
            if (ps.getParameter().getCommonName() == null) {
                if (ps.getSpecifiedValue() instanceof IGenericParameterValue) {
                    if (firstGenericParam) {
                        builder.append("<GenericParameterList>");
                        firstGenericParam = false;
                        hasGenericParams = true;
                    }
                    IGenericParameterValue genericParam = (IGenericParameterValue) ps.getSpecifiedValue();
                    genericParam.writeTo(this);
                }
            }
        }
        if (hasGenericParams) {
            builder.append("</GenericParameterList>");
        }
        builder.append("</Layer>");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("serialization finished");
        }
    }

    private void serialize(ContextWindow contextWindow) {
        WindowType xb_window = WindowType.Factory.newInstance();
        xb_window.setHeight(new BigInteger("" + contextWindow.getHeight()));
        xb_window.setWidth(new BigInteger("" + contextWindow.getWidth()));
        builder.append(xb_window.toString());
    }

    
    public void write(ContextCollection contextCollection) {
        // TODO implement serializing ContextCollection
        throw new NotImplementedException();
    }


}