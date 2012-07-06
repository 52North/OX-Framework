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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectSOSDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1859575837480975542L;

    private static final int PREFERRED_GET_BUTTON_WIDTH = 300;

    private static final int PREFERRED_GET_BUTTON_HEIGHT = 26;

    protected ConnectSOSDialogController controller;

    protected JPanel jContentPane = null;
    protected JPanel buttonPanel = null;
    protected JPanel mainPanel = null;
    protected JButton getCapabilitiesButton = null;
    protected JButton describeSensorButton = null;
    protected JLabel serviceURLLabel = null;
    protected JComboBox serviceURLCB = null;
    protected JLabel serviceVersionLabel = null;
    protected JLabel isMobileLabel = null;
    protected JComboBox serviceVersionCB = null;
    protected JCheckBox isMobileChB = null;

    protected JButton getFeatureOfInterestButton = null;

    protected JButton visualizeButton = null;

    /**
     * This is the default constructor
     */
    public ConnectSOSDialog(Component owner, MapCanvas map, ContentTree tree) {
        initialize(owner);

        controller = new ConnectSOSDialogController(this, map, tree);

        controller.loadURLs();
        controller.loadVersions();
    }

    /**
     * 
     * @param owner
     * @param map
     * @param tree
     * @param sosURLs
     */
    public ConnectSOSDialog(Component owner, MapCanvas map, ContentTree tree, String[] sosURLs) {
        initialize(owner);

        controller = new ConnectSOSDialogController(this, map, tree);

        controller.loadURLs(sosURLs);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    protected void initialize(Component owner) {
        this.setSize(560, 240);
        this.setTitle("Connect SOS");
        this.setLocation(owner.getLocation());
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    protected JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gbcServiceLabel = new GridBagConstraints();
            gbcServiceLabel.gridx = 0;
            gbcServiceLabel.gridy = 0;
            gbcServiceLabel.weightx = 0.0D;
            gbcServiceLabel.weighty = 0.0D;
            gbcServiceLabel.insets = new java.awt.Insets(10, 10, 10, 10);
            gbcServiceLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;

            GridBagConstraints gbcServiceCB = new GridBagConstraints();
            gbcServiceCB.gridx = 1;
            gbcServiceCB.gridy = 0;
            gbcServiceCB.weightx = 100.0D;
            gbcServiceCB.weighty = 0.0D;
            gbcServiceCB.insets = new java.awt.Insets(0, 0, 0, 0);
            gbcServiceCB.fill = java.awt.GridBagConstraints.HORIZONTAL;

            GridBagConstraints gbcVersionLabel = new GridBagConstraints();
            gbcVersionLabel.gridx = 0;
            gbcVersionLabel.gridy = 1;
            gbcVersionLabel.weightx = 0.0D;
            gbcVersionLabel.weighty = 0.0D;
            gbcVersionLabel.insets = new java.awt.Insets(10, 10, 10, 10);
            gbcVersionLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;

            GridBagConstraints gbcVersionCB = new GridBagConstraints();
            gbcVersionCB.gridx = 1;
            gbcVersionCB.gridy = 1;
            gbcVersionCB.weightx = 100.0D;
            gbcVersionCB.weighty = 0.0D;
            gbcVersionCB.insets = new java.awt.Insets(0, 0, 0, 0);
            gbcVersionCB.fill = java.awt.GridBagConstraints.HORIZONTAL;

            GridBagConstraints gbcIsMobileLabel = new GridBagConstraints();
            gbcIsMobileLabel.gridx = 0;
            gbcIsMobileLabel.gridy = 2;
            gbcIsMobileLabel.weightx = 0.0D;
            gbcIsMobileLabel.weighty = 0.0D;
            gbcIsMobileLabel.insets = new java.awt.Insets(10, 10, 10, 10);
            gbcIsMobileLabel.anchor = java.awt.GridBagConstraints.NORTHWEST;

            GridBagConstraints gbcIsMobileChB = new GridBagConstraints();
            gbcIsMobileChB.gridx = 1;
            gbcIsMobileChB.gridy = 2;
            gbcIsMobileChB.weightx = 100.0D;
            gbcIsMobileChB.weighty = 0.0D;
            gbcIsMobileChB.insets = new java.awt.Insets(0, 0, 0, 0);
            gbcIsMobileChB.anchor = java.awt.GridBagConstraints.WEST;

            serviceURLLabel = new JLabel();
            serviceURLLabel.setText("Service-URL:");
            serviceVersionLabel = new JLabel();
            serviceVersionLabel.setText("Version:");
            isMobileLabel = new JLabel();
            isMobileLabel.setText("Mobile:");
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(serviceURLLabel, gbcServiceLabel);
            mainPanel.add(getServiceURLCB(), gbcServiceCB);
            mainPanel.add(serviceVersionLabel, gbcVersionLabel);
            mainPanel.add(getServiceVersionCB(), gbcVersionCB);
            mainPanel.add(isMobileLabel, gbcIsMobileLabel);
        }
        return mainPanel;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    protected JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.insets = new Insets(10, 0, 0, 0);
            gridBagConstraints41.gridy = 1;
            gridBagConstraints41.weightx = 0.0D;
            gridBagConstraints41.weighty = 0.0D;
            gridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints41.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weighty = 100.0D;
            gridBagConstraints1.weightx = 100.0D;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.weightx = 100.0D;
            gridBagConstraints.weighty = 100.0D;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridheight = 2;
            gridBagConstraints.gridy = 1;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getButtonPanel(), gridBagConstraints);
            jContentPane.add(getMainPanel(), gridBagConstraints1);
        }
        return jContentPane;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.weightx = 0.0D;
            gridBagConstraints5.weighty = 0.0D;
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 0, 0);
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridy = 1;

            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new java.awt.Insets(0, 5, 0, 0);
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 0.0D;
            gridBagConstraints6.weighty = 0.0D;
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridx = 1;

            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 0.0D;
            gridBagConstraints4.weighty = 0.0D;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = 0;

            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new java.awt.Insets(5, 0, 0, 0);
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 0.0D;
            gridBagConstraints3.weighty = 0.0D;
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridx = 0;

            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getGetCapabilitiesButton(), gridBagConstraints4);
            buttonPanel.add(getDescribeSensorButton(), gridBagConstraints6);
            buttonPanel.add(getVisualizeButton(), gridBagConstraints5);
            buttonPanel.add(getGetFeatureOfInterestButton(), gridBagConstraints3);

        }
        return buttonPanel;
    }

    /**
     * This method initializes getCapabilitiesButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getGetCapabilitiesButton() {
        if (getCapabilitiesButton == null) {
            getCapabilitiesButton = new JButton();
            getCapabilitiesButton.setText("Show Capabilities Document");
            getCapabilitiesButton.setPreferredSize(new java.awt.Dimension(PREFERRED_GET_BUTTON_WIDTH,
                                                                          PREFERRED_GET_BUTTON_HEIGHT));
            getCapabilitiesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getCapabilitiesButton(e);
                }
            });
        }
        return getCapabilitiesButton;
    }

    /**
     * This method initializes describeSensorButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getDescribeSensorButton() {
        if (describeSensorButton == null) {
            describeSensorButton = new JButton();
            describeSensorButton.setText("Show DescribeSensor Document");
            describeSensorButton.setPreferredSize(new java.awt.Dimension(PREFERRED_GET_BUTTON_WIDTH,
                                                                         PREFERRED_GET_BUTTON_HEIGHT));
            describeSensorButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_describeSensorButton(e);
                }
            });
        }
        return describeSensorButton;
    }

    /**
     * This method initializes serviceURLCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getServiceURLCB() {
        if (serviceURLCB == null) {
            serviceURLCB = new JComboBox();
            serviceURLCB.setEditable(true);
        }
        return serviceURLCB;
    }

    /**
     * This method initializes serviceVersionCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getServiceVersionCB() {
        if (serviceVersionCB == null) {
            serviceVersionCB = new JComboBox();
            serviceVersionCB.setEditable(true);
        }
        return serviceVersionCB;
    }

    /**
     * This method initializes getFeatureOfInterestButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getGetFeatureOfInterestButton() {
        if (getFeatureOfInterestButton == null) {
            getFeatureOfInterestButton = new JButton();
            getFeatureOfInterestButton.setText("Add Features of Interest to Map");
            getFeatureOfInterestButton.setPreferredSize(new java.awt.Dimension(PREFERRED_GET_BUTTON_WIDTH,
                                                                               PREFERRED_GET_BUTTON_HEIGHT));
            getFeatureOfInterestButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getFeatureOfInterestButton(e);
                }
            });
        }
        return getFeatureOfInterestButton;
    }

    /**
     * This method initializes visualizeButton
     * 
     * @return javax.swing.JButton
     */
    protected JButton getVisualizeButton() {
        if (visualizeButton == null) {
            visualizeButton = new JButton();
            visualizeButton.setText("Produce Visualization Directly");
            visualizeButton.setPreferredSize(new java.awt.Dimension(300, 26));
            visualizeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_produceVisualizeButton(e);
                }
            });
        }
        return visualizeButton;
    }

    /**
     * for testing
     * 
     * @param args
     */
    public static void main(String[] args) {
        new ConnectSOSDialog(new JFrame(), null, null).setVisible(true);
    }

} // @jve:decl-index=0:visual-constraint="10,10"