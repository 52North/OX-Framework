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

import java.util.ArrayList;

import javax.xml.namespace.QName;

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x11.ExceptionType;

import org.apache.xmlbeans.XmlCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO extract/separate xmlbinding ows dependency
 */
public class OwsExceptionReport extends Exception {

    private static final long serialVersionUID = 9069373009339881302L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OwsExceptionReport.class);
    
    /**
     * ExceptionCodes as defined in the OWS Common Implementation Specification 0.3.0
     */
    public enum ExceptionCode {
        OperationNotSupported, MissingParameterValue, InvalidParameterValue, VersionNegotiationFailed, InvalidUpdateSequence, NoApplicableCode, NoDataAvailable, InvalidRequest
    }

    /** Exception levels */
    public enum ExceptionLevel {
        PlainExceptions, DetailedExceptions
    }

    /** Exception types */
    private ArrayList<ExceptionType> excs = new ArrayList<ExceptionType>();

    /** exception level */
    private ExceptionLevel excLevel = null;

    /**
     * standard constructor without parameters, sets the ExceptionLevel on PlainExceptions
     * 
     */
    public OwsExceptionReport() {
        this.excLevel = ExceptionLevel.DetailedExceptions;
    }

    /**
     * constructor with message and cause as parameters
     * 
     * @param message
     *        String containing the message of this exception
     * @param cause
     *        Throwable cause of this exception
     */
    public OwsExceptionReport(String message, Throwable cause) {
        super(message, cause);
        this.excLevel = ExceptionLevel.DetailedExceptions;
    }

    /**
     * constructor with cause as parameter
     * 
     * @param cause
     *        Throwable cause of this exception
     */
    public OwsExceptionReport(Throwable cause) {
        super(cause);
        this.excLevel = ExceptionLevel.DetailedExceptions;
    }

    /**
     * �constructor with exceptionLevel as parameter
     * 
     * @param excLevelIn
     */
    public OwsExceptionReport(ExceptionLevel excLevelIn) {
        this.excLevel = excLevelIn;
    }

    /**
     * 
     * @return Returns the ExceptionTypes of this exception
     */
    public ArrayList<ExceptionType> getExceptions() {
        return excs;
    }

    /**
     * adds a coded exception to this exception with code, locator and messages as parameters
     * 
     * @param code
     *        ExceptionCode of the added exception
     * @param locator
     *        String locator of this exception
     * @param messages
     *        String[] messages of this exception
     */
    public void addCodedException(ExceptionCode code, String locator, String[] messages) {
        ExceptionType et = ExceptionType.Factory.newInstance();
        et.setExceptionCode(code.toString());
        if (locator != null)
            et.setLocator(locator);
        for (int i = 0; i < messages.length; i++) {
            String string = messages[i];
            et.addExceptionText(string);
        }
        excs.add(et);
    }

    /**
     * adds a ServiceException to this exception
     * 
     * @param seIn
     *        ServiceException which should be added
     */
    public void addServiceException(OwsExceptionReport seIn) {
        this.excs.addAll(seIn.getExceptions());
    }

    /**
     * checks whether the ExceptionCode parameter is contain in this exception
     * 
     * @param ec
     *        ExceptionCode which should be checked
     * @return Returns boolean true if ExceptionCode is contained, otherwise false
     */
    public boolean containsCode(ExceptionCode ec) {
        for (ExceptionType et : excs) {
            if (et.getExceptionCode().equalsIgnoreCase(ec.toString()))
                return true;
        }
        return false;
    }

    /**
     * adds a coded Exception to this service exception with code, locator and the exception itself as
     * parameters
     * 
     * @param code
     *        ExceptionCode of the added exception
     * @param locator
     *        String locator of the added exception
     * @param e
     *        Exception which should be added
     */
    public void addCodedException(ExceptionCode code, String locator, Exception e) {

        ExceptionType et = ExceptionType.Factory.newInstance();
        et.setExceptionCode(code.toString());
        if (locator != null) {
            et.setLocator(locator);
        }

        String name = e.getClass().getName();
        String message = e.getMessage();
        StackTraceElement[] stackTraces = e.getStackTrace();

        StringBuffer sb = new StringBuffer();
        sb.append("[EXC] internal service exception");
        if (excLevel.compareTo(ExceptionLevel.PlainExceptions) == 0) {
            sb.append(". Message: " + message);
        }
        else if (excLevel.compareTo(ExceptionLevel.DetailedExceptions) == 0) {
            sb.append(": " + name + "\n");
            sb.append("[EXC] message: " + message + "\n");
            for (int i = 0; i < stackTraces.length; i++) {
                StackTraceElement element = stackTraces[i];
                sb.append("[EXC]" + element.toString() + "\n");
            }
        }
        else {
            LOGGER.warn("addCodedException: unknown ExceptionLevel ({})occurred.", excLevel.toString());
        }

        et.addExceptionText(sb.toString());
        // TODO i guess there is a better way to format an exception

        excs.add(et);
    }

    /**
     * 
     * @return Returns the ExceptionReportDocument XmlBean created from this service exception
     */
    public ExceptionReportDocument getDocument() {

        ExceptionReportDocument erd = ExceptionReportDocument.Factory.newInstance();
        ExceptionReport er = ExceptionReport.Factory.newInstance();
        // er.setLanguage("en");
        er.setVersion("1.0.0");
        er.setExceptionArray(excs.toArray(new ExceptionType[excs.size()]));
        erd.setExceptionReport(er);
        XmlCursor c = erd.newCursor(); // Cursor on the documentc.toStartDoc();
        c.toFirstChild();
        c.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"),
                           "http://www.opengeospatial.net/ows http://mars.uni-muenster.de/swerep/trunk/ows/1.0.30/owsExceptionReport.xsd");
        c.dispose();
        return erd;
    }

    /**
     * checks whether this service exception contains another exception
     * 
     * @return Returns true if this service exception contains another exception
     */
    public boolean containsExceptions() {
        return excs.size() > 0;
    }

    /**
     * adds a coded Exception with ExceptionCode,locator and a single String message to this exception
     * 
     * @param code
     *        Exception code of the exception to add
     * @param locator
     *        String locator of the exception to add
     * @param message
     *        String message of the exception to add
     */
    public void addCodedException(ExceptionCode code, String locator, String message) {

        ExceptionType et = ExceptionType.Factory.newInstance();
        et.setExceptionCode(code.toString());
        if (locator != null) {
            et.setLocator(locator);
        }
        et.addExceptionText(message);
        excs.add(et);
    }
}
