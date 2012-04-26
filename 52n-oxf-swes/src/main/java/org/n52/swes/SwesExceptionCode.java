package org.n52.swes;

public enum SwesExceptionCode {
    INVALID_REQUEST("InvalidRequest"),
    REQUEST_EXTENSION_NOT_SUPPORTED("RequestExtensionNotSupported");
    
    private String exceptionCode;
    
    private SwesExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
    
    public String getExceptionCode() {
        return this.exceptionCode;
    }
}
