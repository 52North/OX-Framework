
package org.n52.ows.exception;

/**
 * Value of (optional) updateSequence parameter in GetCapabilities operation request is greater than current
 * value of service metadata updateSequence number
 */
public class InvalidUpdateSequenceException extends OwsException {

    private static final long serialVersionUID = -6218206027480978118L;

    public InvalidUpdateSequenceException() {
        super(OwsExceptionCode.INVALID_UPDATE_SEQUENCE.getExceptionCode());
    }

    @Override
    public int getHttpStatusCode() {
        return BAD_REQUEST;
    }

}
