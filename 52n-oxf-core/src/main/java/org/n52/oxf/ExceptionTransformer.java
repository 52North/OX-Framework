
package org.n52.oxf;

import org.n52.oxf.owsCommon.ExceptionReport;

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