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

package org.n52.oxf.ui.swing.sos;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.serviceAdapters.sos.SOSRequestBuilderFactory;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.SosUtil;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectSOSDialogController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectSOSDialogController.class);

    protected ConnectSOSDialog view;

    protected ContentTree tree;

    protected MapCanvas map;

    /**
     * 
     * @param view
     * @param map
     * @param tree
     */
    public ConnectSOSDialogController(ConnectSOSDialog view, MapCanvas map, ContentTree tree) {
        this.view = view;
        this.map = map;
        this.tree = tree;
    }

    /**
	 * 
	 */
    public void loadURLs() {
        Properties properties = new Properties();
        try {
            properties.load(IServiceAdapter.class.getResourceAsStream("/serviceURLs.properties"));

            Enumeration<Object> propKeys = properties.keys();
            while (propKeys.hasMoreElements()) {
                String key = (String) propKeys.nextElement();

                if (key.substring(0, 3) != null && key.substring(0, 3).equals("SOS")) {
                    String url = (String) properties.get(key);
                    view.getServiceURLCB().addItem(url);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 */
    public void loadVersions() {
        JComboBox cb = view.getServiceVersionCB();
        cb.removeAllItems();

        for (String version : SosUtil.SUPPORTED_VERSIONS) {
            cb.addItem(version);
            cb.setSelectedItem(version);
        }
    }

    /**
     * 
     * @param sosURLs
     */
    public void loadURLs(String[] sosURLs) {
        for (String url : sosURLs)
            view.getServiceURLCB().addItem(url);
    }

    /**
     * 
     * @param event
     */
    public void actionPerformed_getCapabilitiesButton(ActionEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetCapabilities action");
        }

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(new ParameterShell(new Parameter("version",
                                                                        true,
                                                                        new StringValueDomain(SosUtil.SUPPORTED_VERSIONS),
                                                                        "version"),
                                                          view.getServiceVersionCB().getSelectedItem().toString()));
            paramCon.addParameterShell(new ParameterShell(new Parameter("service",
                                                                        true,
                                                                        new StringValueDomain("SOS"),
                                                                        "service"), "SOS"));

            String postRequest = null;

            postRequest = SOSRequestBuilderFactory.generateRequestBuilder(serviceVersion).buildGetCapabilitiesRequest(paramCon);

            int returnVal = new ShowRequestDialog(view, "GetCapabilities Request", postRequest).showDialog();

            if (returnVal == ApprovalDialog.APPROVE_OPTION) {
                SOSAdapter adapter = new SOSAdapter(serviceVersion);
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.GET_CAPABILITIES,
                                                                             url.toString() + "?",
                                                                             url.toString()), paramCon);

                String getCapsDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "GetCapabilitites Response", getCapsDoc).setVisible(true);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
        catch (ExceptionReport e) {
            JOptionPane.showMessageDialog(view,
                                          "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, "The request sended to the SOS produced a service sided exception.\n"
                    + e.getMessage() + "\nPlease view the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void actionPerformed_describeSensorButton(ActionEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DescribeSensor action");
        }

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();
        SOSAdapter adapter = new SOSAdapter(serviceVersion);

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            new DescSensor_Configurator(view, url, adapter).setVisible(true);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_getFeatureOfInterestButton(ActionEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("GetFOI action");
        }

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();
        SOSAdapter adapter = new SOSAdapter(serviceVersion);

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            new AddFOI_Configurator(view, url, adapter, map, tree).setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_produceVisualizeButton(ActionEvent event) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Produce visualization action");
        }

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();

        SOSAdapter adapter = new SOSAdapter(serviceVersion);

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            new ChooseRendererDialog(view, map, tree, url, adapter).setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}