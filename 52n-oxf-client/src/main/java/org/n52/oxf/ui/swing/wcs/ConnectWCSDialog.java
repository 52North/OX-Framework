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

package org.n52.oxf.ui.swing.wcs;

import java.awt.*;
import javax.swing.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;
import javax.swing.JButton;
import java.awt.GridBagConstraints;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ConnectWCSDialog extends JDialog {

    private ConnectWCSDialogController controller;
    
    private JPanel jContentPane = null;
    private JPanel buttonPanel = null;
    private JPanel mainPanel = null;
    private JButton getCapabilitiesButton = null;
    private JButton getCoverageButton = null;
    private JLabel serviceURLLabel = null;
    private JComboBox serviceURLCB = null;

    private JLabel versionLabel = null;

    private JList versionList = null;

    private DefaultListModel defaultListModel = null;  //  @jve:decl-index=0:visual-constraint=""

    private JButton describeCoverageButton = null;

    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 1;
            gridBagConstraints8.gridy = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 2;
            gridBagConstraints5.weightx = 10.0D;
            gridBagConstraints5.weighty = 10.0D;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(0,0,0,0);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 10.0D;
            gridBagConstraints4.weighty = 10.0D;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.NONE;
            gridBagConstraints4.gridx = 0;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getGetCapabilitiesButton(), gridBagConstraints4);
            buttonPanel.add(getGetCoverageButton(), gridBagConstraints5);
            //buttonPanel.add(getDescribeCoverageButton(), gridBagConstraints8);
        }
        return buttonPanel;
    }

    /**
     * This method initializes mainPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints7.gridy = 1;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.weighty = 1.0;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints6.insets = new java.awt.Insets(10,10,10,10);
            gridBagConstraints6.gridy = 1;
            versionLabel = new JLabel();
            versionLabel.setText("WCS Version:");
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 100.0D;
            gridBagConstraints3.weighty = 0.0D;
            gridBagConstraints3.insets = new java.awt.Insets(0,0,0,0);
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new java.awt.Insets(10,10,10,10);
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 0.0D;
            gridBagConstraints2.weighty = 0.0D;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.gridx = 0;
            serviceURLLabel = new JLabel();
            serviceURLLabel.setText("Service-URL:");
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            mainPanel.add(serviceURLLabel, gridBagConstraints2);
            mainPanel.add(getServiceURLCB(), gridBagConstraints3);
            mainPanel.add(versionLabel, gridBagConstraints6);
            mainPanel.add(getVersionList(), gridBagConstraints7);
        }
        return mainPanel;
    }

    /**
     * This method initializes getCapabilitiesButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getGetCapabilitiesButton() {
        if (getCapabilitiesButton == null) {
            getCapabilitiesButton = new JButton();
            getCapabilitiesButton.setText("GetCapabilities");
            getCapabilitiesButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getCapabilitiesButton(e);
                }
            });
        }
        return getCapabilitiesButton;
    }

    /**
     * This method initializes getObservationButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getGetCoverageButton() {
        if (getCoverageButton == null) {
            getCoverageButton = new JButton();
            getCoverageButton.setText("GetCoverage");
            getCoverageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_getCoverageButton(e);
                }
            });
        }
        return getCoverageButton;
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
     * This method initializes versionList	
     * 	
     * @return javax.swing.JList	
     */
    public JList getVersionList() {
        if (versionList == null) {
            versionList = new JList();
            versionList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            versionList.setModel(getDefaultListModel());
            
            ((DefaultListModel)versionList.getModel()).addElement("1.0.0");
            ((DefaultListModel)versionList.getModel()).addElement("1.1.0");
            ((DefaultListModel)versionList.getModel()).addElement("1.1.1");
            
            versionList.setSelectedIndex(2);
        }
        return versionList;
    }

    /**
     * This method initializes defaultListModel	
     * 	
     * @return javax.swing.DefaultListModel	
     */
    private DefaultListModel getDefaultListModel() {
        if (defaultListModel == null) {
            defaultListModel = new DefaultListModel();
        }
        return defaultListModel;
    }

    /**
     * This method initializes describeCoverageButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getDescribeCoverageButton() {
        if (describeCoverageButton == null) {
            describeCoverageButton = new JButton();
            describeCoverageButton.setText("DescribeCoverage");
            describeCoverageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
                }
            });
        }
        return describeCoverageButton;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new ConnectWCSDialog(null, null, null).setVisible(true);

    }

    /**
     * This is the default constructor
     */
    public ConnectWCSDialog(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner);
        
        this.setSize(417, 178);
        this.setLocation(owner.getLocation());
        this.setModal(false);
        this.setTitle("Connect WCS");
        this.setContentPane(getJContentPane());
        
        controller = new ConnectWCSDialogController(this, map, tree);
    }

    
    
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
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
            gridBagConstraints.gridy = 1;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getButtonPanel(), gridBagConstraints);
            jContentPane.add(getMainPanel(), gridBagConstraints1);
        }
        return jContentPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"