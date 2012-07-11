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
package org.n52.oxf.render.coverage;

import org.n52.oxf.owsCommon.capabilities.IBoundingBox;

abstract public class AbstractCoverage {
    
    private IBoundingBox bBox;
    
    private int nCols;
    private int nRows;
    
    private double minValue;
    private double maxValue;

    public AbstractCoverage(IBoundingBox bB, int nCols, int nRows) {
        this.bBox = bB;
        this.nCols = nCols;
        this.nRows = nRows;
    }
    
    public String toString() {
        StringBuffer res = new StringBuffer("");
        exportGrid(res);
        return res.toString();
    }

    public void exportGrid(StringBuffer target) {
        for (int r = 0; r < getNumberOfRows(); r++) {
            for (int c = 0; c < getNumberOfColumns(); c++) {
                target.append(getCellValue(r, c));
                target.append(" ");
            }
            target.append("\n");
        }
    }

    public abstract Object getCellValue(int x, int y);

    public int getNumberOfColumns() {
        return nCols;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public double getXRasterLength() {
        return (bBox.getUpperCorner()[0] - bBox.getLowerCorner()[0]) / nCols;
    }

    public double getYRasterLength() {
        return (bBox.getUpperCorner()[1] - bBox.getLowerCorner()[1]) / nRows;
    }

    public IBoundingBox getBoundingBox() {
        return bBox;
    }
    
    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }
    
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
}