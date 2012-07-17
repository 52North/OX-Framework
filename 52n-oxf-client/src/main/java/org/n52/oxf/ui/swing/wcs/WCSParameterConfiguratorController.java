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

package org.n52.oxf.ui.swing.wcs;

import java.awt.event.*;
import javax.swing.*;
import org.n52.oxf.*;
import org.n52.oxf.adapter.*;
import org.n52.oxf.adapter.wcs.*;
import org.n52.oxf.adapter.wms.*;
import org.n52.oxf.adapter.wms.caps.*;
import org.n52.oxf.layer.*;
import org.n52.oxf.owsCommon.*;
import org.n52.oxf.owsCommon.capabilities.*;
import org.n52.oxf.render.wms.*;
import org.n52.oxf.ui.swing.*;
import org.n52.oxf.ui.swing.tree.*;
import org.n52.oxf.ui.swing.util.*;
import org.n52.oxf.util.*;

public class WCSParameterConfiguratorController {

    private WCSParameterConfigurator view;
    private ServiceDescriptor serviceDesc;
    private MapCanvas map;
    private ContentTree tree;
    private String version;

    public WCSParameterConfiguratorController(WCSParameterConfigurator view,
                                              ServiceDescriptor serviceDesc,
                                              MapCanvas map,
                                              ContentTree tree) {
        this.view = view;
        this.serviceDesc = serviceDesc;
        this.map = map;
        this.tree = tree;

        this.version = serviceDesc.getServiceIdentification().getServiceTypeVersion()[0];
    }

    /**
     * 
     * 
     */
    public void postInit() {

        // init layersCB:
        for (int i = 0; i < serviceDesc.getContents().getDataIdentificationCount(); i++) {
            view.getCoveragesCB().addItem(serviceDesc.getContents().getDataIdentification(i));
        }
    }

    public void actionPerformed_showRequestButton(ActionEvent e) {

        // is there a suitable BBox:
        if (view.getBBoxPanel().getSrsCB().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(view,
                                          "This Layer supports no suitable BBox/SRS for the actual ContextBoundingBox.");
        }
        else {
            try {
                /*
                 * SHOW the GetCoverage-request, DO the GetCoverage-operation and ADD the coverage to the map:
                 */

                Operation getCoverageOp = serviceDesc.getOperationsMetadata().getOperationByName("GetCoverage");

                ParameterContainer paramCon = getFilledParameterContainer(getCoverageOp);

                String request = new WCSRequestBuilder().buildGetCoverageRequest(getCoverageOp, paramCon);

                int returnVal = new ShowGetCoverageRequestDialog(view, request).showDialog();

                if (returnVal == ShowGetCoverageRequestDialog.APPROVE_OPTION) {
                    WCSAdapter adapter = new WCSAdapter();

                    String layerId = ((Dataset) view.getCoveragesCB().getSelectedItem()).getIdentifier();
                    String layerTitle = serviceDesc.getContents().getDataIdentification(layerId).getTitle();

                    RasterServiceLayer layer = new RasterServiceLayer(adapter,
                                                                      new WMSRenderer(),
                                                                      serviceDesc,
                                                                      paramCon,
                                                                      layerId,
                                                                      layerTitle,
                                                                      "GetCoverage");
                    LayerAdder.addLayer(map, tree, layer);
                }
            }
            catch (OXFException exc) {
                exc.printStackTrace();
            }
            catch (OXFEventException exc) {
                exc.printStackTrace();
            }
        }
        view.dispose();
    }

    /**
     * 
     * @param event
     */
    public void itemStateChanged_layersCB(ItemEvent event) {
        initLayer((Dataset) view.getCoveragesCB().getSelectedItem());
    }

    /**
     * 
     * @param offering
     */
    public void initLayer(Dataset coverage) {

        // clear input elements:
        view.getFormatCB().removeAllItems();

        view.removeBBoxPanel();

        
        // init formatCB:
        String[] formats = coverage.getOutputFormats();
        for (int i = 0; i < formats.length; i++) {
            view.getFormatCB().addItem(formats[i]);
        }

        // init bboxPanel:
        if (coverage.getBoundingBoxes() != null) {
            IBoundingBox[] bboxes = coverage.getBoundingBoxes();
            
            view.addBBoxPanel(new BBoxSelectionPanel(coverage.getAvailableCRSs(), bboxes, map));
        }
    }

    /**
     * 
     * @param getObsOp
     * @return
     * @throws OXFException
     */
    public ParameterContainer getFilledParameterContainer(Operation getObsOp) throws OXFException {

        ParameterContainer paramCon = new ParameterContainer();

        //
        // required parameters:
        //

        // bbox:
        IBoundingBox chosenBBox = view.getBBoxPanel().getChosenBBox();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("BBOX"), chosenBBox));

        // srs:
        String srs = chosenBBox.getCRS();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("CRS"), srs));

        // coverages:
        String dataID = ((Dataset) view.getCoveragesCB().getSelectedItem()).getIdentifier();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("COVERAGE"), dataID));

        // format:
        String resultFormat = (String) view.getFormatCB().getSelectedItem();
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("FORMAT"), resultFormat));

        // height:
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("HEIGHT"),
                                                      map.getHeight()));

        // width:
        paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter("WIDTH"),
                                                      map.getWidth()));

        //
        // optional parameters:
        //

        // ---

        return paramCon;

    }

}