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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.opengis.gml.x32.AbstractCurveSegmentType;
import net.opengis.gml.x32.AbstractRingType;
import net.opengis.gml.x32.AbstractSurfacePatchType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.CurveInterpolationType;
import net.opengis.gml.x32.DirectPositionType;
import net.opengis.gml.x32.EnvelopeDocument;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.gml.x32.GeodesicStringDocument;
import net.opengis.gml.x32.LineStringDocument;
import net.opengis.gml.x32.LineStringType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PointDocument;
import net.opengis.gml.x32.PointType;
import net.opengis.gml.x32.PolygonDocument;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.PosDocument;
import net.opengis.gml.x32.CurveInterpolationType.Enum;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.GeodesicStringType;
import net.opengis.gml.x32.LineStringSegmentType;
import net.opengis.gml.x32.RectangleType;
import net.opengis.gml.x32.SurfacePatchArrayPropertyType;

import org.apache.xmlbeans.XmlObject;
import org.n52.oxf.conversion.gml32.geometry.GeometryWithInterpolation;
import org.n52.oxf.conversion.gml32.srs.AxisOrder;
import org.n52.oxf.conversion.gml32.srs.SRSUtils;
import org.n52.oxf.conversion.gml32.util.GeodesicApproximationTools;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.io.ParseException;

import static org.n52.oxf.conversion.gml32.xmlbeans.jts.PolygonFactory.createPolygon;
import static org.n52.oxf.conversion.gml32.xmlbeans.jts.PolygonFactory.createPolygonPatch;
import static org.n52.oxf.conversion.gml32.xmlbeans.jts.PointFactory.createPoint;

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

	protected static Coordinate createCoordinate(double first, double second,
			AxisOrder order) {
		return createCoordinate(first, second, Double.NaN, order);
	}

	protected static Coordinate createCoordinate(double first, double second,
			double third, AxisOrder order) {
		return (order == AxisOrder.LongLat) ?
				new Coordinate(first, second, third) : new Coordinate(second, first, third);
	}

	protected static Coordinate[] createCoordinatesFromCoordinates(
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

	protected static Coordinate[] createCoordinatesFromList(List<?> coordList,
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

	protected static Coordinate createCoordinatesFromPosition(
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

	protected static Coordinate[] createCoordinatesFromPosList(
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

	public static Geometry createLineString(LineStringType ls) {
		GeometryFactory gf = new GeometryFactory();

		Coordinate[] coords = null;
		if (ls.isSetCoordinates()) {
			coords = createCoordinatesFromCoordinates(ls.getCoordinates(), ls.getSrsName());
		}
		else if (ls.isSetPosList()) {
			coords = createCoordinatesFromPosList(ls.getPosList(), ls.getSrsName());
		}

		if (coords != null) {
			return gf.createLineString(coords);
		}
		return null;
	}

	public static List<GeometryWithInterpolation> createMultiPolygonPatch(
			SurfacePatchArrayPropertyType patches, String srs) {
		List<GeometryWithInterpolation> result = new ArrayList<GeometryWithInterpolation>();
		for (AbstractSurfacePatchType p : patches.getAbstractSurfacePatchArray()) {
			result.add(createPolygonPatch(p, srs));
		}
		return result;
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

	/**
	 * Main method for parsing a geometry from an xml-fragment.
	 * This method delegates to the private concrete parsing methods.
	 *
	 * @param geomElement the geometry xml object
	 * @return a {@link Geometry} as a JTS representation.
	 * @throws ParseException if the geometry could not be parsed.
	 * @throws GMLParseException if something could not be parsed or is not supported.
	 */
	public static Geometry parseGeometry(XmlObject geomElement) throws ParseException {
		if (geomElement instanceof EnvelopeType) {
			return createPolygon((EnvelopeType) geomElement);
		}
		else if (geomElement instanceof PointType) {
			return createPoint((PointType) geomElement, null);
		}
		else if (geomElement instanceof DirectPositionType) {
			DirectPositionType dirPos = (DirectPositionType) geomElement;
			String srs;
			if (dirPos.isSetSrsName()) {
				srs = dirPos.getSrsName();
			} else {
				srs = null;
			}
			return createPoint(dirPos, srs);
		}
		else if (geomElement instanceof LineStringType) {
			return createLineString((LineStringType) geomElement);
		}
		else if (geomElement instanceof LinearRingType) {
			return createRing((LinearRingType) geomElement, null);
		}

		else if (geomElement instanceof GeodesicStringType) {
			return createGreatCirlce((GeodesicStringType) geomElement, null).getGeometry();
		}
		else if (geomElement instanceof PolygonType) {
			return createPolygon((PolygonType) geomElement);
		}
		else if (geomElement instanceof EnvelopeDocument) {
			return createPolygon(((EnvelopeDocument) geomElement).getEnvelope());
		}
		else if (geomElement instanceof PosDocument) {
			return createPoint(((PosDocument) geomElement).getPos(), null);
		}
		else if (geomElement instanceof GeodesicStringDocument) {
			return parseGeometry(((GeodesicStringDocument) geomElement).getGeodesicString());
		}
		else if (geomElement instanceof PolygonDocument) {
			return parseGeometry(((PolygonDocument) geomElement).getPolygon());
		}
		else if (geomElement instanceof PointDocument) {
			return parseGeometry(((PointDocument) geomElement).getPoint());
		}
		else if (geomElement instanceof LineStringDocument) {
			return parseGeometry(((LineStringDocument) geomElement).getLineString());
		}
		return null;
	}

}
