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
package org.n52.oxf.render;

import java.util.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.ows.capabilities.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public interface IFeaturePicker {
    
    /**
     * This method returns a Set of all OXFFeatures of this Layer drawn at screen-position (x, y).
     * 
     * @param x
     * @param y
     * @param screenW
     * @param screenH
     * @param featureCollection
     * @param bbox
     * @return a List of OXFFeatures drawn at the screen-position (x, y).
     */
    public Set<OXFFeature> pickFeature(int x,
                                       int y,
                                       int screenW,
                                       int screenH,
                                       OXFFeatureCollection featureCollection,
                                       IBoundingBox bbox);

    /**
     * This method returns a Set of all OXFFeatures of this Layer drawn inside the Screen-BoundingBox
     * specified by the lowerLeft-corner (llX, llY) and the upperRight-corner (urX, urY).
     * 
     * @param llX
     * @param llY
     * @param urX
     * @param urY
     * @param screenW
     * @param screenH
     * @param featureCollection
     * @param bbox
     * @return
     */
    public Set<OXFFeature> pickFeature(int llX,
                                       int llY,
                                       int urX,
                                       int urY,
                                       int screenW,
                                       int screenH,
                                       OXFFeatureCollection featureCollection,
                                       IBoundingBox bbox);
}