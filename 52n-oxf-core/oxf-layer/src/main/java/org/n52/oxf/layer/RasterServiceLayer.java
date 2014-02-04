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

import org.n52.oxf.adapter.*;
import org.n52.oxf.ows.*;
import org.n52.oxf.ows.capabilities.*;
import org.n52.oxf.render.*;
import org.n52.oxf.serialization.ContextWriter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class RasterServiceLayer extends AbstractServiceLayer {

    protected RasterServiceLayerProcessor processor;

    /**
     * @param adapter
     * @param rasterRenderer
     * @param descriptor
     * @param parameterContainer
     * @param layerName
     * @param layerTitle
     */
    public RasterServiceLayer(IServiceAdapter adapter,
                              IRasterDataRenderer rasterRenderer,
                              ServiceDescriptor descriptor,
                              ParameterContainer parameterContainer,
                              String layerName,
                              String layerTitle,
                              String resourceOperationName) {
        super(adapter, rasterRenderer, descriptor, parameterContainer, layerName, layerTitle, resourceOperationName);

        this.processor = new RasterServiceLayerProcessor(this);
    }

    /**
     * 
     */
    @Override
    public IRasterDataRenderer getRenderer() {
        return (IRasterDataRenderer) renderer;
    }

    /**
     * 
     * @return a RasterServiceLayerProcessor which can be used to execute the "download - and - render -
     *         data"-process in a separate <code>Thread</code>.
     */
    public RasterServiceLayerProcessor getProcessor() {
        return processor;
    }
}