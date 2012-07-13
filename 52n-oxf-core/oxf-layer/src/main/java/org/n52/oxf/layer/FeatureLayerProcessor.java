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

package org.n52.oxf.layer;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.util.OXFEventException;

/**
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class FeatureLayerProcessor extends LayerProcessor {
    
    private static Logger LOGGER = LoggingHandler.getLogger(FeatureLayerProcessor.class);

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

        private Logger LOG = LoggingHandler.getLogger(FeatureLayerProcess.class);

        @Override
        public void run() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("run!");
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
