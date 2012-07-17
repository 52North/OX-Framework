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

package org.n52.oxf.ui.swing.wms;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.*;

import org.n52.oxf.*;
import org.n52.oxf.adapter.*;
import org.n52.oxf.adapter.wms.*;
import org.n52.oxf.adapter.wms.caps.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.render.wms.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tool.ZoomToActiveLayerTool;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.ui.swing.util.*;
import org.n52.oxf.util.*;

public class WMSParameterConfiguratorController implements IEventListener {

    private WMSParameterConfigurator view;
    private ServiceDescriptor serviceDesc;
    private MapCanvas map;
    private ContentTree tree;
    private String version;
    
    // the layer that is being processed / requested by this class at any time
    private RasterServiceLayer currentLayer = null;
    private LayerStatus layerStatus = null;
    
    // This will contain one thread  - we only process one query at a time
    private ExecutorService processRequestThreadPool;
    private boolean mapRequestBusy = false;
    
    public WMSParameterConfiguratorController(WMSParameterConfigurator view,
                                              ServiceDescriptor serviceDesc,
                                              MapCanvas map,
                                              ContentTree tree) {
        this.view = view;
        this.serviceDesc = serviceDesc;
        this.map = map;
        this.tree = tree;
        
        processRequestThreadPool = Executors.newSingleThreadExecutor();

        this.version = serviceDesc.getServiceIdentification().getServiceTypeVersion()[0];
    }

    /**
     * 
     * 
     */
    public void postInit() {

        // init layersCB:
        for (int i = 0; i < serviceDesc.getContents().getDataIdentificationCount(); i++) {
            view.getLayersCB().addItem(serviceDesc.getContents().getDataIdentification(i));
        }
    }

