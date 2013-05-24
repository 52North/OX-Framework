/**
 * ﻿Copyright (C) 2013
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

package org.n52.oxf.csw.adapter;

import junit.framework.TestCase;

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
public class CSWAdapterTest extends TestCase {
    
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
    public void testGetRecords() throws OXFException, ExceptionReport {
    	 try {
    	     CSWAdapter adapter = new CSWAdapter();
    	     
             ParameterContainer paramCon = new ParameterContainer();
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_OUTPUT_FORMAT_PARAMETER, "application/xml");
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_OUTPUT_SCHEMA_FORMAT, "http://www.opengis.net/cat/csw/2.0.2");
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_ELEMENT_SET_NAME_FORMAT, "full");
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_MAX_RECORDS, 5);
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_START_POSITION, 1);
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_RESULT_TYPE, "results");
             
             paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_QUERY_TYPE_NAMES_PARAMETER, "csw:Record");
           
             OperationResult opResult = adapter.doOperation(new Operation(CSWAdapter.GET_RECORDS,
                                                                          url + "?",
                                                                          url), paramCon);
    
             LOGGER.info(new String(opResult.getIncomingResult()));
    	} catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            e.printStackTrace();
            fail();
        }
    }
    

    
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
}