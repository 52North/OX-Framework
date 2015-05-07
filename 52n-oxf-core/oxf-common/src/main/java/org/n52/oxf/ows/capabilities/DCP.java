/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.ows.capabilities;

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