package org.n52.oxf.request;

import java.io.InputStream;

/**
 * <b>This class is test only yet!</b>
 */
public interface RequestBuilder {

    public InputStream buildRequest(RequestParameters parameters) throws ProcessingRequestException;
}
