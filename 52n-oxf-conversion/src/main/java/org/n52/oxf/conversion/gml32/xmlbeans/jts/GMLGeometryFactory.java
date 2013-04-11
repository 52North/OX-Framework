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
import java.util.Collection;
import java.util.List;

import net.opengis.gml.x32.AbstractCurveSegmentType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.AbstractRingType;
import net.opengis.gml.x32.AbstractSurfacePatchType;
import net.opengis.gml.x32.BoundingShapeType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.CurveInterpolationType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.CurveInterpolationType.Enum;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.GeodesicStringType;
import net.opengis.gml.x32.LineStringSegmentType;
import net.opengis.gml.x32.PolygonPatchType;
import net.opengis.gml.x32.RectangleType;
import net.opengis.gml.x32.SurfacePatchArrayPropertyType;

import org.n52.oxf.conversion.gml32.geometry.GeometryWithInterpolation;
import org.n52.oxf.conversion.gml32.srs.AxisOrder;
import org.n52.oxf.conversion.gml32.srs.SRSUtils;
import org.n52.oxf.conversion.gml32.util.GeodesicApproximationTools;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Class providing static methods for parsing AIXM
 * and GML geometry abstractions.
 *
 */
public class GMLGeometryFactory {

	private static final double interpolatedSegmentLength = 1.0;

	/**
	 * Checks if a collection of geometries can be interpolated. This depends
	 * on the {@link GeometryFactoryConfiguration} settings and the
	 * geometries itself. Every geometry is treated separately
	 * and interpolation is applied when prerequisites are met.
	 * 
	 * @param geometries the list of geometries with an abstract interpolation
	 * method defined.
	 */
	public static void checkAndApplyInterpolation(
			Collection<GeometryWithInterpolation> geometries) {
		for (GeometryWithInterpolation geom : geometries) {
			checkAndApplyInterpolation(geom);
		}
	}

	/**
	 * @see GMLGeometryFactory#checkAndApplyInterpolation(Collection)
	 */
	public static void checkAndApplyInterpolation(GeometryWithInterpolation geometry) {
		if (GeometryFactoryConfiguration.isUserInternalInterpolation()) {
			geometry.setGeometry(interpolateGeometry(geometry.getGeometry(), geometry.getInterpolation()));		
		}
	}

	/**
	 * @param curve the gml curve segment element
	 * @return a geometry representing abstract curve segment
	 * as LineString with an interpolation method defined.
	 */
	public static GeometryWithInterpolation createCurve(AbstractCurveSegmentType curve, String srs) {
		if (curve instanceof LineStringSegmentType) {
			return createLineString((LineStringSegmentType) curve, srs);
		}
		else if (curve instanceof GeodesicStringType) {
			return createGreatCirlce((GeodesicStringType) curve, srs);
		}
		else {
			throw new UnsupportedOperationException("Currently, only LineStringSegment and GeodesicString are supported.");
		}
	}

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

	/**
	 * @param patch the gml Rectangle
	 * @param srs as defined in srsName
	 * @return a rectangle with a defined interpolation method
	 */
	public static GeometryWithInterpolation createRectangle(RectangleType patch, String srs) {
		LinearRing geom = createRing(patch.getExterior().getAbstractRing(), srs);
		
		String interpol;
		if (patch.isSetInterpolation()) {
			interpol = patch.getInterpolation().toString();
		} else {
			interpol = GeometryWithInterpolation.LINEAR;
		}
		
		return new GeometryWithInterpolation(geom, interpol);
	}

	private static LineString concatenateLineStrings(List<LineString> resultList) {
		List<Coordinate> coords = new ArrayList<Coordinate>();

		for (LineString lineString : resultList) {
			if (!coords.isEmpty()) {
				coords.remove(coords.size()-1);
			}
			for (Coordinate coordinate : lineString.getCoordinates()) {
				coords.add(coordinate);
			}
		}

		Coordinate[] coordsArray = new Coordinate[coords.size()];
		return new GeometryFactory().createLineString(coords.toArray(coordsArray));
	}

