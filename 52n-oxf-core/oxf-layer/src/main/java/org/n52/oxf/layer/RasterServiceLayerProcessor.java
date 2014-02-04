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

import org.n52.oxf.*;
import org.n52.oxf.adapter.*;
import org.n52.oxf.ows.capabilities.*;
import org.n52.oxf.render.*;
import org.n52.oxf.util.*;

/**
 * This class is a specialized Thread which can be used to execute the "download - and - render -
 * data"-process. Perfomance will be increased when this <code>RasterServiceLayerProcessor</code> will be
 * started "parallel" for all <code>Layer</code>s.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class RasterServiceLayerProcessor extends LayerProcessor {

    private RasterServiceLayerProcess process;

    /**
     * @param layer
     */
    public RasterServiceLayerProcessor(RasterServiceLayer layer) {
        super(layer);
    }

    public RasterServiceLayer getLayer() {
        return (RasterServiceLayer) layer;
    }

    public RasterServiceLayerProcess getProcess() {
        if (process == null) {
            process = new RasterServiceLayerProcess();
        }
        else if (process.getState().equals(Thread.State.TERMINATED)) {
            process = new RasterServiceLayerProcess();
        }
        return process;
    }

    //
    // ------------------ inner-class FeatureStaticLayerProcess
    //
    protected class RasterServiceLayerProcess extends Thread {

        public void run() {
            try {

                Operation resourceOperation = getLayer().getServiceDescriptor().getOperationsMetadata().getOperationByName(getLayer().getResourceOperationName());

                // --- download data:
                OperationResult opResult = getLayer().getServiceAdapter().doOperation(resourceOperation,
                                                                                      getLayer().getParameterContainer());
                // --- inform any listeners that the result was received
                eventSupport.fireEvent(EventName.OPERATION_RESULT_RECEIVED, opResult);
                // --- render data:
                IRasterDataRenderer renderer = getLayer().getRenderer();
                
                IVisualization vis = renderer.renderLayer(opResult);

                if (vis != null) {
                    getLayer().setLayerVisualization(vis);
                    eventSupport.fireEvent(EventName.LAYER_VISUALIZATION_READY, vis);
                }
                else {
                    throw new OXFException("LayerImage of the layer '" + getLayer().getIDName()
                            + "' could not be rendered successfully.");
                }

                // this line reached --> no exception occured and the layer is not broken
                getLayer().setBroken(false);
            }
            catch (Throwable t) {
                getLayer().setBroken(true);
                throw new OXFRuntimeException(t);
            }
        }
    }
}