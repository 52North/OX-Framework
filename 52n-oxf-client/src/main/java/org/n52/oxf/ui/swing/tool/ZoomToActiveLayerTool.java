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
import org.apache.log4j.*;
import org.n52.oxf.context.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.capabilities.*;
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
public class ZoomToActiveLayerTool extends MapTool {

    private static final Logger LOGGER = LoggingHandler.getLogger(ZoomToActiveLayerTool.class);

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
            LOGGER.error(e, e);
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