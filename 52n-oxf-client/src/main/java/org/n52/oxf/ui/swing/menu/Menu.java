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

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public abstract class Menu extends JMenu implements ActionListener {

    protected MapCanvas map;

    protected ContentTree tree;
    
    protected JFrame owner;
    
    /**
     * 
     */
    public Menu(JFrame owner, MapCanvas map, ContentTree tree, String title) {
        super(title);
        
        this.map = map;
        this.tree = tree;
        this.owner = owner;
    }
    
    public void setMap(MapCanvas map) {
        this.map = map;
    }
    
    public MapCanvas getMapCanvas(){
        return map;
    }

    public ContentTree getTree() {
        return tree;
    }

    public void setTree(ContentTree tree) {
        this.tree = tree;
    }
}