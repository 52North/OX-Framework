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