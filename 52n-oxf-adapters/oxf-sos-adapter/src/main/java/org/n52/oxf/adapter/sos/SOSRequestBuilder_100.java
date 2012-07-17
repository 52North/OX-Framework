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

package org.n52.oxf.adapter.sos;

import java.util.Collection;

import javax.xml.namespace.QName;

import net.opengis.gml.AbstractTimeObjectType;
import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.MeasureType;
import net.opengis.gml.PointType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePeriodType;
import net.opengis.gml.TimePositionType;
import net.opengis.ogc.BinaryTemporalOpType;
import net.opengis.om.x10.CategoryObservationType;
import net.opengis.om.x10.MeasurementType;
import net.opengis.om.x10.ObservationType;
import net.opengis.om.x10.ProcessPropertyType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.sensorML.x101.InputsDocument.Inputs;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.OutputsDocument.Outputs;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SystemDocument;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sos.x10.DescribeSensorDocument;
import net.opengis.sos.x10.DescribeSensorDocument.DescribeSensor;
import net.opengis.sos.x10.GetCapabilitiesDocument;
import net.opengis.sos.x10.GetCapabilitiesDocument.GetCapabilities;
import net.opengis.sos.x10.GetFeatureOfInterestDocument;
import net.opengis.sos.x10.GetFeatureOfInterestDocument.GetFeatureOfInterest;
import net.opengis.sos.x10.GetObservationByIdDocument;
import net.opengis.sos.x10.GetObservationByIdDocument.GetObservationById;
import net.opengis.sos.x10.GetObservationDocument;
import net.opengis.sos.x10.GetObservationDocument.GetObservation;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.EventTime;
import net.opengis.sos.x10.GetObservationDocument.GetObservation.Result;
import net.opengis.sos.x10.InsertObservationDocument;
import net.opengis.sos.x10.InsertObservationDocument.InsertObservation;
import net.opengis.sos.x10.ObservationTemplateDocument.ObservationTemplate;
import net.opengis.sos.x10.RegisterSensorDocument;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor;
import net.opengis.sos.x10.RegisterSensorDocument.RegisterSensor.SensorDescription;
import net.opengis.sos.x10.ResponseModeType;
import net.opengis.swe.x101.PhenomenonPropertyType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.QuantityDocument.Quantity;
import net.opengis.swe.x101.TimeObjectPropertyType;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;
import net.opengis.swe.x101.VectorType.Coordinate;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.owsCommon.OwsExceptionReport.ExceptionCode;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.xml.XMLConstants;
import org.n52.oxf.xmlbeans.parser.GMLAbstractFeatureCase;
import org.n52.oxf.xmlbeans.parser.OfferingInSMLOutputsCase;
import org.n52.oxf.xmlbeans.parser.SASamplingPointCase;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.tools.XMLBeansTools;

