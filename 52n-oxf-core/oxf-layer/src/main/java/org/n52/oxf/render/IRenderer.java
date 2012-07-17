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




/**
 * @see subclass: <a href=IFeatureDataRenderer.html>IFeatureDataRenderer</a>
 * @see subclass: <a href=IServiceRenderer.html>IServiceRenderer</a>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public interface IRenderer {

    /**
     * @return a plain text description about the functionality and capabilities of this Renderer.
     */
    public String getDescription();
    
    /**
     * @return the type of the service whose data can be rendered with this
     *         ServiceRenderer. Should look like e.g. "OGC:WCS".
     */
    public String getServiceType();

    /**
     * @return the versions of the services whose data can be rendered with this
     *         ServiceRenderer. Should look like e.g. {"1.1.0","1.2.0"}.
     */
    public String[] getSupportedVersions();
}