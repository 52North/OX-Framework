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

package org.n52.oxf.csw.adapter;

import net.opengis.cat.csw.x202.DescribeRecordDocument;
import net.opengis.cat.csw.x202.DescribeRecordType;

import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.xmlbeans.tools.XmlUtil;

/**
 * contains attributes and methods to encode operation requests as Strings in xml-format
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class CSWRequestBuilder {

    public static final String GET_CAPABILITIES_REQUEST_PARAMETER = "request";
    public static final String GET_CAPABILITIES_SERVICE_PARAMETER = "service";
    public static final String GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER = "updateSequence";
    public static final String GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER = "AcceptVersion";
    public static final String GET_CAPABILITIES_ACCEPT_FORMATS_PARAMETER = "acceptFormats";
    public static final String GET_CAPABILITIES_SECTIONS_PARAMETER = "Sections";

    public static final String DESCRIBE_RECORD_MAX_RECORDS = "maxRecords";
    public static final String DESCRIBE_RECORD_START_POSITION = "startPosition";
    public static final String DESCRIBE_RECORD_REQUEST_PARAMETER = "request";
    public static final String DESCRIBE_RECORD_SERVICE_PARAMETER = "service";
    public static final String DESCRIBE_RECORD_VERSION_PARAMETER = "version";
    public static final String DESCRIBE_RECORD_TYPE_NAME_PARAMETER = "typeName";
    public static final String DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER = "outputFormat";
    public static final String DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER = "schemaLanguage";
    public static final String DESCRIBE_RECORD_NAME_SPACE_PARAMETER = "nameSpace";

    public static final String GET_RECORDS_SERVICE_PARAMETER = "service";
    public static final String GET_RECORDS_VERSION_PARAMETER = "version";
    public static final String GET_RECORDS_TYPE_NAME_PARAMETER = "TypeName";
    public static final String GET_RECORDS_OUTPUT_SCHEMA_FORMAT = "outputSchema";
    public static final String GET_RECORDS_RESULT_TYPE_FORMAT = "resultType";
    public static final String GET_RECORDS_ELEMENT_SET_NAME_FORMAT = "ElementSetName";
    public static final String GET_RECORDS_CONSTRAINTLANGUAGE_FORMAT = "CONSTRAINTLANGUAGE";
    public static final String GET_RECORDS_LITERAL = "Literal";
    public static final String GET_RECORDS_PROPERTY_NAME = "PropertyName";
    public static final String GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER = "GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER";
    
    public static final String GET_RECORD_BY_ID_REQUEST = "request";
    public static final String GET_RECORD_BY_ID_SERVICE = "service";
    public static final String GET_RECORD_BY_ID_VERSION = "version";
    public static final String GET_RECORD_BY_ID_ID = "ID";
    public static final String GET_RECORD_BY_ID_ELEMENT_SET_NAME = "ElementSetName";
    public static final String GET_RECORD_BY_ID_OUTPUT_FORMAT = "outputFormat";
    public static final String GET_RECORD_BY_ID_OUTPUT_SCHEMA = "outputSchema";

    /**
     * builds the GetCapabilities-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * 
     * <br>
     * <br>
     * The following are optional: The following are optional:
     * <li>updateSequence</li>
     * <li>acceptVersions</li>
     * <li>sections</li>
     * <li>acceptFormats</li>
     * 
     * @param parameters
     *        the parameters of the request
     * 
     * @return CapabilitiesRequest in xml-Format as String
     */
    public String buildGetCapabilitiesRequest(ParameterContainer parameters) {

        String queryString = "";

        //
        // set required elements:
        //
        queryString += GET_CAPABILITIES_REQUEST_PARAMETER + "=" + "GetCapabilities" + "&";

        // Parameter "service":
        String serviceParam = (String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SERVICE_PARAMETER).getSpecifiedValue();
        queryString += GET_CAPABILITIES_SERVICE_PARAMETER + "=" + serviceParam + "&";

        // Parameter "AcceptVersions":
        String versionParam = (String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER).getSpecifiedValue();
        queryString += GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER + "=" + versionParam + "&";

        String formatsParam = (String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_ACCEPT_FORMATS_PARAMETER).getSpecifiedValue();
        queryString += GET_CAPABILITIES_ACCEPT_FORMATS_PARAMETER + "=" + formatsParam + "&";

        // Parameter "sections":
        ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {

            queryString += GET_CAPABILITIES_SECTIONS_PARAMETER + "=";

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                String[] selectedSections = (String[]) sectionParamShell.getSpecifiedValueArray();
                for (int i = 0; i < selectedSections.length; i++) {
                    queryString += selectedSections[i] + ",";
                }
            }
            else if (sectionParamShell.hasSingleSpecifiedValue()) {
                queryString += sectionParamShell.getSpecifiedValue();
            }
            queryString += "&";
        }

        //
        // set optional elements:
        //

        // Parameter "updateSequence":
        if (parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER) != null) {
            String upSeqParam = (String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER).getSpecifiedValue();
            queryString += GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER + "=" + upSeqParam + "&";
        }

        return queryString;
    }

    public String buildDescribeRecordRequest(ParameterContainer parameters) {
        String queryString = "";

        //
        // set required elements:
        //
        queryString += DESCRIBE_RECORD_REQUEST_PARAMETER + "=" + CSWAdapter.DESCRIBE_RECORD + "&";

        String serviceParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_SERVICE_PARAMETER).getSpecifiedValue();
        queryString += DESCRIBE_RECORD_SERVICE_PARAMETER + "=" + serviceParam + "&";

        String versionParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_VERSION_PARAMETER).getSpecifiedValue();
        queryString += DESCRIBE_RECORD_VERSION_PARAMETER + "=" + versionParam + "&";

        String formatsParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER).getSpecifiedValue();
        queryString += DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER + "=" + formatsParam + "&";

        String schemaLangParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER).getSpecifiedValue();
        queryString += DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER + "=" + schemaLangParam + "&";
