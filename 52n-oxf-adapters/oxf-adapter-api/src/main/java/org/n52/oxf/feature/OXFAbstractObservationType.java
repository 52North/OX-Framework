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

package org.n52.oxf.feature;

import java.util.List;

import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.TimeInstantType;
import net.opengis.gml.TimePositionType;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.feature.dataTypes.OXFPhenomenonPropertyType;
import org.n52.oxf.feature.sos.SOSFoiStore;
import org.n52.oxf.owsCommon.capabilities.ITime;
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

        OXFFeature foi = new SOSFoiStore().parseFoi(xb_featureMember);
        
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