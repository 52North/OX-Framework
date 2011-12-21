/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 30.06.2005
 *********************************************************************************/

package org.n52.oxf.valueDomains.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.n52.oxf.owsCommon.capabilities.*;

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