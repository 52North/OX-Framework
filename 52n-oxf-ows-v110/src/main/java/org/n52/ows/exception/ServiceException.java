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
        if (owsException.isSetLocator()) {
            exceptionType.setLocator(owsException.getLocator());
        }
        exceptionType.setExceptionCode(owsException.getExceptionCode());
        exceptionType.setExceptionTextArray(owsException.getExceptionTextsAsArray());
    }
    
    public boolean containsExceptions() {
        ExceptionType[] exceptionArray = exceptionDocument.getExceptionReport().getExceptionArray();
        return exceptionArray != null && exceptionArray.length > 0;
    }
}