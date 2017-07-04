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
package org.n52.oxf.sos.examples;

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.*;
import static org.n52.oxf.sos.adapter.SOSAdapter.REGISTER_SENSOR;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;

@Ignore // comment out to run demo class via JUnit
public class RegisterSensorRequestExample extends SosAdapterRequestExample {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        // should return an exception when transactional interface is
        // not supported of public
        performOperationParseResult(createRegisterSensorOperation());
    }

    private Operation createRegisterSensorOperation() {
        return new Operation(REGISTER_SENSOR, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        final ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(SERVICE, "SOS");
        parameters.addParameterShell(VERSION, "1.0.0");
        parameters.addParameterShell(REGISTER_SENSOR_ID_PARAMETER, "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1");
        parameters.addParameterShell(REGISTER_SENSOR_ML_DOC_PARAMETER, SENSOR_ML_DUMMY);
        parameters.addParameterShell(REGISTER_SENSOR_LATITUDE_POSITION_PARAMETER, "52.90");
        parameters.addParameterShell(REGISTER_SENSOR_LONGITUDE_POSITION_PARAMETER, "7.52");
        parameters.addParameterShell(REGISTER_SENSOR_OBSERVED_PROPERTY_PARAMETER, "height");
        parameters.addParameterShell(REGISTER_SENSOR_UOM_PARAMETER, "cm");
        parameters.addParameterShell(REGISTER_SENSOR_OBSERVATION_TEMPLATE, OBSERVATION_TEMPLATE_DUMMY);
        parameters.addParameterShell(REGISTER_SENSOR_OBSERVATION_TYPE, REGISTER_SENSOR_OBSERVATION_TYPE_MEASUREMENT);
        return parameters;
    }

    private static final String SENSOR_ML_DUMMY = "<sml:SensorML version=\"1.0.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:swe=\"http://www.opengis.net/swe/1.0.1\" xmlns:sml=\"http://www.opengis.net/sensorML/1.0.1\">\r\n" +
            "        <sml:member>\r\n" +
            "        <sml:System xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"   >\r\n" +
            "              \r\n" +
            "            <!--sml:identification element must contain the ID of the sensor-->\r\n" +
            "            <sml:identification>\r\n" +
            "                <sml:IdentifierList>\r\n" +
            "                    <sml:identifier>\r\n" +
            "                        <sml:Term definition=\"urn:ogc:def:identifier:OGC:uniqueID\">\r\n" +
            "                            <sml:value>urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1</sml:value>\r\n" +
            "                        </sml:Term>\r\n" +
            "                    </sml:identifier>\r\n" +
            "                  </sml:IdentifierList>\r\n" +
            "            </sml:identification>\r\n" +
            "              \r\n" +
            "            <!-- last measured position of sensor -->\r\n" +
            "            <sml:position name=\"sensorPosition\">\r\n" +
            "                <swe:Position referenceFrame=\"urn:ogc:def:crs:EPSG::4326\">\r\n" +
            "                    <swe:location>\r\n" +
            "                        <swe:Vector gml:id=\"STATION_LOCATION\">\r\n" +
            "                            <swe:coordinate name=\"easting\">\r\n" +
            "                                <swe:Quantity>\r\n" +
            "                                    <swe:uom code=\"degree\"/>\r\n" +
            "                                    <swe:value>7.52</swe:value>\r\n" +
            "                                </swe:Quantity>\r\n" +
            "                            </swe:coordinate>\r\n" +
            "                            <swe:coordinate name=\"northing\">\r\n" +
            "                                <swe:Quantity>\r\n" +
            "                                    <swe:uom code=\"degree\"/>\r\n" +
            "                                    <swe:value>52.90</swe:value>\r\n" +
            "                                </swe:Quantity>\r\n" +
            "                            </swe:coordinate>\r\n" +
            "                            <swe:coordinate name=\"altitude\">\r\n" +
            "                                <swe:Quantity>\r\n" +
            "                                    <swe:uom code=\"m\"/>\r\n" +
            "                                    <swe:value>52.0</swe:value>\r\n" +
            "                                </swe:Quantity>\r\n" +
            "                            </swe:coordinate>\r\n" +
            "                        </swe:Vector>\r\n" +
            "                    </swe:location>\r\n" +
            "                </swe:Position>\r\n" +
            "            </sml:position>\r\n" +
            "              \r\n" +
            "              <!-- list containing the input phenomena for this sensor system -->\r\n" +
            "              <sml:inputs>\r\n" +
            "                   <sml:InputList>\r\n" +
            "                        <sml:input name=\"waterlevel\">\r\n" +
            "                             <swe:ObservableProperty definition=\"test_observable_property_6\"/>\r\n" +
            "                        </sml:input>\r\n" +
            "                   </sml:InputList>\r\n" +
            "              </sml:inputs>\r\n" +
            "              \r\n" +
            "              <!-- list containing the output phenomena of this sensor system; ATTENTION: these phenomena are parsed and inserted into the database; they have to contain offering elements to determine the correct offering for the sensors and measured phenomena -->\r\n" +
            "              <sml:outputs>\r\n" +
            "                   <sml:OutputList>\r\n" +
            "                        <sml:output name=\"waterlevel\">\r\n" +
            "                             <swe:Quantity definition=\"test_observable_property_6\">\r\n" +
            "                                    <gml:metaDataProperty>\r\n" +
            "                                            <offering>\r\n" +
            "                                                <id>test_offering_6</id>\r\n" +
            "                                                <name>gauge height in Muenster</name>\r\n" +
            "                                            </offering>\r\n" +
            "                                    </gml:metaDataProperty>\r\n" +
            "                                  <swe:uom code=\"cm\"/>\r\n" +
            "                             </swe:Quantity>\r\n" +
            "                        </sml:output>\r\n" +
            "                   </sml:OutputList>\r\n" +
            "              </sml:outputs>\r\n" +
            "              \r\n" +
            "              <!-- description of components of this sensor system; these are currently not used by the 52N SOS -->\r\n" +
            "              <sml:components>\r\n" +
            "                   <sml:ComponentList>\r\n" +
            "                        <sml:component name=\"gaugeSensor\">\r\n" +
            "                             <sml:Component>\r\n" +
            "                                  <sml:identification>\r\n" +
            "                                       <sml:IdentifierList>\r\n" +
            "                                            <sml:identifier>\r\n" +
            "                                                 <sml:Term definition=\"urn:ogc:def:identifier:OGC:uniqueID\">\r\n" +
            "                                                      <sml:value>urn:ogc:object:feature:Sensor:water_level_sensor</sml:value>\r\n" +
            "                                                 </sml:Term>\r\n" +
            "                                            </sml:identifier>\r\n" +
            "                                       </sml:IdentifierList>\r\n" +
            "                                  </sml:identification>\r\n" +
            "                                  <sml:inputs>\r\n" +
            "                                       <sml:InputList>\r\n" +
            "                                            <sml:input name=\"gaugeHeight\">\r\n" +
            "                                                 <swe:ObservableProperty definition=\"test_observable_property_6\"/>\r\n" +
            "                                            </sml:input>\r\n" +
            "                                       </sml:InputList>\r\n" +
            "                                  </sml:inputs>\r\n" +
            "                                  <sml:outputs>\r\n" +
            "                                       <sml:OutputList>\r\n" +
            "                                            <sml:output name=\"gaugeHeight\">\r\n" +
            "                                                 <swe:Quantity definition=\"test_observable_property_6\">\r\n" +
            "                                                      <swe:uom code=\"cm\"/>\r\n" +
            "                                                 </swe:Quantity>\r\n" +
            "                                            </sml:output>\r\n" +
            "                                       </sml:OutputList>\r\n" +
            "                                  </sml:outputs>\r\n" +
            "                             </sml:Component>\r\n" +
            "                        </sml:component>\r\n" +
            "                   </sml:ComponentList>\r\n" +
            "              </sml:components>\r\n" +
            "         </sml:System>\r\n" +
            "         </sml:member>\r\n" +
            "         </sml:SensorML>";

    private static final String OBSERVATION_TEMPLATE_DUMMY = "<om:Measurement " +
            "  xmlns:om=\"http://www.opengis.net/om/1.0\">\r\n" +
            "    <om:samplingTime/>\r\n" +
            "    <om:procedure/>\r\n" +
            "    <om:observedProperty/>\r\n" +
            "    <om:featureOfInterest></om:featureOfInterest>\r\n" +
            "    <om:result uom=\"\" >0.0</om:result>\r\n" +
            "</om:Measurement>";

}
