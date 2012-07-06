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
package org.n52.oxf.serviceAdapters.ses;

import org.n52.oxf.OXFException;
import org.n52.oxf.serviceAdapters.ParameterContainer;

/**
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * @version 20.07.2009
 */
public interface ISESRequestBuilder {

    public static final String GET_CAPABILITIES_SES_URL = "getCapabilitiesSesUrl";

    public static final String REGISTER_PUBLISHER_SES_URL = "registerPublisherSesUrl";
    public static final String REGISTER_PUBLISHER_LIFETIME_DURATION = "registerPublisherLifetimeDuration";
    public static final String REGISTER_PUBLISHER_TOPIC = "registerPublisherTopic";
    public static final String REGISTER_PUBLISHER_TOPIC_DIALECT = "registerPublisherTopicDialect";
    public static final String REGISTER_PUBLISHER_SENSORML = "registerPublisherSensorML";
    public static final String REGISTER_PUBLISHER_FROM = "registerPublisherFrom";

    public static final String SUBSCRIBE_SES_URL = "subscribeSesUrl";
    public static final String SUBSCRIBE_CONSUMER_REFERENCE_ADDRESS = "subscribeConsumerReferenceAddress";
    public static final String SUBSCRIBE_FROM = "subscribeFrom";
    public static final String SUBSCRIBE_FILTER_TOPIC_DIALECT = "subscribeFilterTopicDialect";
    public static final String SUBSCRIBE_FILTER_TOPIC = "subscribeFilterTopic";
    public static final String SUBSCRIBE_FILTER_MESSAGE_CONTENT_DIALECT = "subscribeFilterMessageContentDialect";
    public static final String SUBSCRIBE_FILTER_MESSAGE_CONTENT = "subscribeFilterMessageContent";
    public static final String SUBSCRIBE_INITIAL_TERMINATION_TIME = "subscribeInitialTerminationTime";

    public static final String DESCRIBE_SENSOR_SENSOR_ID = "describeSensorSensorID";
    public static final String DESCRIBE_SENSOR_SES_URL = "describeSensorSesUrl";

    public static final String NOTIFY_SES_URL = "notifySesUrl";
    public static final String NOTIFY_TOPIC_DIALECT = "notifyTopicDialect";
    public static final String NOTIFY_TOPIC = "notifyTopic";
    public static final String NOTIFY_XML_MESSAGE = "notifyXmlMessage";
    
    public static final String UNSUBSCRIBE_SES_URL = "unsubscribeSesUrl";
    public static final String UNSUBSCRIBE_REFERENCE = "unsubscribeReference";

    /**
     * Builds the GetCapabilities SOAP SES Request
     * (section 10.1.1.1 of OGC 08-133 SES Interface Description DP)
     * @param parameters MUST contain the target URL of the SES
     * @return the GetCapabilities SOAP SES Request
     * @throws OXFException if problems with the SOAP implementation occur 
     */
    public String buildGetCapabilitiesRequest(ParameterContainer parameter) throws OXFException;

    /**
     * Build the RegisterPublisher SOAP SES Request
     * (section 10.5.2.1 of OGC 08-133 SES Interface Description DP)
     * @param parameter
     * @return
     * @throws OXFException
     */
    public String buildRegisterPublisherRequest(ParameterContainer parameter) throws OXFException;

    public String buildSubscribeRequest(ParameterContainer parameter) throws OXFException;
    
    public String buildUnsubscribeRequest(ParameterContainer parameter) throws OXFException;

    public String buildDescribeSensorRequest(ParameterContainer parameter) throws OXFException;

    public String buildNotifyRequest(ParameterContainer parameter) throws OXFException;

}
