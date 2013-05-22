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

import java.util.ArrayList;
import java.util.List;

import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.AbstractSurfacePatchType;
import net.opengis.gml.x32.BoundingShapeType;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.PolygonPatchType;
import net.opengis.gml.x32.PolygonType;

import org.n52.oxf.conversion.gml32.geometry.GeometryWithInterpolation;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import static org.n52.oxf.conversion.gml32.xmlbeans.jts.GMLGeometryFactory.createRing;
import static org.n52.oxf.conversion.gml32.xmlbeans.jts.GMLGeometryFactory.createCoordinatesFromPosition;
import static org.n52.oxf.conversion.gml32.xmlbeans.jts.GMLGeometryFactory.createCoordinatesFromCoordinates;

/**
 * Outsource of {@link Polygon}-related methods,
 * closely linked with {@link GMLGeometryFactory}.
 * 
 * @author matthes rieke
 *
 */
public class PolygonFactory {

	/**
	 * Creates a polygon with one exterior and 0..*
	 * interior holes.
	 * 
	 * @param exterior the exterior ring
	 * @param interiors the holes
	 * @param srs as defined in srsName
	 * @return a raw polygon
	 */
	public static Polygon createPolygon(AbstractRingPropertyType exterior,
			AbstractRingPropertyType[] interiors, String srs) {
		LinearRing exteriorRing = createRing(exterior.getAbstractRing(), srs);

		List<LinearRing> interiorRings = new ArrayList<LinearRing>();
		for (AbstractRingPropertyType interiorRing : interiors) {
			interiorRings.add(createRing(interiorRing.getAbstractRing(), srs));
		}
		LinearRing[] castedArray = new LinearRing[0];
		return exteriorRing.getFactory().createPolygon(exteriorRing, interiorRings.toArray(castedArray));
	}

	/**
	 * @param patch the gml polygon patch
	 * @param srs as defined in srsName
	 * @return a polygon with a defined interpolation method.
	 */
	public static GeometryWithInterpolation createPolygonPatch(AbstractSurfacePatchType abstractPatch, String srs) {
		if (!(abstractPatch instanceof PolygonPatchType)) {
			throw new UnsupportedOperationException("Currently, only PolygonPatch is supported.");
		}
		
		PolygonPatchType patch = (PolygonPatchType) abstractPatch;
		
		if (!patch.isSetExterior()) throw new IllegalStateException("No exterior found in the Polygon patch.");

		Polygon polygon = createPolygon(patch.getExterior(), patch.getInteriorArray(), srs);

		String interpol;
		if (patch.isSetInterpolation()) {
			interpol = patch.getInterpolation().toString();
		} else {
			interpol = GeometryWithInterpolation.LINEAR;
		}

		return new GeometryWithInterpolation(polygon, interpol);
	}
	
	public static Geometry createPolygon(BoundingShapeType boundedBy) {
		if (boundedBy.isSetEnvelope()) {
			return createPolygon(boundedBy.getEnvelope());
		}
		return null;
	}

	public static Geometry createPolygon(PolygonType polygon) {
		LinearRing ext = createRing(polygon.getExterior().getAbstractRing(), polygon.getSrsName());
		
		List<LinearRing> innerRings = new ArrayList<LinearRing>(polygon.getInteriorArray().length);
		for (AbstractRingPropertyType innerRing : polygon.getInteriorArray()) {
			innerRings.add(createRing(innerRing.getAbstractRing(), polygon.getSrsName()));
		}
		
		GeometryFactory gf = new GeometryFactory();
		return gf.createPolygon(ext, innerRings.toArray(new LinearRing[]{}));
	}
	
	public static Geometry createPolygon(EnvelopeType envelope) {
		String srs = envelope.getSrsName();
		if (envelope.isSetLowerCorner() && envelope.isSetUpperCorner()) {
			Coordinate lowerLeft = createCoordinatesFromPosition(envelope.getLowerCorner(), srs);
			Coordinate upperRight = createCoordinatesFromPosition(envelope.getUpperCorner(), srs);
			Coordinate upperLeft = new Coordinate(lowerLeft.x, upperRight.y);
			Coordinate lowerRight = new Coordinate(upperRight.x, lowerLeft.y);
			GeometryFactory gf = new GeometryFactory();
			LinearRing lr = gf.createLinearRing(new Coordinate[] {
					lowerLeft, upperLeft, upperRight, lowerRight, lowerLeft
			});
			return gf.createPolygon(lr, null);
		}
		
		if (envelope.isSetCoordinates()) {
			Coordinate[] coords = createCoordinatesFromCoordinates(envelope.getCoordinates(),
					envelope.getSrsName());
			GeometryFactory gf = new GeometryFactory();
			LinearRing lr = gf.createLinearRing(coords);
			return gf.createPolygon(lr, null);
		}
		
		if (envelope.getPosArray().length > 0) {

		}
		throw new UnsupportedOperationException("Currently only gml:Coordinates and gml:posList are supported.");
	}
	
}
