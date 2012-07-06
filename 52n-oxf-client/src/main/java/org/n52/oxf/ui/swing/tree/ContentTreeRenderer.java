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

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.apache.log4j.*;
import org.n52.oxf.ui.swing.icons.*;
import org.n52.oxf.util.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class ContentTreeRenderer implements TreeCellRenderer {

    private static Logger LOGGER = LoggingHandler.getLogger(ContentTreeRenderer.class);

    private Color selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
    private Color selectionForeground = UIManager.getColor("Tree.selectionForeground");
    private Color selectionBackground = UIManager.getColor("Tree.selectionBackground");
    private Color textForeground = UIManager.getColor("Tree.textForeground");
    private Color textBackground = UIManager.getColor("Tree.textBackground");

    private Font fontValue = UIManager.getFont("Tree.font");

    public ContentTreeRenderer() {

//        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
//        checkBoxRenderer.setFocusPainted( (booleanValue != null) && (booleanValue.booleanValue()));
//
//        defaultRenderer.setBackgroundSelectionColor(selectionBackground);
//        defaultRenderer.setBorderSelectionColor(selectionBorderColor);
    }

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {

        if ( (value != null) && (value instanceof LayerNode)) {
            return renderLayerNode(tree, (LayerNode) value, selected, expanded, leaf, row, hasFocus);
        }

        else if ( (value != null) && (value instanceof AbstractTreeNode)) {
            return renderDefaultNode(tree, (AbstractTreeNode) value, selected, expanded, leaf, row, hasFocus);
        }
        
        return null;
    }



    private Component renderLayerNode(JTree tree,
                                      LayerNode layerNode,
                                      boolean selected,
                                      boolean expanded,
                                      boolean leaf,
                                      int row,
                                      boolean hasFocus) {
        JCheckBox checkBoxRenderer = new JCheckBox();

        if (fontValue != null) {
            checkBoxRenderer.setFont(fontValue);
        }

        if (selected) {
            checkBoxRenderer.setForeground(selectionForeground);
            checkBoxRenderer.setBackground(selectionBackground);
        }
        else {
            checkBoxRenderer.setForeground(textForeground);
            checkBoxRenderer.setBackground(textBackground);
        }

        checkBoxRenderer.setText(layerNode.getText());
        checkBoxRenderer.setSelected( !layerNode.getLayer().isHidden());

        return checkBoxRenderer;
    }
    

    private DefaultTreeCellRenderer renderDefaultNode(JTree tree, AbstractTreeNode node, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();
        
        defaultRenderer.getTreeCellRendererComponent(tree,
                                                     node,
                                                     selected,
                                                     expanded,
                                                     leaf,
                                                     row,
                                                     hasFocus);
        
        if (fontValue != null) {
            defaultRenderer.setFont(fontValue);
        }
        
        defaultRenderer.setIcon(node.getIcon());
        defaultRenderer.setToolTipText(node.getToolTip());
        defaultRenderer.setText(node.getText());
        
        return defaultRenderer;
    }
}