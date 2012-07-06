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

public class OXFFeatureType /*implements org.opengis.feature.FeatureType*/ {

    protected String typeName;

    protected List<OXFFeatureAttributeDescriptor> featureAttributeDescriptors;

    /**
     * 
     * @param typeName
     * @param featureAttributeDescriptors
     * @param defaultShapeAttribute
     */
    public OXFFeatureType(String typeName, List<OXFFeatureAttributeDescriptor> featureAttributeDescriptors) {
        this.typeName = typeName;
        this.featureAttributeDescriptors = featureAttributeDescriptors;
    }

    /**
     * 
     * @return
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 
     * @param attributeName
     * @return true, if there is an FeatureAttributeDescriptor which has got the specified attributeName.
     */
    public boolean hasAttribute(String attributeName) {
        for (OXFFeatureAttributeDescriptor attributeDesc : featureAttributeDescriptors) {
            if (attributeDesc.getName().equals(attributeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param attributeName
     * @return the FeatureAttributeDescriptor with the specified name; or <code>null</code> if there is no
     *         such FeatureAttributeDescriptor.
     */
    public OXFFeatureAttributeDescriptor getAttributeDescriptor(String attributeName) {
        for (OXFFeatureAttributeDescriptor attributeDesc : featureAttributeDescriptors) {
            if (attributeDesc.getName().equals(attributeName)) {
                return attributeDesc;
            }
        }
        // if none found:
        return null;
    }

    // inherited methods:

    /**
     * Returns a list of descriptors that lists all of the attributes that a {@link Feature} of this type will
     * have.
     */
    public List<OXFFeatureAttributeDescriptor> getAttributeDescriptors() {
        return featureAttributeDescriptors;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return "FeatureType:'" + typeName + "'";
    }
    

    /** not supported */
    public List getChildTypes() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    /** not supported */
    public OXFGeometryAttribute getDefaultShapeAttribute() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
}