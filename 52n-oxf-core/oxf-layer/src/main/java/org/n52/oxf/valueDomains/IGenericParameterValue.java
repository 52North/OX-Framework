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