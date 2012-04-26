
package org.n52.ows.exception;

/**
 * List of versions in ―AcceptVersions‖ parameter value in GetCapabilities operation request did not include
 * any version supported by this server
 */
public class VersionNegotiationFailedException extends OwsException {

    private static final long serialVersionUID = -1275356358561628923L;

    public VersionNegotiationFailedException() {
        super(OwsExceptionCode.VERSION_NEGOTIATION_FAILED.getExceptionCode());
    }

    @Override
    public int getHttpStatusCode() {
        return BAD_REQUEST;
    }

}