/**
 * Contains attributes and methods to encode SOSOperationRequests as String in xml-format
 * 
 * @author <a href="mailto:broering@52north.org">Arne Br&ouml;ring</a>
 * @author <a href="mailto:ehjuerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class SOSRequestBuilder_100 implements ISOSRequestBuilder {
    
	/**
     * Builds the GetCapabilities-Request. <br>
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

        GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();

        GetCapabilities getCap = getCapDoc.addNewGetCapabilities();

        //
        // set required elements:
        //  

        getCap.setService((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SERVICE_PARAMETER).getSpecifiedValue());

        //
        // set optional elements:
        //

        // Parameter "updateSequence":
        if (parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER) != null) {
            getCap.setUpdateSequence((String) parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_UPDATE_SEQUENCE_PARAMETER).getSpecifiedValue());
        }

        // Parameter "AcceptVersions":
        ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_ACCEPT_VERSIONS_PARAMETER);
        if (versionPS == null){
            versionPS = parameters.getParameterShellWithCommonName(Parameter.COMMON_NAME_VERSION);
        }
        if (versionPS != null) {
            AcceptVersionsType acceptedVersions = getCap.addNewAcceptVersions();
            
            if (versionPS.hasSingleSpecifiedValue()) {
                acceptedVersions.addVersion((String) versionPS.getSpecifiedValue());
            }
            else {
                Object[] versionArray = versionPS.getSpecifiedValueArray();
                for (Object version : versionArray) {
                    acceptedVersions.addVersion((String) version);
                }
            }
        }

        // Parameter "sections":
        ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {
            SectionsType sections = getCap.addNewSections();

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                String[] selectedSections = (String[]) sectionParamShell.getSpecifiedValueArray();
                for (int i = 0; i < selectedSections.length; i++) {
                    sections.addSection(selectedSections[i]);
                }
            }
            else if (sectionParamShell.hasSingleSpecifiedValue()) {
                sections.addSection((String) sectionParamShell.getSpecifiedValue());
            }
        }

        return getCapDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }

    /**
     * Builds the GetObservation-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * <li>version</li>
     * <li>offering</li>
     * <li>observedProperty</li>
     * <li>resultFormat</li>
     * 
     * <br>
     * <br>
     * The following ones are optional:
     * <li>eventTime</li>
     * <li>procedure</li>
     * <li>featureOfInterest</li>
     * <li>result</li>
     * <li>resultModel</li>
     * <li>responseMode</li>
     * 
     * @param parameters
     *        parameters of the request
     * 
     * @return GetObservationRequest in XML-Format as String
     * @throws OXFException
     * @throws OXFException
     * 
     */
    public String buildGetObservationRequest(ParameterContainer parameters) throws OXFException {

        GetObservationDocument xb_getObsDoc = GetObservationDocument.Factory.newInstance();

        GetObservation xb_getObs = xb_getObsDoc.addNewGetObservation();

        //
        // set required elements:
        //
        xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());

        xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());

        xb_getObs.setOffering((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OFFERING_PARAMETER).getSpecifiedValue());

        xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());

        ParameterShell observedPropertyPS = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER);
        if (observedPropertyPS.hasMultipleSpecifiedValues()) {
            Object[] observedProperties = observedPropertyPS.getSpecifiedValueArray();
            xb_getObs.setObservedPropertyArray((String[]) observedProperties);
        }
        else if (observedPropertyPS.hasSingleSpecifiedValue()) {
            String observedProperty = (String) observedPropertyPS.getSpecifiedValue();
            xb_getObs.setObservedPropertyArray(new String[] {observedProperty});
        }
        
        //
        // set optional elements:
        //
        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER) != null) {
            ITime specifiedTime;

            Object timeParamValue = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_EVENT_TIME_PARAMETER).getSpecifiedValue();
            if (timeParamValue instanceof ITime) {
                specifiedTime = (ITime) timeParamValue;
            }
            else if (timeParamValue instanceof String) {
                specifiedTime = TimeFactory.createTime((String) timeParamValue);
            }
            else {
                throw new OXFException("The class (" + timeParamValue.getClass()
                        + ") of the value of the parameter 'eventTime' is not supported.");
            }

            BinaryTemporalOpType xb_binTempOp = BinaryTemporalOpType.Factory.newInstance();
            
            String timeType = null;

            if (specifiedTime instanceof ITimePeriod) {
                ITimePeriod oc_timePeriod = (ITimePeriod) specifiedTime;
                TimePeriodType xb_timePeriod = TimePeriodType.Factory.newInstance();

                TimePositionType xb_beginPosition = xb_timePeriod.addNewBeginPosition();
                xb_beginPosition.setStringValue(oc_timePeriod.getStart().toISO8601Format());

                TimePositionType xb_endPosition = xb_timePeriod.addNewEndPosition();
                xb_endPosition.setStringValue(oc_timePeriod.getEnd().toISO8601Format());
                
                xb_binTempOp.setTimeObject(xb_timePeriod);
                timeType = "TimePeriod";
            }
            else if (specifiedTime instanceof ITimePosition) {
                ITimePosition oc_timePosition = (ITimePosition) specifiedTime;
                TimeInstantType xb_timeInstant = TimeInstantType.Factory.newInstance();

                TimePositionType xb_timePosition = TimePositionType.Factory.newInstance();
                xb_timePosition.setStringValue(oc_timePosition.toISO8601Format());

                xb_timeInstant.setTimePosition(xb_timePosition);

                xb_binTempOp.setTimeObject(xb_timeInstant);
                timeType = "TimePosition";
            }

            EventTime eventTime = xb_getObs.addNewEventTime();
            eventTime.setTemporalOps(xb_binTempOp);
            
            // rename elements:
            XmlCursor cursor = eventTime.newCursor();
            cursor.toChild(new QName("http://www.opengis.net/ogc", "temporalOps"));
            cursor.setName(new QName("http://www.opengis.net/ogc", "TM_Equals"));

            cursor.toChild(new QName("http://www.opengis.net/gml", "_TimeObject"));
            cursor.setName(new QName("http://www.opengis.net/gml", timeType));

            // TODO Spec-Too-Flexible-Problem: for eventTime are several other "temporalOps" possible (not
            // only "TEquals")
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER) != null) {
            Object[] procedures = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValueArray();
            xb_getObs.setProcedureArray((String[]) procedures);
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER) != null) {
            ParameterShell foiParamShell = parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER);
            if (foiParamShell.hasMultipleSpecifiedValues()) {
                Object[] fois = foiParamShell.getSpecifiedValueArray();
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray((String[]) fois);
            }
            else {
                Object foi = foiParamShell.getSpecifiedValue();
                xb_getObs.addNewFeatureOfInterest().setObjectIDArray(new String[] { (String)foi});
            }
            // TODO Spec-Too-Flexible-Problem: it is also possible that the FeatureOfInterest is specified as
            // a "spatialOps"
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER) != null) {
            String filter = (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_PARAMETER).getSpecifiedValue();

            Result resultFilter = xb_getObs.addNewResult();

            try {
                XmlObject xobj = XmlObject.Factory.parse(filter);
                resultFilter.set(xobj);
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER) != null) {
            xb_getObs.setResultModel( (new QName("http://www.opengis.net/om/1.0", //http://www.opengis.net/sos/0.0
                                                 (String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESULT_MODEL_PARAMETER).getSpecifiedValue())));
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER) != null) {
            ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
            xb_getObs.setResponseMode(responseModeEnum);
        }

        return xb_getObsDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }
    
    
    /**
     * builds the GetObservationByID-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <ul><li>service</li>
     * <li>version</li>
     * <li>observationid</li>
     * <li>responseFormat</li></ul>
     * 
     * <br>
     * <br>
     * The following ones are optional:
     * <li>responseMode</li>
     * 
     * @param parameters
     *        parameters of the request
     * 
     * @return GetObservationByID-Request in XML-Format as String
     * @throws OXFException
     * 
     */
    public String buildGetObservationByIDRequest(ParameterContainer parameters)
    throws OXFException {
    	GetObservationByIdDocument xb_getObsDoc = GetObservationByIdDocument.Factory.newInstance();

    	GetObservationById xb_getObs = xb_getObsDoc.addNewGetObservationById();


//  	set required elements:

    	xb_getObs.setService((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_SERVICE_PARAMETER).getSpecifiedValue());

    	xb_getObs.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_VERSION_PARAMETER).getSpecifiedValue());

    	xb_getObs.setObservationId((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_OBSERVATION_ID_PARAMETER).getSpecifiedValue());

    	xb_getObs.setResponseFormat((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_FORMAT_PARAMETER).getSpecifiedValue());


