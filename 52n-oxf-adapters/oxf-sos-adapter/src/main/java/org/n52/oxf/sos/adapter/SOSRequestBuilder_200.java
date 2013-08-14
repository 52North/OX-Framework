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
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * contains attributes and methods to encode SOSOperationRequests as String in xml-format
 */
public class SOSRequestBuilder_200 implements ISOSRequestBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SOSRequestBuilder_200.class);

    @Override
	public String buildGetCapabilitiesRequest(final ParameterContainer parameters) {
        
        // FIXME not tested yet
        final GetCapabilitiesDocument getCapDoc = GetCapabilitiesDocument.Factory.newInstance();
        final GetCapabilitiesType getCap = getCapDoc.addNewGetCapabilities2();

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
            final AcceptVersionsType acceptedVersions = getCap.addNewAcceptVersions();
            
            if (versionPS.hasSingleSpecifiedValue()) {
                acceptedVersions.addVersion((String) versionPS.getSpecifiedValue());
            }
            else {
                final String[] versionArray = versionPS.getSpecifiedTypedValueArray(String[].class);;
                for (final String version : versionArray) {
                    acceptedVersions.addVersion(version);
                }
            }
        }

        // Parameter "sections":
        final ParameterShell sectionParamShell = parameters.getParameterShellWithServiceSidedName(GET_CAPABILITIES_SECTIONS_PARAMETER);
        if (sectionParamShell != null) {
            final SectionsType sections = getCap.addNewSections();

            if (sectionParamShell.hasMultipleSpecifiedValues()) {
                final String[] selectedSections = sectionParamShell.getSpecifiedTypedValueArray(String[].class);
                for (final String selectedSection : selectedSections) {
                    sections.addSection(selectedSection);
                }
            }
            else if (sectionParamShell.hasSingleSpecifiedValue()) {
                sections.addSection((String) sectionParamShell.getSpecifiedValue());
            }
        }

        return getCapDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    @Override
	public String buildGetObservationRequest(final ParameterContainer parameters) throws OXFException {
        final GetObservationDocument xb_getObsDoc = GetObservationDocument.Factory.newInstance();
        final GetObservationType xb_getObs = xb_getObsDoc.addNewGetObservation();
        xb_getObs.setService((String) getShellForServerParameter(parameters, GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());
        xb_getObs.setVersion((String) getShellForServerParameter(parameters, GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());
        processOffering(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_OFFERING_PARAMETER));
        processResponseFormat(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
        processObservedProperty(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        processTemporalFilter(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_TEMPORAL_FILTER_PARAMETER));
        processProcedure(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_PROCEDURE_PARAMETER));
        processFeatureOfInterest(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
        processSpatialFilter(xb_getObs, getShellForServerParameter(parameters, GET_OBSERVATION_SPATIAL_FILTER_PARAMETER));
        return xb_getObsDoc.xmlText(XmlUtil.PRETTYPRINT);
    }
    
    private ParameterShell getShellForServerParameter(final ParameterContainer container, final String name) {
        return container.getParameterShellWithServiceSidedName(name);
    }

    private void processSpatialFilter(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        // TODO implement
        throw new NotImplementedException();
    }

    private void processFeatureOfInterest(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        final ParameterShell foiParamShell = shell;
        if (foiParamShell.hasMultipleSpecifiedValues()) {
            final String[] fois = foiParamShell.getSpecifiedTypedValueArray(String[].class);
            xb_getObs.setFeatureOfInterestArray(fois);
        }
        else {
            final Object foi = foiParamShell.getSpecifiedValue();
            xb_getObs.addNewFeatureOfInterest().setStringValue((String) foi);
        }
    }

    protected void processOffering(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell != null) { // optional parameter
            xb_getObs.setOfferingArray(shell.getSpecifiedTypedValueArray(String[].class));
        }
    }

    protected void processResponseFormat(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell != null) {
            xb_getObs.setResponseFormat((String) shell.getSpecifiedValue());
        }
    }

    protected void processProcedure(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell != null) { // optional parameter
            xb_getObs.setProcedureArray(shell.getSpecifiedTypedValueArray(String[].class));
        }
    }
    
    protected void processObservedProperty(final GetObservationType xb_getObs, final ParameterShell shell) {
        if (shell == null) {
            return; // optional parameter
        }
        final String[] observedProperties = shell.getSpecifiedTypedValueArray(String[].class);
        xb_getObs.setObservedPropertyArray(observedProperties);
    }

    protected void processTemporalFilter(final GetObservationType xb_getObs, final ParameterShell shell) throws OXFException {
        if (shell == null) {
            return; // optional parameter
        }
        
        ITime specifiedTime;
        final Object timeParamValue = shell.getSpecifiedValue();
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
            
            final ITimePeriod oc_timePeriod = (ITimePeriod) specifiedTime;
            final TimePeriodDocument timePeriodDoc = TimePeriodDocument.Factory.newInstance();
            final TimePeriodType xb_timePeriod = timePeriodDoc.addNewTimePeriod();
            final DuringDocument duringDoc = DuringDocument.Factory.newInstance();
            final BinaryTemporalOpType during = duringDoc.addNewDuring();
            
            final TimePositionType xb_beginPosition = xb_timePeriod.addNewBeginPosition();
            final TimePositionType xb_endPosition = xb_timePeriod.addNewEndPosition();
            xb_beginPosition.setStringValue(oc_timePeriod.getStart().toISO8601Format());
            xb_endPosition.setStringValue(oc_timePeriod.getEnd().toISO8601Format());

            xb_timePeriod.setId("_1");
            during.set(timePeriodDoc);
            during.setValueReference("phenomenonTime");

            final TemporalFilter spatialFilter = xb_getObs.addNewTemporalFilter();
            spatialFilter.set(duringDoc);
        }
        else if (specifiedTime instanceof ITimePosition) {
            final ITimePosition oc_timePosition = (ITimePosition) specifiedTime;
            final TimeInstantDocument timeInstanceDoc = TimeInstantDocument.Factory.newInstance();
            final TimeInstantType xb_timeInstant = timeInstanceDoc.addNewTimeInstant();
            final TEqualsDocument equalsDoc = TEqualsDocument.Factory.newInstance();
            final BinaryTemporalOpType equals = equalsDoc.addNewTEquals();
            
            final TimePositionType xb_timePosition = xb_timeInstant.addNewTimePosition();
            xb_timePosition.setStringValue(oc_timePosition.toISO8601Format());

            xb_timeInstant.setId("_1");
            equals.set(timeInstanceDoc);
            equals.setValueReference("phenomenonTime");
            
            final TemporalFilter spatialFilter = xb_getObs.addNewTemporalFilter();
            spatialFilter.set(equalsDoc);
        }
    }
    
    
    @Override
	public String buildGetObservationByIDRequest(final ParameterContainer parameters) throws OXFException {
        throw new NotImplementedException("SOS Specification 2.0 not officially supported by now.");
    }

    @Override
	public String buildDescribeSensorRequest(final ParameterContainer parameters) {
        final DescribeSensorDocument descSensorDoc = DescribeSensorDocument.Factory.newInstance();
        final DescribeSensorType descSensor = descSensorDoc.addNewDescribeSensor();

        // set required elements:

        descSensor.setService((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
        descSensor.setVersion((String) parameters.getParameterShellWithServiceSidedName(DESCRIBE_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
        processProcedure(descSensor, getShellForServerParameter(parameters, DESCRIBE_SENSOR_PROCEDURE_PARAMETER));
        processProcedureDescriptionFormat(descSensor, getShellForServerParameter(parameters, DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT));
        
        
        return descSensorDoc.xmlText(XmlUtil.PRETTYPRINT);
    }
    
    protected void processProcedureDescriptionFormat(final DescribeSensorType descSensor, final ParameterShell shell) {
        if (shell == null) {
            LOGGER.error("Missing shell parameter '" + DESCRIBE_SENSOR_PROCEDURE_DESCRIPTION_FORMAT + "'.");
            return; // throwing OXFException would break interface
        }
        descSensor.setProcedureDescriptionFormat((String) shell.getSpecifiedValue());
    }

    private void processProcedure(final DescribeSensorType xb_descSensor, final ParameterShell shell) {
        if (shell == null) {
            xb_descSensor.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/1.0.1");
        } else {
            xb_descSensor.setProcedure((String) shell.getSpecifiedValue());
        }
    }

    @Override
	public String buildGetFeatureOfInterestRequest(final ParameterContainer parameters) {
    	final GetFeatureOfInterestDocument xb_getFOIDoc = GetFeatureOfInterestDocument.Factory.newInstance();
    	final GetFeatureOfInterestType xb_getFOI = xb_getFOIDoc.addNewGetFeatureOfInterest();
    	xb_getFOI.setService((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_SERVICE_PARAMETER).getSpecifiedValue());
    	xb_getFOI.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_VERSION_PARAMETER).getSpecifiedValue());
    	xb_getFOI.addProcedure((String) parameters.getParameterShellWithServiceSidedName("procedure").getSpecifiedValue());
        return xb_getFOIDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    @Override
	public String buildInsertObservation(final ParameterContainer parameters) {
        // TODO implement
    	throw new NotImplementedException();
    }

    /**
     * Builds a <b>Insert</b>Sensor request and returns it.
     * A SensorML file can either be passed along or a set of parameters is used to create one.
     * @throws OXFException
     */
    @Override
	public String buildRegisterSensor(final ParameterContainer parameters) throws OXFException {
    	if (parameters == null) {
    		throw new OXFException(new IllegalArgumentException("ParameterContainer 'parameters' should not be null"));
    	}
        // TODO implement
    	final InsertSensorDocument xb_InsertSensorDoc = InsertSensorDocument.Factory.newInstance();
    	final InsertSensorType xb_InsertSensorType = xb_InsertSensorDoc.addNewInsertSensor();
    	// add version and service
    	xb_InsertSensorType.setService((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
    	xb_InsertSensorType.setVersion((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
    	// add observable property
    	// add procedure description format
    	// add procedure description
    	// add insertion metadata
    	return xb_InsertSensorDoc.xmlText(XmlUtil.PRETTYPRINT);
    }	
    
}