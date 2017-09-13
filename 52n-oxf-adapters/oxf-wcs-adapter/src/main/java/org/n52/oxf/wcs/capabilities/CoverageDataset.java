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
package org.n52.oxf.wcs.capabilities;

import java.util.Locale;

import org.n52.oxf.ows.capabilities.Dataset;
import org.n52.oxf.ows.capabilities.IBoundingBox;
import org.n52.oxf.ows.capabilities.IDiscreteValueDomain;
import org.n52.oxf.ows.capabilities.ITime;

public class CoverageDataset extends Dataset {

    // new in Version 1.1.1 of WCS

    private String gridBaseCRS;
    private String gridType;
    private double[] gridOrigin;
    private double[] gridOffset;
    private String gridCS;

    private final String rpcLink;

    /**
     * this constructor has ALL attributes of the class as its parameters.(Also WCS 1.1.1)
     *
     * @param title
     * @param identifier
     * @param boundingBoxes
     * @param outputFormats
     * @param availableCRSs
     * @param fees
     * @param language
     * @param pointOfContactString
     * @param temporalDomain
     * @param abstractDescription
     * @param keywords
     * @param gridBaseCRS
     * @param gridType
     * @param gridOrigin
     * @param gridOffset
     * @param gridCS
     * @param rpcLink
     */
    public CoverageDataset(String title,
                   String identifier,
                   IBoundingBox[] boundingBoxes,
                   String[] outputFormats,
                   String[] availableCRSs,
                   String fees,
                   Locale[] language,
                   String pointOfContactString,
                   IDiscreteValueDomain<ITime> temporalDomain,
                   String abstractDescription,
                   String[] keywords,
                   String gridBaseCRS,
                   String gridType,
                   double[] gridOrigin,
                   double[] gridOffset,
                   String gridCS,
                   String rpcLink) {
        super(title,identifier,boundingBoxes, outputFormats,availableCRSs,fees,language,pointOfContactString,temporalDomain, abstractDescription, keywords);
        setGridBaseCRS(gridBaseCRS);
        setGridType(gridType);
        setGridOffset(gridOffset);
        setGridOrigin(gridOrigin);
        setGridCS(gridCS);
        this.rpcLink = rpcLink;
    }

        public String getGridBaseCRS() {
            return gridBaseCRS;
        }

        protected void setGridBaseCRS(String gridBaseCRS) {
            this.gridBaseCRS = gridBaseCRS;
        }

        public String getGridType() {
            return gridType;
        }

        protected void setGridType(String gridType) {
            this.gridType = gridType;
        }

        public double[] getGridOrigin() {
            return gridOrigin == null ? null : gridOrigin.clone();
        }

        protected void setGridOrigin(double[] gridOrigin) {
            this.gridOrigin = gridOrigin;
        }

        public double[] getGridOffset() {
            return gridOffset == null ? null : gridOffset.clone();
        }

        protected void setGridOffset(double[] gridOffset) {
            this.gridOffset = gridOffset;
        }

        public String getGridCS() {
            return gridCS;
        }

        protected void setGridCS(String gridCS) {
            this.gridCS = gridCS;
        }

        public String getRpcLink () {
            return rpcLink;
        }
}
