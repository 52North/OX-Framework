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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ObservedPropertyPanel4OneProp extends ObservedPropertyPanel {

    private JComboBox observedPropertyComboBox = null;

    private JComboBox getObservedPropertyComboBox() {
        if (observedPropertyComboBox == null) {
            observedPropertyComboBox = new JComboBox();
        }
        return observedPropertyComboBox;
    }

    public ObservedPropertyPanel4OneProp(String[] observedProperties) {
        super(observedProperties);
        initialize();

        initPropertyValues();
    }

    private void initialize() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(300, 29);
        this.add(getObservedPropertyComboBox(), gridBagConstraints);
    }

    private void initPropertyValues() {

        for (int i = 0; i < observedProperties.length; i++) {
            getObservedPropertyComboBox().addItem(observedProperties[i]);
        }
    }

    @Override
    public String[] getChosenProperties() {
        return new String[] {(String) getObservedPropertyComboBox().getSelectedItem()};
    }

    @Override
    public void clear() {
        getObservedPropertyComboBox().removeAllItems();
    }

} // @jve:decl-index=0:visual-constraint="10,10"