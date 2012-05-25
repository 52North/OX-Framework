/**
 * Copyright (C) 2012
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

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x11.ExceptionType;


public class ServiceException {

    private ExceptionReportDocument exceptionDocument;

    public static ServiceException createFromOwsExceptionReport(OwsExceptionReport owsExceptionReport) {
        return new ServiceException(owsExceptionReport);
    }

    private ServiceException(OwsExceptionReport owsExceptionReport) {
        exceptionDocument = createExceptionReport(owsExceptionReport);
    }

    public static ServiceException createFromOwsException(OwsException owsException) {
        return new ServiceException(owsException);
    }
    
    private ServiceException(OwsException owsException) {
        exceptionDocument = ExceptionReportDocument.Factory.newInstance();
        ExceptionReport report = exceptionDocument.addNewExceptionReport();
        ExceptionType exceptionType = report.addNewException();
        setExceptionContent(owsException, exceptionType);
    }
    
    public void setVersion(String version) {
        ExceptionReport exceptionReport = exceptionDocument.getExceptionReport();
        exceptionReport.setVersion(version);
    }
    
    public void setLanguage(String language) {
        ExceptionReport exceptionReport = exceptionDocument.getExceptionReport();
        exceptionReport.setLang(language);
    }
    
    public ExceptionReportDocument getExceptionReport() {
        return exceptionDocument;
    }
    
    private ExceptionReportDocument createExceptionReport(OwsExceptionReport owsExceptionReport) {
        ExceptionReportDocument reportDocument = ExceptionReportDocument.Factory.newInstance();
        ExceptionReport report = reportDocument.addNewExceptionReport();
        setOwsExceptions(report, owsExceptionReport);
        return reportDocument;
    }
    
    private void setOwsExceptions(ExceptionReport report, OwsExceptionReport owsExceptionReport) {
        for (OwsException owsException : owsExceptionReport.getOwsExceptionsArray()) {
            setExceptionContent(owsException, report.addNewException());
        }
    }
    
    private void setExceptionContent(OwsException owsException, ExceptionType exceptionType) {
        exceptionType.setLocator(owsException.getLocator());
        exceptionType.setExceptionCode(owsException.getExceptionCode());
        exceptionType.setExceptionTextArray(owsException.getExceptionTextsAsArray());
    }
    
    public boolean containsExceptions() {
        ExceptionType[] exceptionArray = exceptionDocument.getExceptionReport().getExceptionArray();
        return exceptionArray != null && exceptionArray.length > 0;
    }
}