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

import java.awt.Component;
import java.util.Set;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.adapter.sos.ISOSRequestBuilder;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.feature.FeatureStore;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.render.IChartRenderer;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.ui.swing.ChartDialog;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.util.LayerAdder;
import org.n52.oxf.util.OXFEventException;
import org.n52.oxf.valueDomains.StringValueDomain;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class SOSLayerAdder {

    public static void addSOSLayer(Component owner,
                                   MapCanvas map,
                                   ContentTree tree,
                                   String layerName,
                                   String layerTitle,
                                   SOSAdapter sosAdapter,
                                   ServiceDescriptor serviceDesc,
                                   IFeatureDataRenderer renderer,
                                   IFeatureStore featureStore,
                                   Set<OXFFeature> preSelectedFeatures,
                                   int minNumOfProps,
                                   int maxNumOfProps) {

        // enable the user to build up a GetObservation request for the selectedFeatures:
        GetObs_Configurator getObsDialog = new GetObs_Configurator(owner,
                                                                   sosAdapter,
                                                                   serviceDesc,
                                                                   preSelectedFeatures,
                                                                   minNumOfProps,
                                                                   maxNumOfProps);
        int returnVal = getObsDialog.showDialog();

        if (returnVal == ShowGetObservationRequestDialog.VISUALIZE_OPTION) {
            OperationResult opResult = getObsDialog.getOperationResult();
            ParameterContainer paramCon = opResult.getUsedParameters();

            try {
                OXFFeatureCollection observations = featureStore.unmarshalFeatures(opResult);

                // make layer name unique:
                while (map.getLayerContext().contains(layerName)) {
                    int layerNumber = Integer.parseInt(layerName.substring(layerName.length() - 2, layerName.length()));
                    layerNumber = layerNumber + 1;
                    if (layerNumber < 10) {
                        layerName = layerName.substring(0, layerName.length() - 2) + "0" + layerNumber;
                    }
                    else {
                        layerName = layerName.substring(0, layerName.length() - 2) + layerNumber;
                    }
                }

                FeatureServiceLayer layer = new FeatureServiceLayer(sosAdapter,
                                                                    renderer,
                                                                    featureStore,
                                                                    null,
                                                                    serviceDesc,
                                                                    paramCon,
                                                                    layerName,
                                                                    layerTitle,
                                                                    sosAdapter.getResourceOperationName(),
                                                                    true);

                String[] foiIDs = (String[]) paramCon.getParameterShellWithServiceSidedName(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER).getSpecifiedValueArray();

                // the chosen FOIs will be the selectedFeatures in the new Layer:
                layer.setSelectedFeatures(requestFeatureOfInterestInstances(sosAdapter, serviceDesc, foiIDs));

                LayerAdder.addLayer(map, tree, layer);
            }
            catch (OXFException exc) {
                exc.printStackTrace();
            }
            catch (OXFEventException exc) {
                exc.printStackTrace();
            }
            catch (ExceptionReport e) {
                e.printStackTrace();
            }
        }
    }

    public static Set<OXFFeature> requestFeatureOfInterestInstances(SOSAdapter sosAdapter,
                                                                    ServiceDescriptor serviceDesc,
                                                                    String[] foiIDs) throws OXFException,
            ExceptionReport {

        ParameterContainer paramCon = new ParameterContainer();

        paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_SERVICE_PARAMETER, "SOS");
        paramCon.addParameterShell(ISOSRequestBuilder.GET_FOI_VERSION_PARAMETER, sosAdapter.getServiceVersion());
        paramCon.addParameterShell(new ParameterShell(new Parameter(ISOSRequestBuilder.GET_FOI_ID_PARAMETER,
                                                                    false,
                                                                    new StringValueDomain(foiIDs),
                                                                    null), foiIDs));
        OperationResult opResult = sosAdapter.doOperation(serviceDesc.getOperationsMetadata().getOperationByName(SOSAdapter.GET_FEATURE_OF_INTEREST),
                                                          paramCon);

        IFeatureStore featureStore = new FeatureStore();
        OXFFeatureCollection featureCollection = featureStore.unmarshalFeatures(opResult);

        return featureCollection.toSet();
    }

    public static void createChart(Component owner,
                                   MapCanvas map,
                                   ContentTree tree,
                                   SOSAdapter sosAdapter,
                                   ServiceDescriptor serviceDesc,
                                   IChartRenderer renderer,
                                   IFeatureStore featureStore,
                                   Set<OXFFeature> preSelectedFeatures,
                                   int minNumOfProps,
                                   int maxNumOfProps) {
        // enable the user to build up a GetObservation request for the selectedFeatures:
        GetObs_Configurator getObsDialog = new GetObs_Configurator(owner,
                                                                   sosAdapter,
                                                                   serviceDesc,
                                                                   preSelectedFeatures,
                                                                   minNumOfProps,
                                                                   maxNumOfProps);
        int returnVal = getObsDialog.showDialog();

        if (returnVal == ShowGetObservationRequestDialog.VISUALIZE_OPTION) {
            OperationResult opResult = getObsDialog.getOperationResult();
            ParameterContainer paramCon = opResult.getUsedParameters();
            try {
                OXFFeatureCollection observations = featureStore.unmarshalFeatures(opResult);
                //new ReducedChartDialog(owner, paramCon, observations, sosAdapter, serviceDesc, featureStore, renderer).setVisible(true);
                new ChartDialog(owner, paramCon, observations, sosAdapter, serviceDesc, featureStore, renderer).setVisible(true);
            }
            catch (OXFException exc) {
                exc.printStackTrace();
            }
        }
    }
}