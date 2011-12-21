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

package org.n52.oxf.ui.swing.generic;

import java.util.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.n52.oxf.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.render.*;
import org.n52.oxf.serviceAdapters.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.icons.*;
import org.n52.oxf.ui.swing.tool.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.ui.swing.util.*;
import org.n52.oxf.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class GenericLayerAddTool extends MapTool {

    private static final Logger LOGGER = LoggingHandler.getLogger(GenericLayerAddTool.class);

    private ContentTree tree;

    /**
     * 
     */
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
            LOGGER.error(e, e);
        }
        catch (ExceptionReport e) {
            LOGGER.error(e, e);
        }
        catch (OXFEventException e) {
            LOGGER.error(e, e);
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