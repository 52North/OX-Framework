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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.gml.FeaturePropertyType;
import net.opengis.gml.x32.PointType;
import net.opengis.sampling.x10.SamplingPointDocument;
import net.opengis.sampling.x10.SamplingSurfaceDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;
import net.opengis.samplingSpatial.x20.ShapeType;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;

import com.vividsolutions.jts.geom.Geometry;

import de.bafg.grdc.sampling.x10.GrdcSamplingPointDocument;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFFeature /*implements org.opengis.feature.Feature*/ {

    /**
     * reference to an existing immutable (-> final) schema reference (<code>FeatureType</code>). This
     * reference should not be altered after a feature has been created.
     */
    private final OXFFeatureType featureType;

    /**
     * All features contain zero or more attributes, which can have one or more occurrences inside the
     * feature. Attributes may be any valid Java object. A feature should never reach a state where its
     * attributes (or sub-attributes) do not conform to their FeatureType definitions.
     */
    private HashMap<String, Object> attributeMap;

    /**
     * this attribute uniquely identifies this {@code Feature} instance.
     */
    private String id;

    /**
     * the collection in which this Feature is contained.
     */
    private OXFFeatureCollection parent = null;

    /**
     * 
     */
    private Geometry geom = null;

    /**
     * 
     * 
     */
    public OXFFeature(String id, OXFFeatureType featureType) {
        this.id = id;
        this.featureType = featureType;

        attributeMap = new HashMap<String, Object>();
    }

    /**
     * 
     * 
     */
    public OXFFeature(String id, OXFFeatureType featureType, OXFFeatureCollection parent) {
        this(id, featureType);

        this.parent = parent;
    }

    /**
     * 
     * @param feature
     * @return true if the specified feature is an OXFFeature and not null and the IDs of both features are
     *         equal.
     */
    @Override
    public boolean equals(Object feature) {
        if (feature != null && feature instanceof OXFFeature
                && getID().equals( ((OXFFeature) feature).getID())) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 
     * @return the names of the attributes contained by the attributeMap of this <code>OXFFeature</code>.
     */
    public String[] getSpecifiedAttributes() {
        Set<String> keySet = attributeMap.keySet();
        String[] keys = new String[keySet.size()];

        keySet.toArray(keys);

        return keys;
    }

    // inherited methods

    /**
     * Returns the description of this feature's type.
     */
    public OXFFeatureType getFeatureType() {
        return featureType;
    }

    /**
     * Returns the value of the named attribute of this {@code Feature}. If the maximum cardinality of this
     * attribute is one, then this method returns the value of the attribute. Otherwise, if the maximum
     * cardinality of this attribute is greater than one, then this method will return an instance of
     * {@link Collection}.
     * 
     * @param name
     *        The name of the feature attribute to retrieve.
     * 
     * @throws IllegalArgumentException
     *         If an attribute of the given name does not exist in this feature's type.
     */
    public Object getAttribute(String name) throws IllegalArgumentException {
        if ( !featureType.hasAttribute(name)) {
            throw new IllegalArgumentException("FeatureType '" + getFeatureType() + "' hasn't got attribute '" + name + "'");
        }
        else {
            return attributeMap.get(name);
        }
    }

    /**
     * Sets the value of the named attribute. The value can either be a single object, if the maximum
     * cardinality of the given attribute is one, or the value can be an instance of {@link Collection} if the
     * attribute's maximum cardinality is greater than one. <br>
     * <br>
     * If the attributeMap previously contained a mapping for the specified name, the old value is replaced.
     * 
     * @param name
     *        The name of the attribute whose value to set.
     * @param value
     *        The new value of the given attribute.
     * 
     * @throws IllegalArgumentException
     *         If {@code value} is a collection (other than a {@linkplain Collections#singleton singleton})
     *         and it's a single-valued attribute, or if the given name does not match any of the attributes
     *         of this feature.
     * 
     * @throws ClassCastException
     *         If the attribute type is a type other than {@link Object} in the {@link FeatureType} and an
     *         incorrect type is passed in.
     */
    public void setAttribute(String name, Object value) throws IllegalArgumentException,
            ClassCastException {
        
        if ( !featureType.hasAttribute(name)) {
            throw new IllegalArgumentException("FeatureType '" + getFeatureType() + "' hasn't got attribute '" + name + "'");
        }
        if (value == null) {
            throw new NullPointerException("Value of attribute '" + name + "' of feature '"+ getID() + "' is null.");
        }
        else {
            OXFFeatureAttributeDescriptor attribDesc = featureType.getAttributeDescriptor(name);
            if (attribDesc.getObjectClass().isAssignableFrom(value.getClass())) {
                attributeMap.put(name, value);
            }
            else {
                throw new ClassCastException("wrong class for attribute '" + name
                        + "' of " + featureType + ": expected class: "
                        + attribDesc.getObjectClass() + " - received class: " + value.getClass());
            }
        }
    }

    /**
     * Returns a String that uniquely identifies this {@code Feature} instance with this Java virtual machine
     * (and perhaps uniquely in a broader scope as well). This value is not necessarily one of the attributes
     * of this feature. Some features may implement this method by concatenating this feature's type name with
     * the String values of all of the primary key attributes. (This is only a suggestion, however, and a
     * given {@code Feature} implementation may choose to compute the ID in whatever way makes sense.)
     */
    public String getID() {
        return id;
    }

    /**
     * Returns the collection in which this Feature is contained.
     */
    public OXFFeatureCollection getParent() {
        return parent;
    }

    /**
     * @return the <code>envelope</code> of the geometry of this feature. or null if this object does not
     *         contain a geometry.
     */
    public Geometry getBoundingBox() {
        Geometry envelope = null;

        if (geom != null) {
            envelope = geom.getEnvelope();
        }

        return envelope;
    }

    /**
     * 
     * @return
     */
    public Geometry getGeometry() {
        return geom;
    }

    /**
     * 
     * @param g
     */
    public void setGeometry(Geometry g) {
        geom = g;
    }

    public String toString() {
        return id;
    }
    

    public String produceDescription() {
        String res = id + ": ";
        
        for (String attName : getSpecifiedAttributes()) {
            res += "[" + attName + " - " + getAttribute(attName) + "]";
        }
        
        return res;
    }
    
    public static OXFFeature createFrom(SFSpatialSamplingFeatureType samplingFeatureType) {
        ShapeType shapeType = samplingFeatureType.getShape();
        XmlCursor c = samplingFeatureType.newCursor();
        
        //
        // parse: Sampling 2.0.0
        //
        
        boolean hasGmlPoint32 = c.toChild(new QName("http://www.opengis.net/gml/3.2", "Point"));
        if (hasGmlPoint32){
            PointType gmlPoint = (PointType) shapeType;
            return OXFGmlPointType.create(gmlPoint);
        } else {
            String featureId = samplingFeatureType.getIdentifier().getStringValue();
            String featureType = samplingFeatureType.getType().getHref();
            // XXX check if setting featureAttributeDescriptors is appropriate
            List<OXFFeatureAttributeDescriptor> featureAttributeDescriptors = new OXFSamplingPointType().getAttributeDescriptors();
            return new OXFFeature(featureId, new OXFFeatureType(featureType, featureAttributeDescriptors));
        }
    }
    
    /**
     * Parses a single feature entity to an OXFFeature object.
     * The method supports the Sampling Specification of version 0.0 and 1.0.
     */
    public static OXFFeature createFrom(FeaturePropertyType xb_featureMember) throws OXFException {
        OXFFeature feature = null;

        XmlCursor c = xb_featureMember.newCursor();
        
        //
        // parse: Sampling 1.0.0
        //
        
        // if feature is a SamplingPoint:
        if (c.toChild(new QName("http://www.opengis.net/sampling/1.0", "SamplingPoint"))){
            try {
                SamplingPointDocument xb_saPointDoc = SamplingPointDocument.Factory.parse(c.getDomNode());

                feature = OXFSamplingPointType.create(xb_saPointDoc);

                return feature;
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }
        
        // if feature is a SamplingSurface:
        else if (c.toChild(new QName("http://www.opengis.net/sampling/1.0", "SamplingSurface"))){
            try {
                SamplingSurfaceDocument xb_saSurfaceDoc = SamplingSurfaceDocument.Factory.parse(c.getDomNode());

                feature = OXFSamplingSurfaceType.create(xb_saSurfaceDoc);

                return feature;
            }
            catch (XmlException e) {
                throw new OXFException(e);
            }
        }
        
        // if feature is a GrdcSamplingPoint:
        else if (c.toChild(new QName("http://www.grdc.de/sampling/1.0", "GrdcSamplingPoint"))) {
            try {
                GrdcSamplingPointDocument xb_grdcSaPoDoc = GrdcSamplingPointDocument.Factory.parse(c.getDomNode());
                
                feature = OXFGrdcSamplingPointType.create(xb_grdcSaPoDoc);
                
                return feature;
            } catch (Exception e) {
                throw new OXFException(e);
            }
        }
        
        
        // if feature is not known:
        else {
            String featureID = xb_featureMember.getHref();

            // TODO: not nice --> feature has no FeatureType
            feature = new OXFFeature(featureID, null);

            return feature;
        }
    }
}