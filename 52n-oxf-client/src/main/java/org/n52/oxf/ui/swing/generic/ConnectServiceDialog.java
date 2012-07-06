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

package org.n52.oxf.ui.swing.generic;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import org.n52.oxf.*;
import org.n52.oxf.context.*;
import org.n52.oxf.plugin.*;
import org.n52.oxf.render.*;
import org.n52.oxf.serviceAdapters.*;
import org.n52.oxf.ui.swing.*;

/**
 * Sample code for how to use the ConnectServiceDialog:
 * 
 * <pre>
 * ConnectServiceDialog csd = new ConnectServiceDialog();
 * int returnVal = csd.showDialog();
 * 
 * IServiceAdapter serviceAdapter = null;
 * IRenderer renderer = null;
 * String serviceURL = null;
 * if (returnVal == ConnectServiceDialog.APPROVE_OPTION) {
 *     serviceAdapter = csd.getSelectedServiceAdapter();
 *     renderer = csd.getSelectedRenderer();
 *     serviceURL = csd.getSelectedServiceURL();
 * }
 * </pre>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ConnectServiceDialog extends ApprovalDialog {

    private JComboBox adapterComboBox;
    private JLabel adapterLabel;

    private JComboBox rendererComboBox;
    private JLabel rendererLabel;

    private JComboBox serviceUrlTextField;
    private JLabel serviceUrlLabel;

    private JButton okButton;
    private JPanel buttonPanel;

    /**
     * 
     */
    public ConnectServiceDialog() {
        super();
        setModal(true);
        
        serviceUrlTextField = new JComboBox();
        serviceUrlTextField.addItem("http://www.geoserver.nrw.de:80/GeoOgcWms1.3/servlet/NW2");
        serviceUrlTextField.addItem("http://iceds.ge.ucl.ac.uk/cgi-bin/icedswms");
        serviceUrlTextField.addItem("http://mars.uni-muenster.de:8080/geoserver/wcs");
        serviceUrlTextField.addItem("http://mars.uni-muenster.de:8080/SOSnew/sos");
        
        serviceUrlTextField.setEditable(true);

        adapterLabel = new JLabel("Available ServiceAdapters: ");
        rendererLabel = new JLabel("Available RendererAdapters: ");
        serviceUrlLabel = new JLabel("Service URL: ");

        okButton = new JButton("ok");
        buttonPanel = new JPanel();

        try {
            //TODO: this line enables JavaWeb-Start-class-loading:
            //ServiceAdapterFactory.setClassLoaderToUse(ServiceAdapterFactory.ClassLoaderName.STANDARD_CLASSLOADER);
            List<String> serviceAdapterList = ServiceAdapterFactory.getAvailableServiceAdapterClassNames();
            adapterComboBox = new JComboBox(serviceAdapterList.toArray());

            //TODO: this line enables JavaWeb-Start-class-loading:
            //RendererFactory.setClassLoaderToUse(RendererFactory.ClassLoaderName.STANDARD_CLASSLOADER);
            List<String> rendererList = RendererFactory.getAvailableRendererClassNames();
            rendererComboBox = new JComboBox(rendererList.toArray());
        }
        catch (OXFException e) {
            e.printStackTrace();
        }

        MyGridBagLayout layout = new MyGridBagLayout(this);
        getContentPane().setLayout(layout);

        layout.addComponent(adapterLabel, 0, 0, 1, 1, 100, 100);
        layout.addComponent(adapterComboBox, 1, 0, 1, 1, 100, 100);

        layout.addComponent(rendererLabel, 0, 1, 1, 1, 100, 100);
        layout.addComponent(rendererComboBox, 1, 1, 1, 1, 100, 100);

        layout.addComponent(serviceUrlLabel, 0, 2, 1, 1, 100, 100);
        layout.addComponent(serviceUrlTextField, 1, 2, 1, 1, 100, 100);

        layout.addComponent(buttonPanel, 0, 3, 2, 1, 100, 100);
        buttonPanel.add(okButton, BorderLayout.CENTER);

        setSize(500, 150);
        setLocation(120, 120);

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setReturnVal(APPROVE_OPTION);
                setVisible(false);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setReturnVal(CANCEL_OPTION);
                setVisible(false);
            }
        });
    }

    public IServiceAdapter getSelectedServiceAdapter() throws OXFException {
        return ServiceAdapterFactory.getServiceAdapter((String) adapterComboBox.getSelectedItem());
    }

    public IRenderer getSelectedRenderer() throws OXFException {
        return RendererFactory.getRenderer((String) rendererComboBox.getSelectedItem());
    }

    public String getSelectedServiceURL() {
        return (String)serviceUrlTextField.getSelectedItem();
    }

    /**
     * test...
     * 
     * @param args
     * @throws OXFException
     */
    public static void main(String[] args) throws OXFException {

        ConnectServiceDialog csd = new ConnectServiceDialog();
        int returnVal = csd.showDialog();

        IServiceAdapter serviceAdapter = null;
        IRenderer renderer = null;
        String serviceURL = null;
        if (returnVal == ConnectServiceDialog.APPROVE_OPTION) {
            serviceAdapter = csd.getSelectedServiceAdapter();
            renderer = csd.getSelectedRenderer();
            serviceURL = csd.getSelectedServiceURL();
        }

        System.out.println(serviceAdapter.getDescription());
        System.out.println(renderer.getDescription());
        System.out.println(serviceURL);
        System.out.println("ready");
    }
}