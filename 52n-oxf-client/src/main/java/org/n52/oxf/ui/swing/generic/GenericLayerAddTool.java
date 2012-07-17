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

package org.n52.oxf.ui.swing.generic;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.layer.RasterServiceLayer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IRasterDataRenderer;
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.icons.IconAnchor;
import org.n52.oxf.ui.swing.tool.MapTool;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.util.LayerAdder;
import org.n52.oxf.util.OXFEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericLayerAddTool extends MapTool {
    
    private static final long serialVersionUID = -3260549803644661099L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericLayerAddTool.class);

    private ContentTree tree;

    public GenericLayerAddTool(JFrame owner, MapCanvas map, ContentTree tree) {
        super(owner, map);

        this.tree = tree;

        setIcon(new ImageIcon(IconAnchor.class.getResource("add_layer.gif")));
        setToolTipText("Add layer");
    }

    public void activate() {
        super.activate();

        try {
            addLayer();
        }
        catch (OXFException e) {
            LOGGER.error("Could not add layer.", e);
        }
        catch (ExceptionReport e) {
            LOGGER.error("Could not add layer.", e);
        }
        catch (OXFEventException e) {
            LOGGER.error("Could not add layer.", e);
        }
    }

    /**
     * calls the ConnectServiceDialog and the GenericParameterConfigurator and adds after that the configured
     * Layer to the map and the tree.
     * 
     * @throws OXFException
     * @throws ExceptionReport
     * @throws OXFEventException
     */
    protected void addLayer() throws OXFException, ExceptionReport, OXFEventException {

        ConnectServiceDialog csd = new ConnectServiceDialog();
        int csdReturnVal = csd.showDialog();

        if (csdReturnVal == ConnectServiceDialog.APPROVE_OPTION) {
            IServiceAdapter adapter = csd.getSelectedServiceAdapter();
            IRenderer renderer = csd.getSelectedRenderer();
            String serviceURL = csd.getSelectedServiceURL();

            ServiceDescriptor descriptor = adapter.initService(serviceURL);

            List<Parameter> parameterList = descriptor.getOperationsMetadata().getOperationByName(adapter.getResourceOperationName()).getParameters();
            Parameter[] params = new Parameter[parameterList.size()];
            parameterList.toArray(params);

            GenericParameterConfigurator gpc = new GenericParameterConfigurator();
            int gpcReturnVal = gpc.showDialog(params);

            if (gpcReturnVal == GenericParameterConfigurator.APPROVE_OPTION) {
                ParameterContainer paramContainer = gpc.getFilledParameterContainer();

                String layerId = (String) paramContainer.getParameterShellWithCommonName(Parameter.COMMON_NAME_RESOURCE_ID).getSpecifiedValue();
                String layerTitle = descriptor.getContents().getDataIdentification(layerId).getTitle();

                RasterServiceLayer layer = new RasterServiceLayer(adapter,
                                                                  (IRasterDataRenderer) renderer,
                                                                  descriptor,
                                                                  paramContainer,
                                                                  layerId,
                                                                  layerTitle,
                                                                  adapter.getResourceOperationName());
                LayerAdder.addLayer(map, tree, layer);
            }
        }
    }
}