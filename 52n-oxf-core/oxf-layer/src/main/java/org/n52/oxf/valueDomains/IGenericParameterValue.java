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

package org.n52.oxf.valueDomains;

import org.n52.oxf.adapter.*;
import org.n52.oxf.serialization.*;
import org.w3c.dom.*;

/**
 * This Parameter is used to represent Parameters, which are not covered directly by the OXF.
 * 
 * This class should write something like this XML snippet in
 * {@link org.n52.oxf.serialization.WritableContext#writeTo(StringBuffer)}: <p/>
 * 
 * <pre>
 *  &lt;GenericParameter name=&quot;className&quot;&gt;
 *  .... SOME PARAMETER STUFF
 *  &lt;/GenericParameter&gt;
 * </pre>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public interface IGenericParameterValue extends WritableContext {

    /**
     * informs about the current implementing class...
     * 
     * @return full qualified java class name
     */
    String getClassName();

    /**
     * the name of the parameter
     * 
     * @return
     */
    String getName();

    /**
     * 
     * @param elem
     * @return
     */
    ParameterShell initParameter(Element elem);

}