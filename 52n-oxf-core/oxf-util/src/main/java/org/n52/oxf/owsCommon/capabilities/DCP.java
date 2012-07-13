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
package org.n52.oxf.owsCommon.capabilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the different RequestMethods. Currently only HTTP POST and GET supported.
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class DCP {
    
    private List<RequestMethod> requestMethods;
	
    /**
     * 
     * @param httpGet
     * @param httpPost
     */
	public DCP(GetRequestMethod httpGet, PostRequestMethod httpPost){
        requestMethods = new ArrayList<RequestMethod>();
        
        addHTTPGet(httpGet);
        addHTTPPost(httpPost);
	}
	
    /**
     * 
     * @param methods
     */
    public DCP(List<RequestMethod> methods){
        requestMethods = methods;
    }
    
	/**
	 * @return a XML representation of this DCP. 
	 */
	public String toXML(){
		String res = "<DCP>";
		
		if(requestMethods != null) {
            for (RequestMethod method : requestMethods) {
                method.toXML();
            }
        }
		res += "</DCP>";
		
		return res;
	}
	
    /**
     * @return Returns a list of all HTTPGetRequestMethods.
     */
    public List<GetRequestMethod> getHTTPGetRequestMethods() {
        List<GetRequestMethod> getRequestMethods = new ArrayList<GetRequestMethod>();
        
        for (RequestMethod method : requestMethods) {
            if(method instanceof GetRequestMethod){
                getRequestMethods.add((GetRequestMethod)method);
            }
        }
        
        return getRequestMethods;
    }
    
    /**
     * @param get The hTTPGet to set.
     */
    protected void addHTTPGet(GetRequestMethod getMethod) {
        this.requestMethods.add(getMethod);
    }

    /**
     * @return Returns a list of all HTTPPostRequestMethods.
     */
    public List<PostRequestMethod> getHTTPPostRequestMethods() {
        List<PostRequestMethod> postRequestMethods = new ArrayList<PostRequestMethod>();
        
        for (RequestMethod method : requestMethods) {
            if(method instanceof PostRequestMethod){
                postRequestMethods.add((PostRequestMethod)method);
            }
        }
        
        return postRequestMethods;
    }
    
    /**
     * @param post The hTTPPost to set.
     */
    protected void addHTTPPost(PostRequestMethod postMethod) {
        this.requestMethods.add(postMethod);
    }

	@Override
	public String toString() {
		return String.format("DCP [requestMethods=%s]", requestMethods);
	}
    
}