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


import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.IBoundingBox;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IVisualization;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a can be used to execute the "download - marshal features - and - render - data"-process.
 * Performance will be increased when this <code>FeatureServiceLayerProcessor</code> will be started
 * "parallel" for all <code>Layer</code>s.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class FeatureServiceLayerProcessor extends LayerProcessor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureServiceLayerProcessor.class);

    private FeatureServiceLayerProcess process;

    boolean caching;

    /**
     * marks whether the process runs for the first time or not.
     */
    boolean firstTimeRun = true;

    /**
     * 
     */
    public FeatureServiceLayerProcessor(FeatureServiceLayer layer, boolean caching) {
        super(layer);

        this.caching = caching;
    }

    @Override
    public FeatureServiceLayerProcess getProcess() {
        if (process == null) {
            process = new FeatureServiceLayerProcess();
        }
        else if (process.getState().equals(Thread.State.TERMINATED)) {
            process = new FeatureServiceLayerProcess();
        }
        return process;
    }

    /**
     * 
     * @return the associated FeatureServiceLayer.
     */
    @Override
    public FeatureServiceLayer getLayer() {
        return (FeatureServiceLayer) layer;
    }

    /**
     * 
     * @throws OXFEventException
     * @throws OXFException
     */
    protected void renderData() throws OXFEventException, OXFException {

        LOGGER.info("Start rendering layer '" + getLayer().getIDName() + "'");

        ParameterShell heightShell = getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_HEIGHT);
        Object heightValue = heightShell.getSpecifiedValue();

        // int height = Integer.parseInt((String) o);
        int height = ((Integer) heightValue).intValue();

        // int width = Integer.parseInt((String) getLayer()
        // .getParameterContainer().getParameterShellWithCommonName(
        // Parameter.COMMON_NAME_WIDTH).getSpecifiedValue());

        Object widthValue = getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_WIDTH).getSpecifiedValue();
        int width = ((Integer) widthValue).intValue();

        IBoundingBox bbox = (IBoundingBox) getLayer().getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

        IFeatureDataRenderer renderer = getLayer().getRenderer();
        IVisualization vis = renderer.renderLayer(getLayer().getFeatureCollection(),
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

    //
    // ------------------ inner-class FeatureServiceLayerProcess
    //
    protected class FeatureServiceLayerProcess extends Thread {

        @Override
        public void run() {

            try {
                if (getLayer().getFeatureCollection() == null) {
                    if (firstTimeRun || caching == false) {
                        
                        initializeDownloadAndUnmarshalFeatureCollection();

                        firstTimeRun = false;
                    }
                }

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

        /**
         * @throws OXFException
         * @throws ExceptionReport
         * 
         */
        protected void initializeDownloadAndUnmarshalFeatureCollection() throws ExceptionReport, OXFException {
            LOGGER.info("Initializing feature collection: building operation... downloading data... unmarshalling features...");
            
            String resourceOpName = getLayer().getResourceOperationName();

            Operation resourceOperation = getLayer().getServiceDescriptor().getOperationsMetadata().getOperationByName(resourceOpName);

            // --- download data:
            OperationResult dataToUnmarshal = getLayer().getServiceAdapter().doOperation(resourceOperation,
                                                                                         getLayer().getParameterContainer());
            
            // --- unmarshal features:
            if(dataToUnmarshal != null){
            	OXFFeatureCollection featureCollection = getLayer().getFeatureStore().unmarshalFeatures(dataToUnmarshal);
            	getLayer().setFeatureCollection(featureCollection);
            }
        }
    }
}