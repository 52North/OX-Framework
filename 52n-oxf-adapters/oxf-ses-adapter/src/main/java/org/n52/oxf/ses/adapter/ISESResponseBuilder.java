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
