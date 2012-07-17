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

package org.n52.oxf.ui.swing.wms;

import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.n52.oxf.ows.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WMSParameterConfigurator extends JDialog {

    private WMSParameterConfiguratorController controller;

    private JPanel jContentPane = null;
    private JPanel requiredParametersPanel = null;
    private JPanel buttonPanel = null;
    private JButton showRequestButton = null;
    private JLabel layersLabel = null;
    private JComboBox layersCB = null;
    private JScrollPane observedPropertySP = null;
    private JList stylesList = null;
    private JLabel layerStatus = null;
    private JLabel statusLabel = null;
    private JProgressBar progressBar = null;
    private int progressLength = 10;
    private JLabel formatLabel = null;
    private JComboBox formatCB = null;
    private JPanel optionalParametersPanel = null;
    private JPanel statusPanel = null;
    private DefaultListModel defaultListModel = null; // @jve:decl-index=0:visual-constraint=""

    private JLabel styleLabel = null;

    private JLabel timeLabel = null;

    private JTextField timeTextField = null;

    private JLabel bboxLabel = null;

    private BBoxSelectionPanel bboxPanel = null;

    private JLabel versionLabel = null;

    private JTextField versionTextField = null;

    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (requiredParametersPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 3;
            gridBagConstraints13.weightx = 1.0;
            gridBagConstraints13.gridx = 1;
            gridBagConstraints13.insets = new Insets(5,0,5,0);
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints12.gridy = 3;
            gridBagConstraints12.insets = new Insets(5,0,5,0);
            versionLabel = new JLabel();
            versionLabel.setText("WMS-Version: ");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints10.gridy = 4;
            bboxLabel = new JLabel();
            bboxLabel.setText("BoundingBox:");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.insets = new Insets(5,0,0,0);
            styleLabel = new JLabel();
            styleLabel.setText("Style:");
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 2;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.gridx = 1;
            gridBagConstraints7.insets = new Insets(5,0,0,0);
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints6.gridy = 2;
            gridBagConstraints6.insets = new Insets(5,0,0,0);
            formatLabel = new JLabel();
            formatLabel.setText("Format:");
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.weightx = 100.0D;
            gridBagConstraints5.weighty = 10.0D;
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.insets = new Insets(5,0,0,0);
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 100.0D;
            gridBagConstraints3.weighty = 0.0D;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints2.gridy = 0;
            layersLabel = new JLabel();
            layersLabel.setText("Layer:");
            requiredParametersPanel = new JPanel();
            requiredParametersPanel.setLayout(new GridBagLayout());
            requiredParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                                                                                           "Required Parameters",
                                                                                           javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                           javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                           null,
                                                                                           null));
            requiredParametersPanel.add(layersLabel, gridBagConstraints2);
            requiredParametersPanel.add(styleLabel, gridBagConstraints4);
            requiredParametersPanel.add(getLayersCB(), gridBagConstraints3);
            requiredParametersPanel.add(getObservedPropertySP(), gridBagConstraints5);
            requiredParametersPanel.add(formatLabel, gridBagConstraints6);
            requiredParametersPanel.add(getFormatCB(), gridBagConstraints7);
            requiredParametersPanel.add(bboxLabel, gridBagConstraints10);
            requiredParametersPanel.add(versionLabel, gridBagConstraints12);
            requiredParametersPanel.add(getVersionTextField(), gridBagConstraints13);
        }
        return requiredParametersPanel;
    }

    /**
     * This method initializes buttonPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getShowRequestButton(), null);
        }
        return buttonPanel;
    }

    /**
     * This method initializes showRequestButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getShowRequestButton() {
        if (showRequestButton == null) {
            showRequestButton = new JButton();
            showRequestButton.setText("Show Request");
            showRequestButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_showRequestButton(e);
                    updateRequestStatus();
                }
            });
        }
        return showRequestButton;
    }

    /**
     * This method initializes layersCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getLayersCB() {
        if (layersCB == null) {
            layersCB = new JComboBox();
            layersCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_layersCB(e);
                }
            });
        }
        return layersCB;
    }

    /**
     * This method initializes observedPropertySP
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getObservedPropertySP() {
        if (observedPropertySP == null) {
            observedPropertySP = new JScrollPane();
            observedPropertySP.setPreferredSize(new java.awt.Dimension(0, 50));
            observedPropertySP.setViewportView(getStylesList());
        }
        return observedPropertySP;
    }

    /**
     * This method initializes stylesList
     * 
     * @return javax.swing.JList
     */
    public JList getStylesList() {
        if (stylesList == null) {
            stylesList = new JList();
            stylesList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            stylesList.setModel(getDefaultListModel());
        }
        return stylesList;
    }

    /**
     * This method initializes formatCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getFormatCB() {
        if (formatCB == null) {
            formatCB = new JComboBox();
        }
        return formatCB;
    }

    /**
     * This method initializes optionalParametersPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getOptionalParametersPanel() {
        if (optionalParametersPanel == null) {
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.weightx = 1.0;
            gridBagConstraints9.gridx = 1;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.gridy = 0;
            timeLabel = new JLabel();
            timeLabel.setText("Time:");
            optionalParametersPanel = new JPanel();
            optionalParametersPanel.setLayout(new GridBagLayout());
            optionalParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                                                                                           "Optional Parameters",
                                                                                           javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                           javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                           null,
                                                                                           null));
            optionalParametersPanel.add(timeLabel, gridBagConstraints8);
            optionalParametersPanel.add(getTimeTextField(), gridBagConstraints9);
        }
        return optionalParametersPanel;
    }

    public void removeBBoxPanel() {
        if (bboxPanel != null) {
            requiredParametersPanel.remove(bboxPanel);
        }
    }

    public void addBBoxPanel(BBoxSelectionPanel bboxPanel) {

        // at first remove the old timePanel (if existing)
        if (bboxPanel != null) {
            requiredParametersPanel.remove(bboxPanel);
        }

        this.bboxPanel = bboxPanel;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 10;
        gridBagConstraints.weighty = 10;
        
        requiredParametersPanel.add(bboxPanel, gridBagConstraints);

        requiredParametersPanel.updateUI();
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
     * This method initializes timeTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getTimeTextField() {
        if (timeTextField == null) {
            timeTextField = new JTextField();
        }
        return timeTextField;
    }

    /**
     * This method initializes versionTextField
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getVersionTextField() {
        if (versionTextField == null) {
            versionTextField = new JTextField();
            versionTextField.setEditable(false);
        }
        return versionTextField;
    }
    
    /**
     * Initialize the layer status panel.
     * @return javax.swing.JPanel
     */
    private JPanel getStatusPanel() {
    	if (statusPanel == null) {
    		statusPanel = new JPanel();
    		statusPanel.setLayout(new GridBagLayout());
            statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                    "Pending Request Status",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    null,
                    null));
            // add scroll pane
            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.gridx = 0;
            gbc1.gridy = 0;
            gbc1.fill = GridBagConstraints.HORIZONTAL;
            gbc1.anchor = GridBagConstraints.WEST;
            gbc1.weightx = 5.0D;
            gbc1.weighty = 5.0D;
            gbc1.insets = new Insets(0,5,0,10);
            
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.anchor = GridBagConstraints.EAST;
            gbc2.weightx = 10.0D;
            gbc2.weighty = 10.0D;
            
            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.gridx = 0;
            gbc3.gridy = 1;
            gbc3.gridwidth = 2;
            gbc3.fill = GridBagConstraints.HORIZONTAL;
            gbc3.anchor = GridBagConstraints.CENTER;
            gbc3.insets = new Insets(10,20,0,20);
            gbc3.weightx = 10.0D;
            gbc3.weighty = 10.0D;
            
            statusLabel = new JLabel("No active request.");
            statusPanel.add((statusLabel), gbc1);
            layerStatus = new JLabel();
            statusPanel.add(layerStatus, gbc2);
            progressBar = new JProgressBar(0, progressLength);
            progressBar.setValue(progressLength);
            statusPanel.add(progressBar, gbc3);
    	}
    	return statusPanel;
    }   
   

    /**
     * This is the default constructor
     */
    public WMSParameterConfigurator(JDialog owner, ServiceDescriptor serviceDesc, MapCanvas map, ContentTree tree) {
    	super(owner, false);
       
    	this.setSize(500, 440);
        this.setLocation(owner.getLocation());
        this.setContentPane(getJContentPane());
        this.setTitle("Build GetMap Request");     

        controller = new WMSParameterConfiguratorController(this, serviceDesc, map, tree);

        controller.postInit();
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        //  add a window closing listener to shut down the controller cleanly
        this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				 warn if any requests are still pending
				if (controller.isMapRequestStillBusy())
				{
					 Object[] options = { "OK", "CANCEL" };
					 String warning_message = "A map request is still busy.\n"
						 + "Clicking 'OK' will attempt to cancel all busy tasks and \nclose all dependent dialogs. \n"
						 + "Do you wish to continue?";
					 int chosenOption  = JOptionPane.showOptionDialog(
							    null, warning_message, "Warning",
					             JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					             null, options, options[0]);
					 if (chosenOption == JOptionPane.OK_OPTION)
					 {				 
							super.windowClosing(e);
							controller.dialogWindowClosed();
							(e.getWindow()).dispose();
					 } 	
				} else {
					super.windowClosing(e);
					controller.dialogWindowClosed();
					(e.getWindow()).dispose();
				}        	
			}
        });
    }


    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints11.weightx = 100.0D;
            gridBagConstraints11.weighty = 10.0D;
            gridBagConstraints11.insets = new java.awt.Insets(10, 10, 0, 10);
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 10.0D;
            gridBagConstraints1.weighty = 10.0D;
            gridBagConstraints1.gridy = 2;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new java.awt.Insets(0, 8, 0, 0);;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 10.0D;
            gridBagConstraints2.weighty = 10.0D;
            gridBagConstraints2.gridy = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
            gridBagConstraints.weightx = 100.0D;
            gridBagConstraints.weighty = 100.0D;
            gridBagConstraints.ipadx = 0;
            gridBagConstraints.ipady = 0;
            gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.gridwidth = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 3;
            gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints3.weightx = 100.0D;
            gridBagConstraints3.weighty = 20.0D;
            //gridBagConstraints3.insets = new java.awt.Insets(10, 10, 0, 10);
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;            
            
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getMainPanel(), gridBagConstraints);
            jContentPane.add(getButtonPanel(), gridBagConstraints1);
            jContentPane.add(getOptionalParametersPanel(), gridBagConstraints11);
            jContentPane.add(getStatusPanel(), gridBagConstraints3);
        }
        return jContentPane;
    }

    public BBoxSelectionPanel getBBoxPanel() {
        return bboxPanel;
    }
    
    /**
     * Sets the text on the pending layer request status panel
     * @param layerName
     * @param layerStatus
     */
    public void setLayerStatusText(final String layerName, final String status) {
    	if (SwingUtilities.isEventDispatchThread()) {
    		statusLabel.setText(layerName);
    		layerStatus.setText(status);
    		updateRequestStatus();
    	} else {
    		Runnable setStatus = new Runnable() {
    			public void run() {
    				setLayerStatusText(layerName, status);
    			}
    		};
    		SwingUtilities.invokeLater(setStatus);
    	}
    }
    
    /**
     * Sets the enabled state of the Request button and the state
     * of the progress bar
     *
     */
    public void updateRequestStatus() {
    	if (SwingUtilities.isEventDispatchThread()) {
    		if (controller.isMapRequestStillBusy()) {
    			showRequestButton.setEnabled(false);
    			progressBar.setIndeterminate(true);
    		}
    		else {
    			showRequestButton.setEnabled(true);
    			progressBar.setIndeterminate(false);
    			progressBar.setValue(progressLength);
    		}
    	} else {
    		Runnable updateRequestStatus = new Runnable() {
    			public void run() {
    				updateRequestStatus();
    			}
    		};
    		SwingUtilities.invokeLater(updateRequestStatus);
    	}
    }
    
   		

} // @jve:decl-index=0:visual-constraint="13,65"