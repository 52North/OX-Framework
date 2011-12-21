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
 
 Created on: 01.07.2006
 *********************************************************************************/

package org.n52.oxf.render.coverage;

import org.n52.oxf.owsCommon.capabilities.IBoundingBox;

public class IntCoverage extends AbstractCoverage {
	
    private int[][] grid;
    
	public IntCoverage(IBoundingBox bB, int nCols, int nRows) {
		super(bB, nCols, nRows);
	}

	public IntCoverage(IBoundingBox bB, int nCols, int nRows, int[][] grid) {
		super(bB, nCols, nRows);
		setGrid(grid);
	}

    
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}

	public Integer getCellValue(int r, int c) {
		return grid[r][c];
	}


}