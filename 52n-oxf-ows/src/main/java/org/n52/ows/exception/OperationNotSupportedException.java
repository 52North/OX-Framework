package org.n52.ows.exception;

/**
 * Request is for an operation that is not supported by this server.
 */
public class OperationNotSupportedException extends OwsException {

    private static final long serialVersionUID = 507996059086269700L;
    
    /**
     * @param locator Name of operation not supported
     */
    public OperationNotSupportedException(String locator) {
        super(OwsExceptionCode.OPERATION_NOT_SUPPORTED.getExceptionCode(), locator);
    }

    @Override
    public int getHttpStatusCode() {
        return NOT_IMPLEMENTED;
    }

}
