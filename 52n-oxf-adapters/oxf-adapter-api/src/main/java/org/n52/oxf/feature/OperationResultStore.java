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
package org.n52.oxf.feature;

import static org.n52.oxf.ows.capabilities.Parameter.COMMON_NAME_VERSION;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;
import org.n52.oxf.xmlbeans.parser.XMLBeansParser;
import org.n52.oxf.xmlbeans.parser.XMLHandlingException;

public abstract class OperationResultStore {

    protected XmlObject xmlObject;
    
    protected String version;

    /**
     * @deprecated Use argument constructor {@link OperationResultStore#OperationResultStore(OperationResult)}
     *             with {@link #unmarshalFeatures(OperationResult)}
     */
    protected OperationResultStore() {
        // for backward compatibility .. TODO remove when deprecated contructor is going to be removed
    }

    protected OperationResultStore(OperationResult operationResult) throws OXFException {
        try {
            this.xmlObject = XMLBeansParser.parse(operationResult.getIncomingResultAsStream(), false);
            this.version = getVersion(operationResult);
        }
        catch (XMLHandlingException e) {
            throw new OXFException("Could not parse OperationResult.", e);
        }
    }

    protected String getVersion(OperationResult operationResult) {
        ParameterContainer parameters = operationResult.getUsedParameters();
        ParameterShell shell = parameters.getParameterShellWithCommonName(COMMON_NAME_VERSION);
        return (String) shell.getSpecifiedValue();
    }

}
