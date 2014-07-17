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
package org.n52.oxf.sos.feature;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.n52.oxf.ows.capabilities.Parameter.COMMON_NAME_VERSION;

import java.io.InputStream;

import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.sos.ObservationSeriesCollection;
import org.n52.oxf.xmlbeans.tools.XmlFileLoader;

public class SOSObservationStoreTest {

    private static final String SOS_200_GETOBSERVATION_AGS_SOE_VALID = "/files/observationData/SOS_2.0.0_GetObservationResponse_ags_soe_valid.xml";

    private static final String SOS_200_GETOBSERVATION_HYPROFILE_WML20_VALID = "/files/observationData/SOS_2.0.0_GetObservationResponse_hyprofile_kiwis_valid.xml";

    @Test public void
    shouldParseValidAgsSoeResult()
    throws Exception
    {
        SOSObservationStore seam = new FeatureCollectionStoreSeam(SOS_200_GETOBSERVATION_AGS_SOE_VALID, "2.0.0");
        OXFFeatureCollection featureCollection = seam.unmarshalFeatures();
        String[] fois = new String[] {"http://cdr.eionet.europa.eu/gb/eu/aqd/e2a/colutn32a/envuvlxkq/D_GB_Sample.xml#GB_SamplingFeature_993"};
        String[] obsProps = new String[] {"http://dd.eionet.europa.eu/vocabulary/aq/pollutant/1"};
        ObservationSeriesCollection seriesCollection = new ObservationSeriesCollection(featureCollection, fois, obsProps, false);
        assertThat(seriesCollection.getSortedTimeArray().length, is(23));
    }

    @Test public void
    shouldParseValidHyProfileWML20Result()
    throws Exception
    {
        SOSObservationStore seam = new FeatureCollectionStoreSeam(SOS_200_GETOBSERVATION_HYPROFILE_WML20_VALID, "2.0.0");
        OXFFeatureCollection featureCollection = seam.unmarshalFeatures();
        String[] fois = new String[] {"http://kiwis.kisters.de/stations/6731310"};
        String[] obsProps = new String[] {"http://kiwis.kisters.de/parameters/Q"};
        ObservationSeriesCollection seriesCollection = new ObservationSeriesCollection(featureCollection, fois, obsProps, false);
        assertThat(seriesCollection.getSortedTimeArray().length, is(43));
    }

    @Before
    public void setUp() throws Exception {
    }

    private class FeatureCollectionStoreSeam extends SOSObservationStore {
        public FeatureCollectionStoreSeam(String file, String version) throws OXFException {
            super(createOperationResultFrom(file, version));
        }
    }

    private static OperationResult createOperationResultFrom(String file, String version) {
        try {
            ParameterContainer parameters = new ParameterContainer();
            parameters.addParameterShell(COMMON_NAME_VERSION, "2.0.0");
            InputStream stream = getTestFile(file).newInputStream();
            return new OperationResult(stream, parameters, "NA");
        } catch(Exception e) {
            fail("Could not instantiate test case.");
            return null;
        }
    }

    private static XmlObject getTestFile(String file) {
        try {
            return XmlFileLoader.loadXmlFileViaClassloader(file, SOSObservationStoreTest.class);
        } catch (Exception e) {
            fail("Could not load test file " + file);
            return null;
        }
    }
}
