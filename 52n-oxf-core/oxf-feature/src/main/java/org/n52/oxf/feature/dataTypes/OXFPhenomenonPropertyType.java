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


package org.n52.oxf.feature.dataTypes;


/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class OXFPhenomenonPropertyType {

    private String urn;
    private String uom;
    
    /**
     * 
     */
    public OXFPhenomenonPropertyType(String urn) {
        this.urn = urn;
    }

    /**
     * 
     */
    public OXFPhenomenonPropertyType(String urn, String uom) {
        this.urn = urn;
        this.uom = uom;
    }
    
    public String getURN() {
        return urn;
    }
    
    public String getUOM() {
        return uom;
    }
    
    /**
     * 
     */
    @Override
    public boolean equals(Object o) {
        OXFPhenomenonPropertyType ppt = (OXFPhenomenonPropertyType)o;
        
        if (this.getUOM() == null && ppt.getUOM() != null){
        	return false;
        }
        
        if (this.getUOM() != null && ppt.getUOM() == null){
        	return false;
        }
        
        if (this.getUOM() == null && ppt.getUOM() == null){
        	return this.getURN().equals(ppt.getURN());
        }
        
        if (this.getURN().equals(ppt.getURN()) && this.getUOM().equals(ppt.getUOM()) ) {
            return true;
        }
        else {
            return false;
        }
    }
}