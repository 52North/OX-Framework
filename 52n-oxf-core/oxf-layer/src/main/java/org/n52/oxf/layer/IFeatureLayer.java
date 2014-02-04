/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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