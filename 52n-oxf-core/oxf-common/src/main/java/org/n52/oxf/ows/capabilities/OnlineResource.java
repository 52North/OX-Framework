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

/**
 * 
 * This class refers to CI_OnlineResource of ISO 19115 and to the XLink Recommendation.
 * (ie. http://www.w3.org/TR/2001/REC-xlink-20010627/)
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OnlineResource {
    
	public static String TYPE_EXTENDED	= "extended";
	public static String TYPE_SIMPLE	= "simple";
	public static String TYPE_RESOURCE	= "resource";
	public static String TYPE_LOCATOR	= "locator";
	public static String TYPE_ARC		= "arc";
	public static String TYPE_TITLE		= "title";
	
    /**
     * The allowed values for 'type' are: extended, simple, resource, locator, arc, title.
     * Standard is 'simple'. This should be ok. So please be careful in changing this attribute.
     */
    private String type;
    
    private String href;
    
    private String role;
    
    private String arcrole;
    
    private String show;
    
    private String actuate;
    
    private String title;
    
    
    /**
     * this constructor has all required attributes as its parameters.
     * @param href
     */
    public OnlineResource(String href){
        setType("simple");
        setHref(href);
    }
	
	/**
	 * this constructor has all required attributes as its parameters.
	 * @param type
	 * @param href
	 */
	public OnlineResource(String type, String href){
		setType(type);
		setHref(href);
	}
	
	/**
	 * this constructor has all attributes as its parameters.
	 * @param type
	 * @param href
	 * @param role
	 * @param arcrole
	 * @param show
	 * @param actuate
	 * @param title
	 */
	public OnlineResource(String type, String href, String role, String arcrole, String show, String actuate, String title){
		setType(type);
		setHref(href);
		setRole(role);
		setArcrole(arcrole);
		setShow(show);
		setActuate(actuate);
		setTitle(title);
	}
	
	/**
	 * @return a XML representation of this OnlineResource. 
	 */
	public String toXML(){
		String res = "<OnlineResource"
				+ " type=\""	+ type + "\""
				+ " href=\""	+ href + "\""
				+ " role=\""	+ role + "\""
				+ " arcrole=\"" + arcrole + "\""
				+ " show=\""	+ show + "\""
				+ " actuate=\"" + actuate + "\""
				+ " title=\""	+ title + "\"/>";
		
		return res;
	}
	
    /**
     * @return Returns the actuate.
     */
    public String getActuate() {
        return actuate;
    }
    /**
     * @param actuate The actuate to set.
     */
    protected void setActuate(String actuate) {
        this.actuate = actuate;
    }
    /**
     * @return Returns the arcrole.
     */
    public String getArcrole() {
        return arcrole;
    }
    /**
     * @param arcrole The arcrole to set.
     */
	protected void setArcrole(String arcrole) {
        this.arcrole = arcrole;
    }
    /**
     * @return Returns the href.
     */
    public String getHref() {
        return href;
    }
    /**
     * @param href The href to set.
     */
	protected void setHref(String href) {
        this.href = href;
    }
    /**
     * @return Returns the role.
     */
    public String getRole() {
        return role;
    }
    /**
     * @param role The role to set.
     */
	protected void setRole(String role) {
        this.role = role;
    }
    /**
     * @return Returns the show.
     */
    public String getShow() {
        return show;
    }
    /**
     * @param show The show to set.
     */
	protected void setShow(String show) {
        this.show = show;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
	protected void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the type.
     */
    public String getType() {
        return type;
    }
    /**
     * 
     * @param type the parameter must be compliant to the recommendation. There are only some allowed. 
     * Standard is simple. This should be ok. So please be careful in changing this attribute.
     */
	protected void setType(String type) {
        this.type = type;
    }
}