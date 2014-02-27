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
package org.n52.oxf.ses.util;

import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePositionType;
import net.opengis.om.x10.ObservationDocument;
import net.opengis.om.x10.ObservationType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingPointType;
import net.opengis.swe.x101.PhenomenonPropertyType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.valueDomains.time.TimeFactory;


public class OMParser {
	
	public static ParameterContainer parseOM(ObservationDocument document) throws Exception {
		ObservationType observation = null;
		ParameterContainer paramCon = new ParameterContainer();

		if (document != null) {
			observation = document.getObservation();
		} else throw new Exception("No Observation found!");


		// parse featureOfInterest
		FeaturePropertyType foi = observation.getFeatureOfInterest();
		SamplingPointType sa = SamplingPointType.Factory.parse(foi.toString());
		SamplingPointDocument doc = SamplingPointDocument.Factory.parse(sa.toString());
		
//		OXFFeature feature = OXFSamplingPointType.create(doc);// XXX TEMPORARY COMMENT TO LET IT COMPILE
		
		// position
//		double x = feature.getGeometry().getInteriorPoint().getCoordinate().x;// XXX TEMPORARY COMMENT TO LET IT COMPILE
//		double y = feature.getGeometry().getInteriorPoint().getCoordinate().y;// XXX TEMPORARY COMMENT TO LET IT COMPILE
//		paramCon.addParameterShell(SesLayerAdder.X_COORD, Double.toString(x));// XXX TEMPORARY COMMENT TO LET IT COMPILE
//		paramCon.addParameterShell(SesLayerAdder.Y_COORD, Double.toString(y));// XXX TEMPORARY COMMENT TO LET IT COMPILE
		
		// foiID
//		paramCon.addParameterShell(SesLayerAdder.FOI_ID, feature.getID());// XXX TEMPORARY COMMENT TO LET IT COMPILE
		
		// observedProperty
		PhenomenonPropertyType phType = observation.getObservedProperty();
//		paramCon.addParameterShell(SesLayerAdder.OBSERVED_PROPERTY, phType.getHref());// XXX TEMPORARY COMMENT TO LET IT COMPILE
		
		// samplingTime
		ITime time = null;
		if (observation.getSamplingTime().getTimeObject() != null) {
            XmlObject timeXo = observation.getSamplingTime().getTimeObject().newCursor().getObject();
            SchemaType timeSchemaType = timeXo.schemaType();

            if (timeSchemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                TimeInstantType xb_timeInstant = (TimeInstantType) timeXo;
                TimePositionType xb_timePosition = xb_timeInstant.getTimePosition();

                time = TimeFactory.createTime(xb_timePosition.getStringValue());
//                paramCon.addParameterShell(SesLayerAdder.SAMPLING_TIME, time.toISO8601Format());// XXX TEMPORARY COMMENT TO LET IT COMPILE
            }
		}
		// result
		String resultValue = observation.getResult().newCursor().getTextValue();
//		paramCon.addParameterShell(SesLayerAdder.RESULT_VALUE, resultValue);// XXX TEMPORARY COMMENT TO LET IT COMPILE
		
//		PhenomenonType b = (PhenomenonType) bla.getPhenomenon();
//		PhenomenonType type =  PhenomenonType.Factory.parse(bla.toString());
//		XmlObject result = observation.getResult();
//		PhenomenonPropertyType test = PhenomenonPropertyType.Factory.parse(observation.getObservedProperty().toString());
		
		return paramCon;
	}
}
