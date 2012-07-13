package org.n52.oxf.util;

public class SosUtil {

    /**
     * The Type of the service which is connectable by this ServiceAdapter
     */
    public static final String SERVICE_TYPE = "SOS";
    
    /**
     * the Versions of the services which are connectable by this ServiceAdapter.
     * Array contains: [0.0.0, 1.0.0, 2.0.0]
     */
    public static final String[] SUPPORTED_VERSIONS = {"0.0.0", "1.0.0", "2.0.0"};

    public static boolean isVersion000(String version) {
        return SUPPORTED_VERSIONS[0].equals(version);
    }

    public static boolean isVersion100(String version) {
        return SUPPORTED_VERSIONS[1].equals(version);
    }

    public static boolean isVersion200(String version) {
        return SUPPORTED_VERSIONS[2].equals(version);
    }

}
