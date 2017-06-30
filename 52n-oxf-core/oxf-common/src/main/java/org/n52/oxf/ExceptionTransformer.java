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
package org.n52.oxf;

import org.n52.oxf.ows.ExceptionReport;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class ExceptionTransformer {

    /**
     * transforms the specified Exception into an HTML representation and returns it as a String.
     *
     * @param e The Exception to transform
     * @param debugMode <code>true</code> to activate the transformation into HTML, <code>false</code>
     * 			will print "Sorry, a server error occured!"
     *
     * @return HTML code representing the exception
     *
     */
    public static String transformExceptionToHTML(Exception e, boolean debugMode) {
        String res = "";

        if (debugMode) {
            res += "<h2>Service-sided exception occured:</h2>";
            res += transformHelper(e);
        }
        else {
            res += "<b>Sorry, a server error occured!</b>";
        }

        return res;
    }

    private static String transformHelper(Throwable t) {
        String res = "<b>" + t.getClass().getName() + "</b>";

        if (t.getLocalizedMessage() != null) {
            res += " - " + t.getLocalizedMessage();
        }

        // case: Exception occured on SOS-side:
        if (t instanceof ExceptionReport) {
            res += "<blockquote>";

            ExceptionReport excReport = (ExceptionReport) t;
            res += excReport.toHtmlString();

            res += "</blockquote>";
        }

        // case: Exception occured on Facade-side
        else {
            res += "<blockquote>";
            StringBuilder sBuf = new StringBuilder();
            for (StackTraceElement element : t.getStackTrace()) {
                sBuf.append(element).append("<br>");
            }
            res += sBuf.toString() + "</blockquote>";

            Throwable cause = t.getCause();
            if (cause != null) {
                res += "Caused by: " + transformHelper(cause);
            }
        }
        return res;
    }
}
