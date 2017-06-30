/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.oxf.sos.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.n52.oxf.OXFException;
import org.n52.oxf.OXFRuntimeException;
import org.n52.oxf.feature.DataType;
import org.n52.oxf.feature.OXFAbstractFeatureType;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureAttributeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import net.opengis.sensorML.x101.CapabilitiesDocument.Capabilities;
import net.opengis.sensorML.x101.HistoryDocument.History;
import net.opengis.sensorML.x101.IdentificationDocument.Identification;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList;
import net.opengis.sensorML.x101.IdentificationDocument.Identification.IdentifierList.Identifier;
import net.opengis.sensorML.x101.InputsDocument.Inputs.InputList;
import net.opengis.sensorML.x101.IoComponentPropertyType;
import net.opengis.sensorML.x101.OutputsDocument.Outputs.OutputList;
import net.opengis.sensorML.x101.PositionDocument.Position;
import net.opengis.sensorML.x101.SystemType;
import net.opengis.sensorML.x101.TermDocument.Term;
import net.opengis.swe.x101.AbstractDataRecordType;
import net.opengis.swe.x101.AnyScalarPropertyType;
import net.opengis.swe.x101.DataComponentPropertyType;
import net.opengis.swe.x101.DataRecordType;
import net.opengis.swe.x101.PositionType;
import net.opengis.swe.x101.SimpleDataRecordType;
import net.opengis.swe.x101.VectorPropertyType;
import net.opengis.swe.x101.VectorType;

/**
 * Wrapper class for the SensorML System type.
 */
public class OXFSensorType extends OXFAbstractFeatureType {

    private static final Logger LOGGER = LoggerFactory.getLogger(OXFSensorType.class);

    public static final String POSITION = "oxfSensorTypePosition";

    public static final String HISTORY = "oxfSensorTypeHistory";

    public static final String ID = "oxfSensorTypeId";

    public static final String ACTIVE = "oxfSensorTypeActive";

    public static final String MOBILE = "oxfSensorTypeMobile";

    public static final Object ACTIVE_PARAMETER_NAME = "status";

    public static final Object MOBILE_PARAMETER_NAME = "mobile";

    public static final String INPUTS = "oxfSensorTypeInputs";

    public static final String OUTPUTS = "oxfSensorTypeOutputs";

    public static final List<String> X_AXIS_IDENTIFIERS = Collections.unmodifiableList(
	    new ArrayList<String>(Arrays.asList(new String[] { "x", "xcoord", "easting", "longitude" })));

    public static final List<String> Y_AXIS_IDENTIFIERS = Collections.unmodifiableList(
	    new ArrayList<String>(Arrays.asList(new String[] { "y", "ycoord", "northing", "latitude" })));

    public static final List<String> Z_AXIS_IDENTIFIERS = Collections.unmodifiableList(
	    new ArrayList<String>(Arrays.asList(new String[] { "z", "zcoord", "altitude", "elevation" })));


    public OXFSensorType() {
        super();
        typeName = "OXFSensorType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.n52.oxf.feature.OXFAbstractFeatureType#generateAttributeDescriptors()
     */
    @Override
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        final List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();
        // location (abstract feature type)

        // TODO should this attribute not have the DataType.GEOMETRY
        final OXFFeatureAttributeDescriptor position = new OXFFeatureAttributeDescriptor(POSITION,
                                                                                   DataType.OBJECT,
                                                                                   Point.class,
                                                                                   1,
                                                                                   1,
                                                                                   "The Position as a com.vividsolutions.jts.geom.Point.");
        attributeDescriptors.add(position);

        final OXFFeatureAttributeDescriptor history = new OXFFeatureAttributeDescriptor(HISTORY,
                                                                                  DataType.OBJECT,
                                                                                  History[].class,
                                                                                  0,
                                                                                  1,
                                                                                  "The history as an array of net.opengis.sensorML.x101.HistoryDocument.History.");
        attributeDescriptors.add(history);

        final OXFFeatureAttributeDescriptor id = new OXFFeatureAttributeDescriptor(ID,
                                                                             DataType.STRING,
                                                                             String.class,
                                                                             1,
                                                                             1,
                                                                             "The identification string of a sensor.");
        attributeDescriptors.add(id);

        final OXFFeatureAttributeDescriptor active = new OXFFeatureAttributeDescriptor(ACTIVE,
                                                                                 DataType.OBJECT,
                                                                                 Boolean.class,
                                                                                 1,
                                                                                 1,
                                                                                 "The activity attribute of a sensor as a Boolean.");
        attributeDescriptors.add(active);

        final OXFFeatureAttributeDescriptor mobile = new OXFFeatureAttributeDescriptor(MOBILE,
                                                                                 DataType.OBJECT,
                                                                                 Boolean.class,
                                                                                 1,
                                                                                 1,
                                                                                 "The mobility attribute of a sensor as a Boolean.");
        attributeDescriptors.add(mobile);

        final OXFFeatureAttributeDescriptor inputs = new OXFFeatureAttributeDescriptor(INPUTS,
                                                                                 DataType.OBJECT,
                                                                                 IoComponentPropertyType[].class,
                                                                                 0,
                                                                                 1,
                                                                                 "The inputs of a sensor as an array of IoComponentPropertyType.");
        attributeDescriptors.add(inputs);

        final OXFFeatureAttributeDescriptor outputs = new OXFFeatureAttributeDescriptor(OUTPUTS,
                                                                                  DataType.OBJECT,
                                                                                  IoComponentPropertyType[].class,
                                                                                  0,
                                                                                  1,
                                                                                  "The outputs of a sensor as an array of IoComponentPropertyType.");
        attributeDescriptors.add(outputs);

        return attributeDescriptors;
    }

