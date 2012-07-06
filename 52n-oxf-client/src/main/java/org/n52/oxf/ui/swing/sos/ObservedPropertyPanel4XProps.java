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

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ObservedPropertyPanel4XProps extends ObservedPropertyPanel {

    private JScrollPane observedPropertyScrollPane = null;
    private JList observedPropertyList = null;

    /**
     * This method initializes observedPropertyScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getObservePropertyScrollPane() {
        if (observedPropertyScrollPane == null) {
            observedPropertyScrollPane = new JScrollPane();
            observedPropertyScrollPane.setViewportView(getObservedPropertyList());
        }
        return observedPropertyScrollPane;
    }

    /**
     * This method initializes observedPropertyList
     * 
     * @return javax.swing.JList
     */
    private JList getObservedPropertyList() {
        if (observedPropertyList == null) {
            observedPropertyList = new JList();
            observedPropertyList.setModel(new DefaultListModel());
        }
        return observedPropertyList;
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
    public ObservedPropertyPanel4XProps(String[] observedProperties) {
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
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.gridx = 0;
        this.setLayout(new GridBagLayout());
        this.setSize(300, 200);
        this.add(getObservePropertyScrollPane(), gridBagConstraints);
    }

    private void initPropertyValues() {

        for (int i = 0; i < observedProperties.length; i++) {
            DefaultListModel listModel = (DefaultListModel) getObservedPropertyList().getModel();

            listModel.addElement(observedProperties[i]);
            getObservedPropertyList().setSelectedIndex(0);
        }
    }

    /**
     * 
     */
    @Override
    public String[] getChosenProperties() {
        Object[] observedPropertyObjects = getObservedPropertyList().getSelectedValues();

        String[] observedProperties = new String[observedPropertyObjects.length];
        for (int i = 0; i < observedPropertyObjects.length; i++) {
            observedProperties[i] = observedPropertyObjects[i].toString();
        }
        return observedProperties;
    }

    @Override
    public void clear() {
        DefaultListModel listModel = (DefaultListModel) getObservedPropertyList().getModel();
        listModel.clear();
    }
}