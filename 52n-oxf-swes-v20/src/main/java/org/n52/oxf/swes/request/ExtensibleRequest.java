package org.n52.oxf.swes.request;

import org.apache.xmlbeans.XmlObject;

public interface ExtensibleRequest {

    public boolean isSetExtensions();
    
    /**
     * @return an Array of extension parameters (is of length 0 when no extension were given).
     */
    public XmlObject[] getExtensions();
}
