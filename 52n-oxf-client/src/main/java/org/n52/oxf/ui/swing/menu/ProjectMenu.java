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

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import org.n52.oxf.serialization.ContextWriter;
import org.n52.oxf.serialization.XmlBeansContextWriter;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.FileFilterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectMenu extends Menu {

    private static final long serialVersionUID = 2810931435649839107L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectMenu.class);
    
    private JMenuItem saveProjectMI;
    private JMenuItem openProjectMI;
    private JMenuItem saveContextMI;
    private JMenuItem openContextMI;

    /**
     * @param map
     */
    public ProjectMenu(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map, tree, "Project");

        saveProjectMI = new JMenuItem("Save Project");
        openProjectMI = new JMenuItem("Open Project");
        saveContextMI = new JMenuItem("Save Context");
        openContextMI = new JMenuItem("Open Context");

        saveProjectMI.setIcon(new ImageIcon(IconAnchor.class.getResource("saveProject.gif")));
        openProjectMI.setIcon(new ImageIcon(IconAnchor.class.getResource("openProject.gif")));
        saveContextMI.setIcon(new ImageIcon(IconAnchor.class.getResource("save.gif")));
        openContextMI.setIcon(new ImageIcon(IconAnchor.class.getResource("open.gif")));

//        add(saveProjectMI);
//        add(openProjectMI);
//        addSeparator();
        add(saveContextMI);
        add(openContextMI);
        
        saveProjectMI.addActionListener(this);
        openProjectMI.addActionListener(this);
        saveContextMI.addActionListener(this);
        openContextMI.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(saveProjectMI)) {
            LOGGER.info("... saveProjectMenuItem pressed ...");
        }
        else if (e.getSource().equals(openProjectMI)) {
            LOGGER.info("... openProjectMenuItem pressed ...");
        }
        else if (e.getSource().equals(saveContextMI)) {
            
            JFileChooser chooser = new JFileChooser();
            
            FileFilterImpl filter = new FileFilterImpl();
            filter.addExtension("xml");
            filter.setDescription("OGC Context Documents");
            chooser.setFileFilter(filter);
            
            int returnVal = chooser.showSaveDialog(owner);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String filePath = chooser.getSelectedFile().getAbsolutePath();
                
                ContextWriter serializer = new XmlBeansContextWriter(map.getLayerContext());
                serializer.saveContextFile(filePath);
                
            }
        }
        else if (e.getSource().equals(openContextMI)) {
            LOGGER.info("... openContexttMenuItem pressed ...");
        }
    }

}