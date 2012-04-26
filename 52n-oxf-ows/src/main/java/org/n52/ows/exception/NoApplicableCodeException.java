package org.n52.ows.exception;

/**
 * No other exceptionCode specified by this service and server applies to this exception. 
 * <br><br>Http Status Code vary from 3xx, 4xx, and 5xx. 
 */
public class NoApplicableCodeException extends OwsException {

    private static final long serialVersionUID = 7670774591278476665L;
    
    private int statusCode;

    /**
     * @param httpStatusCode HTTP Status Code of category 3xx, 4xx, or 5xx
     */
    public NoApplicableCodeException(int httpStatusCode) {
        super(OwsExceptionCode.NO_APPLICABLE_CODE.getExceptionCode());
        if (!hasValidRange(httpStatusCode)) {
            throw new IllegalArgumentException("HttpStatusCode must be 3xx, 4xx, or 5xx");
        }
        this.statusCode = httpStatusCode;
    }

    private boolean hasValidRange(int httpStatusCode) {
        return httpStatusCode < 300 && 500 < httpStatusCode;
    }

    @Override
    public int getHttpStatusCode() {
        return statusCode;
    }

}
