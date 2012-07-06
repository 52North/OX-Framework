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
import java.util.List;

public abstract class OwsException extends Exception {
    
    private static final long serialVersionUID = 3899044347528650839L;
    
    public static final int BAD_REQUEST = 400;
    public static final int FORBIDDEN = 403;
    public static final int GONE = 410;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;

    private List<String> exceptionTexts = new ArrayList<String>();
    private String exceptionCode;
    private String locator;

    public OwsException(String exceptionCode) {
        this(exceptionCode, null);
    }

    public OwsException(String exceptionCode, String locator) {
        this.exceptionCode = exceptionCode;
        this.locator = locator;
    }

    public void addExceptionText(String exceptionText) {
        this.exceptionTexts.add(exceptionText);
    }

    public String getExceptionCode() {
        return this.exceptionCode;
    }

    public String[] getExceptionTextsAsArray() {
        return exceptionTexts.toArray(new String[exceptionTexts.size()]);
    }

    public List<String> getExceptionTexts() {
        return this.exceptionTexts;
    }

    public String getLocator() {
        return this.locator;
    }
    
    public boolean isSetLocator() {
        return locator != null;
    }

    public abstract int getHttpStatusCode();

}
