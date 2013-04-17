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

package org.n52.oxf.sos.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

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

    public static final ArrayList<String> X_AXIS_IDENTIFIERS = new ArrayList<String>(Arrays.asList(new String[] {"x",
                                                                                                                 "xcoord",
                                                                                                                 "easting",
                                                                                                                 "longitude"}));

    public static final ArrayList<String> Y_AXIS_IDENTIFIERS = new ArrayList<String>(Arrays.asList(new String[] {"y",
                                                                                                                 "ycoord",
                                                                                                                 "northing",
                                                                                                                 "latitude"}));

    public static final ArrayList<String> Z_AXIS_IDENTIFIERS = new ArrayList<String>(Arrays.asList(new String[] {"z",
                                                                                                                 "zcoord",
                                                                                                                 "altitude",
                                                                                                                 "elevation"}));

    /**
	 * 
	 */
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

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();
        // location (abstract feature type)

        // TODO should this attribute not have the DataType.GEOMETRY
        OXFFeatureAttributeDescriptor position = new OXFFeatureAttributeDescriptor(POSITION,
                                                                                   DataType.OBJECT,
                                                                                   Point.class,
                                                                                   1,
                                                                                   1,
                                                                                   "The Position as a com.vividsolutions.jts.geom.Point.");
        attributeDescriptors.add(position);

        OXFFeatureAttributeDescriptor history = new OXFFeatureAttributeDescriptor(HISTORY,
                                                                                  DataType.OBJECT,
                                                                                  History[].class,
                                                                                  0,
                                                                                  1,
                                                                                  "The history as an array of net.opengis.sensorML.x101.HistoryDocument.History.");
        attributeDescriptors.add(history);

        OXFFeatureAttributeDescriptor id = new OXFFeatureAttributeDescriptor(ID,
                                                                             DataType.STRING,
                                                                             String.class,
                                                                             1,
                                                                             1,
                                                                             "The identification string of a sensor.");
        attributeDescriptors.add(id);

        OXFFeatureAttributeDescriptor active = new OXFFeatureAttributeDescriptor(ACTIVE,
                                                                                 DataType.OBJECT,
                                                                                 Boolean.class,
                                                                                 1,
                                                                                 1,
                                                                                 "The activity attribute of a sensor as a Boolean.");
        attributeDescriptors.add(active);

        OXFFeatureAttributeDescriptor mobile = new OXFFeatureAttributeDescriptor(MOBILE,
                                                                                 DataType.OBJECT,
                                                                                 Boolean.class,
                                                                                 1,
                                                                                 1,
                                                                                 "The mobility attribute of a sensor as a Boolean.");
        attributeDescriptors.add(mobile);

        OXFFeatureAttributeDescriptor inputs = new OXFFeatureAttributeDescriptor(INPUTS,
                                                                                 DataType.OBJECT,
                                                                                 IoComponentPropertyType[].class,
                                                                                 0,
                                                                                 1,
                                                                                 "The inputs of a sensor as an array of IoComponentPropertyType.");
        attributeDescriptors.add(inputs);

        OXFFeatureAttributeDescriptor outputs = new OXFFeatureAttributeDescriptor(OUTPUTS,
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
    public static OXFFeature create(SystemType xb_system) throws OXFException {

        /** id **/
        String id = getId(xb_system);

        // FeatureType of the feature:
        OXFSensorType oxf_sensorType = new OXFSensorType();

        OXFFeature feature = new OXFFeature(id, oxf_sensorType);

        // initialize the feature with the attributes from the XMLBean:
        oxf_sensorType.initializeFeature(feature, xb_system);

        return feature;

    }

    /**
     * 
     * @param xb_sensor
     * @return
     * @throws OXFException
     */
    private static String getId(SystemType xb_sensor) throws OXFException {

        try {
            Identification[] xb_identificatinArray = xb_sensor.getIdentificationArray();
            if (xb_identificatinArray.length > 1) {
                LOGGER.warn("just the first sml:IdentifierList of the identification array will be used!");
            }
            IdentifierList xb_identifiersList = xb_identificatinArray[0].getIdentifierList();
            if (xb_identifiersList.sizeOfIdentifierArray() > 1) {
                LOGGER.warn("just the first sml:identifier of the identification array will be used!");
            }
            Identifier xb_identifier = xb_identifiersList.getIdentifierArray(0);
            Term term = xb_identifier.getTerm();
            return term.getValue();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.error("no id found in sml:IdentifierList, trying fallback solution...", e);
            if (xb_sensor.getId() != null) {
                return xb_sensor.getId();
            }
            throw new OXFException("no identifier could be found in the given System");
        }
    }

    /**
     * 
     * @param feature
     * @param xb_sensor
     * @throws OXFException
     * @throws ClassCastException
     * @throws IllegalArgumentException
     */
    public void initializeFeature(OXFFeature feature, SystemType xb_sensor) throws IllegalArgumentException,
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
        History[] xb_history = xb_sensor.getHistoryArray();
        feature.setAttribute(HISTORY, xb_history);

        /** position **/
        initializePosition(feature, xb_sensor);

        /** mobile and active **/
        // SOSmobileRequestBuilder.REGISTER_SENSOR_MOBILE_PARAMETER
        Capabilities[] xb_capsArray = xb_sensor.getCapabilitiesArray();

        for (Capabilities capabilities : xb_capsArray) {
            AbstractDataRecordType xb_abstractDataRecord = capabilities.getAbstractDataRecord();
            QName qName = new QName("http://www.opengis.net/swe/1.0.1", "SimpleDataRecord");
            if (xb_abstractDataRecord instanceof SimpleDataRecordType) {
				SimpleDataRecordType simpleDataRec = (SimpleDataRecordType) xb_abstractDataRecord.substitute(qName, SimpleDataRecordType.type);
				AnyScalarPropertyType[] xb_fieldArray = simpleDataRec.getFieldArray();
				for (AnyScalarPropertyType anyScalarPropertyType : xb_fieldArray) {
					String name = anyScalarPropertyType.getName();
					if (name.equals(ACTIVE_PARAMETER_NAME)) {
						boolean active = anyScalarPropertyType.getBoolean()
								.getValue();
						feature.setAttribute(ACTIVE, Boolean.valueOf(active));
					}
					if (name.equals(MOBILE_PARAMETER_NAME)) {
						boolean mobile = anyScalarPropertyType.getBoolean()
								.getValue();
						feature.setAttribute(MOBILE, Boolean.valueOf(mobile));
					}
				}
			} else if (xb_abstractDataRecord instanceof DataRecordType) {
				DataRecordType simpleDataRec = (DataRecordType) xb_abstractDataRecord
						.substitute(qName, DataRecordType.type);
				DataComponentPropertyType[] xb_fieldArray = simpleDataRec
						.getFieldArray();
				for (DataComponentPropertyType dataComponent : xb_fieldArray) {
					String name = dataComponent.getName();
					if (name.equals(ACTIVE_PARAMETER_NAME)) {
						boolean active = dataComponent.getBoolean()
								.getValue();
						feature.setAttribute(ACTIVE, Boolean.valueOf(active));
					}
					if (name.equals(MOBILE_PARAMETER_NAME)) {
						boolean mobile = dataComponent.getBoolean()
								.getValue();
						feature.setAttribute(MOBILE, Boolean.valueOf(mobile));
					}
				}
			}
        }

        /** input list **/
        InputList inputList = xb_sensor.getInputs().getInputList();
        IoComponentPropertyType[] inputArray = inputList.getInputArray();
        feature.setAttribute(INPUTS, inputArray);

        /** output list **/
        OutputList outputList = xb_sensor.getOutputs().getOutputList();
        IoComponentPropertyType[] outputArray = outputList.getOutputArray();
        feature.setAttribute(OUTPUTS, outputArray);
    }

    private void initializePosition(OXFFeature feature, SystemType xb_sensor) {
        if (xb_sensor.getPosition() != null && xb_sensor.getPosition().getPosition() != null
                && xb_sensor.getPosition().getPosition().getLocation() != null) {

            Position xb_smlPostion = xb_sensor.getPosition();
            PositionType xb_swePosition = xb_smlPostion.getPosition();
            VectorPropertyType xb_sweLocation = xb_swePosition.getLocation();

            VectorType xb_sweVector = xb_sweLocation.getVector();

            net.opengis.swe.x101.VectorType.Coordinate[] xb_sweCoordinates = xb_sweVector.getCoordinateArray();

            double xCoord = Double.NaN;
            double yCoord = Double.NaN;
            double zCoord = Double.NaN;
            for (net.opengis.swe.x101.VectorType.Coordinate coord : xb_sweCoordinates) {
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

            Point point = new GeometryFactory().createPoint(new Coordinate(xCoord, yCoord, zCoord));

            String srsString = xb_swePosition.getReferenceFrame();
            point.setSRID(Integer.parseInt(srsString.substring(srsString.lastIndexOf(":") + 1)));

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
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        return sb.toString();
    }

}
