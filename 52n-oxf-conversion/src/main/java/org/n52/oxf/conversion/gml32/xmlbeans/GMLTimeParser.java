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
package org.n52.oxf.conversion.gml32.xmlbeans;

import net.opengis.gml.x32.AbstractTimePrimitiveType;
import net.opengis.gml.x32.TimeInstantPropertyType;
import net.opengis.gml.x32.TimeInstantType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.gml.x32.TimePrimitivePropertyType;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Helper class for parsing GML time objects.
 */
public class GMLTimeParser {

	/**
	 * @param validTime a GML time primitive object
	 * @return a joda-time interval
	 */
	public static Interval parseTimePrimitive(TimePrimitivePropertyType validTime) {
		AbstractTimePrimitiveType primitive = validTime.getAbstractTimePrimitive();
		if (primitive instanceof TimePeriodType) {
			TimePeriodType period = (TimePeriodType) primitive;
			if (period.isSetBegin() && period.isSetEnd()) {
				return parseByTimeInstant(period.getBegin(), period.getEnd());
			}
			else if (period.isSetBeginPosition() && period.isSetEndPosition()) {
				return parseByTimePosition(period.getBeginPosition(), period.getEndPosition());
			}
		}
		else if (primitive instanceof TimeInstantType) {
			TimeInstantType instance = (TimeInstantType) primitive;
			DateTime position = parseTimePosition(instance.getTimePosition());
			return new Interval(position, position);
		}
		throw new UnsupportedOperationException("Currently, only gml:TimePeriodType and gml:TimeInstantType are supported.");
	}

	private static Interval parseByTimePosition(TimePositionType beginPosition,
			TimePositionType endPosition) {
		DateTime begin = parseTimePosition(beginPosition);
		DateTime end = parseTimePosition(endPosition);
		return new Interval(begin, end);
	}

	private static DateTime parseTimePosition(TimePositionType position) {
		DateTime begin;
		if (position.isSetIndeterminatePosition()) {
			begin = new DateTime("9999-12-31");
		} else {
			String val = position.getStringValue().trim();
			begin = new DateTime(val.isEmpty() ? "9999-12-31" : val);
		}
		return begin;
	}

	private static Interval parseByTimeInstant(TimeInstantPropertyType begin,
			TimeInstantPropertyType end) {
		if (begin.isSetTimeInstant() && end.isSetTimeInstant()) {
			return parseByTimePosition(begin.getTimeInstant().getTimePosition(), end.getTimeInstant().getTimePosition());
		}
		throw new UnsupportedOperationException("Currently, only TimeInstants are allowed inside gml:begin and gml:end");
	}
	

	
}
