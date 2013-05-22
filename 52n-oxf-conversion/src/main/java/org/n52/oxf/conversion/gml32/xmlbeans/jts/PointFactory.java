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
