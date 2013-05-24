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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.cat.csw.x202.AbstractQueryType;
import net.opengis.cat.csw.x202.DescribeRecordDocument;
import net.opengis.cat.csw.x202.DescribeRecordType;
import net.opengis.cat.csw.x202.GetRecordsDocument;
import net.opengis.cat.csw.x202.GetRecordsType;
import net.opengis.cat.csw.x202.QueryDocument;
import net.opengis.cat.csw.x202.QueryType;
import net.opengis.cat.csw.x202.ResultType;

import org.apache.xmlbeans.XmlOptions;
import org.n52.oxf.OXFException;
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

    public static final String GET_RECORDS_QUERY_TYPE_NAMES_PARAMETER = "typeNames";
    public static final String GET_RECORDS_OUTPUT_SCHEMA_FORMAT = "outputSchema";
    public static final String GET_RECORDS_RESULT_TYPE = "resultType";
    public static final String GET_RECORDS_ELEMENT_SET_NAME_FORMAT = "ElementSetName";
    public static final String GET_RECORDS_CONSTRAINTLANGUAGE_FORMAT = "CONSTRAINTLANGUAGE";
    public static final String GET_RECORDS_LITERAL = "Literal";
    public static final String GET_RECORDS_PROPERTY_NAME = "PropertyName";
    public static final String GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER = "GET_RECORDS_SERVICE_TYPE_2SEARCH4_PARAMETER";
    public static final String GET_RECORDS_MAX_RECORDS = "maxRecords";
    public static final String GET_RECORDS_START_POSITION = "startPosition";
    public static final String GET_RECORDS_OUTPUT_FORMAT_PARAMETER = "outputFormat";
    
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
    
    public String buildGetRecordsRequest(ParameterContainer parameters) throws OXFException {
        
        GetRecordsDocument xb_getRecordsDocument = GetRecordsDocument.Factory.newInstance();
        GetRecordsType xb_getRecords = xb_getRecordsDocument.addNewGetRecords();
        
        xb_getRecords.setService(CSWAdapter.SERVICE_TYPE);
        xb_getRecords.setVersion(CSWAdapter.SUPPORTED_VERSIONS[0]);
        
        
        ParameterShell maxRecords = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_MAX_RECORDS);
        if (maxRecords != null) {
            xb_getRecords.setMaxRecords( BigInteger.valueOf((Integer) maxRecords.getSpecifiedValue()));
        }
        
        ParameterShell startPosition = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_START_POSITION);
        if (startPosition != null) {
            xb_getRecords.setStartPosition( BigInteger.valueOf((Integer) startPosition.getSpecifiedValue()));
        }
        
        ParameterShell formatsParam = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_OUTPUT_FORMAT_PARAMETER);
        if (formatsParam != null) {
            xb_getRecords.setOutputFormat( (String) formatsParam.getSpecifiedValue());
        }
        
        ParameterShell outputSchemaParam = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_OUTPUT_SCHEMA_FORMAT);
        if (outputSchemaParam != null) {
            xb_getRecords.setOutputSchema( (String) outputSchemaParam.getSpecifiedValue());
        }
        
        ParameterShell resultTypeParam = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_RESULT_TYPE);
        if (resultTypeParam != null) {
            
            String resultTypeString = (String) resultTypeParam.getSpecifiedValue();
            if (resultTypeString.equals("results")) {            
                xb_getRecords.setResultType( ResultType.RESULTS );
            }
            else {
                throw new OXFException("Specified value '" + resultTypeString + "' for parameter '" + GET_RECORDS_RESULT_TYPE + "' is not yet supported.");
            }
        }
        
        //
        // constructing a Query:
        
        QueryType xb_query = QueryType.Factory.newInstance();
        
        ParameterShell typeNamesParam = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_QUERY_TYPE_NAMES_PARAMETER);
        if (typeNamesParam != null) {
            
            List typeNameList = new ArrayList();
            
            if (((String) typeNamesParam.getSpecifiedValue()).equals("csw:Record")) {
                typeNameList.add(new QName(CSWAdapter.NAMESPACE, "Record"));
            }
            else {
                throw new OXFException("typeNames not yet supported.");
            }
            
            xb_query.setTypeNames(typeNameList);
        }
        
        ParameterShell elementSetNameParam = parameters.getParameterShellWithServiceSidedName(GET_RECORDS_ELEMENT_SET_NAME_FORMAT);
        if (elementSetNameParam != null) {
            if (xb_query == null) {
                throw new OXFException("Query not yet initialized.");
            }
            xb_query.addNewElementSetName().setStringValue((String) elementSetNameParam.getSpecifiedValue());
        }
        
        // adding the Query and setting correct type:
        
        AbstractQueryType abstractQuery = xb_getRecords.addNewAbstractQuery();
        abstractQuery.set(xb_query);
        XmlUtil.qualifySubstitutionGroup(xb_getRecords.getAbstractQuery(),
                QueryDocument.type.getDocumentElementName(),
                QueryType.type);
        
        Map suggestedPrefixes = new HashMap();
        suggestedPrefixes.put(CSWAdapter.NAMESPACE, "csw");
        
        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveSuggestedPrefixes(suggestedPrefixes);
        xmlOptions.setSavePrettyPrint();
        
        return xb_getRecordsDocument.xmlText(xmlOptions);
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