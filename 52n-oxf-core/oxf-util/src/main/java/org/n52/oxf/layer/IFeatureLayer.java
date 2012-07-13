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

package org.n52.oxf.layer;

import java.util.*;
import org.n52.oxf.feature.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 * @see implementing class: FeatureServiceLayer
 */
public interface IFeatureLayer extends IContextLayer {

    /**
     * 
     * @return
     */
    public IFeatureStore getFeatureStore();

    /**
     * @return the actual collection of features that will be portrayed in this layer. A null value is allowed
     *         and indicates that nothing will be drawn.
     */
    public OXFFeatureCollection getFeatureCollection();

    /**
     * sets the actual collection of features.
     * @param featureCollection
     */
    public void setFeatureCollection(OXFFeatureCollection featureCollection);
    
    /**
     * This method returns a Set of all OXFFeatures of this Layer drawn at screen-position (x, y).
     * 
     * @param x
     * @param y
     * @return a List of all OXFFeatures of this Layer drawn at screen-position (x, y).
     */
    public Set<OXFFeature> pickFeature(int x, int y);

    /**
     * This method returns a Set of all OXFFeatures of this Layer drawn inside the Screen-BoundingBox
     * specified by the lowerLeft-corner (llX, llY) and the upperRight-corner (urX, urY).
     * 
     * @param llX
     * @param llY
     * @param urX
     * @param urY
     * @return
     */
    public Set<OXFFeature> pickFeature(int llX, int llY, int urX, int urY);

    /**
     * 
     * @return the currently selected Set of features in this Layer.
     */
    public Set<OXFFeature> getSelectedFeatures();

    /**
     * this method should be primarly be invoked by the method selectFeatures() of the LayerContext class.
     * That will clear all current selections.
     * 
     * @param selectedFeatures
     */
    public void setSelectedFeatures(Set<OXFFeature> selectedFeatures);

    public void clearSelection();
}