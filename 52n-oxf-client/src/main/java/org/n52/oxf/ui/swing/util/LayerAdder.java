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

package org.n52.oxf.ui.swing.util;

import javax.swing.*;
import org.n52.oxf.*;
import org.n52.oxf.context.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tool.ZoomToActiveLayerTool;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.util.*;
import org.n52.oxf.valueDomains.spatial.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class LayerAdder {

    /**
     * adds the Layer to the map, the tree and the layerContext.
     * 
     * @param map
     * @param tree
     * @param context
     * @param layer
     * @throws OXFException
     * @throws OXFEventException
     */
    public static void addLayer(MapCanvas map, ContentTree tree, IContextLayer layer) throws OXFException,
            OXFEventException {

        LayerContext context = map.getLayerContext();

        // if the context already contains a Layer with the specified layerId
        if (context.contains(layer.getIDName())) {
            JOptionPane.showMessageDialog(null,
                                          "The LayerContext already contains a Layer with the layerId '"
                                                  + layer.getIDName() + "'",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
        else {

            // if this layer is the FIRST in the LayerContext: use its BoundingBox as
            // ContextBoundingBox and its SRS as the srs of the context.
            if (map.getLayerContext().getLayerCount() == 0) {

                ContextBoundingBox contextBBox = map.getLayerContext().getContextBoundingBox();

                if (layer.getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX)) {
                    BoundingBox bBox = (BoundingBox) layer.getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_BBOX).getSpecifiedValue();

                    contextBBox.setBBox(bBox, true);

                    contextBBox.fitBBox2Screen(map.getLayerContext().getContextWindow().getWidth(),
                                               map.getLayerContext().getContextWindow().getHeight(),
                                               ContextBoundingBox.FIT_MAXIMUM,
                                               true);
                }
                else {
                    throw new OXFException("The extent of the ContextBoundingBox has to be set.");
                }

                if (layer.getParameterContainer().containsParameterShellWithCommonName(Parameter.COMMON_NAME_SRS)) {
                    String srs = (String) layer.getParameterContainer().getParameterShellWithCommonName(Parameter.COMMON_NAME_SRS).getSpecifiedValue();
                    contextBBox.setSRS(srs);
                }
            }

            //
            // add to view:
            //
            String serviceTitle = layer.getLayerSourceTitle();
            String serviceType = layer.getLayerSourceType();

            LayerNode layerNode = null;
            LayerStorageNode layerStorageNode = tree.getLayerStorageNode(serviceTitle, serviceType);
            if (layerStorageNode != null) {
                layerNode = new LayerNode(layer, layerStorageNode);
            }
            else {
                layerStorageNode = new LayerStorageNode(serviceTitle, serviceType);
                layerNode = new LayerNode(layer, layerStorageNode);
                tree.addLayerStorageNode(layerStorageNode);

            }
            tree.addLayerNode(layerStorageNode, layerNode);

            //
            // add to model:
            //
            context.addLayer(tree.positionOf(layerNode), layer);
        }
    }

}