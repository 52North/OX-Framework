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

/**
 * Represents a TimePeriod specified in OGC WCS spec 1.0.0 and consists of the TimePositions start and end and
 * the TimeResolution resolution.
 * 
 * Valid example time period strings: <li>1998-11-01/2005-11-02</li> <li>
 * 1998-11-01/2005-11-02/P1Y</li>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public class TimePeriod implements ITimePeriod {

    public static String PERIOD_PATTERN = ".+/.+/.+";
    private ITimePosition start;
    private ITimePosition end;
    private ITimeResolution resolution;

    /**
     * constructs a TimePeriod without a resolution. The default resolution has to be set explicitly.
     */
    public TimePeriod(String begin, String end) {
        this.start = new TimePosition(begin);
        this.end = new TimePosition(end);
    }

    /**
     * 
     * @param currentStart
     * @param currentEnd
     * @param currentResolution
     */
    public TimePeriod(ITimePosition currentStart, ITimePosition currentEnd, ITimeResolution currentResolution) {
        this.start = currentStart;
        this.end = currentEnd;
        this.resolution = currentResolution;
    }

    /**
     * 
     * @param period
     */
    public TimePeriod(String period) {
        if (period == null) {
            throw new NullPointerException();
        }
        if (period.matches(PERIOD_PATTERN)) {
            String[] periodParts = period.split("/");
            start = new TimePosition(periodParts[0]);
            end = new TimePosition(periodParts[1]);
            resolution = new TimeResolution(periodParts[2]);
        }
        else {
            throw new IllegalArgumentException("period does not match ISO compliant time pattern, received: " + period);
        }
    }

    /**
     * 
     * @param currentStart
     * @param currentEnd
     */
    public TimePeriod(ITimePosition currentStart, ITimePosition currentEnd) {
        this.start = currentStart;
        this.end = currentEnd;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ITimePeriod) {

            ITimePeriod timePeriod = (ITimePeriod) obj;

            if (timePeriod.toISO8601Format().equals(this.toISO8601Format())) {
                return true;
            }
        }
        return false;
    }

    public ITimePosition getStart() {
        return start;
    }

    public ITimePosition getEnd() {
        return end;
    }

    public ITimeResolution getResolution() {
        return resolution;
    }

    /**
     * Sets a default resolution iff the resolution has not been set yet in any other method.
     */
    public void setDefaultResolution(String resolution) {
        if (this.resolution == null) {
            this.resolution = new TimeResolution(resolution);
        }
    }

    public boolean isResolutionSet() {
        return resolution != null;
    }

    public String toISO8601Format() {
        return start.toISO8601Format() + "/" + end.toISO8601Format() + (resolution != null ? ("/" + resolution) : "");
    }

    @Override
    public String toString() {
        return "start: " + start.toString() + "  end: " + end.toString();
    }

    /**
     * proofs whether timePeriod is contained in this TimePeriod.
     */
    public boolean contains(ITimePeriod timePeriod) {
        return (start.before(timePeriod.getStart()) || start.equals(timePeriod.getStart()))
                && (end.after(timePeriod.getEnd()) || end.equals(timePeriod.getEnd()));
    }

    /**
     * proofs whether timePosition is contained in this TimePeriod.
     */
    public boolean contains(ITimePosition timePos) {
        return (start.before(timePos) || start.equals(timePos)) && (end.after(timePos) || end.equals(timePos));
    }

}