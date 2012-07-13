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
package org.n52.oxf.owsCommon;

import java.io.*;
import java.util.*;

/**
 * Encapsulates Exceptions which occur on service side processing.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public class ExceptionReport extends Exception {

    private String version;

    private String language;

    private ArrayList<OWSException> exceptions;

    public ExceptionReport(String version, String language) {
        this.version = version;
        this.language = language;

        exceptions = new ArrayList<OWSException>();
    }

    /**
     * @return Returns the exceptions.
     */
    public Iterator<OWSException> getExceptionsIterator() {
        return exceptions.iterator();
    }

    /**
     * @param exceptions
     *        The exceptions to set.
     */
    public void addException(OWSException exception) {
        this.exceptions.add(exception);
    }

    /**
     * prints all contained Exceptions to the System.err stream.
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * 
     */
    @Override
    public void printStackTrace(PrintStream s) {
        s.println("- OGC Web Service returned exception:");

        super.printStackTrace(s);

        for (OWSException e : exceptions) {
            e.printStackTrace(s);
        }
    }

    /**
     * @return a HTML representation of this OWSException
     */
    public String toHtmlString() {
        String res = "<b>OGC Web Service returned exception:</b><br>";

        for (OWSException e : exceptions) {
            res += e.toHtmlString();
        }
        
        return res;
    }
    
    /**
     * 
     * @return the number of exceptions contained in this ExceptionReport.
     */
    public int countExceptions() {
        return exceptions.size();
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *        The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *        The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }
}