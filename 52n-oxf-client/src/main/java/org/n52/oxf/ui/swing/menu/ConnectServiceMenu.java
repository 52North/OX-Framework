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

package org.n52.oxf.ui.swing.menu;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.csw.CSWChooser;
import org.n52.oxf.ui.swing.ses.SesGUI;
import org.n52.oxf.ui.swing.sos.ConnectSOSDialog;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.wcs.ConnectWCSDialog;
import org.n52.oxf.ui.swing.wms.ConnectWMSDialog;
import org.n52.oxf.util.LoggingHandler;
import org.n52.sps.wupper.client.Client;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ConnectServiceMenu extends Menu {

    private static final Logger LOGGER = LoggingHandler.getLogger(ConnectServiceMenu.class);
    
    private JMenuItem connectCSWMenuItem;
    
    private JMenuItem connectSOSMenuItem;
    
    private JMenuItem connectSASMenuItem;

    private JMenuItem connectWMSMenuItem;
    
    private JMenuItem connectWCSMenuItem;
    
    private JMenuItem connectSPSMenuItem;
    
    private JMenuItem connectSESMenuItem;
    
    /**
     */
    public ConnectServiceMenu(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map, tree, "Connect");
        
        connectCSWMenuItem = new JMenuItem("Connect to CSW");
        connectSOSMenuItem = new JMenuItem("Connect to SOS");
        connectSASMenuItem = new JMenuItem("Connect to SAS");
        connectWMSMenuItem = new JMenuItem("Connect to WMS");
        connectWCSMenuItem = new JMenuItem("Connect to WCS");
        connectSPSMenuItem = new JMenuItem("Connect to SPS");
        connectSESMenuItem = new JMenuItem("Connect to SES");
        
        add(connectCSWMenuItem);
        add(connectSOSMenuItem);
        add(connectSASMenuItem);
        add(connectWMSMenuItem);
        add(connectWCSMenuItem);
        add(connectSPSMenuItem);
        add(connectSESMenuItem);
        
        connectCSWMenuItem.addActionListener(this);
        connectSOSMenuItem.addActionListener(this);
        connectSASMenuItem.addActionListener(this);
        connectWMSMenuItem.addActionListener(this);
        connectWCSMenuItem.addActionListener(this);
        connectSPSMenuItem.addActionListener(this);
        connectSESMenuItem.addActionListener(this);
    }

    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(connectWMSMenuItem)) {
            new ConnectWMSDialog(owner, map, tree).setVisible(true);
        }
        
        else if (e.getSource().equals(connectSOSMenuItem)) {
            new ConnectSOSDialog(owner, map, tree).setVisible(true);
        }
        
        else if (e.getSource().equals(connectWCSMenuItem)) {
            new ConnectWCSDialog(owner, map, tree).setVisible(true);
        }
        
        else if (e.getSource().equals(connectSPSMenuItem)){
        	Client.startAsPart(new String[0]);
        }
        
        else if (e.getSource().equals(connectCSWMenuItem)) {
            new CSWChooser(owner, map, tree).setVisible(true);
        }
        
        else if (e.getSource().equals(connectSESMenuItem)) {
        	new SesGUI(owner, map, tree);
        }
    }

}