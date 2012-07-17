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

import net.opengis.gml.AbstractFeatureCollectionType;
import net.opengis.gml.FeaturePropertyType;

import org.n52.oxf.OXFException;
import org.n52.oxf.feature.sos.FeatureStore;

public class OXFAbstractFeatureCollectionType extends OXFAbstractFeatureType {
    
    public OXFAbstractFeatureCollectionType() {
        super();
        
        typeName = "OXFAbstractFeatureCollectionType";
        featureAttributeDescriptors = generateAttributeDescriptors();
    }

    protected List<OXFFeatureAttributeDescriptor> generateAttributeDescriptors() {

        List<OXFFeatureAttributeDescriptor> attributeDescriptors = super.generateAttributeDescriptors();

        /**
         * the "featureMember" attribute is realized through the features-attribute in the class
         * <code>OXFFeatureCollection<code><br>
         * <br>
         * so use the <code>add</code>-methods in <code>OXFFeatureCollection<code>-class to add members
         * to the FeatureCollection.
         */

        return attributeDescriptors;
    }

    /**
     * 
     */
    public void initializeFeature(OXFFeatureCollection featureCollection,
                                  AbstractFeatureCollectionType xb_featureCollection) throws OXFException {
        super.initializeFeature(featureCollection, xb_featureCollection);

        for (FeaturePropertyType xb_featureMember : xb_featureCollection.getFeatureMemberArray()) {

            // the feature to add:
            OXFFeature feature = new FeatureStore().parseFoi(xb_featureMember);
            featureCollection.add(feature);
        }
    }
}