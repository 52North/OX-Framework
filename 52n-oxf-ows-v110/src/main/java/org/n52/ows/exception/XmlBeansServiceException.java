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

import net.opengis.ows.x11.ExceptionReportDocument;
import net.opengis.ows.x11.ExceptionReportDocument.ExceptionReport;
import net.opengis.ows.x11.ExceptionType;

/**
 * Provides static construction of service exception containing either an OwsException and a complete
 * OwsExceptionReport.
 */
public class XmlBeansServiceException {

    private ExceptionReportDocument exceptionDocument;

    public static XmlBeansServiceException createFromOwsExceptionReport(OwsExceptionReport owsExceptionReport) {
        return new XmlBeansServiceException(owsExceptionReport);
    }

    private XmlBeansServiceException(OwsExceptionReport owsExceptionReport) {
        exceptionDocument = createExceptionReport(owsExceptionReport);
    }

    public static XmlBeansServiceException createFromOwsException(OwsException owsException) {
        return new XmlBeansServiceException(owsException);
    }

    private XmlBeansServiceException(OwsException owsException) {
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
