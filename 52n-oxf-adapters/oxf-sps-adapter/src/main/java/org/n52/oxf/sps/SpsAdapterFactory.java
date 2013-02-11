package org.n52.oxf.sps;

import static java.lang.String.format;

import org.n52.oxf.sps.v100.SpsAdapterV100;

public class SpsAdapterFactory {
    
    private static final String VERSION_100 = "1.0.0";
    
    private static final String VERSION_200 = "2.0.0";
    
    private SpsAdapterFactory() {
        // do not instantiate static factory
    }
    
    public static SpsAdapter createAdapter(String serviceUrl, String version) throws MissingAdapterImplementationException {
        if (serviceUrl == null) {
            throw new NullPointerException("Service URL must not be null.");
        }
        if (version == null) {
            throw new NullPointerException("Service version must not be null.");
        }
        if (VERSION_100.equals(version)) {
            return new SpsAdapterV100(serviceUrl);
        } else {
            String msg = "No adapter found for version '%s'.";
            throw new MissingAdapterImplementationException(format(msg, version));
        }
    }

}
