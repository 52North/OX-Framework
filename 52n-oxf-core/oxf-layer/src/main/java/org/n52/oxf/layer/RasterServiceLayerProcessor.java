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