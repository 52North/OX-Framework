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
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.ui.swing.ApprovalDialog;
import org.n52.oxf.ui.swing.TimePanel;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class GetObs_Configurator extends ApprovalDialog {

    private GetObs_ConfiguratorController controller;

    private JPanel jContentPane = null;
    private JPanel requiredParametersPanel = null;
    private JPanel buttonPanel = null;
    private JButton showRequestButton = null;
    private JLabel offeringLabel = null;
    private JComboBox offeringCB = null;
    private JLabel observedPropertyLabel = null;
    private JLabel resultFormatLabel = null;
    private JComboBox responseFormatCB = null;
    private JPanel optionalParametersPanel = null;
    private JLabel procedureLabel = null;
    private JLabel resultModelLabel = null;
    private JLabel responseModeLabel = null;
    private JLabel eventTimeLabel = null;
    private JComboBox resultModelCB = null;
    private JComboBox responseModeCB = null;
    private JScrollPane procedureSP = null;
    private JList procedureList = null;

    private DefaultListModel defaultListModel = null; // @jve:decl-index=0:visual-constraint=""

    private DefaultListModel defaultListModel1 = null; // @jve:decl-index=0:visual-constraint=""

    private TimePanel timePanel = null;

    private JLabel foiLabel = null;

    private JLabel resultLabel = null;

    private JScrollPane foiSP = null;

    private JList foiList = null;

    private DefaultListModel defaultListModel_foiList = null; // @jve:decl-index=0:visual-constraint=""

    private OperationResult opResult = null;

    private ObservedPropertyPanel observedPropertyPanel = null;

    private JComboBox filterComboBox = null;

    private JScrollPane filterScrollPane = null;

    private JTextArea filterTextArea = null;

    private boolean onlyViewGetObsDoc;

    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (requiredParametersPanel == null) {

            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 2;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints6.gridy = 2;
            resultFormatLabel = new JLabel();
            resultFormatLabel.setText("Response Format:");
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridheight = 1;
            gridBagConstraints4.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints4.gridy = 1;
            observedPropertyLabel = new JLabel();
            observedPropertyLabel.setText("Observed Property:");
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
            offeringLabel = new JLabel();
            offeringLabel.setText("Offering:");
            requiredParametersPanel = new JPanel();
            requiredParametersPanel.setLayout(new GridBagLayout());
            requiredParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                                                                                           "Required MultimapRequestParameters",
                                                                                           javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                           javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                           null,
                                                                                           null));
            requiredParametersPanel.add(offeringLabel, gridBagConstraints2);
            requiredParametersPanel.add(getOfferingCB(), gridBagConstraints3);
            requiredParametersPanel.add(observedPropertyLabel, gridBagConstraints4);
            requiredParametersPanel.add(resultFormatLabel, gridBagConstraints6);
            requiredParametersPanel.add(getResponseFormatCB(), gridBagConstraints7);
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
            showRequestButton.setText("Ok");
            showRequestButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    controller.actionPerformed_showRequestButton(e, onlyViewGetObsDoc);
                }
            });
        }
        return showRequestButton;
    }

    /**
     * This method initializes offeringCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getOfferingCB() {
        if (offeringCB == null) {
            offeringCB = new JComboBox();
            offeringCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_offeringCB(e);
                }
            });
        }
        return offeringCB;
    }

    /**
     * This method initializes resultFormatCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getResponseFormatCB() {
        if (responseFormatCB == null) {
            responseFormatCB = new JComboBox();
        }
        return responseFormatCB;
    }

    /**
     * This method initializes optionalParametersPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getOptionalParametersPanel() {
        if (optionalParametersPanel == null) {
            GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
            gridBagConstraints20.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints20.gridy = 6;
            gridBagConstraints20.weightx = 100.0D;
            gridBagConstraints20.weighty = 100.0D;
            gridBagConstraints20.gridx = 1;
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints19.gridy = 5;
            gridBagConstraints19.weightx = 1.0;
            gridBagConstraints19.gridx = 1;
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints18.gridy = 4;
            gridBagConstraints18.weightx = 10.0D;
            gridBagConstraints18.weighty = 10.0D;
            gridBagConstraints18.gridx = 1;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 0;
            gridBagConstraints17.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints17.gridy = 5;
            resultLabel = new JLabel();
            resultLabel.setText("Result:");
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints16.gridy = 4;
            foiLabel = new JLabel();
            foiLabel.setText("Feature of Interest:");
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints15.gridy = 0;
            gridBagConstraints15.weightx = 10.0D;
            gridBagConstraints15.weighty = 10.0D;
            gridBagConstraints15.gridx = 1;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 2;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 1;
            gridBagConstraints13.weightx = 10.0D;
            gridBagConstraints13.gridx = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints12.gridy = 3;
            eventTimeLabel = new JLabel();
            eventTimeLabel.setText("Event Time:");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints10.gridy = 2;
            responseModeLabel = new JLabel();
            responseModeLabel.setText("Response Mode:");
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints9.gridy = 1;
            resultModelLabel = new JLabel();
            resultModelLabel.setText("Result Model:");
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints8.gridy = 0;
            procedureLabel = new JLabel();
            procedureLabel.setText("Procedure:");
            optionalParametersPanel = new JPanel();
            optionalParametersPanel.setLayout(new GridBagLayout());
            optionalParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                                                                                           "Optional MultimapRequestParameters",
                                                                                           javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                           javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                           null,
                                                                                           null));
            optionalParametersPanel.add(procedureLabel, gridBagConstraints8);
            optionalParametersPanel.add(resultModelLabel, gridBagConstraints9);
            optionalParametersPanel.add(responseModeLabel, gridBagConstraints10);
            optionalParametersPanel.add(eventTimeLabel, gridBagConstraints12);
            optionalParametersPanel.add(getResultModelCB(), gridBagConstraints13);
            optionalParametersPanel.add(getResponseModeCB(), gridBagConstraints14);
            optionalParametersPanel.add(getProcedureSP(), gridBagConstraints15);
            optionalParametersPanel.add(foiLabel, gridBagConstraints16);
            optionalParametersPanel.add(resultLabel, gridBagConstraints17);
            optionalParametersPanel.add(getFoiSP(), gridBagConstraints18);
            optionalParametersPanel.add(getFilterComboBox(), gridBagConstraints19);
            optionalParametersPanel.add(getFilterScrollPane(), gridBagConstraints20);
        }
        return optionalParametersPanel;
    }

    /**
     * This method initializes resultModelCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getResultModelCB() {
        if (resultModelCB == null) {
            resultModelCB = new JComboBox();
        }
        return resultModelCB;
    }

    /**
     * This method initializes responseModeCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getResponseModeCB() {
        if (responseModeCB == null) {
            responseModeCB = new JComboBox();
        }
        return responseModeCB;
    }

    /**
     * This method initializes procedureSP
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getProcedureSP() {
        if (procedureSP == null) {
            procedureSP = new JScrollPane();
            procedureSP.setPreferredSize(new java.awt.Dimension(0, 50));
            procedureSP.setViewportView(getProcedureList());
        }
        return procedureSP;
    }

    /**
     * This method initializes procedureList
     * 
     * @return javax.swing.JList
     */
    public JList getProcedureList() {
        if (procedureList == null) {
            procedureList = new JList();
            procedureList.setModel(getDefaultListModel1());
        }
        return procedureList;
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
     * This method initializes defaultListModel1
     * 
     * @return javax.swing.DefaultListModel
     */
    private DefaultListModel getDefaultListModel1() {
        if (defaultListModel1 == null) {
            defaultListModel1 = new DefaultListModel();
        }
        return defaultListModel1;
    }

    /**
     * This method initializes foiSP
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getFoiSP() {
        if (foiSP == null) {
            foiSP = new JScrollPane();
            foiSP.setViewportView(getFoiList());
        }
        return foiSP;
    }

    /**
     * This method initializes foiList
     * 
     * @return javax.swing.JList
     */
    public JList getFoiList() {
        if (foiList == null) {
            foiList = new JList();
            foiList.setModel(getDefaultListModel_foiList());
        }
        return foiList;
    }

    /**
     * This method initializes defaultListModel_foiList
     * 
     * @return javax.swing.DefaultListModel
     */
    private DefaultListModel getDefaultListModel_foiList() {
        if (defaultListModel_foiList == null) {
            defaultListModel_foiList = new DefaultListModel();
        }
        return defaultListModel_foiList;
    }
    
    public GetObs_Configurator(Component owner,
                               SOSAdapter sosAdapter,
                               ServiceDescriptor serviceDescriptor,
                               Set<OXFFeature> preSelectedFeaures,
                               int minNumOfProps,
                               int maxNumOfProps,
                               boolean onlyViewGetObsDoc) {
        this(owner, sosAdapter, serviceDescriptor, preSelectedFeaures, minNumOfProps, maxNumOfProps);
        
        this.onlyViewGetObsDoc = onlyViewGetObsDoc;
    }

    /**
     * This is the default constructor
     */
    public GetObs_Configurator(Component owner,
                               SOSAdapter sosAdapter,
                               ServiceDescriptor serviceDescriptor,
                               Set<OXFFeature> preSelectedFeaures,
                               int minNumOfProps,
                               int maxNumOfProps) {
        setModal(true);
        initialize(owner);
        controller = new GetObs_ConfiguratorController(this,
                                                       sosAdapter,
                                                       serviceDescriptor,
                                                       preSelectedFeaures,
                                                       minNumOfProps,
                                                       maxNumOfProps);
        controller.postInit();
        
        this.onlyViewGetObsDoc = false;
    }

    protected void setOperationResult(OperationResult opResult) {
        this.opResult = opResult;
    }

    public OperationResult getOperationResult() {
        return opResult;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(Component owner) {
        this.setContentPane(getJContentPane());
        this.setTitle("Build GetObservation Request");
        this.setSize(560,730);
        this.setLocation(owner.getLocation());
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
            gridBagConstraints11.weighty = 100.0D;
            gridBagConstraints11.insets = new java.awt.Insets(10, 10, 0, 10);
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 10.0D;
            gridBagConstraints1.weighty = 10.0D;
            gridBagConstraints1.gridy = 2;
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
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());
            jContentPane.add(getMainPanel(), gridBagConstraints);
            jContentPane.add(getButtonPanel(), gridBagConstraints1);
            jContentPane.add(getOptionalParametersPanel(), gridBagConstraints11);
        }
        return jContentPane;
    }

    public TimePanel getTimePanel() {
        return timePanel;
    }

    public void removeTimePanel() {
        if (timePanel != null) {
            optionalParametersPanel.remove(timePanel);
        }
    }

    public void addTimePanel(TimePanel timePanel) {

        // at first remove the old timePanel (if existing)
        if (timePanel != null) {
            optionalParametersPanel.remove(timePanel);
        }

        this.timePanel = timePanel;

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;

        optionalParametersPanel.add(timePanel, gridBagConstraints);

        optionalParametersPanel.updateUI();
    }

    /**
     * This method initializes observedPropertyPanel
     * 
     * @return javax.swing.JPanel
     */
    public ObservedPropertyPanel getObservedPropertyPanel() {
        return observedPropertyPanel;
    }

    public void removeObservedPropertyPanel() {
        if (observedPropertyPanel != null) {
            optionalParametersPanel.remove(observedPropertyPanel);
        }
    }

    public void addObservedPropertyPanel(ObservedPropertyPanel propertyPanel) {

        // at first remove the old propertyPanel (if existing)
        if (this.observedPropertyPanel != null) {
            requiredParametersPanel.remove(this.observedPropertyPanel);
        }

        this.observedPropertyPanel = propertyPanel;

        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        // gridBagConstraints5.weightx = 10.0D;
        // gridBagConstraints5.weighty = 10.0D;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints5.gridy = 1;

        requiredParametersPanel.add(propertyPanel, gridBagConstraints5);

        requiredParametersPanel.updateUI();
    }

    /**
     * This method initializes filterComboBox	
     * 	
     * @return javax.swing.JComboBox	
     */
    public JComboBox getFilterComboBox() {
        if (filterComboBox == null) {
            filterComboBox = new JComboBox();
            filterComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_filterComboBox();
                }
            });
            
        }
        return filterComboBox;
    }

    /**
     * This method initializes filterScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getFilterScrollPane() {
        if (filterScrollPane == null) {
            filterScrollPane = new JScrollPane();
            filterScrollPane.setViewportView(getFilterTextArea());
        }
        return filterScrollPane;
    }

    /**
     * This method initializes filterTextArea	
     * 	
     * @return javax.swing.JTextArea	
     */
    public JTextArea getFilterTextArea() {
        if (filterTextArea == null) {
            filterTextArea = new JTextArea();
        }
        return filterTextArea;
    }
    
} // @jve:decl-index=0:visual-constraint="10,10"