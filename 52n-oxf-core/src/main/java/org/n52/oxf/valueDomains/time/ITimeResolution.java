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
 
 Created on: 18.05.2005
 *********************************************************************************/
package org.n52.oxf.valueDomains.time;

import org.n52.oxf.owsCommon.capabilities.*;

/**
 * Specifies a resolution for time by defining
 * year, month, day, hours, minutes and seconds. 
 */
public interface ITimeResolution extends ITime {
    
    long getYears();
    
    int getMonths();
    
    int getDays();
    
    int getHours();

    int getMinutes();
    
    float getSeconds();

}