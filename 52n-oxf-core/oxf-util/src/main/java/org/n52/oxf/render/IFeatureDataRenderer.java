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
import org.n52.oxf.*;
import org.n52.oxf.adapter.*;
import org.n52.oxf.feature.*;
import org.n52.oxf.owsCommon.capabilities.*;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public interface IFeatureDataRenderer extends IRenderer {

    /**
     * 
     * @param featuresToRender
     *        die in dieser Collection enthaltenen Features beschreiben die zu visualisierenden Daten.
     * @param parameterContainer
     *        der Renderer kann hier Parameter-Werte entnehmen, die bei der Datenanfrage benutzt wurden.
     * @param screenWidth
     *        die Breite des "Views"
     * @param screenHeight
     *        die H�he des "Views"
     * @param bbox
     *        die geografische Ausdehnung
     * @param selectedFeatures
     *        diese Menge enth�lt selektierte Features. Somit k�nnen selektierte Features bei der
     *        Visualisierung separat behandelt werden.
     * @return
     */
    public IVisualization renderLayer(OXFFeatureCollection featuresToRender,
                                      ParameterContainer parameterContainer,
                                      int screenWidth,
                                      int screenHeight,
                                      IBoundingBox bbox,
                                      Set<OXFFeature> selectedFeatures) throws OXFException;
}