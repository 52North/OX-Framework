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