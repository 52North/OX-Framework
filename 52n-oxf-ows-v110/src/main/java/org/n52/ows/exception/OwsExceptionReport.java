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
