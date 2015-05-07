/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePositionType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.ows.capabilities.ITime;
import org.n52.oxf.valueDomains.time.TimeFactory;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFAbstractObservationType extends OXFAbstractFeatureType {

    public static final String PROCEDURE = "procedure";
    public static final String OBSERVED_PROPERTY = "observedProperty";
    public static final String FEATURE_OF_INTEREST = "featureOfInterest";
    public static final String SAMPLING_TIME = "samplingTime";
    
    
    /**
     * 
     */
    public OXFAbstractObservationType() {
        super();

        typeName = "OXFAbstractObservationType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    /**
     * 
     */
    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        OXFFeatureAttributeDescriptor procedure = new OXFFeatureAttributeDescriptor(PROCEDURE,
                                                                                    DataType.STRING,
                                                                                    String.class,
                                                                                    1,
                                                                                    1,
                                                                                    "");
        attributeDescriptors.add(procedure);

        OXFFeatureAttributeDescriptor observedProperty = new OXFFeatureAttributeDescriptor(OBSERVED_PROPERTY,
                                                                                           DataType.OBJECT,
                                                                                           OXFPhenomenonPropertyType.class,
                                                                                           1,
                                                                                           1,
                                                                                           "");
        attributeDescriptors.add(observedProperty);

        OXFFeatureAttributeDescriptor featureOfInterest = new OXFFeatureAttributeDescriptor(FEATURE_OF_INTEREST,
                                                                                            DataType.OBJECT,
                                                                                            OXFFeature.class,
                                                                                            1,
                                                                                            1,
                                                                                            "");
        attributeDescriptors.add(featureOfInterest);
        
        OXFFeatureAttributeDescriptor time = new OXFFeatureAttributeDescriptor(SAMPLING_TIME,
                                                                               DataType.OBJECT,
                                                                               ITime.class,
                                                                               1,
                                                                               1,
                                                                               "");
        attributeDescriptors.add(time);

        return attributeDescriptors;
    }
    
    /**
     * supports O&M 1.0
     */
    public void initializeFeature(OXFFeature feature,
                                  net.opengis.om.x10.ObservationType xb_abstractObservation) throws OXFException {
        super.initializeFeature(feature, xb_abstractObservation);

        
        // create the PROCEDURE-attribute:
        net.opengis.om.x10.ProcessPropertyType xb_procedureProperty = xb_abstractObservation.getProcedure();
        String procedureValue = xb_procedureProperty.getHref();
        feature.setAttribute(PROCEDURE, procedureValue);

        
        // create the OBSERVEDPROPERTY-attribute:
        net.opengis.swe.x101.PhenomenonPropertyType xb_observedProperty = xb_abstractObservation.getObservedProperty();
        OXFPhenomenonPropertyType oxf_observedProperty = new OXFPhenomenonPropertyType(xb_observedProperty.getHref());
        feature.setAttribute(OBSERVED_PROPERTY, oxf_observedProperty);

        
        // create the SAMPLINGTIME-attribute:
        if (xb_abstractObservation.getSamplingTime().getTimeObject() != null) {
            XmlObject timeXo = xb_abstractObservation.getSamplingTime().getTimeObject().newCursor().getObject();
            SchemaType timeSchemaType = timeXo.schemaType();

            ITime time = null;
            if (timeSchemaType.getJavaClass().isAssignableFrom(TimeInstantType.class)) {
                TimeInstantType xb_timeInstant = (TimeInstantType) timeXo;
                TimePositionType xb_timePosition = xb_timeInstant.getTimePosition();

                time = TimeFactory.createTime(xb_timePosition.getStringValue());
            }
            else {
                throw new OXFException("The schema type "
                        + timeSchemaType
                        + " is currently not supported as a substitution for 'gml:_TimeObject'-type");
            }
            feature.setAttribute(SAMPLING_TIME, time);
        }
        
        
        // create the FEATUREOFINTEREST-attribute:
        FeaturePropertyType xb_featureMember = xb_abstractObservation.getFeatureOfInterest();

        OXFFeature foi = OXFFeature.createFrom(xb_featureMember);
        
        feature.setAttribute(FEATURE_OF_INTEREST, foi);
    }
    
    
    public void initializeFeature(OXFFeature feature,
                                  String[] nameValue,
                                  String descriptionValue,
                                  Geometry locationValue,
                                  ITime timeValue,
                                  String procedureValue,
                                  OXFPhenomenonPropertyType observedPropertyValue,
                                  OXFFeature featureOfInterestValue) {
        super.initializeFeature(feature, nameValue, descriptionValue, locationValue);
        
        feature.setAttribute(PROCEDURE, procedureValue);
        feature.setAttribute(OBSERVED_PROPERTY, observedPropertyValue);
        feature.setAttribute(FEATURE_OF_INTEREST, featureOfInterestValue);
        feature.setAttribute(SAMPLING_TIME, timeValue);
    }
}