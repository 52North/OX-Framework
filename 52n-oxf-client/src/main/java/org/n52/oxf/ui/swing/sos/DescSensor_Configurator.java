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
 
 Created on: 12.03.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.oxf.serviceAdapters.sos.SOSAdapter;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class DescSensor_Configurator extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6092795238845572730L;
    private JPanel jContentPane = null;
    private JLabel sensorIdLabel = null;
    private JComboBox sensorIdComboBox = null;
    private JComboBox serviceComboBox = null;
    private JLabel serviceLabel = null;
    private JComboBox versionComboBox = null;
    private JLabel versionLabel = null;
    private JPanel buttonPanel = null;
    private JButton okButton = null;
    private JButton cancelButton = null;

    protected DescSensor_ConfiguratorController controller;

    /**
     * This is the default constructor
     */
    public DescSensor_Configurator(JDialog owner, URL serviceURL, SOSAdapter adapter) {
        super(owner, "Show Describe Sensor Document");

        initialize(owner);
        controller = new DescSensor_ConfiguratorController(this, serviceURL, adapter);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(JDialog owner) {
        this.setSize(420, 180);
        this.setLocation(owner.getLocation());
        this.setPreferredSize(new java.awt.Dimension(420, 180));
        this.setMinimumSize(new java.awt.Dimension(300, 180));
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.weightx = 10.0D;
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridwidth = 2;
            gridBagConstraints8.gridy = 3;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 2;
            versionLabel = new JLabel();
            versionLabel.setText("Version:");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridy = 2;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridx = 1;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;
            serviceLabel = new JLabel();
            serviceLabel.setText("Service:");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 1;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;
            sensorIdLabel = new JLabel();
            sensorIdLabel.setText("Sensor ID:");
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(sensorIdLabel, gridBagConstraints);
            jContentPane.add(getSensorIdComboBox(), gridBagConstraints1);
            jContentPane.add(getServiceComboBox(), gridBagConstraints2);
            jContentPane.add(serviceLabel, gridBagConstraints3);
            jContentPane.add(getVersionComboBox(), gridBagConstraints4);
            jContentPane.add(versionLabel, gridBagConstraints5);
            jContentPane.add(getButtonPanel(), gridBagConstraints8);
        }
        return jContentPane;
    }

    /**
     * This method initializes sensorIdComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getSensorIdComboBox() {
        if (sensorIdComboBox == null) {
            sensorIdComboBox = new JComboBox();
        }
        return sensorIdComboBox;
    }

    /**
     * This method initializes serviceComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getServiceComboBox() {
        if (serviceComboBox == null) {
            serviceComboBox = new JComboBox();
        }
        return serviceComboBox;
    }

    /**
     * This method initializes versionComboBox
     * 
     * @return javax.swing.JComboBox
     */
    protected JComboBox getVersionComboBox() {
        if (versionComboBox == null) {
            versionComboBox = new JComboBox();
        }
        return versionComboBox;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(4, 4, 4, 4);
            gridBagConstraints7.gridy = 0;
            gridBagConstraints7.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getOkButton(), gridBagConstraints7);
            buttonPanel.add(getCancelButton(), gridBagConstraints8);
        }
        return buttonPanel;
    }

    /**
     * This method initializes okButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText("ok");
            okButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_okButton();
                }
            });
        }
        return okButton;
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
            cancelButton.setMnemonic(java.awt.event.KeyEvent.VK_ESCAPE);
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_cancelButton();
                }
            });
        }
        return cancelButton;
    }

} // @jve:decl-index=0:visual-constraint="10,10"