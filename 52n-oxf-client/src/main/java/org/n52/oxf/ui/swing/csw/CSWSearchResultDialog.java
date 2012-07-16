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

package org.n52.oxf.ui.swing.csw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.wcs.WCSAdapter;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.sos.ConnectSOSDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.wcs.WCSParameterConfigurator;
import org.n52.oxf.ui.swing.wms.ConnectWMSDialog;

public class CSWSearchResultDialog extends JDialog implements ActionListener {

    private Vector<ServiceEntry> services;
    private ButtonGroup group;
    private JButton ok;
    private JButton cancel;
    private Map<JRadioButton, ServiceEntry> entryButtonMap;
    private ServiceEntry selectedEntry;

    private Component owner;
    private MapCanvas map;
    private ContentTree tree;
    private String serviceType;

    public CSWSearchResultDialog(Component owner,
                                 MapCanvas map,
                                 ContentTree tree,
                                 Vector<ServiceEntry> services,
                                 String serviceType) {
        super();

        this.owner = owner;
        this.map = map;
        this.tree = tree;
        this.serviceType = serviceType;
        this.services = services;

        setLocation(owner.getLocation());
        setSize(500, 200);
        setTitle("Choose Service");

        JPanel panelMain = new JPanel();
        JScrollPane scrollPaneMain = new JScrollPane(panelMain);
        panelMain.setAutoscrolls(true);
        panelMain.setMinimumSize(new Dimension(300, 200));
        GridLayout gridLayout = new GridLayout(services.size() * 4, 1);
        panelMain.setLayout(gridLayout);

        getContentPane().add(scrollPaneMain, BorderLayout.CENTER);

        JPanel panelBtn = new JPanel();
        GridLayout gridLayout2 = new GridLayout(1, 2);
        gridLayout2.setVgap(5);
        gridLayout2.setHgap(5);
        panelBtn.setLayout(gridLayout2);
        ok = new JButton("OK");
        ok.addActionListener(this);
        panelBtn.add(ok);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        panelBtn.add(cancel);

        getContentPane().add(panelBtn, BorderLayout.SOUTH);

        group = new ButtonGroup();
        entryButtonMap = new HashMap<JRadioButton, ServiceEntry>();

        for (ServiceEntry entry : services) {
            JRadioButton radioButton = new JRadioButton(entry.getName());
            entryButtonMap.put(radioButton, entry);
            radioButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    selectedEntry = entryButtonMap.get(arg0.getSource());

                }
            });
            group.add(radioButton);
            panelMain.add(radioButton);
            JLabel labelDescription = new JLabel(entry.getDescription());
            panelMain.add(labelDescription);
            JLabel labelURL = new JLabel(entry.getUrl());
            panelMain.add(labelURL);
            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            panelMain.add(separator);
        }

        if (services.size() == 0) {
            JOptionPane.showMessageDialog(owner,
                                          "Your CSW GetRecords query returned no results.",
                                          "Alert",
                                          JOptionPane.ERROR_MESSAGE);
            setVisible(false);
        }
        else {
            setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {

            /**
             * possible serviceTypes: WebMapService WebCoverageService SensorObservationService
             */
            try {
                if (serviceType.equals("WebCoverageService")) {
                    WCSAdapter wcsAdapter = new WCSAdapter();
                    ServiceDescriptor descriptor;
                    if (selectedEntry.getUrl().endsWith("/") || selectedEntry.getUrl().endsWith("\\")
                            || selectedEntry.getUrl().endsWith("?")) {
                        descriptor = wcsAdapter.initService(selectedEntry.getUrl(), false);
                    }
                    else {
                        descriptor = wcsAdapter.initService(selectedEntry.getUrl(), true);
                    }
                    WCSParameterConfigurator configurator = new WCSParameterConfigurator(owner, descriptor, map, tree);
                    configurator.setVisible(true);
                }
                else if (serviceType.equals("SensorObservationService")) {
                    ConnectSOSDialog dialog = new ConnectSOSDialog(owner, map, tree, new String[]{selectedEntry.getUrl()});
                    dialog.setVisible(true);
                }
                else if (serviceType.equals("WebMapService")) {
                    ConnectWMSDialog dialog = new ConnectWMSDialog(owner, map, tree, new String[]{selectedEntry.getUrl()});
                    dialog.setVisible(true);
                }
            }
            catch (OXFException e1) {
                e1.printStackTrace();
            }

        }
        else if (e.getSource() == cancel) {
            // nothing
        }
        this.setVisible(false);
    }

}