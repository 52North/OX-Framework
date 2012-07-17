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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.render.AnimatedVisualization;
import org.n52.oxf.ui.swing.icons.IconAnchor;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LayerNode extends AbstractTreeNode {

    private IContextLayer layer;

    private LayerStorageNode parentStorageNode;

    /**
     * 
     */
    public LayerNode(IContextLayer layer, LayerStorageNode parentStorageNode) {
        super();

        this.layer = layer;

        this.parentStorageNode = parentStorageNode;

        setText(layer.getIDName());

        if (layer.getTitle() != null) {
            this.add(new TitleNode(this));
        }
        if (layer.getTimeStamp() != null) {
            this.add(new TimeStampNode(this));
        }
    }

    public IContextLayer getLayer() {
        return layer;
    }

    public LayerStorageNode getParentStorageNode() {
        return parentStorageNode;
    }
}

class TitleNode extends AbstractTreeNode {
    
    public TitleNode(LayerNode owner) {
        setText("Title: " + owner.getLayer().getTitle());
    }
}

class TimeStampNode extends AbstractTreeNode {

    private ImageIcon timeStampPlayIcon = new ImageIcon(IconAnchor.class.getResource("timestamp_node_play.gif"));
    private ImageIcon timeStampIcon = new ImageIcon(IconAnchor.class.getResource("timestamp_node.gif"));
    
    private LayerNode owner;
    
    public TimeStampNode(LayerNode owner) {
        this.owner = owner;
        
        setToolTip("the time-stamp of the layer");
        setText(owner.getLayer().getTimeStamp().toString());
    }
    
    public LayerNode getOwner() {
        return owner;
    }

    @Override
    public Icon getIcon() {
        if (owner.getLayer().getLayerVisualization() instanceof AnimatedVisualization) {
            return timeStampPlayIcon;
        }
        else {
            return timeStampIcon;
        }
    }
}