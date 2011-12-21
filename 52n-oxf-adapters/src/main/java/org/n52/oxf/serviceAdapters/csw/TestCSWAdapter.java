/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 22.11.2007
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.csw;

import org.apache.log4j.Logger;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.ExceptionReport;
import org.n52.oxf.owsCommon.capabilities.Operation;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.util.LoggingHandler;

/**
 * This class demonstrates how to use the CSWAdapter. You might use it as an example for your own code.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TestCSWAdapter {

    private static Logger LOGGER = LoggingHandler.getLogger(TestCSWAdapter.class);

    // private final String url = "http://laits.gmu.edu:8099/csw/CSW_Service";
    //private final String url = "http://uddi.csiss.gmu.edu:1090/GEOSSCSW202/discovery";
    //private final String url = "http://laits.gmu.edu:8099/LAITSCSW2/discovery"; //only working with post request
    //private final String url = "http://geobrain.laits.gmu.edu:8099/LAITSCSW2/discovery";
    private static final String url = "http://geossregistries.info:1090/GEOSSCSW202/discovery";
//    private final String url = "http://geobrain.laits.gmu.edu:8099/LAITSCSF2/discovery";
    
    // not working:
    // private final String url = "http://laits.gmu.edu:8099/LAITSCSW2/discovery";
    // private final String url = "http://laits.gmu.edu:8099/LAITSCSW/discovery";

    public static void main(String[] args) throws OXFException, ExceptionReport {
    	new TestCSWAdapter().testDescribeRecord();
//        new TestCSWAdapter().testGetRecords();
//    	String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//"<csw:GetRecords"+
//" xmlns=\"http://www.opengis.net/cat/csw\""+ 
//" xmlns:csw=\"http://www.opengis.net/cat/csw\""+ 
//" xmlns:ogc=\"http://www.opengis.net/ogc\" "+
//" xmlns:gml=\"http://www.opengis.net/gml\" "+
//" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\""+
//" version=\"2.0.2\" "+
//" outputFormat=\"text/xml; charset=UTF-8\""+ 
//" outputSchema=\"http://www.opengis.net/cat/csw\""+ 
//" maxRecords=\"1\""+
//" startPosition=\"1\">"+
//" <csw:Query typeNames=\"rim:Service\">"+
//"  <csw:ElementName>/rim:Service/</csw:ElementName>"+
//"  <csw:Constraint version=\"1.1.0\">"+
//"   <ogc:Filter>"+
//"    <ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\">"+
//"     <ogc:PropertyName>/rim:Service/rim:Name/rim:LocalizedString/@value</ogc:PropertyName>"+
//"     <ogc:Literal>*Geospatial One Stop*</ogc:Literal>"+
//"    </ogc:PropertyIsLike>"+
//"   </ogc:Filter>"+
//"  </csw:Constraint>"+
//" </csw:Query>"+
//"</csw:GetRecords>";
//    	try {
//    		BufferedReader bs = new BufferedReader(new InputStreamReader(IOHelper.sendPostMessage(url, request)));
//    		do{
//    			System.out.println(bs.readLine());
//    		}while(bs.ready());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }

    private void testGetCapabilities() throws ExceptionReport, OXFException {

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

        // ServiceDescriptor desc = adapter.initService(opResult);

        // LOGGER.info(desc.getServiceIdentification().getTitle());

    }

    private void testDescribeRecord() throws OXFException, ExceptionReport {
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

    private void testGetRecords() throws OXFException, ExceptionReport {
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
         paramCon.addParameterShell(CSWRequestBuilder.GET_RECORDS_TYPE_NAME_PARAMETER,typeNames);
         paramCon.addParameterShell(CSWRequestBuilder.DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER,
                                    "XMLSCHEMA");
       
         OperationResult opResult = adapter.doOperation(new Operation("GetRecords",
                                                                      url + "?",
                                                                      url), paramCon);

         LOGGER.info(new String(opResult.getIncomingResult()));
    }
}