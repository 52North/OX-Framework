/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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

import net.opengis.swe.x101.ScopedNameType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.feature.dataTypes.OXFScopedName;
import org.n52.oxf.ows.capabilities.ITime;

import com.vividsolutions.jts.geom.Geometry;

public class OXFCategoryObservationType extends OXFAbstractObservationType {
    
    public static final String RESULT = "result";

    /**
     * 
     */
    public OXFCategoryObservationType() {
        super();

        typeName = "OXFCategoryObservationType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     * 
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor result = new OXFFeatureAttributeDescriptor(RESULT,
                                                                                 DataType.OBJECT,
                                                                                 OXFScopedName.class,
                                                                                 1,
                                                                                 1,
                                                                                 "");
        attributeDescriptors.add(result);

        return attributeDescriptors;
    }

    /**
     * supports O&M 1.0:
     */
    public void initializeFeature(OXFFeature feature, net.opengis.om.x10.CategoryObservationType xb_categoryObservation) throws OXFException {
        super.initializeFeature(feature, xb_categoryObservation);

        // create the RESULT-attribute:
        XmlCursor c = xb_categoryObservation.getResult().newCursor();
        try {
            ScopedNameType xb_result = ScopedNameType.Factory.parse(c.getDomNode());

            OXFScopedName oxf_scopedName = new OXFScopedName(xb_result.getCodeSpace(), xb_result.getStringValue());

            feature.setAttribute(RESULT, oxf_scopedName);
        }
        catch (XmlException e) {
            throw new OXFException(e);
        }
    }

    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue,
                                  ITime timeValue,
                                  String procedureValue,
                                  OXFPhenomenonPropertyType observedPropertyValue,
                                  OXFFeature featureOfInterestValue,
                                  OXFScopedName resultValue) {
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