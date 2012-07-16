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

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.layer.IContextLayer;
import org.n52.oxf.layer.IFeatureLayer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class SelectTool extends MapTool {

    private static final long serialVersionUID = 287693998828816554L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectTool.class);

    private Point startPoint = null;

    private ContentTree tree;

    private boolean startDrawingRect = false;

    /**
     * 
     */
    public SelectTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("select.gif")));
        setToolTipText("Select Features");
    }

    public void activate() {
        super.activate();
    }

    public void mousePressed(MouseEvent evt) {
        List<IContextLayer> selectedLayers = tree.getSelectedLayers();
        if (selectedLayers.size() == 1 && selectedLayers.get(0) instanceof IFeatureLayer) {
            rectStart(evt);

            startDrawingRect = true;
        }
        else {
            startDrawingRect = false;
            JOptionPane.showMessageDialog(this,
                                          "Please select one FeatureLayer in the ContentTree!",
                                          "Problem occured",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void mouseDragged(MouseEvent evt) {
        if (startDrawingRect) {
            rectDragged(evt);
        }
    }

    public void mouseReleased(MouseEvent evt) {
        if (startDrawingRect) {
            try {
                rectReleased(evt);
            }
            catch (OXFEventException e) {
                LOGGER.error("Could not release rectangle.", e);
            }
            catch (OXFException e) {
                LOGGER.error("Could not release rectangle.", e);
            }
        }
    }

    /**
     * 
     * @param event
     */
    private void rectStart(MouseEvent event) {
        startPoint = new Point(event.getX(), event.getY());
    }

    /**
     * 
     * @param event
     */
    private void rectDragged(MouseEvent event) {
        map.drawRect(startPoint.x, startPoint.y, event.getX(), event.getY(), Color.blue);
    }

    /**
     * 
     * @param event
     * @throws OXFEventException
     * @throws OXFException
     */
    private void rectReleased(MouseEvent event) throws OXFEventException, OXFException {

        Point endPoint = new Point(event.getX(), event.getY());
        int llX = Math.min(endPoint.x, startPoint.x);
        int llY = Math.min(endPoint.y, startPoint.y);
        int urX = Math.max(endPoint.x, startPoint.x);
        int urY = Math.max(endPoint.y, startPoint.y);

        List<IContextLayer> selectedLayers = tree.getSelectedLayers();

        IFeatureLayer featureLayer = (IFeatureLayer) selectedLayers.get(0);
        Set<OXFFeature> selectedFeatures = featureLayer.pickFeature(llX, llY, urX, urY);

        if (selectedFeatures != null) {
            map.getLayerContext().selectFeatures(featureLayer, selectedFeatures);

            map.repaint();
        }
        else {
            LOGGER.info("No FeaturePicker defined for the selected Layer");
        }
    }
}