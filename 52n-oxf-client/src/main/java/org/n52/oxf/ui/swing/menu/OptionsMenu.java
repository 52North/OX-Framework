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

import java.awt.event.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.animation.*;
import org.n52.oxf.ui.swing.sos.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.util.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OptionsMenu extends Menu {

    private static final Logger LOGGER = LoggingHandler.getLogger(OptionsMenu.class);

    private Menu memoryMenu;

    private JMenuItem showMsgConsoleMI;
    private JMenuItem showAnimationDialogMI;
    private JMenuItem showLegendDialogMI;
    
    private JMenuItem specifyProxyMI;
    
    private MessageConsole msgConsole;
    private LegendDialog legendDialog;

    /**
     * @param map
     * @param title
     */
    public OptionsMenu(JFrame owner,
                       MapCanvas map,
                       ContentTree tree,
                       MessageConsole msgConsole,
                       LegendDialog legendDialog) {
        super(owner, map, tree, "Options");

        this.msgConsole = msgConsole;
        this.legendDialog = legendDialog;

        memoryMenu = new MemoryMenu(owner, map, tree);
        showMsgConsoleMI = new JMenuItem("Show / Hide Message Console");
        showAnimationDialogMI = new JMenuItem("Show / Hide Animation Controller");
        showLegendDialogMI = new JMenuItem("Show / Hide Legend Window");
        specifyProxyMI = new JMenuItem("Specify Proxy Settings");
        
        add(showMsgConsoleMI);
        //add(showAnimationDialogMI);
        add(showLegendDialogMI);
        add(memoryMenu);
        add(specifyProxyMI);

        showMsgConsoleMI.addActionListener(this);
        showAnimationDialogMI.addActionListener(this);
        showLegendDialogMI.addActionListener(this);
        specifyProxyMI.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(showMsgConsoleMI)) {
            msgConsole.setVisible( !msgConsole.isVisible());
        }
        else if (e.getSource().equals(showLegendDialogMI)) {
            legendDialog.setVisible( !legendDialog.isVisible());
        }
        else if (e.getSource().equals(specifyProxyMI)) {
            new ProxyPreferencesDialog(owner).setVisible(true);
        }
    }
}