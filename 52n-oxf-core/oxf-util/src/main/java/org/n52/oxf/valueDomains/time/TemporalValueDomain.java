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

import java.util.*;
import org.n52.oxf.owsCommon.capabilities.*;

/**
 * This class is a ValueDomain for ITime objects. These objects could be instances of TimePosition or
 * TimePeriod.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 * 
 */
public class TemporalValueDomain implements IDiscreteValueDomain<ITime> {

    /**
     * a List of ITime objects; so either TimePosition or TimePeriod objects.
     */
    List<ITime> timeList;

    public TemporalValueDomain(ITime time) {
        this.timeList = new ArrayList<ITime>();
        add(time);
    }
    
    public TemporalValueDomain(List<ITime> timeList) {
        this.timeList = timeList;
    }

    /**
     * adds an ITime object to the timeList.
     * 
     * @param time
     */
    public void add(ITime time) {
        timeList.add(time);
    }

    /**
     * adds an ITime object to the timeList. This object will be generated from the timeString which has to
     * bee ISO8601 compliant.
     * 
     * @param timeString
     *        ISO8601 compliant timeString.
     */
    public void add(String timeString) {
        timeList.add(TimeFactory.createTime(timeString));
    }
    
    public boolean isEmpty(){
        return timeList.isEmpty();
    }

    /**
     * @return the List of all possible ITime objects.
     */
    public List<ITime> getPossibleValues() {
        return timeList;
    }

    /**
     * proofs whether the overgiven ITime object is contained in this TemporalValueDomain. (It also proofs if
     * time is contained in TimePeriods which are contained in this TemporalValueDomain.
     * 
     */
    //TODO: sind hier wirklich auch TimePeriods als Eingabe sinnvoll? oder nur TimePositions?
    public boolean containsValue(ITime time) {
        for (ITime t : timeList) {
            if (t instanceof ITimePosition && time instanceof ITimePeriod) {
                return false;
            }
            else if (t instanceof ITimePosition && time instanceof ITimePosition) {
                if (t.equals(time)) {
                    return true;
                }
            }
            else if (t instanceof ITimePeriod && time instanceof ITimePosition) {
                ITimePosition timePosition = (ITimePosition) time;
                ITimePeriod timePeriod = (ITimePeriod) t;

                if (timePeriod.contains(timePosition)) {
                    return true;
                }
            }
            else if (t instanceof ITimePeriod && time instanceof ITimePeriod) {
                ITimePeriod timePeriod2 = (ITimePeriod) time;
                ITimePeriod timePeriod1 = (ITimePeriod) t;

                if (timePeriod1.contains(timePeriod2)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getDomainDescription() {
        return "ValueDomain for ITime objects; so either TimePosition or TimePeriod objects.";
    }

    public String toXML() {
        String res = "<temporalDomain>";

        for (ITime t : timeList) {
            if (t instanceof ITimePeriod) {
                res += "<timePeriod>" + t + "</timePeriod>";
            }
            else if (t instanceof ITimePosition) {
                res += "<timePosition>" + t + "</timePosition>";
            }
        }

        res += "</temporalDomain>";
        return res;
    }

    /**
     * builds from the first element of the stringArray an ITime object. The string must be ISO8601 compliant.
     */
    public ITime produceValue(String... stringArray) {
        return TimeFactory.createTime(stringArray[0]);
    }

}