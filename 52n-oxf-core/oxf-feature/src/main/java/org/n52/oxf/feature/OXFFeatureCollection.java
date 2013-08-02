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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFFeatureCollection extends OXFFeature implements Iterable<OXFFeature> {

    protected Collection<OXFFeature> features;

    public OXFFeatureCollection(String id, OXFFeatureType featureType) {
        super(id, featureType);
        this.features = new Vector<OXFFeature>();
    }

    public OXFFeatureCollection(String id, OXFFeatureType featureType, Collection<OXFFeature> features) {
        super(id, featureType);
        this.features = features;
    }

    /**
     * 
     * @param worldX
     * @param worldY
     * @return
     */
    public OXFFeature getFeatureAtPoint(double worldX, double worldY) {
        return getFeatureAtPoint(worldX, worldY, Double.NaN);
    }

    /**
     * 
     * @param worldX
     * @param worldY
     * @return
     */
    public OXFFeature getFeatureAtPoint(double worldX, double worldY, double worldZ) {

        Coordinate c = new Coordinate(worldX, worldY, worldZ);

        for (OXFFeature feature : features) {
            if (feature.getGeometry().contains(new GeometryFactory().createPoint(c))) {
                return feature;
            }
        }

        return null;
    }

    public OXFFeature getFeature(String gmlID) {
        for (OXFFeature feature : features) {
            if (feature.getID().equals(gmlID)) {
                return feature;
            }
        }
        return null;
    }

    /**
     * @return the computed BoundingBox of all contained OXFFeatures. or <code>null</code> if this object
     *         does not contain any features or the contained features have no geometry.
     */
    public Geometry getBoundingBox() {
        Geometry envelope = null;

        for (OXFFeature feature : features) {
            if (feature.getBoundingBox() != null) {
                if (envelope != null) {
                    envelope = envelope.union(feature.getBoundingBox());
                }
                else {
                    envelope = feature.getBoundingBox();
                }
            }
        }

        return envelope;
    }

    /**
     * Returns an iterator that enumerates all of the features in this collection.
     */
    public Iterator<OXFFeature> iterator() {
        return features.iterator();
    }

    /**
     * @return a List containing all OXFFeatures of this OXFFeatureCollection.
     */
    public List<OXFFeature> toList() {
        return new Vector<OXFFeature>(features);
    }

    /**
     * @return a Set containing all OXFFeatures of this OXFFeatureCollection.
     */
    public Set<OXFFeature> toSet() {
        HashSet<OXFFeature> hSet = new HashSet<OXFFeature>();
        hSet.addAll(this.features);
        return hSet;
    }

    /**
     * Creates a {@link Feature} array and populates it.
     * 
     * @throws OutOfMemoryError
     *         if the feature collection is too large to fit into memory.
     */
    public OXFFeature[] toArray() throws OutOfMemoryError {
        OXFFeature[] featureArray = new OXFFeature[size()];
        features.toArray(featureArray);

        return featureArray;
    }

    /**
     * Returns {@code true} if this collection contains no {@link Feature}s.
     */
    public boolean isEmpty() {
        return features.isEmpty();
    }

    /**
     * Returns the size of the collection.
     */
    public int size() {
        return features.size();
    }

    /**
     * 
     * @param fArray
     */
    public void add(OXFFeature[] fArray) {
        List<OXFFeature> fList = Arrays.asList(fArray);
        add(fList);
    }

    /**
     * 
     * @param fCollection
     */
    public void add(Collection<OXFFeature> fCollection) {
        features.addAll(fCollection);
    }

    /**
     * 
     * @param f
     */
    public void add(OXFFeature f) {
        features.add(f);
    }

    /**
     * Checks if the given feature is a member of this collection.
     */
    public boolean contains(OXFFeature f) {
        return features.contains(f);
    }

    /**
     * Checks if every feature in the given collection is also a member of this feature collection.
     */
    public boolean containsAll(Collection<OXFFeature> c) {
        return features.contains(c);
    }

    public String produceDescription() {
        String res = "";
        
        for (OXFFeature feature : features) {
            res += feature.produceDescription() + "\n";
        }
        
        return res;
    }
    
    /*
     * 
     */
    public static void main(String[] args) {
        OXFFeatureCollection featureColl = new OXFFeatureCollection("", null);
        {
            OXFFeature aF = new OXFFeature("1", null);
            Geometry aFGeom = new GeometryFactory().createLineString(new Coordinate[] {new Coordinate(1,
                                                                                                      1),
                                                                                       new Coordinate(3,
                                                                                                      3)});
            aF.setGeometry(aFGeom);
            featureColl.add(aF);
        }
        {
            OXFFeature bF = new OXFFeature("2", null);
            Geometry bFGeom = new GeometryFactory().createLineString(new Coordinate[] {new Coordinate(2,
                                                                                                      2),
                                                                                       new Coordinate(5,
                                                                                                      5)});
            bF.setGeometry(bFGeom);
            featureColl.add(bF);
        }

        System.out.println(featureColl.getBoundingBox());
    }
}