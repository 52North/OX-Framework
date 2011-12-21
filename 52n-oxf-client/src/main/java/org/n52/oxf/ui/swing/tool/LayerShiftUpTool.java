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

package org.n52.oxf.ui.swing.tool;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.context.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.icons.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class LayerShiftUpTool extends MapTool {

    private static final Logger LOGGER = LoggingHandler.getLogger(LayerShiftUpTool.class);

    private ContentTree tree;

    /**
     * 
     */
    public LayerShiftUpTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("arrows_up.gif")));
        setToolTipText("Shift layer up");
    }

    public void activate() {
        super.activate();

        try {
            shiftLayerUp();
        }
        catch (OXFException e) {
            LOGGER.error(e);
        }
        catch (OXFEventException e) {
            LOGGER.error(e);
        }
    }

    /**
     * 
     * @throws OXFException
     * @throws OXFEventException 
     */
    private void shiftLayerUp() throws OXFException, OXFEventException {

        DefaultMutableTreeNode node = tree.getSelectedNode();

        if ( ! node.isRoot()) {

            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

            int position = parent.getIndex(node);
            if (position > 0) { // node is not at first position

                //
                // shifting up in "model":
                //
                LayerContext context = map.getLayerContext();

                if (node instanceof LayerStorageNode) {
                    LayerStorageNode storageNode = (LayerStorageNode) node;
                    LayerStorageNode precedingStorageNode = (LayerStorageNode) parent.getChildBefore(storageNode);

                    IContextLayer firstLayerOfPrecedingNode = precedingStorageNode.getLayerNodes().get(0).getLayer();

                    // posA := position of the firstLayer of the preceding LayerStorageNode
                    int posA = context.getPosition(firstLayerOfPrecedingNode);

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

                    context.shiftUp(layer);
                }

                //
                // shifting up in "view":
                //
                tree.shiftUpNode(node);
            }
        }
    }
}