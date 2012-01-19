/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 01.02.2006
 *********************************************************************************/

package org.n52.oxf.feature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFGeometryAttribute extends OXFFeatureAttributeDescriptor {

    /**
     * 
     * @param <geomClazz> 
     * @param name
     * @param dataType
     * @param objectClass
     */
    public <geomClazz extends Geometry> OXFGeometryAttribute(String name, Class<geomClazz> geometryClass) {
        super(name, DataType.OBJECT, geometryClass);
    }

    /**
     * 
     * @param <geomClazz>
     * @param name
     * @param dataType
     * @param objectClass
     * @param minOccurrences
     * @param maxOccurrences
     * @param documentation
     * @param geometryClass
     */
    public <geomClazz extends Geometry> OXFGeometryAttribute(String name, DataType dataType, Class objectClass, int minOccurrences, int maxOccurrences, String documentation, Class<geomClazz> geometryClass) {
        super(name, DataType.OBJECT, geometryClass, minOccurrences, maxOccurrences, documentation);
    }
}