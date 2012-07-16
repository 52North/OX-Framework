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

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class MemoryMenu extends Menu {

    private static final long serialVersionUID = -3303772644905232473L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryMenu.class);

    private JMenuItem logFreeMemoryMI;

    private JMenuItem logMaxMemoryMI;

    private JMenuItem runGarbageCollectionMI;

    /**
     * @param map
     */
    public MemoryMenu(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map, tree, "Memory");

        logFreeMemoryMI = new JMenuItem("Log Free Memory");
        logMaxMemoryMI = new JMenuItem("Log Max Memory");
        runGarbageCollectionMI = new JMenuItem("Run Garbage Collection");

        add(logFreeMemoryMI);
        add(logMaxMemoryMI);
        add(runGarbageCollectionMI);

        logFreeMemoryMI.addActionListener(this);
        logMaxMemoryMI.addActionListener(this);
        runGarbageCollectionMI.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(logFreeMemoryMI)) {
            LOGGER.info("Remaining Heap Size: " + Runtime.getRuntime().freeMemory() / 1000000
                    + "MB  (=" + Runtime.getRuntime().freeMemory() / 1000 + "KB)");
        }
        else if (e.getSource().equals(logMaxMemoryMI)) {
            LOGGER.info("Max Heap Size: " + Runtime.getRuntime().maxMemory() / 1000000 + "MB  (="
                    + Runtime.getRuntime().maxMemory() / 1000 + "KB)");
        }
        else if (e.getSource().equals(runGarbageCollectionMI)) {
            Runtime.getRuntime().gc();
            LOGGER.info("Garbage Collection done.");
        }
    }

}