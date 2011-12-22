/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 21.10.2008
 *********************************************************************************/
package org.n52.oxf.util;

import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class TestIOHelper extends TestCase {

    private static Logger LOGGER = LoggingHandler.getLogger(TestIOHelper.class);
    
    /**
     * @throws java.lang.Exception
     */
    protected void setUp() throws Exception {
        
    }

    /**
     * Test method for {@link org.n52.oxf.util.IOHelper#sendPostMessage(java.lang.String, java.lang.String)}.
     */
    public void testGetHostConfiguration1() {
        try {
            URL serviceURL = new URL("http://my.sos.de?REQUEST=GetObservation");
            
            System.setProperty("http.proxyHost", "proxy");
            System.setProperty("http.proxyPort", "8080");
            
            HostConfiguration hostConfig_0 = IOHelper.getHostConfiguration(serviceURL);
            LOGGER.info("selected proxy is: " + hostConfig_0.getProxyHost());
            assertEquals("proxy", hostConfig_0.getProxyHost());
            
            System.setProperty("http.nonProxyHosts", "localhost|www.test.de");
            HostConfiguration hostConfig_1 = IOHelper.getHostConfiguration(serviceURL);
            LOGGER.info("selected proxy is: " + hostConfig_1.getProxyHost());
            assertEquals("proxy", hostConfig_1.getProxyHost());
            
            System.setProperty("http.nonProxyHosts", "my.sos.de");
            HostConfiguration hostConfig_2 = IOHelper.getHostConfiguration(serviceURL);
            LOGGER.info("selected proxy is: " + hostConfig_2.getProxyHost());
            assertEquals(null, hostConfig_2.getProxyHost());
            
            System.setProperty("http.nonProxyHosts", "localhost|my.sos.de");
            HostConfiguration hostConfig_3 = IOHelper.getHostConfiguration(serviceURL);
            LOGGER.info("selected proxy is: " + hostConfig_3.getProxyHost());
            assertEquals(null, hostConfig_3.getProxyHost());
            
            System.setProperty("http.nonProxyHosts", "");
            HostConfiguration hostConfig_4 = IOHelper.getHostConfiguration(serviceURL);
            LOGGER.info("selected proxy is: " + hostConfig_4.getProxyHost());
            assertEquals("proxy", hostConfig_4.getProxyHost());
            
            
        } catch(Exception e) {
            LOGGER.error(e,e);
            fail();
        }
    }
    
    public static void main(String[] args) {
        String nonProxyHosts = "scheisse|test";
        String[] nonProxyHostsArray = nonProxyHosts.split("\\|"); // u007C = unicode for "|"
        System.out.print(nonProxyHostsArray.length);
    }
}