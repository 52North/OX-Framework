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

package org.n52.oxf.ui.swing.sos;

import java.net.*;

import javax.swing.*;

import org.n52.oxf.*;
import org.n52.oxf.adapter.sos.*;
import org.n52.oxf.feature.sos.*;
import org.n52.oxf.ows.*;
import org.n52.oxf.plugin.*;
import org.n52.oxf.plugin.RendererFactory.*;
import org.n52.oxf.render.*;
import org.n52.oxf.render.sos.*;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.feature.SOSObservationStore;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChooseRendererDialogController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ChooseRendererDialogController.class);

    private ChooseRendererDialog view;
    private URL sosUrl;
    private MapCanvas map;
    private ContentTree tree;
    private SOSAdapter adapter;

    public ChooseRendererDialogController(ChooseRendererDialog view,
                                          MapCanvas map,
                                          ContentTree tree,
                                          URL sosUrl,
                                          SOSAdapter adapter) {
        this.view = view;
        this.sosUrl = sosUrl;
        this.adapter = adapter;
        this.map = map;
        this.tree = tree;
        postInitView();
    }

    public void postInitView() {
        view.getUrlTextField().setText(sosUrl.toString());

        // TODO: verf�gbare Renderer aus Konfigurationsdatei auslesen

        //((DefaultListModel) view.getRendererList().getModel()).addElement(new IDWRenderer());

        ((DefaultListModel) view.getRendererList().getModel()).addElement(new NNRenderer());

        ((DefaultListModel) view.getRendererList().getModel()).addElement(new AnimatedMapBarChartRenderer());

        ((DefaultListModel) view.getRendererList().getModel()).addElement(new ProportionalCircleMapRenderer());

        ((DefaultListModel) view.getRendererList().getModel()).addElement(new TimeSeriesMapChartRenderer());

        // Chart-Renderer:
        ((DefaultListModel) view.getRendererList().getModel()).addElement(new ScatterPlotChartRenderer());

        ((DefaultListModel) view.getRendererList().getModel()).addElement(new TimeSeriesChartRenderer());
    }

    public void pluginRendererButton_actionPerformed() {
        
        PluginRendererDialog pluginDialog = new PluginRendererDialog(view);
        pluginDialog.setVisible(true);
        String className = pluginDialog.getClassName();
        
        JFileChooser chooser = new JFileChooser();
        FileFilterImpl filter = new FileFilterImpl();
        filter.addExtension("class");
        filter.setDescription("OX-Framework Renderer");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(view);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           
            try {
                URL classPath = chooser.getSelectedFile().toURL();
                System.out.println(classPath);
                
                ClassLoader loader = URLClassLoader.newInstance(new URL[] { classPath });
                
                Class<IRenderer> rendererClass = (Class<IRenderer>) loader.loadClass(className);
                IRenderer rendererInstance = rendererClass.newInstance();
                
                ((DefaultListModel) view.getRendererList().getModel()).addElement(rendererInstance);
            } catch (Exception e) {
                LOGGER.warn("Could load renderer '" + className + "'.", e);
            }
        }
    }

    public void selectButton_actionPerformed() {

        if (view.getRendererList().getSelectedValues() != null
                && view.getRendererList().getSelectedValues().length > 0) {
            IRenderer renderer = (IRenderer) view.getRendererList().getSelectedValues()[0];

            ServiceDescriptor descriptor = null;
            try {
                descriptor = adapter.initService(sosUrl.toString());
            }
            catch (ExceptionReport e) {
                JOptionPane.showMessageDialog(view,
                                              "The request sended to the SOS produced a service sided exception. Please view the logs for details.",
                                              "Error",
                                              JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            catch (OXFException e) {
                if (e.getCause() instanceof ConnectException) {
                    JOptionPane.showMessageDialog(view, "Could not connect to service!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                e.printStackTrace();
            }
            if (renderer instanceof TimeSeriesChartRenderer) {
                SOSLayerAdder.createChart(view,
                                          map,
                                          tree,
                                          adapter,
                                          descriptor,
                                          new TimeSeriesChartRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }
            else if (renderer instanceof ScatterPlotChartRenderer) {
                SOSLayerAdder.createChart(view,
                                          map,
                                          tree,
                                          adapter,
                                          descriptor,
                                          new ScatterPlotChartRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          2,
                                          2);
            }
            else if (renderer instanceof WindFieldRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Wind-Field Map - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new WindFieldRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }
            else if (renderer instanceof ProportionalCircleMapRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Animated Proportional Circle Map - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new ProportionalCircleMapRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }
            else if (renderer instanceof TimeSeriesMapChartRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Map Charts - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new TimeSeriesMapChartRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }
            else if (renderer instanceof AnimatedMapBarChartRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Animated Bar Charts - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new AnimatedMapBarChartRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          Integer.MAX_VALUE);
            }

            else if (renderer instanceof NNRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Nearest-Neighbor Interpolation - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new NNRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }

            else if (renderer instanceof IDWRenderer) {
                SOSLayerAdder.addSOSLayer(view,
                                          map,
                                          tree,
                                          "Inverse-Distance Interpolation - 01",
                                          "Title",
                                          adapter,
                                          descriptor,
                                          new IDWRenderer(),
                                          new SOSObservationStore(),
                                          null,
                                          1,
                                          1);
            }
        }
    }

    public void cancelButton_actionPerformed() {
        view.setVisible(false);
    }

}