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
 
 Created on: 21.06.2005
 *********************************************************************************/
package org.n52.oxf.valueDomains;

import java.util.*;
import org.n52.oxf.owsCommon.capabilities.*;



public class ObjectValueDomain implements IDiscreteValueDomain<Object> {
  
    private ArrayList<Object> possibleValues;
    
    private final String DOMAIN_DESCRIPTION="Generic ValueDomain for objects of type Object";
    
    
    public ObjectValueDomain(ArrayList<Object> possibleValues) {
        this.possibleValues = possibleValues;
    }

    public List getPossibleValues() {
        return this.possibleValues;
    }

    public boolean containsValue(Object object) {
        for (Object o : possibleValues) {
            if (o.equals(this)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @return domain description
     */
    public String getDomainDescription() {
        return DOMAIN_DESCRIPTION;
    }

    /**
     * 
     * @return xml-string of this valueDomain
     */
    public String toXML() {
        String result="";
        for (int i=0; i<this.possibleValues.size();++i) {
            result += this.possibleValues.get(i).toString();
        }
        return result;
    }
    

    public Object produceValue(String... stringArray) {
        // TODO Auto-generated method stub
        return null;
    }

}