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
 
 Created on: 18.05.2005
 *********************************************************************************/

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
     * @param lowerLeft the lowerCorner
     * @param upperRight the upperCorner
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