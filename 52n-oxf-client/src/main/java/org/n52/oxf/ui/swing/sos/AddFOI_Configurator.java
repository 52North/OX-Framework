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

import java.awt.*;
import java.net.*;
import javax.swing.*;

import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;
import javax.swing.JButton;
import java.awt.GridBagConstraints;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class AddFOI_Configurator extends JDialog {

    private JPanel jContentPane = null;
    private JLabel offeringLabel = null;
    private JComboBox offeringComboBox = null;
    private JPanel buttonPanel = null;
    private JButton addToMapButton = null;
    private JTextField nameTextField = null;
    private JLabel nameLabel = null;
    private AddFOI_ConfiguratorController controller;
    private JButton cancelButton = null;

    /**
     * This is the default constructor
     * @param adapter 
     */
    public AddFOI_Configurator(JDialog owner, URL serviceURL, SOSAdapter adapter, MapCanvas map, ContentTree tree) {
            super(owner, "Add Features Of Interest to Map");

            controller = new AddFOI_ConfiguratorController(this, serviceURL, adapter, map, tree);
            
            initialize(owner);
            
            controller.postInit();
        }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(380, 150);
        this.setLocation(owner.getLocation());
        this.setPreferredSize(new java.awt.Dimension(150,140));
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
            nameLabel = new JLabel();
            nameLabel.setText("Name:");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(4,4,4,4);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridwidth = 2;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 10.0D;
            gridBagConstraints2.weighty = 0.0D;
            gridBagConstraints2.gridy = 3;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(4,4,4,4);
            gridBagConstraints.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new java.awt.Insets(4,4,4,4);
            gridBagConstraints1.gridy = 1;
            offeringLabel = new JLabel();
            offeringLabel.setText("Choose Offering:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(offeringLabel, gridBagConstraints1);
            jContentPane.add(getOfferingComboBox(), gridBagConstraints);
            jContentPane.add(getButtonPanel(), gridBagConstraints2);
            jContentPane.add(getNameTextField(), gridBagConstraints4);
            jContentPane.add(nameLabel, gridBagConstraints5);
        }
        return jContentPane;
    }

    /**
     * This method initializes offeringComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getOfferingComboBox() {
        if (offeringComboBox == null) {
            offeringComboBox = new JComboBox();
            offeringComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_offeringCB(e);
                }
            });
        }
        return offeringComboBox;
    }

    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new java.awt.Insets(4,4,4,4);
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getAddToMapButton(), gridBagConstraints3);
            buttonPanel.add(getCancelButton(), gridBagConstraints6);
        }
        return buttonPanel;
    }

    /**
     * This method initializes addToMapButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getAddToMapButton() {
        if (addToMapButton == null) {
            addToMapButton = new JButton();
            addToMapButton.setText("ok");
            addToMapButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_addToMapButton();
                }
            });
        }
        return addToMapButton;
    }

    /**
     * This method initializes nameTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getNameTextField() {
        if (nameTextField == null) {
            nameTextField = new JTextField();
            nameTextField.setEnabled(true);
            nameTextField.setEditable(false);
        }
        return nameTextField;
    }

    /**
     * This method initializes cancelButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_cancelButton();
                }
            });
        }
        return cancelButton;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"