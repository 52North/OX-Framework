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

package org.n52.oxf.adapter.wcs;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.wcs.caps.CoverageDataset;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates how to use the WCSAdapter. You might use it as an example for your own code.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TestWCSAdapter {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestWCSAdapter.class);

    private final String url = "http://ogcdemo.pcigeomatics.com:8181/swe/wcs";

    public static void main(String[] args) throws OXFException, ExceptionReport {
        new TestWCSAdapter().testServiceInitialization();
    }

    public void testServiceInitialization() throws ExceptionReport, OXFException {

        WCSAdapter adapter = new WCSAdapter();

        ServiceDescriptor desc = adapter.initService(url);

        CoverageDataset coverage = (CoverageDataset) desc.getContents().getDataIdentification(0);

        LOGGER.info("Coverage Title: {}", coverage.getTitle());
    }

}