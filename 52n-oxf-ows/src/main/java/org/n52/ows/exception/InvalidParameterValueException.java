
package org.n52.ows.exception;

/**
 * Operation request contains an invalid parameter value
 */
public class InvalidParameterValueException extends OwsException {

    private static final long serialVersionUID = -506733054106380305L;

    /**
     * @param locator Name of parameter with invalid value
     */
    public InvalidParameterValueException(String locator) {
        super(OwsExceptionCode.INVALID_PARAMETER_VALUE.getExceptionCode(), locator);
    }

    @Override
    public int getHttpStatusCode() {
        return BAD_REQUEST;
    }

}
