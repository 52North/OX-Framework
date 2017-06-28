/*
 * ﻿Copyright (C) 2012-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the Free
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
package org.n52.oxf.ses.adapter;

import org.n52.oxf.adapter.ParameterContainer;
import org.n52.oxf.adapter.ParameterShell;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 03.08.2009
 * @deprecated where should this be used?!
 */
public interface ISESResponseBuilder {
    
    public static final String NOTIFY_SOAP_ENVELOPE_HEADER_TO = "notifySoapEnvHeaderTo";
    
    public static final String NOTIFY_SOAP_ENVELOPE_HEADER_FROM = "notifySoapEnvHeaderFrom";
    
    public static final String NOTIFY_SOAP_ENVELOPE_HEADER_RELATES_TO = "notifySoapEnvHeaderRelatesTo";
    
    public static final String NOTIFY_SOAP_ENVELOPE_HEADER_MESSAGE_ID = "notifySoapEnvHeaderMsgId";
    
    /**
     * Builds the NotifyResponse<br>
     * If the {@link ParameterContainer} does not contain a {@link ParameterShell} with the common name of {@link ISESResponseBuilder#NOTIFY_SOAP_ENVELOPE_HEADER_MESSAGE_ID}
     * a system generated message id will be defined
     * @param parameter
     * @return
     */
    public String buildNotifyResponseRequest(ParameterContainer parameter);

}
