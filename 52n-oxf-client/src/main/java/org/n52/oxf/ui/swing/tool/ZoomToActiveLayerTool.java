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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.layer.AbstractServiceLayer;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZoomToActiveLayerTool extends MapTool {

    private static final long serialVersionUID = -5340460863424024536L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ZoomToActiveLayerTool.class);
    
    private ContentTree tree;

    /**
     * 
     */
    public ZoomToActiveLayerTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("zoom_to_active_layer.gif")));
        setToolTipText("Zoom to active layer");
    }

    public void activate() {
        super.activate();

        try {
            zoomToActiveLayer(map, tree);
        }
        catch (OXFEventException e) {
            LOGGER.error("Could not zoom to active layer.", e);
        }
    }

    public static void zoomToActiveLayer(MapCanvas map, ContentTree tree) throws OXFEventException {

        List<IContextLayer> layerList = tree.getSelectedLayers();
        String contextSRS = map.getLayerContext().getContextBoundingBox().getSRS();

        if (layerList.size() >= 1) {
            if (layerList.get(0) instanceof AbstractServiceLayer) {
                AbstractServiceLayer layer = (AbstractServiceLayer) layerList.get(0);

                IBoundingBox fittingBBox = null;

                IBoundingBox[] bBoxes = layer.getServiceDescriptor().getContents().getDataIdentification(layer.getIDName()).getBoundingBoxes();

                for (IBoundingBox b : bBoxes) {
                    if (b.getCRS().equalsIgnoreCase(contextSRS)) {
                        fittingBBox = b;
                    }
                }

                if (fittingBBox != null) {
                    map.getLayerContext().getContextBoundingBox().setBBox(fittingBBox, true);
                    map.getLayerContext().getContextBoundingBox().fitBBox2Screen(map.getWidth(),
                                                                                 map.getHeight(),
                                                                                 ContextBoundingBox.FIT_MAXIMUM,
                                                                                 false);
                }
                else {
                    JOptionPane.showMessageDialog(map,
                                                  "The activated layer has no BoundingBox fitting the actual Context-SRS.");
                }
            }
        }
    }
}