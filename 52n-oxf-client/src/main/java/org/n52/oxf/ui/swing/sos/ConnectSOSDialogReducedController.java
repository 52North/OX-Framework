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
import java.net.URL;

import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.serviceAdapters.sos.SOSAdapter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectSOSDialogReducedController extends ConnectSOSDialogController {

    /**
     * 
     * @param view
     * @param map
     * @param tree
     */
    public ConnectSOSDialogReducedController(ConnectSOSDialogReduced view) {
        super(view, null, null);

        this.view = view;
    }

    /**
     * 
     */
    @Override
    public void actionPerformed_getFeatureOfInterestButton(ActionEvent event) {

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();

        SOSAdapter adapter = new SOSAdapter(serviceVersion);

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            new GetFeatureOfInterest_Configurator(view, url, adapter).setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param event
     */
    public void actionPerformed_getObservationButton(ActionEvent event) {

        String serviceVersion = view.getServiceVersionCB().getSelectedItem().toString();

        SOSAdapter adapter = new SOSAdapter(serviceVersion);

        try {
            URL url = new URL(view.getServiceURLCB().getSelectedItem().toString().trim());

            ServiceDescriptor descriptor = adapter.initService(url.toString());

            // enable the user to build up a GetObservation request for the selectedFeatures:
            GetObs_Configurator getObsDialog = new GetObs_Configurator(view, adapter, descriptor, null, 0, 100, true);

            int returnVal = getObsDialog.showDialog();
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}