//  	set optional elements:

    	if (parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER) != null) {
    		ResponseModeType.Enum responseModeEnum = ResponseModeType.Enum.forString((String) parameters.getParameterShellWithServiceSidedName(GET_OBSERVATION_BY_ID_RESPONSE_MODE_PARAMETER).getSpecifiedValue());
    		xb_getObs.setResponseMode(responseModeEnum);
    	}
//  	TODO ResultModel?
    	return xb_getObsDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }
    

    /**
     * builds a DescribeSensor-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <ul><li>service</li>
     * <li>version</li>
     * <li>sensorID</li></ul>
     * 
     * <br>
     * <br>
     * The following are optional:
     * <li>outputFormat</li>
     * 
     * 
     * @param parameters
     *        parameters and values of the request
     * 
     * @return DescribeSensorRequest in XML-Format as String
     */
    public String buildDescribeSensorRequest(ParameterContainer parameters) {

        DescribeSensorDocument descSensorDoc = DescribeSensorDocument.Factory.newInstance();

        DescribeSensor descSensor = descSensorDoc.addNewDescribeSensor();

        // set required elements:

        descSensor.setService((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());

        descSensor.setVersion((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue());

        descSensor.setProcedure((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_PROCEDURE_PARAMETER).getSpecifiedValue());

        if (parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT) != null) {
            descSensor.setOutputFormat((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_OUTPUT_FORMAT).getSpecifiedValue());
        }
        // TODO HACK -> outputformat is hard-coded:
        else {
            descSensor.setOutputFormat("text/xml;subtype=\"sensorML/1.0.1\"");
        }

        return descSensorDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }

    public String buildGetFeatureOfInterestRequest(ParameterContainer parameters) {

        GetFeatureOfInterestDocument getFoIDoc = GetFeatureOfInterestDocument.Factory.newInstance();

        GetFeatureOfInterest getFoI = getFoIDoc.addNewGetFeatureOfInterest();

        // set required elements:

        getFoI.setService((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_SERVICE_PARAMETER).getSpecifiedValue());

        ParameterShell versionPS = parameters.getParameterShellWithServiceSidedName(GET_FOI_VERSION_PARAMETER);
        getFoI.setVersion((String) versionPS.getSpecifiedValue());
        
        // set optional elements:

        if (parameters.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER) != null) {
            ParameterShell foiIDParamShell = parameters.getParameterShellWithServiceSidedName(GET_FOI_ID_PARAMETER);
            if (foiIDParamShell.hasSingleSpecifiedValue()) {
                String foiIDParamValue = (String) foiIDParamShell.getSpecifiedValue();
                getFoI.setFeatureOfInterestIdArray(new String[] {foiIDParamValue});
            }
            else {
                String[] foiIDParamValue = (String[]) foiIDParamShell.getSpecifiedValueArray();
                getFoI.setFeatureOfInterestIdArray(foiIDParamValue);
            }
        }
        else if (parameters.getParameterShellWithServiceSidedName(GET_FOI_LOCATION_PARAMETER) != null) {
            throw new UnsupportedOperationException("The parameter '" + GET_FOI_LOCATION_PARAMETER
                    + "' is not yet supported.");
        }

        if (parameters.getParameterShellWithServiceSidedName(GET_FOI_EVENT_TIME_PARAMETER) != null) {
            throw new UnsupportedOperationException("The parameter '"
                    + GET_FOI_EVENT_TIME_PARAMETER + "' is not yet supported.");
        }

        return getFoIDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }
    
    public String buildInsertObservation(ParameterContainer parameters) throws OXFException {
    	InsertObservationDocument insObDoc = InsertObservationDocument.Factory.newInstance();
    	InsertObservation insert = insObDoc.addNewInsertObservation();
    	addOperationMetadata(insert,parameters);
    	addAssignedSensorId(insert,parameters);
    	addObservation(insert,parameters);
    	/*
    	 * Validate before returning -> throw OXFException if validation fails
    	 */
    	doLaxRequestValidation(insObDoc);
    	return insObDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }

	public String buildRegisterSensor(ParameterContainer parameters) throws OXFException{
		
		RegisterSensorDocument regSensorDoc = RegisterSensorDocument.Factory.newInstance();
		RegisterSensor regSensor = regSensorDoc.addNewRegisterSensor();
		addOperationMetadata(regSensor, parameters);
		addSensorDescription(regSensor,parameters);
		addObservationTemplate(regSensor,parameters);
		doLaxRequestValidation(regSensorDoc);
		return regSensorDoc.xmlText(XMLBeansTools.PRETTYPRINT);
	}

	/**
	 * Observation Type, e.g. om:Measurement<br />
	 *  om:samplingTime<br />
     * 	om:procedure<br />
     * 	om:observedProperty<br />
     * 	om:featureOfInterest<br />
     * 	om:result<br />
	 */
	private void addObservation(InsertObservation insert,
			ParameterContainer parameters) {
		ObservationType obsType = insert.addNewObservation();
    	String observationType = ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT;
    	try {
    		observationType = (String)parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE).getSpecifiedValue();
		} catch (Exception e) {
			// ignoring -> value not defined -> using default
		}
    	// TODO add more observation types
    	if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT)){
    		// fill in measurement
    		obsType = (ObservationType) obsType.
    				substitute(XMLConstants.QNAME_OM_1_0_MEASUREMENT,
    						MeasurementType.type);
    	} else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_CATEGORY)){
    		//fill in category
    		obsType = (ObservationType) obsType.
    				substitute(XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION, 
    						CategoryObservationType.type);
    	}
    	insert.setObservation(obsType);
    	addSamplingTime(obsType,parameters);
    	addProcedure(obsType,parameters);
    	addObservedProperty(obsType,parameters);
    	addFeatureOfInterest(obsType,parameters);
    	addResult(obsType,parameters,observationType);
	}

	/**
	 * om:result
	 */
	private void addResult(ObservationType obsType,
			ParameterContainer parameters,
			String observationType) {
    	XmlObject result = obsType.addNewResult();
    	MeasureType mt = MeasureType.Factory.newInstance();
    	
    	ParameterShell ps = parameters.getParameterShellWithCommonName(
    			INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE);
    	if (ps != null) {
    		 String uomCode = ps.getSpecifiedValue().toString();
    		 mt.setUom(uomCode);
    	}
    	
    	mt.setStringValue((String) 
    			parameters.getParameterShellWithCommonName(
    					ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER).
    					getSpecifiedValue());
    	
    	/*
    	 * if observation is a CategoryObservation 
    	 * and the CodeSpace for the result value is set, add it
    	 */
    	// TODO Check if this is working with validating SOS?
    	if(observationType != null && observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_CATEGORY)){

    		ParameterShell pS = parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE);
    		if (pS != null){

    			String codeSpace = (String) pS.getSpecifiedValue();

    			if(codeSpace != null &&	!codeSpace.equals("")){
    				XmlCursor cursorRes = result.newCursor();
    				cursorRes.toNextToken();
    				cursorRes.insertAttributeWithValue(XMLConstants.QNAME_OM_1_0_CODE_SPACE,codeSpace);
    			}
    		}
    	}
    	result.set(mt);
	}

	/**
	 * Feature of Interest
	 */
	private void addFeatureOfInterest(ObservationType obsType,
			ParameterContainer parameters) {
    	FeaturePropertyType featureProp = obsType.addNewFeatureOfInterest();
    	SamplingPointDocument sampPointDoc = 
    			SamplingPointDocument.Factory.newInstance();
    	SamplingPointType sampPoint = sampPointDoc.addNewSamplingPoint();
    	sampPoint.setId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue());
    	//Code for new Features
    	ParameterShell nameObj =  parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_NAME);
    	if(nameObj !=null){
    		String name = (String) nameObj.getSpecifiedValue();
    		sampPoint.addNewName().setStringValue(name);
    		
    		String desc = (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_DESC).getSpecifiedValue();
    		sampPoint.addNewDescription().setStringValue(desc);
    		
    		sampPoint.addNewSampledFeature();
    		
    		PointType type = sampPoint.addNewPosition().addNewPoint();
    		type.addNewPos().setStringValue((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_NEW_FOI_POSITION).getSpecifiedValue());
    		String srsName = (String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_POSITION_SRS).getSpecifiedValue();
    		if (srsName != null) {
    			type.setSrsName(srsName);
    		} else { //default is epsg:4326 (= WGS84)
    			type.setSrsName("urn:ogc:def:crs:EPSG::4326");
    		}
    	}
    	//End Code for new Features
    	featureProp.set(sampPointDoc);
	}

	/**
	 * Observed Property
	 */
	private void addObservedProperty(ObservationType obsType,
			ParameterContainer parameters) {
    	PhenomenonPropertyType phenProp = obsType.addNewObservedProperty();
    	phenProp.setHref((String) parameters.
    			getParameterShellWithCommonName(
    					ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).
    					getSpecifiedValue());
	}

	/**
	 * Procedure/Sensor
	 */
	private void addProcedure(ObservationType obsType,
			ParameterContainer parameters) {
    	ProcessPropertyType procedure = obsType.addNewProcedure();
    	procedure.setHref((String) parameters.
    			getParameterShellWithCommonName(
    					ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER).
    					getSpecifiedValue());
	}

	/**
	 * om:samplingTime
	 */
	private void addSamplingTime(ObservationType obsType,
			ParameterContainer parameters) {
    	TimeObjectPropertyType timeProp = obsType.addNewSamplingTime();
    	AbstractTimeObjectType atot = timeProp.addNewTimeObject();
    	TimeInstantType timeInstant = 
    			(TimeInstantType) atot.
    			substitute(XMLConstants.QNAME_GML_TIMEINSTANT,
    					TimeInstantType.type);
    	TimePositionType timePos = timeInstant.addNewTimePosition();
    	String timeParamValue = (String)parameters.
    			getParameterShellWithCommonName(
    					ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME).
    					getSpecifiedValue();
    	ITimePosition timePosition = (ITimePosition)TimeFactory.createTime(timeParamValue);
    	timePos.setStringValue(timePosition.toISO8601Format());
    	timeInstant.setId("t_"+timeInstant.hashCode());
	}

	/**
	 * sos:AssignedSensorId
	 */
	private void addAssignedSensorId(InsertObservation insert,
			ParameterContainer parameters) {
    	insert.setAssignedSensorId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());
	}

	/**
	 * Operation Metadata: Service type and version
	 */
	private void addOperationMetadata(InsertObservation insert,
			ParameterContainer parameters) {
		String version =(String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue();
    	insert.setVersion(version);
    	String service =(String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue();
    	insert.setService(service);
	}

	/**
	 * TODO Review and make better use of XmlError methods
	 * Throws OXFException at the first error that is found
	 * @throws OXFException in the case of not valid request
	 */
	private void doLaxRequestValidation(XmlObject xmlDoc) throws OXFException {
		XMLBeansParser.registerLaxValidationCase(GMLAbstractFeatureCase.getInstance());
		XMLBeansParser.registerLaxValidationCase(SASamplingPointCase.getInstance());
		
		if (xmlDoc instanceof RegisterSensorDocument) {
			XMLBeansParser.registerLaxValidationCase(OfferingInSMLOutputsCase.getInstance());
		}
		
		/*
		 * get errors. if empty, do not throw exception
		 */
		Collection<XmlError> exs = XMLBeansParser.validate(xmlDoc);

		String message = null;
		String parameterName = null;
		for (XmlError error : exs) {
			// ExceptionCode for Exception
			ExceptionCode exCode = null;

			// get name of the missing or invalid parameter
			message = error.getMessage();
			if (message != null) {

				// check, if parameter is missing or value of parameter
				// is
				// invalid to ensure, that correct
				// exceptioncode in exception response is used

				// invalid parameter value
				if (message.startsWith("The value")) {
					exCode = ExceptionCode.InvalidParameterValue;

					// split message string to get attribute name
					String[] messAndAttribute = message
							.split("attribute '");
					if (messAndAttribute.length == 2) {
						parameterName = messAndAttribute[1]
								.replace("'", "");
					}
				}

				// invalid enumeration value --> InvalidParameterValue
				else if (message.contains("not a valid enumeration value")) {
					exCode = ExceptionCode.InvalidParameterValue;

					// get attribute name
					String[] messAndAttribute = message.split(" ");
					parameterName = messAndAttribute[10];
				}

				// mandatory attribute is missing -->
				// missingParameterValue
				else if (message.startsWith("Expected attribute")) {
					exCode = ExceptionCode.MissingParameterValue;

					// get attribute name
					String[] messAndAttribute = message
							.split("attribute: ");
					if (messAndAttribute.length == 2) {
						String[] attrAndRest = messAndAttribute[1]
								.split(" in");
						if (attrAndRest.length == 2) {
							parameterName = attrAndRest[0];
						}
					}
				}

				// mandatory element is missing -->
				// missingParameterValue
				else if (message.startsWith("Expected element")) {
					exCode = ExceptionCode.MissingParameterValue;

					// get element name
					String[] messAndElements = message.split(" '");
					if (messAndElements.length >= 2) {
						String elements = messAndElements[1];
						if (elements.contains("offering")) {
							parameterName = "offering";
						} else if (elements.contains("observedProperty")) {
							parameterName = "observedProperty";
						} else if (elements.contains("responseFormat")) {
							parameterName = "responseFormat";
						} else if (elements.contains("procedure")) {
							parameterName = "procedure";
						} else if (elements.contains("featureOfInterest")) {
							parameterName = "featureOfInterest";
						} else {
							// TODO check if other elements are invalid
						}
					}
				}
				// invalidParameterValue
				else if (message.startsWith("Element")) {
					exCode = ExceptionCode.InvalidParameterValue;

					// get element name
					String[] messAndElements = message.split(" '");
					if (messAndElements.length >= 2) {
						String elements = messAndElements[1];
						if (elements.contains("offering")) {
							parameterName = "offering";
						} else if (elements.contains("observedProperty")) {
							parameterName = "observedProperty";
						} else if (elements.contains("responseFormat")) {
							parameterName = "responseFormat";
						} else if (elements.contains("procedure")) {
							parameterName = "procedure";
						} else if (elements.contains("featureOfInterest")) {
							parameterName = "featureOfInterest";
						} else {
							// TODO check if other elements are invalid
						}
					}
				} else {
					// create simple OXFExcpetion exception
					String msg = String.format("Error in XMLBeans request validation: %s",
							message);
					throw new OXFException(msg);
				}

				// create OXFException with more information exception
				String msg = String.format("Error in XMLBeans request validation. Causing error: %s. Element name: %s. Message: %s",
						exCode,
						parameterName,
						message);
				throw new OXFException(msg);
			}
		}
	}

	private void addObservationTemplate(RegisterSensor regSensor,
			ParameterContainer parameters) throws OXFException {
    	// TODO insert other observation templates, current state: CategoryObservation, Measurement
    	ObservationTemplate obsTemp;
    	/*
    	 *	A: parse given XML-String
    	 */
    	if(parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE) != null){
    		try {
				obsTemp = regSensor.addNewObservationTemplate();
    			ObservationType obsType = ObservationType.Factory.parse((String)parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE).getSpecifiedValue());
				obsTemp.set(obsType);
			} catch (XmlException e) {
				throw new OXFException("Could not parse observation type from paramter shell.");
			}
		/*
		 *	B: create template depending on parameters 
		 */
    	} else if (parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TYPE) != null){
    		String observationType = (String)parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TYPE).getSpecifiedValue();
    		obsTemp = createObservationTemplate(observationType,parameters);
    		regSensor.addNewObservationTemplate().set(obsTemp);
    	}
	}

	private void addSensorDescription(RegisterSensor regSensor,
			ParameterContainer parameters) throws OXFException {
    	SensorDescription sensorDesc = regSensor.addNewSensorDescription();
    	SystemDocument systemDocument = null;
    	// Is a SensorML file passed along?
    	if(parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER)!= null){
    		try {
				systemDocument = SystemDocument.Factory.parse((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue());
			} catch (XmlException e) {
				throw new OXFException(
						String.format(
								"Given SensorML document from paramter ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER could not be parsed. Error: %s",
								e.getMessage()),
						e);
			}
    	}
    	else {
    		systemDocument = createSystemDocumentFromParameters(parameters);
    	}
    	SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
    	sensorMLDocument.addNewSensorML().addNewMember().set(systemDocument);
    	// Get SensorML version by checking the class of SystemDocument
    	if (systemDocument instanceof net.opengis.sensorML.x101.SystemDocument) {
    		XmlCursor c = sensorMLDocument.getSensorML().newCursor();
    		c.toNextToken();
    		c.insertAttributeWithValue("version","1.0.1");
    		c.dispose();
    	}
    	sensorDesc.set(sensorMLDocument);
	}

	private void addOperationMetadata(RegisterSensor regSensor,
			ParameterContainer parameters) {
		regSensor.setVersion((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
    	regSensor.setService((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
	}

	private SystemDocument createSystemDocumentFromParameters(
			ParameterContainer parameters) {
		SystemDocument systemDocument = SystemDocument.Factory.newInstance();
		SystemType system = null;
    	system = SystemType.Factory.newInstance();
    	
    	system.setId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ID_PARAMETER).getSpecifiedValue());
    	
    	Position pos = Position.Factory.newInstance();
    	pos.setName((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_NAME_PARAMETER).getSpecifiedValue());
    	PositionType posType = pos.addNewPosition();
    	posType.setFixed(new Boolean((String)parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_POSITION_FIXED_PARAMETER).getSpecifiedValue()));
    	posType.setReferenceFrame("urn:ogc:crs:epsg:4326");
    	VectorPropertyType vecPropType =  posType.addNewLocation();
    	VectorType vecType = vecPropType.addNewVector();
    	Coordinate cordLatitude = vecType.addNewCoordinate();
    	cordLatitude.setName("latitude");
    	cordLatitude.addNewQuantity().setValue(new Double ((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER).getSpecifiedValue()));
    	Coordinate cordLongitude = vecType.addNewCoordinate();
    	cordLongitude.setName("longitude");
    	cordLongitude.addNewQuantity().setValue(new Double ((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER).getSpecifiedValue()));
    	system.setPosition(pos);
    	
    	//TODO support Input List here
    	Inputs inputs = system.addNewInputs();
    	inputs.addNewInputList();
    	
    	Outputs outputs = system.addNewOutputs();
    	OutputList outputList = outputs.addNewOutputList();
    	IoComponentPropertyType IoCompProp = outputList.addNewOutput();
    	Quantity quantity = IoCompProp.addNewQuantity();
    	quantity.setDefinition((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue());
    	quantity.addNewUom().setCode((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER).getSpecifiedValue());
    	
    	systemDocument.addNewSystem().set(system);
    	
    	return systemDocument;
	}

	private ObservationTemplate createObservationTemplate(
			String observationType, ParameterContainer parameters) throws OXFException {
		QName type = null;
		if (observationType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY) ){
			type = XMLConstants.QNAME_OM_1_0_CATEGORY_OBSERVATION;
		} else if (observationType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT) ){
			type = XMLConstants.QNAME_OM_1_0_MEASUREMENT;
		} else{
			throw new OXFException("Observation type '" + observationType + "' not supported.");
		}
		ObservationTemplate obsTemp;
		obsTemp = ObservationTemplate.Factory.newInstance();
		XmlCursor xmlCur = obsTemp.newCursor();
		xmlCur.toNextToken();
		xmlCur.beginElement(type);
		xmlCur.insertElement(XMLConstants.QNAME_OM_1_0_SAMPLING_TIME);
		xmlCur.insertElement(XMLConstants.QNAME_OM_1_0_PROCEDURE);
		xmlCur.insertElement(XMLConstants.QNAME_OM_1_0_OBSERVED_PROPERTY);
		xmlCur.insertElement(XMLConstants.QNAME_OM_1_0_FEATURE_OF_INTEREST);
		if (observationType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT)) {
			xmlCur.beginElement(XMLConstants.QNAME_OM_1_0_RESULT);
			xmlCur.insertAttributeWithValue("uom",(String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_UOM_PARAMETER).getSpecifiedValue());
		} else {
			xmlCur.insertElement(XMLConstants.QNAME_OM_1_0_RESULT);
		}
		xmlCur.insertChars((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_DEFAULT_RESULT_VALUE).getSpecifiedValue());
		return obsTemp;
	}	
    
}