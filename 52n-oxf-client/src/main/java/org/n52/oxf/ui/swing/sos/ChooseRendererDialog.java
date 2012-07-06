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

import org.n52.oxf.serviceAdapters.sos.SOSAdapter;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;

public class ChooseRendererDialog extends JDialog {

    private JPanel jContentPane = null;
    private JTextField urlTextField = null;
    private JLabel urlLabel = null;
    private JPanel buttonPanel = null;
    private JButton selectButton = null;
    private JLabel rendererLabel = null;
    private JScrollPane rendererScrollPane = null;
    private JList rendererList = null;
    private JButton cancelButton = null;
    private JButton pluginRenderButton = null;
    protected ChooseRendererDialogController controller;
    private DefaultListModel defaultListModel = null;  //  @jve:decl-index=0:visual-constraint=""
    /**
     * This method initializes urlTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getUrlTextField() {
        if (urlTextField == null) {
            urlTextField = new JTextField();
            urlTextField.setEditable(false);
        }
        return urlTextField;
    }

    /**
     * This method initializes buttonPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 2;
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.gridx = 1;
            buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridBagLayout());
            buttonPanel.add(getSelectButton(), gridBagConstraints5);
            buttonPanel.add(getCancelButton(), gridBagConstraints4);
            buttonPanel.add(getPluginRenderButton(), gridBagConstraints2);
        }
        return buttonPanel;
    }

    /**
     * This method initializes selectButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getSelectButton() {
        if (selectButton == null) {
            selectButton = new JButton();
            selectButton.setText("Select Renderer");
            selectButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.selectButton_actionPerformed();
                }
            });
        }
        return selectButton;
    }

    /**
     * This method initializes rendererScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getRendererScrollPane() {
        if (rendererScrollPane == null) {
            rendererScrollPane = new JScrollPane();
            rendererScrollPane.setViewportView(getRendererList());
        }
        return rendererScrollPane;
    }

    /**
     * This method initializes rendererList	
     * 	
     * @return javax.swing.JList	
     */
    public JList getRendererList() {
        if (rendererList == null) {
            rendererList = new JList();
            rendererList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            rendererList.setModel(getDefaultListModel());
        }
        return rendererList;
    }

    /**
     * This method initializes cancelButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.cancelButton_actionPerformed();
                }
            });
        }
        return cancelButton;
    }

    /**
     * This method initializes pluginRenderButton	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getPluginRenderButton() {
        if (pluginRenderButton == null) {
            pluginRenderButton = new JButton();
            pluginRenderButton.setText("Plugin new Renderer");
            pluginRenderButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.pluginRendererButton_actionPerformed();
                }
            });
        }
        return pluginRenderButton;
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
     * This is the default constructor
     * @param adapter 
     */
    public ChooseRendererDialog(Component owner, MapCanvas map, ContentTree tree, URL sosUrl, SOSAdapter adapter) {
        initialize(owner);
        
        controller = new ChooseRendererDialogController(this, map, tree, sosUrl, adapter);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(Component owner) {
        this.setSize(524, 259);
        this.setLocation(owner.getLocation());
        this.setTitle("Choose SOS-Renderer");
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 10.0D;
            gridBagConstraints3.weighty = 10.0D;
            gridBagConstraints3.insets = new java.awt.Insets(0,0,10,10);
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.insets = new java.awt.Insets(10,10,10,10);
            gridBagConstraints21.weightx = 0.0D;
            gridBagConstraints21.weighty = 0.0D;
            gridBagConstraints21.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints21.gridy = 1;
            rendererLabel = new JLabel();
            rendererLabel.setText("Choose Renderer:");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridwidth = 2;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 2;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new java.awt.Insets(10,10,10,10);
            gridBagConstraints1.weightx = 0.0D;
            gridBagConstraints1.weighty = 0.0D;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints1.gridy = 0;
            urlLabel = new JLabel();
            urlLabel.setText("SOS URL:");
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(10,0,0,10);
            gridBagConstraints.weighty = 1.0D;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints.gridx = 1;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getUrlTextField(), gridBagConstraints);
            jContentPane.add(urlLabel, gridBagConstraints1);
            jContentPane.add(getButtonPanel(), gridBagConstraints11);
            jContentPane.add(rendererLabel, gridBagConstraints21);
            jContentPane.add(getRendererScrollPane(), gridBagConstraints3);
        }
        return jContentPane;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"