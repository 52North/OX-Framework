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
 
 Created on: 25.08.2005
 *********************************************************************************/

package org.n52.oxf.util;

import java.net.*;
import java.util.*;

/**
 * 
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class ParameterMap {
    
    Map<String, String> paramMap;

    public static String PARAMETER_PATTERN = "([^=&]+=[^=&]*&?)+";
    
    
    /**
     * 
     * @param parameterString
     *        should look like this: "param1=value1&param2=value2&...&paramN=valueN" <br>
     *        but also an empty 'value' is allowed (e.g.: "param1=&param2=value2&...") 
     */
    public ParameterMap(String paramString) {
        paramMap = new HashMap<String, String>();
        
        if(paramString.matches(PARAMETER_PATTERN)) {
            String[] paramParts = paramString.split("&");
            
            for(String paramPart : paramParts){
                String[] paramAndValue = paramPart.split("=");
                if (paramAndValue.length > 1) {
                    paramMap.put(paramAndValue[0], paramAndValue[1]);
                }
                else {
                    paramMap.put(paramAndValue[0], null);
                }
            }
        }
        else{
            throw new IllegalArgumentException("paramString does not match the pattern, received: " + paramString);
        }
    }
    
    /**
     * 
     * @param caseInsensitiveName
     * @return
     */
    public String getParameterValue(String caseInsensitiveName) {
        if (this.paramMap != null && caseInsensitiveName != null) {
            
            Iterator keyIterator = paramMap.keySet().iterator();
            while(keyIterator.hasNext()){
                String key = (String)keyIterator.next();
                if(key.equalsIgnoreCase(caseInsensitiveName)){
                    return paramMap.get(key);
                }
            }
        }
        return null;
    }
    

    /**
     * test it...
     * @param args
     * @throws MalformedURLException 
     */
    public static void main(String[] args) throws MalformedURLException {
        URL requestURL = new URL("http://server.uni-muenster.de:8080/service?SERVICE=WMS&LAYERS=SOS-WeatherSA-FOIs&EXCEPTIONS=application/vnd.ogc.se_xml&FORMAT=image/png&HEIGHT=432&TRANSPARENT=TRUE&REQUEST=GetMap&BBOX=16.53,-34.42,32.4,-22.27&WIDTH=565&STYLES=&SRS=EPSG:4326&VERSION=1.1.1 ");
        
        String paramString = requestURL.getQuery();
        
        System.out.println(paramString.matches(PARAMETER_PATTERN));
        
        if(paramString.matches(PARAMETER_PATTERN)) {
            String[] paramParts = paramString.split("&");
            
            for(String paramPart : paramParts){
                System.out.println(paramPart);
            }
        }
        
        System.out.println("value of parameter STYLES = " + new ParameterMap(paramString).getParameterValue("STYLES"));
    }
}