/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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

import org.n52.oxf.*;

/**
 * 
 * This represents a 3-dimensional BoundingBox.
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class BoundingBox3D extends BoundingBox {

	
	/**
     * this constructor has all required attributes as its parameters.
     * @param llx x-coordinate of the lowerCorner
     * @param lly y-coordinate of the lowerCorner
     * @param llz z-coordinate of the lowerCorner
     * @param urx x-coordinate of the upperCorner
     * @param ury y-coordinate of the upperCorner
     * @param urz z-coordinate of the upperCorner
     * @throws OXFException if the dimensions of lowerLeft and upperRight are not the same.
     */
	public BoundingBox3D(double llx, double lly, double llz, double urx, double ury, double urz) throws OXFException{
		super(new double[]{llx, lly, llz}, new double[]{urx, ury, urz});
	}
	
    /**
     * this constructor has all attributes as its parameters.
     * @param crs a EPSG String for instance
     * @param llx x-coordinate of the lowerCorner
     * @param lly y-coordinate of the lowerCorner
     * @param llz z-coordinate of the lowerCorner
     * @param urx x-coordinate of the upperCorner
     * @param ury y-coordinate of the upperCorner
     * @param urz z-coordinate of the upperCorner
     * @throws OXFException if the dimensions of lowerLeft and upperRight are not the same.
     */
	public BoundingBox3D(String crs, double llx, double lly, double llz, double urx, double ury, double urz) throws OXFException{
		super(crs, new double[]{llx, lly, llz}, new double[]{urx, ury, urz});
	}
	
	public double getLlx(){
		return getLowerCorner()[0];
	}
	
	public double getLly(){
		return getLowerCorner()[1];
	}
	
	public double getLlz(){
		return getLowerCorner()[2];
	}
	
	public double getUrx(){
		return getUpperCorner()[0];
	}
	
	public double getUry(){
		return getUpperCorner()[1];
	}
	
	public double getUrz(){
		return getUpperCorner()[2];
	}
}