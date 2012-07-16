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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.IEventListener;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentTree extends JTree implements IEventListener {

    private static final long serialVersionUID = -7139084068103317928L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentTree.class);
    
    protected RootNode root;
    protected DefaultTreeModel treeModel;

    public ContentTree(MapCanvas map) {
        super();

        map.getLayerContext().addEventListener(this);

        root = new RootNode();
        treeModel = new DefaultTreeModel(root);
        TreeSelectionModel treeSelectionModel = new DefaultTreeSelectionModel();
        treeSelectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setSelectionModel(treeSelectionModel);
        setModel(treeModel);
        setEditable(true);
        setShowsRootHandles(false);
        setCellRenderer(new ContentTreeRenderer());
        setCellEditor(new ContentTreeEditor(this));
    }

    public void addLayerStorageNode(LayerStorageNode layerStorageNode) {
        treeModel.insertNodeInto(layerStorageNode, root, root.getChildCount());

        TreeNode[] path = treeModel.getPathToRoot(root);
        expandPath(new TreePath(path));
        setSelectionPath(new TreePath(path));
    }

    public void addLayerNode(LayerStorageNode parent, LayerNode child) {
        treeModel.insertNodeInto(child, parent, parent.getChildCount());

        TreeNode[] path = treeModel.getPathToRoot(parent);
        expandPath(new TreePath(path));
        setSelectionPath(new TreePath(path));
    }

    /**
     * 
     * @param node
     */
    public void removeNode(DefaultMutableTreeNode node) {
        TreeNode parent = node.getParent();
        TreeNode[] path = treeModel.getPathToRoot(parent);
        treeModel.removeNodeFromParent(node);
        setSelectionPath(new TreePath(path));
    }

    /**
     * 
     * @param title
     * @param type
     * @return
     */
    public LayerStorageNode getLayerStorageNode(String title, String type) {
        for (int i = 0; i < root.getChildCount(); i++) {
            LayerStorageNode child = (LayerStorageNode) root.getChildAt(i);
            if (child.getTitle().equals(title) && child.getType().equals(type)) {
                return child;
            }
        }
        return null;
    }

    /**
     * shifts up the specified DefaultMutableTreeNode; but only if its position is not directly below its
     * parent. So a LayerNode cannot leave its LayerStoragNode and a LayerStorageNode cannot be moved above
     * the root node.
     * 
     * @param node
     */
    public void shiftUpNode(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

        int position = parent.getIndex(node);
        if (position > 0) { // node is not at first position
            treeModel.removeNodeFromParent(node);
            treeModel.insertNodeInto(node, parent, --position);

            TreeNode[] path = treeModel.getPathToRoot(node);
            expandPath(new TreePath(path));
            setSelectionPath(new TreePath(path));
        }
    }

    /**
     * shifts down the specified DefaultMutableTreeNode; but only if the node is not at last position. So a
     * LayerNode cannot leave its LayerStoragNode.
     * 
     * @param node
     */
    public void shiftDownNode(DefaultMutableTreeNode node) {
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

        int position = parent.getIndex(node);
        if (position < parent.getChildCount()) { // node is not at last position
            treeModel.removeNodeFromParent(node);
            treeModel.insertNodeInto(node, parent, ++position);

            TreeNode[] path = treeModel.getPathToRoot(node);
            expandPath(new TreePath(path));
            setSelectionPath(new TreePath(path));
        }
    }

    /**
     * 
     * 
     */
    public void clearTree() {
        while (root.getChildCount() > 0) {
            removeNode(root.getFirstLeaf());
        }
    }

    /**
     * Returns the position of the specified LayerNode or -1 if it is not contained in the ContentTree.<br>
     * In the following example this method will return 4 for the 'X'-LayerNode.
     * 
     * <pre>
     *   -O
     *    |-O
     *    |-O
     *    |-O
     *   -O
     *    |-O
     *    |-X
     *    |-O
     * 
     * &#064;param layerNode
     * @return
     * 
     */
    public int positionOf(LayerNode layerNode) {
        int position = 0;
        for (int i = 0; i < root.getChildCount(); i++) {
            TreeNode storageNode = root.getChildAt(i);

            for (int j = 0; j < storageNode.getChildCount(); j++) {

                if (storageNode.getChildAt(j) != layerNode) {
                    position++;
                }
                else {
                    return position;
                }
            }
        }

        return -1;
    }

    /**
     * 
     * @return
     */
    public DefaultMutableTreeNode getSelectedNode() {
        TreePath tp = getLeadSelectionPath();

        if (tp != null) {
            return (DefaultMutableTreeNode) tp.getLastPathComponent();
        }
            return null;
    }

    /**
     * 
     * @return - if 1 or more <code>LayerNode<code>s are selected, their <code>Layer<code>s will be returned.
     *         <br> if a <code>LayerStorageNode<code> is selected, all its contained <code>Layer<code>s will be returned.
     */
    public List<IContextLayer> getSelectedLayers() {
        List<IContextLayer> selectedLayers = new ArrayList<IContextLayer>();

        DefaultMutableTreeNode selectedNode = getSelectedNode();

        if (selectedNode instanceof LayerNode) {
            LayerNode layerNode = (LayerNode) selectedNode;
            selectedLayers.add(layerNode.getLayer());
        }
        else if (selectedNode instanceof LayerStorageNode) {
            LayerStorageNode layerStorageNode = (LayerStorageNode) selectedNode;

            List<LayerNode> layerNodes = layerStorageNode.getLayerNodes();

            for (LayerNode layerNode : layerNodes) {
                selectedLayers.add(layerNode.getLayer());
            }
        }

        return selectedLayers;
    }

    /**
     * 
     */
    public void eventCaught(OXFEvent evt) throws OXFEventException {

        // events from LayerContext:
        if (evt.getName().equals(EventName.NEW_LAYER_ADDED)) {

            IContextLayer layer = (IContextLayer) evt.getValue();
            layer.addEventListener(this);
        }

        else if (evt.getName().equals(EventName.LAYER_REMOVED)) {
            LOGGER.info("Event not handled... " + evt);
        }

        else if (evt.getName().equals(EventName.LAYER_SHIFTED_DOWN)) {
            LOGGER.info("Event not handled... " + evt);
        }

        else if (evt.getName().equals(EventName.LAYER_SHIFTED_UP)) {
            LOGGER.info("Event not handled... " + evt);
        }

        // events from Layer:
        else if (evt.getName().equals(EventName.LAYER_VISIBILITY_CHANGED)) {
            LOGGER.info("Event not handled... " + evt);
        }
    }

}