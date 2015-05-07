/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.sos.adapter.v200;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.opengis.fes.x20.ComparisonOperatorType;
import net.opengis.fes.x20.ComparisonOperatorsType;
import net.opengis.fes.x20.FilterCapabilitiesDocument.FilterCapabilities;
import net.opengis.fes.x20.ScalarCapabilitiesType;
import net.opengis.fes.x20.SpatialCapabilitiesType;
import net.opengis.fes.x20.TemporalCapabilitiesType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.EnvelopeDocument;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.ows.x11.AllowedValuesDocument.AllowedValues;
import net.opengis.ows.x11.ContactType;
import net.opengis.ows.x11.DCPDocument;
import net.opengis.ows.x11.DomainType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.ows.x11.OperationDocument;
import net.opengis.ows.x11.RequestMethodType;
import net.opengis.ows.x11.ResponsiblePartySubsetType;
import net.opengis.ows.x11.ValueType;
import net.opengis.sos.x20.CapabilitiesDocument;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.CapabilitiesType.Contents;
import net.opengis.sos.x20.ContentsType;
import net.opengis.sos.x20.ObservationOfferingDocument;
import net.opengis.sos.x20.ObservationOfferingType;
import net.opengis.sos.x20.ObservationOfferingType.ResultTime;
import net.opengis.swes.x20.AbstractContentsType.Offering;
import net.opengis.swes.x20.AbstractOfferingType.RelatedFeature;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.ows.Constraint;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Contact;
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
import org.n52.oxf.ows.capabilities.ServiceContact;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.util.web.HttpClient;
import org.n52.oxf.util.web.HttpClientException;
import org.n52.oxf.util.web.SimpleHttpClient;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.filter.ComparisonFilter;
import org.n52.oxf.valueDomains.filter.FilterValueDomain;
import org.n52.oxf.valueDomains.filter.IFilter;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSCapabilitiesMapper_200 {

	private static final Logger LOGGER = LoggerFactory.getLogger(SOSCapabilitiesMapper_200.class);

    public ServiceDescriptor mapCapabilities(final CapabilitiesDocument capabilitiesDoc) throws OXFException {

        final String version = mapVersion(capabilitiesDoc);
        final CapabilitiesType capabilities = capabilitiesDoc.getCapabilities();
        final ServiceProvider serviceProvider = mapServiceProvider(capabilities);
        final OperationsMetadata operationsMetadata = mapOperationsMetadata(capabilities);
        final ServiceIdentification serviceIdentification = mapServiceIdentification(capabilities);
        // OperationsMetadata operationsMetadata = null;
        final SOSContents contents = mapContents(capabilitiesDoc);

        // addDatasetParameterFromContentsSection(operationsMetadata, contents);

        final ServiceDescriptor serviceDesc = new ServiceDescriptor(version,
                                                              serviceIdentification,
                                                              serviceProvider,
                                                              operationsMetadata,
                                                              contents);
        return serviceDesc;
    }

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

    private OperationsMetadata mapOperationsMetadata(final CapabilitiesType capabilities) {

        if ( !capabilities.isSetOperationsMetadata()) {
            return null;
        }
        final net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata operationsMetadata = capabilities.getOperationsMetadata();
        //
        // map the operations:
        //
        final OperationDocument.Operation[] xbOperations = operationsMetadata.getOperationArray();
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
                    addConstraints(xbGetRequestMethod, ocRequestMethod);
                    ocRequestMethods.add(ocRequestMethod);
                }
                final RequestMethodType[] xbPostRequestMethods = xbDcp.getHTTP().getPostArray();
                for (final RequestMethodType xbPostRequestMethod : xbPostRequestMethods) {
                    final OnlineResource ocOnlineRessource = new OnlineResource(xbPostRequestMethod.getHref());
                    final RequestMethod ocRequestMethod = new PostRequestMethod(ocOnlineRessource);
                    addConstraints(xbPostRequestMethod, ocRequestMethod);
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

	private void addConstraints(final RequestMethodType xbGetRequestMethod,
			final RequestMethod ocRequestMethod)
	{
		if (xbGetRequestMethod.getConstraintArray() != null ) {
			for (final DomainType xbConstraint : xbGetRequestMethod.getConstraintArray()) {
				final String name = xbConstraint.getName();
				final ArrayList<String> allowedValues = new ArrayList<String>();
				if(xbConstraint.isSetAllowedValues()) {
					for (final ValueType xbAllowedValue : xbConstraint.getAllowedValues().getValueArray()) {
						allowedValues.add(xbAllowedValue.getStringValue());
					}
				}
				if (name != null && !name.isEmpty() && !allowedValues.isEmpty()) {
					ocRequestMethod.addOwsConstraint(new Constraint(name,allowedValues.toArray(new String[allowedValues.size()])));
				}
			}
		}
	}

    public static void main(final String[] args) {
        try {
            final HttpClient client = new SimpleHttpClient();
            final String request = "http://sensorweb.demo.52north.org/52nSOSv3_200/sos?REQUEST=GetCapabilities&SERVICE=SOS";

            final HttpResponse response = client.executeGet(request);
            final HttpEntity responseEntity = response.getEntity();
            final InputStream responseStream = responseEntity.getContent();
            new SOSCapabilitiesMapper_200().mapCapabilities(CapabilitiesDocument.Factory.parse(responseStream));
        }
        catch (final HttpClientException e) {
        	LOGGER.error("Exception thrown: {}",e.getMessage(),e);
        }
        catch (final IOException e) {
        	LOGGER.error("Exception thrown: {}",e.getMessage(),e);
        }
        catch (final XmlException e) {
        	LOGGER.error("Exception thrown: {}",e.getMessage(),e);
        }
        catch (final OXFException e) {
        	LOGGER.error("Exception thrown: {}",e.getMessage(),e);
        }
    }

    private SOSContents mapContents(final CapabilitiesDocument capabilitiesDoc) throws OXFException {
        final CapabilitiesType capabilities = capabilitiesDoc.getCapabilities();
        final Contents xb_contents = capabilities.getContents();
        final ContentsType contentsType = xb_contents.getContents();
        final String[] observablePropertys = contentsType.getObservablePropertyArray();
        final String[] responseFormats = contentsType.getResponseFormatArray();
        final Offering[] xb_obsOfferings = contentsType.getOfferingArray();
        final ArrayList<ObservationOffering> oc_obsOffList = new ArrayList<ObservationOffering>();
        for (final Offering xb_obsOffering2 : xb_obsOfferings) {
            ObservationOfferingDocument xb_obsOfferingDoc = null;
            try {
                xb_obsOfferingDoc = ObservationOfferingDocument.Factory.parse(xb_obsOffering2.newInputStream());
            }
            catch (final XmlException e) {
                throw new OXFException("Could not parse DOM node.", e);
            }
            catch (final IOException e) {
                throw new OXFException("Could not parse DOM node.", e);
            }
            final ObservationOfferingType xb_obsOffering = xb_obsOfferingDoc.getObservationOffering();

            if ( !xb_obsOffering.isSetObservedArea()) {
                continue; // does not contain any observations/features
            }

            IBoundingBox[] oc_bbox = null;
            String[] oc_availabaleCRSs = null;
            EnvelopeDocument envelopeDoc = null;
            try {
                envelopeDoc = EnvelopeDocument.Factory.parse(xb_obsOffering.getObservedArea().newInputStream());
            }
            catch (final IOException e) {
                throw new OXFException("Could not get DOM node.", e);
            }
            catch (final XmlException e) {
                throw new OXFException("Could not get DOM node.", e);
            }

            final EnvelopeType envelope = envelopeDoc.getEnvelope();
            if (envelope != null) {
                // availableCRSs:
                final String oc_crs = envelope.getSrsName();
                if (oc_crs != null) {
                    oc_availabaleCRSs = new String[] {oc_crs};
                }
//                else {
                    // TODO Think about throwing an exception because of one missing attribute because this
                    // situation can occur often when a new offering is set up and no data is available. Set
                    // it null and then it's okay! (ehjuerrens@52north.org)
                    // throw new
                    // NullPointerException("SRS not specified in the Capabilities' 'gml:Envelope'-tag.");
//                }

                // BoundingBox:
                oc_bbox = new IBoundingBox[1];
                final List<?> xb_lowerCornerList = envelope.getLowerCorner().getListValue();
                final double[] oc_lowerCornerList = new double[xb_lowerCornerList.size()];

                final List<?> xb_upperCornerList = envelope.getUpperCorner().getListValue();
                final double[] oc_upperCornerList = new double[xb_upperCornerList.size()];

                for (int j = 0; j < xb_lowerCornerList.size(); j++) {
                    oc_lowerCornerList[j] = (Double) xb_lowerCornerList.get(j);
                }
                for (int j = 0; j < xb_upperCornerList.size(); j++) {
                    oc_upperCornerList[j] = (Double) xb_upperCornerList.get(j);
                }

                oc_bbox[0] = new BoundingBox(oc_crs, oc_lowerCornerList, oc_upperCornerList);
            }

            // result:
            final RelatedFeature[] oc_foiIDs = xb_obsOffering.getRelatedFeatureArray();
            final String[] oc_relatedFeatures = new String[oc_foiIDs.length];
            for (int j = 0; j < oc_foiIDs.length; j++) {
                // FIXME this is 52n SOS 2.0 specific
                oc_relatedFeatures[j] = oc_foiIDs[j].getFeatureRelationship().getTarget().getHref();
            }

            // TODO: these variables should also be initialized
            final String oc_pointOfContact = null;
            final Locale[] oc_language = null;
            final String[] oc_keywords = null;
            final String oc_abstractDescription = null;

            final ObservationOffering oc_obsOff = new ObservationOffering(getTitle(xb_obsOffering),
                                                                    xb_obsOffering.getIdentifier(),
                                                                    oc_bbox,
                                                                    (String[]) ArrayUtils.addAll(responseFormats,xb_obsOffering.getResponseFormatArray()),
                                                                    oc_availabaleCRSs,
                                                                    capabilities.getServiceIdentification().getFees(),
                                                                    oc_language,
                                                                    oc_pointOfContact,
                                                                    getTemporalDomain(xb_obsOffering),
                                                                    oc_abstractDescription,
                                                                    oc_keywords,
                                                                    new String[] {xb_obsOffering.getProcedure()},
                                                                    oc_relatedFeatures,
                                                                    (String[]) ArrayUtils.addAll(xb_obsOffering.getObservablePropertyArray(),observablePropertys),
                                                                    null, // not supported in SOS 2.0
                                                                    xb_obsOffering.getResponseFormatArray(),
                                                                    getFilterDomain(capabilities));

            oc_obsOffList.add(oc_obsOff);
        }

        return new SOSContents(oc_obsOffList);
    }

    private IDiscreteValueDomain<ITime> getTemporalDomain(final ObservationOfferingType xb_obsOffering)
    {
    	// TemporalDomain:
    	final List<ITime> oc_timeList = new ArrayList<ITime>();
    	final ResultTime resultTime = xb_obsOffering.getResultTime();
    	if (resultTime != null && resultTime.getTimePeriod() != null) {
    		final TimePeriodType xb_timePeriod = resultTime.getTimePeriod();
    		final String beginPos = xb_timePeriod.getBeginPosition().getStringValue();
    		final String endPos = xb_timePeriod.getEndPosition().getStringValue();
    		if ( !beginPos.equals("") && !endPos.equals("")) {
    			final TimePeriod oc_timePeriod = new TimePeriod(beginPos, endPos);
    			oc_timeList.add(oc_timePeriod);
    		}
    	}
    	return new TemporalValueDomain(oc_timeList);
    }

	private FilterValueDomain getFilterDomain(final CapabilitiesType capabilities)
	{
		final FilterValueDomain filterDomain = new FilterValueDomain();
		if (capabilities.getFilterCapabilities() != null) {
		    final FilterCapabilities filterCaps = capabilities.getFilterCapabilities().getFilterCapabilities();
		    if (filterCaps != null) {
		        processScalarFilterCapabilities(filterDomain, filterCaps.getScalarCapabilities());
		        processSpatialFilterCapabilities(filterDomain, filterCaps.getSpatialCapabilities());
		        processTemporalFilterCapabilities(filterDomain, filterCaps.getTemporalCapabilities());
		    }
		}
		return filterDomain;
	}

	private String getTitle(final ObservationOfferingType xb_obsOffering)
	{
		// title (take the first name or if name does not exist take the id)
		final CodeType[] xb_names = xb_obsOffering.getNameArray();
        String oc_title;
		if (xb_names != null && xb_names.length > 0 && xb_names[0].getStringValue() != null) {
            oc_title = xb_names[0].getStringValue();
        }
        else {
            oc_title = xb_obsOffering.getIdentifier();
        }
		return oc_title;
	}

	private void processScalarFilterCapabilities(final FilterValueDomain filterDomain,
                                                 final ScalarCapabilitiesType scalarCapabilities) {
        if (scalarCapabilities != null) {
            final ComparisonOperatorsType comparisonOperators = scalarCapabilities.getComparisonOperators();
            if (comparisonOperators != null) {
                final ComparisonOperatorType[] xb_compOpsArray = comparisonOperators.getComparisonOperatorArray();
                for (final ComparisonOperatorType compOp : xb_compOpsArray) {
                    if (compOp.equals(ComparisonOperatorType.EQUAL)) {
                        final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_EQUAL_TO);
                        filterDomain.addPossibleValue(filter);
                    }
                    else if (compOp.equals(ComparisonOperatorType.GREATER_THAN)) {
                        final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_GREATER_THAN);
                        filterDomain.addPossibleValue(filter);
                    }
                    else if (compOp.equals(ComparisonOperatorType.LESS_THAN)) {
                        final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_LESS_THAN);
                        filterDomain.addPossibleValue(filter);
                    }
                    else if (compOp.equals(ComparisonOperatorType.NOT_EQUAL)) {
                        final IFilter filter = new ComparisonFilter(ComparisonFilter.PROPERTY_IS_NOT_EQUAL_TO);
                        filterDomain.addPossibleValue(filter);
                    }
                }
            }
        }
    }

    private void processSpatialFilterCapabilities(final FilterValueDomain filterDomain,
                                                  final SpatialCapabilitiesType spatialCapabilities) {
//        if (spatialCapabilities != null) {
            // TODO implement
            // throw new NotImplementedException();
//        }
    }

    private void processTemporalFilterCapabilities(final FilterValueDomain filterDomain,
                                                   final TemporalCapabilitiesType temporalCapabilities) {
//        if (temporalCapabilities != null) {
            // TODO implement
            // throw new NotImplementedException();
//        }

    }

    private ServiceIdentification mapServiceIdentification(final CapabilitiesType capabilities) {

        if ( !capabilities.isSetServiceIdentification()) {
            return null;
        }

        final net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification serviceIdentification = capabilities.getServiceIdentification();

        final String oc_title = serviceIdentification.getTitleArray(0).getStringValue();
        final String oc_serviceType = serviceIdentification.getServiceType().getStringValue();
        final String[] oc_serviceTypeVersions = serviceIdentification.getServiceTypeVersionArray();

        final String oc_fees = serviceIdentification.getFees();
        final String[] oc_accessConstraints = serviceIdentification.getAccessConstraintsArray();
        String oc_abstract = null;
        if (serviceIdentification.getAbstractArray() != null) {
            oc_abstract = serviceIdentification.getAbstractArray(0).getStringValue();
        }
        String[] oc_keywords = null;

        final List<String> oc_keywordsVec = new ArrayList<String>();
        for (int i = 0; i < serviceIdentification.getKeywordsArray().length; i++) {
            final LanguageStringType[] xb_keywords = serviceIdentification.getKeywordsArray(i).getKeywordArray();
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

    private String mapVersion(final CapabilitiesDocument capsDoc) {
        return capsDoc.getCapabilities().getVersion();
    }

    private static ServiceProvider mapServiceProvider(final CapabilitiesType capabilities) {
        if ( !capabilities.isSetServiceProvider()) {
            return null;
        }
        final net.opengis.ows.x11.ServiceProviderDocument.ServiceProvider serviceProvider = capabilities.getServiceProvider();
        final String name = serviceProvider.getProviderName();
        final ResponsiblePartySubsetType contact = serviceProvider.getServiceContact();

        final String individualName = contact.getIndividualName();
        final String organisationName = null;
        final String positionName = contact.getPositionName();
        final ContactType contactInfo = contact.getContactInfo(); // TODO parse info
        final Contact info = new Contact(null, null, null, null, null, null);
        final ServiceContact serviceContact = new ServiceContact(individualName, organisationName, positionName, info);

        return new ServiceProvider(name, serviceContact);
    }

}