    /**
     *
     * @param sensorMlDoc
     * @return
     * @throws OXFException
     */
    public static OXFFeature create(final SystemType xb_system) throws OXFException {

        /** id **/
        final String id = getId(xb_system);

        // FeatureType of the feature:
        final OXFSensorType oxf_sensorType = new OXFSensorType();

        final OXFFeature feature = new OXFFeature(id, oxf_sensorType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_sensorType.initializeFeature(feature, xb_system);

        return feature;

    }

    /**
     *
     * @param xbSensor
     * @return
     * @throws OXFException
     */
    private static String getId(final SystemType xbSensor) throws OXFException {

        final Identification[] xbIdentificationArray = xbSensor.getIdentificationArray();
        if (xbIdentificationArray.length > 0) {
            if (xbIdentificationArray.length > 1) {
                LOGGER.warn("just the first sml:IdentifierList of the identification array will be used!");
            }
            final IdentifierList xbIdentifiersList = xbIdentificationArray[0].getIdentifierList();
            if (xbIdentifiersList.sizeOfIdentifierArray() > 0) {
                if (xbIdentifiersList.sizeOfIdentifierArray() > 1) {
                    LOGGER.warn("just the first sml:identifier of the identification array will be used!");
                }
                final Identifier xbIdentifier = xbIdentifiersList.getIdentifierArray(0);
                final Term term = xbIdentifier.getTerm();
                return term.getValue();
            }
        }
        LOGGER.error("no id found in sml:IdentifierList, trying fallback solution...");
        if (xbSensor.getId() != null) {
            return xbSensor.getId();
        }
        throw new OXFException("no identifier could be found in the given System");
    }

    /**
     *
     * @param feature
     * @param xb_sensor
     * @throws OXFException
     * @throws ClassCastException
     * @throws IllegalArgumentException
     */
    public void initializeFeature(final OXFFeature feature, final SystemType xb_sensor) throws IllegalArgumentException,
            ClassCastException,
            OXFException {
        super.initializeFeature(feature, xb_sensor);

        feature.setAttribute(ID, getId(xb_sensor));

        // are all "null":
        // Location xb_loc = xb_sensor.getLocation();
        // ValidTime xb_validTime = xb_sensor.getValidTime();
        // TimePosition xb_timepos = xb_sensor.getTimePosition();
        // StringOrRefType xb_description = xb_sensor.getDescription();
        // SpatialReferenceFrame xb_srs = xb_sensor.getSpatialReferenceFrame();
        // MultimapRequestParameters xb_parameters = xb_sensor.getParameters();

        /** history **/
        final History[] xb_history = xb_sensor.getHistoryArray();
        feature.setAttribute(HISTORY, xb_history);

        /** position **/
        initializePosition(feature, xb_sensor);

        /** mobile and active **/
        // SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER
        final Capabilities[] xb_capsArray = xb_sensor.getCapabilitiesArray();

        for (final Capabilities capabilities : xb_capsArray) {
            final AbstractDataRecordType xb_abstractDataRecord = capabilities.getAbstractDataRecord();
            final QName qName = new QName("http://www.opengis.net/swe/1.0.1", "SimpleDataRecord");
            if (xb_abstractDataRecord instanceof SimpleDataRecordType) {
                final SimpleDataRecordType simpleDataRec = (SimpleDataRecordType) xb_abstractDataRecord.substitute(qName, SimpleDataRecordType.type);
                final AnyScalarPropertyType[] xb_fieldArray = simpleDataRec.getFieldArray();
                for (final AnyScalarPropertyType anyScalarPropertyType : xb_fieldArray) {
                    final String name = anyScalarPropertyType.getName();
                    if (name.equals(ACTIVE_PARAMETER_NAME)) {
                        final boolean active = anyScalarPropertyType.getBoolean()
                                .getValue();
                        feature.setAttribute(ACTIVE, Boolean.valueOf(active));
                    }
                    if (name.equals(MOBILE_PARAMETER_NAME)) {
                        final boolean mobile = anyScalarPropertyType.getBoolean()
                                .getValue();
                        feature.setAttribute(MOBILE, Boolean.valueOf(mobile));
                    }
                }
            } else if (xb_abstractDataRecord instanceof DataRecordType) {
                final DataRecordType simpleDataRec = (DataRecordType) xb_abstractDataRecord
                        .substitute(qName, DataRecordType.type);
                final DataComponentPropertyType[] xb_fieldArray = simpleDataRec
                        .getFieldArray();
                for (final DataComponentPropertyType dataComponent : xb_fieldArray) {
                    final String name = dataComponent.getName();
                    if (name.equals(ACTIVE_PARAMETER_NAME)) {
                        final boolean active = dataComponent.getBoolean()
                                .getValue();
                        feature.setAttribute(ACTIVE, Boolean.valueOf(active));
                    }
                    if (name.equals(MOBILE_PARAMETER_NAME)) {
                        final boolean mobile = dataComponent.getBoolean()
                                .getValue();
                        feature.setAttribute(MOBILE, Boolean.valueOf(mobile));
                    }
                }
            }
        }

        /** input list **/
        final InputList inputList = xb_sensor.getInputs().getInputList();
        final IoComponentPropertyType[] inputArray = inputList.getInputArray();
        feature.setAttribute(INPUTS, inputArray);

        /** output list **/
        final OutputList outputList = xb_sensor.getOutputs().getOutputList();
        final IoComponentPropertyType[] outputArray = outputList.getOutputArray();
        feature.setAttribute(OUTPUTS, outputArray);
    }

    private void initializePosition(final OXFFeature feature, final SystemType xb_sensor) {
        if (xb_sensor.getPosition() != null && xb_sensor.getPosition().getPosition() != null
                && xb_sensor.getPosition().getPosition().getLocation() != null) {

            final Position xb_smlPostion = xb_sensor.getPosition();
            final PositionType xb_swePosition = xb_smlPostion.getPosition();
            final VectorPropertyType xb_sweLocation = xb_swePosition.getLocation();

            final VectorType xb_sweVector = xb_sweLocation.getVector();

            final net.opengis.swe.x101.VectorType.Coordinate[] xb_sweCoordinates = xb_sweVector.getCoordinateArray();

            double xCoord = Double.NaN;
            double yCoord = Double.NaN;
            double zCoord = Double.NaN;
            for (final net.opengis.swe.x101.VectorType.Coordinate coord : xb_sweCoordinates) {
                if (X_AXIS_IDENTIFIERS.contains(coord.getName())) {
                    xCoord = coord.getQuantity().getValue();
                }
                else if (Y_AXIS_IDENTIFIERS.contains(coord.getName())) {
                    yCoord = coord.getQuantity().getValue();
                }
                else if (Z_AXIS_IDENTIFIERS.contains(coord.getName())) {
                    zCoord = coord.getQuantity().getValue();
                }
            }
            if (xb_sweCoordinates.length < 2 || xb_sweCoordinates.length > 3) {
                LOGGER.error("There are 2 or 3 coordinates required.");
                throw new OXFRuntimeException("Error parsing SensorML document - There are 2 or 3 coordinates required in sensor position definition.");
            }
            if (Double.isNaN(xCoord) || Double.isNaN(yCoord) || (xb_sweCoordinates.length == 3 && Double.isNaN(zCoord))) {
                LOGGER.error("There are 2 or 3 coordinates required, given:\n" + xb_sweVector.xmlText());
                throw new OXFRuntimeException("Error parsing SensorML document - coordinates definitions are required to have named swe:coordinate elements."
                        + " Allowed names for x; y; and z axis respectively: "
                        + Arrays.toString(X_AXIS_IDENTIFIERS.toArray())
                        + "; "
                        + Arrays.toString(Y_AXIS_IDENTIFIERS.toArray())
                        + "; "
                        + Arrays.toString(Z_AXIS_IDENTIFIERS.toArray()) + ".");
            }

            final Point point = new GeometryFactory().createPoint(new Coordinate(xCoord, yCoord, zCoord));

            final String srsString = xb_swePosition.getReferenceFrame();
            point.setSRID(Integer.parseInt(srsString.substring(srsString.lastIndexOf(':') + 1)));

            feature.setAttribute(POSITION, point);
            initializeFeaturesGeometry(feature, point);
        }

        // check whether the geometry-attribute was set: (could be set in this
        // or the super class)
        if (feature.getGeometry() == null) {
            throw new IllegalArgumentException("The geometry attribute could not be initialized.");
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
    }

}
