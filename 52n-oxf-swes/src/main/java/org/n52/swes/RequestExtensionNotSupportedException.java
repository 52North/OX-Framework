
package org.n52.swes;

import org.n52.ows.exception.OwsException;

/**
 * One or more of the extensions used in the operation request are unknown to or not supported by the service. 
 * <br><br>
 * <b>Note:</b> A service shall add the complete unsupported request extension into the ExtensionText property of the
 * Exception. In the XML encoding, use CDATA to surround markup. If the encoded extension itself contains the
 * string “]]>”, use two CDATA tags following this pattern: <![CDATA[content]]]]><![CDATA[>content]]>
 */
public class RequestExtensionNotSupportedException extends OwsException {

    private static final long serialVersionUID = -3798668983249511518L;

    public RequestExtensionNotSupportedException() {
        super(SwesExceptionCode.REQUEST_EXTENSION_NOT_SUPPORTED.getExceptionCode());
    }

    @Override
    public int getHttpStatusCode() {
        // not found in specification, but NOT_IMPLEMENTED is most obvious
        return NOT_IMPLEMENTED;
    }

}
