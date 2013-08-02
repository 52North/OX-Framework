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

package org.n52.oxf.valueDomains.time;

import java.util.regex.*;

/**
 * Represents a TimeResolution as specified in ISO8601:2004. Inputs are validated against this pattern:
 * "P(\\d+Y)?(\\d+M)?(\\d+D)?(T(\\d+H)?(\\d+M)?(\\d+([.]\\d+)?S)?)?" <br>
 * Valid example time strings: <li>P2Y</li> <li>P1Y1M3DT6H2M8.5S</li>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 */
public class TimeResolution implements ITimeResolution {

    private long years = NOT_SET;
    private int months = NOT_SET;
    private int days = NOT_SET;
    private int hours = NOT_SET;
    private int minutes = NOT_SET;
    private float seconds = NOT_SET;
    public static final String RESOLUTION_PATTERN = "P(\\d+Y)?(\\d+M)?(\\d+D)?(T(\\d+H)?(\\d+M)?(\\d+([.]\\d+)?S)?)?";

    private String resString;

    /**
     * constructs a timeResolution. Validates against the pattern mentioned in the ISO8601:2004 spec (section
     * 4.4.4.2.1). This pattern is extended in order to support a number of days with more than 2 digits.
     * 
     * @param res
     */
    public TimeResolution(String res) {
        if (res == null) {
            throw new NullPointerException();
        }
        if (res.equals("P")) {
            throw new IllegalArgumentException("resolution has to be more specified. Not only P!");
        }
        if (res.endsWith("T")) {
            throw new IllegalArgumentException(res + " : ends with T. That indicates a following time"
                    + "which is missing. Add time or remove T!");
        }
        Pattern resPattern = Pattern.compile(RESOLUTION_PATTERN);
        Matcher matcher = resPattern.matcher(res);
        if ( !matcher.matches()) {
            throw new IllegalArgumentException("Resolution String does not match the pattern");
        }

        // indicates whether the group containing "T" has been passed.
        boolean tPassed = false;
        for (int i = 1; i <= matcher.groupCount(); i++) {
            String group = matcher.group(i);
            if (group != null && !group.startsWith("T")) {
                if (group.endsWith("Y")) {
                    years = Long.parseLong(group.substring(0, group.length() - 1));
                }
                else if (group.endsWith("M") && !tPassed) {
                    // MONTH FOUND
                    months = Integer.parseInt(group.substring(0, group.length() - 1));
                }
                else if (group.endsWith("D")) {
                    days = Integer.parseInt(group.substring(0, group.length() - 1));
                }
                else if (group.endsWith("H")) {
                    hours = Integer.parseInt(group.substring(0, group.length() - 1));
                }
                else if (group.endsWith("M") && tPassed) {
                    // MINUTE FOUND
                    minutes = Integer.parseInt(group.substring(0, group.length() - 1));
                }
                else if (group.endsWith("S")) {
                    seconds = Float.parseFloat(group.substring(0, group.length() - 1));
                }
            }
            else {
                tPassed = true;
            }
        }

        this.resString = res;
    }

    public long getYears() {
        return years;
    }

    public int getMonths() {
        return months;
    }

    public int getDays() {
        return days;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public float getSeconds() {
        return seconds;
    }

    public String toISO8601Format() {
        return resString;
    }

    @Override
    public String toString() {
        return toISO8601Format();
    }
}