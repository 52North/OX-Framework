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

import java.awt.event.ItemEvent;
import java.net.ConnectException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.adapter.sos.ISOSRequestBuilder;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.adapter.sos.caps.ObservationOffering;
import org.n52.oxf.feature.FeatureStore;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.sos.FeatureGeometryRenderer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.util.LayerAdder;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class AddFOI_ConfiguratorController {

    private AddFOI_Configurator view;

    private ContentTree tree;
    private MapCanvas map;

    // ---
    private SOSAdapter adapter;

    private ServiceDescriptor serviceDesc;

    /**
     * 
     */
    public AddFOI_ConfiguratorController(AddFOI_Configurator view,
                                         URL serviceURL,
                                         SOSAdapter adapter,
                                         MapCanvas map,
                                         ContentTree tree) {
        this.view = view;
        this.tree = tree;
        this.map = map;
        this.adapter = adapter;

        try {
            serviceDesc = adapter.initService(serviceURL.toString());
        }
        catch (ExceptionReport e) {
            JOptionPane.showMessageDialog(view,
                                          "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    /**
     * 
     * 
     */
    public void postInit() {

        // init offeringCB:
        for (int i = 0; i < serviceDesc.getContents().getDataIdentificationCount(); i++) {
            view.getOfferingComboBox().addItem(serviceDesc.getContents().getDataIdentification(i));
        }
    }

    /**
     * 
     * @param e
     */
    public void itemStateChanged_offeringCB(ItemEvent e) {
        initOffering((ObservationOffering) view.getOfferingComboBox().getSelectedItem());
    }

    /**
     * 
     * @param offering
     */
    public void initOffering(org.n52.oxf.adapter.sos.caps.ObservationOffering offering) {
        // clear input elements:
        view.getNameTextField().setText("");

        // init observedPropertyList:
        view.getNameTextField().setText(offering.getTitle());
    }

    /**
     * 
     * 
     */
    public void actionPerformed_addToMapButton() {

        try {
            ObservationOffering offering = (ObservationOffering) view.getOfferingComboBox().getSelectedItem();
            
            
            // the paramCon contains the parameters used to execute a GetFeatureOfInterest-request
            ParameterContainer paramCon = new ParameterContainer();
            
            paramCon.addParameterShell("version", adapter.getServiceVersion());
            paramCon.addParameterShell("service", "SOS");

            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.GET_FOI_ID_PARAMETER,
                                                                        false,
                                                                        new StringValueDomain(offering.getFeatureOfInterest()),
                                                                        null),
                                                          offering.getFeatureOfInterest()));
            
            // add a BoundingBox-parameter to the paramCon, because it is needed by the rendering process:
            paramCon.addParameterShell(new ParameterShell(new Parameter(Parameter.COMMON_NAME_BBOX,
                                                                        true,
                                                                        offering.getBoundingBoxes()[0],
                                                                        Parameter.COMMON_NAME_BBOX),
                                                          offering.getBoundingBoxes()[0]));
            
            String layerId = offering.getIdentifier();
            String layerTitle = offering.getTitle();

            FeatureGeometryRenderer renderer = new FeatureGeometryRenderer();
            FeatureServiceLayer layer = new FeatureServiceLayer(adapter,
                                                                renderer,
                                                                new FeatureStore(),
                                                                renderer,
                                                                serviceDesc,
                                                                paramCon,
                                                                layerId,
                                                                layerTitle,
                                                                SOSAdapter.GET_FEATURE_OF_INTEREST,
                                                                true);
            LayerAdder.addLayer(map, tree, layer);
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
        catch (OXFEventException e) {
            e.printStackTrace();
        }

        view.dispose();
    }

    /**
     * 
     * 
     */
    public void actionPerformed_cancelButton() {
        view.dispose();
    }
}