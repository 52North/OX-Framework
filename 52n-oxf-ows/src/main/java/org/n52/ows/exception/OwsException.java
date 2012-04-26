
package org.n52.ows.exception;

import java.util.ArrayList;
import java.util.List;

public abstract class OwsException extends Exception {
    
    public static final int BAD_REQUEST = 400;
    public static final int FORBIDDEN = 403;
    public static final int GONE = 410;
    public static final int NOT_IMPLEMENTED = 501;

    private static final long serialVersionUID = 4346872527665253094L;

    private List<String> exceptionTexts = new ArrayList<String>();
    private String exceptionCode;
    private String locator;

    public OwsException(String exceptionCode) {
        this(exceptionCode, null);
    }

    public OwsException(String exceptionCode, String locator) {
        this.exceptionCode = exceptionCode;
        this.locator = locator;
    }

    public void addExceptionText(String exceptionText) {
        this.exceptionTexts.add(exceptionText);
    }

    public String getExceptionCode() {
        return this.exceptionCode;
    }

    public String[] getExceptionTextsAsArray() {
        return exceptionTexts.toArray(new String[exceptionTexts.size()]);
    }

    public List<String> getExceptionTexts() {
        return this.exceptionTexts;
    }

    public String getLocator() {
        return this.locator;
    }

    public abstract int getHttpStatusCode();

}
