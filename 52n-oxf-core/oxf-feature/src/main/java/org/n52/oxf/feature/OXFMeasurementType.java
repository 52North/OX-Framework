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
package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.MeasureType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFMeasureType;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.ows.capabilities.ITime;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFMeasurementType extends OXFAbstractObservationType {

    public static final String RESULT = "result";

    /**
     *
     */
    public OXFMeasurementType() {
        super();

        typeName = "OXFMeasurementType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     *
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor result = new OXFFeatureAttributeDescriptor(RESULT,
                                                                                 DataType.OBJECT,
                                                                                 OXFMeasureType.class,
                                                                                 1,
                                                                                 1,
                                                                                 "");
        attributeDescriptors.add(result);

        return attributeDescriptors;
    }

    /**
     * supports O&M 1.0:
     */
    public void initializeFeature(OXFFeature feature, net.opengis.om.x10.MeasurementType xb_measurement) throws OXFException {
        super.initializeFeature(feature, xb_measurement);

        // create the RESULT-attribute:

        XmlCursor c = xb_measurement.getResult().newCursor();
        try {
            MeasureType xb_result = MeasureType.Factory.parse(c.getDomNode());

            OXFMeasureType measureResult = new OXFMeasureType(xb_result.getUom(),
                                                              xb_result.getDoubleValue());
            feature.setAttribute(RESULT, measureResult);
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
    }

    /**
     *
     * @param feature
     * @param nameValue
     * @param descriptionValue
     * @param locationValue
     * @param timeValue
     * @param procedureValue
     * @param observedPropertyValue
     * @param featureOfInterestValue
     * @param resultValue
     */
    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue,
                                  ITime timeValue,
                                  String procedureValue,
                                  OXFPhenomenonPropertyType observedPropertyValue,
                                  OXFFeature featureOfInterestValue,
                                  OXFMeasureType resultValue) {
        super.initializeFeature(feature,
                                nameValue,
                                descriptionValue,
                                locationValue,
                                timeValue,
                                procedureValue,
                                observedPropertyValue,
                                featureOfInterestValue);

        feature.setAttribute(RESULT, resultValue);

    }
}
