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
package org.n52.oxf.ses.adapter;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.ParameterContainer;

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
    public static final String UNSUBSCRIBE_REFERENCE_XML = "unsubscribeReferenceXmlMarkup";
    
	public static final String DESTROY_REGISTRATION_SES_URL = "destroyRegistrationSesUrl";
	public static final String DESTROY_REGISTRATION_REFERENCE = "destroyRegistrationReference";

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

	public String buildDestroyRegistrationRequest(
			ParameterContainer parameterContainer) throws OXFException;

}
