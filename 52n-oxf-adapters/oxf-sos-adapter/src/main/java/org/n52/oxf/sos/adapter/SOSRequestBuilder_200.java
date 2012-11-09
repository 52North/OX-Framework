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

package org.n52.oxf.sos.adapter;

import net.opengis.fes.x20.BinaryTemporalOpType;
import net.opengis.fes.x20.DuringDocument;
import net.opengis.fes.x20.TEqualsDocument;
import net.opengis.gml.x32.TimeInstantDocument;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodDocument;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.sos.x20.GetCapabilitiesDocument;
import net.opengis.sos.x20.GetCapabilitiesType;
import net.opengis.sos.x20.GetFeatureOfInterestDocument;
import net.opengis.sos.x20.GetFeatureOfInterestType;
import net.opengis.sos.x20.GetObservationDocument;
import net.opengis.sos.x20.GetObservationType;
import net.opengis.sos.x20.GetObservationType.TemporalFilter;
import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;

import org.apache.commons.lang.NotImplementedException;
import org.jfree.util.Log;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.xmlbeans.tools.XMLBeansTools;

/**
 * contains attributes and methods to encode SOSOperationRequests as String in xml-format
 */
public class SOSRequestBuilder_200 implements ISOSRequestBuilder {

    public String buildGetCapabilitiesRequest(ParameterContainer parameters) {
        
        // FIXME not tested yet
        GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();
        GetCapabilitiesType getCap = getCapDoc.addNewGetCapabilities2();

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
                String[] versionArray = versionPS.getSpecifiedTypedValueArray(String[].class);;
                for (String version : versionArray) {
                    acceptedVersions.addVersion(version);
                }
            }
        }

        // Parameter "sections":
        ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {
            SectionsType sections = getCap.addNewSections();

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                String[] selectedSections = sectionParamShell.getSpecifiedTypedValueArray(String[].class);
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

    public String buildGetObservationRequest(ParameterContainer parameters) throws OXFException {
        GetObservationDocument xb_getObsDoc = GetObservationDocument.Factory.newInstance();
        GetObservationType xb_getObs = xb_getObsDoc.addNewGetObservation();
        xb_getObs.setService((String) getShellForServerParameter(parameters, GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());
        xb_getObs.setVersion((String) getShellForServerParameter(parameters, GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());
        processOffering(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_OFFERING_PARAMETER));
        processResponseFormat(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
        processObservedProperty(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        processTemporalFilter(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_TEMPORAL_FILTER_PARAMETER));
        processProcedure(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_PROCEDURE_PARAMETER));
        processFeatureOfInterest(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
        processSpatialFilter(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_SPATIAL_FILTER_PARAMETER));
        return xb_getObsDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }
    
    private ParameterShell getShellForServerParameter(ParameterContainer container, String name) {
        return container.getParameterShellWithServiceSidedName(name);
    }

    private void processSpatialFilter(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        // TODO implement
        throw new NotImplementedException();
    }

    private void processFeatureOfInterest(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        ParameterShell foiParamShell = shell;
        if (foiParamShell.hasMultipleSpecifiedValues()) {
            String[] fois = foiParamShell.getSpecifiedTypedValueArray(String[].class);
            xb_getObs.setFeatureOfInterestArray(fois);
        }
        else {
            Object foi = foiParamShell.getSpecifiedValue();
            xb_getObs.addNewFeatureOfInterest().setStringValue((String) foi);
        }
    }

    protected void processOffering(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell != null) { // optional parameter
            xb_getObs.setOfferingArray(shell.getSpecifiedTypedValueArray(String[].class));
        }
    }

    protected void processResponseFormat(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell != null) {
            xb_getObs.setResponseFormat((String) shell.getSpecifiedValue());
        }
    }

    protected void processProcedure(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell != null) { // optional parameter
            xb_getObs.setProcedureArray(shell.getSpecifiedTypedValueArray(String[].class));
        }
    }
    
    protected void processObservedProperty(GetObservationType xb_getObs, ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        String[] observedProperties = shell.getSpecifiedTypedValueArray(String[].class);
        xb_getObs.setObservedPropertyArray(observedProperties);
    }

    protected void processTemporalFilter(GetObservationType xb_getObs, ParameterShell shell) throws OXFException {
        if (shell == null) {
            return; // optional parameter
        }
        
        ITime specifiedTime;
        Object timeParamValue = shell.getSpecifiedValue();
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

        if (specifiedTime instanceof ITimePeriod) {
            
            ITimePeriod oc_timePeriod = (ITimePeriod) specifiedTime;
            TimePeriodDocument timePeriodDoc = TimePeriodDocument.Factory.newInstance();
            TimePeriodType xb_timePeriod = timePeriodDoc.addNewTimePeriod();
            DuringDocument duringDoc = DuringDocument.Factory.newInstance();
            BinaryTemporalOpType during = duringDoc.addNewDuring();
            
            TimePositionType xb_beginPosition = xb_timePeriod.addNewBeginPosition();
            TimePositionType xb_endPosition = xb_timePeriod.addNewEndPosition();
            xb_beginPosition.setStringValue(oc_timePeriod.getStart().toISO8601Format());
            xb_endPosition.setStringValue(oc_timePeriod.getEnd().toISO8601Format());

            xb_timePeriod.setId("_1");
            during.set(timePeriodDoc);
            during.setValueReference("phenomenonTime");

            TemporalFilter spatialFilter = xb_getObs.addNewTemporalFilter();
            spatialFilter.set(duringDoc);
        }
        else if (specifiedTime instanceof ITimePosition) {
            ITimePosition oc_timePosition = (ITimePosition) specifiedTime;
            TimeInstantDocument timeInstanceDoc = TimeInstantDocument.Factory.newInstance();
            TimeInstantType xb_timeInstant = timeInstanceDoc.addNewTimeInstant();
            TEqualsDocument equalsDoc = TEqualsDocument.Factory.newInstance();
            BinaryTemporalOpType equals = equalsDoc.addNewTEquals();
            
            TimePositionType xb_timePosition = TimePositionType.Factory.newInstance();
            xb_timePosition.setStringValue(oc_timePosition.toISO8601Format());
            xb_timeInstant.setTimePosition(xb_timePosition);

            xb_timeInstant.setId("_1");
            equals.set(timeInstanceDoc);
            equals.setValueReference("phenomenonTime");
            
            TemporalFilter spatialFilter = xb_getObs.addNewTemporalFilter();
            spatialFilter.set(equalsDoc);
        }
    }
    
    
    public String buildGetObservationByIDRequest(ParameterContainer parameters) throws OXFException {
        throw new NotImplementedException("SOS Specification 2.0 not officially supported by now.");
    }

    public String buildDescribeSensorRequest(ParameterContainer parameters) {
        DescribeSensorDocument descSensorDoc = DescribeSensorDocument.Factory.newInstance();
        DescribeSensorType descSensor = descSensorDoc.addNewDescribeSensor();

        // set required elements:

        descSensor.setService((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
        descSensor.setVersion((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
        processProcedure(descSensor, getShellForServerParameter(parameters, DESCRIBE_SENSOR_PROCEDURE_PARAMETER));
        processProcedureDescriptionFormat(descSensor, getShellForServerParameter(parameters, DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT));
        
        
        return descSensorDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }
    
    protected void processProcedureDescriptionFormat(DescribeSensorType descSensor, ParameterShell shell) {
        if (shell == null) {
            Log.error("Missing shell parameter '" + DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT + "'.");
            return; // throwing OXFException would break interface
        }
        descSensor.setProcedureDescriptionFormat((String) shell.getSpecifiedValue());
    }

    private void processProcedure(DescribeSensorType xb_descSensor, ParameterShell shell) {
        if (shell == null) {
            xb_descSensor.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/1.0.1");
        } else {
            xb_descSensor.setProcedure((String) shell.getSpecifiedValue());
        }
    }

    public String buildGetFeatureOfInterestRequest(ParameterContainer parameters) {
    	GetFeatureOfInterestDocument xb_getFOIDoc = GetFeatureOfInterestDocument.Factory.newInstance();
    	GetFeatureOfInterestType xb_getFOI = xb_getFOIDoc.addNewGetFeatureOfInterest();

    	xb_getFOI.addProcedure((String) parameters.getParameterShellWithServiceSidedName("procedure").getSpecifiedValue());
        
        return xb_getFOIDoc.xmlText(XMLBeansTools.PRETTYPRINT);
    }

    public String buildInsertObservation(ParameterContainer parameters) {
        // TODO implement
    	throw new NotImplementedException();
    }

    /**
     * Builds a RegisterSensor request and returns it.
     * A SensorML file can either be passed along or a set of parameters is used to create one.
     */
    public String buildRegisterSensor(ParameterContainer parameters) throws OXFException {
        // TODO implement
        throw new NotImplementedException();
    }	
    
}