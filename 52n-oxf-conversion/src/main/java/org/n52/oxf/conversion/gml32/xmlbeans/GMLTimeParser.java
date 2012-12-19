/**
 * Copyright (C) 2012
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
