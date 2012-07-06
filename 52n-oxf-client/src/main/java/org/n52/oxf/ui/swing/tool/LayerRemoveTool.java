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
public class LayerRemoveTool extends MapTool {

    private static final Logger LOGGER = LoggingHandler.getLogger(LayerRemoveTool.class);

    private ContentTree tree;

    /**
     * 
     */
    public LayerRemoveTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("delete_layer.gif")));
        setToolTipText("Remove layer");
    }

    public void activate() {
        super.activate();

        try {
            removeLayer();
        }
        catch (OXFEventException e) {
            LOGGER.error(e);
        }
        catch (OXFException e) {
            LOGGER.error(e);
        }
    }

    /**
     * removes the selected <code>LayerNode</code> (respectively <code>LayerStorageNode</code>) from the
     * ContentTree (view) and the associated <code>Layer</code>s from the LayerContext (model).
     * 
     * @throws OXFException
     * 
     * @throws OXFException
     * 
     */
    private void removeLayer() throws OXFEventException, OXFException {

        DefaultMutableTreeNode node = tree.getSelectedNode();

        if ( !node.isRoot()) {

            //
            // removing from "model":
            //
            LayerContext layerContext = map.getLayerContext();
            List<IContextLayer> layers = tree.getSelectedLayers();
            for (IContextLayer layer : layers) {
                layerContext.removeLayer(layer);
            }

            //
            // removing from "view":
            //
            if (node instanceof LayerNode) {
                if (node.getParent().getChildCount() == 1) {
                    // if LayerNode is the only child -> remove the complete LayerStorageNode:
                    tree.removeNode((DefaultMutableTreeNode) node.getParent());
                }
                else {
                    tree.removeNode(node);
                }
            }
            else if (node instanceof LayerStorageNode) {
                tree.removeNode(node);
            }
        }
    }
}