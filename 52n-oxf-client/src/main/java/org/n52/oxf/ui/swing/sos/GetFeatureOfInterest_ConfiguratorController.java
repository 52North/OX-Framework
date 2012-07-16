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

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.sos.ISOSRequestBuilder;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.adapter.sos.SOSRequestBuilderFactory;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.SosUtil;
import org.n52.oxf.valueDomains.StringValueDomain;

public class GetFeatureOfInterest_ConfiguratorController {

    private GetFeatureOfInterest_Configurator view;

    // ---
    private URL serviceURL;
    private SOSAdapter adapter;

    private ServiceDescriptor serviceDesc;

    /**
     * 
     */
    public GetFeatureOfInterest_ConfiguratorController(GetFeatureOfInterest_Configurator view,
                                                       URL serviceURL,
                                                       SOSAdapter adapter) {
        this.view = view;
        this.serviceURL = serviceURL;
        this.adapter = adapter;

        try {
            this.serviceDesc = adapter.initService(serviceURL.toString());
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

        postInit();
    }

    /**
     * 
     * 
     */
    public void postInit() {

        // init versionComboBox:
        Parameter versionParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST).getParameter(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER);
        StringValueDomain versionVD = (StringValueDomain) versionParameter.getValueDomain();
        for (String version : versionVD.getPossibleValues()) {
            view.getVersionComboBox().addItem(version);
        }

        // init serviceComboBox:
        Parameter serviceParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST).getParameter(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER);
        StringValueDomain serviceVD = (StringValueDomain) serviceParameter.getValueDomain();
        for (String service : serviceVD.getPossibleValues()) {
            view.getServiceComboBox().addItem(service);
        }

        // init featureIdComboBox:
        Parameter featureIdParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST).getParameter(ISOSRequestBuilder.GET_FOI_ID_PARAMETER);
        StringValueDomain featureIdVD = (StringValueDomain) featureIdParameter.getValueDomain();
        for (String featureID : featureIdVD.getPossibleValues()) {
            view.getFeatureIdComboBox().addItem(featureID);
        }
    }

    public void actionPerformed_okButton() {
        try {
            String selectedVersion = (String) view.getVersionComboBox().getSelectedItem();
            String selectedServiceType = (String) view.getServiceComboBox().getSelectedItem();
            String selectedFeatureID = (String) view.getFeatureIdComboBox().getSelectedItem();

            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(SosUtil.SUPPORTED_VERSIONS),
                                                                        ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER),
                                                          selectedVersion));
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedServiceType),
                                                                        ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER),
                                                          selectedServiceType));
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.GET_FOI_ID_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedFeatureID),
                                                                        ISOSRequestBuilder.GET_FOI_ID_PARAMETER),
                                                          selectedFeatureID));

            String postRequest = SOSRequestBuilderFactory.generateRequestBuilder(selectedVersion).buildGetFeatureOfInterestRequest(paramCon);
            int returnVal = new ShowRequestDialog(view, "GetFeatureOfInterest Request", postRequest).showDialog();

            if (returnVal == ShowRequestDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.GET_FEATURE_OF_INTEREST,
                                                                             serviceURL.toString() + "?",
                                                                             serviceURL.toString()),
                                                               paramCon);

                String getFOIDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "GetFeatureOfInterest Response", getFOIDoc).setVisible(true);
            }
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
        catch (ExceptionReport e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_cancelButton() {
        view.dispose();
    }
}