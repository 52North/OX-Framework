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

package org.n52.oxf.ui.swing.tree;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.n52.oxf.ui.swing.icons.*;

/**
 * This class is a specialized DefaultMutableTreeNode which represents a LayerStorage (such as a WebService or
 * a ShapeFile).
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LayerStorageNode extends AbstractTreeNode {

    protected String title;
    
    protected String type;
    
    /**
     * 
     * @param title the title of this LayerStorageNode
     * @param type the type of the associated storage (mostly the service type).
     */
    public LayerStorageNode(String title, String type) {
        super();
        
        this.title = title;
        this.type = type;
        
        setIcon(new ImageIcon(IconAnchor.class.getResource("LAYERSTORAGE.gif")));
        setText(title);
    }
    
    public List<LayerNode> getLayerNodes(){
        List<LayerNode> res = new ArrayList<LayerNode>();
        for(int i=0; i<getChildCount(); i++){
            LayerNode layerNode = (LayerNode)getChildAt(i);
            res.add(layerNode);
        }
        return res;
    }

    public String getTitle() {
        return title;
    }
    
    public String getType() {
        return type;
    }
}