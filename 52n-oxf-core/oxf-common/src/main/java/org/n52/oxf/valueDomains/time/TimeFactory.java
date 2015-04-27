/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.n52.oxf.ows.capabilities.*;

/**
 * Creates an appropriate ITime object, (--> TimePeriod or TimePosition)
 */
public class TimeFactory {

    public static DateFormat ISO8601LocalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * 
     * @param timeString
     * @return an appropriate ITime object depending on the timeString that may be "now" for the most recent
     *         available data, a TimePosition (e.g. "2005-08-04") or "min/max(/res)" to create a TimePeriod.
     * 
     * @throws IllegalArgumentException
     *         if timeString is not in correct format.
     */
    public static ITime createTime(String timeString) throws IllegalArgumentException {
        if (timeString.equals("now")) {
            TimePosition now = new TimePosition(timeString);
            return now;
        }
        if (timeString.contains("/")) {
            // TIME=min/max/res
            if (timeString.split("/").length == 3) {
                TimePeriod period = new TimePeriod(timeString);
                return period;
            }
            else if (timeString.split("/").length == 2) {
                TimePeriod period = new TimePeriod(timeString.split("/")[0], timeString.split("/")[1]);
                return period;
            }
            else {
                throw new IllegalArgumentException("Time parameter is not in correct format");
            }
        }
        // TIME=timePos
        return new TimePosition(timeString);
    }
}