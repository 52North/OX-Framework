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
package org.n52.oxf.wcs.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import net.opengis.ows.x11.BoundingBoxType;
import net.opengis.wcs.x11.CapabilitiesDocument;
import net.opengis.wcs.x11.CoverageDescriptionType;
import net.opengis.wcs.x11.CoverageDescriptionsDocument;
import net.opengis.wcs.x11.CoverageSummaryType;
import net.opengis.wcs.x11.GridCrsType;

import org.apache.xmlbeans.XmlCursor;
import org.n52.oxf.OXFException;
import org.n52.oxf.OXFThrowableCollection;
import org.n52.oxf.ows.capabilities.Address;
import org.n52.oxf.ows.capabilities.Contact;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.Dataset;
import org.n52.oxf.ows.capabilities.DatasetParameter;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ows.capabilities.PostRequestMethod;
import org.n52.oxf.ows.capabilities.ServiceContact;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.valueDomains.IntegerRangeValueDomain;
import org.n52.oxf.valueDomains.StringValueDomain;
import org.n52.oxf.valueDomains.spatial.BoundingBox;
import org.n52.oxf.valueDomains.time.TemporalValueDomain;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.wcs.capabilities.AddressType;
import org.n52.oxf.wcs.capabilities.ContactType;
import org.n52.oxf.wcs.capabilities.CoverageDataset;
import org.n52.oxf.wcs.capabilities.CoverageDescription;
import org.n52.oxf.wcs.capabilities.CoverageOfferingBriefType;
import org.n52.oxf.wcs.capabilities.CoverageOfferingType;
import org.n52.oxf.wcs.capabilities.DCPTypeType;
import org.n52.oxf.wcs.capabilities.LonLatEnvelopeType;
import org.n52.oxf.wcs.capabilities.OnlineResourceType;
import org.n52.oxf.wcs.capabilities.ResponsiblePartyType;
import org.n52.oxf.wcs.capabilities.SpatialDomainType;
import org.n52.oxf.wcs.capabilities.TelephoneType;
import org.n52.oxf.wcs.capabilities.TimePeriodType;
import org.n52.oxf.wcs.capabilities.TimeSequenceType;
import org.n52.oxf.wcs.capabilities.WCSCapabilitiesType;
import org.n52.oxf.wcs.capabilities.WCSCapabilityType.Request;
import org.n52.oxf.wcs.model.CodeListType;
import org.n52.oxf.wcs.model.DirectPositionType;
import org.n52.oxf.wcs.model.EnvelopeType;
import org.n52.oxf.wcs.model.TimePositionType;

