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

import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeatureLayerProcessor extends LayerProcessor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureLayerProcessor.class);
    
    private FeatureLayerProcess process;

    protected FeatureLayerProcessor(FeatureLayer layer) {
        super(layer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.layer.LayerProcessor#getProcess()
     */
    @Override
    public Thread getProcess() {
        if (this.process == null) {
            this.process = new FeatureLayerProcess();
        }
        else if (process.getState().equals(Thread.State.TERMINATED)) {
            process = new FeatureLayerProcess();
        }
        return process;
    }

    /**
     * 
     * @throws OXFEventException
     * @throws OXFException
     */
    public void renderData() throws OXFEventException, OXFException {
        LOGGER.info("Start rendering layer '" + getLayer().getIDName() + "'");

        Object heightValue = getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT).getSpecifiedValue();
        int height = ((Integer) heightValue).intValue();

        Object widthValue = getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue();
        int width = ((Integer) widthValue).intValue();

        IBoundingBox bbox = (IBoundingBox) getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

        IVisualization vis = getLayer().getRenderer().renderLayer(getLayer().getFeatureCollection(),
                                                                  getLayer().getParameterContainer(),
                                                                  width,
                                                                  height,
                                                                  bbox,
                                                                  getLayer().getSelectedFeatures());
        if (vis != null) {
            getLayer().setLayerVisualization(vis);
            eventSupport.fireEvent(EventName.LAYER_VISUALIZATION_READY, vis);
        }
        else {
            throw new OXFException("LayerImage of the layer '" + getLayer().getIDName()
                    + "' could not be rendered successfully.");
        }
    }

    protected class FeatureLayerProcess extends Thread {
        
        private final Logger LOGGER = LoggerFactory.getLogger(FeatureLayerProcessor.FeatureLayerProcess.class);

        @Override
        public void run() {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run!");
            }
            try {
                // --- render data:
                renderData();

                // this line reached --> no exception occured and the layer is
                // not broken
                getLayer().setBroken(false);
            }
            catch (Throwable t) {
                getLayer().setBroken(true);
                throw new OXFRuntimeException(t);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.oxf.layer.LayerProcessor#getLayer()
     */
    @Override
    public FeatureLayer getLayer() {
        return (FeatureLayer) super.getLayer();
    }

}
