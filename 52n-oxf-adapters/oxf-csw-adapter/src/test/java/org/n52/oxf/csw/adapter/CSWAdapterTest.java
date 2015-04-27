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
package org.n52.oxf.csw.adapter;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.capabilities.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates how to use the CSWAdapter. You might use it as an example for your own code.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */

/*
 * These tests require network access and rely on external services to be available.
 * This is why this test is set to be ignored by JUnit runs (@Ignore annotation).
 * 
 * To enable this test it is necessary to uncomment the @Ignore annotation bolow.
 */
@Ignore // uncomment if you want to run tests
public class CSWAdapterTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CSWAdapterTest.class);
    
    
    //private final String url = "http://laits.gmu.edu:8099/csw/CSW_Service";
    //private final String url = "http://uddi.csiss.gmu.edu:1090/GEOSSCSW202/discovery";
    //private final String url = "http://laits.gmu.edu:8099/LAITSCSW2/discovery"; //only working with post request
    //private final String url = "http://geobrain.laits.gmu.edu:8099/LAITSCSW2/discovery";
    //private final String url = "http://geobrain.laits.gmu.edu:8099/LAITSCSF2/discovery";
    //private static final String url = "http://geossregistries.info:1090/GEOSSCSW202/discovery";
    private static String url = "http://catalog.glues.geo.tu-dresden.de:8080/soapServices/CSWStartup";

    @Test
    public void testGetCapabilities() throws ExceptionReport, OXFException {

        try {
            CSWAdapter adapter = new CSWAdapter();
    
            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(CSWRequestBuilder.GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER,
                                       CSWAdapter.SUPPORTED_VERSIONS[0]);
            paramCon.addParameterShell(CSWRequestBuilder.GET_CAPABILITIES_SERVICE_PARAMETER,
                                       CSWAdapter.SERVICE_TYPE);
            paramCon.addParameterShell(CSWRequestBuilder.GET_CAPABILITIES_SECTIONS_PARAMETER, "ALL");
            paramCon.addParameterShell(CSWRequestBuilder.GET_CAPABILITIES_ACCEPT_FORMATS_PARAMETER,
                                       "application/xml");
    
            OperationResult opResult = adapter.doOperation(new Operation("GetCapabilities",
                                                                         url + "?",
                                                                         url), paramCon);
    
           LOGGER.info(new String(opResult.getIncomingResult()));
            
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            fail();
        }
    }

    @Test
    public void testGetRecordById() throws OXFException, ExceptionReport {
        try {
            CSWAdapter adapter = new CSWAdapter();
    
            String recordID = "glues:pik:metadata:dataset:noco2-echo-g-sresa1-annualcropyieldincreases";
            String elementSetName = "full";
            String outputSchema= "http://www.isotc211.org/2005/gmd";
            
            
            ParameterContainer paramCon = new ParameterContainer();
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_REQUEST,
                                       CSWAdapter.GET_RECORD_BY_ID);
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_VERSION,
                                       CSWAdapter.SUPPORTED_VERSIONS[0]);
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_SERVICE,
                                       CSWAdapter.SERVICE_TYPE);
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_ID,
                                       recordID);
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_ELEMENT_SET_NAME,
                                       elementSetName);
            paramCon.addParameterShell(CSWRequestBuilder.GET_RECORD_BY_ID_OUTPUT_SCHEMA,
                                       outputSchema);
            
            OperationResult opResult = adapter.doOperation(new Operation(CSWAdapter.GET_RECORD_BY_ID,
                    url + "?",
                    url), paramCon);
    
            LOGGER.info(new String(opResult.getIncomingResult()));
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testDescribeRecord() throws OXFException, ExceptionReport {
        CSWAdapter adapter = new CSWAdapter();

        ParameterContainer paramCon = new ParameterContainer();
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_VERSION_PARAMETER,
                                   CSWAdapter.SUPPORTED_VERSIONS[0]);
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SERVICE_PARAMETER,
                                   CSWAdapter.SERVICE_TYPE);
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER,
                                   "application/xml");
//        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_TYPE_NAME_PARAMETER,
//                                   "csw:Record,rim:Service,rim:Organization");
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_TYPE_NAME_PARAMETER,
       "laits:DataGranule,laits:WCSCoverage,laits:WMSLayer, laits:SV_OperationMetadata,laits:SV_Parameter");
        
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER,
                                   "XMLSCHEMA");
        paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_NAME_SPACE_PARAMETER,
        							"xmlns(rim=urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0)");
                                    //"xmlns(rim=urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5),xmlns(csw=http://www.opengis.net/cat/csw)");
//    "xmlns(rim=urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5),xmlns(csw=http://www.opengis.net/cat/csw)");
        System.out.println("http://geossregistries.info:1090/GEOSSCSW202/discovery?Request=DescribeRecord&Service=CSW&Version=2.0.2&NAMESPACE=xmlns(rim=urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0)");
        OperationResult opResult = adapter.doOperation(new Operation("DescribeRecord",
                                                                     url + "?",
                                                                     url), paramCon);

        LOGGER.info(new String(opResult.getIncomingResult()));
    }

    @Test
    public void testGetRecords() throws OXFException, ExceptionReport {
    	 CSWAdapter adapter = new CSWAdapter();

         ParameterContainer paramCon = new ParameterContainer();
         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_VERSION_PARAMETER,
                                    CSWAdapter.SUPPORTED_VERSIONS[0]);
         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SERVICE_PARAMETER,
                                    CSWAdapter.SERVICE_TYPE);
         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER,
                                    "application/xml");
//         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_TYPE_NAME_PARAMETER,
//                                    "csw:Record,rim:Service,rim:Organization");

         
         paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_OUTPUT_SCHEMA_FORMAT,"http://www.opengis.net/cat/csw");
         String typeNames[] = {"Service","Organization"};
         
         
         // FIXME this parameter is missing
         //paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_TYPE_NAME_PARAMETER,typeNames);
         
         
         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER,
                                    "XMLSCHEMA");
       
         OperationResult opResult = adapter.doOperation(new Operation("GetRecords",
                                                                      url + "?",
                                                                      url), paramCon);

         LOGGER.info(new String(opResult.getIncomingResult()));
    }
}