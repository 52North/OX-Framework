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
 
 Created on: 11.08.2005
 *********************************************************************************/

package org.n52.oxf.ui.swing.sos;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tool.MapTool;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class SOSConnectTool extends MapTool {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4980672874872957063L;

    private static final Logger LOGGER = LoggingHandler.getLogger(SOSConnectTool.class);

    private ContentTree tree;

    /**
     * 
     */
    public SOSConnectTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("add_layer.gif")));
        setToolTipText("Add SOS Layer");
    }

    @Override
    public void activate() {
        super.activate();

        new ConnectSOSDialog(owner, map, tree).setVisible(true);

        LOGGER.debug(this.getName() + "activated");
    }

}