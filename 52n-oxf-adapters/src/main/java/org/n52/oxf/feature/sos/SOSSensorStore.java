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
 
 Created on: 29.01.2006
 *********************************************************************************/


package org.n52.oxf.feature.sos;

import java.io.IOException;

import net.opengis.sensorML.x101.AbstractProcessType;
import net.opengis.sensorML.x101.SensorMLDocument;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML;
import net.opengis.sensorML.x101.SensorMLDocument.SensorML.Member;
import net.opengis.sensorML.x101.SystemType;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.serviceAdapters.OperationResult;
import org.n52.oxf.util.LoggingHandler;

/**
 * 
 * Class to unmarshal a collection of OXFSensorTypes from an OperationResult, typically a 
 * SensorMLDocument containing a collection of SensorML SystemTypes.
 * 
 * @author <a href="mailto:daniel.nuest@uni-muenster.de">Daniel Nüst</a>
 * 
 */
public class SOSSensorStore implements IFeatureStore {

    public static final String SENSOR_ML_FEATURE_ID = "sml_id";
    public static final String SENSOR_ML_FEATURE_COLLECTION_ID = "sml_id";

    private static Logger LOGGER = LoggingHandler.getLogger(SOSSensorStore.class);

    public OXFFeatureCollection unmarshalFeatures(OperationResult opsRes) throws OXFException {
        if (LOGGER.isDebugEnabled()) {
            String incoming = new String(opsRes.getIncomingResult());
            LOGGER.debug("starting to unmarshal features, incoming is:\n" + incoming);
        }

        // parse incoming to SensorML document
        SensorMLDocument xb_smlDoc;
        try {
            xb_smlDoc = SensorMLDocument.Factory.parse(opsRes.getIncomingResultAsStream());
        }
        catch (XmlException e) {
            LOGGER.error("xml error parsing SensorML document", e);
            throw new OXFException(e);
        }
        catch (IOException e) {
            LOGGER.error("io error parsing SensorML document", e);
            throw new OXFException(e);
        }

        SensorML xb_sml = xb_smlDoc.getSensorML();
        Member[] xb_memberArray = xb_sml.getMemberArray();

        // wrap in feature collection
        OXFFeatureCollection oxf_featureCollection = new OXFFeatureCollection(SENSOR_ML_FEATURE_COLLECTION_ID,
                                                                              new OXFSensorType());

        for (Member member : xb_memberArray) {
            AbstractProcessType xb_proc = member.getProcess();
            if (xb_proc instanceof SystemType) {
                SystemType xb_system = (SystemType) xb_proc;

                // extract feature
                OXFFeature feature = parseSensor(xb_system);

                oxf_featureCollection.add(feature);
            }
        }

        return oxf_featureCollection;
    }

    /**
     * Can be used to parse a single feature entity to an OXFFeature object.
     * 
     * @throws OXFException
     */
    public OXFFeature parseSensor(SystemType xb_system) throws OXFException {
        OXFFeature feature = null;
        feature = OXFSensorType.create(xb_system);
        return feature;
    }

    /**
     * 
     * @param opResult
     * @return
     * @throws OXFException 
     */
    public OXFFeatureCollection unmarshalFeaturesFromHistory(OperationResult opResult) throws OXFException {
        // TODO Auto-generated method stub
        return unmarshalFeatures(opResult);
    }

	public OXFFeatureCollection unmarshalFeatures() throws OXFException {
		// FIXME Implement
		throw new RuntimeException("NOT YET IMPLEMENTED");
	}

}
