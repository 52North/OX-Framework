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