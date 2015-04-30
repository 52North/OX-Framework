/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
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

import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_OUTPUT_FORMAT;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_PROCEDURE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_SERVICE_PARAMETER;
import static org.n52.oxf.sos.adapter.ISOSRequestBuilder.DESCRIBE_SENSOR_VERSION_PARAMETER;
import static org.n52.oxf.sos.adapter.SOSAdapter.DESCRIBE_SENSOR;

import java.io.IOException;

import net.opengis.sensorML.x101.SensorMLDocument;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.sml.util.SmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore // comment out to run demo class via JUnit
public class DescribeSensorRequestExample extends SosAdapterRequestExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(DescribeSensorRequestExample.class);

    private static final String PROCEDURE_WASSERSTAND = "Wasserstand-Ledasperrwerk_Up_3880050";

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void describeSensor() {
        performOperationParseResult(createDescribeSensorOperation());
    }

    private Operation createDescribeSensorOperation() {
        return new Operation(DESCRIBE_SENSOR, getServiceGETUrl(), getServicePOSTUrl());
    }

    @Override
    protected ParameterContainer createParameterContainer() throws OXFException {
        ParameterContainer parameters = new ParameterContainer();
        parameters.addParameterShell(DESCRIBE_SENSOR_SERVICE_PARAMETER, "SOS");
        parameters.addParameterShell(DESCRIBE_SENSOR_VERSION_PARAMETER, "1.0.0");
        parameters.addParameterShell(DESCRIBE_SENSOR_OUTPUT_FORMAT, "text/xml;subtype=\"sensorML/1.0.1\"");
        parameters.addParameterShell(DESCRIBE_SENSOR_PROCEDURE_PARAMETER, PROCEDURE_WASSERSTAND);
        return parameters;
    }

    @Test
    public void parseSosSensorStoreFromSensorML() {
        try {
            SmlHelper smlHelper = new SmlHelper();
            OperationResult result = performOperation(createDescribeSensorOperation());
            XmlObject xmlResponse = XmlObject.Factory.parse(result.getIncomingResultAsAutoCloseStream());
            SensorMLDocument smlDoc = smlHelper.getSmlFrom(xmlResponse);

            String longName = smlHelper.getIdValueFrom("longName", smlHelper.getIdentification(smlDoc));
            LOGGER.info("longName: {}", longName);

//            SOSSensorStore store = new SOSSensorStore(result);
//            OXFFeatureCollection featureCollection = store.unmarshalFeatures();
//            for (OXFFeature oxfFeature : featureCollection.toSet()) {
//                LOGGER.info("OXFFeature: {}", oxfFeature.toString());
//                LOGGER.info("oxfSensorTypePosition: {}", oxfFeature.getAttribute("oxfSensorTypePosition"));
//                LOGGER.info("oxfSensorTypeId: {}", oxfFeature.getAttribute("oxfSensorTypeId"));
//                LOGGER.info("description: {}", oxfFeature.getAttribute("description"));
//
//            }
        }
        catch (XmlException e) {
            LOGGER.error("Could not parse XML.", e);
        }
        catch (IOException e) {
            LOGGER.error("Could not read response.", e);
        }
    }


}
