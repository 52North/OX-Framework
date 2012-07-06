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
package org.n52.oxf.valueDomains.filter;

import java.util.*;
import org.n52.oxf.serviceAdapters.*;
import org.w3c.dom.*;

/**
 * Class represents a logical filter which is conform to OGC Filter
 * Encoding Specification 1.1.0
 * 
 * @author <a href="mailto:staschc@52north.org">Christoph Stasch</a>
 *
 */
public class LogicFilter implements IFilter {
    
    /** First Filter of the LogicFilter*/
    private IFilter leftFilter;
    
    /** Second and following Filters of the LogicFilter (only for AND and OR Filters)*/
    private ArrayList<IFilter> rightFilters;
    
    /**Type of LogicFilter (one of the filter types of this class. e.g. NOT)*/
    private String filterType;
    
    /**
     * Constructor with filterType as parameter
     * 
     * @param filterType
     *              one of the three logical filter types
     */
    public LogicFilter(String filterType){
        this.filterType = filterType;
    }

    /**
     * Constructor with all parameters, should be used as constructor for
     * AND and OR filters
     * 
     * @param filterType 
     *          type of this LogicFilter (AND or OR)
     * @param leftFilter
     *          firstFilter of the LogicFilter
     * @param rightFilters
     *          second and following filters of the logical filter
     */
    public LogicFilter(String filterType, IFilter leftFilter, ArrayList<IFilter> rightFilters){
        this.filterType = filterType;
        this.leftFilter = leftFilter;
        this.rightFilters = rightFilters;
    }
    
    /**
     * Constructor only useable for NOT Filters
     * 
     * @param filterType 
     *              logical filtertype (only NOT!!)
     * @param leftFilter 
     *              Filter which should be applied of a NOT Filter
     */
    public LogicFilter(String filterType, IFilter leftFilter) {
        this.filterType=IFilter.NOT;
        this.leftFilter=leftFilter;
    }
    
    /**
     * returns the type of this logical filter
     * 
     * @return the type of this logical filter (e.g. NOT)
     */
    public String getFilterType() {
        return filterType;
    }
    
    /**
     *
     * @return the first filter of this LogicFilter
     */
    public IFilter getLeftFilter() {
        return this.leftFilter;
    }
    
    /**
    *
    * @return the second filter and following filters of this LogicFilter as ArrayList
    */
    public ArrayList<IFilter> getRightFilters() {
        return this.rightFilters;
    }

    
    /**
     * creates a string representation of the logical filter (without <ogc:Filter> begin and end-tag!) 
     * in xml-format
     * 
     * @return logical filter as xml-string
     */
    public String toXML() {
       
        String result = "<"+filterType+">"; 
        result += this.leftFilter.toXML();
        
        //Adding second and following Filters to xml string
        if (!this.filterType.equals(IFilter.NOT)) {
        for (IFilter f: rightFilters){
        result += f.toXML();
        }
        }
        
        result += "</"+filterType+">"; 
    
        return result;
    }

    public String getClassName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    public ParameterShell initParameter(Element elem) {
        // TODO Auto-generated method stub
        return null;
    }

    public void serializeToContext(StringBuffer sb) {
        // TODO Auto-generated method stub
        
    }

    public boolean equals() {
        // TODO Auto-generated method stub
        return false;
    }

}