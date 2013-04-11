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
package org.n52.oxf.conversion.gml32.srs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SRSUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(SRSUtils.class);
	public static final String DEFAULT_SRS = "urn:ogc:def:crs:EPSG::4326";
	private static Map<String, AxisOrder> mapping;

	static {
		mapping = new HashMap<String, AxisOrder>();
		try {
			loadMappings();
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
	}
	
	public static AxisOrder resolveAxisOrder(String srsName) {
		return srsName != null ? mapping.get(srsName) : mapping.get(DEFAULT_SRS);
	}

	private static void loadMappings() throws IOException {
		InputStream is = SRSUtils.class.getResourceAsStream("axisOrder.mapping");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		while (br.ready()) {
			addMapping(br.readLine());
		}
		
		br.close();
	}

	private static void addMapping(String mappingLine) {
		String[] kvp = mappingLine.split("=");
		if (kvp != null && kvp.length == 2) {
			mapping.put(kvp[0].trim(), parseAxisOrder(kvp[1].trim()));
		}
	}

	private static AxisOrder parseAxisOrder(String axisString) {
		if (axisString.equals("AxisOrder.LongLat")) {
			return AxisOrder.LongLat;
		} else {
			return AxisOrder.LatLong;
		}
	}
}
