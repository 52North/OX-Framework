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

package org.n52.oxf.ui.swing;

import java.awt.event.ItemEvent;

import org.n52.oxf.context.ContextBoundingBox;
import org.n52.oxf.ows.capabilities.IBoundingBox;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class BBoxSelectionPanelController {

    private BBoxSelectionPanel view;

    /**
     * the BBoxes associated with the layer.
     */
    private IBoundingBox[] bboxes;

    /**
     * 
     */
    public BBoxSelectionPanelController(BBoxSelectionPanel view) {
        super();

        this.view = view;
    }

    public void postInit(String[] supportedSrsOfDataResource, IBoundingBox[] bboxes) {
        this.bboxes = bboxes;

        // layer is the first in the LayerContext:
        if (view.getMap().getLayerContext().getLayerCount() == 0) {
            // add crs' which act as identifiers for the BBoxes.
            for (int i = 0; i < bboxes.length; i++) {
                view.getSrsCB().addItem(bboxes[i].getCRS());
            }
        }

        // layer is an additional one in the LayerContext:
        else {
            loadContextBBoxValues(supportedSrsOfDataResource);

            view.getSrsCB().setEnabled(false);
            view.getMaxXTextField().setEditable(false);
            view.getMaxYTextField().setEditable(false);
            view.getMinXTextField().setEditable(false);
            view.getMinYTextField().setEditable(false);
        }
    }

    public void itemStateChanged_srsCB(ItemEvent e) {
        String selectedSrs = (String) view.getSrsCB().getSelectedItem();

        IBoundingBox bbox = searchBBox(selectedSrs);

        if (bbox != null) {
            view.getMaxXTextField().setText("" + bbox.getUpperCorner()[0]);
            view.getMaxYTextField().setText("" + bbox.getUpperCorner()[1]);
            view.getMinXTextField().setText("" + bbox.getLowerCorner()[0]);
            view.getMinYTextField().setText("" + bbox.getLowerCorner()[1]);
        }
    }

    /**
     * 
     * @param srs
     * @return
     */
    private IBoundingBox searchBBox(String srs) {
        for (IBoundingBox bbox : bboxes) {
            if (bbox.getCRS().equalsIgnoreCase(srs)) {
                return bbox;
            }
        }
        return null;
    }

    /**
     * loads in the values of the actual ContextBoundingBox but only if the actual context-SRS is supported by
     * the layer.
     * 
     * @return true if the Layer supports a suitable SRS for the actual ContextBoundingBox. false otherwise.
     */
    public boolean loadContextBBoxValues(String[] supportedSrsOfDataResource) {

        ContextBoundingBox cBBox = view.getMap().getLayerContext().getContextBoundingBox();

        if (cBBox != null) {
            String srs = cBBox.getSRS();

            if (srs != null) {
                if (supportsSRS(supportedSrsOfDataResource, srs) == true) {
                    view.getSrsCB().addItem(srs);
                    view.getSrsCB().setSelectedItem(srs);

                    view.getMaxXTextField().setText("" + cBBox.getUpperRight().getX());
                    view.getMaxYTextField().setText("" + cBBox.getUpperRight().getY());
                    view.getMinXTextField().setText("" + cBBox.getLowerLeft().getX());
                    view.getMinYTextField().setText("" + cBBox.getLowerLeft().getY());

                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * 
     * @param selectedLayer
     * @param srs
     * @return true if the selectedLayer supports the specified srs.
     */
    private boolean supportsSRS(String[] srsArray, String srs) {
        for (String tmpSRS : srsArray) {
            if (tmpSRS.equalsIgnoreCase(srs)) {
                return true;
            }
        }
        return false;
    }
}