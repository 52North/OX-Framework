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

import static org.n52.oxf.xml.XMLConstants.*;

import java.math.BigInteger;

import net.opengis.fes.x20.BinaryTemporalOpType;
import net.opengis.fes.x20.DuringDocument;
import net.opengis.fes.x20.TEqualsDocument;
import net.opengis.gml.x32.AbstractTimeObjectType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.MeasureType;
import net.opengis.gml.x32.PointDocument;
import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.ReferenceType;
import net.opengis.gml.x32.TimeInstantDocument;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodDocument;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.om.x20.OMObservationType;
import net.opengis.ows.x11.AcceptVersionsType;
import net.opengis.ows.x11.SectionsType;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.sos.x20.GetCapabilitiesDocument;
import net.opengis.sos.x20.GetCapabilitiesType;
import net.opengis.sos.x20.GetFeatureOfInterestDocument;
import net.opengis.sos.x20.GetFeatureOfInterestType;
import net.opengis.sos.x20.GetObservationDocument;
import net.opengis.sos.x20.GetObservationType;
import net.opengis.sos.x20.GetObservationType.TemporalFilter;
import net.opengis.sos.x20.InsertObservationDocument;
import net.opengis.sos.x20.InsertObservationType;
import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;

import org.apache.commons.lang.NotImplementedException;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.valueDomains.time.ITimePeriod;
import org.n52.oxf.valueDomains.time.ITimePosition;
import org.n52.oxf.valueDomains.time.TimeFactory;
import org.n52.oxf.valueDomains.time.TimePeriod;
import org.n52.oxf.valueDomains.time.TimePosition;
import org.n52.oxf.xml.XMLConstants;
import org.n52.oxf.xmlbeans.tools.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains attributes and methods to encode SOSOperationRequests as String in xml-format
 * 
 * TODO: add java doc for each public method including mandatory and optional parameters like {@link SOSRequestBuilder_100}.
 * TODO: add validation to each request creation
 * 
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
                final String[] versionArray = versionPS.getSpecifiedTypedValueArray(String[].class);
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
        final GetObservationDocument xbGetObsDoc = GetObservationDocument.Factory.newInstance();
        final GetObservationType xbGetObs = xbGetObsDoc.addNewGetObservation();
        xbGetObs.setService((String) getShellForServerParameter(parameters, GET_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());
        xbGetObs.setVersion((String) getShellForServerParameter(parameters, GET_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());
        processOffering(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_OFFERING_PARAMETER));
        processResponseFormat(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_RESPONSE_FORMAT_PARAMETER));
        processObservedProperty(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_OBSERVED_PROPERTY_PARAMETER));
        processTemporalFilter(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_TEMPORAL_FILTER_PARAMETER));
        processProcedure(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_PROCEDURE_PARAMETER));
        processFeatureOfInterest(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_FEATURE_OF_INTEREST_PARAMETER));
        processSpatialFilter(xbGetObs, getShellForServerParameter(parameters, GET_OBSERVATION_SPATIAL_FILTER_PARAMETER));
        return xbGetObsDoc.xmlText(XmlUtil.PRETTYPRINT);
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
        throw new OXFException(new NotImplementedException("SOS 2.0 GetObservationById not implemented."));
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
    	final GetFeatureOfInterestDocument xbGetFOIDoc = GetFeatureOfInterestDocument.Factory.newInstance();
    	final GetFeatureOfInterestType xb_getFOI = xbGetFOIDoc.addNewGetFeatureOfInterest();
    	xb_getFOI.setService((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_SERVICE_PARAMETER).getSpecifiedValue());
    	xb_getFOI.setVersion((String) parameters.getParameterShellWithServiceSidedName(GET_FOI_VERSION_PARAMETER).getSpecifiedValue());
    	xb_getFOI.addProcedure((String) parameters.getParameterShellWithServiceSidedName("procedure").getSpecifiedValue());
        return xbGetFOIDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

    /**
     * <ul><li>TODO implement support for OM_SWEArrayObservation</li>
     * <li>TODO implement support for request extensions</li>
     * <li>TODO implement feature of interest as in document instance (SpatialSamplingPoint)</li>
     * <li>TODO implement referencing of duplicate values: result/phenomenonTime; foi instance</ul>
     */
    @Override
	public String buildInsertObservation(final ParameterContainer parameters) throws OXFException {
    	checkParameterContainer(parameters);
    	final InsertObservationDocument xbInsertObservationDocument = InsertObservationDocument.Factory.newInstance();
    	final InsertObservationType xbInsertObservationType = xbInsertObservationDocument.addNewInsertObservation();
    	addMetadata(parameters, xbInsertObservationType);
    	addObservations(parameters, xbInsertObservationType);
		addOfferings(parameters, xbInsertObservationType);
    	return xbInsertObservationDocument.xmlText(XmlUtil.PRETTYPRINT);
    }

	private void addMetadata(final ParameterContainer parameters,
			final InsertObservationType xbInsertObservationType)
	{
		xbInsertObservationType.setVersion((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VERSION_PARAMETER).getSpecifiedValue());
    	xbInsertObservationType.setService((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_SERVICE_PARAMETER).getSpecifiedValue());
	}

	private void addObservations(final ParameterContainer parameters,
			final InsertObservationType xbInsertObservationType) throws OXFException
	{
		// TODO add 1..n observation(s)
    	// add observation
    	final OMObservationType xbObservation = addObservationType(parameters, xbInsertObservationType);
    	xbObservation.setId("observation");
		addProcedure(parameters, xbObservation);
		addObservedProperty(parameters, xbObservation);
		addFeatureOfInterest(parameters, xbObservation);
		addResultTime(parameters, xbObservation);
		addPhenomenonTime(parameters, xbObservation);
		// add result 
		addResult(parameters,xbObservation);
	}

	private void addResult(final ParameterContainer parameters,
			final OMObservationType xbObservation) throws OXFException
	{
		if (xbObservation.getType().getHref().equals(OGC_OM_2_0_OM_MEASUREMENT)) {
			final String value = (String) parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue();
			final String uom = (String) parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_UOM_ATTRIBUTE).getSpecifiedValue();
			final MeasureType xbResult = MeasureType.Factory.newInstance();
			xbResult.setStringValue(value);
			xbResult.setUom(uom);
			xbObservation.setResult(xbResult);
		} 
		else if (xbObservation.getType().getHref().equals(OGC_OM_2_0_OM_CATEGORY_OBSERVATION)) {
			final ReferenceType xbCategory = ReferenceType.Factory.newInstance();
			xbCategory.setHref((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue());
			xbObservation.setResult(xbCategory);
		}
		else if (xbObservation.getType().getHref().equals(OGC_OM_2_0_OM_TRUTH_OBSERVATION)) {
			final XmlBoolean xbBoolean = XmlBoolean.Factory.newInstance();
			xbBoolean.setBooleanValue(
					Boolean.parseBoolean(
							(String)parameters
							.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER)
							.getSpecifiedValue()));
			xbObservation.setResult(xbBoolean);
		}
		else if (xbObservation.getType().getHref().equals(OGC_OM_2_0_OM_TEXT_OBSERVATION)) {
			final XmlString xbString = XmlString.Factory.newInstance();
			xbString.setStringValue((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue());
			xbObservation.setResult(xbString);
		}
		else if (xbObservation.getType().getHref().equals(OGC_OM_2_0_OM_COUNT_OBSERVATION)) {
			final XmlInteger xbInteger = XmlInteger.Factory.newInstance();
			xbInteger.setBigIntegerValue(new BigInteger(parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_VALUE_PARAMETER).getSpecifiedValue().toString()));
			xbObservation.setResult(xbInteger);
		}
		else { 
			final String errorMsg = String.format("Observation Type '%s' not supported.", xbObservation.getType().getHref());
			LOGGER.error(errorMsg);
			throw new OXFException(errorMsg);
		}
	}

	private OMObservationType addObservationType(final ParameterContainer parameters,
			final InsertObservationType xbInsertObservationType) throws OXFException
	{
		// add observation type identifier
    	final OMObservationType xbObservation = xbInsertObservationType.addNewObservation().addNewOMObservation();
    	final String observationType = (String) parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_TYPE).getSpecifiedValue();
		xbObservation.addNewType().setHref(getObservationTypeIdentifier(observationType));
		return xbObservation;
	}

	private void addPhenomenonTime(final ParameterContainer parameters,
			final OMObservationType xbObservation) throws OXFException
	{
		// add phenomenonTime
		final Object phenomenonTimeObj = parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_PHENOMENON_TIME).getSpecifiedValue();
		// add as reference
		if (isObjStringAndInDocumentReference(phenomenonTimeObj)) {
			xbObservation.addNewPhenomenonTime().setHref((String) phenomenonTimeObj);
		}
		else if (phenomenonTimeObj instanceof TimePosition) {
			addTimePosition(xbObservation, phenomenonTimeObj);
		}
		else if (phenomenonTimeObj instanceof TimePeriod) {
			addTimePeriod(xbObservation, phenomenonTimeObj);
		}
		else {
			throw new OXFException(
					String.format("Unsupported type for phenTime not yet implemented. Received type: %s",
							phenomenonTimeObj!=null?phenomenonTimeObj.getClass().getName():phenomenonTimeObj));
		}
	}

	private void addTimePeriod(final OMObservationType xbObservation,
			final Object phenomenonTimeObj)
	{
		final AbstractTimeObjectType timeObject = xbObservation.addNewPhenomenonTime().addNewAbstractTimeObject();
		final TimePeriodType timePeriod = 
				(TimePeriodType) timeObject.
				substitute(XMLConstants.QNAME_GML_3_2_TIME_PERIOD,
						TimePeriodType.type);
		timePeriod.addNewBeginPosition().setStringValue(((TimePeriod) phenomenonTimeObj).getStart().toISO8601Format());
		timePeriod.addNewEndPosition().setStringValue(((TimePeriod) phenomenonTimeObj).getEnd().toISO8601Format());
		timePeriod.setId("phenomenonTime");
	}

	private void addTimePosition(final OMObservationType xbObservation,
			final Object phenomenonTimeObj)
	{
		final AbstractTimeObjectType timeObject = xbObservation.addNewPhenomenonTime().addNewAbstractTimeObject();
		final TimeInstantType timeInstant = 
				(TimeInstantType) timeObject.
				substitute(XMLConstants.QNAME_GML_3_2_TIMEINSTANT,
						TimeInstantType.type);
		final TimePositionType timePos = timeInstant.addNewTimePosition();
		timePos.setStringValue(((TimePosition) phenomenonTimeObj).toISO8601Format());
		timeInstant.setId("phenomenonTime");
	}

	private void addFeatureOfInterest(final ParameterContainer parameters,
			final OMObservationType xbObservation) throws OXFException
	{
		// add feature
		if (parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER) == null) {
			xbObservation.addNewFeatureOfInterest().setHref((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_FOI_ID_PARAMETER).getSpecifiedValue());
		}
		else {// TODO add new feature instance
			// create new spatial sampling feature from values
			final SFSpatialSamplingFeatureDocument xbFeatureDoc = SFSpatialSamplingFeatureDocument.Factory.newInstance();
			final SFSpatialSamplingFeatureType xbSpatialSamplingFeature = xbFeatureDoc.addNewSFSpatialSamplingFeature();//SFSpatialSamplingFeatureType.Factory.newInstance();
			xbSpatialSamplingFeature.setId("ssf");
			xbSpatialSamplingFeature.addNewIdentifier().setStringValue((String) parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_ID_PARAMETER).getSpecifiedValue());
			xbSpatialSamplingFeature.addNewName().setStringValue((String) parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_NAME).getSpecifiedValue());
			xbSpatialSamplingFeature.addNewType().setHref(OGC_OM_2_0_SF_SAMPLING_POINT);
			// add optional parent feature id
			if (parameters.containsParameterShellWithCommonName(INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID) || parameters.containsParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID)) {
				xbSpatialSamplingFeature.addNewSampledFeature().setHref((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_PARENT_FEATURE_ID).getSpecifiedValue());
			} 
			else {
				xbSpatialSamplingFeature.addNewSampledFeature().setHref(XMLConstants.OGC_UNKNOWN_VALUE);
			}
			final PointDocument xbGmlPointDoc = PointDocument.Factory.newInstance();
			final PointType xbGmlPoint = xbGmlPointDoc.addNewPoint();
			xbGmlPoint.setId("ssf_point");
			final DirectPositionType pos = xbGmlPoint.addNewPos();
			pos.setSrsName(OGC_URI_START_CRS + (String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_POSITION_SRS).getSpecifiedValue());
			pos.setStringValue((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_NEW_FOI_POSITION).getSpecifiedValue());
			// add position
			xbSpatialSamplingFeature.addNewShape().set(xbGmlPointDoc);
			
			// add to xbObservation
			xbObservation.addNewFeatureOfInterest().set(xbFeatureDoc);
		} 
	}

	private void addOfferings(final ParameterContainer parameters,
			final InsertObservationType xbInsertObservationType)
	{
		// add offerings
    	final ParameterShell offeringsPS = parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_OFFERINGS_PARAMETER);
    	if (offeringsPS != null) {
    		if (offeringsPS.hasSingleSpecifiedValue()) {
    			xbInsertObservationType.addOffering((String) offeringsPS.getSpecifiedValue());
    		}
    		else {
    			final String[] offerings = offeringsPS.getSpecifiedTypedValueArray(String[].class);
    			for (final String offering : offerings) {
    				xbInsertObservationType.addOffering(offering);
    			}
    		}
    	}
	}

	private void addProcedure(final ParameterContainer parameters,
			final OMObservationType xbObservation)
	{
		// add procedure identifier
		xbObservation.addNewProcedure().setHref((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_PROCEDURE_PARAMETER).getSpecifiedValue());
	}

	private void addObservedProperty(final ParameterContainer parameters,
			final OMObservationType xbObservation)
	{
		// add observed property
		xbObservation.addNewObservedProperty().setHref((String)parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_OBSERVED_PROPERTY_PARAMETER).getSpecifiedValue());
	}

	private void addResultTime(final ParameterContainer parameters,
			final OMObservationType xbObservation)
	{
		// add resultTime
		final Object resultTimeObj = parameters.getParameterShellWithServiceSidedName(INSERT_OBSERVATION_RESULT_TIME).getSpecifiedValue();
		if (resultTimeObj instanceof TimePosition) {
			final TimeInstantType resultTime = xbObservation.addNewResultTime().addNewTimeInstant();
			resultTime.addNewTimePosition().setStringValue(((TimePosition)resultTimeObj).toISO8601Format());
			resultTime.setId("resultTime");
		}
		else if (isObjStringAndInDocumentReference(resultTimeObj)){ // add resultTime as reference
			xbObservation.addNewResultTime().setHref((String) resultTimeObj);
		}
	}

	private boolean isObjStringAndInDocumentReference(final Object resultTimeObj)
	{
		return resultTimeObj instanceof String && resultTimeObj.toString().startsWith("#");
	}
    
	private String getObservationTypeIdentifier(final String observationType) throws OXFException
	{
		if (observationType.equals(INSERT_OBSERVATION_TYPE_MEASUREMENT)) {
			return OGC_OM_2_0_OM_MEASUREMENT;
		}
		else if (observationType.equals(INSERT_OBSERVATION_TYPE_CATEGORY)) {
			return OGC_OM_2_0_OM_CATEGORY_OBSERVATION;
		}
		else if (observationType.equals(INSERT_OBSERVATION_TYPE_TRUTH)) {
			return OGC_OM_2_0_OM_TRUTH_OBSERVATION;
		}
		else if (observationType.equals(INSERT_OBSERVATION_TYPE_TEXT)) {
			return OGC_OM_2_0_OM_TEXT_OBSERVATION;
		}
		else if (observationType.equals(INSERT_OBSERVATION_TYPE_COUNT)) {
			return OGC_OM_2_0_OM_COUNT_OBSERVATION;
		}
		final String errorMsg = String.format("Observation Type '%s' not supported.", observationType);
		LOGGER.error(errorMsg);
		throw new OXFException(errorMsg);
	}

	/**
     * Delegate to be SOS conform regarding operation names. For more details, see
     * {@link #buildRegisterSensor(ParameterContainer)}. 
     * @see {@link #buildRegisterSensor(ParameterContainer)}
     */
    public String buildInsertSensor(final ParameterContainer parameters) throws OXFException {
    	return buildRegisterSensor(parameters);
    }

    /**
     * Builds a <b>Insert</b>Sensor request and returns it.
     * A SensorML document MUST be passed using <tt>ISOSRequestBuilder.REGISTER_SENSOR_ML_DOC_PARAMETER</tt>.
     * @throws OXFException
     */
    @Override
	public String buildRegisterSensor(final ParameterContainer parameters) throws OXFException {
    	checkParameterContainer(parameters);
    	final InsertSensorDocument xbInsertSensorDoc = InsertSensorDocument.Factory.newInstance();
    	final InsertSensorType xbInsertSensorType = xbInsertSensorDoc.addNewInsertSensor();
    	
    	addOperationMetadata(parameters, xbInsertSensorType);
    	addObservableProperties(parameters, xbInsertSensorType);
    	addProcedure(parameters, xbInsertSensorType);
    	addInsertionMetadata(parameters, xbInsertSensorType);
    	
    	return xbInsertSensorDoc.xmlText(XmlUtil.PRETTYPRINT);
    }

	private void checkParameterContainer(final ParameterContainer parameters) throws OXFException
	{
		if (parameters == null) {
    		throw new OXFException(new IllegalArgumentException("ParameterContainer 'parameters' should not be null"));
    	}
	}

	private void addInsertionMetadata(final ParameterContainer parameters,
			final InsertSensorType xbInsertSensorType)
	{
		// add insertion metadata
		SosInsertionMetadataType xbSosInsertionMetadata = null;
		// add observation types
		final ParameterShell obsTypeS = parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_OBSERVATION_TYPE);
		if (obsTypeS != null) {
			xbSosInsertionMetadata = SosInsertionMetadataType.Factory.newInstance();
			if (obsTypeS.hasSingleSpecifiedValue()) {
				xbSosInsertionMetadata.addObservationType((String)obsTypeS.getSpecifiedValue());
			}
			else {
				final String[] obsTypes = obsTypeS.getSpecifiedTypedValueArray(String[].class);
				for (final String obsType : obsTypes) {
					xbSosInsertionMetadata.addObservationType(obsType);
				}
			}
		}
		// add feature types
		final ParameterShell foiTypeS = parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_FEATURE_TYPE_PARAMETER);
		if (foiTypeS != null) {
			if (xbSosInsertionMetadata == null) {
				xbSosInsertionMetadata = SosInsertionMetadataType.Factory.newInstance();
			}
			if (foiTypeS.hasSingleSpecifiedValue()) {
				xbSosInsertionMetadata.addFeatureOfInterestType((String)foiTypeS.getSpecifiedValue());
			}
			else {
				final String[] foiTypes = foiTypeS.getSpecifiedTypedValueArray(String[].class);
				for (final String foiType : foiTypes) {
					xbSosInsertionMetadata.addFeatureOfInterestType(foiType);
				}
			}
		}
		if (xbSosInsertionMetadata != null) {
			xbInsertSensorType.addNewMetadata().setInsertionMetadata(xbSosInsertionMetadata);
		}
	}

	private void addProcedure(final ParameterContainer parameters,
			final InsertSensorType xbInsertSensorType) throws OXFException
	{
		// add procedure description format
    	xbInsertSensorType.setProcedureDescriptionFormat((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_PROCEDURE_DESCRIPTION_FORMAT_PARAMETER).getSpecifiedValue());
    	
    	// add procedure description
    	XmlObject xbObj;
		try {
			xbObj = XmlObject.Factory.parse((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_ML_DOC_PARAMETER).getSpecifiedValue());
			xbInsertSensorType.addNewProcedureDescription().set(xbObj);
		} catch (final XmlException e) {
			final String errorMsg = "Error while parsing MANDATORY parameter 'procedure description'!";
			LOGGER.error("{} Exception message: {}", errorMsg, e.getMessage(), e);
			throw new OXFException(errorMsg, e);
		}
	}

	private void addObservableProperties(final ParameterContainer parameters,
			final InsertSensorType xbInsertSensorType)
	{
		// add observable property
    	final ParameterShell observedPropertyPS = parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER);
    	if (observedPropertyPS != null) {
    		if (observedPropertyPS.hasSingleSpecifiedValue()) {
    			xbInsertSensorType.addObservableProperty((String) observedPropertyPS.getSpecifiedValue());
    		}
    		else {
    			final String[] properties = observedPropertyPS.getSpecifiedTypedValueArray(String[].class);
    			for (final String property : properties) {
    				xbInsertSensorType.addObservableProperty(property);
    			}
    		}
    	}
	}

	private void addOperationMetadata(final ParameterContainer parameters,
			final InsertSensorType xbInsertSensorType)
	{
		// add version and service
    	xbInsertSensorType.setService((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_SERVICE_PARAMETER).getSpecifiedValue());
    	xbInsertSensorType.setVersion((String) parameters.getParameterShellWithServiceSidedName(REGISTER_SENSOR_VERSION_PARAMETER).getSpecifiedValue());
	}
    
}