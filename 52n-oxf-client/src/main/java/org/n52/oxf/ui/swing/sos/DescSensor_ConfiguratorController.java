/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 15.06.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.serviceAdapters.sos.ISOSRequestBuilder;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.ShowRequestDialog;
import org.n52.oxf.ui.swing.ShowXMLDocDialog;
import org.n52.oxf.util.IOHelper;
import org.n52.oxf.util.LoggingHandler;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class DescSensor_ConfiguratorController {

    private static Logger LOGGER = LoggingHandler.getLogger(DescSensor_ConfiguratorController.class);

    public static String GET_CAPABILITIES_EVENT_TIME_PARAMETER = "eventTime";

    private DescSensor_Configurator view;

    private URL serviceURL;
    private SOSAdapter adapter;

    private ServiceDescriptor serviceDesc;

    /**
     * 
     */
    public DescSensor_ConfiguratorController(DescSensor_Configurator view, URL serviceURL, SOSAdapter adapter) {
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
            LOGGER.error("error initiating service", e);
        }
        catch (OXFException e) {
            if (e.getCause() instanceof ConnectException) {
                JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            LOGGER.error("error initiating service", e);
        }
        catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(view, "The request sended to the SOS produced a service sided exception.\n"
                    + e.getMessage() + "\nPlease view the logs for details.", "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.error("error initiating service", e);
        }

        init();
    }

    /**
     * 
     * 
     */
    public void init() {

        // init versionComboBox:
        Parameter versionParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameter(ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER);
        StringValueDomain versionVD = (StringValueDomain) versionParameter.getValueDomain();
        for (String version : versionVD.getPossibleValues()) {
            view.getVersionComboBox().addItem(version);
        }

        // init serviceComboBox:
        Parameter serviceParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameter(ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER);
        StringValueDomain serviceVD = (StringValueDomain) serviceParameter.getValueDomain();
        for (String service : serviceVD.getPossibleValues()) {
            view.getServiceComboBox().addItem(service);
        }

        // init sensorIdComboBox:
        Parameter sensorIdParameter = serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.DESCRIBE_SENSOR).getParameter(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER);
        StringValueDomain sensorIdVD = (StringValueDomain) sensorIdParameter.getValueDomain();
        for (String sensorID : sensorIdVD.getPossibleValues()) {
            view.getSensorIdComboBox().addItem(sensorID);
        }
    }

    public void actionPerformed_okButton() {
        try {
            String selectedVersion = (String) view.getVersionComboBox().getSelectedItem();
            String selectedServiceType = (String) view.getServiceComboBox().getSelectedItem();
            String selectedSensorID = (String) view.getSensorIdComboBox().getSelectedItem();

            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(SOSAdapter.SUPPORTED_VERSIONS),
                                                                        ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER),
                                                          selectedVersion));
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedServiceType),
                                                                        ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER),
                                                          selectedServiceType));
            paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER,
                                                                        true,
                                                                        new StringValueDomain(selectedSensorID),
                                                                        ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER),
                                                          selectedSensorID));
            paramCon.addParameterShell(ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT,
                                       "text/xml;subtype=\"sensorML/1.0.1\"");

            String postRequest = adapter.getRequestBuilder().buildDescribeSensorRequest(paramCon);
            
            int returnVal = new ShowRequestDialog(view, "DescribeSensor Request", postRequest).showDialog();

            if (returnVal == ApprovalDialog.APPROVE_OPTION) {
                OperationResult opResult = adapter.doOperation(new Operation(SOSAdapter.DESCRIBE_SENSOR,
                                                                             serviceURL.toString() + "?",
                                                                             serviceURL.toString()), paramCon);

                String describeSensorDoc = IOHelper.readText(opResult.getIncomingResultAsStream());

                new ShowXMLDocDialog(view.getLocation(), "DescribeSensor Response", describeSensorDoc).setVisible(true);
            }
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
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed_cancelButton() {
        view.dispose();
    }
}