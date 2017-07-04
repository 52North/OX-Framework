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
package org.n52.oxf.valueDomains;

import org.hamcrest.CoreMatchers;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.n52.oxf.valueDomains.time.TimePosition;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:h.bredel@52north.org">Henning Bredel</a>
 */
public class TimePositionTest {

//    private static final Logger LOGGER = LoggerFactory.getLogger(TimePositionTest.class);

    @Test
    public void testTimePositionBeforeDaylightSavingSwitch() {
        final String beforeSwitch = "2007-10-28T02:55:00.000+02:00";
        final String atSwitch = "2007-10-28T02:00:00.000+01:00";
        final String afterSwitch = "2007-10-28T02:05:00.000+01:00";

        final DateTime jodaBeforeSwitch = DateTime.parse(beforeSwitch);
        final DateTime jodaAtSwitch = DateTime.parse(atSwitch);
        final DateTime jodaAfterSwitch = DateTime.parse(afterSwitch);

        Assert.assertTrue(jodaBeforeSwitch.isBefore(jodaAtSwitch));
        Assert.assertTrue(jodaBeforeSwitch.isBefore(jodaAfterSwitch));
        Assert.assertTrue(jodaAtSwitch.isBefore(jodaAfterSwitch));

        final TimePosition oxfBeforeSwitch = new TimePosition(beforeSwitch);
        final TimePosition oxfAtSwitch = new TimePosition(atSwitch);
        final TimePosition oxfAfterSwitch = new TimePosition(afterSwitch);

//        LOGGER.debug("JODA before: {}", jodaBeforeSwitch.toString());
//        LOGGER.debug("OX-F before: {}", oxfBeforeSwitch.toString());
//        LOGGER.debug("JODA at: {}", jodaAtSwitch.toString());
//        LOGGER.debug("OX-F at: {}", oxfAtSwitch.toString());
//        LOGGER.debug("JODA after: {}", jodaAfterSwitch.toString());
//        LOGGER.debug("OX-F after: {}", oxfAfterSwitch.toString());

        Assert.assertThat(jodaBeforeSwitch, CoreMatchers.is(DateTime.parse(oxfBeforeSwitch.toISO8601Format())));
        Assert.assertThat(jodaAtSwitch, CoreMatchers.is(DateTime.parse(oxfAtSwitch.toISO8601Format())));
        Assert.assertThat(jodaAfterSwitch, CoreMatchers.is(DateTime.parse(oxfAfterSwitch.toISO8601Format())));

        Assert.assertTrue(oxfBeforeSwitch.before(oxfAtSwitch));
        Assert.assertTrue(oxfBeforeSwitch.before(oxfAfterSwitch));
        Assert.assertTrue(oxfAtSwitch.before(oxfAfterSwitch));
    }

    @Test public void testIfCalendarCompareDoesRespectTimezone() {

        final String timeBeforeDaylightSwitch = "2007-10-28T02:55:00.000+02:00";
        final String timeAtDaylightSwitch = "2007-10-28T02:00:00.000+01:00";
        final String timeAfterDaylightSwitch = "2007-10-28T02:05:00.000+01:00";

        final TimePosition oxfTimeBeforeSwitch = new TimePosition(timeBeforeDaylightSwitch);
        final TimePosition oxfTimeAtSwitch = new TimePosition(timeAtDaylightSwitch);
        final TimePosition oxfTimeAfterSwitch = new TimePosition(timeAfterDaylightSwitch);

        Assert.assertTrue(oxfTimeBeforeSwitch.getCalendar().before(oxfTimeAtSwitch.getCalendar()));
        Assert.assertTrue(oxfTimeBeforeSwitch.getCalendar().before(oxfTimeAfterSwitch.getCalendar()));
        Assert.assertTrue(oxfTimeAtSwitch.getCalendar().before(oxfTimeAfterSwitch.getCalendar()));

    }
}
