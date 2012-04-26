
package org.n52.ows.exception;

/**
 * Operation request does not include a parameter value, and this server did not declare a default value for
 * that parameter
 */
public class MissingParameterValueException extends OwsException {

    private static final long serialVersionUID = 6871580141329735488L;

    /**
     * @param locator Name of missing parameter
     */
    public MissingParameterValueException(String locator) {
        super(OwsExceptionCode.MISSING_PARAMETER_VALUE.getExceptionCode(), locator);
    }

    @Override
    public int getHttpStatusCode() {
        return BAD_REQUEST;
    }

}
