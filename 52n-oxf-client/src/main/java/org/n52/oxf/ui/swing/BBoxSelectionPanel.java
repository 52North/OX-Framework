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

package org.n52.oxf.ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.valueDomains.spatial.BoundingBox;

/**
 * This UI-component allows the user to specify a certain BBox.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class BBoxSelectionPanel extends JPanel {

    private JLabel srsLabel = null;
    private JLabel lowerCornerLabel = null;
    private JLabel upperCornerLabel = null;
    private JComboBox srsCB = null;
    private JTextField maxXTextField = null;
    private JTextField maxYTextField = null;
    private JLabel xLabel = null;
    private JLabel yLabel = null;
    private JTextField minXTextField = null;
    private JTextField minYTextField = null;

    private BBoxSelectionPanelController controller;
    private MapCanvas map;

    /**
     * This method initializes srsCB
     * 
     * @return javax.swing.JComboBox
     */
    public JComboBox getSrsCB() {
        if (srsCB == null) {
            srsCB = new JComboBox();
            srsCB.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    controller.itemStateChanged_srsCB(e);
                }

            });
        }
        return srsCB;
    }

    /**
     * This method initializes maxXTextField
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getMaxXTextField() {
        if (maxXTextField == null) {
            maxXTextField = new JTextField();
        }
        return maxXTextField;
    }

    /**
     * This method initializes maxYTextField
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getMaxYTextField() {
        if (maxYTextField == null) {
            maxYTextField = new JTextField();
        }
        return maxYTextField;
    }

    /**
     * This method initializes minXTextField
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getMinXTextField() {
        if (minXTextField == null) {
            minXTextField = new JTextField();
        }
        return minXTextField;
    }

    /**
     * This method initializes minYTextField
     * 
     * @return javax.swing.JTextField
     */
    public JTextField getMinYTextField() {
        if (minYTextField == null) {
            minYTextField = new JTextField();
        }
        return minYTextField;
    }

    /**
     * This is the default constructor
     */
    public BBoxSelectionPanel(String[] supportedSrsOfDataResource, IBoundingBox[] bboxes, MapCanvas map) {
        super();

        this.map = map;

        initialize();

        controller = new BBoxSelectionPanelController(this);

        controller.postInit(supportedSrsOfDataResource, bboxes);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints10.gridy = 3;
        gridBagConstraints10.weightx = 100.0D;
        gridBagConstraints10.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints10.weighty = 100.0D;
        gridBagConstraints10.gridx = 2;
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints9.gridy = 3;
        gridBagConstraints9.weightx = 100.0D;
        gridBagConstraints9.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints9.weighty = 100.0D;
        gridBagConstraints9.gridx = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 2;
        gridBagConstraints8.insets = new java.awt.Insets(10, 0, 0, 0);
        gridBagConstraints8.weightx = 10.0D;
        gridBagConstraints8.weighty = 10.0D;
        gridBagConstraints8.gridy = 1;
        yLabel = new JLabel();
        yLabel.setText("Y");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.insets = new java.awt.Insets(10, 0, 0, 0);
        gridBagConstraints7.weightx = 10.0D;
        gridBagConstraints7.weighty = 10.0D;
        gridBagConstraints7.gridy = 1;
        xLabel = new JLabel();
        xLabel.setText("X");
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.gridy = 2;
        gridBagConstraints6.weightx = 100.0D;
        gridBagConstraints6.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints6.weighty = 100.0D;
        gridBagConstraints6.gridx = 2;
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.weightx = 100.0D;
        gridBagConstraints5.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints5.weighty = 100.0D;
        gridBagConstraints5.gridx = 1;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints4.gridy = 0;
        gridBagConstraints4.weightx = 100.0D;
        gridBagConstraints4.gridwidth = 2;
        gridBagConstraints4.weighty = 10.0D;
        gridBagConstraints4.gridx = 1;
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.weightx = 10.0D;
        gridBagConstraints3.weighty = 10.0D;
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints3.gridy = 2;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.weightx = 10.0D;
        gridBagConstraints2.weighty = 10.0D;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints2.gridy = 3;
        upperCornerLabel = new JLabel();
        upperCornerLabel.setText("Upper Corner:");
        lowerCornerLabel = new JLabel();
        lowerCornerLabel.setText("Lower Corner:");

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 10.0D;
        gridBagConstraints.weighty = 10.0D;
        gridBagConstraints.gridx = 0;
        srsLabel = new JLabel();
        srsLabel.setText("SRS:");
        this.setLayout(new GridBagLayout());
        this.setSize(382, 133);
        this.add(lowerCornerLabel, gridBagConstraints2);
        this.add(upperCornerLabel, gridBagConstraints3);
        this.add(getMaxXTextField(), gridBagConstraints5);
        this.add(getMaxYTextField(), gridBagConstraints6);
        this.add(xLabel, gridBagConstraints7);
        this.add(yLabel, gridBagConstraints8);
        this.add(getMinXTextField(), gridBagConstraints9);
        this.add(getMinYTextField(), gridBagConstraints10);
        this.add(srsLabel, gridBagConstraints);
        this.add(getSrsCB(), gridBagConstraints4);
    }

    public IBoundingBox getChosenBBox() {

        String crs = (String) getSrsCB().getSelectedItem();
        double minX = Double.parseDouble(getMinXTextField().getText());
        double minY = Double.parseDouble(getMinYTextField().getText());
        double maxX = Double.parseDouble(getMaxXTextField().getText());
        double maxY = Double.parseDouble(getMaxYTextField().getText());

        double[] lowerCorner = {minX, minY};
        double[] upperCorner = {maxX, maxY};

        IBoundingBox result = new BoundingBox(crs, lowerCorner, upperCorner);

        return result;
    }

    public MapCanvas getMap() {
        return map;
    }

} // @jve:decl-index=0:visual-constraint="10,10"