    public void actionPerformed_showRequestButton(ActionEvent event) {
    	setMapRequestBusy(true);
    	// is there a suitable BBox:
        if (view.getBBoxPanel().getSrsCB().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                                          "This Layer supports no suitable BBox/SRS for the actual ContextBoundingBox.");
        }
        else {
                /*
                 * SHOW the GetMap-request, then in a background thread, 
                 *  DO the GetMap-operation and ADD the layer to the map:
                 */           
        	try {
	            Operation getMapOp = serviceDesc.getOperationsMetadata().getOperationByName(WMSAdapter.OPERATION_GETMAP);
	
	            ParameterContainer paramCon = getFilledParameterContainer(getMapOp);
	
	            String request = new WMSRequestBuilder().buildGetMapRequest(getMapOp, paramCon);
	
	            int returnVal = new ShowGetMapRequestDialog(view, request).showDialog();
	
	            if (returnVal == ShowGetMapRequestDialog.APPROVE_OPTION) {
	                WMSAdapter adapter = new WMSAdapter(version);
	
	                String layerId = ((Dataset) view.getLayersCB().getSelectedItem()).getIdentifier();
	                String layerTitle = serviceDesc.getContents().getDataIdentification(layerId).getTitle();
	
	                final RasterServiceLayer layer = new RasterServiceLayer(adapter,
	                                                                  new WMSRenderer(),
	                                                                  serviceDesc,
	                                                                  paramCon,
	                                                                  layerId,
	                                                                  layerTitle,
	                                                                  adapter.OPERATION_GETMAP);
	                currentLayer = layer;
	                layerStatus = new LayerStatus(currentLayer.getIDName(), LayerStatus.STATUS_REQUEST_INITIATED);
	                view.setLayerStatusText(layerStatus.getLayerName(), layerStatus.getLayerStatus());
	                // add a listener to this layer processor to be informed about progress
	                currentLayer.getProcessor().addEventListener(this);
	                map.getLayerContext().addEventListener(this);
	                // This call triggers a new thread which handles downloading and display
	                // of information - but it seems to block until that process is finished - so I'm
	                // putting it in a processing thread
	                Runnable doAddLayer = new Runnable() {
	                	public void run() {
	                		try {
	                			
	                		    LayerAdder.addLayer(map, tree, layer);
	                			
	                            ZoomToActiveLayerTool.zoomToActiveLayer(map, tree);
	                            
	                		} catch (OXFException e) {
	                			e.printStackTrace();
	                		} catch (OXFEventException e) {
	                			e.printStackTrace();
	                		}
	                	}
	                }; // end defination doAddLayer
	                processRequestThreadPool.execute(doAddLayer);
	            } // endif (returnVal== .... 	                  
			} catch (OXFException ex) {
				   ex.printStackTrace();
			} 
			finally {
				setMapRequestBusy(false);
				view.updateRequestStatus();
			}           
        } // end else
    }

    /**
     * 
     * @param event
     */
    public void itemStateChanged_layersCB(ItemEvent event) {
        initLayer((WMSLayer) view.getLayersCB().getSelectedItem());
    }

    /**
     * 
     * @param offering
     */
    public void initLayer(WMSLayer wmsLayer) {

        // clear input elements:

        ((DefaultListModel) view.getStylesList().getModel()).clear();

        view.getFormatCB().removeAllItems();

        view.removeBBoxPanel();

        view.getVersionTextField().setText("");

        // init versionTextField:
        view.getVersionTextField().setText(version);

        // init formatCB:
        String[] formats = wmsLayer.getOutputFormats();
        for (int i = 0; i < formats.length; i++) {
            view.getFormatCB().addItem(formats[i]);
        }

        // init stylesList: dataID.getIdentifier());
        Style[] styleArray = wmsLayer.getStyles();
        if (styleArray != null || styleArray.length > 0) {
            for (Style style : styleArray) {
                ((DefaultListModel) view.getStylesList().getModel()).addElement(style);
            }
            view.getStylesList().setSelectedIndex(0);
        }

        // init bboxPanel:
        if (wmsLayer.getBoundingBoxes() != null) {
            IBoundingBox[] bboxes = wmsLayer.getBoundingBoxes();

            view.addBBoxPanel(new BBoxSelectionPanel(wmsLayer.getAvailableCRSs(), bboxes, map));
        }
    }

    /**
     * 
     * @param getObsOp
     * @return
     * @throws OXFException
     */
    public ParameterContainer getFilledParameterContainer(Operation getObsOp) throws OXFException {

        ParameterContainer paramCon = new ParameterContainer();

        //
        // required parameters:
        //

        // bbox:
        IBoundingBox chosenBBox = view.getBBoxPanel().getChosenBBox();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("BBOX"), chosenBBox));

        // srs:
        String srs = chosenBBox.getCRS();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("SRS"), srs));

        // version:
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("VERSION"), version));

        // layers:
        String dataID = ((Dataset) view.getLayersCB().getSelectedItem()).getIdentifier();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("LAYERS"), dataID));

        // format:
        String resultFormat = (String) view.getFormatCB().getSelectedItem();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("FORMAT"), resultFormat));

        // styles
        if (view.getStylesList().getSelectedValue() != null) {
            Style style = (Style) view.getStylesList().getSelectedValue();

            paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("STYLES"),
                                                          style.getName()));
        }

        // height:
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("HEIGHT"),
                                                      map.getHeight()));

        // width:
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("WIDTH"),
                                                      map.getWidth()));

        //
        // optional parameters:
        //

        // ---

        return paramCon;

    }
    
    // When the request window is closed, make sure that the background threads are shutdown cleanly
    public void dialogWindowClosed() {
    	// note:  tries to force running threads to be interrupted
     	processRequestThreadPool.shutdownNow();
    }
    
    public void eventCaught(OXFEvent evt) throws OXFEventException {
    	System.out.println("event caught: " + evt);
    	if (evt.getName().equals(EventName.NEW_LAYER_ADDED)) {
    		IContextLayer layer = (IContextLayer)evt.getValue(); 
    		if (layer.equals(currentLayer)) {
    			layerStatus.setLayerStatus(LayerStatus.STATUS_NEW_LAYER_ADDED);
    			view.setLayerStatusText(currentLayer.getIDName(), LayerStatus.STATUS_NEW_LAYER_ADDED); 
    		}
    	} else if (evt.getName().equals(EventName.OPERATION_RESULT_RECEIVED)) {
    		OperationResult result = (OperationResult)evt.getValue();
    		String layerID = (String) result.getUsedParameters().getParameterShellWithCommonName(Parameter.COMMON_NAME_RESOURCE_ID).getSpecifiedValue();
    		if (layerID.equals(currentLayer.getIDName())) {
    			layerStatus.setLayerStatus(LayerStatus.STATUS_OPERATION_RESULT_RECEIVED);
    			view.setLayerStatusText(layerID, LayerStatus.STATUS_OPERATION_RESULT_RECEIVED);
    		}
    	} else if (evt.getName().equals(EventName.LAYER_VISUALIZATION_READY)) {
    		IContextLayer layer = ((LayerProcessor)evt.getSource()).getLayer();
    		if (layer.equals(currentLayer)) {
    			layerStatus.setLayerStatus(LayerStatus.STATUS_LAYER_VISUALIZATION_READY);
    			view.setLayerStatusText(layer.getIDName(), LayerStatus.STATUS_LAYER_VISUALIZATION_READY);
    		}
    	} else if (evt.getName().equals(EventName.ALL_LAYERS_READY_TO_OVERLAY)) {
    		ArrayList layerList = (ArrayList)evt.getValue();
      		for (Object layer : layerList) {
    			if (((IContextLayer)layer).equals(currentLayer)) {
    				layerStatus.setLayerStatus(LayerStatus.STATUS_READY_TO_OVERLAY);
     				view.setLayerStatusText(currentLayer.getIDName(), LayerStatus.STATUS_READY_TO_OVERLAY);
    				break;
    			}
    		}
      		if (currentLayer.isBroken()) {
      			layerStatus.setLayerStatus(LayerStatus.STATUS_READY_TO_OVERLAY);
      			view.setLayerStatusText(currentLayer.getIDName(), LayerStatus.STATUS_READY_TO_OVERLAY);
      		}
    	}
	}
    
    /**
     * Checks whether or not a request initiated by a getMap action event is
     * still being processed.
     * @return true if the layer request has not finished rendering
     */
    public synchronized boolean isMapRequestStillBusy() {
    	if (layerStatus != null) {
    		if (layerStatus.getLayerStatus().equals(LayerStatus.STATUS_NO_REQUEST) ||
    				layerStatus.getLayerStatus().equals(LayerStatus.STATUS_READY_TO_OVERLAY)) {
    			return (mapRequestBusy);
    		} else {
    			return true;
    		}
    	}
    	return (mapRequestBusy);
    } 
    

	public synchronized void setMapRequestBusy(boolean mapRequestBusy) {
		this.mapRequestBusy = mapRequestBusy;
	}
    


}