	private static Coordinate createCoordinate(double first, double second,
			AxisOrder order) {
		return createCoordinate(first, second, Double.NaN, order);
	}

	private static Coordinate createCoordinate(double first, double second,
			double third, AxisOrder order) {
		return (order == AxisOrder.LongLat) ?
				new Coordinate(first, second, third) : new Coordinate(second, first, third);
	}

	private static Coordinate[] createCoordinatesFromCoordinates(
			CoordinatesType coordinates, String srs) {
		String coordinateSeparator;
		if (coordinates.isSetCs()) {
			coordinateSeparator = coordinates.getCs().trim();	
		} else {
			coordinateSeparator = " ";
		}

		String tokenSeparator;
		if (coordinates.isSetTs()) {
			tokenSeparator = coordinates.getTs().trim();
		} else {
			tokenSeparator = ",";
		}

		AxisOrder order = SRSUtils.resolveAxisOrder(srs);

		String[] coordList = coordinates.getStringValue().split(tokenSeparator);
		Coordinate[] resultCoordinates = new Coordinate[coordList.length];
		for (int i = 0; i < coordList.length; i++) {
			String[] coord = coordList[i].split(coordinateSeparator);
			double first = Double.parseDouble(coord[0]);
			double second = Double.parseDouble(coord[1]);
			resultCoordinates[i] = createCoordinate(first, second, order);
		}

		return resultCoordinates;
	}

	public static Coordinate[] createCoordinatesFromPosList(
			DirectPositionListType posList, String srs) {
		List<?> coordList = posList.getListValue();

		int dim;
		if (posList.isSetSrsDimension()) {
			dim = posList.getSrsDimension().intValue();
		} else {
			if (posList.isSetCount()) {
				dim = coordList.size() / posList.getCount().intValue();
			} else {
				dim = 2;
			}
		}
		return createCoordinatesFromList(coordList, dim, srs != null ? srs : posList.getSrsName());
	}

	public static GeometryWithInterpolation createGreatCirlce(GeodesicStringType segment, String srs) {
		LineString geom;
		if (segment.isSetPosList()) {
			if (srs == null) {
				srs = segment.getPosList().getSrsName();
			}
			geom = new GeometryFactory().createLineString(createCoordinatesFromPosList(segment.getPosList(), srs));
		}
		else {
			throw new UnsupportedOperationException("Currently, only PosList is supported for GeodesicString.");
		}

		Enum interpol;
		if (segment.isSetInterpolation()) {
			interpol = segment.getInterpolation();
		} else {
			interpol = CurveInterpolationType.GEODESIC;
		}

		return new GeometryWithInterpolation(geom, interpol.equals(CurveInterpolationType.GEODESIC) ? 
				GeometryWithInterpolation.GEODESIC : GeometryWithInterpolation.LINEAR);
	}

	public static GeometryWithInterpolation createLineString(LineStringSegmentType segment, String srs) {
		LineString geom;
		if (segment.isSetCoordinates()) {
			geom = new GeometryFactory().createLineString(createCoordinatesFromCoordinates(segment.getCoordinates(), srs));
		}
		else if (segment.isSetPosList()) {
			geom = new GeometryFactory().createLineString(createCoordinatesFromPosList(segment.getPosList(), srs));
		}
		else {
			throw new UnsupportedOperationException("Currently, only Coordinates and PosList are supported for LineStringSegment.");
		}

		return new GeometryWithInterpolation(geom, GeometryWithInterpolation.LINEAR);
	}

	public static LinearRing createRing(AbstractRingType abstractRing, String srs) {
		if (abstractRing instanceof LinearRingType) {
			LinearRingType linearRing = (LinearRingType) abstractRing;
			if (linearRing.isSetPosList()) {
				return new GeometryFactory().createLinearRing(
						createCoordinatesFromPosList(linearRing.getPosList(), srs));
			}
			else if (linearRing.isSetCoordinates()) {
				return new GeometryFactory().createLinearRing(
						createCoordinatesFromCoordinates(linearRing.getCoordinates(), srs));
			}

		}
		throw new UnsupportedOperationException("Only LinearRing supported currently.");
	}