//        
//        String typeNameParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_TYPE_NAME_PARAMETER).getSpecifiedValue();
//        queryString += DESCRIBE_RECORD_TYPE_NAME_PARAMETER + "=" + typeNameParam + "&";
        
        String nameSpaceParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_NAME_SPACE_PARAMETER).getSpecifiedValue();
        queryString += DESCRIBE_RECORD_NAME_SPACE_PARAMETER + "=" + nameSpaceParam + "&";
        
        return queryString;
    }
    
    public String buildDescribeRecordRequestPost(ParameterContainer parameters) {
       
        DescribeRecordDocument describeRecordDocument = DescribeRecordDocument.Factory.newInstance();
        
        DescribeRecordType describeRecordType =  describeRecordDocument.addNewDescribeRecord();

        String versionParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_VERSION_PARAMETER).getSpecifiedValue();
        describeRecordType.setVersion(versionParam);
        
        String formatsParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER).getSpecifiedValue();
        describeRecordType.setOutputFormat(formatsParam);
        
        String schemaLangParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_SCHEMA_LANGUAGE_PARAMETER).getSpecifiedValue();
        describeRecordType.setSchemaLanguage(schemaLangParam);
        
        String serviceParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_SERVICE_PARAMETER).getSpecifiedValue();
        describeRecordType.setService(serviceParam);
        
        return describeRecordDocument.xmlText(XmlUtil.PRETTYPRINT);
    }
    
    // TODO: use adjust to XML Beans
    public String buildGetRecordsRequest(ParameterContainer parameters) {
    	String literal = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORDS_LITERAL).getSpecifiedValue();
    	String propertyName = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORDS_PROPERTY_NAME).getSpecifiedValue();
    	String maxRecords = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_MAX_RECORDS).getSpecifiedValue();
    	String startPosition = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_START_POSITION).getSpecifiedValue();
    	String versionParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_VERSION_PARAMETER).getSpecifiedValue();   
        String formatsParam = (String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_RECORD_OUTPUT_FORMAT_PARAMETER).getSpecifiedValue();
        String service2Search4Param = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER).getSpecifiedValue();
        
    	String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    	request +=       "<csw:GetRecords \n";
    	request += 		 "xmlns=\"http://www.opengis.net/cat/csw\" \n";
    	request += 		 "xmlns:csw=\"http://www.opengis.net/cat/csw\" \n";
    	request += 		 "xmlns:ogc=\"http://www.opengis.net/ogc\" \n";
    	request += 		 "xmlns:gml=\"http://www.opengis.net/gml\" \n";
    	request += 		 "xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" \n";
    	request += 		 "version=\""+versionParam+"\" \n";
    	request += 		 "outputFormat=\""+formatsParam+"\" \n";
    	request += 		 "outputSchema=\"http://www.opengis.net/cat/csw\" \n";
    	request += 		 "maxRecords=\""+maxRecords+"\" \n";
    	request += 		 "startPosition=\""+startPosition+"\"> \n";
    	request += 		 "<csw:Query typeNames=\"rim:Service\"> \n";
    	request += 		  "<csw:ElementName>/rim:Service/</csw:ElementName> \n";
    	request += 		  "<csw:Constraint version=\"1.1.0\"> \n";
    	request += 		   "<ogc:Filter> \n";
    	
    	if ( (propertyName.trim().equals("")) || (literal.trim().equals(""))) {
    	    request +=        "<ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\"> \n";
            request +=            "<ogc:PropertyName>/rim:Service/rim:Slot/rim:ValueList/rim:Value</ogc:PropertyName>\n";
            request +=            "<ogc:Literal>*" + service2Search4Param + "*</ogc:Literal>\n";
            request +=        "</ogc:PropertyIsLike>\n";
    	}
    	else {
        	request += 		  "<ogc:And> \n";
        	request += 			  "<ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\"> \n";
        	request += 			    "<ogc:PropertyName>"+propertyName+"</ogc:PropertyName> \n";
        	request += 			    "<ogc:Literal>"+literal+"</ogc:Literal> \n";
        	request += 		      "</ogc:PropertyIsLike> \n";
        	request += 		      "<ogc:PropertyIsLike wildCard=\"*\" singleChar=\"#\" escapeChar=\"\\\"> \n";
        	request += 		        "<ogc:PropertyName>/rim:Service/rim:Slot/rim:ValueList/rim:Value</ogc:PropertyName>\n";
        	request += 		        "<ogc:Literal>*" + service2Search4Param + "*</ogc:Literal>\n";
        	request += 	          "</ogc:PropertyIsLike>\n";
        	request += 	      "</ogc:And> \n";
    	}
    	
    	request += 		   "</ogc:Filter> \n";
    	request += 		  "</csw:Constraint> \n";
    	request += 		 "</csw:Query> \n";
    	request +=     	"</csw:GetRecords> \n";
    	
    	return request;
    }
    
    public String buildGetRecordByIdRequest(ParameterContainer parameters) {
        String queryString = "";

        //
        // set required elements:
        //
        queryString += GET_RECORD_BY_ID_REQUEST + "=" + CSWAdapter.GET_RECORD_BY_ID + "&";

        String serviceParam = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_SERVICE).getSpecifiedValue();
        queryString += GET_RECORD_BY_ID_SERVICE + "=" + serviceParam + "&";

        String versionParam = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_VERSION).getSpecifiedValue();
        queryString += GET_RECORD_BY_ID_VERSION + "=" + versionParam + "&";

        String idParam = (String) parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_ID).getSpecifiedValue();
        queryString += GET_RECORD_BY_ID_ID + "=" + idParam + "&";

        //
        // set optional elements:
        //
        ParameterShell elementSetNameParam = parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_ELEMENT_SET_NAME);
        if (elementSetNameParam != null) {
            String elementSetName = (String) elementSetNameParam.getSpecifiedValue();
            queryString += GET_RECORD_BY_ID_ELEMENT_SET_NAME + "=" + elementSetName + "&";
        }

        ParameterShell outputFormatParam = parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_OUTPUT_FORMAT);
        if (outputFormatParam != null) {
            String outputFormat = (String) outputFormatParam.getSpecifiedValue();
            queryString += GET_RECORD_BY_ID_OUTPUT_FORMAT + "=" + outputFormat + "&";
        }
        
        ParameterShell outputSchemaParam = parameters.getParameterShellWithServiceSidedName(GET_RECORD_BY_ID_OUTPUT_SCHEMA);
        if (outputSchemaParam != null) {
            String outputSchema = (String) outputSchemaParam.getSpecifiedValue();
            queryString += GET_RECORD_BY_ID_OUTPUT_SCHEMA + "=" + outputSchema + "&";
        }
        
        return queryString;
    }
}