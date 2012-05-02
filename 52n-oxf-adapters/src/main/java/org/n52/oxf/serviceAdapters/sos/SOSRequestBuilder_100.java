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
 
 Created on: 06.01.2006
 *********************************************************************************/

package org.n52.oxf.serviceAdapters.sos;

import javax.xml.namespace.QName;

import net.opengis.gml.FeaturePropertyType;
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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.owsCommon.capabilities.ITime;
import org.n52.oxf.owsCommon.capabilities.Parameter;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ParameterShell;
import org.n52.oxf.util.XmlBeansHelper;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;

/**
 * contains attributes and methods to encode SOSOperationRequests as String in xml-format
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * @author <a href="mailto:ehjuerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class SOSRequestBuilder_100 implements ISOSRequestBuilder {
	
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

        return XmlBeansHelper.formatStringRequest(getCapDoc);
    }

    /**
     * builds the GetObservation-Request. <br>
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

        return XmlBeansHelper.formatStringRequest(xb_getObsDoc);
    }
    
    
    /**
     * builds the GetObservationByID-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * <li>version</li>
     * <li>observationid</li>
     * <li>responseFormat</li>
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
    	return XmlBeansHelper.formatStringRequest(xb_getObsDoc);
    }
    

    /**
     * builds a DescribeSensor-Request. <br>
     * <br>
     * For the ParameterContainer 'parameters' are the ParameterShells with the following serviceSidedNames
     * required:
     * <li>service</li>
     * <li>version</li>
     * <li>sensorID</li>
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

        return XmlBeansHelper.formatStringRequest(descSensorDoc);
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

        return XmlBeansHelper.formatStringRequest(getFoIDoc);
    }

    public String buildInsertObservation(ParameterContainer parameters) {
    	
    	InsertObservationDocument insObDoc = InsertObservationDocument.Factory.newInstance();
    	String version =(String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue();
    	

    	InsertObservation insert = insObDoc.addNewInsertObservation();
    	
    	//Use right Namespace
		XmlBeansHelper.addDefaultNS(insert);
    	
    	insert.setVersion(version);
    	
    	
    	String service =(String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue();
    	insert.setService(service);
    	insert.setAssignedSensorId((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());
    	
    	
        
    	ObservationType obsType = null;
    	String observationType = null;
    	try {
    		observationType = (String)parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE).getSpecifiedValue();
		} catch (Exception e) {
			//nothing bad happend so switch to default
			observationType = ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT;
		}
    	
    	if (observationType == null) {
    		// fill in measurement
    		obsType = MeasurementType.Factory.newInstance();
    	} else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_MEASUREMENT)){
    		// fill in measurement
    		obsType = MeasurementType.Factory.newInstance();
    	} else if (observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_CATEGORY)){
    		//fill in category
    		obsType = CategoryObservationType.Factory.newInstance();
    	}
    	
		XmlBeansHelper.addDefaultNS(obsType);
    	
    	ProcessPropertyType procedure = obsType.addNewProcedure();
    	procedure.setHref((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());
    	
    	FeaturePropertyType featureProp = obsType.addNewFeatureOfInterest();
    	SamplingPointType sampPoint = SamplingPointType.Factory.newInstance();
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
    			type.setSrsName("urn:ogc:def:crs:EPSG:4326");
    		}
    	}
    	//End Code for new Features
    	featureProp.setAbstractFeature(sampPoint);
    	XmlCursor cursorProp = featureProp.newCursor();
    	cursorProp.toChild(new QName("http://www.opengis.net/gml", "_Feature"));
    	cursorProp.setName(new QName("http://www.opengis.net/sampling/1.0", "SamplingPoint"));
    	
    	PhenomenonPropertyType phenProp = obsType.addNewObservedProperty();
    	phenProp.setHref((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue());
    	
    	
    	// add Time:
    	TimeObjectPropertyType timeProp = obsType.addNewSamplingTime();
    	
    	
    	TimeInstantType time = TimeInstantType.Factory.newInstance();
    	TimePositionType timePos = time.addNewTimePosition();
    	
    	String timeParamValue = (String)parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_SAMPLING_TIME).getSpecifiedValue();
    	ITimePosition timePosition = (ITimePosition)TimeFactory.createTime( timeParamValue);
    	
    	timePos.setStringValue(timePosition.toISO8601Format());
    	
    	timeProp.setTimeObject(time);
    
    	XmlCursor cursor = timeProp.newCursor();
    	cursor.toChild(new QName("http://www.opengis.net/gml", "_TimeObject"));
        cursor.setName(new QName("http://www.opengis.net/gml", "TimeInstant"));
    	
    	//add Result:
    	XmlObject result = obsType.addNewResult();
    	XmlCursor cursorRes = result.newCursor();
    	cursorRes.setTextValue((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue());
    	// if observation is a CategoryObservation and the CodeSpace for the result value is set, add it
    	{
    		if(observationType != null && observationType.equals(ISOSRequestBuilder.INSERT_OBSERVATION_TYPE_CATEGORY)){
    	
    			ParameterShell pS = parameters.getParameterShellWithCommonName(ISOSRequestBuilder.INSERT_OBSERVATION_CATEGORY_OBSERVATION_RESULT_CODESPACE);
    			if (pS != null){

    				String codeSpace = (String) pS.getSpecifiedValue();

    				if(codeSpace != null &&	!codeSpace.equals("")){
    					cursorRes.toNextToken();
    					cursorRes.insertAttributeWithValue(new QName("http://www.opengis.net/om/1.0", "codeSpace"),codeSpace);
    				}
    				
    			}
    			
    		}
    	}
    	
    	insObDoc.getInsertObservation().setObservation(obsType);
    	
    	XmlCursor measurementCur = insert.newCursor();
//    	measurementCur.toFirstContentToken();
//    	measurementCur.toNextToken();
    	if (observationType.equals(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT)) {
    		measurementCur.toChild(new QName("http://www.opengis.net/om/1.0", "Observation"));
        	measurementCur.setName(new QName("http://www.opengis.net/om/1.0", "Measurement"));
    	} else if(observationType.equals(ISOSRequestBuilder.REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY)) {
    		measurementCur.toChild(new QName("http://www.opengis.net/om/1.0", "Observation"));
        	measurementCur.setName(new QName("http://www.opengis.net/om/1.0", "CategoryObservation"));
    	}
    	
    	return XmlBeansHelper.formatStringRequest(insObDoc);
    }

    /**
     * Builds a RegisterSensor request and returns it.
     * A SensorML file can either be passed along or a set of parameters is used to create one.
     */
    public String buildRegisterSensor(ParameterContainer parameters) throws OXFException{
    	
    	RegisterSensorDocument regSensorDoc = RegisterSensorDocument.Factory.newInstance();
    	RegisterSensor regSensor = regSensorDoc.addNewRegisterSensor();
    	
    	regSensor.setVersion((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
    	regSensor.setService((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
    	
    	SensorDescription sensorDesc = regSensor.addNewSensorDescription();
    	
    	SystemDocument systemDocument = null;
    	SystemType system = null;
    	
    	// Is a SensorML file passed along?:
    	if(parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER)!= null){
    		try {
				systemDocument = SystemDocument.Factory.parse((String) parameters.getParameterShellWithCommonName(ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue());
			} catch (XmlException e) {
				throw new OXFException(e);
			}
    	}
    	else {
    		systemDocument = SystemDocument.Factory.newInstance();
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
    	}
    	SensorMLDocument sensorMLDocument = SensorMLDocument.Factory.newInstance();
    	sensorMLDocument.addNewSensorML().addNewMember().set(systemDocument);
    	sensorDesc.set(sensorMLDocument);
    	
    	// TODO insert other observation templates, current state: CategoryObservation, Measurement
    	/*
    	 *	OBSERVATION TEMPLATE
    	 *
    	 *	A: parse given XML-String
    	 *
    	 *	or
    	 *
    	 *	B: create template depending on parameters 
    	 */
    	ObservationTemplate obsTemp;
    	/*
    	 *	A
    	 */
    	if(parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE) != null){
    		try {
				obsTemp = regSensor.addNewObservationTemplate();
    			ObservationType obsType = ObservationType.Factory.parse((String)parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TEMPLATE).getSpecifiedValue());
				
				obsTemp.set(obsType);
				
			} catch (XmlException e) {
				// FIXME do sinnvoll handling
				e.printStackTrace();
			}
			
		/*
		 *	B
		 */
    	} else if (parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TYPE) != null){
    		// so we have to identify the type of observation
    		String observationType = (String)parameters.getParameterShellWithCommonName(REGISTER_SENSOR_OBSERVATION_TYPE).getSpecifiedValue();
    		
    		//
    		//  CATEGORY_OBSERVATION
    		//
    		if(	observationType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_CATEGORY) ){
    			
//    			CategoryObservationType obsType = CategoryObservationType.Factory.newInstance();
    			obsTemp = ObservationTemplate.Factory.newInstance();
    			
    			XmlCursor xmlCur = obsTemp.newCursor();

    			xmlCur.toNextToken();
    			// new QName("http://www.opengis.net/om/1.0", "element", "om")
    			xmlCur.beginElement(new QName("http://www.opengis.net/om/1.0", "CategoryObservation", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "samplingTime", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "procedure", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "observedProperty", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "featureOfInterest", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "result", "om"));
    			
    			regSensor.addNewObservationTemplate().set(obsTemp);
    			
    		// 
    		//	MEASUREMENT
    		// 
    		} else if( observationType.equals(REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT) ){
    			
    			obsTemp = ObservationTemplate.Factory.newInstance();
    			
    			XmlCursor xmlCur = obsTemp.newCursor();

    			xmlCur.toNextToken();
    			// new QName("http://www.opengis.net/om/1.0", "element", "om")
    			xmlCur.beginElement(new QName("http://www.opengis.net/om/1.0", "Measurement", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "samplingTime", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "procedure", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "observedProperty", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "featureOfInterest", "om"));
    			xmlCur.insertElement(new QName("http://www.opengis.net/om/1.0", "result", "om"));
    			
    			regSensor.addNewObservationTemplate().set(obsTemp);
            	
    		} else{
    			throw new OXFException("Observation type '" + observationType + "' not supported.");
    		}
    	}
    	
    	// FIXME are the next two lines really needed for MeasurementObservationTemplates?
//    	XmlCursor cursor = obsTemp.newCursor();
//    	cursor.toChild(new QName("http://www.opengis.net/om/1.0", "Observation"));
//    	cursor.setName(new QName("http://www.opengis.net/om/1.0", "Measurement"));
    	
    	return XmlBeansHelper.formatStringRequest(regSensorDoc);
    }	
    
}