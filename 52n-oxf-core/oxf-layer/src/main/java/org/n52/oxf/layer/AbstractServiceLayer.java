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
package org.n52.oxf.layer;

import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public abstract class AbstractServiceLayer extends AbstractLayer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceLayer.class);

    protected IServiceAdapter serviceAdapter;

    protected ServiceDescriptor serviceDescriptor;

    /**
     * the name of the service Operation providing the data.
     */
    protected String resourceOperationName;

    /**
     * 
     * @param adapter
     * @param serviceRenderer
     * @param descriptor
     * @param parameterContainer
     *        initial ParameterContainer
     * @param layerName
     */
    public AbstractServiceLayer(IServiceAdapter adapter,
                                IRenderer renderer,
                                ServiceDescriptor descriptor,
                                ParameterContainer parameterContainer,
                                String layerName,
                                String layerTitle,
                                String resourceOperationName) {
        super(layerName, layerTitle, parameterContainer, renderer);

        this.serviceAdapter = adapter;
        this.serviceDescriptor = descriptor;
        this.resourceOperationName = resourceOperationName;
    }

    public IServiceAdapter getServiceAdapter() {
        return serviceAdapter;
    }

    public String getResourceOperationName() {
        return resourceOperationName;
    }

    /**
     * 
     * @return
     */
    public ServiceDescriptor getServiceDescriptor() {
        return serviceDescriptor;
    }

    /**
     * 
     */
    public String getLayerSourceType() {
        return getServiceDescriptor().getServiceIdentification().getServiceType();
    }

    /**
     * 
     */
    public String getLayerSourceTitle() {
        return getServiceDescriptor().getServiceIdentification().getTitle();
    }

    /**
     * 
     */
    public IBoundingBox getBBox() {
        if (getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX)) {
            BoundingBox paramBBox = (BoundingBox) getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();
            BoundingBox bBox = new BoundingBox(getSrs(), paramBBox.getLowerCorner(), paramBBox.getUpperCorner());
            return bBox;
        }
        return null;
    }

    /**
     * 
     */
    public String getSrs() {
        if (getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_SRS)) {
            String srs = (String) getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_SRS).getSpecifiedValue();
            return srs;
        }
        return null;
    }
}