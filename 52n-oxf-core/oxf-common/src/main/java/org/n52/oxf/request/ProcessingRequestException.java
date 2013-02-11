package org.n52.oxf.request;

public class ProcessingRequestException extends Exception {

    private static final long serialVersionUID = 6123396880633581396L;

    public ProcessingRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingRequestException(String message) {
        super(message);
    }

}
