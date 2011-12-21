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
 
 Created on: 05.10.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ObservedPropertyPanel4TwoProps extends ObservedPropertyPanel {

    private JLabel xLabel = null;
    private JComboBox xComboBox = null;
    private JComboBox yComboBox = null;
    private JLabel yLabel = null;

    /**
     * This method initializes xComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getXComboBox() {
        if (xComboBox == null) {
            xComboBox = new JComboBox();
        }
        return xComboBox;
    }

    /**
     * This method initializes yComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getYComboBox() {
        if (yComboBox == null) {
            yComboBox = new JComboBox();
        }
        return yComboBox;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * This is the default constructor
     */
    public ObservedPropertyPanel4TwoProps(String[] observedProperties) {
        super(observedProperties);
        initialize();

        initPropertyValues();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints3.gridy = 1;
        yLabel = new JLabel();
        yLabel.setText("Y:");
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.gridx = 1;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.gridy = 0;
        xLabel = new JLabel();
        xLabel.setText("X:");
        this.setLayout(new GridBagLayout());
        this.setSize(315, 57);
        this.add(xLabel, gridBagConstraints);
        this.add(getXComboBox(), gridBagConstraints1);
        this.add(getYComboBox(), gridBagConstraints2);
        this.add(yLabel, gridBagConstraints3);
    }

    private void initPropertyValues() {

        for (int i = 0; i < observedProperties.length; i++) {

            getXComboBox().addItem(observedProperties[i]);
            getYComboBox().addItem(observedProperties[i]);

        }
    }

    @Override
    public String[] getChosenProperties() {
        String[] result = new String[2];
        result[0] = (String) getXComboBox().getSelectedItem();
        result[1] = (String) getYComboBox().getSelectedItem();

        return result;
    }

    @Override
    public void clear() {
        getXComboBox().removeAllItems();
        getYComboBox().removeAllItems();
    }

} // @jve:decl-index=0:visual-constraint="10,10"