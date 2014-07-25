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
package org.n52.oxf.valueDomains.time;

import static org.hamcrest.CoreMatchers.is;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.n52.oxf.ows.capabilities.ITime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Henning Bredel <h.bredel@52north.org>
 */
public class TimeFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeFactoryTest.class);

    @Test
    public void
            createTimeValuesBeforeAtAndAfterDaylightSavingTimeSwitch() {

        String beforeSwitch = "2007-10-28T02:55:00.000+02:00";
        String atSwitch = "2007-10-28T02:00:00.000+01:00";
        String afterSwitch = "2007-10-28T02:05:00.000+01:00";

        ITime oxfBeforeSwitch = TimeFactory.createTime(beforeSwitch);
        ITime oxfAtSwitch = TimeFactory.createTime(atSwitch);
        ITime oxfAfterSwitch = TimeFactory.createTime(afterSwitch);

//        LOGGER.debug("OX-F before: {}", oxfBeforeSwitch.toISO8601Format());
//        LOGGER.debug("OX-F at: {}", oxfAtSwitch.toISO8601Format());
//        LOGGER.debug("OX-F after {}:", oxfAfterSwitch.toISO8601Format());

        DateTime jodaBeforeSwitch = DateTime.parse(beforeSwitch);
        DateTime jodaAtSwitch = DateTime.parse(atSwitch);
        DateTime jodaAfterSwitch = DateTime.parse(afterSwitch);

        assertThat(jodaBeforeSwitch, is(DateTime.parse(oxfBeforeSwitch.toISO8601Format())));
        assertThat(jodaAtSwitch, is(DateTime.parse(oxfAtSwitch.toISO8601Format())));
        assertThat(jodaAfterSwitch, is(DateTime.parse(oxfAfterSwitch.toISO8601Format())));



    }
}