	private static Geometry interpolateGeometry(Geometry geometry,
			String interpolation) {
		if (geometry instanceof LineString) {
			return interpolateLineString((LineString) geometry, interpolation);
		}
		return geometry;
	}

	private static LineString interpolateGreatCircle(LineString geom) {
		List<LineString> resultList = new ArrayList<LineString>();
		Coordinate last = null;
		for (Coordinate current : geom.getCoordinates()) {
			if (last != null) {
				resultList.add(GeodesicApproximationTools.approximateGreatCircle(last, current, interpolatedSegmentLength));
			}
			last = current;
		}

		if (resultList.size() == 1) {
			return resultList.get(0);
		} else {
			return concatenateLineStrings(resultList);
		}
	}

	private static LineString interpolateLineString(LineString geom,
			String linear) {
		if (geom.getLength() > interpolatedSegmentLength) {
			if (linear.equals(GeometryWithInterpolation.LINEAR)) {
				return interpolateRhumbLine(geom);
			} else {
				return interpolateGreatCircle(geom);
			}	
		}
		return geom;
	}

	private static LineString interpolateRhumbLine(LineString geom) {
		List<LineString> resultList = new ArrayList<LineString>();
		Coordinate last = null;
		for (Coordinate current : geom.getCoordinates()) {
			if (last != null) {
				resultList.add(GeodesicApproximationTools.approximateRhumbline(last, current, interpolatedSegmentLength));
			}
			last = current;
		}

		if (resultList.size() == 1) {
			return resultList.get(0);
		} else {
			return concatenateLineStrings(resultList);
		}
	}

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

	public static Geometry createAggregatedGeometry(
			List<GeometryWithInterpolation> geometryList) {
		for (GeometryWithInterpolation geom : geometryList) {
			return geom.getGeometry();
		}
		
		/*
		 * TODO implement actual aggregation
		 */
		
		return null;
	}

	public static Geometry createPolygon(BoundingShapeType boundedBy) {
		if (boundedBy.isSetEnvelope()) {
			return createPolygon(boundedBy.getEnvelope());
		}
		return null;
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

	private static Coordinate createCoordinatesFromPosition(
			DirectPositionType position, String srs) {
		int dim;
		if (position.isSetSrsDimension()) {
			dim = position.getSrsDimension().intValue();
		} else {
			dim = position.getListValue().size();
		}
		return createCoordinatesFromList(position.getListValue(),
				dim, srs != null ? srs : position.getSrsName())[0];
	}

	private static Coordinate[] createCoordinatesFromList(List<?> coordList,
			int dim, String srs) {
		AxisOrder order = SRSUtils.resolveAxisOrder(srs);
		Coordinate[] resultCoordinates = new Coordinate[coordList.size() / dim];
		
		int index = 0;
		while (index+dim-1 < coordList.size()) {
			
			if (dim == 2) {
				resultCoordinates[index/dim] = createCoordinate(Double.parseDouble(coordList.get(index).toString()),
						Double.parseDouble(coordList.get(index+1).toString()), order);
			} else if (dim == 3) {
				resultCoordinates[index/dim] = createCoordinate(Double.parseDouble(coordList.get(index).toString()),
						Double.parseDouble(coordList.get(index+1).toString()),
						Double.parseDouble(coordList.get(index+2).toString()), order);
			}
			index += dim;
		}
		
		return resultCoordinates;
	}

	public static List<GeometryWithInterpolation> createMultiPolygonPatch(
			SurfacePatchArrayPropertyType patches, String srs) {
		List<GeometryWithInterpolation> result = new ArrayList<GeometryWithInterpolation>();
		for (AbstractSurfacePatchType p : patches.getAbstractSurfacePatchArray()) {
			result.add(createPolygonPatch(p, srs));
		}
		return result;
	}


}
