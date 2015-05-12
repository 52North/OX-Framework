/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.oxf;

import java.io.*;
import java.util.*;


/**
 * This class can be used to collect several occuring Throwables(/Exceptions). After collecting the throwable,
 * you may use the overriden <code>printStackTrace</code>-methods to print a cumulated stack trace of all
 * ocuured exceptions/throwables.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class OXFThrowableCollection extends OXFException {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Throwable> throwables;

    
    public OXFThrowableCollection() {
        throwables = new ArrayList<Throwable>();
    }
    
    public OXFThrowableCollection(String reason) {
        super(reason);

        throwables = new ArrayList<Throwable>();
    }

    public void addThrowable(Throwable throwable) {
        this.throwables.add(throwable);
    }

    public boolean isEmpty(){
        return throwables.isEmpty();
    }
    
    public Iterator<Throwable> getThrowablesIterator() {
        return throwables.iterator();
    }

    
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);

        for (Throwable t : throwables) {
            t.printStackTrace(s);
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);

        for (Throwable t : throwables) {
            t.printStackTrace(s);
        }
    }

}