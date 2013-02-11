
package org.n52.ows.request.getcapabilities;

/**
 * <b>This class is test only yet!</b>
 * <br><br>
 * The sections names as specified by the SPS 1.0.0 implementation specification (OGC 07-014r3).<br>
 * <br>
 * Call {@link #toString()} method to retrieve the section names as defined in the spec.
 */
public enum GetCapabilitiesSection {
    /**
     * Metadata about this specific server.
     */
    SERVICE_IDENTIFICATION("ServiceIdentification"),
    /**
     * Metadata about the organization operating this server.
     */
    SERVICE_PROVIDER("ServiceProvider"),
    /**
     * Metadata about the operations specified by this service and implemented by this server, including the
     * URLs for operation requests.
     */
    OPERATION_METADATA("OperationsMetadata"),
    /**
     * Metadata about the data served by this server.
     */
    CONTENTS("Contents");

    private String sectionName;

    private GetCapabilitiesSection(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * @return the section name defined within the spec.
     */
    @Override
    public String toString() {
        return sectionName;
    }
}