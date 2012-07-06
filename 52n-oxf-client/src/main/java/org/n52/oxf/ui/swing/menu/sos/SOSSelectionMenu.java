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

package org.n52.oxf.ui.swing.menu.sos;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.feature.sos.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.render.*;
import org.n52.oxf.render.sos.*;
import org.n52.oxf.serviceAdapters.*;
import org.n52.oxf.serviceAdapters.sos.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.menu.*;
import org.n52.oxf.ui.swing.sos.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.ui.swing.util.*;
import org.n52.oxf.util.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class SOSSelectionMenu extends Menu {

    static final Logger LOGGER = LoggingHandler.getLogger(SOSSelectionMenu.class);

    private JMenu layerRendererMenu;
    private JMenu chartRendererMenu;

    private JMenuItem timeCurveMI;

    private JMenuItem scatterPlotMI;

    private JMenuItem mapCurveMI;

    private JMenuItem animatedMapCurveMI;

    private JMenuItem idwInterpolationMI;

    private JMenuItem nnInterpolationMI;

    private JMenuItem proportionalPointMI;

    private JMenuItem windFieldMI;

    /**
     * @param map
     * @param tree
     * @param title
     */
    public SOSSelectionMenu(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map, tree, "SOS - Observations");

        layerRendererMenu = new JMenu("Render Layer");
        chartRendererMenu = new JMenu("Render Chart");

        timeCurveMI = initMenuItem("Time Series Chart", chartRendererMenu);
        scatterPlotMI = initMenuItem("Scatterplot", chartRendererMenu);

        proportionalPointMI = initMenuItem("Animated Proportional Circle Map", layerRendererMenu);
        mapCurveMI = initMenuItem("Time Series Map Charts", layerRendererMenu);
        animatedMapCurveMI = initMenuItem("Animated Bar Charts", layerRendererMenu);
        nnInterpolationMI = initMenuItem("Nearest-Neighbor Interpolation", layerRendererMenu);
        idwInterpolationMI = initMenuItem("Inverse-Distance Interpolation", layerRendererMenu);
        windFieldMI = initMenuItem("Wind-Field Map", layerRendererMenu);

        this.add(layerRendererMenu);
        this.add(chartRendererMenu);
    }

    private JMenuItem initMenuItem(String title, JMenu ownerMenu) {
        JMenuItem menuItem = new JMenuItem(title);
        ownerMenu.add(menuItem);
        menuItem.addActionListener(this);
        return menuItem;
    }

    public void actionPerformed(ActionEvent e) {

        List<IContextLayer> selectedLayers = tree.getSelectedLayers();

        if (selectedLayers.size() == 1 && selectedLayers.get(0) instanceof FeatureServiceLayer) {

            FeatureServiceLayer sourceLayer = (FeatureServiceLayer) selectedLayers.get(0);
            IServiceAdapter adapter = sourceLayer.getServiceAdapter();

            // go ahead if the sourceLayer is a SOS-Layer:
            if (adapter instanceof SOSAdapter) {
                SOSAdapter sosAdapter = (SOSAdapter) adapter;

                ServiceDescriptor descriptor = sourceLayer.getServiceDescriptor();

                if (e.getSource().equals(timeCurveMI)) {
                    SOSLayerAdder.createChart(owner,
                                              map,
                                              tree,
                                              sosAdapter,
                                              descriptor,
                                              new TimeSeriesChartRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }
                else if (e.getSource().equals(scatterPlotMI)) {
                    SOSLayerAdder.createChart(owner,
                                              map,
                                              tree,
                                              sosAdapter,
                                              descriptor,
                                              new ScatterPlotChartRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              2,
                                              2);
                }
                else if (e.getSource().equals(windFieldMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName() + " - Wind-Field Map - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new WindFieldRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }
                else if (e.getSource().equals(proportionalPointMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName()
                                                      + " - Animated Proportional Circle Map - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new ProportionalCircleMapRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }
                else if (e.getSource().equals(mapCurveMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName() + " - Map Charts - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new TimeSeriesMapChartRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }
                else if (e.getSource().equals(animatedMapCurveMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName()
                                                      + " - Animated Bar Charts - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new AnimatedMapBarChartRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              Integer.MAX_VALUE);
                }

                else if (e.getSource().equals(nnInterpolationMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName()
                                                      + " - Nearest-Neighbor Interpolation - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new NNRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }

                else if (e.getSource().equals(idwInterpolationMI)) {
                    SOSLayerAdder.addSOSLayer(owner,
                                              map,
                                              tree,
                                              sourceLayer.getIDName()
                                                      + " - Inverse-Distance Interpolation - 01",
                                              sourceLayer.getTitle(),
                                              sosAdapter,
                                              descriptor,
                                              new IDWRenderer(),
                                              new SOSObservationStore(),
                                              sourceLayer.getSelectedFeatures(),
                                              1,
                                              1);
                }
            }
        }
    }
}