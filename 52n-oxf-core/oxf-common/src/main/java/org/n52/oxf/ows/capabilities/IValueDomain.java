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
 * This class can be seen as a special container. It is used by the class <a href=Parameter.html>Parameter</a>
 * to lay down the domain of values which can be associated with the Parameter.<br>
 * A central point of this class is the <code>containsVaue(T t)</code> method which can be used by the
 * client to proof whether a value is contained by the domain or not.
 * 
 * Some remarks on implementing this interface: Please don�t allow empty constructors. Because empty constructors
 * dont make sense for the framework.
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * @param <T>
 *        T is the type of the values that are contained in and can be added to this IValueDomain.
 * @see <a href=IRangeValueDomain.html>IRangeValueDomain</a>
 * @see <a href=IDiscreteValueDomain.html>IDiscreteValueDomain</a>
 */
public interface IValueDomain<T> {

    /**
     * @return if the value t is part of this value domain.
     */
    public boolean containsValue(T t);

    /**
     * @return plain text description of this valueDomain.
     */
    public String getDomainDescription();

    /**
     * @return a XML representation of this ValueDomain.
     */
    public String toXML();
    
//    /**
//     * @return the type of values which can be added to this IValueDomain.
//     */
//    public Class<T> getValueClass();
    
    public T produceValue(String... stringArray);
}