/**
 * This class supplies methods to map the WCS-capabilities to the OWS-common-capabilities.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class WCSCapabilitiesMapper {

    private static final String DEFAULT_SRS = "WGS84";

    /**
     * @return
     * @throws OXFException
     */
    public ServiceIdentification mapServiceIdentification(WCSCapabilitiesType w) throws OXFException {
        String title = w.getService().getLabel();
        String serviceType = "OGC:WCS"; // -> cause its a WCS-Adapter
        String[] serviceTypeVersion = new String[] {"1.0.0"};

        ServiceIdentification si;
        try {
            si = new ServiceIdentification(title, serviceType, serviceTypeVersion);
        }
        catch (IllegalArgumentException e) {
            throw new OXFException(e);
        }
        return si;
    }

    public ServiceIdentification mapServiceIdentification(CapabilitiesDocument capDoc) throws OXFException {
        String title = capDoc.getCapabilities().getServiceIdentification().getTitleArray().toString();
        String serviceType = "OGC:WCS"; // -> cause its a WCS-Adapter
        String[] serviceTypeVersion = new String[] {"1.1.1"};

        ServiceIdentification si;
        try {
            si = new ServiceIdentification(title, serviceType, serviceTypeVersion);
        }
        catch (IllegalArgumentException e) {
            throw new OXFException(e);
        }

        return si;
    }

    /**
     * Supports WCS 1.0.0:
     * 
     * uses the CoverageOfferingDescription returned by the DescribeCoverage-Operation to produce a
     * Content-object.
     * 
     * @param w
     * @return
     * @throws OXFException
     * @throws EmptyParameterException
     */
    public Contents mapContents(CoverageDescription cd, OXFThrowableCollection throwCollect) {

        List<CoverageOfferingType> covOffList = cd.getCoverageOffering();

        ArrayList<Dataset> dataIdentificationList = new ArrayList<Dataset>();

        for (CoverageOfferingType covOff : covOffList) {

            // --- create identifier:
            List<JAXBElement> nameList = covOff.getName();
            String identifier = nameList.get(0).getValue().toString();

            // --- create title:
            String title = covOff.getLabel();

            // --- create boundingBox:
            // TODO: fuer die BBox koennte man auch
            // "(SpatialDomainType)covOff.getDomainSet().getContent().get(0).getValue()" nehmen

            ArrayList<BoundingBox> bBoxArrayList = new ArrayList<BoundingBox>();

            List<DirectPositionType> directPositionList = null;
            String srsName = null;

            if (covOff.getLonLatEnvelope() != null) {
                /*
                 * WCS 1.0.0 schema comment to 'LonLatEnvelopeBaseType': "For WCS use, LonLatEnvelopeBaseType
                 * restricts gml:Envelope to the WGS84 geographic CRS with Longitude preceding Latitude and
                 * both using decimal degrees only. If included, height values are third and use metre units."
                 */
                LonLatEnvelopeType lle = covOff.getLonLatEnvelope();
                directPositionList = lle.getPos();

                srsName = covOff.getLonLatEnvelope().getSrsName();

                if (srsName == null) {
                    srsName = DEFAULT_SRS;
                }
                bBoxArrayList.add(makeBBox(srsName, directPositionList));

            }

            List<JAXBElement> domainSetList = covOff.getDomainSet().getContent();
            JAXBElement firstDomainSet = domainSetList.get(0);

            if (firstDomainSet.getValue() != null) {
                SpatialDomainType spatialDomain = (SpatialDomainType) firstDomainSet.getValue();
                for (JAXBElement jaxvEnvelope : spatialDomain.getEnvelope()) {
                    EnvelopeType envelope = (EnvelopeType) jaxvEnvelope.getValue();

                    directPositionList = envelope.getPos();

                    srsName = envelope.getSrsName();

                    bBoxArrayList.add(makeBBox(srsName, directPositionList));
                }
            }

            // --- create timeEnvelope:
            List<JAXBElement> domainSet = covOff.getDomainSet().getContent();

            List<ITime> timeList = new ArrayList<ITime>();

            for (int i = 0; i < domainSet.size(); i++) {
                if (domainSet.get(i).getName().toString().equals("{http://www.opengis.net/wcs}temporalDomain")) {

                    TimeSequenceType timeSequence = (TimeSequenceType) domainSet.get(i).getValue();
                    List<Object> timeObjectList = timeSequence.getTimePositionOrTimePeriod();

                    for (Object o : timeObjectList) {
                        if (o instanceof TimePositionType) {
                            TimePositionType timePosition = (TimePositionType) o;
                            try {
                                ITime time = TimeFactory.createTime(timePosition.getValue().toString());
                                timeList.add(time);
                            }
                            catch (IllegalArgumentException e) {
                                // catch Time declarations with wrong syntax.
                                throwCollect.addThrowable(e);
                            }
                        }
                        else if (o instanceof TimePeriodType) {
                            TimePeriodType timePeriod = (TimePeriodType) o;
                            // TODO: ITime time =
                            // TimeFactory.createTime(timePeriod.getBeginPosition().getValue().toString()
                            // + "/" + timePeriod.getEndPosition().getValue().toString()
                            // + "/" + timePeriod.getTimeResolution());
                            // temporalDomain.add(time);

                        }
                    }
                }
            }

            // --- create outputFormat:
            List<String> outputFormats = new ArrayList<String>();
            for (CodeListType clt : covOff.getSupportedFormats().getFormats()) {
                for (String formatString : clt.getValue()) {
                    outputFormats.add(formatString);
                }
            }
            
            String[] outputFormatsArray = new String[outputFormats.size()];
            outputFormats.toArray(outputFormatsArray);

            // --- create availableCRSs:
            List<String> availableCRSs = new ArrayList<String>();
            for (CodeListType clt : covOff.getSupportedCRSs().getNativeCRSs()) {
                for (String crsString : clt.getValue()) {
                    if ( !availableCRSs.contains(crsString)) {
                        availableCRSs.add(crsString);
                    }
                }
            }
            for (CodeListType clt : covOff.getSupportedCRSs().getRequestCRSs()) {
                for (String crsString : clt.getValue()) {
                    if ( !availableCRSs.contains(crsString)) {
                        availableCRSs.add(crsString);
                    }
                }
            }
            for (CodeListType clt : covOff.getSupportedCRSs().getRequestResponseCRSs()) {
                for (String crsString : clt.getValue()) {
                    if ( !availableCRSs.contains(crsString)) {
                        availableCRSs.add(crsString);
                    }
                }
            }
            String[] availableCRSsArray = new String[availableCRSs.size()];
            availableCRSs.toArray(availableCRSsArray);

            BoundingBox[] bBoxes = bBoxArrayList.toArray(new BoundingBox[0]);
            // add Dataset to list:
            Dataset dataID = new Dataset(title,
                                         identifier,
                                         bBoxes,
                                         outputFormatsArray,
                                         availableCRSsArray,
                                         null,
                                         null,
                                         null,
                                         (timeList.isEmpty() ? null : new TemporalValueDomain(timeList)),
                                         null,
                                         null);
            dataIdentificationList.add(dataID);
        }

        Contents c = new Contents(dataIdentificationList);

        return c;
    }

    /**
     * Supports WCS 1.1.1:
     * 
     * Parses Capabilities Contents section.
     */
    public Contents mapContents(CoverageDescriptionType[] xb_covDesTypeArray) {
        
        ArrayList<Dataset> dataIdentificationList = new ArrayList<Dataset>();
        
        for (CoverageDescriptionType xb_covDesType : xb_covDesTypeArray) {
            String[] availableCRSsArray = xb_covDesType.getSupportedCRSArray();
            String[] outputFormatsArray = xb_covDesType.getSupportedFormatArray();

            String title = xb_covDesType.getTitleArray()[0].getStringValue();
            String identifier = xb_covDesType.getIdentifier();

            // initialize BBOX:

            BoundingBoxType[] xb_bBoxTypeArray = xb_covDesType.getDomain().getSpatialDomain().getBoundingBoxArray();
            List<BoundingBox> bBoxes = new ArrayList<BoundingBox>();
            for (BoundingBoxType xb_bBoxType : xb_bBoxTypeArray) {
                String ll = xb_bBoxType.getLowerCorner().toString();
                String ur = xb_bBoxType.getUpperCorner().toString();

                double[] lla = new double[2];
                for (int i = 0; i < 2; i++) {
                    lla[i] = new Double(ll.split(" ")[i]);
                }

                double[] ura = new double[2];
                for (int i = 0; i < 2; i++) {
                    ura[i] = new Double(ur.split(" ")[i]);
                }

                BoundingBox bBox = new BoundingBox(xb_bBoxType.getCrs(), lla, ura);
                bBoxes.add(bBox);
            }

            // parse Grid CRS configuration:
            
            GridCrsType xb_gridCrs = xb_covDesType.getDomain().getSpatialDomain().getGridCRS();

            String gridBaseCRS = null;
            String gridType = null;
            String gridCS = null;
            double[] gridOrigin = null;
            double[] gridOffsets = null;
            
            if (xb_gridCrs != null) {
                gridBaseCRS = xb_gridCrs.getGridBaseCRS();
                gridCS = xb_gridCrs.getGridCS();
                gridType = xb_gridCrs.getGridType();

                gridOrigin = new double[2];
                for (int i = 0; i < 2; i++) {
                    gridOrigin[i] = new Double(xb_gridCrs.getGridOrigin().get(i).toString());
                }
                
                gridOffsets = new double[2];
                for (int i = 0; i < 2; i++) {
                    gridOffsets[i] = new Double(xb_gridCrs.getGridOffsets().get(i).toString());
                }
            }
            
            // parse Image CRS configuration:
            
            
            // parse RPC link: (OWS-5 specific)
            String rpcLink = null;
            XmlCursor cursor = xb_covDesType.getDomain().getSpatialDomain().newCursor();
            boolean isTransformationAvailable = cursor.toChild(new QName("http://www.opengis.net/wcs/1.1.1", "OtherTransformation"));
            if (isTransformationAvailable) {
                rpcLink = cursor.getAttributeText(new QName("http://www.w3.org/1999/xlink", "href"));
            }
            
            CoverageDataset dataID = new CoverageDataset(title,
                                                         identifier,
                                                         bBoxes.toArray(new BoundingBox[0]),
                                                         outputFormatsArray,
                                                         availableCRSsArray,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         gridBaseCRS,
                                                         gridType,
                                                         gridOrigin,
                                                         gridOffsets,
                                                         gridCS,
                                                         rpcLink);
            dataIdentificationList.add(dataID);
        }

        return new Contents(dataIdentificationList);
    }
    
    /**
     * Supports WCS 1.1.1:
     * 
     * Parses Capabilities Contents section.
     */
    public Contents mapContents(CoverageDescriptionsDocument desCovDoc) {

        CoverageDescriptionType[] xb_covDesTypeArray = desCovDoc.getCoverageDescriptions().getCoverageDescriptionArray();

        return mapContents(xb_covDesTypeArray);
    }

    /**
     * Supports WCS 1.0.0
     * 
     * uses the WCSCapabilitiesType returned by the GetCapabilities-Operation to produce a Content-object.
     * 
     * @param wc
     * @return
     * @throws OXFException
     */
    public Contents mapContents(WCSCapabilitiesType wc) {

        List<CoverageOfferingBriefType> covOffList = wc.getContentMetadata().getCoverageOfferingBrief();

        ArrayList<Dataset> dataIdentificationList = new ArrayList<Dataset>();

        for (CoverageOfferingBriefType covOff : covOffList) {

            // --- create identifier:
            List<JAXBElement> nameList = covOff.getName();
            String identifier = nameList.get(0).getValue().toString();

            // --- create title:
            String title = covOff.getLabel();

            // --- create boundingBox:
            LonLatEnvelopeType lle = covOff.getLonLatEnvelope();

            // directPositionList enth�lt 2 Double-Listen die die Koordinatenwerte f�r die 2 definierenden
            // Punkte der BBox enthalten
            List<DirectPositionType> directPositionList = lle.getPos();

            DirectPositionType dpt0 = directPositionList.get(0);
            DirectPositionType dpt1 = directPositionList.get(1);

            double[] lowerLeft = new double[dpt0.getValue().size()];
            for (int i = 0; i < dpt0.getValue().size(); i++) {
                lowerLeft[i] = dpt0.getValue().get(i);
            }

            double[] upperRight = new double[dpt1.getValue().size()];
            for (int i = 0; i < dpt1.getValue().size(); i++) {
                upperRight[i] = dpt1.getValue().get(i);
            }

            String srsName = covOff.getLonLatEnvelope().getSrsName();

            BoundingBox bBox = new BoundingBox(srsName, lowerLeft, upperRight);

            // --- create srs:
            String[] srsArray = new String[] {lle.getSrsName()};

            // --- create temporalDomain:
            List<TimePositionType> timePosList = lle.getTimePosition();
            List<ITime> timeList = new ArrayList<ITime>();
            if (timePosList != null) {
                for (TimePositionType timePosition : timePosList) {
                    ITime time = TimeFactory.createTime(timePosition.getValue().toString());
                    timeList.add(time);
                }
            }

            // add Dataset to list:
            Dataset dataID = new Dataset(title,
                                         identifier,
                                         new BoundingBox[] {bBox},
                                         null,
                                         srsArray,
                                         null,
                                         null,
                                         null,
                                         (timeList.isEmpty() ? null : new TemporalValueDomain(timeList)),
                                         null,
                                         null);
            dataIdentificationList.add(dataID);
        }

        Contents c = new Contents(dataIdentificationList);

        return c;
    }

    /**
     * Supports WCS 1.1.1
     */
    public Contents mapContents(CapabilitiesDocument capDoc) {

        CoverageSummaryType[] covSumArray = capDoc.getCapabilities().getContents().getCoverageSummaryArray();

        ArrayList<Dataset> dataIdentificationList = new ArrayList<Dataset>();

        for (CoverageSummaryType covSum : covSumArray) {
            String title = covSum.getTitleArray()[0].getStringValue();
            String identifier = covSum.getIdentifier();
            String ll = covSum.getWGS84BoundingBoxArray()[0].getLowerCorner().toString();
            String ur = covSum.getWGS84BoundingBoxArray()[0].getUpperCorner().toString();

            double[] lla = new double[2];
            for (int i = 0; i < 2; i++) {
                lla[i] = new Double(ll.split(" ")[i]);
            }

            double[] ura = new double[2];
            for (int i = 0; i < 2; i++) {
                ura[i] = new Double(ur.split(" ")[i]);
            }

            BoundingBox bBox = new BoundingBox(lla, ura);

            // add Dataset to list:
            Dataset dataID = new Dataset(title, identifier, new BoundingBox[] {bBox});
            dataIdentificationList.add(dataID);
        }

        Contents c = new Contents(dataIdentificationList);

        return c;
    }

    /**
     * Supports WCS 1.0.0
     */
    public ServiceProvider mapServiceProvider(WCSCapabilitiesType wt) throws OXFException {
        ResponsiblePartyType responsibleParty = wt.getService().getResponsibleParty();

        ServiceContact serviceContact = null;

        String providerName = "xyz";// TODO: vernuenftigen Provider-Namen heraus ziehen

        if (responsibleParty != null) {
            List<JAXBElement> list = responsibleParty.getContent();
            for (JAXBElement e : list) {
                String localPart = e.getName().getLocalPart();

                String individualName = null;
                String organisationName = null;
                String positionName = null;
                Contact contact = null;

                if (localPart.equalsIgnoreCase("individualName")) {
                    individualName = e.getValue().toString();
                }
                else if (localPart.equalsIgnoreCase("organisationName")) {
                    organisationName = e.getValue().toString();
                }
                else if (localPart.equalsIgnoreCase("positionName")) {
                    positionName = e.getValue().toString();
                }
                else if (localPart.equalsIgnoreCase("contactInfo")) {

                    // Sub-Section "ContactInfo":
                    ContactType ct = (ContactType) e.getValue();

                    // Sub-Section "OnlineResource":
                    OnlineResourceType ort = (OnlineResourceType) ct.getOnlineResource();
                    OnlineResource onlineResource = null;
                    if (ort != null) {
                        onlineResource = new OnlineResource(ort.getType(),
                                                            ort.getHref(),
                                                            ort.getRole(),
                                                            ort.getArcrole(),
                                                            ort.getShow(),
                                                            ort.getActuate(),
                                                            ort.getTitle());
                    }

                    // Sub-Section "Phone":
                    TelephoneType tt = (TelephoneType) ct.getPhone();
                    String[] telephone = null;
                    if (tt != null) {
                        telephone = new String[tt.getVoice().size()];
                        tt.getVoice().toArray(telephone);
                    }

                    // Sub-Section "Address":
                    AddressType at = (AddressType) ct.getAddress();
                    Address address = null;

                    if (at != null) {
                        String administrativeArea = at.getAdministrativeArea();
                        String city = at.getCity();
                        String country = at.getCountry();
                        String postalCode = at.getPostalCode();

                        String[] tempDeliveryPointArray = new String[at.getDeliveryPoint().size()];
                        at.getDeliveryPoint().toArray(tempDeliveryPointArray);
                        String[] deliveryPoints = tempDeliveryPointArray;

                        String[] tempElectronicMailAddressArray = new String[at.getElectronicMailAddress().size()];
                        at.getElectronicMailAddress().toArray(tempElectronicMailAddressArray);
                        String[] electronicMailAddresses = tempElectronicMailAddressArray;

                        address = new Address(city,
                                              administrativeArea,
                                              postalCode,
                                              country,
                                              deliveryPoints,
                                              electronicMailAddresses);
                    }
                    contact = new Contact(telephone, null, null, null, address, onlineResource);
                }
                serviceContact = new ServiceContact(individualName, organisationName, positionName, contact);
            }
        }
        ServiceProvider sp = new ServiceProvider(providerName, serviceContact);

        return sp;
    }

    /**
     * Supports WCS 1.1.1
     */
    public ServiceProvider mapServiceProvider(CapabilitiesDocument capDoc) {

        Contact contact;
        ServiceContact serviceContact;

        String providerName = capDoc.getCapabilities().getServiceProvider().getProviderName();
        String individualName = capDoc.getCapabilities().getServiceProvider().getServiceContact().getIndividualName();
        String positionName = capDoc.getCapabilities().getServiceProvider().getServiceContact().getPositionName();

        String city = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getCity();
        String country = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getCountry();
        String administrativeArea = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getAdministrativeArea();
        String postalCode = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getPostalCode();
        String[] deliveryPoint = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getDeliveryPointArray();
        String[] mailAdress = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getAddress().getElectronicMailAddressArray();

        // TODO: check if organisation Name ist available
        String organisationName = providerName;

        Address address = new Address(city, administrativeArea, postalCode, country, deliveryPoint, mailAdress);
        String[] telephone = capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getPhone().getVoiceArray();

        OnlineResource onlineResource = new OnlineResource("http://www.onlineresource.de/hardcoded URL changes this WCSCapabilitiesMapper.java");
        // OnlineResource onlineResource = new
        // OnlineResource(capDoc.getCapabilities().getServiceProvider().getServiceContact().getContactInfo().getOnlineResource().getHref());
        contact = new Contact(telephone, null, null, null, address, onlineResource);

        serviceContact = new ServiceContact(individualName, organisationName, positionName, contact);
        ServiceProvider sp = new ServiceProvider(providerName, serviceContact);

        return sp;

    }

    /**
     * Supports WCS 1.0.0
     */
    public OperationsMetadata mapOperationsMetadata(WCSCapabilitiesType wt, Contents contents) throws OXFException {

        // --- GetCapabilities-Operation:
        Request.GetCapabilities getCaps = wt.getCapability().getRequest().getGetCapabilities();
        List<DCPTypeType> dcpListGetCapabilities = getCaps.getDCPType();

        Parameter serviceParameter = new Parameter("SERVICE", true, new StringValueDomain("WCS"), null);
        Parameter getCapRequest = new Parameter("REQUEST", true, new StringValueDomain("GetCapabilities"), null);
        String[] versions = new String[] {"1.0.0"};
        Parameter versionParameter = new Parameter("VERSION", true, new StringValueDomain(versions), null);
        Parameter section = new Parameter("SECTION",
                                          false,
                                          new StringValueDomain(new String[] {"WCS_Capabilities/Service",
                                                                              "WCS_Capabilities/Capability",
                                                                              "WCS_Capabilities/ContentMetadata"}),
                                          null);

        Parameter[] getCapabilitiesParameters = new Parameter[] {versionParameter,
                                                                 serviceParameter,
                                                                 getCapRequest,
                                                                 section};

        Operation opGetCapabilities = new Operation("GetCapabilities",
                                                    getCapabilitiesParameters,
                                                    null,
                                                    parseDCPList(dcpListGetCapabilities));

        // --- DescribeCoverage-Operation:
        Request.DescribeCoverage descCov = wt.getCapability().getRequest().getDescribeCoverage();
        List<DCPTypeType> dcpListDescribeCoverage = descCov.getDCPType();

        Parameter descCovRequest = new Parameter("REQUEST", true, new StringValueDomain("DescribeCoverage"), null);

        // -------- Coverage-Namen raus-parsen:
        String[] covNames = new String[contents.getDataIdentificationCount()];
        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            covNames[i] = contents.getDataIdentification(i).getIdentifier();
        }
        // -------- fertig

        Parameter coverageParameter = new Parameter("COVERAGE",
                                                    false,
                                                    new StringValueDomain(covNames),
                                                    Parameter.COMMON_NAME_RESOURCE_ID);

        Parameter[] describeCoverageParameters = new Parameter[] {versionParameter,
                                                                  serviceParameter,
                                                                  descCovRequest,
                                                                  coverageParameter};

        Operation opDescribeCoverage = new Operation("DescribeCoverage",
                                                     describeCoverageParameters,
                                                     null,
                                                     parseDCPList(dcpListDescribeCoverage));

        // --- GetCoverage-Operation:
        Request.GetCoverage getCov = wt.getCapability().getRequest().getGetCoverage();
        List<DCPTypeType> dcpListGetCoverage = getCov.getDCPType();

        ArrayList<Parameter> getCoverageParameterList = new ArrayList<Parameter>();

        Parameter getCovRequestParameter = new Parameter("REQUEST", true, new StringValueDomain("GetCoverage"), null);

        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            String[] availableCRS = contents.getDataIdentification(i).getAvailableCRSs();
            if (availableCRS != null) {
                getCoverageParameterList.add(new DatasetParameter("CRS",
                                                                  true,
                                                                  new StringValueDomain(availableCRS),
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_SRS));
            }

            for (int j = 0; j < contents.getDataIdentification(i).getBoundingBoxes().length; j++) {
                getCoverageParameterList.add(new DatasetParameter("BBOX",
                                                                  true,
                                                                  contents.getDataIdentification(i).getBoundingBoxes()[j],
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_BBOX));
            }

            TemporalValueDomain temporalDomain = (TemporalValueDomain) contents.getDataIdentification(i).getTemporalDomain();
            if (temporalDomain != null) {
                getCoverageParameterList.add(new DatasetParameter("TIME",
                                                                  true,
                                                                  temporalDomain,
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_TIME));
            }

            ArrayList<String> formatList = new ArrayList<String>();
            if (contents.getDataIdentification(i).getOutputFormats() != null) {
                for (int j = 0; j < contents.getDataIdentification(i).getOutputFormats().length; j++) {
                    formatList.add(contents.getDataIdentification(i).getOutputFormats()[j]);
                }
                getCoverageParameterList.add(new DatasetParameter("FORMAT",
                                                                  true,
                                                                  new StringValueDomain(formatList),
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_FORMAT));
            }
        }

        Parameter widthParameter = new Parameter("WIDTH",
                                                 true,
                                                 new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                 Parameter.COMMON_NAME_WIDTH);
        Parameter heightParameter = new Parameter("HEIGHT",
                                                  true,
                                                  new IntegerRangeValueDomain(0, Integer.MAX_VALUE),
                                                  Parameter.COMMON_NAME_HEIGHT);

        getCoverageParameterList.add(versionParameter);
        getCoverageParameterList.add(serviceParameter);
        getCoverageParameterList.add(getCovRequestParameter);
        getCoverageParameterList.add(coverageParameter);
        getCoverageParameterList.add(widthParameter);
        getCoverageParameterList.add(heightParameter);

        Parameter[] getCoverageParameters = new Parameter[getCoverageParameterList.size()];
        getCoverageParameterList.toArray(getCoverageParameters);

        Operation opGetCoverage = new Operation("GetCoverage",
                                                getCoverageParameters,
                                                null,
                                                parseDCPList(dcpListGetCoverage));

        Operation[] ops = new Operation[] {opGetCapabilities, opDescribeCoverage, opGetCoverage};
        OperationsMetadata om = new OperationsMetadata(ops);
        return om;

    }

    /**
     * Supports WCS 1.1.1
     */
    public OperationsMetadata mapOperationsMetadata(CapabilitiesDocument capDoc, Contents contents) throws OXFException {

        List<DCP> getCapDcps = new ArrayList<DCP>();
        List<DCP> desCovDcps = new ArrayList<DCP>();
        List<DCP> getCovDcps = new ArrayList<DCP>();

        net.opengis.ows.x11.OperationDocument.Operation[] opsArray = capDoc.getCapabilities().getOperationsMetadata().getOperationArray();
        for (net.opengis.ows.x11.OperationDocument.Operation op : opsArray) {
            if (op.getName().equalsIgnoreCase("GetCapabilities")) {
                getCapDcps.add(makeDCP(op));
            }
            else if (op.getName().equalsIgnoreCase("GetCoverage")) {
                getCovDcps.add(makeDCP(op));
            }
            else if (op.getName().equalsIgnoreCase("DescribeCoverage")) {
                desCovDcps.add(makeDCP(op));
            }
        }

        // --- GetCapabilities-Operation: ----------------------------------------------------------

        Parameter serviceParameter = new Parameter("SERVICE", true, new StringValueDomain("WCS"), null);
        Parameter getCapRequest = new Parameter("REQUEST", true, new StringValueDomain("GetCapabilities"), null);
        String[] versions = new String[] {"1.1.1"};
        Parameter versionParameter = new Parameter("VERSION", true, new StringValueDomain(versions), null);
        Parameter section = new Parameter("SECTION",
                                          false,
                                          new StringValueDomain(new String[] {"WCS_Capabilities/Service",
                                                                              "WCS_Capabilities/Capability",
                                                                              "WCS_Capabilities/ContentMetadata"}),
                                          null);

        Parameter[] getCapabilitiesParameters = new Parameter[] {versionParameter,
                                                                 serviceParameter,
                                                                 getCapRequest,
                                                                 section};

        Operation opGetCapabilities = new Operation("GetCapabilities",
                                                    getCapabilitiesParameters,
                                                    null,
                                                    getCapDcps.toArray(new DCP[0]));

        // --- DescribeCoverage Operation ----------------------------------------------------------

        Parameter descCovRequest = new Parameter("REQUEST", true, new StringValueDomain("DescribeCoverage"), null);

        // -------- Coverage-Namen raus-parsen:
        String[] covNames = new String[contents.getDataIdentificationCount()];
        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            covNames[i] = contents.getDataIdentification(i).getIdentifier();
        }
        // -------- fertig

        Parameter coverageParameter = new Parameter("COVERAGE",
                                                    false,
                                                    new StringValueDomain(covNames),
                                                    Parameter.COMMON_NAME_RESOURCE_ID);

        Parameter[] describeCoverageParameters = new Parameter[] {versionParameter,
                                                                  serviceParameter,
                                                                  descCovRequest,
                                                                  coverageParameter};

        Operation opDescribeCoverage = new Operation("DescribeCoverage",
                                                     describeCoverageParameters,
                                                     null,
                                                     desCovDcps.toArray(new DCP[0]));

        // --- GetCoverage Operation ---------------------------------------------------------------

        ArrayList<Parameter> getCoverageParameterList = new ArrayList<Parameter>();

        Parameter getCovRequestParameter = new Parameter("REQUEST", true, new StringValueDomain("GetCoverage"), null);

        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            String[] availableCRS = contents.getDataIdentification(i).getAvailableCRSs();
            if (availableCRS != null) {
                getCoverageParameterList.add(new DatasetParameter("CRS",
                                                                  true,
                                                                  new StringValueDomain(availableCRS),
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_SRS));
            }

            for (int j = 0; j < contents.getDataIdentification(i).getBoundingBoxes().length; j++) {
                getCoverageParameterList.add(new DatasetParameter("BBOX",
                                                                  true,
                                                                  contents.getDataIdentification(i).getBoundingBoxes()[j],
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_BBOX));
            }

            TemporalValueDomain temporalDomain = (TemporalValueDomain) contents.getDataIdentification(i).getTemporalDomain();
            if (temporalDomain != null) {
                getCoverageParameterList.add(new DatasetParameter("TIME",
                                                                  true,
                                                                  temporalDomain,
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_TIME));
            }

            ArrayList<String> formatList = new ArrayList<String>();
            if (contents.getDataIdentification(i).getOutputFormats() != null) {
                for (int j = 0; j < contents.getDataIdentification(i).getOutputFormats().length; j++) {
                    formatList.add(contents.getDataIdentification(i).getOutputFormats()[j]);
                }
                getCoverageParameterList.add(new DatasetParameter("FORMAT",
                                                                  true,
                                                                  new StringValueDomain(formatList),
                                                                  contents.getDataIdentification(i),
                                                                  Parameter.COMMON_NAME_FORMAT));
            }
        }

        // GridParameter

        // -------- GridParameter raus-parsen:
        String[] gridBaseCRSs = new String[contents.getDataIdentificationCount()];
        String[] gridTypes = new String[contents.getDataIdentificationCount()];
        String[] gridCSs = new String[contents.getDataIdentificationCount()];
        for (int i = 0; i < contents.getDataIdentificationCount(); i++) {
            gridBaseCRSs[i] = ((CoverageDataset) contents.getDataIdentification(i)).getGridBaseCRS();
            gridTypes[i] = ((CoverageDataset) contents.getDataIdentification(i)).getGridType();
            gridCSs[i] = ((CoverageDataset) contents.getDataIdentification(i)).getGridCS();
        }

        Parameter gridBaseCRS = new Parameter("GRIDBASECRS", true, new StringValueDomain(gridBaseCRSs), null);
        Parameter gridType = new Parameter("GRIDTYPE", true, new StringValueDomain(gridTypes), null);
        Parameter gridCS = new Parameter("GRIDCS", true, new StringValueDomain(gridCSs), null);

        Parameter gridOrigin = new Parameter("GRIDORIGIN", true, new StringValueDomain(), null);
        Parameter gridOffsets = new Parameter("GRIDOFFSETS", true, new StringValueDomain(), null);

        Parameter widthParameter = new Parameter("WIDTH", true, new IntegerRangeValueDomain(0, Integer.MAX_VALUE), Parameter.COMMON_NAME_WIDTH);
        Parameter heightParameter = new Parameter("HEIGHT", true, new IntegerRangeValueDomain(0, Integer.MAX_VALUE), Parameter.COMMON_NAME_HEIGHT);

        getCoverageParameterList.add(versionParameter);
        getCoverageParameterList.add(serviceParameter);
        getCoverageParameterList.add(getCovRequestParameter);
        getCoverageParameterList.add(coverageParameter);

        getCoverageParameterList.add(gridBaseCRS);
        getCoverageParameterList.add(gridType);
        getCoverageParameterList.add(gridCS);
        getCoverageParameterList.add(gridOffsets);
        getCoverageParameterList.add(gridOrigin);

        getCoverageParameterList.add(widthParameter);
        getCoverageParameterList.add(heightParameter);

        Parameter[] getCoverageParameters = new Parameter[getCoverageParameterList.size()];
        getCoverageParameterList.toArray(getCoverageParameters);

        Operation opGetCoverage = new Operation("GetCoverage",
                                                getCoverageParameters,
                                                null,
                                                getCovDcps.toArray(new DCP[0]));

        Operation[] ops = new Operation[] {opGetCapabilities, opDescribeCoverage, opGetCoverage};
        OperationsMetadata om = new OperationsMetadata(ops);
        return om;
    }

    private DCP[] parseDCPList(List<DCPTypeType> dcpList) {
        DCP[] dcps = new DCP[dcpList.size()];

        int i = 0;
        for (DCPTypeType dcpType : dcpList) {
            DCPTypeType.HTTP http = dcpType.getHTTP();
            List protocolList = http.getGetOrPost();

            GetRequestMethod rmGet = null;
            PostRequestMethod rmPost = null;
            for (Object protocol : protocolList) {
                if (protocol.getClass().equals(DCPTypeType.HTTP.Get.class)) {
                    OnlineResourceType ort = ((DCPTypeType.HTTP.Get) protocol).getOnlineResource();

                    OnlineResource or = new OnlineResource(ort.getType(),
                                                           ort.getHref(),
                                                           ort.getRole(),
                                                           ort.getArcrole(),
                                                           ort.getShow(),
                                                           ort.getActuate(),
                                                           ort.getTitle());
                    rmGet = new GetRequestMethod(or);
                }
                else if (protocol.getClass().equals(DCPTypeType.HTTP.Post.class)) {
                    OnlineResourceType ort = ((DCPTypeType.HTTP.Post) protocol).getOnlineResource();

                    OnlineResource or = new OnlineResource(ort.getType(),
                                                           ort.getHref(),
                                                           ort.getRole(),
                                                           ort.getArcrole(),
                                                           ort.getShow(),
                                                           ort.getActuate(),
                                                           ort.getTitle());
                    rmPost = new PostRequestMethod(or);
                }
            }
            DCP dcp = new DCP(rmGet, rmPost);
            dcps[i++] = dcp;
        }
        return dcps;
    }

    private DCP makeDCP(net.opengis.ows.x11.OperationDocument.Operation op) {
        // GetRequest
        OnlineResource orGet = new OnlineResource(op.getDCPArray()[0].getHTTP().getGetArray()[0].getHref());
        GetRequestMethod getCapReq = new GetRequestMethod(orGet);

        // PostRequest
        OnlineResource orPost = new OnlineResource(op.getDCPArray()[0].getHTTP().getPostArray()[0].getHref());
        PostRequestMethod postCapReq = new PostRequestMethod(orPost);

        DCP dcp = new DCP(getCapReq, postCapReq);
        return dcp;
    }

    private BoundingBox makeBBox(String srsName, List<DirectPositionType> directPositionList) {
        DirectPositionType dpt0 = directPositionList.get(0);
        DirectPositionType dpt1 = directPositionList.get(1);

        double[] lowerLeft = new double[dpt0.getValue().size()];
        for (int i = 0; i < dpt0.getValue().size(); i++) {
            lowerLeft[i] = dpt0.getValue().get(i);
        }

        double[] upperRight = new double[dpt1.getValue().size()];
        for (int i = 0; i < dpt1.getValue().size(); i++) {
            upperRight[i] = dpt1.getValue().get(i);
        }
        BoundingBox bBox = new BoundingBox(srsName, lowerLeft, upperRight);
        return bBox;
    }

}