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