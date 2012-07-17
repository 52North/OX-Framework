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

package org.n52.oxf.ui.swing.wcs;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.wcs.adapter.WCSAdapter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectWCSDialogController {

    private ConnectWCSDialog view;

    private ContentTree tree;
    private MapCanvas map;

    /**
     * 
     * @param view
     * @param map
     * @param tree
     */
    public ConnectWCSDialogController(ConnectWCSDialog view, MapCanvas map, ContentTree tree) {
        this.view = view;

        this.map = map;
        this.tree = tree;
        
        loadURLs();
    }
    
    private void loadURLs() {
        Properties properties = new Properties();
        try {
            properties.load(IServiceAdapter.class.getResourceAsStream("/serviceURLs.properties"));
            
            Enumeration propKeys = properties.keys();
            while (propKeys.hasMoreElements()) {
                String key = (String)propKeys.nextElement();
                
                if (key.substring(0,3) != null && key.substring(0,3).equals("WCS")) {
                    String url = (String)properties.get(key);
                    view.getServiceURLCB().addItem(url);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_getCapabilitiesButton(ActionEvent event) {
        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());
            
            String version = "1.1.1";
            if(view.getVersionList().getSelectedValue() != null) {
                version = (String)view.getVersionList().getSelectedValue();
            }
            WCSAdapter adapter = new WCSAdapter();
            
            ServiceDescriptor serviceDesc = adapter.initService(url.toString());

            Operation getCapsOp = new Operation("GetCapabilities", url.toString(), null);

            ParameterContainer paramCon = new ParameterContainer();

            paramCon.addParameterShell("version", version);

            OperationResult opResult = adapter.doOperation(getCapsOp, paramCon);

            String getCapsDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

            new ShowXMLDocDialog(view.getLocation(), "GetCapabilitites Response", getCapsDoc).setVisible(true);
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
    }

    public void actionPerformed_getCoverageButton(ActionEvent event) {
        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            WCSAdapter adapter = new WCSAdapter();
            
            ServiceDescriptor serviceDesc = adapter.initService(url.toString());
            
            new WCSParameterConfigurator(view, serviceDesc, map, tree).setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}