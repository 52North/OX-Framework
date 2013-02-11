package org.n52.oxf.sps;

public class MissingAdapterImplementationException extends Exception {

    private static final long serialVersionUID = -5354685062505767985L;
    
    private String service;
    
    private String version;

    public MissingAdapterImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAdapterImplementationException(String message) {
        super(message);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
}
