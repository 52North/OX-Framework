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
package org.n52.oxf.valueDomains.spatial;

import java.awt.geom.*;

/**
 * 
 * This represents a 2-dimensional BoundingBox.
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class BoundingBox2D extends BoundingBox {

	
	/**
     * this constructor has all required attributes as its parameters.
     * 
     * @throws IllegalArgumentException
     *         if the dimensions of lowerLeft and upperRight are not the same.
     */
	public BoundingBox2D(double llx, double lly, double urx, double ury){
		super(new double[]{llx, lly}, new double[]{urx, ury});
	}
	
    /**
     * this constructor has all attributes as its parameters.
     * @param crs a EPSG String for instance
     * @param lowerLeft the lowerCorner
     * @param upperRight the upperCorner
     * 
     * @throws IllegalArgumentException
     *         if the dimensions of lowerLeft and upperRight are not the same.
     */
	public BoundingBox2D(String crs, double llx, double lly, double urx, double ury) {
		super(crs, new double[]{llx, lly}, new double[]{urx, ury});
	}
	
	public Rectangle2D.Double asRectangle2D(){
		return new Rectangle2D.Double(getLlx(), getLly(), getUrx() - getLlx(), getUry() - getLly());
	}
	
	public double getLlx(){
		return getLowerCorner()[0];
	}
	
	public double getLly(){
		return getLowerCorner()[1];
	}
	
	public double getUrx(){
		return getUpperCorner()[0];
	}
	
	public double getUry(){
		return getUpperCorner()[1];
	}
}