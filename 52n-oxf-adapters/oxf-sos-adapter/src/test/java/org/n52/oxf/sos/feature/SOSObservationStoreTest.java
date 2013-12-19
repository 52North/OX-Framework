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
