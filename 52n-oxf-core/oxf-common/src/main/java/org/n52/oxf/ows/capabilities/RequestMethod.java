/**
 * ﻿Copyright (C) 2012-2014 52°North Initiative for Geospatial Open Source
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

import java.util.Arrays;

/**
 *  Specifies the type of request of a specific operation. Only HTTP is supported.
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public abstract class RequestMethod {
    
	/**
	 * required
	 */
	private OnlineResource or;
	
	/**
	 * optional
	 */
    private String[] constraints;
	
	
	/**
	 * this constructor has all attributes as its parameters.
	 * @param or
	 * @param type
	 * @param constraints
	 */
	public RequestMethod(OnlineResource or, String[] constraints){
		setOnlineResource(or);
		setConstraints(constraints);
	}
	
	/**
	 * this constructor has all required attributes as its parameters.
	 * @param or
	 * @param type
	 */
	public RequestMethod(OnlineResource or){
		setOnlineResource(or);
	}
	
    /**
     * @return a XML representation.
     */
    public abstract String toXML();
	
    /**
     * @return Returns the constraints.
     */
    public String[] getConstraints() {
        return constraints;
    }
    /**
     * @param constraints The constraints to set.
     */
    protected void setConstraints(String[] constraints) {
        this.constraints = constraints;
    }
    /**
     * 
     * @return
     */
    public OnlineResource getOnlineResource() {
        return or;
    }
    /**
     * 
     * @param url
     */
    protected void setOnlineResource(OnlineResource url) {
        this.or = url;
    }

	@Override
	public String toString() {
		return String.format("RequestMethod [onlineResource=%s, constraints=%s]", 
				or,
				Arrays.toString(constraints));
	}
    
}