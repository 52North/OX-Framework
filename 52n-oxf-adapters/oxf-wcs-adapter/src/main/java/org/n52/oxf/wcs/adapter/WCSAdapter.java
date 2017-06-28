/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import net.opengis.wcs.x11.CapabilitiesDocument;
import net.opengis.wcs.x11.CoverageDescriptionType;
import net.opengis.wcs.x11.CoverageDescriptionsDocument;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.OXFThrowableCollection;
import org.n52.oxf.adapter.IServiceAdapter;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.Dataset;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;
import org.n52.oxf.wcs.capabilities.CoverageDescription;
import org.n52.oxf.wcs.capabilities.WCSCapabilitiesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class WCSAdapter implements IServiceAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WCSAdapter.class);

    public static void main(String[] args) {

        // String wcsUrl = "D:/Eigene Dateien/Eigene eBooks/OGC - Specs/wcs/example_xml_responses/";
        // String wcsUrl = "http://gws2.pcigeomatics.com/wcs1.0.0/wcs";
        // String wcsUrl = "http://demo.cubewerx.com/ows2/cubeserv/cubeserv.cgi";
        // String wcsUrl = "http://ogc.intergraph.com/wcs/request.asp";
        // String wcsUrl = "http://ws.spotimage.com/sisa_wcs_sd/coverage/SS5_031105";

        String wcsUrl = "http://ws.spotimage.com/axis2/services/WcsLevel1A";
        // String wcsUrl = "http://localhost:8080/TestWCS/TestWCS";

        try {
            ServiceDescriptor sd = new WCSAdapter().initService(wcsUrl);

            System.out.println(sd.toXML());
        }
        catch (OXFException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param urlString
     * @throws OXFException
     */
    public ServiceDescriptor initService(String serviceUrlString) throws OXFException {
        return initService(serviceUrlString, false);
    }

    /**
     * @param urlString
     * @param urlIsGetCGetCapabilities
     * @throws OXFException
     */
    public ServiceDescriptor initService(String serviceUrlString, boolean urlIsGetCGetCapabilities) throws OXFException {

        OXFThrowableCollection throwCollect = new OXFThrowableCollection();

        ServiceDescriptor sd;

        DCP[] dcps = new DCP[] {new DCP(new GetRequestMethod(new OnlineResource(serviceUrlString)), null)};

        //
        // request GetCapabilities information to build up the ServiceDescriptor:
        //

        OperationResult opResult_GetCapabilities;

        try {
            if (urlIsGetCGetCapabilities) {
                URL requestURL = new URL(serviceUrlString);
                InputStream res = requestURL.openStream();

                opResult_GetCapabilities = new OperationResult(res, null, serviceUrlString);
                serviceUrlString = serviceUrlString.substring(0, serviceUrlString.indexOf("?"));
                dcps = new DCP[] {new DCP(new GetRequestMethod(new OnlineResource(serviceUrlString)), null)};
            }
            else {
                opResult_GetCapabilities = doOperation(new Operation("GetCapabilities", dcps), new ParameterContainer());
            }

            String version;
            WCSCapabilitiesMapper wcsCapsMapper = new WCSCapabilitiesMapper();
            ServiceIdentification serviceIdentificationSection;
            ServiceProvider serviceProviderSection;
            Contents contentsSection;
            OperationsMetadata operationsMetadataSection;


            // VERSION 1.0.0:

            try {
                JAXBContext jc = JAXBContext.newInstance("org.n52.oxf.wcs.model.version100.wcsCapabilities:org.n52.oxf.adapter.wcs.model.version100.gml");
                Unmarshaller u = jc.createUnmarshaller();

                WCSCapabilitiesType wc = (WCSCapabilitiesType) ((JAXBElement) u.unmarshal(opResult_GetCapabilities.getIncomingResultAsAutoCloseStream())).getValue();
                version = wc.getVersion();

                serviceIdentificationSection = wcsCapsMapper.mapServiceIdentification(wc);
                serviceProviderSection = wcsCapsMapper.mapServiceProvider(wc);
                contentsSection = wcsCapsMapper.mapContents(wc);

                //
                // try to request DescribeCoverage information and integrate it into the ServiceDescriptor:
                //
                String[] coverages = new String[contentsSection.getDataIdentificationCount()];
                for (int i = 0; i < contentsSection.getDataIdentificationCount(); i++) {
                    Dataset coverageSummary = contentsSection.getDataIdentification(i);
                    coverages[i] = coverageSummary.getIdentifier();
                }
                ParameterContainer paramCon = new ParameterContainer();
                paramCon.addParameterShell("COVERAGE", coverages);
                paramCon.addParameterShell("VERSION", version);

                OperationResult opResult_DescribeCoverage = doOperation(new Operation("DescribeCoverage", dcps),
                                                                        paramCon);
                CoverageDescription cd = null;
                try {
                    cd = (CoverageDescription) u.unmarshal(opResult_DescribeCoverage.getIncomingResultAsAutoCloseStream());
                }
                catch (Exception exc) {
                    // --> probably DescribeCoverage-operation not supported
                    throwCollect.addThrowable(new OXFException("Exception while trying to parse DescribeCoverage-document.",
                                                               exc));
                    LOGGER.error("Exception while trying to parse DescribeCoverage-document.", exc);
                }

                if (cd != null) {
                    // use the richer DescribeCoverage information to re-init the Contents section:
                    contentsSection = wcsCapsMapper.mapContents(cd, throwCollect);
                }

                operationsMetadataSection = wcsCapsMapper.mapOperationsMetadata(wc, contentsSection);

            }
            catch (Exception exc) {
            	exc.printStackTrace();
                // VERSION 1.1.1:

//                LOGGER.info("Not convertible to version 1.0.0 --> trying 1.1.1");

                try {
                    CapabilitiesDocument capDoc = CapabilitiesDocument.Factory.parse(opResult_GetCapabilities.getIncomingResultAsAutoCloseStream());
                    LOGGER.info("Capabilities succesfully read");
                    version = capDoc.getCapabilities().getVersion();

                    serviceIdentificationSection = wcsCapsMapper.mapServiceIdentification(capDoc);
                    serviceProviderSection = wcsCapsMapper.mapServiceProvider(capDoc);
                    contentsSection = wcsCapsMapper.mapContents(capDoc);

                    //
                    // try to request DescribeCoverage information and integrate it into the
                    // ServiceDescriptor:
                    //
                    String[] coverages = new String[contentsSection.getDataIdentificationCount()];
                    for (int i = 0; i < contentsSection.getDataIdentificationCount(); i++) {
                        Dataset coverageSummary = contentsSection.getDataIdentification(i);
                        coverages[i] = coverageSummary.getIdentifier();
                    }


                    // OWS-5 requires ONE DescribeCoverage request per Coverage :-(
                    CoverageDescriptionType[] coverageDescArray = new CoverageDescriptionType[coverages.length];
                    for (int i = 0; i < coverages.length; i++) {

                        ParameterContainer paramCon = new ParameterContainer();
                        paramCon.addParameterShell("VERSION", version);
                        paramCon.addParameterShell("COVERAGE", coverages[i]);
                        OperationResult opResult_DescribeCoverage = doOperation(new Operation("DescribeCoverage", dcps),
                                                                                paramCon);

                        CoverageDescriptionsDocument desCovDoc;
						try {
							desCovDoc = CoverageDescriptionsDocument.Factory.parse(opResult_DescribeCoverage.getIncomingResultAsAutoCloseStream());
						} catch (XmlException e) {
							String error = new String(opResult_DescribeCoverage.getIncomingResult());
							LOGGER.info(error);
//							throw new OWSException(new String[]{error},error,null);
							throw new OXFException(error,new Throwable("Access Denied"));
						}

                        // CoverageDescriptionsDocument contains only ONE CoverageDescription
                        CoverageDescriptionType covDesType = desCovDoc.getCoverageDescriptions().getCoverageDescriptionArray(0);

                        coverageDescArray[i] = covDesType;

                        LOGGER.info("DescribeCoverage successfully read for coverage: " + coverages[i]);
                    }

                    try {
                        contentsSection = wcsCapsMapper.mapContents(coverageDescArray);
                        operationsMetadataSection = wcsCapsMapper.mapOperationsMetadata(capDoc, contentsSection);
                    }
                    catch (Exception ex) {
                        LOGGER.warn("DescribeCoverage was not succesfull or could not be parsed");
                        ex.printStackTrace();
                        operationsMetadataSection = new OperationsMetadata(new Operation[0]);
                    }
                }catch (OXFException ex) {
                	LOGGER.error(ex.getMessage());
//                    LOGGER.error("Could not read WCS Capabilities as 1.0.0 or 1.1.1");
//                    ex.printStackTrace();
                    throw ex;
                }
                catch (Exception ex) {
                    LOGGER.error("Could not read WCS Capabilities as 1.0.0 or 1.1.1");
                    ex.printStackTrace();
                    throw new OXFException();
                }
            }

            if ( ! ( (version.equals("1.0.0") || version.equals("1.0") || version.equals("1") || version.equals("1.1.1")))) {
                throw new OXFException("This WCSAdapter only supports access to Web Coverage Services of the version 1.0.0 and 1.1.1.");
            }

            sd = new ServiceDescriptor(version, serviceIdentificationSection, serviceProviderSection, operationsMetadataSection, contentsSection);
        }catch (OXFException ex) {
        	LOGGER.error(ex.getMessage());
//          LOGGER.error("Could not read WCS Capabilities as 1.0.0 or 1.1.1");
//          ex.printStackTrace();
          throw ex;
        }
        catch (Exception e) {
            throw new OXFException(e);
        }

        if ( !throwCollect.isEmpty()) {
            LOGGER.error("Errors occured during initialisation process.", throwCollect);
        }

        return sd;
    }

    /**
     * This method can perform the following operations of a Web Coverage Service 1.0.0:
     * <li>GetCapabilities (GET-Request) </li>
     * <li>DescribeCoverage (GET-Request) </li>
     * <li>GetCoverage (GET-Request) </li>
     * <br>
     * Attention: the URL which will be used to connect the service will be taken from the DCP specification
     * of the operation (be sure that the capabilities document of the service is correct and up to date in
     * this point).
     */
    public OperationResult doOperation(Operation operation, ParameterContainer parameterContainer) throws ExceptionReport,
            OXFException {

        OperationResult result = null;

        String requestString = "";

        // ----- Operation: GetCapabilities
        if (operation.getName().equalsIgnoreCase("GetCapabilities")) {

            try {
                requestString = new WCSRequestBuilder().buildGetCapabilitiesRequest(operation, parameterContainer);

                URL requestURL = new URL(requestString);
                InputStream res = requestURL.openStream();

                result = new OperationResult(res, parameterContainer, requestString);

            }
            catch (MalformedURLException e) {
                throw new OXFException(e);
            }
            catch (IOException e) {
                throw new OXFException(e);
            }
        }

        // ----- Operation: DescribeCoverage
        else if (operation.getName().equalsIgnoreCase("DescribeCoverage")) {
            try {
                requestString = new WCSRequestBuilder().buildDescribeCoverageRequest(operation, parameterContainer);
                URL requestURL = new URL(requestString);
                InputStream res = requestURL.openStream();

                result = new OperationResult(res, parameterContainer, requestString);

            }
            catch (MalformedURLException e) {
                throw new OXFException(e);
            }
            catch (IOException e) {
                throw new OXFException(e);
            }
        }

        // ----- Operation: GetCoverage
        if (operation.getName().equalsIgnoreCase("GetCoverage")) {

            try {
                requestString = new WCSRequestBuilder().buildGetCoverageRequest(operation, parameterContainer);

                URL requestURL = new URL(requestString);
                InputStream res = requestURL.openStream();

                result = new OperationResult(res, parameterContainer, requestString);

            }
            catch (MalformedURLException e) {
                throw new OXFException(e);
            }
            catch (IOException e) {
                throw new OXFException(e);
            }
        }

        // ->
        LOGGER.info("send request: " + requestString);

        return result;
    }

    /**
     * @return a description of the implemented OGC Web Service Adapter.
     */
    public String getDescription() {
        return "This ServiceAdapter can be used to connect to Web Coverage Services. ...";
    }

    /**
     * @return the type of the service which is connectable by this ServiceAdapter
     */
    public String getServiceType() {
        return "OGC:WCS";
    }

    /**
     * @return the versions of the services which are connectable by this ServiceAdapter. Should look like
     *         e.g. {"1.1.0","1.2.0"}.
     */
    public String[] getSupportedVersions() {
        return new String[] {"1.0.0", "1.1.0", "1.1.1"};
    }

    /**
     * @return the name of the serice operation which returns the data to be added to a map-view as a layer.
     *         In the case of the WCS it is 'GetCoverage'.
     */
    public String getResourceOperationName() {
        return "GetCoverage";
    }
}