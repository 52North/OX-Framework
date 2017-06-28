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
package org.n52.ows.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A container collecting {@link OwsException}s to be thrown together as OWS Exception Report (according to
 * chapter 8 of [OGC 06-121r3]).
 */
public class OwsExceptionReport extends Exception {

    private static final long serialVersionUID = 8369604913484927730L;

    public enum LevelOfDetail {
        PLAIN, DETAILED;
    }
    
    private LevelOfDetail levelOfDetail = LevelOfDetail.DETAILED;
    
    private List<OwsException> exceptionList = new ArrayList<OwsException>();
    
    public OwsExceptionReport() {
        // allow default construction
    }
    
    public OwsExceptionReport(OwsException exception) {
        this.addOwsException(exception);
    }
    
    public OwsException[] getOwsExceptionsArray() {
        return exceptionList.toArray(new OwsException[exceptionList.size()]);
    }
    
    public void addOwsException(OwsException exception) {
        this.exceptionList.add(exception);
    }
    
    public void addAllOwsExceptions(Collection<OwsException> exceptions) {
        this.exceptionList.addAll(exceptions);
    }
    
    public boolean containsExceptions() {
        return !exceptionList.isEmpty();
    }

    public LevelOfDetail getLevelOfDetail() {
        return levelOfDetail;
    }

    public void setLevelOfDetail(LevelOfDetail levelOfDetail) {
        this.levelOfDetail = levelOfDetail;
    }
    
}
