/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
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


