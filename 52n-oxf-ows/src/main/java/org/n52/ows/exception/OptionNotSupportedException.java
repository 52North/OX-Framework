package org.n52.ows.exception;

/**
 * Request is for an option that is not supported by this server
 */
public class OptionNotSupportedException extends OwsException {

    private static final long serialVersionUID = 3060757902117394170L;

    /**
     * @param locator Identifier of option not supported
     */
    public OptionNotSupportedException(String locator) {
        super(OwsExceptionCode.OPTION_NOT_SUPPORTED.getExceptionCode(), locator);
    }

    @Override
    public int getHttpStatusCode() {
        return NOT_IMPLEMENTED;
    }

}
