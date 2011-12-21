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
 
 Created on: 07.01.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectSOSDialogReduced extends ConnectSOSDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8524569944040909044L;

    protected JButton getObservationButton = null;

    protected ConnectSOSDialogReducedController reducedController;

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    @Override
    protected JPanel getButtonPanel() {
        return super.getButtonPanel();
        //    	
        // if (buttonPanel == null) {
        // GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        // gridBagConstraints5.gridx = 1;
        // gridBagConstraints5.weightx = 0.0D;
        // gridBagConstraints5.weighty = 0.0D;
        // gridBagConstraints5.insets = new java.awt.Insets(0, 0, 0, 0);
        // gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints5.gridy = 1;
        //
        // GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        // gridBagConstraints6.insets = new java.awt.Insets(0, 0, 0, 0);
        // gridBagConstraints6.gridy = 0;
        // gridBagConstraints6.weightx = 0.0D;
        // gridBagConstraints6.weighty = 0.0D;
        // gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints6.gridx = 1;
        //
        // GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        // gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
        // gridBagConstraints4.gridy = 0;
        // gridBagConstraints4.weightx = 0.0D;
        // gridBagConstraints4.weighty = 0.0D;
        // gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints4.gridx = 0;
        //
        // GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        // gridBagConstraints3.insets = new java.awt.Insets(0, 0, 0, 0);
        // gridBagConstraints3.gridy = 1;
        // gridBagConstraints3.weightx = 0.0D;
        // gridBagConstraints3.weighty = 0.0D;
        // gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        // gridBagConstraints3.gridx = 0;
        //
        // buttonPanel = new JPanel();
        // buttonPanel.setLayout(new GridBagLayout());
        // buttonPanel.add(getGetCapabilitiesButton(), gridBagConstraints4);
        // buttonPanel.add(getDescribeSensorButton(), gridBagConstraints6);
        // buttonPanel.add(getGetObservationButton(), gridBagConstraints5);
        // buttonPanel.add(getGetFeatureOfInterestButton(), gridBagConstraints3);
        // }
        // return buttonPanel;
    }

    /**
     * This method initializes visualizeButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getGetObservationButton() {
        if (getObservationButton == null) {
            getObservationButton = new JButton();
            getObservationButton.setText("Execute GetObservation");
            getObservationButton.setPreferredSize(new java.awt.Dimension(300, 26));
            getObservationButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reducedController.actionPerformed_getObservationButton(e);
                }
            });
        }
        return getObservationButton;
    }

    /**
     * This method initializes getFeatureOfInterestButton
     * 
     * @return javax.swing.JButton
     */
    @Override
    protected JButton getGetFeatureOfInterestButton() {
        if (getFeatureOfInterestButton == null) {
            getFeatureOfInterestButton = new JButton();
            getFeatureOfInterestButton.setText("Execute GetFeatureOfInterest");
            getFeatureOfInterestButton.setPreferredSize(new java.awt.Dimension(300, 26));
            getFeatureOfInterestButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    reducedController.actionPerformed_getFeatureOfInterestButton(e);
                }
            });
        }
        return getFeatureOfInterestButton;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new ConnectSOSDialogReduced(new JFrame()).setVisible(true);
    }

    /**
     * This is the default constructor
     */
    public ConnectSOSDialogReduced(JFrame owner) {
        super(owner, null, null);

        initialize(owner);

        reducedController = new ConnectSOSDialogReducedController(this);

        getGetCapabilitiesButton().setText("Execute GetCapabilities");
        getDescribeSensorButton().setText("Execute DescribeSensor");
    }

}