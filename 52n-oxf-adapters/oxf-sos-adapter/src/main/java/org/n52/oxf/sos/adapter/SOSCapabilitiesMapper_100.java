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

    public ServiceDescriptor mapCapabilities(final CapabilitiesDocument capabilitiesDoc) throws OXFException {

        final String version = mapVersion(capabilitiesDoc);
        final ServiceIdentification serviceIdentification = mapServiceIdentification(capabilitiesDoc.getCapabilities().getServiceIdentification());
        final ServiceProvider serviceProvider = mapServiceProvider(capabilitiesDoc);
        final OperationsMetadata operationsMetadata = mapOperationsMetadata(capabilitiesDoc.getCapabilities().getOperationsMetadata());
        final SOSContents contents = mapContents(capabilitiesDoc);

        addDatasetParameterFromContentsSection(operationsMetadata, contents);

        final ServiceDescriptor serviceDesc = new ServiceDescriptor(version,
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
    private void addDatasetParameterFromContentsSection(final OperationsMetadata operationsMetadata, final SOSContents contents) {
        final Operation getObservationOp = operationsMetadata.getOperationByName("GetObservation");

        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            final ObservationOffering obsOff = contents.getDataIdentification(i);

            //
            // --- id:
            //
            final StringValueDomain idDomain = new StringValueDomain(obsOff.getIdentifier());
            final DatasetParameter idParam = new DatasetParameter("id",
                                                            false,
                                                            idDomain,
                                                            obsOff,
                                                            Parameter.COMMON_NAME_RESOURCE_ID);
            getObservationOp.addParameter(idParam);

            //
            // --- bbox:
            //
            final IBoundingBox[] bboxDomain = obsOff.getBoundingBoxes();
            for (final IBoundingBox bbox : bboxDomain) {
                final DatasetParameter bboxParam = new DatasetParameter("bbox",
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
                final StringValueDomain srsDomain = new StringValueDomain(obsOff.getAvailableCRSs());
                final DatasetParameter srsParam = new DatasetParameter("srs",
                                                                 false,
                                                                 srsDomain,
                                                                 obsOff,
                                                                 Parameter.COMMON_NAME_SRS);
                getObservationOp.addParameter(srsParam);
            }

            //
            // --- time:
            //
            final IDiscreteValueDomain<ITime> temporalDomain = obsOff.getTemporalDomain();
            final DatasetParameter eventTimeParam = new DatasetParameter("eventTime",
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
    private OperationsMetadata mapOperationsMetadata(final net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata xb_opMetadata) {

        //
        // map the operations:
        //

        final OperationDocument.Operation[] xb_operations = xb_opMetadata.getOperationArray();
        final Operation[] oc_operations = new Operation[xb_operations.length];
        for (int i = 0; i < xb_operations.length; i++) {
            final OperationDocument.Operation xb_operation = xb_operations[i];

            final String oc_operationName = xb_operation.getName();

            //
            // map the operations DCPs:
            //

            final DCPDocument.DCP[] xb_dcps = xb_operation.getDCPArray();
            final DCP[] oc_dcps = new DCP[xb_dcps.length];
            for (int j = 0; j < xb_dcps.length; j++) {
                final DCPDocument.DCP xb_dcp = xb_dcps[j];

                //
                // map the RequestMethods:
                //

                final List<RequestMethod> oc_requestMethods = new ArrayList<RequestMethod>();

                final RequestMethodType[] xb_getRequestMethods = xb_dcp.getHTTP().getGetArray();
                for (final RequestMethodType xb_getRequestMethod : xb_getRequestMethods) {
                    final OnlineResource oc_onlineRessource = new OnlineResource(xb_getRequestMethod.getHref());
                    final RequestMethod oc_requestMethod = new GetRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                final RequestMethodType[] xb_postRequestMethods = xb_dcp.getHTTP().getPostArray();
                for (final RequestMethodType xb_postRequestMethod : xb_postRequestMethods) {
                    final OnlineResource oc_onlineRessource = new OnlineResource(xb_postRequestMethod.getHref());
                    final RequestMethod oc_requestMethod = new PostRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                oc_dcps[j] = new DCP(oc_requestMethods);
            }

            //
            // map the operations parameters:
            //

            final DomainType[] xb_parameters = xb_operation.getParameterArray();

            final List<Parameter> oc_parameters = new ArrayList<Parameter>();

            for (final DomainType xb_parameter : xb_parameters) {

                final String parameterName = xb_parameter.getName();

                if (!parameterName.equalsIgnoreCase("eventTime")) {

                    //
                    // map the parameters' values to StringValueDomains
                    //

                    final AllowedValues xb_allowedValues = xb_parameter.getAllowedValues();
                    
                    if (xb_allowedValues != null) {
                        final ValueType[] xb_values = xb_allowedValues.getValueArray();
    
                        final StringValueDomain oc_values = new StringValueDomain();
                        for (final ValueType xb_value : xb_values) {
                            oc_values.addPossibleValue(xb_value.getStringValue());
                        }
    
                        final Parameter oc_parameter = new Parameter(parameterName, true, oc_values, null);
                        oc_parameters.add(oc_parameter);
                    }
                }
            }

            final Parameter[] parametersArray = new Parameter[oc_parameters.size()];
            oc_parameters.toArray(parametersArray);

            oc_operations[i] = new Operation(oc_operationName, parametersArray, null, oc_dcps);
        }

        return new OperationsMetadata(oc_operations);
    }

    private SOSContents mapContents(final CapabilitiesDocument capabilitiesDoc) throws OXFException {
        final ContentsDocument.Contents xb_contents = capabilitiesDoc.getCapabilities().getContents();

        final ContentsDocument.Contents.ObservationOfferingList xb_obsOfferingList = xb_contents.getObservationOfferingList();

        final ObservationOfferingType[] xb_obsOfferings = xb_obsOfferingList.getObservationOfferingArray();
        final ArrayList<ObservationOffering> oc_obsOffList = new ArrayList<ObservationOffering>();
        for (final ObservationOfferingType xb_obsOffering : xb_obsOfferings) {
            

//            if(xb_obsOffering instanceof net.opengis.gml.AbstractFeatureType);
//            if(xb_obsOffering instanceof net.opengis.gml.AbstractGMLType);
            
            // identifier
            final String oc_identifier = xb_obsOffering.getId();

            // title (take the first name or if name does not exist take the id)
            String oc_title;
            final net.opengis.gml.CodeType[] xb_names = xb_obsOffering.getNameArray();
            if (xb_names != null && xb_names.length > 0 && xb_names[0].getStringValue() != null) {
                oc_title = xb_names[0].getStringValue();
            }
            else {
                oc_title = xb_obsOffering.getId();
            }

            // availableCRSs:
            final EnvelopeType envelope = xb_obsOffering.getBoundedBy().getEnvelope();
            final String oc_crs = envelope.getSrsName();

            String[] oc_availabaleCRSs = null;
            if (oc_crs != null) {
                oc_availabaleCRSs = new String[] {oc_crs};
            }
            else {
                // TODO Think about throwing an exception because of one missing attribute because this situation can occur often when a new offering is set up and no data is available. Set it null and then it's okay! (ehjuerrens@52north.org)
                // throw new NullPointerException("SRS not specified in the Capabilities' 'gml:Envelope'-tag.");
            }

            // BoundingBox:
            final DirectPositionType lowerCorner = envelope.getLowerCorner();
            final List<?> xb_lowerCornerList = lowerCorner.getListValue();
            double[] oc_lowerCornerList = new double[xb_lowerCornerList.size()];

            final DirectPositionType upperCorner = envelope.getUpperCorner();
            final List<?> xb_upperCornerList = upperCorner.getListValue();
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
            

            final IBoundingBox[] oc_bbox = new IBoundingBox[1];
            oc_bbox[0] = new BoundingBox(oc_crs, oc_lowerCornerList, oc_upperCornerList);

            // outputFormats:
            final String[] oc_outputFormats = xb_obsOffering.getResponseFormatArray();

            // TemporalDomain:
            final List<ITime> oc_timeList = new ArrayList<ITime>();
            if (xb_obsOffering.getTime() != null && xb_obsOffering.getTime().getTimeGeometricPrimitive() != null) {
              
                final XmlObject xo = xb_obsOffering.getTime().getTimeGeometricPrimitive().newCursor().getObject();
                final SchemaType schemaType = xo.schemaType();
                if (schemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                    final TimeInstantType xb_timeInstant = (TimeInstantType) xo;
                    final String xb_timePos = xb_timeInstant.getTimePosition().getStringValue();

                    if ( !xb_timePos.equals("")) {
                        oc_timeList.add(TimeFactory.createTime(xb_timePos));
                    }
                }
                else if (schemaType.getJavaClass().isAssignableFrom(TimePeriodType.class)) {
                    final TimePeriodType xb_timePeriod = (TimePeriodType) xo;

                    final String beginPos = xb_timePeriod.getBeginPosition().getStringValue();
                    final String endPos = xb_timePeriod.getEndPosition().getStringValue();

                    if ( !beginPos.equals("") && !endPos.equals("")) {
                        final TimePeriod oc_timePeriod = new TimePeriod(beginPos, endPos);
                        oc_timeList.add(oc_timePeriod);
                    }
                }
            }
            final IDiscreteValueDomain<ITime> oc_temporalDomain = new TemporalValueDomain(oc_timeList);

            // resultModels:
            final List<String> resultModelList = new ArrayList<String>();

            final Object[] resultModelArray = xb_obsOffering.getResultModelArray();
            for (final Object resultModelObject : resultModelArray) {
                final QName resultModelQName = (QName) resultModelObject;
                resultModelList.add(resultModelQName.getLocalPart());
            }
            final String[] oc_resultModels = new String[resultModelList.size()];
            resultModelList.toArray(oc_resultModels);

            // result:
            final FilterValueDomain filterDomain = new FilterValueDomain();
            final FilterCapabilities filterCaps = capabilitiesDoc.getCapabilities().getFilterCapabilities();
            if (filterCaps != null){
                if (filterCaps.getScalarCapabilities().getComparisonOperators()!= null){
                	final ComparisonOperatorType.Enum[] xb_compOpsArray = filterCaps.getScalarCapabilities().getComparisonOperators().getComparisonOperatorArray();
                	for (final ComparisonOperatorType.Enum compOp : xb_compOpsArray) {
                		if (compOp.equals(ComparisonOperatorType.EQUAL_TO)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.GREATER_THAN)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_GREATER_THAN);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.GREATER_THAN_EQUAL_TO)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LESS_THAN)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LESS_THAN);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LESS_THAN_EQUAL_TO)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LESS_THAN_OR_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.LIKE)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LIKE);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.NOT_EQUAL_TO)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_NOT_EQUAL_TO);
                			filterDomain.addPossibleValue(filter);
                		}
                		else if (compOp.equals(ComparisonOperatorType.NULL_CHECK)) {
                			final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_NULL);
                			filterDomain.addPossibleValue(filter);
                		}
                	}
                }
            }

            // observedProperty:
            final PhenomenonPropertyType[] xb_observedProperties = xb_obsOffering.getObservedPropertyArray();
            final List<String> obsPropsList = new ArrayList<String>();

            for (final PhenomenonPropertyType xb_observedPropertie : xb_observedProperties) {
                if (xb_observedPropertie.getHref() != null) {
                    obsPropsList.add(xb_observedPropertie.getHref());
                }
                else if (xb_observedPropertie.getPhenomenon() != null
                        && xb_observedPropertie.getPhenomenon() instanceof CompositePhenomenonType) {
                    final CompositePhenomenonType compositePhenomenon = (CompositePhenomenonType) xb_observedPropertie.getPhenomenon();

                    final PhenomenonPropertyType[] phenomenonPropArray = compositePhenomenon.getComponentArray();
                    for (final PhenomenonPropertyType phenomenonProp : phenomenonPropArray) {
                        obsPropsList.add(phenomenonProp.getHref());
                    }
                }
            }
            String[] oc_obsProps = new String[obsPropsList.size()];
            oc_obsProps = obsPropsList.toArray(oc_obsProps);

            // procedures:
            final ReferenceType[] xb_procedures = xb_obsOffering.getProcedureArray();
            final String[] oc_procedures = new String[xb_procedures.length];
            for (int j = 0; j < xb_procedures.length; j++) {
                oc_procedures[j] = xb_procedures[j].getHref();
            }

            // featuresOfInterest:
            final ReferenceType[] xb_foi = xb_obsOffering.getFeatureOfInterestArray();
            final String[] oc_foiIDs = new String[xb_foi.length];
            for (int j = 0; j < xb_foi.length; j++) {
                oc_foiIDs[j] = xb_foi[j].getHref();
            }

            // responseModes:
            final ResponseModeType.Enum[] xb_responseModes = xb_obsOffering.getResponseModeArray();
            final String[] oc_respModes = new String[xb_responseModes.length];
            for (int j = 0; j < xb_responseModes.length; j++) {
                oc_respModes[j] = xb_responseModes[j].toString();
            }

            // TODO: these variables should also be initialized
            final String oc_fees = null;
            final String oc_pointOfContact = null;
            final Locale[] oc_language = null;
            final String[] oc_keywords = null;
            final String oc_abstractDescription = null;

            final ObservationOffering oc_obsOff = new ObservationOffering(oc_title,
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
    private ServiceIdentification mapServiceIdentification(final net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification xb_serviceId) {

        final String oc_title = xb_serviceId.getTitleArray(0).getStringValue();
        final String oc_serviceType = xb_serviceId.getServiceType().getStringValue();
        final String[] oc_serviceTypeVersions = xb_serviceId.getServiceTypeVersionArray();

        final String oc_fees = xb_serviceId.getFees();
        final String[] oc_accessConstraints = xb_serviceId.getAccessConstraintsArray();
        final String oc_abstract = xb_serviceId.getAbstractArray(0).getStringValue();
        String[] oc_keywords = null;

        final Vector<String> oc_keywordsVec = new Vector<String>();
        for (int i = 0; i < xb_serviceId.getKeywordsArray().length; i++) {
            final LanguageStringType[] xb_keywords = xb_serviceId.getKeywordsArray(i).getKeywordArray();
            for (final LanguageStringType xb_keyword : xb_keywords) {
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
    private String mapVersion(final CapabilitiesDocument capsDoc) {
        return capsDoc.getCapabilities().getVersion();
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private static ServiceProvider mapServiceProvider(final CapabilitiesDocument capsDoc) {
        // TODO has to be implemented
        return null;
    }
}