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
     */
    public static String transformExceptionToHTML(Exception e, boolean debugMode) {
        String res = "";
        
        if (debugMode) {
            res += "<h2>Service-sided exception occured:</h2>";
            res += transformHelper(e);
        }
        else {
            res += "<br>Sorry, a server error occured!</br>";
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
            for (StackTraceElement element : t.getStackTrace()) {
                res += element + "<br>";
            }
            res += "</blockquote>";

            Throwable cause = t.getCause();
            if (cause != null) {
                res += "Caused by: " + transformHelper(cause);
            }
        }
        return res;
    }
}