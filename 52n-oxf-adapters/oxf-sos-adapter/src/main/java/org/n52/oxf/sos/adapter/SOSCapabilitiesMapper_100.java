/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.sos.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.xml.namespace.QName;

import net.opengis.gml.DirectPositionType;
import net.opengis.gml.EnvelopeType;
import net.opengis.gml.ReferenceType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodType;
import net.opengis.ogc.ComparisonOperatorType;
import net.opengis.ows.x11.AllowedValuesDocument.AllowedValues;
import net.opengis.ows.x11.DCPDocument;
import net.opengis.ows.x11.DomainType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.ows.x11.OperationDocument;
import net.opengis.ows.x11.RequestMethodType;
import net.opengis.ows.x11.ValueType;
import net.opengis.sos.x10.CapabilitiesDocument;
import net.opengis.sos.x10.ContentsDocument;
import net.opengis.sos.x10.FilterCapabilitiesDocument.FilterCapabilities;
import net.opengis.sos.x10.ObservationOfferingType;
import net.opengis.sos.x10.ResponseModeType;
import net.opengis.swe.x101.CompositePhenomenonType;
import net.opengis.swe.x101.PhenomenonPropertyType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.DatasetParameter;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.IDiscreteValueDomain;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ows.capabilities.PostRequestMethod;
import org.n52.oxf.ows.capabilities.RequestMethod;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.filter.ComparisonFilter;
import org.n52.oxf.valueDomains.filter.FilterValueDomain;
import org.n52.oxf.valueDomains.filter.IFilter;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSCapabilitiesMapper_100 {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSCapabilitiesMapper_100.class);

    public ServiceDescriptor mapCapabilities(CapabilitiesDocument capabilitiesDoc) throws OXFException {

        String version = mapVersion(capabilitiesDoc);
        ServiceIdentification serviceIdentification = mapServiceIdentification(capabilitiesDoc.getCapabilities().getServiceIdentification());
        ServiceProvider serviceProvider = mapServiceProvider(capabilitiesDoc);
        OperationsMetadata operationsMetadata = mapOperationsMetadata(capabilitiesDoc.getCapabilities().getOperationsMetadata());
        SOSContents contents = mapContents(capabilitiesDoc);

        addDatasetParameterFromContentsSection(operationsMetadata, contents);

        ServiceDescriptor serviceDesc = new ServiceDescriptor(version,
                                                              serviceIdentification,
                                                              serviceProvider,
                                                              operationsMetadata,
                                                              contents);
        return serviceDesc;
    }

    /**
     * takes selected informations from the SOSContents (e.g. the contents-section of the capabilities
     * document) and builds DatasetParameters from it. These DatasetParameters will be added to the
     * OperationsMetadata object.
     * 
     * @param operationsMetadata
     * @param contents
     */
    private void addDatasetParameterFromContentsSection(OperationsMetadata operationsMetadata, SOSContents contents) {
        Operation getObservationOp = operationsMetadata.getOperationByName("GetObservation");

        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            ObservationOffering obsOff = contents.getDataIdentification(i);

            //
            // --- id:
            //
            StringValueDomain idDomain = new StringValueDomain(obsOff.getIdentifier());
            DatasetParameter idParam = new DatasetParameter("id",
                                                            false,
                                                            idDomain,
                                                            obsOff,
                                                            Parameter.COMMON_NAME_RESOURCE_ID);
            getObservationOp.addParameter(idParam);

            //
            // --- bbox:
            //
            IBoundingBox[] bboxDomain = obsOff.getBoundingBoxes();
            for (IBoundingBox bbox : bboxDomain) {
                DatasetParameter bboxParam = new DatasetParameter("bbox",
                                                                  false,
                                                                  bbox,
                                                                  obsOff,
                                                                  Parameter.COMMON_NAME_BBOX);
                getObservationOp.addParameter(bboxParam);
            }

            //
            // --- srs:
            //
            if (obsOff.getAvailableCRSs() != null) {
                StringValueDomain srsDomain = new StringValueDomain(obsOff.getAvailableCRSs());
                DatasetParameter srsParam = new DatasetParameter("srs",
                                                                 false,
                                                                 srsDomain,
                                                                 obsOff,
                                                                 Parameter.COMMON_NAME_SRS);
                getObservationOp.addParameter(srsParam);
            }

            //
            // --- time:
            //
            IDiscreteValueDomain<ITime> temporalDomain = obsOff.getTemporalDomain();
            DatasetParameter eventTimeParam = new DatasetParameter("eventTime",
                                                                   false,
                                                                   temporalDomain,
                                                                   obsOff,
                                                                   Parameter.COMMON_NAME_TIME);
            getObservationOp.addParameter(eventTimeParam);
        }
    }

    /**
     * this method goes through the supported operations declared in the OperationsMetadata-section of the
     * SOS-Capabilities and maps the provided informations to the OXF internal capabilities-model (e.g. the
     * class org.n52.oxf.ows.capabilities.OperationsMetadata)
     * 
     * @param xb_opMetadata
     * @return
     */
    private OperationsMetadata mapOperationsMetadata(net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata xb_opMetadata) {

        //
        // map the operations:
        //

        OperationDocument.Operation[] xb_operations = xb_opMetadata.getOperationArray();
        Operation[] oc_operations = new Operation[xb_operations.length];
        for (int i = 0; i < xb_operations.length; i++) {
            OperationDocument.Operation xb_operation = xb_operations[i];

            String oc_operationName = xb_operation.getName();

            //
            // map the operations DCPs:
            //

            DCPDocument.DCP[] xb_dcps = xb_operation.getDCPArray();
            DCP[] oc_dcps = new DCP[xb_dcps.length];
            for (int j = 0; j < xb_dcps.length; j++) {
                DCPDocument.DCP xb_dcp = xb_dcps[j];

                //
                // map the RequestMethods:
                //

                List<RequestMethod> oc_requestMethods = new ArrayList<RequestMethod>();

                RequestMethodType[] xb_getRequestMethods = xb_dcp.getHTTP().getGetArray();
                for (int k = 0; k < xb_getRequestMethods.length; k++) {
                    RequestMethodType xb_getRequestMethod = xb_getRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_getRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new GetRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                RequestMethodType[] xb_postRequestMethods = xb_dcp.getHTTP().getPostArray();
                for (int k = 0; k < xb_postRequestMethods.length; k++) {
                    RequestMethodType xb_postRequestMethod = xb_postRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_postRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new PostRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                oc_dcps[j] = new DCP(oc_requestMethods);
            }

            //
            // map the operations parameters:
            //

            DomainType[] xb_parameters = xb_operation.getParameterArray();

            List<Parameter> oc_parameters = new ArrayList<Parameter>();

            for (int j = 0; j < xb_parameters.length; j++) {

                DomainType xb_parameter = xb_parameters[j];

                String parameterName = xb_parameter.getName();

                if (parameterName.equalsIgnoreCase("eventTime")) {
                    // do nothing! because the eventTime Parameter will be added from Contents section
                }
                else {

                    //
                    // map the parameters' values to StringValueDomains
                    //

                    AllowedValues xb_allowedValues = xb_parameter.getAllowedValues();
                    
                    if (xb_allowedValues != null) {
                        ValueType[] xb_values = xb_allowedValues.getValueArray();
    
                        StringValueDomain oc_values = new StringValueDomain();
                        for (int k = 0; k < xb_values.length; k++) {
                            ValueType xb_value = xb_values[k];
    
                            oc_values.addPossibleValue(xb_value.getStringValue());
                        }
    
                        Parameter oc_parameter = new Parameter(parameterName, true, oc_values, null);
                        oc_parameters.add(oc_parameter);
                    }
                }
            }

            Parameter[] parametersArray = new Parameter[oc_parameters.size()];
            oc_parameters.toArray(parametersArray);

            oc_operations[i] = new Operation(oc_operationName, parametersArray, null, oc_dcps);
        }

        return new OperationsMetadata(oc_operations);
    }

    private SOSContents mapContents(CapabilitiesDocument capabilitiesDoc) throws OXFException {
        ContentsDocument.Contents xb_contents = capabilitiesDoc.getCapabilities().getContents();

        ContentsDocument.Contents.ObservationOfferingList xb_obsOfferingList = xb_contents.getObservationOfferingList();

        ObservationOfferingType[] xb_obsOfferings = xb_obsOfferingList.getObservationOfferingArray();
        ArrayList<ObservationOffering> oc_obsOffList = new ArrayList<ObservationOffering>();
        for (int i = 0; i < xb_obsOfferings.length; i++) {
            ObservationOfferingType xb_obsOffering = xb_obsOfferings[i];

//            if(xb_obsOffering instanceof net.opengis.gml.AbstractFeatureType);
//            if(xb_obsOffering instanceof net.opengis.gml.AbstractGMLType);
            
            // identifier
            String oc_identifier = xb_obsOffering.getId();

            // title (take the first name or if name does not exist take the id)
            String oc_title;
            net.opengis.gml.CodeType[] xb_names = xb_obsOffering.getNameArray();
            if (xb_names != null && xb_names.length > 0 && xb_names[0].getStringValue() != null) {
                oc_title = xb_names[0].getStringValue();
            }
            else {
                oc_title = xb_obsOffering.getId();
            }

            // availableCRSs:
            EnvelopeType envelope = xb_obsOffering.getBoundedBy().getEnvelope();
            String oc_crs = envelope.getSrsName();

            String[] oc_availabaleCRSs = null;
            if (oc_crs != null) {
                oc_availabaleCRSs = new String[] {oc_crs};
            }
            else {
                // TODO Think about throwing an exception because of one missing attribute because this situation can occur often when a new offering is set up and no data is available. Set it null and then it's okay! (ehjuerrens@52north.org)
                // throw new NullPointerException("SRS not specified in the Capabilities' 'gml:Envelope'-tag.");
            }

            // BoundingBox:
            DirectPositionType lowerCorner = envelope.getLowerCorner();
            List xb_lowerCornerList = lowerCorner.getListValue();
            double[] oc_lowerCornerList = new double[xb_lowerCornerList.size()];

            DirectPositionType upperCorner = envelope.getUpperCorner();
            List xb_upperCornerList = upperCorner.getListValue();
            double[] oc_upperCornerList = new double[xb_upperCornerList.size()];

            if (oc_lowerCornerList.length == 0) {
                LOGGER.warn("Evelope contains invalid lower corner: {}.", envelope.xmlText());
                oc_lowerCornerList = new double[] {0.0, 0.0};
            } else {
                for (int j = 0; j < xb_lowerCornerList.size(); j++) {
                    oc_lowerCornerList[j] = (Double) xb_lowerCornerList.get(j);
                }
            }
            
            if (oc_upperCornerList.length == 0) {
                LOGGER.warn("Evelope contains invalid upper corner: {}.", envelope.xmlText());
                oc_upperCornerList = new double[] {0.0, 0.0};
            } else {
                for (int j = 0; j < xb_upperCornerList.size(); j++) {
                    oc_upperCornerList[j] = (Double) xb_upperCornerList.get(j);
                }
            }
            

            IBoundingBox[] oc_bbox = new IBoundingBox[1];
            oc_bbox[0] = new BoundingBox(oc_crs, oc_lowerCornerList, oc_upperCornerList);

            // outputFormats:
            String[] oc_outputFormats = xb_obsOffering.getResponseFormatArray();

            // TemporalDomain:
            List<ITime> oc_timeList = new ArrayList<ITime>();
            if (xb_obsOffering.getTime() != null && xb_obsOffering.getTime().getTimeGeometricPrimitive() != null) {
              
                XmlObject xo = xb_obsOffering.getTime().getTimeGeometricPrimitive().newCursor().getObject();
                SchemaType schemaType = xo.schemaType();
                if (schemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                    TimeInstantType xb_timeInstant = (TimeInstantType) xo;
                    String xb_timePos = xb_timeInstant.getTimePosition().getStringValue();

                    if ( !xb_timePos.equals("")) {
                        oc_timeList.add(TimeFactory.createTime(xb_timePos));
                    }
                }
                else if (schemaType.getJavaClass().isAssignableFrom(TimePeriodType.class)) {
                    TimePeriodType xb_timePeriod = (TimePeriodType) xo;

                    String beginPos = xb_timePeriod.getBeginPosition().getStringValue();
                    String endPos = xb_timePeriod.getEndPosition().getStringValue();

                    if ( !beginPos.equals("") && !endPos.equals("")) {
                        TimePeriod oc_timePeriod = new TimePeriod(beginPos, endPos);
                        oc_timeList.add(oc_timePeriod);
                    }
                }
            }
            IDiscreteValueDomain<ITime> oc_temporalDomain = new TemporalValueDomain(oc_timeList);

            // resultModels:
            List<String> resultModelList = new ArrayList<String>();

            Object[] resultModelArray = xb_obsOffering.getResultModelArray();
            for (Object resultModelObject : resultModelArray) {
                QName resultModelQName = (QName) resultModelObject;
                resultModelList.add(resultModelQName.getLocalPart());
            }
            String[] oc_resultModels = new String[resultModelList.size()];
            resultModelList.toArray(oc_resultModels);

            // result:
            FilterValueDomain filterDomain = new FilterValueDomain();
            FilterCapabilities filterCaps = capabilitiesDoc.getCapabilities().getFilterCapabilities();
            if (filterCaps != null){
                if (filterCaps.getScalarCapabilities().getComparisonOperators()!= null){
                	ComparisonOperatorType.Enum[] xb_compOpsArray = filterCaps.getScalarCapabilities().getComparisonOperators().getComparisonOperatorArray();
                	for (ComparisonOperatorType.Enum compOp : xb_compOpsArray) {
                		if (compOp.equals(ComparisonOperatorType.EQUAL_TO)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.GREATER_THAN)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_GREATER_THAN);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.GREATER_THAN_EQUAL_TO)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LESS_THAN)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LESS_THAN);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LESS_THAN_EQUAL_TO)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LESS_THAN_OR_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LIKE)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LIKE);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.NOT_EQUAL_TO)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_NOT_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.NULL_CHECK)) {
                			IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_NULL);
                			filterDomain.addPossibleValue(filter);
                		}
                	}
                }
            }

            // observedProperty:
            PhenomenonPropertyType[] xb_observedProperties = xb_obsOffering.getObservedPropertyArray();
            List<String> obsPropsList = new ArrayList<String>();

            for (int j = 0; j < xb_observedProperties.length; j++) {
                if (xb_observedProperties[j].getHref() != null) {
                    obsPropsList.add(xb_observedProperties[j].getHref());
                }
                else if (xb_observedProperties[j].getPhenomenon() != null
                        && xb_observedProperties[j].getPhenomenon() instanceof CompositePhenomenonType) {
                    CompositePhenomenonType compositePhenomenon = (CompositePhenomenonType) xb_observedProperties[j].getPhenomenon();

                    PhenomenonPropertyType[] phenomenonPropArray = compositePhenomenon.getComponentArray();
                    for (PhenomenonPropertyType phenomenonProp : phenomenonPropArray) {
                        obsPropsList.add(phenomenonProp.getHref());
                    }
                }
            }
            String[] oc_obsProps = new String[obsPropsList.size()];
            oc_obsProps = obsPropsList.toArray(oc_obsProps);

            // procedures:
            ReferenceType[] xb_procedures = xb_obsOffering.getProcedureArray();
            String[] oc_procedures = new String[xb_procedures.length];
            for (int j = 0; j < xb_procedures.length; j++) {
                oc_procedures[j] = xb_procedures[j].getHref();
            }

            // featuresOfInterest:
            ReferenceType[] xb_foi = xb_obsOffering.getFeatureOfInterestArray();
            String[] oc_foiIDs = new String[xb_foi.length];
            for (int j = 0; j < xb_foi.length; j++) {
                oc_foiIDs[j] = xb_foi[j].getHref();
            }

            // responseModes:
            ResponseModeType.Enum[] xb_responseModes = xb_obsOffering.getResponseModeArray();
            String[] oc_respModes = new String[xb_responseModes.length];
            for (int j = 0; j < xb_responseModes.length; j++) {
                oc_respModes[j] = xb_responseModes[j].toString();
            }

            // TODO: these variables should also be initialized
            String oc_fees = null;
            String oc_pointOfContact = null;
            Locale[] oc_language = null;
            String[] oc_keywords = null;
            String oc_abstractDescription = null;

            ObservationOffering oc_obsOff = new ObservationOffering(oc_title,
                                                                    oc_identifier,
                                                                    oc_bbox,
                                                                    oc_outputFormats,
                                                                    oc_availabaleCRSs,
                                                                    oc_fees,
                                                                    oc_language,
                                                                    oc_pointOfContact,
                                                                    oc_temporalDomain,
                                                                    oc_abstractDescription,
                                                                    oc_keywords,
                                                                    oc_procedures,
                                                                    oc_foiIDs,
                                                                    oc_obsProps,
                                                                    oc_resultModels,
                                                                    oc_respModes,
                                                                    filterDomain);

            oc_obsOffList.add(oc_obsOff);
        }

        return new SOSContents(oc_obsOffList);
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private ServiceIdentification mapServiceIdentification(net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification xb_serviceId) {

        String oc_title = xb_serviceId.getTitleArray(0).getStringValue();
        String oc_serviceType = xb_serviceId.getServiceType().getStringValue();
        String[] oc_serviceTypeVersions = xb_serviceId.getServiceTypeVersionArray();

        String oc_fees = xb_serviceId.getFees();
        String[] oc_accessConstraints = xb_serviceId.getAccessConstraintsArray();
        String oc_abstract = xb_serviceId.getAbstractArray(0).getStringValue();
        String[] oc_keywords = null;

        Vector<String> oc_keywordsVec = new Vector<String>();
        for (int i = 0; i < xb_serviceId.getKeywordsArray().length; i++) {
            LanguageStringType[] xb_keywords = xb_serviceId.getKeywordsArray(i).getKeywordArray();
            for (LanguageStringType xb_keyword : xb_keywords) {
                oc_keywordsVec.add(xb_keyword.getStringValue());
            }
        }
        oc_keywords = new String[oc_keywordsVec.size()];
        oc_keywordsVec.toArray(oc_keywords);

        return new ServiceIdentification(oc_title,
                                         oc_serviceType,
                                         oc_serviceTypeVersions,
                                         oc_fees,
                                         oc_accessConstraints,
                                         oc_abstract,
                                         oc_keywords);
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private String mapVersion(CapabilitiesDocument capsDoc) {
        return capsDoc.getCapabilities().getVersion();
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private static ServiceProvider mapServiceProvider(CapabilitiesDocument capsDoc) {
        // TODO has to be implemented
        return null;
    }
}