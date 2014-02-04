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
package org.n52.oxf.render.coverage;

import org.n52.oxf.ows.capabilities.IBoundingBox;

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