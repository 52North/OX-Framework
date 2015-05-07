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

import java.util.ArrayList;
import java.util.List;

import org.n52.oxf.ows.capabilities.IDiscreteValueDomain;
import org.n52.oxf.ows.capabilities.ITime;

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
    
    public TemporalValueDomain(ITime[] possibleValues){
        this.timeList = new ArrayList<ITime>(java.util.Arrays.asList(possibleValues));
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
            else if (t.equals(time)) {
                return true;
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