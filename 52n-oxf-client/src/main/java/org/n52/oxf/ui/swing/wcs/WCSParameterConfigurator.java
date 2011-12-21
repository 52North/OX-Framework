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

import org.n52.oxf.owsCommon.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class WCSParameterConfigurator extends JDialog {

    private WCSParameterConfiguratorController controller;

    private JPanel jContentPane = null;
    private JPanel requiredParametersPanel = null;
    private JPanel buttonPanel = null;
    private JButton showRequestButton = null;
    private JLabel coveragesLabel = null;
    private JComboBox coveragesCB = null;
    private JLabel formatLabel = null;
    private JComboBox formatCB = null;
    private JPanel optionalParametersPanel = null;
    private DefaultListModel defaultListModel = null; // @jve:decl-index=0:visual-constraint=""

    private JLabel timeLabel = null;

    private JTextField timeTextField = null;

    private JLabel bboxLabel = null;

    private BBoxSelectionPanel bboxPanel = null;

    /**
     * This method initializes mainPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (requiredParametersPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints10.gridy = 4;
            bboxLabel = new JLabel();
            bboxLabel.setText("BoundingBox:");
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints7.gridy = 2;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.gridx = 1;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints6.gridy = 2;
            formatLabel = new JLabel();
            formatLabel.setText("Format:");
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
            coveragesLabel = new JLabel();
            coveragesLabel.setText("Coverage:");
            requiredParametersPanel = new JPanel();
            requiredParametersPanel.setLayout(new GridBagLayout());
            requiredParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                                                                                           "Required Parameters",
                                                                                           javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                           javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                           null,
                                                                                           null));
            requiredParametersPanel.add(coveragesLabel, gridBagConstraints2);
            requiredParametersPanel.add(getCoveragesCB(), gridBagConstraints3);
            requiredParametersPanel.add(formatLabel, gridBagConstraints6);
            requiredParametersPanel.add(getFormatCB(), gridBagConstraints7);
            requiredParametersPanel.add(bboxLabel, gridBagConstraints10);
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
                }
            });
        }
        return showRequestButton;
    }

    /**
     * This method initializes coveragesCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getCoveragesCB() {
        if (coveragesCB == null) {
            coveragesCB = new JComboBox();
            coveragesCB.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    controller.itemStateChanged_layersCB(e);
                }
            });
        }
        return coveragesCB;
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
     * This is the default constructor
     */
    public WCSParameterConfigurator(Component owner, ServiceDescriptor serviceDesc, MapCanvas map, ContentTree tree) {
        super();

        this.setSize(470, 328);
        this.setLocation(owner.getLocation());
        this.setContentPane(getJContentPane());
        this.setTitle("Build GetCoverage Request");

        controller = new WCSParameterConfiguratorController(this, serviceDesc, map, tree);

        controller.postInit();
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

    public BBoxSelectionPanel getBBoxPanel() {
        return bboxPanel;
    }

}  //  @jve:decl-index=0:visual-constraint="13,152"