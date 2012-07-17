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

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import org.n52.oxf.util.OXFEventException;

/**
 * This class is needed to realize the "CheckBox"-driven ContentTree.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ContentTreeEditor extends AbstractCellEditor implements TreeCellEditor {

    private ContentTreeRenderer renderer = new ContentTreeRenderer();
    // private ChangeEvent changeEvent = null;
    private ContentTree tree;

    public ContentTreeEditor(ContentTree tree) {
        this.tree = tree;
    }

    /**
     * this method will be executed when a cell/node of the tree is clicked/edited. If this node is an
     * instance of LayerNode the visibility of the Layer will be changed.
     */
    public Object getCellEditorValue() {

        DefaultMutableTreeNode node = tree.getSelectedNode();

        if (node instanceof LayerNode) {
            LayerNode layerNode = (LayerNode) node;

            try {
                layerNode.getLayer().setHidden( !layerNode.getLayer().isHidden());
            }
            catch (OXFEventException e) {
                e.printStackTrace();
            }

            return layerNode;
        }
        else {
            return node;
        }
    }

    /**
     * if clicked node instanceof LayerNode --> return true.
     */
    public boolean isCellEditable(EventObject event) {
        boolean returnValue = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;

            // only react on double-click:
            if (mouseEvent.getClickCount() == 2) {

                TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
                if (path != null) {
                    Object node = path.getLastPathComponent();
                    returnValue = (node != null) && (node instanceof LayerNode);
                }
            }
        }
        return returnValue;
    }

    public Component getTreeCellEditorComponent(JTree tree,
                                                Object value,
                                                boolean selected,
                                                boolean expanded,
                                                boolean leaf,
                                                int row) {
        Component editor = renderer.getTreeCellRendererComponent(tree,
                                                                 value,
                                                                 true,
                                                                 expanded,
                                                                 leaf,
                                                                 row,
                                                                 true);

        // editor always selected / focused
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
                if (stopCellEditing()) {
                    fireEditingStopped();
                }
            }
        };
        if (editor instanceof JCheckBox) {
            ((JCheckBox) editor).addItemListener(itemListener);
        }

        return editor;
    }

}