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
 
 Created on: 28.07.2005
 *********************************************************************************/

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

    private ArrayList<Throwable> throwables;

    
    public OXFThrowableCollection() {
        throwables = new ArrayList<Throwable>();
    }
    
    public OXFThrowableCollection(String reason) {
        super(reason);

        throwables = new ArrayList<Throwable>();
    }

    /**
     * 
     * @param exception
     */
    public void addThrowable(Throwable throwable) {
        this.throwables.add(throwable);
    }

    /**
     * 
     * @return
     */
    public boolean isEmpty(){
        return throwables.isEmpty();
    }
    
    /**
     * 
     * @return
     */
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