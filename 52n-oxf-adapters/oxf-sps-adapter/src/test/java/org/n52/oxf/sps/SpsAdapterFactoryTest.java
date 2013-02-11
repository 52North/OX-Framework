package org.n52.oxf.sps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.n52.oxf.sps.SpsAdapterFactory.createAdapter;

import org.junit.Test;

public class SpsAdapterFactoryTest {

    @Test(expected = NullPointerException.class) public void 
    shouldThrowExceptionWhenPassedServiceUrlIsNull() 
    throws Exception {
        createAdapter(null, null);
    }
    
    @Test(expected = NullPointerException.class) public void 
    shouldThrowExceptionWhenPassedVersionIsNull() 
    throws Exception {
        createAdapter("http://fake.url", null);
    }
    
    @Test(expected = MissingAdapterImplementationException.class) public void 
    shouldThrowExceptionWhenPassedVersionMatchesNoAdapterImplementation() 
    throws Exception {
        createAdapter("http://fake.url", "42");
    }

    @Test public void 
    shouldCreateSpsAdapterWithVersion100() 
    {
        String version = "1.0.0";
        try {
            SpsAdapter adapter = createAdapter("http://fake.url", version);
            assertThat(adapter.isSupporting(version), is(true));
        } catch (MissingAdapterImplementationException e) {
            fail("SpsAdapter with " + version + " could not be created");
        }
    }
    
}


