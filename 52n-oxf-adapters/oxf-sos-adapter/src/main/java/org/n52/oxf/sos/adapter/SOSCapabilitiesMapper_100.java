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
     * @param xbOpMetadata
     * @return
     */
    private OperationsMetadata mapOperationsMetadata(final net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata xbOpMetadata) {

        //
        // map the operations:
        //

        final OperationDocument.Operation[] xbOperations = xbOpMetadata.getOperationArray();
        final Operation[] ocOperations = new Operation[xbOperations.length];
        for (int i = 0; i < xbOperations.length; i++) {
            final OperationDocument.Operation xbOperation = xbOperations[i];

            final String ocOperationName = xbOperation.getName();

            //
            // map the operations DCPs:
            //

            final DCPDocument.DCP[] xbDcps = xbOperation.getDCPArray();
            final DCP[] ocDcps = new DCP[xbDcps.length];
            for (int j = 0; j < xbDcps.length; j++) {
                final DCPDocument.DCP xbDcp = xbDcps[j];

                //
                // map the RequestMethods:
                //

                final List<RequestMethod> ocRequestMethods = new ArrayList<RequestMethod>();

                final RequestMethodType[] xbGetRequestMethods = xbDcp.getHTTP().getGetArray();
                for (final RequestMethodType xbGetRequestMethod : xbGetRequestMethods) {
                    final OnlineResource ocOnlineRessource = new OnlineResource(xbGetRequestMethod.getHref());
                    final RequestMethod ocRequestMethod = new GetRequestMethod(ocOnlineRessource);
                    ocRequestMethods.add(ocRequestMethod);
                }

                final RequestMethodType[] xbPostRequestMethods = xbDcp.getHTTP().getPostArray();
                for (final RequestMethodType xbPostRequestMethod : xbPostRequestMethods) {
                    final OnlineResource ocOnlineRessource = new OnlineResource(xbPostRequestMethod.getHref());
                    final RequestMethod ocRequestMethod = new PostRequestMethod(ocOnlineRessource);
                    ocRequestMethods.add(ocRequestMethod);
                }

                ocDcps[j] = new DCP(ocRequestMethods);
            }

            //
            // map the operations parameters:
            //

            final DomainType[] xbParameters = xbOperation.getParameterArray();

            final List<Parameter> ocParameters = new ArrayList<Parameter>();

            for (final DomainType xbParameter : xbParameters) {

                final String parameterName = xbParameter.getName();

                if (!parameterName.equalsIgnoreCase("eventTime")) {

                    //
                    // map the parameters' values to StringValueDomains
                    //

                    final AllowedValues xbAllowedValues = xbParameter.getAllowedValues();
                    
                    if (xbAllowedValues != null) {
                        final ValueType[] xbValues = xbAllowedValues.getValueArray();
    
                        final StringValueDomain ocValues = new StringValueDomain();
                        for (final ValueType xbValue : xbValues) {
                            ocValues.addPossibleValue(xbValue.getStringValue());
                        }
    
                        final Parameter ocParameter = new Parameter(parameterName, true, ocValues, null);
                        ocParameters.add(ocParameter);
                    }
                }
            }

            final Parameter[] parametersArray = new Parameter[ocParameters.size()];
            ocParameters.toArray(parametersArray);

            ocOperations[i] = new Operation(ocOperationName, parametersArray, null, ocDcps);
        }

        return new OperationsMetadata(ocOperations);
    }

    private SOSContents mapContents(final CapabilitiesDocument capabilitiesDoc) throws OXFException {
        final ContentsDocument.Contents xbContents = capabilitiesDoc.getCapabilities().getContents();

        final ContentsDocument.Contents.ObservationOfferingList xbObsOfferingList = xbContents.getObservationOfferingList();

        final ObservationOfferingType[] xbObsOfferings = xbObsOfferingList.getObservationOfferingArray();
        final ArrayList<ObservationOffering> ocObsOffList = new ArrayList<ObservationOffering>();
        for (final ObservationOfferingType xbObsOffering : xbObsOfferings) {
            

//            if(xbObsOffering instanceof net.opengis.gml.AbstractFeatureType);
//            if(xbObsOffering instanceof net.opengis.gml.AbstractGMLType);
            
            // identifier
            final String ocIdentifier = xbObsOffering.getId();

            // title (take the first name or if name does not exist take the id)
            String ocTitle;
            final net.opengis.gml.CodeType[] xbNames = xbObsOffering.getNameArray();
            if (xbNames != null && xbNames.length > 0 && xbNames[0].getStringValue() != null) {
                ocTitle = xbNames[0].getStringValue();
            }
            else {
                ocTitle = xbObsOffering.getId();
            }

            // availableCRSs:
            final EnvelopeType envelope = xbObsOffering.getBoundedBy().getEnvelope();
            final String ocCrs = envelope.getSrsName();

            String[] ocAvailabaleCRSs = null;
            if (ocCrs != null) {
                ocAvailabaleCRSs = new String[] {ocCrs};
            }
            else {
                // TODO Think about throwing an exception because of one missing attribute because this situation can occur often when a new offering is set up and no data is available. Set it null and then it's okay! (ehjuerrens@52north.org)
                // throw new NullPointerException("SRS not specified in the Capabilities' 'gml:Envelope'-tag.");
            }

            // BoundingBox:
            final DirectPositionType lowerCorner = envelope.getLowerCorner();
            final List<?> xbLowerCornerList = lowerCorner.getListValue();
            double[] ocLowerCornerList = new double[xbLowerCornerList.size()];

            final DirectPositionType upperCorner = envelope.getUpperCorner();
            final List<?> xbUpperCornerList = upperCorner.getListValue();
            double[] ocUpperCornerList = new double[xbUpperCornerList.size()];

            if (ocLowerCornerList.length == 0) {
                LOGGER.warn("Evelope contains invalid lower corner: {}.", envelope.xmlText());
                ocLowerCornerList = new double[] {0.0, 0.0};
            } else {
                for (int j = 0; j < xbLowerCornerList.size(); j++) {
                    ocLowerCornerList[j] = (Double) xbLowerCornerList.get(j);
                }
            }
            
            if (ocUpperCornerList.length == 0) {
                LOGGER.warn("Evelope contains invalid upper corner: {}.", envelope.xmlText());
                ocUpperCornerList = new double[] {0.0, 0.0};
            } else {
                for (int j = 0; j < xbUpperCornerList.size(); j++) {
                    ocUpperCornerList[j] = (Double) xbUpperCornerList.get(j);
                }
            }
            

            final IBoundingBox[] ocBbox = new IBoundingBox[1];
            ocBbox[0] = new BoundingBox(ocCrs, ocLowerCornerList, ocUpperCornerList);

            // outputFormats:
            final String[] ocOutputFormats = xbObsOffering.getResponseFormatArray();

            // TemporalDomain:
            final List<ITime> ocTimeList = new ArrayList<ITime>();
            if (xbObsOffering.getTime() != null && xbObsOffering.getTime().getTimeGeometricPrimitive() != null) {
              
                final XmlObject xo = xbObsOffering.getTime().getTimeGeometricPrimitive().newCursor().getObject();
                final SchemaType schemaType = xo.schemaType();
                if (schemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                    final TimeInstantType xbTimeInstant = (TimeInstantType) xo;
                    final String xbTimePos = xbTimeInstant.getTimePosition().getStringValue();

                    if ( !xbTimePos.equals("")) {
                        ocTimeList.add(TimeFactory.createTime(xbTimePos));
                    }
                }
                else if (schemaType.getJavaClass().isAssignableFrom(TimePeriodType.class)) {
                    final TimePeriodType xbTimePeriod = (TimePeriodType) xo;

                    final String beginPos = xbTimePeriod.getBeginPosition().getStringValue();
                    final String endPos = xbTimePeriod.getEndPosition().getStringValue();

                    if ( !beginPos.equals("") && !endPos.equals("")) {
                        final TimePeriod ocTimePeriod = new TimePeriod(beginPos, endPos);
                        ocTimeList.add(ocTimePeriod);
                    }
                }
            }
            final IDiscreteValueDomain<ITime> ocTemporalDomain = new TemporalValueDomain(ocTimeList);

            // resultModels:
            final List<String> resultModelList = new ArrayList<String>();

            final Object[] resultModelArray = xbObsOffering.getResultModelArray();
            for (final Object resultModelObject : resultModelArray) {
                final QName resultModelQName = (QName) resultModelObject;
                resultModelList.add(resultModelQName.getLocalPart());
            }
            final String[] ocResultModels = new String[resultModelList.size()];
            resultModelList.toArray(ocResultModels);

            // result:
            final FilterValueDomain filterDomain = new FilterValueDomain();
            final FilterCapabilities filterCaps = capabilitiesDoc.getCapabilities().getFilterCapabilities();
            if (filterCaps != null){
                if (filterCaps.getScalarCapabilities().getComparisonOperators()!= null){
                	final ComparisonOperatorType.Enum[] xbCompOpsArray = filterCaps.getScalarCapabilities().getComparisonOperators().getComparisonOperatorArray();
                	for (final ComparisonOperatorType.Enum compOp : xbCompOpsArray) {
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
            final PhenomenonPropertyType[] xbObservedProperties = xbObsOffering.getObservedPropertyArray();
            final List<String> obsPropsList = new ArrayList<String>();

            for (final PhenomenonPropertyType xbObservedPropertie : xbObservedProperties) {
                if (xbObservedPropertie.getHref() != null) {
                    obsPropsList.add(xbObservedPropertie.getHref());
                }
                else if (xbObservedPropertie.getPhenomenon() != null
                        && xbObservedPropertie.getPhenomenon() instanceof CompositePhenomenonType) {
                    final CompositePhenomenonType compositePhenomenon = (CompositePhenomenonType) xbObservedPropertie.getPhenomenon();

                    final PhenomenonPropertyType[] phenomenonPropArray = compositePhenomenon.getComponentArray();
                    for (final PhenomenonPropertyType phenomenonProp : phenomenonPropArray) {
                        obsPropsList.add(phenomenonProp.getHref());
                    }
                }
            }
            String[] ocObsProps = new String[obsPropsList.size()];
            ocObsProps = obsPropsList.toArray(ocObsProps);

            // procedures:
            final ReferenceType[] xbProcedures = xbObsOffering.getProcedureArray();
            final String[] ocProcedures = new String[xbProcedures.length];
            for (int j = 0; j < xbProcedures.length; j++) {
                ocProcedures[j] = xbProcedures[j].getHref();
            }

            // featuresOfInterest:
            final ReferenceType[] xbFoi = xbObsOffering.getFeatureOfInterestArray();
            final String[] ocFoiIDs = new String[xbFoi.length];
            for (int j = 0; j < xbFoi.length; j++) {
                ocFoiIDs[j] = xbFoi[j].getHref();
            }

            // responseModes:
            final ResponseModeType.Enum[] xbResponseModes = xbObsOffering.getResponseModeArray();
            final String[] ocRespModes = new String[xbResponseModes.length];
            for (int j = 0; j < xbResponseModes.length; j++) {
                ocRespModes[j] = xbResponseModes[j].toString();
            }

            // TODO: these variables should also be initialized
            final String ocFees = null;
            final String ocPointOfContact = null;
            final Locale[] ocLanguage = null;
            final String[] ocKeywords = null;
            final String ocAbstractDescription = null;

            final ObservationOffering ocObsOff = new ObservationOffering(ocTitle,
                                                                    ocIdentifier,
                                                                    ocBbox,
                                                                    ocOutputFormats,
                                                                    ocAvailabaleCRSs,
                                                                    ocFees,
                                                                    ocLanguage,
                                                                    ocPointOfContact,
                                                                    ocTemporalDomain,
                                                                    ocAbstractDescription,
                                                                    ocKeywords,
                                                                    ocProcedures,
                                                                    ocFoiIDs,
                                                                    ocObsProps,
                                                                    ocResultModels,
                                                                    ocRespModes,
                                                                    filterDomain);

            ocObsOffList.add(ocObsOff);
        }

        return new SOSContents(ocObsOffList);
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private ServiceIdentification mapServiceIdentification(final net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification xbServiceId) {

        final String ocTitle = xbServiceId.getTitleArray(0).getStringValue();
        final String ocServiceType = xbServiceId.getServiceType().getStringValue();
        final String[] ocServiceTypeVersions = xbServiceId.getServiceTypeVersionArray();

        final String ocFees = xbServiceId.getFees();
        final String[] ocAccessConstraints = xbServiceId.getAccessConstraintsArray();
        final String ocAbstract = xbServiceId.getAbstractArray(0).getStringValue();
        String[] ocKeywords = null;

        final Vector<String> ocKeywordsVec = new Vector<String>();
        for (int i = 0; i < xbServiceId.getKeywordsArray().length; i++) {
            final LanguageStringType[] xbKeywords = xbServiceId.getKeywordsArray(i).getKeywordArray();
            for (final LanguageStringType xbKeyword : xbKeywords) {
                ocKeywordsVec.add(xbKeyword.getStringValue());
            }
        }
        ocKeywords = new String[ocKeywordsVec.size()];
        ocKeywordsVec.toArray(ocKeywords);

        return new ServiceIdentification(ocTitle,
                                         ocServiceType,
                                         ocServiceTypeVersions,
                                         ocFees,
                                         ocAccessConstraints,
                                         ocAbstract,
                                         ocKeywords);
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