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

package org.n52.oxf.ui.swing.tool;

import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.n52.oxf.OXFException;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.tree.LayerNode;
import org.n52.oxf.ui.swing.tree.LayerStorageNode;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LayerShiftDownTool extends MapTool {

    private static final long serialVersionUID = 860306209459953381L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LayerShiftDownTool.class);
    
    private ContentTree tree;

    /**
     * 
     */
    public LayerShiftDownTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("arrows_down.gif")));
        setToolTipText("Shift layer down");
    }

    public void activate() {
        super.activate();

        try {
            shiftLayerDown();
        }
        catch (OXFException e) {
            LOGGER.error("Could not shift layer.", e);
        }
        catch (OXFEventException e) {
            LOGGER.error("Could not shift layer.", e);
        }
    }

    /**
     * 
     * @throws OXFException
     * @throws OXFEventException 
     */
    private void shiftLayerDown() throws OXFException, OXFEventException {

        DefaultMutableTreeNode node = tree.getSelectedNode();

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

        if (parent != null) { // <- parent ==root => null

            int position = parent.getIndex(node);
            if (position + 1 < parent.getChildCount()) { // node is not at last position

                //
                // shifting up in "model":
                //
                LayerContext context = map.getLayerContext();

                if (node instanceof LayerStorageNode) {
                    LayerStorageNode storageNode = (LayerStorageNode) node;
                    LayerStorageNode followingStorageNode = (LayerStorageNode) parent.getChildAfter(storageNode);

                    List<LayerNode> followingLayerList = followingStorageNode.getLayerNodes();
                    IContextLayer lastLayerOfFollowingNode = followingLayerList.get(followingLayerList.size() - 1).getLayer();

                    // posA := position of the lastLayer of the following LayerStorageNode
                    int posA = context.getPosition(lastLayerOfFollowingNode);

                    List<LayerNode> layerNodes = storageNode.getLayerNodes();
                    List<IContextLayer> layerList = new Vector<IContextLayer>(layerNodes.size());

                    for (LayerNode layerNode : layerNodes) {
                        layerList.add(layerNode.getLayer());
                    }

                    context.moveLayer(posA, layerList);
                }
                else if (node instanceof LayerNode) {
                    LayerNode layerNode = (LayerNode) node;
                    IContextLayer layer = layerNode.getLayer();

                    int layerPos = parent.getIndex(layerNode);

                    context.shiftDown(layer);
                }

                //
                // shifting up in "view":
                //
                tree.shiftDownNode(node);
            }
        }
    }
}