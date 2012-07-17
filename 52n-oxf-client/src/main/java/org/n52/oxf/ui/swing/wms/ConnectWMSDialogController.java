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

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.wms.adapter.WMSAdapter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectWMSDialogController {
	
	// the threadpool for executing queries
	private ExecutorService threadPool;
	
	// the status of the getCapabilites task
	private Future capabilitiesTask;
	
	// the status of the getMap task
	private Future mapTask;
	
	private ConnectWMSDialog view;

    private ContentTree tree;
    private MapCanvas map;
    
    // Having variables to show that a dependant dialog is open seems to be doing things the hard way,
    // and it is - it is easier to use a modal dialog. But if these dialogs are modal, you can't 
    // easily switch between them - and you can't quickly bring up a getCapabilities 
    // if you can't remember what a layer does - so it works nicely to do it this way.
    
    // true if an XMLDocDialog is open
    private boolean XMLDocDialogOpen = false;
    // true if a WMSParameterConfigurator dialog is open
    private boolean wmsConfDialogOpen = false;
    
    private ShowXMLDocDialog docDialog;
    private WMSParameterConfigurator wmsConfDialog;
    
    // The request to getCapabilities can only be canceled at certain points - 
    // i.e. before the response document starts to be built - there is no point in 
    // canceling after this as the cancellation has no effect and the response is still 
    // displayed. Knowledge of the state of the "getCapabilities request" is essential in managing the
    // user experience.
    /**
     * enumeration to keep track of the state of the getCapabilities request initiated
     * by the ConnectWMSDialog
     */
    public static enum capabilitiesState {
    	NO_ACTIVE_REQUEST("No Active Request"), 
    	INITIALIZING_REQUEST("Initializing Capabilities Request"), 
    	WAITING_FOR_SERVICE("Waiting for Service Response"), 
    	INITIALIZING_RESPONSE_DISPLAY("Response recieved - initializing display"), 
    	DONE("Done");
    	
        private final String message;
        
        capabilitiesState(String message) {
        	this.message = message;
        }
        	
        public String toString() {
            return message;
        } 
    }

    // the state of the request - default to NO_ACTIVE_REQUEST
	private capabilitiesState requestState = capabilitiesState.NO_ACTIVE_REQUEST;
    
    /**
     * 
     * @param view
     * @param map
     * @param tree
     */
    public ConnectWMSDialogController(ConnectWMSDialog view, MapCanvas map, ContentTree tree) {
    	
        this.view = view;
        
        this.map = map;
        this.tree = tree;
        
        // get the background thread needed to execute service queries
        // use 2 threads as only two types of queries may be initiated from the
        // ConnectWMSDialog
        threadPool = Executors.newFixedThreadPool(2);
    }
    

    public void loadURLs(String[] wmsURLS) {
        for (String url : wmsURLS)
            view.getServiceURLCB().addItem(url);
    }
    
    public void loadURLs() {
        Properties properties = new Properties();
        try {
            properties.load(IServiceAdapter.class.getResourceAsStream("/serviceURLs.properties"));
            
            Enumeration propKeys = properties.keys();
            while (propKeys.hasMoreElements()) {
                String key = (String)propKeys.nextElement();
                
                if (key.substring(0,3) != null && key.substring(0,3).equals("WMS")) {
                    String url = (String)properties.get(key);
                    view.getServiceURLCB().addItem(url);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Passes the getCapabilities task to a background thread, and returns a "Future" 
     * object representing the results of that execution.
     */
    public void actionPerformed_getCapabilitiesButton(ActionEvent event) {	
    	capabilitiesTask = threadPool.submit(new getWMSCapabilites());
    }

    public void actionPerformed_getMapButton(ActionEvent event) {
        mapTask = threadPool.submit(new getWMSMap());
    }
    
     
    // When the request window is closed, make sure that the background threads are shutdown cleanly
    public void dialogWindowClosed() {
    	threadPool.shutdownNow();
    }
    
    // used to check whether or not the getCapabilities task has finished executing
    // returns false if there is no background task, or if it is complete (and no result window is open)
    // returns true if a background task is busy executing
    public boolean isGetCapabilitiesTaskBusy() {
    	if ((capabilitiesTask == null) && (!isXMLDocDialogOpen())) { // no task executing and no GetCapabilities result showing
    		return false;
    	}
    	return (!capabilitiesTask.isDone() || isXMLDocDialogOpen()) ;
    }
    /**
     * Checks whether or not there is a getMap task busy executing
     * @return true if a thread is busy executing a getMap task, false otherwise
     */
    public boolean isGetMapTaskBusy() {
    	if ((mapTask == null) && (!isWmsConfDialogOpen())) {
    		return false;
    	}
    	return (!mapTask.isDone() || isWmsConfDialogOpen());
    }
    
    private synchronized void XMLDocDialogClosed() {
    	XMLDocDialogOpen = false;
    	requestState = capabilitiesState.NO_ACTIVE_REQUEST;
    }
    
    // methods to check on / modify the dialog states
    private synchronized boolean isXMLDocDialogOpen() {
    	return XMLDocDialogOpen;
    }
    
    private synchronized void wmsConfDialogOpened() {
    	wmsConfDialogOpen = true;
	}
    
    private synchronized void wmsConfDialogClosed() {
    	wmsConfDialogOpen = false;
    }
    
    private synchronized boolean isWmsConfDialogOpen() {
    	return wmsConfDialogOpen;
    }
    
    private synchronized void XMLDocDialogOpened() {
    	XMLDocDialogOpen = true;
	}
    
    /**
     * Queries the current state of the getCapabilities request
     * @return the current capabilitiesState
     */
    public synchronized capabilitiesState getState() {
    	return requestState;
    }
    
    private synchronized void setCapabilitiesState(capabilitiesState state) {
		this.requestState = state;
	}
    
    /** Inner classes representing the tasks to be done by the background thread i.e.
     * those that involve querying services
     */
    
    
    /**
     * Private class to handle the getCapabilities request and result display
     */
    private class getWMSCapabilites implements Runnable {
		public void run() {
			try {
				setCapabilitiesState(capabilitiesState.INITIALIZING_REQUEST);
				view.setButtonStates();
				
	            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());
	            
	            String version = WMSAdapter.VERSION_111;
	            if(view.getVersionList().getSelectedValue() != null) {
	                version =(String)view.getVersionList().getSelectedValue();
	            }
	            WMSAdapter adapter = new WMSAdapter(version);
	            	          	            
	            Operation getCapsOp = new Operation("GetCapabilities", url.toString(), null);

	            ParameterContainer paramCon = new ParameterContainer();

	            paramCon.addParameterShell(new ParameterShell(new Parameter("version",
	                                                                        true,
	                                                                        new StringValueDomain(new String[]{"1.1.1", "1.1.0", "1.0.0"}),
	                                                                        Parameter.COMMON_NAME_VERSION),
	                                                          version));

	            setCapabilitiesState(capabilitiesState.WAITING_FOR_SERVICE);
	            view.setButtonStates();
	            
	            OperationResult opResult = adapter.doOperation(getCapsOp, paramCon);

	            String getCapsDoc = IOHelper.readText(opResult.getIncomingResultAsStream());
	            
	            setCapabilitiesState(capabilitiesState.INITIALIZING_RESPONSE_DISPLAY);
	            view.setButtonStates();
	            
	            docDialog = new ShowXMLDocDialog(view.getLocation(), "GetCapabilitites Response", getCapsDoc);	     
               	XMLDocDialogOpened();
	           	docDialog.addWindowListener(new XMLWindowListener());
	            docDialog.setVisible(true);
	            
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        catch (OXFException e) {
	            e.printStackTrace();
	        }
	        catch (ExceptionReport e) {
	            e.printStackTrace();
	        }
	        setCapabilitiesState(capabilitiesState.DONE);
        	view.setButtonStates();
		} // end run()   	
    }
    
    /**
     * Private class to handle setting up the getMap request.
     * @author sroos
     *
     */
    private class getWMSMap implements Runnable {
		public void run() {
			try {
	            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

	            String version = WMSAdapter.VERSION_111;
	            if(view.getVersionList().getSelectedValue() != null) {
	                version =(String)view.getVersionList().getSelectedValue();
	            }
	            WMSAdapter adapter = new WMSAdapter(version);
	            
	            // note initService executes a GetCapabilities 
	            ServiceDescriptor serviceDesc = adapter.initService(url.toString());
	            wmsConfDialog = new WMSParameterConfigurator(view, serviceDesc, map, tree);
	            wmsConfDialogOpened();
	            wmsConfDialog.addWindowListener(new WMSConfiguratorListener());
	            wmsConfDialog.setVisible(true);
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        catch (OXFException e) {
	            e.printStackTrace();
	        }
	        catch (ExceptionReport e) {
	            e.printStackTrace();
	        }
	        view.setButtonStates();
		}    	
    }
    
    /**
     * Private class to listen for the ShowXMLDocDialogWindow closing
     * This is necessary as this dialog is non-modal.
     * @author sroos
     *
     */
    private class XMLWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			XMLDocDialogClosed();
			view.setButtonStates();
		}    	
    }
    /**
     * Private class to listen for WMSParameter closing
     * This is necessary as this dialog is non-modal.
     * @author sroos
     *
     */
    private class WMSConfiguratorListener extends WindowAdapter {
    	public void windowClosing(WindowEvent e) {
    		super.windowClosing(e);
    		wmsConfDialogClosed();
    		view.setButtonStates();
    	}
    }
    
}
    