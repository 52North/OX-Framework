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

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.adapter.sos.ISOSRequestBuilder;
import org.n52.oxf.adapter.sos.SOSAdapter;
import org.n52.oxf.adapter.sos.caps.ObservationOffering;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.DatasetParameter;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ui.swing.TimePanel;
import org.n52.oxf.ui.swing.TimePeriodPanel;
import org.n52.oxf.ui.swing.TimePositionPanel;
import org.n52.oxf.util.SosUtil;
import org.n52.oxf.valueDomains.filter.ComparisonFilter;
import org.n52.oxf.valueDomains.filter.FilterValueDomain;
import org.n52.oxf.valueDomains.filter.IFilter;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.n52.oxf.valueDomains.time.TimePosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GetObs_ConfiguratorController {

    private GetObs_Configurator view;

    private ServiceDescriptor serviceDesc;
    private SOSAdapter adapter;
    
    private Set<OXFFeature> preSelectedFeatureSet;

    private int minNumOfProps;
    private int maxNumOfProps;


    public GetObs_ConfiguratorController(GetObs_Configurator view,
                                         SOSAdapter sosAdapter,
                                         ServiceDescriptor serviceDescriptor,
                                         Set<OXFFeature> preSelectedFeaures,
                                         int minNumOfProps,
                                         int maxNumOfProps) {
        this.view = view;

        this.serviceDesc = serviceDescriptor;
        this.adapter = sosAdapter;
        this.preSelectedFeatureSet = preSelectedFeaures;

        this.minNumOfProps = minNumOfProps;
        this.maxNumOfProps = maxNumOfProps;
    }

    /**
     * 
     * 
     */
    protected void postInit() {

        // init offeringCB:
        for (int i = 0; i < serviceDesc.getContents().getDataIdentificationCount(); i++) {
            view.getOfferingCB().addItem(serviceDesc.getContents().getDataIdentification(i));

            // the addItem()-method invokes an ItemStateChanged-event. So the
            // itemStateChanged_offeringCB()-method will be executed.
        }
    }

    /**
     * 
     * @param event
     */
    public void itemStateChanged_offeringCB(ItemEvent event) {
        initOffering((ObservationOffering) view.getOfferingCB().getSelectedItem());
    }

    /**
     * 
     * @param offering
     */
    protected void initOffering(org.n52.oxf.adapter.sos.caps.ObservationOffering offering) {

        //
        // clear input elements:
        //

        view.removeObservedPropertyPanel();

        view.getResponseFormatCB().removeAllItems();

        ((DefaultListModel) view.getProcedureList().getModel()).clear();

        view.getResultModelCB().removeAllItems();

        view.getResponseModeCB().removeAllItems();

        view.removeTimePanel();

        ((DefaultListModel) view.getFoiList().getModel()).clear();

        view.getFilterTextArea().setText("");

        view.getFilterComboBox().removeAllItems();

        //
        // init input elements:
        //

        // init observedPropertyList:
        String[] observedProperties = offering.getObservedProperties();
        addObservedPropertyPanel(observedProperties);

        // init resultFormatCB:
        String[] resultFormats = offering.getOutputFormats();
        for (int i = 0; i < resultFormats.length; i++) {
            view.getResponseFormatCB().addItem(resultFormats[i]);
        }

        //
        // optional:
        //

        // init procedureList:
        if (offering.getProcedures() != null) {
            String[] procedureList = offering.getProcedures();
            for (int i = 0; i < procedureList.length; i++) {
                ((DefaultListModel) view.getProcedureList().getModel()).addElement(procedureList[i]);
            }
        }

        // init resultModelCB:
        if (offering.getResultModels() != null) {
            String[] resultModels = offering.getResultModels();
            for (int i = 0; i < resultModels.length; i++) {
                view.getResultModelCB().addItem(resultModels[i]);
            }
        }

        // init responseModeCB:
        if (offering.getResponseModes() != null) {
            String[] responseModes = offering.getResponseModes();
            for (int i = 0; i < responseModes.length; i++) {
                view.getResponseModeCB().addItem(responseModes[i]);
            }
        }

        // init timePanel:
        if (offering.getTemporalDomain() != null) {
            TemporalValueDomain temporalDomain = (TemporalValueDomain) offering.getTemporalDomain();

            addTimePanel(temporalDomain);
        }

        // init foiList:
        if (offering.getFeatureOfInterest() != null) {
            JList foiList = view.getFoiList();
            DefaultListModel listModel = (DefaultListModel) foiList.getModel();

            String[] fois = offering.getFeatureOfInterest();

            for (String featureReference : fois) {
                listModel.addElement(featureReference);

                // selected Features of the source-layer shall be prechosen in the list:
                if (preSelectedFeatureSet != null) {
                    for (OXFFeature selectedFeature : preSelectedFeatureSet) {
                        if (selectedFeature.getID().equals(featureReference)) {
                            foiList.getSelectionModel().addSelectionInterval(listModel.getSize() - 1,
                                                                             listModel.getSize() - 1);
                        }
                    }
                }
            }
            
            // if none FOI is selected -> select the first one: 
            if (foiList.getSelectionModel().getMaxSelectionIndex() == -1) {
                if (listModel.size() > 0) {
                    foiList.getSelectionModel().addSelectionInterval(0, 0);
                }
            }
        }

        // init result-filterComboBox
        view.getFilterComboBox().addItem("");
        if (offering.getResult() != null) {
            FilterValueDomain filterDomain = offering.getResult();
            List<IFilter> filterList = filterDomain.getPossibleValues();
            for (IFilter filter : filterList) {
                
                if (filter instanceof ComparisonFilter) {
                    ComparisonFilter cFilter = (ComparisonFilter) filter;
                    
                    String[] chosenObsProp = view.getObservedPropertyPanel().getChosenProperties();
                    if (chosenObsProp.length > 0) {
                        cFilter.setPropertyName(chosenObsProp[0]);
                    }
                }
                
                view.getFilterComboBox().addItem(filter);
            }
        }
    }

    /**
     * helper-method for initOffering().
     */
    private void addTimePanel(TemporalValueDomain temporalDomain) {
        TimePanel timePanel = null; // to be added
        List<ITime> timeList = temporalDomain.getPossibleValues();

        if (timeList.size() > 0) {
            ITime time = timeList.get(0);

            if (time instanceof TimePeriod) {
                timePanel = new TimePeriodPanel((TimePeriod) time);
            }
            else if (time instanceof TimePosition) {
                timePanel = new TimePositionPanel((TimePosition) time);
            }
            view.addTimePanel(timePanel);
        }
    }

    /**
     * helper-method for initOffering().
     */
    private void addObservedPropertyPanel(String[] observedProperties) {

        ObservedPropertyPanel opPanel;

        if (minNumOfProps == maxNumOfProps && minNumOfProps == 1) {
            opPanel = new ObservedPropertyPanel4OneProp(observedProperties);
        }
        else if (minNumOfProps == maxNumOfProps && minNumOfProps == 2) {
            opPanel = new ObservedPropertyPanel4TwoProps(observedProperties);
        }
        else {
            opPanel = new ObservedPropertyPanel4XProps(observedProperties);
        }

        view.addObservedPropertyPanel(opPanel);
    }

    /**
     * 
     * @param event
     */
    public void actionPerformed_showRequestButton(ActionEvent event, boolean onlyViewGetObsDoc) {
        try {
            Operation getObsOp = serviceDesc.getOperationsMetadata().getOperationByName("GetObservation");

            ParameterContainer filledParamCon = getFilledParameterContainer(getObsOp);

            Object[] obsPropsArray = filledParamCon.getParameterShellWithServiceSidedName("observedProperty").getSpecifiedValueArray();

            if (obsPropsArray.length < minNumOfProps || obsPropsArray.length > maxNumOfProps) {
                JOptionPane.showMessageDialog(null,
                                              "Please select at least " + minNumOfProps
                                                      + " and at most " + maxNumOfProps
                                                      + " observed properties.",
                                              "Incorrect number of observed properties.",
                                              JOptionPane.ERROR_MESSAGE);
            }
            else {
                String postRequest = adapter.getRequestBuilder().buildGetObservationRequest(filledParamCon);

                ShowGetObservationRequestDialog showGetObsReqDialog = new ShowGetObservationRequestDialog(view,
                                                                                                          "GetObservation Request",
                                                                                                          postRequest,
                                                                                                          adapter,
                                                                                                          getObsOp,
                                                                                                          filledParamCon);
                
                if (onlyViewGetObsDoc) {
                    showGetObsReqDialog.getVisualizeObsButton().setVisible(false);
                }
                
                int returnVal = showGetObsReqDialog.showDialog();

                view.setReturnVal(returnVal);

                view.setOperationResult(showGetObsReqDialog.getOperationResult());
            }
        }
        catch (OXFException e) {
            e.printStackTrace();
        }

        view.dispose();
    }

    /**
     * helper-method for actionPerformed_showRequestButton.
     * 
     * @param getObsOp
     * @return
     * @throws OXFException
     */
    protected ParameterContainer getFilledParameterContainer(Operation getObsOp) throws OXFException {

        ParameterContainer paramCon = new ParameterContainer();

        //
        // required parameters:
        //

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_SERVICE_PARAMETER, SosUtil.SERVICE_TYPE);

        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_VERSION_PARAMETER, adapter.getServiceVersion());

        String offeringIdentifier = ((ObservationOffering) view.getOfferingCB().getSelectedItem()).getIdentifier();
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OFFERING_PARAMETER, offeringIdentifier);

        String responseFormat = (String) view.getResponseFormatCB().getSelectedItem();
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER, responseFormat);

        String[] observedProperties = view.getObservedPropertyPanel().getChosenProperties();
        paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER, observedProperties);

        //
        // optional parameters:
        //

        if (view.getProcedureList().getSelectedValue() != null) {
            Object[] procedureObjects = view.getProcedureList().getSelectedValues();
            String[] procedures = new String[procedureObjects.length];
            for (int i = 0; i < procedureObjects.length; i++) {
                procedures[i] = procedureObjects[i].toString();
            }
            paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_PROCEDURE_PARAMETER, procedures);
        }

        if (view.getResultModelCB().getSelectedItem() != null) {
            String resultModel = (String) view.getResultModelCB().getSelectedItem();
            paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_MODEL_PARAMETER, resultModel);
        }

        if (view.getResponseModeCB().getSelectedItem() != null) {
            String responseMode = (String) view.getResponseModeCB().getSelectedItem();
            paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESPONSE_MODE_PARAMETER, responseMode);
        }

        if (view.getTimePanel() != null && view.getTimePanel().getChosenTime() != null) {
            ITime chosenTime = view.getTimePanel().getChosenTime();
            paramCon.addParameterShell(new ParameterShell(getObsOp.getParameter(ISOSRequestBuilder.GET_OBSERVATION_EVENT_TIME_PARAMETER),
                                                          chosenTime));
        }

        if (view.getFoiList().getSelectedValue() != null) {
            Object[] foiObjects = view.getFoiList().getSelectedValues();
            String[] fois = new String[foiObjects.length];
            for (int i = 0; i < foiObjects.length; i++) {
                fois[i] = foiObjects[i].toString();
            }
            paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER, fois);
        }

        if (view.getFilterTextArea().getText() != null
                && !view.getFilterTextArea().getText().equals("")) {
            String filterDefinition = view.getFilterTextArea().getText();
            paramCon.addParameterShell(ISOSRequestBuilder.GET_OBSERVATION_RESULT_PARAMETER, filterDefinition);
        }

        //
        // the BBOX-parameter has to be added because it is needed by the oxf:
        //

        for (Parameter param : getObsOp.getParameters()) {
            if (param instanceof DatasetParameter) {
                DatasetParameter dataParam = (DatasetParameter) param;

                String datasetId = dataParam.getAssociatedDataset().getIdentifier();

                // BBOX:
                if (dataParam.getCommonName().equals(Parameter.COMMON_NAME_BBOX)
                        && datasetId.equals(offeringIdentifier)) {

                    // use the BoundingBox specified as valueDomain also as the specifiedValue in the
                    // ParameterShell:
                    IBoundingBox bbox = (IBoundingBox) dataParam.getValueDomain();

                    paramCon.addParameterShell(new ParameterShell(param, bbox));
                }
            }
        }

        return paramCon;
    }

    public void itemStateChanged_filterComboBox() {
        if (view.getFilterComboBox().getSelectedItem() != null) {
            
            if (view.getFilterComboBox().getSelectedItem().equals("")) {
                view.getFilterTextArea().setText("");
            } else {
                IFilter selectedFilter = (IFilter) view.getFilterComboBox().getSelectedItem();
                view.getFilterTextArea().setText(selectedFilter.toXML());
            }
        }
    }
}