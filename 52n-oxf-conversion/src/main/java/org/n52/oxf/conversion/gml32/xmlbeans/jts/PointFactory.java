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
package org.n52.oxf.conversion.gml32.xmlbeans.jts;

import java.util.List;

import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.PointType;

import org.n52.oxf.conversion.gml32.srs.SRSUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import static org.n52.oxf.conversion.gml32.xmlbeans.jts.GMLGeometryFactory.createCoordinate;

/**
 * Outsource of {@link Point}-related methods, closely
 * linked to {@link GMLGeometryFactory}.
 *
 * @author matthes rieke
 *
 */
public class PointFactory {

	public static Point createPoint(DirectPositionType pos, String srs) {
		List<?> list = pos.getListValue();
		int dim;
		if (pos.isSetSrsDimension()) {
			dim = pos.getSrsDimension().intValue();
		} else {
			dim = list.size();
		}

		if (dim == 2) {
			return new GeometryFactory().createPoint(createCoordinate(Double.parseDouble(list.get(0).toString()),
					Double.parseDouble(list.get(1).toString()),
					SRSUtils.resolveAxisOrder(srs != null ? srs : pos.getSrsName())));
		}
		else if (dim == 3) {
			return new GeometryFactory().createPoint(createCoordinate(Double.parseDouble(list.get(0).toString()),
					Double.parseDouble(list.get(1).toString()),
					Double.parseDouble(list.get(2).toString()),
					SRSUtils.resolveAxisOrder(srs != null ? srs : pos.getSrsName())));
		}

		throw new IllegalStateException("Point must have dimension 2 or 3.");
	}

	protected static Geometry createPoint(PointType point, String srs) {
		if (point.isSetPos()) {
			return createPoint(point.getPos(), srs);
		}
		return null;
	}

}
